# Consultas Power BI
Para realizar las consultas, se usarán los datos de [csv_nuevo.csv](../../CSV_final/csv_nuevo.csv), que es generado por [PC2-Macro.ipynb](../Python/PC2-Macro.ipynb)

## Consulta 1
2 gráficos de columnas apiladas vertical y horizontal
### Gráfico 1: ¿Cuál es la cantidad de aeródromos por Departamento, segmentados por su Estado de Operatividad?
Eje X: DEPARTAMENTO
Eje Y: ID_AERODROMO
Leyenda: ESTADO

Comparamos el volumen total entre las regiones.
**Página: C_1_1** - Columnas apiladas vertical

### Gráfico 2: ¿Cómo se distribuye la infraestructura según el Administrador y su Situación Legal?
Eje Y: ADMINISTRADOR
Eje X: ID_AERODROMO
Leyenda: SITUACION_LEGAL

Verificamos que CORPAC domina la infraestructura pública nacional
**Página: C_1_2** - Columnas apiladas horizontal

## Consulta 2
2 gráficos de líneas
### Gráfico 1: ¿Cómo ha crecido el número de infraestructuras registradas en la Costa, Sierra y Selva entre 2022 y 2024?
Eje X: ANIO_CORTE
Eje Y: ID_AERODROMO
Leyenda: MACROREGION

Confirmar la centralización en la Costa
**Página: C_2_1** - Grafico de Lineas

### Gráfico 2: ¿Cuál es la tendencia de crecimiento de los Aeródromos frente a los Aeropuertos y Helipuertos?
Eje X: ANIO_CORTE
Eje Y: ID_AERODROMO
Leyenda: TIPO_AERODROMO

Verificación de que tipo de aerodromo es el que ha tenido mayor crecimiento a lo largo de los años
**Página: C_2_2** - Gráfico de Líneas

## Consulta 3
Gráfico de dispersión
### ¿Cómo se distribuyen espacialmente las sedes aeroportuarias según su ubicación exacta y que tipo de infraestructura predomina en cada zona?

#### Caso 1
Valores: Nombre
Eje X: LONGITUD
Eje Y: LATITUD
Leyenda: TIPO_AERODROMO
Tamaño: ID_AERODROMO

#### Caso 2
Valores: Nombre
Eje X: LONGITUD
Eje Y: LATITUD
Leyenda: MACROREGION
Tamaño: ID_AERODROMO

#### Caso 3
Valores: ID_AERODROMO
Eje X: LONGITUD
Eje Y: LATITUD
Leyenda: TIPO_AERODROMO
Tamaño: ID_AERODROMO

#### Caso 4
Valores: ID_AERODROMO
Eje X: LONGITUD
Eje Y: LATITUD
Leyenda: MACROREGION
Tamaño: ID_AERODROMO

**Página: C_3**

## Consulta 4
Gráfico de mapas
### Distribución geográfica real de la infraestructura aérea nacional

#### Caso 1:
Leyenda: DEPARTAMENTO
Latitud: LATITUD
Longitud: LONGITUD
Tamaño de burbuja: ID_AERODROMO

#### Caso 2:
Leyenda: MACROREGION
Latitud: LATITUD
Longitud: LONGITUD
Tamaño de burbuja: ID_AERODROMO

Esto ayuda a verificar que hay aeródromos, que pertenecen a la SELVA, pero al tener la leyenda en Macroregión, sale que pertenecen a la COSTA, por lo cual, la primera explicación sería que el algoritmo de clasificación tiene errores, pero al revisar el **Caso 1**, notamos que los departamentos que aparecen, están dentro de la clasificacion, por lo que si están siendo tomados, y otra explicación sería que hay aeródromos que son de la selva, pero fueron registradas como Costa, ya que las latitudes y longitudes de estos aeródromos, ubican la zona de operación, en la Selva y no en su zona correspondiente.

**Página: C_4**

## Consulta 5
Gráfico circular
### ¿Cuál es la participación porcentual de cada Macroregión en el total de la infraestructura nacional?
Leyenda: MACROREGION
Valores: ID_AERODROMO

Verificación de que MACROREGIÓN concentra la infraestructura aérea del país
**Página: C_5**