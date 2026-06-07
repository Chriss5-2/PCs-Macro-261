-- =============================================================================
-- SCRIPT DE CREACIÓN DE ARQUITECTURA RELACIONAL
-- =============================================================================

DROP TABLE IF EXISTS mediciones;
DROP TABLE IF EXISTS estaciones;

CREATE TABLE estaciones (
    estacion VARCHAR(100),
    departamento VARCHAR(50),
    provincia VARCHAR(50),
    distrito VARCHAR(50),
    ubigeo FLOAT,
    longitud FLOAT,
    latitud FLOAT,
    altitud FLOAT,
    id_estacion INT PRIMARY KEY
);

CREATE TABLE mediciones (
    id BIGINT PRIMARY KEY,
    id_estacion INT REFERENCES estaciones(id_estacion),
    fecha INT,
    hora INT,
    pm10 FLOAT,
    pm2_5 FLOAT,
    no2 FLOAT,
    fecha_corte INT
);

-- =============================================================================
-- PROCESO ETL VIA COMANDO COPY MASIVO (EJECUTADO EN ENTORNO WSL) 
-- =============================================================================

-- IMPORTANTE: Reemplazar las rutas por la ubicación exacta de tus archivos dentro de tu WSL.
\COPY estaciones FROM '~/pc3_datasets/estaciones_clean.csv' DELIMITER ',' CSV HEADER
\COPY mediciones FROM '~/pc3_datasets/mediciones_clean.csv' DELIMITER ',' CSV HEADER
