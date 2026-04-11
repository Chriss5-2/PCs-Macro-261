import polars as pl
import os

carpeta = "CSV's"

columnas_objetivo = [
    "ANIO_REGISTRO",
    "MES_REGISTRO",
    "TIPO_ACTA",
    "DE_GENERO",
    "DEPART_CIUDAD_ESTADO_DOM_SOL",
    "PROVINCIA_DOM_SOL",
    "CANT_COPIAS_EMITIDAS"
]

def detectar_separador(ruta: str) -> str:
    with open(ruta, "r", encoding="latin1") as f:
        primera_linea = f.readline()

    semicolons = primera_linea.count(";")
    commas = primera_linea.count(",")
    return ";" if semicolons >= commas else ","


def normalizar_columna(nombre: str) -> str:
    return nombre.replace("\ufeff", "").replace("ï»¿", "").strip().strip('"')


def limpiar_y_unificar(df: pl.DataFrame) -> pl.DataFrame:
    df.columns = [normalizar_columna(col) for col in df.columns]

    columnas_validas = []
    for col in df.columns:
        col_lower = col.lower()
        if col in columnas_objetivo:
            columnas_validas.append(col)
            continue

        # Elimina columnas índice típicas: vacías, #, Unnamed, H1, etc.
        if (
            col == ""
            or col == "#"
            or col_lower.startswith("unnamed")
            or col_lower in {"h1", "index", "idx", "column_1"}
        ):
            continue

        # Cualquier otra columna no objetivo se descarta para mantener esquema único.
        if col not in columnas_objetivo:
            continue

    if columnas_validas:
        df = df.select(columnas_validas)
    else:
        df = df.clear()

    faltantes = [col for col in columnas_objetivo if col not in df.columns]
    if faltantes:
        df = df.with_columns([pl.lit(None).alias(col) for col in faltantes])

    return df.select(columnas_objetivo)


dfs = []
errores = []

for archivo in sorted(os.listdir(carpeta)):
    if not archivo.endswith(".csv"):
        continue

    ruta = os.path.join(carpeta, archivo)
    try:
        separador = detectar_separador(ruta)
        df = pl.read_csv(
            ruta,
            encoding="latin1",
            separator=separador,
            ignore_errors=True,
        )
        dfs.append(limpiar_y_unificar(df))
    except Exception as e:
        errores.append((archivo, str(e)))

if not dfs:
    raise RuntimeError("No se pudo leer ningun CSV valido.")

df_final = pl.concat(dfs, how="vertical_relaxed")
df_final.write_csv("Registros.csv")

if errores:
    print("Archivos con error de lectura:")
    for archivo, error in errores:
        print(f"- {archivo}: {error}")

print(f"Listo. Archivos procesados: {len(dfs)}")