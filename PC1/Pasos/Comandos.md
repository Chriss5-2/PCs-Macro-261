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
# Acta: TIPO_ACTA -[2]
# Provincia: PROVINCIA_DOM_SOL - [5]
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_1/cons_1.2
```

3. ¿Cuál es la distribución de trámites por Género y Mes entre los años 2023 y 2026? (cons_1.3)
```bash
# Año: ANIO_REGISTRO - [0]
# Mes: MES_REGISTRO - [1]
# Género: DE_GENERO - [3]
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_1/cons_1.3
```

4. ¿Qué provincia tuvo más trámites por cada mes en cada año? (cons_1.4)
```bash
# Provincias: PROVINCIA_DOM_SOL - [5]
# Acta: TIPO_ACTA - [2]
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_1/cons_1.4
```

5. Variación de la 4, pero ahora será la provincia que tuvo el menor número de trámites por mes (esto lo hice ya que en la 4, todo sale LIMA y para tener más variación, será tomando el menor) (cons_1.5)
```bash
# Provincias: PROVINCIA_DOM_SOL - [5]
# Acta: TIPO_ACTA - [2]
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_1/cons_1.5
```

### 2. Encontrar el promedio, la media y la desviación de un campo (Columna) a selección (cons_2)
- El promedio, media y desviación estándar, del total de copias certificadas por mes cada año
```bash
# Año: ANIO_REGISTRO - [0]
# Mes: MES_REGISTRO - [1]
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_2
```
- Variación_1: El promedio, media y desviación estándar del total de copias certificadas por año
```bash
# Año: ANIO_REGISTRO - [0]
# Mes: MES_REGISTRO - [1]
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_2/var_1
```

**Resultado**
```md
2021	Prom: 69.10 | Mediana: 4.00 | DesvStd: 699.55
2022	Prom: 56.46 | Mediana: 4.00 | DesvStd: 631.65
2023	Prom: 48.20 | Mediana: 3.00 | DesvStd: 546.92
2024	Prom: 52.23 | Mediana: 3.00 | DesvStd: 571.26
2025	Prom: 55.54 | Mediana: 3.00 | DesvStd: 617.17
2026	Prom: 67.36 | Mediana: 3.00 | DesvStd: 803.01
```
```bash
Como notamos que la desviación estándar es muy alta a comparación del promedio, entonces ahora lo que haremos será aplicar un filtro de outliers
```
- Variación_2: El promedio, media y desviación estándar del total de copias certificadas por año pero aplicando un filtro de outliers en el rango de 0 a 100
```bash
# Año: ANIO_REGISTRO - [0]
# Mes: MES_REGISTRO - [1]
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_2/var_2
```

### 3. Hacer búsqueda de un subtexto general que pueda buscar en varios campos de textos y me dé como resultado(s) uno o varios registros completos (cons_3)
Se mostrarán todos los resultados donde exista la palabra "LIMA", es un ejemplo simple pero como no hay tanta coincidencia entre columnas, es dificil encontrar un patrón que se repita en todas las columnas, pero con este ejemplo, se mostrará todas las columnas donde algún valor de alguna columna contiene la palabra "LIMA", "lima", "Lima" o cualquier otra variación de la palabra, y esto se debe a que usamos "toLowerCase()" para evitar la restricción por mayúsculas y minúsculas para tener resultados generales
```bash
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_3
```

### 4. Hacer una búsqueda por intervalo de fechas y dar como reporte uno o más registros completos (cons_4)
Para resolver el enunciado, de manera general, las fechas que están en formato **dd/mm/aaaa** deben convertirse a la forma de número **aaaammdd** para así evitar condicionales anidadas donde una filtre el año, otra el mes y otra el dia.
Pero como en este dataset no tenemos el día de la operación sino solo el mes y año, filtraremos el formato de fecha de la forma **aaaamm** y de esta manera, podremos establecer un intervalo de fechas y dar un reporte de registros completos que están en ese intervalo
```bash
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_4
```

### 5. El mayor y el menor valor de un campo o un conjunto de campos (Columnas) agrupados. (cons_5)
Para resolver esto, lo que haremos será mostrar el mínimo y máximo de cada provincia y departamento.
```bash
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_5
```

### 6. Hacer 2 consultas con campos decimales que implique al menos 2 MapReduce. Cada consulta por separado debe tener los 2 Mapreduce enlazados (cons_6)
Para resolver esta consulta, hemos hallado la diferencia decimal que presenta cada provincia con respecto al promedio de su respectivo año, para realizar esto, usamos 2 Mapreduce enlazados, uno que será el job1 que se encargará de hallar el promedio por año y lo guardará en la carpeta **/output_pc1/cons6_mid** usando [SalesMapper.java](../Resultados/Cons_6/src/SalesMapper.java) y [SalesReducer.java](../Resultados/Cons_6/src/SalesCountryReducer.java), luego se usará el job2 que será el encargado de tomar estos promedio hallados y el csv original para hallar la diferencia decimal entre provincias en cada año, para ello se usa [SalesMapper2.java](../Resultados/Cons_6/src/SalesMapper2.java) y [SalesReducer2.java](../Resultados/Cons_6/src/SalesCountryReducer2.java), donde se puede ver que normalmente se hacen estos procesos:
- Ejecución del job1
- Modificación de scripts para nuevo procedimiento
- Ejecución del job2
Todo de manera manual ejecutando por separado **hadoop jar..** pero en esta ocasión, el script [SalesCountryDriver](../Resultados/Cons_6/src/SalesCountryDriver.java) genera job1 y job2, donde el output de job1 será una carpeta temporal y será uno de los input de job2, para que lo lea y así generamos que de una sola ejecución, corran los dos jobs
```bash
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_6
```

### 7. Hacer 1 consulta con campos decimales o texto que implique el uso de un modelo de clasificación (investigar) (cons_7)
El modelo a usar a sido un **Modelo Clasificados Basado en Umbrales Dinámicos con Media Móvil Anual** o basicamente un **Dynamic Threshold Classifier** donde tendremos cuatro etiquetas:
- ESTABLE: Natalidad < prom y Mortalidad < prom
- MES_CRITICO: Natalidad < prom y Mortalidad > prom
- ALTA_NATALIDAD: Natalidad > prom
- ALTA_MORTALIDAD: Mortalidad > prom
Son categorias básicas dado que los datos son muy variados y deberían de haber más reglas para clasificar, pero como una consulta básica de modelo de clasificación podría servir para ver su funcionamiento
```bash
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_7
```

Para esto aplicaremos nuevamente 2 jobs, el job1 se encargará de filtrar por año y mes, para sumar la cantidad de actas que eran de tipo nacimiento y defunción en cada año y dividirlo entre 12 a ambos, así tendremos el promedio de actas de nacimiento y defunción por cada año, luego el job2 se encargará de usar estos promedios por año, para ver la cantidad de actas que hay de uno de estos dos tipos por mes, y clasificarlo según el año que le corresponde, así tendremos una clasificación simple tomando en cuenta el número de actas de nacimiento y número de actas de defunción de cada mes en cada año y clasificarlo para generar un patrón entre estos meses con relación a los años.


### 8. Hacer 1 consulta con campos decimales que implique el uso de un modelo de Regresión. (investigar) (cons_8)
Para realizar el modelo de regresión, me base en el modelo de clasificación anterior, para generalizar la formular de regresión lineal para que esté de la forma y = mx+b y de esta manera tenemos un modelo de regresión lineal para predecir los meses a futuro, la precisión será baja porque para el ejemplo solo tomamos algunos campos en el proceso, más que nada los procesos de numeros de actas de nacimiento y defunción.
```bash
hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_8
```