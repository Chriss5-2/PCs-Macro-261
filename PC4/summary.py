import pandas as pd

df = pd.read_csv("IGP_IonosferaRadarDigisonda_2013-2026_Dataset.csv")

print("========= Columnas del dataset =========")
print(df.columns)

print("\n========= Información general del dataset =========")
print(df.info())

print("\n========= Estadísticas descriptivas del dataset =========")
print(df.describe())