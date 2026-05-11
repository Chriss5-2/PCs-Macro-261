import pandas as pd

df = pd.read_csv('ITS_2025_limpio.csv', encoding='latin1', sep=';')
print("========= Primeras 10 filas del DataFrame =========")
print(df.head(10))
print("========= Nombres de las columnas =========")
print(df.columns)
print("========= Información del DataFrame =========")
print(df.info())
print("========= Estadísticas descriptivas =========")
print(df.describe())
print("========= Tipos de datos =========")
print(df.dtypes)