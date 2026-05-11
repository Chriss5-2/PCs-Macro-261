# Examen Parcial - Análisis de Macrodatos - 2026_1
## Hadoop Cluster
Se crearon VMs de tipo UbuntuServer de los cuales 1 es el **hadoop-master** y 2 son **worker1** y **worker2** teniendo así 3 nodos para la ejecución en Hadoop

### IPs
```bash
master : 192.168.56.10
worker1: 192.168.56.11
worker2: 192.168.56.12
```
### Hostname
```bash
master : master
worker1: worker1
worker2: worker2
```

### Usuario
```bash
master:  hadoop
worker1: hadoop
worker2: hadoop
```

### ISO
La iso usada para cada VMS fue la de Ubuntu server
- ubuntu-26.04-live-server-amd64.iso

### VM
La VM usada para levantar el master y los workers fue VirtualBox
- VirtualBox-7.2.8-173730-Win.exe

## Imagenes
Las imágenes presentadas, presentan una tendencia de que al iniciar un job, el worker2 tiene un consumo de casi el 100% del CPU, cuando empieza el mappeo en el job1, el worker1 es el que aumenta su consumo a un 100% aprox y el worker2 se mantiene en 20, y cuando está en el proceso de reduce, ambos workers están en 20 o 30% de uso, y esto se repite en cada job, con esto podemos ver la tendencia de repartición de trabajo para más de un nodo o workers en este caso.
## MultiNode vs SingleNode
En cada carpeta de consulta, hay una carpeta llamada **SingleNode** ahí están las capturas del procesamiento de la misma consulta, pero realizadas con un solo nodo, ya que el base es el MultiNode con el master, worker1, worker2 en distintos VMs, lo que se puede notar es que en **SingleNode** el tiempo de ejecución es más corto, y esto se debe a la presencia de un cuello de botella existente en **MultiNode**, ya que para dataset relativamente pequeños, el **MultiNode** toma más tiempo en distribuir la carga entre los workers y el master, y esto genera que demore más y por lo tanto, para este dataset, usar **SingleNode** es más conveniente ya que su tiempo de ejecución total por cada consulta en la mayoria de casos es casi la mitad del total.
**Tener en cuenta que los outputs son los mismos, los corroboré y tanto para la sección 1 hasta la 3, el resultado en MultiNode y SingleNode es igual**
## Dataset usado
El dataset se obtuvo de la siguiente página [ITS-Tacna](https://www.datosabiertos.gob.pe/dataset/reporte-de-infecciones-de-transmisi%C3%B3n-sexual-its-gobierno-regional-de-tacna) y se guardó con el nombre [ITS_2025.csv](./ITS_2025.csv) pero se creó un nuevo csv llamado [ITS_2025_limpio.csv](./ITS_2025_limpio.csv) ya que este nuevo está formateado en UTF-8 además de que para el nombre de la primera columna lo cambiamos por el nombre de "Anio" para evitar errores de formato.

### Descripción del dataset
```bash
# python summary.py
========= Primeras 10 filas del DataFrame =========
   Anio  RENIPRESS                               EESS  Ubigeo_Declarado_Paciente                  Distrito_RH_Paciente  ... id_genero grupo_edad        cie                                         desc_grupo TOTAL
0  2025       2864  HOSPITAL HIPOLITO UNANUE DE TACNA                   220101.0                                 TACNA  ...         M    29d-11m  A50 - A64  INFECCIONES C/MODO DE TRANSMISION PREDOMINANTE...     1
1  2025       6724                            VIÃANI                   100106.0                                 SALAS  ...         F     30-59a  A50 - A64  INFECCIONES C/MODO DE TRANSMISION PREDOMINANTE...     1
2  2025       2864  HOSPITAL HIPOLITO UNANUE DE TACNA                   220101.0                                 TACNA  ...         M    29d-11m  A50 - A64  INFECCIONES C/MODO DE TRANSMISION PREDOMINANTE...     1
3  2025       2864  HOSPITAL HIPOLITO UNANUE DE TACNA                   220109.0                              POCOLLAY  ...         F    29d-11m  A50 - A64  INFECCIONES C/MODO DE TRANSMISION PREDOMINANTE...     1
4  2025       2914                             INCLAN                   220101.0                                 TACNA  ...         F     30-59a  A50 - A64  INFECCIONES C/MODO DE TRANSMISION PREDOMINANTE...     1
5  2025       2864  HOSPITAL HIPOLITO UNANUE DE TACNA                   220113.0  CORONEL GREGORIO ALBARRACIN LANCHIPA  ...         M      0-28d  A50 - A64  INFECCIONES C/MODO DE TRANSMISION PREDOMINANTE...     1
6  2025       6724                            VIÃANI                   220113.0  CORONEL GREGORIO ALBARRACIN LANCHIPA  ...         M       60a+  A50 - A64  INFECCIONES C/MODO DE TRANSMISION PREDOMINANTE...     1
7  2025       2888                      SAN FRANCISCO                   220113.0  CORONEL GREGORIO ALBARRACIN LANCHIPA  ...         M     12-17a  A50 - A64  INFECCIONES C/MODO DE TRANSMISION PREDOMINANTE...     1
8  2025       2890                       LAS BEGONIAS                   220113.0  CORONEL GREGORIO ALBARRACIN LANCHIPA  ...         M     30-59a  A50 - A64  INFECCIONES C/MODO DE TRANSMISION PREDOMINANTE...     1
9  2025       2913                            LOCUMBA                   220301.0                               LOCUMBA  ...         M     30-59a  A50 - A64  INFECCIONES C/MODO DE TRANSMISION PREDOMINANTE...     2

[10 rows x 13 columns]
========= Nombres de las columnas =========
Index(['Anio', 'RENIPRESS', 'EESS', 'Ubigeo_Declarado_Paciente',
       'Distrito_RH_Paciente', 'Codigo_Item', 'Codigo_Item_F',
       'Descripcion_Item', 'id_genero', 'grupo_edad', 'cie', 'desc_grupo',
       'TOTAL'],
      dtype='str')
========= Información del DataFrame =========
<class 'pandas.DataFrame'>
RangeIndex: 1197 entries, 0 to 1196
Data columns (total 13 columns):
 #   Column                     Non-Null Count  Dtype  
---  ------                     --------------  -----  
 0   Anio                       1197 non-null   int64  
 1   RENIPRESS                  1197 non-null   int64  
 2   EESS                       1197 non-null   str    
 3   Ubigeo_Declarado_Paciente  1195 non-null   float64
 4   Distrito_RH_Paciente       1195 non-null   str    
 5   Codigo_Item                1197 non-null   str    
 6   Codigo_Item_F              1197 non-null   str    
 7   Descripcion_Item           1197 non-null   str    
 8   id_genero                  1197 non-null   str    
 9   grupo_edad                 1197 non-null   str    
 10  cie                        1197 non-null   str    
 11  desc_grupo                 1197 non-null   str    
 12  TOTAL                      1197 non-null   int64  
dtypes: float64(1), int64(3), str(9)
memory usage: 121.7 KB
None
========= Estadísticas descriptivas =========
         Anio     RENIPRESS  Ubigeo_Declarado_Paciente        TOTAL
count  1197.0   1197.000000                1195.000000  1197.000000
mean   2025.0   4263.037594              192674.384937     3.058480
std       0.0   5946.081840               52567.704772     9.134535
min    2025.0   2864.000000               10205.000000     1.000000
25%    2025.0   2886.000000              200102.000000     1.000000
50%    2025.0   2899.000000              220101.000000     1.000000
75%    2025.0   2919.000000              220113.000000     2.000000
max    2025.0  34527.000000              250202.000000   136.000000
========= Tipos de datos =========
Anio                           int64
RENIPRESS                      int64
EESS                             str
Ubigeo_Declarado_Paciente    float64
Distrito_RH_Paciente             str
Codigo_Item                      str
Codigo_Item_F                    str
Descripcion_Item                 str
id_genero                        str
grupo_edad                       str
cie                              str
desc_grupo                       str
TOTAL                          int64
dtype: object
```

# Consultas de complejidad en Hadoop
## Consulta 1 [c1](Resultados/c1)
Hacer 3 consultas con decimales que implique al menos un Mapreduce doble. Cada consulta por separado debe tener el Mapreduce doble como se vió en clase
### Consulta 1.1 [c1.1](./Resultados/c1/c1.1/)
#### ¿Cuál es el promedio de incidencia (casos reportados) de Infecciones de Transmisión Sexual (ITS) por Distrito del Paciente y Tipo de Diagnóstico en el año 2025?
Columnas a usar:
- Distrito_RH_Paciente : 4
- Descripcion_Item: 7
- TOTAL: 12
Descripción:
- Job1 - Mapper1
Emite una llave compuesta **Distrito|Diagnostico** y como valor el **TOTAL** en par del 1.
- Job1 - Reducer1
Agrupa la llave compuesta, suma los casos y cuenta los registros, emite una salida **Llave \t SumaAcumulada,ConteoTotal**
(Guarda la salida en la carpeta temp_mr_parcial_c1)
- Job2 - Mapper2
Lee la salida del Job1, separa el valor mediante la tabulación **\t** y pasa los datos a reducer
- Job2 - Reducer2
Recibe **SumaAcumulada** y **ConteoTotal**, los formatea a decimales, realiza la división y devuelve la salida final.
(Guarda la salida en la carpeta output_consulta1)

### Consulta 1.2 [c1.2](./Resultados/c2/)
#### ¿Cuál es el promedio de casos reportados por Tipo de ITS y Grupo de Edad del paciente?
Columnas a usar:
- Descripcion_Item: 7
- grupo_edad: 9
- TOTAL: 12
Descripción:
- Job1 - Mapper1
Emite una llave **Diagnostico|Grupo_de_edad** y como valor **TOTAL** igual que la anterior en par de un 1.
- Job1 - Reducer1
Agrupa la llave, suma los casos y cuena los registros, emite una salida **Llave \t SumaAcumulada,ConteoTotal**
(Guarda la salida en la carpeta temp_mr_parcial_c1)
- Job2 - Mapper2
Separa la salida del job1 y los pasa a reducer
- Job2 - Reducer2
Se encarga de ejecutar las divisiones y emite un promedio con 3 decimales
(Guarda la salida en la carpeta output_consulta2)
### Consulta 1.3 [c1.3](./Resultados/c3/)
#### ¿Cuál es el promedio de casos reportados por Tipo de Diagnóstico ITS y Género del paciente?
Columnas a usar:
- Descripcion_Item: 7
- id_genero: 8
- TOTAL: 12
Descripción:
- Job1 - Mapper1
Agrupa por **Diagnostico|Genero** con valor **TOTAL** seguido de un 1.
- Job1 - Reducer1
Genera una salida de tipo **Llave \t SumaAcumulada,ConteoTotal**
- Job2 - Mapper2
Realiza la división y el formateo a 3 decimales
## Consulta 2 [c2](./Resultados/c2/)
Implementar 1 consulta con campos decimales o cadenas de 1 modelo de machine learning de clasificacion (árboles de decisión, Random Forest, Support Vector Machine, KNN o Native Bayes) utilizando Hadoop Mapreduce
#### Algoritmo usado
Clasificación K-Nearest Neighbors (KNN) con K=5
#### Variable Objetivo
Descripción_Item (Diagnóstico de ITS)
#### Características
- Distrito
- Genero
- Grupo de edad
#### Perfil de Prueba
Paciente en Tacna, Masculino, perteneciente al grupo de edad de entre 18 a 29 años.
Descripción:

## Consulta 3 [c3](./Resultados/c3/)
Implementar 1 consulta con campos decimales o cadenas de 1 modelo de redes neuronales usando el concepto clásico de gradiente descendiente en Hadoop Mapreduce
### Modelo
Perceptrón (Red Neuronal de 1 capa) con optimización por Gradiente Descendente
### Función del Job
Ejecuta un Epoch de entrenmiento. El Mapper procesa el Forward Propagation y calcula las derivadas parciales (error).
El Reducer recibe los gradientes, los promedia, calcula el ajusta (Delta) multiplicado por la tasa de aprendizaje (alpha=0.01)
### Features Analizadas
Binarización de Género (X1) y Distrito (X2) contra la variable dependiente Total de Casos (Y)

# PowerBI
#### Con los resultados obtenidos de las consultas, implementar gráficos usando PowerBI y adjuntarlos a su informe y presentación. Mínimo 4 gráficos de los tipos vistos en clase
Para esta sección, lo que hacemos es cargar los siguientes outputs de la sección 1 de consultas al PowerBI:
- [output_1.1](./Resultados/c1/c1.1/Output/resultado_consulta_1.1.csv)
- [output_1.2](./Resultados/c1/c1.2/Output/resultado_consulta_1.2.csv)
- [output_1.3](./Resultados/c1/c1.3/Output/resultado_consulta_1.3.csv)

## Gráfico 1: El Mapa de Calor (Consulta 1.1 - Distrito)
Nos mostrará que distritos de TACNA presentan mayor volumen promedio de ITS reportadas.
- Tipo: Gráfico de Barras Horizontales
```bash
Categoria: Distrito
Valores: Promedio de Casos
```

### Gráfico 2: El Perfil Epidemiológico (De la Consulta 1.1 - Diagnóstico)
Nos mostrará la suma promedio por distrito al igual que la anterior, pero ahora nos mostrará que enfermedad específica predomina dentro de cada distrito
- Tipo: Gráfico de Columnas Apiladas
```bash
Eje X = Distrito
Eje Y = Promedio de Casos
Leyenda = Diagnóstico
```

### Gráfico 3: Alerta por Edades y Diagnóstico (De la Consulta 1.2 - Grupo de Edad)
Permite identificar cual es el rango de edad más vulnetable e ideal para realizar campañas de prevención con respecto a un diagnóstico en específico
- Tipo: Gráfico 
```bash
Eje X = Rango de Edad
Eje Y = Promedio de Casos
Leyenda = Diagnóstico
```

### Gráfico 4: Distribución por sexo (De la Consulta 1.3 - Género)
Mostrará la proporción de contagios promedio entre hombres y muejres a una ITS de forma general
- Tipo: Gráfico de Anillo
```bash
Leyenda = Sexo
Valores = Promedio de Casos
```

## Comandos usados
### Gestión de Archivos en Hadoop (HDFS)
```bash
# Meter el dataset al cluster
hdfs dfs -put /ruta/local/ITS_2025_limpio.csv /datos_parcial/

# Borrar carpetas
hdfs dfs -rm -r /output_consulta1 /temp_mr_parcial_c1

# Ver resultados directamente en la terminal
hdfs dfs -cat /output_seccion2/part-r-00000

# Descargar los resultados del HDFS al Ubuntu local
hdfs dfs -get /output_consulta1/part-r-00000 ~/resultado.csv
```

### Ejecución de Código (MapReduce)
```bash
# Ejecución estándar en el Cluster
hadoop jar PC-1.0.jar epmaccro.epmacro.EPDriver /datos_parcial/ITS_2025_limpio.csv /output_consulta1
```

### Monitoreo y Transferencia
```bash
# Verificar servicios de Hadoop
jps

# Monitor de Recursos en Tiempo Real
htop

# Puente SSH hacia Windows (Descargar un archivo de la VM Ubuntu a Windows)
scp hadoop@192.168.56.10:~/Parcial/consultas/resultado_seccion3.csv /mnt/c/Users/chris/OneDrive/Desktop/

# Puente de Windows a la VM de Ubuntu (para master)
scp PC-1.0.jar hadoop@192.168.56.10:/home/hadoop/Parcial/
```

### Control del Entorno (Para el SingleNode en WSL)
```bash
# Arrancar todos los servicios (HDFS + YARN)
start-all.sh

# Apagar todos los servicios
stop-all.sh
```