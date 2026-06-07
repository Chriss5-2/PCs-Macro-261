import pandas as pd
import numpy as np

print("Cargando dataset...")
# Usamos latin1 por si hay tildes en los nombres de los distritos
df = pd.read_csv('Monitoreo de los contaminantes del aire en Lima Metropolitana - [Servicio Nacional de Meteorología e Hidrología del Perú - SENAMHI]_1.csv', encoding='latin1')

print("Limpiando datos...")
# 1. Eliminar filas donde los 3 contaminantes sean nulos (no aportan valor estadístico)
df.dropna(subset=['PM10', 'PM2_5', 'NO2'], how='all', inplace=True)

# 2. Rellenar los nulos restantes con -99.9 para identificarlos fácilmente en Spark/SQL sin romper los tipos numéricos
df['PM10'] = df['PM10'].fillna(-99.9)
df['PM2_5'] = df['PM2_5'].fillna(-99.9)
df['NO2'] = df['NO2'].fillna(-99.9)

# 3. Crear Tabla Dimensión: ESTACIONES
estaciones = df[['ESTACION', 'DEPARTAMENTO', 'PROVINCIA', 'DISTRITO', 'UBIGEO', 'LONGITUD', 'LATITUD', 'ALTITUD']].drop_duplicates().reset_index(drop=True)
estaciones['ID_ESTACION'] = estaciones.index + 1

# 4. Crear Tabla Hechos: MEDICIONES (haciendo merge para traer el ID_ESTACION)
mediciones = df.merge(estaciones, on=['ESTACION', 'DEPARTAMENTO', 'PROVINCIA', 'DISTRITO', 'UBIGEO', 'LONGITUD', 'LATITUD', 'ALTITUD'])
mediciones = mediciones[['ID', 'ID_ESTACION', 'FECHA', 'HORA', 'PM10', 'PM2_5', 'NO2', 'FECHA_CORTE']]

print("Exportando CSVs limpios...")
estaciones.to_csv('estaciones_clean.csv', index=False)
mediciones.to_csv('mediciones_clean.csv', index=False)
print("¡Listo! Archivos generados.")