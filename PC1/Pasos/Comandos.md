## Crear carpeta donde irá el CSV
```bash
hadoop fs -mkdir /input_pc1
```
## Crear carpeta donde irán los resultados de cada consulta
```bash
hadoop fs -mkdir /output_pc1
```

### Borrar carpeta
```bash
hadoop fs -rm -r /output_pc1/
```

### Salir de modo seguro
```bash
hadoop dfsadmin -safemode leave
```

### Mover el archivo [Registros.csv](../CSV_final/Registros.csv) al disco C:\
## Mover el csv a la carpeta input_pc1
```bash
hadoop fs -put C:\Registros.csv /input_pc1/
```


#### Nombre de carpeta general (output_pc1)
```bash
# Ya se creó antes
# Esta carpeta contendrá las subcarpetas por las consultas realizadas
hadoop fs -mkdir /output_pc1
```
Cada output de las consultas, se guardará en una carpeta con nombre distinto dentro de la carpeta output_pc1 para identificarlos
- En la primera consulta, se divide en 4 enunciados, así que en la carpeta de la parte 1, habrán 4 subcarpetas que identifican cada enunciado (cons_1.1, cons_1.2, cons_1.3, cons_1.4)
# Columnas dataset 
- "ANIO_REGISTRO"
- "MES_REGISTRO"
- "TIPO_ACTA"
- "DE_GENERO"
- "DEPART_CIUDAD_ESTADO_DOM_SOL"
- "PROVINCIA_DOM_SOL"
- "CANT_COPIAS_EMITIDAS"

# Consultas a realizar
### 1. Hacer 4 consultas Marp reduce que usen 2 campos (Columnas) o más cada una (cons_1)
```bash
hadoop fs -mkdir /output_pc1/cons_1
```
1. ¿Cuál es el total de copias emitidas por Departamento/País, Provincia y Género? (cons_1.1)
```bash
# Departamento/País: DEPART_CIUDAD_ESTADO_DOM_SOL - [4]
# Provincia: PROVINCIA_DOM_SOL - [5]
# Género: DE_GENERO - [3]
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_1/cons_1.1
```

2. ¿Cuántas actas de nacimiento se emitieron por Año y Provincia? (cons_1.2)
```bash
# Año: ANIO_REGISTRO - [0]
# Provincia: PROVINCIA_DOM_SOL - [5]
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_1/cons_1.2
```
3. ¿Cuál es la distribución de trámites por Género y Mes en un año específico? (cons_1.3)
4. Ranking de provincias con mayor emisión de copias según el Tipo de Acta (cons_1.4)

### 2. Encontrar el promedio, la media y la desviación de un campo (Columna) a selección (cons_2)

### 3. Hacer búsqueda de un subtexto general que pueda buscar en varios campos de textos y me dé como resultado(s) uno o varios registros completos (cons_3)

### 4. Hacer una búsqueda por intervalo de fechas y dar como reporte uno o más registros completos (cons_4)

### 5. El mayor y el menor valor de un campo o un conjunto de campos (Columnas) agrupados. (cons_5)

### 6. Hacer 2 consultas con campos decimales que implique al menos 2 MapReduce. Cada consulta por separado debe tener los 2 Mapreduce enlazados (cons_6)

### 7. Hacer 1 consulta con campos decimales o texto que implique el uso de un modelo de clasificación (investigar) (cons_7)

### 8. Hacer 1 consulta con campos decimales que implique el uso de un modelo de Regresión. (investigar) (cons_8)