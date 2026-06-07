# Practica Calificada 3

# Fase 1: Limpieza de Datos
El archivo limpiar_datos.py, genera una separación en dos tablas **Estacione (dimensión)** y **Mediciones (hechos)** para así poder hacer posile la realización de Joins que pide más adelante la guía de práctica.

# Fase 2: Generar dataset en postgres
## Comandos en PostgresSQL
´´´bash
psql -U postgres -h localhost -W
´´´

## Pasar los datasets a WSL
´´´bash
chris@LAPTOP-CHRIS:~$ cp /mnt/c/Users/chris/Downloads/Macrodatos_26-1/PCs-Macro-261/PC3/Datasets/estaciones_clean.csv ~/pc3_datasets/
chris@LAPTOP-CHRIS:~$ cp /mnt/c/Users/chris/Downloads/Macrodatos_26-1/PCs-Macro-261/PC3/Datasets/mediciones_clean.csv ~/
pc3_datasets/
chris@LAPTOP-CHRIS:~$ ls pc3_datasets/
estaciones_clean.csv  mediciones_clean.csv
chris@LAPTOP-CHRIS:~$
´´´

### Crear base de datos
´´´bash
create database pc3
## conectarse a la base de datos creada
\c pc3
´´´

### Creacion de tablas
#### Tabla de Estaciones
´´´bash
CREATE TABLE estaciones (
    estacion VARCHAR(100),
    departamento VARCHAR(50),
    provincia VARCHAR(50),
    distrito VARCHAR(50),
    ubigeo FLOAT,
    longitud FLOAT,
    latitud FLOAT,
    altitud FLOAT,
    id_estacion SERIAL PRIMARY KEY
);
´´´

#### Tabla de Mediciones
´´´bash
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
´´´

#### Rellenar tablas con los datos
´´´bash
\COPY estaciones FROM '~/pc3_datasets/estaciones_clean.csv' DELIMITER ',' CSV HEADER
//COPY 7
\COPY mediciones FROM '~/pc3_datasets/mediciones_clean.csv' DELIMITER ',' CSV HEADER
//COPY 464638
´´´

#### Verificar rellenado
estaciones:
´´´bash
pc3=# select * from estaciones limit 4;
    estacion    | departamento | provincia |  distrito   | ubigeo | longitud | latitud  | altitud | id_estacion
----------------+--------------+-----------+-------------+--------+----------+----------+---------+-------------
 CAMPO_DE_MARTE | LIMA         | LIMA      | JESUS_MARIA | 150113 | -77.0432 | -12.0705 |     117 |           1
 CARABAYLLO     | LIMA         | LIMA      | CARABAYLLO  | 150106 | -77.0336 | -11.9022 |     179 |           2
 SANTA_ANITA    | LIMA         | LIMA      | SANTA_ANITA | 150137 | -76.9714 |  -12.043 |     253 |           3
 SAN_BORJA      | LIMA         | LIMA      | SAN_BORJA   | 150130 | -77.0077 | -12.1086 |     128 |           4
(4 rows)
´´´

mediciones:
´´´bash
pc3=# select * from mediciones limit 4;
 id | id_estacion |  fecha   | hora  |  pm10  | pm2_5 |  no2  | fecha_corte
----+-------------+----------+-------+--------+-------+-------+-------------
  1 |           1 | 20150101 | 50000 |  37.92 | -99.9 | -99.9 |    20240531
  2 |           1 | 20150101 | 60000 | 153.39 | -99.9 | -99.9 |    20240531
  3 |           1 | 20150101 | 70000 | 116.49 | -99.9 | -99.9 |    20240531
  4 |           1 | 20150101 | 80000 |  80.74 | -99.9 | -99.9 |    20240531
(4 rows)
´´´

# Fase 3: Consultas con Scala-Spark y MapReduce
Pasando los datasets en la dirección donde se levanta scala
´´´bash
chris@LAPTOP-CHRIS:~$ cp pc3_datasets/estaciones_clean.csv spark/bin/
chris@LAPTOP-CHRIS:~$ cp pc3_datasets/mediciones_clean.csv spark/bin/
cd spark/bin
spark-shell
´´´
## Cargando datos
Tomemos en cuenta que los valores nulos del dataset, fueron rellenados con el valor -99.9
´´´bash
val rddMediciones = spark.sparkContext.textFile("mediciones_clean.csv")
val headerMediciones = rddMediciones.first()
val dataMediciones = rddMediciones.filter(row => row != headerMediciones).map(_.split(","))

val rddEstaciones = spark.sparkContext.textFile("estaciones_clean.csv")
val headerEstaciones = rddEstaciones.first()
val dataEstaciones = rddEstaciones.filter(row => row != headerEstaciones).map(_.split(","))
´´´

### 3a: Agrupando por id_estacion y encontrar el mayor y menor de PM10
´´´bash
val pm10_rdd = dataMediciones.filter(r => r(4).toDouble != -99.9).map(r => (r(1), r(4).toDouble))
    
val maxPM10PorEstacion = pm10_rdd.reduceByKey((a, b) => math.max(a, b))
val minPM10PorEstacion = pm10_rdd.reduceByKey((a, b) => math.min(a, b))
´´´
#### Resultados
´´´bash
scala> println("=== Salida 3a: Máximos de PM10 por ID de Estación ===")
=== Salida 3a: Máximos de PM10 por ID de Estación ===

scala> maxPM10PorEstacion.collect().foreach(println)
(4,463.6)
(6,488.02)
(2,712.7)
(7,974.0)
(5,628.5)
(3,715.0)
(1,259.0)

scala> println("=== Salida 3a: Mínimos de PM10 por ID de Estación ===")
=== Salida 3a: Mínimos de PM10 por ID de Estación ===

scala> minPM10PorEstacion.collect().foreach(println)
(4,5.53)
(6,3.588333)
(2,5.25)
(7,2.31)
(5,5.06)
(3,4.495)
(1,4.83)
´´´

### 3b: Calcular Promedio, media, desviación, mayor y menor de PM2.5 usando mapreduce
´´´bash
val pm25_valores = dataMediciones.filter(r => r(5).toDouble != -99.9).map(r => r(5).toDouble)
    
// El StatCounter de Spark calcula internamente mediante MapReduce estas métricas distribuidas
val statsPM25 = pm25_valores.aggregate(StatCounter())(_ merge _, _ merge _)
    
// Para la mediana exacta en RDD es necesario ordenar y extraer el elemento central
val valoresOrdenados = pm25_valores.sortBy(x => x).zipWithIndex().map(_.swap)
val conteo = statsPM25.count
val medianaPM25 = if (conteo % 2 == 0) {
  val m1 = valoresOrdenados.lookup(conteo / 2 - 1).head
  val m2 = valoresOrdenados.lookup(conteo / 2).head
  (m1 + m2) / 2.0
} else {
  valoresOrdenados.lookup(conteo / 2).head
    }
println("=== Salida 3b: Estadísticas de PM2.5 ===")
println(s"Promedio/Media Aritmética: ${statsPM25.mean}")
println(s"Mediana: $medianaPM25")
println(s"Desviación Estándar: ${statsPM25.stdev}")
println(s"Mayor Valor: ${statsPM25.max}")
println(s"Menor Valor: ${statsPM25.min}")
´´´
### Imprimiendo resultados
´´´bash
scala> println("=== Salida 3b: Estadísticas de PM2.5 ===")
=== Salida 3b: Estadísticas de PM2.5 ===

scala> println(s"Promedio/Media Aritmética: ${statsPM25.mean}")
Promedio/Media Aritmética: 24.25334719903103

scala> println(s"Mediana: $medianaPM25")
Mediana: 20.3

scala> println(s"Desviación Estándar: ${statsPM25.stdev}")
Desviación Estándar: 15.813815360058202

scala> println(s"Mayor Valor: ${statsPM25.max}")
Mayor Valor: 720.7

scala> println(s"Menor Valor: ${statsPM25.min}")
Menor Valor: 1.73
´´´

### 3c: Consultas con decimales que impliquen al menos 3 mapreduce
#### Consulta 1: Suma total ponderada de PM10 por estación si la medición fue de mañana (hora < 120000)
´´´bash
val c1 = dataMediciones.filter(r => r(6).toDouble != -99.9)                // MapReduce 1: Filter
               .map(r => (r(1), (r(6).toDouble * 1.15, 1)))                     // MapReduce 2: Transformación decimal
               .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))               // MapReduce 3: Agregación masiva
               .mapValues(v => v._1 / v._2)
´´´

#### Consulta 2: Diferencia entre PM10 y PM2.5 máxima por día (fecha)
´´´bash
val c2 = dataMediciones.filter(r => r(4).toDouble != -99.9 && r(5).toDouble != -99.9) // MapReduce 1
               .map(r => (r(2).substring(0, 4), r(4).toDouble + (r(5).toDouble * 0.85)))  // MapReduce 2
               .reduceByKey(_ + _)                                                        // MapReduce 3
´´´

#### Consulta 3: Promedio manual (suma/conteo) de NO2 por estación usando combiners
´´´bash
val c3 = dataMediciones.filter(r => r(4).toDouble > 0 && r(5).toDouble != -99.9) // MapReduce 1
               .map(r => (r(1), (r(5).toDouble / r(4).toDouble, 1)))                 // MapReduce 2
               .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))                     // MapReduce 3
               .mapValues(v => v._1 / v._2)
´´´

#### Resultados
´´´bash
scala> println("=== Salida 3c: Consulta C1 ==="); c1.collect().foreach(println)
=== Salida 3c: Consulta C1 ===
(4,24.0980824325179)
(6,26.54393253739011)
(2,25.95358996984233)
(7,20.407989039306628)
(5,36.091087956933194)
(3,29.17916628796369)
(1,20.53862849183438)

scala> println("=== Salida 3c: Consulta C2 ==="); c2.collect().foreach(println)
=== Salida 3c: Consulta C2 ===
(2015,3983887.40999999)
(2017,3996607.6049999828)
(2024,1078303.6123609245)
(2022,4755346.158500003)
(2019,1031695.8229999992)
(2020,1108193.8084328123)
(2021,592607.6155000001)
(2016,3969511.670000015)
(2023,2519566.369345591)
(2018,3202495.095)

scala> println("=== Salida 3c: Consulta C3 ==="); c3.collect().foreach(println)
=== Salida 3c: Consulta C3 ===
(4,0.34221631557154425)
(6,0.4875260202471285)
(2,0.4670937824144551)
(7,0.41770276446144)
(5,0.4653496616994144)
(3,0.5252104331300864)
(1,0.6204783955119277)
´´´

# Fase 4: Consultas con API de Scala-Spark SQL
## Cargando datos como DataFrame
´´´bash
import org.apache.spark.sql.functions._
import spark.implicits._
val dfMediciones = spark.read.option("header", "true").option("inferSchema", "true").csv("mediciones_clean.csv")
val dfEstaciones = spark.read.option("header", "true").option("inferSchema", "true").csv("estaciones_clean.csv")
´´´

## 5a. 3 Consultas con filter (2 o más columnas)
´´´bash
val f1 = dfMediciones.filter($"PM10" > 120.0 && $"PM2_5" > 50.0)
val f2 = dfMediciones.filter($"FECHA" >= 20220101 && $"NO2" =!= -99.9)
val f3 = dfEstaciones.filter($"ALTITUD" > 100.0 && $"DISTRITO" === "CARABAYLLO")
´´´
### Resultados
´´´bash
scala> f1.show()
+----+-----------+--------+------+------+-----+-----+-----------+
|  ID|ID_ESTACION|   FECHA|  HORA|  PM10|PM2_5|  NO2|FECHA_CORTE|
+----+-----------+--------+------+------+-----+-----+-----------+
|2385|          1|20150410|130000|142.58| 87.1|-99.9|   20240531|
|2386|          1|20150410|140000| 150.4| 96.4|-99.9|   20240531|
|2387|          1|20150410|150000|149.95| 87.2|-99.9|   20240531|
|2482|          1|20150414|140000| 150.0| 70.3|-99.9|   20240531|
|2483|          1|20150414|150000|138.98| 94.1|-99.9|   20240531|
|2578|          1|20150418|140000|171.71| 82.7|-99.9|   20240531|
|2579|          1|20150418|150000|146.72| 78.2|-99.9|   20240531|
|2698|          1|20150423|140000|165.02| 66.5|-99.9|   20240531|
|2699|          1|20150423|150000|127.55| 71.6|-99.9|   20240531|
|2986|          1|20150505|140000|177.47| 73.8|-99.9|   20240531|
|2987|          1|20150505|150000|209.74| 77.4|-99.9|   20240531|
|3741|          1|20150606| 10000|157.93| 51.1|-99.9|   20240531|
|3742|          1|20150606| 20000|142.83| 53.0|-99.9|   20240531|
|3754|          1|20150606|140000| 144.2| 52.4|-99.9|   20240531|
|3755|          1|20150606|150000|155.42| 52.7|-99.9|   20240531|
|3803|          1|20150608|150000|155.05| 58.3|-99.9|   20240531|
|3804|          1|20150608|160000| 172.9| 54.0|-99.9|   20240531|
|4354|          1|20150701|140000|148.19| 65.6|-99.9|   20240531|
|8594|          1|20151225| 60000|149.74| 77.0| 80.3|   20240531|
|8762|          1|20160101| 60000|120.53| 65.7| 82.2|   20240531|
+----+-----------+--------+------+------+-----+-----+-----------+
only showing top 20 rows


scala> f2.show()
+-----+-----------+--------+------+-----+-----+----+-----------+
|   ID|ID_ESTACION|   FECHA|  HORA| PM10|PM2_5| NO2|FECHA_CORTE|
+-----+-----------+--------+------+-----+-----+----+-----------+
|61364|          1|20220101|     0|10.43| 8.42|14.1|   20240531|
|61365|          1|20220101| 10000|10.37| 9.21|12.3|   20240531|
|61366|          1|20220101| 20000|14.74|12.75|14.6|   20240531|
|61367|          1|20220101| 30000|14.36|12.54|13.0|   20240531|
|61368|          1|20220101| 40000|13.41|11.95|14.7|   20240531|
|61369|          1|20220101| 50000|13.02|11.72|12.3|   20240531|
|61370|          1|20220101| 60000|14.46|13.03| 6.7|   20240531|
|61371|          1|20220101| 70000| 20.0|18.21| 6.8|   20240531|
|61372|          1|20220101| 80000|29.42| 27.5|10.0|   20240531|
|61373|          1|20220101| 90000| 27.1|25.14| 9.6|   20240531|
|61374|          1|20220101|100000|21.27|18.95| 6.8|   20240531|
|61375|          1|20220101|110000|24.48|21.46| 9.7|   20240531|
|61376|          1|20220101|120000|30.47| 28.0|13.2|   20240531|
|61377|          1|20220101|130000|20.08|17.95| 9.1|   20240531|
|61378|          1|20220101|140000|20.16|18.27| 9.1|   20240531|
|61379|          1|20220101|150000|14.82|13.22| 6.0|   20240531|
|61380|          1|20220101|160000|13.28|11.65| 5.4|   20240531|
|61381|          1|20220101|170000|10.18| 8.45| 4.3|   20240531|
|61382|          1|20220101|180000|  7.7| 6.25| 3.2|   20240531|
|61383|          1|20220101|190000| 6.59| 5.15| 3.3|   20240531|
+-----+-----------+--------+------+-----+-----+----+-----------+
only showing top 20 rows


scala> f3.show()
+----------+------------+---------+----------+--------+--------+--------+-------+-----------+
|  ESTACION|DEPARTAMENTO|PROVINCIA|  DISTRITO|  UBIGEO|LONGITUD| LATITUD|ALTITUD|ID_ESTACION|
+----------+------------+---------+----------+--------+--------+--------+-------+-----------+
|CARABAYLLO|        LIMA|     LIMA|CARABAYLLO|150106.0|-77.0336|-11.9022|  179.0|          2|
+----------+------------+---------+----------+--------+--------+--------+-------+-----------+
´´´

## 5b. 3 Consultas groupBy y count
´´´bash
val gb1 = dfMediciones.groupBy("ID_ESTACION").count()
val gb2 = dfEstaciones.groupBy("DISTRITO").count()
val gb3 = dfMediciones.groupBy("FECHA").count()
´´´

### Resultados
´´´bash
scala> gb1.show()
+-----------+-----+
|ID_ESTACION|count|
+-----------+-----+
|          1|71072|
|          2|63590|
|          3|72454|
|          4|66429|
|          5|72865|
|          6|62148|
|          7|56080|
+-----------+-----+


scala> gb2.show()
+--------------------+-----+
|            DISTRITO|count|
+--------------------+-----+
|         JESUS_MARIA|    1|
|         SANTA_ANITA|    1|
|VILLA_MARIA_DEL_T...|    1|
|SAN_JUAN_DE_LURIG...|    1|
|           SAN_BORJA|    1|
|          CARABAYLLO|    1|
|SAN_MARTIN_DE_PORRES|    1|
+--------------------+-----+


scala> gb3.show()
+--------+-----+
|   FECHA|count|
+--------+-----+
|20150528|  143|
|20160911|  144|
|20180615|  168|
|20190228|   72|
|20190628|   72|
|20191119|   96|
|20230530|  144|
|20240118|  168|
|20240212|  168|
|20150323|  144|
|20150703|  144|
|20150815|  144|
|20160320|  168|
|20160704|  144|
|20160722|  168|
|20170907|  168|
|20180301|  117|
|20180420|  160|
|20190713|   48|
|20190810|   41|
+--------+-----+
only showing top 20 rows
´´´

## 5c. Promedio de una columna y ordenamiento decreciente
´´´bash
val promOrd = dfMediciones.filter($"PM10" =!= -99.9)
      .groupBy("ID_ESTACION")
      .agg(avg("PM10").alias("Promedio_PM10"))
      .orderBy(desc("Promedio_PM10"))
´´´

### Resultados
´´´bash
scala> promOrd.show()
+-----------+------------------+
|ID_ESTACION|     Promedio_PM10|
+-----------+------------------+
|          7| 97.43308072053651|
|          5| 74.87453247293904|
|          2| 71.79205524019638|
|          3|62.944336993703715|
|          4| 58.76875560333776|
|          6| 41.51255382166422|
|          1|30.715066063932547|
+-----------+------------------+
´´´

## 5d. 3 Consultas con Join (Diversos tipos)
´´´bash
val j1 = dfMediciones.join(dfEstaciones, Seq("ID_ESTACION"), "inner")
val j2 = dfMediciones.join(dfEstaciones, Seq("ID_ESTACION"), "left")
val j3 = dfEstaciones.join(dfMediciones, Seq("ID_ESTACION"), "right")
´´´

### Resultados
´´´bash
scala> j1.show(5)
+-----------+---+--------+-----+------+-----+-----+-----------+--------------+------------+---------+-----------+--------+--------+--------+-------+
|ID_ESTACION| ID|   FECHA| HORA|  PM10|PM2_5|  NO2|FECHA_CORTE|      ESTACION|DEPARTAMENTO|PROVINCIA|   DISTRITO|  UBIGEO|LONGITUD| LATITUD|ALTITUD|
+-----------+---+--------+-----+------+-----+-----+-----------+--------------+------------+---------+-----------+--------+--------+--------+-------+
|          1|  1|20150101|50000| 37.92|-99.9|-99.9|   20240531|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|
|          1|  2|20150101|60000|153.39|-99.9|-99.9|   20240531|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|
|          1|  3|20150101|70000|116.49|-99.9|-99.9|   20240531|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|
|          1|  4|20150101|80000| 80.74|-99.9|-99.9|   20240531|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|
|          1|  5|20150101|90000|  27.4|-99.9|-99.9|   20240531|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|
+-----------+---+--------+-----+------+-----+-----+-----------+--------------+------------+---------+-----------+--------+--------+--------+-------+
only showing top 5 rows


scala> j2.show(5)
+-----------+---+--------+-----+------+-----+-----+-----------+--------------+------------+---------+-----------+--------+--------+--------+-------+
|ID_ESTACION| ID|   FECHA| HORA|  PM10|PM2_5|  NO2|FECHA_CORTE|      ESTACION|DEPARTAMENTO|PROVINCIA|   DISTRITO|  UBIGEO|LONGITUD| LATITUD|ALTITUD|
+-----------+---+--------+-----+------+-----+-----+-----------+--------------+------------+---------+-----------+--------+--------+--------+-------+
|          1|  1|20150101|50000| 37.92|-99.9|-99.9|   20240531|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|
|          1|  2|20150101|60000|153.39|-99.9|-99.9|   20240531|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|
|          1|  3|20150101|70000|116.49|-99.9|-99.9|   20240531|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|
|          1|  4|20150101|80000| 80.74|-99.9|-99.9|   20240531|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|
|          1|  5|20150101|90000|  27.4|-99.9|-99.9|   20240531|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|
+-----------+---+--------+-----+------+-----+-----+-----------+--------------+------------+---------+-----------+--------+--------+--------+-------+
only showing top 5 rows


scala> j3.show(5)
+-----------+--------------+------------+---------+-----------+--------+--------+--------+-------+---+--------+-----+------+-----+-----+-----------+
|ID_ESTACION|      ESTACION|DEPARTAMENTO|PROVINCIA|   DISTRITO|  UBIGEO|LONGITUD| LATITUD|ALTITUD| ID|   FECHA| HORA|  PM10|PM2_5|  NO2|FECHA_CORTE|
+-----------+--------------+------------+---------+-----------+--------+--------+--------+-------+---+--------+-----+------+-----+-----+-----------+
|          1|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|  1|20150101|50000| 37.92|-99.9|-99.9|   20240531|
|          1|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|  2|20150101|60000|153.39|-99.9|-99.9|   20240531|
|          1|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|  3|20150101|70000|116.49|-99.9|-99.9|   20240531|
|          1|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|  4|20150101|80000| 80.74|-99.9|-99.9|   20240531|
|          1|CAMPO_DE_MARTE|        LIMA|     LIMA|JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|  5|20150101|90000|  27.4|-99.9|-99.9|   20240531|
+-----------+--------------+------------+---------+-----------+--------+--------+--------+-------+---+--------+-----+------+-----+-----+-----------+
only showing top 5 rows
´´´

## 5e. 3 Consultas utilizando org.apache.spark.sql.functions._
´´´bash
val func1 = dfMediciones.withColumn("PM10_Redondeado", round($"PM10", 1))
val func2 = dfMediciones.withColumn("Evaluacion_Aire", when($"PM2_5" > 50.0, "Malo").otherwise("Aceptable"))
val func3 = dfEstaciones.select(upper($"ESTACION").alias("ESTACION_UPPER"), $"DISTRITO")
´´´
### Resultados
´´´bash
scala> func1.show(5)
+---+-----------+--------+-----+------+-----+-----+-----------+---------------+
| ID|ID_ESTACION|   FECHA| HORA|  PM10|PM2_5|  NO2|FECHA_CORTE|PM10_Redondeado|
+---+-----------+--------+-----+------+-----+-----+-----------+---------------+
|  1|          1|20150101|50000| 37.92|-99.9|-99.9|   20240531|           37.9|
|  2|          1|20150101|60000|153.39|-99.9|-99.9|   20240531|          153.4|
|  3|          1|20150101|70000|116.49|-99.9|-99.9|   20240531|          116.5|
|  4|          1|20150101|80000| 80.74|-99.9|-99.9|   20240531|           80.7|
|  5|          1|20150101|90000|  27.4|-99.9|-99.9|   20240531|           27.4|
+---+-----------+--------+-----+------+-----+-----+-----------+---------------+
only showing top 5 rows


scala> func2.show(5)
+---+-----------+--------+-----+------+-----+-----+-----------+---------------+
| ID|ID_ESTACION|   FECHA| HORA|  PM10|PM2_5|  NO2|FECHA_CORTE|Evaluacion_Aire|
+---+-----------+--------+-----+------+-----+-----+-----------+---------------+
|  1|          1|20150101|50000| 37.92|-99.9|-99.9|   20240531|      Aceptable|
|  2|          1|20150101|60000|153.39|-99.9|-99.9|   20240531|      Aceptable|
|  3|          1|20150101|70000|116.49|-99.9|-99.9|   20240531|      Aceptable|
|  4|          1|20150101|80000| 80.74|-99.9|-99.9|   20240531|      Aceptable|
|  5|          1|20150101|90000|  27.4|-99.9|-99.9|   20240531|      Aceptable|
+---+-----------+--------+-----+------+-----+-----+-----------+---------------+
only showing top 5 rows


scala> func3.show(5)
+--------------------+--------------------+
|      ESTACION_UPPER|            DISTRITO|
+--------------------+--------------------+
|      CAMPO_DE_MARTE|         JESUS_MARIA|
|          CARABAYLLO|          CARABAYLLO|
|         SANTA_ANITA|         SANTA_ANITA|
|           SAN_BORJA|           SAN_BORJA|
|SAN_JUAN_DE_LURIG...|SAN_JUAN_DE_LURIG...|
+--------------------+--------------------+
only showing top 5 rows
´´´

# Fase 5: Consultas con Vistas Temporales
## Creacion de vistas
´´´bash
dfMediciones.createOrReplaceTempView("v_mediciones")
dfEstaciones.createOrReplaceTempView("v_estaciones")
´´´

## 5f. 3 Selecciones usando join en diversos tipos
´´´bash
val sqlJ1 = spark.sql("SELECT m.ID, e.ESTACION, m.PM10 FROM v_mediciones m INNER JOIN v_estaciones e ON m.ID_ESTACION = e.ID_ESTACION WHERE m.PM10 > 100")
val sqlJ2 = spark.sql("SELECT e.DISTRITO, m.PM2_5 FROM v_estaciones e LEFT JOIN v_mediciones m ON e.ID_ESTACION = m.ID_ESTACION AND m.PM2_5 > 0")
val sqlJ3 = spark.sql("SELECT m.FECHA, e.ESTACION FROM v_mediciones m FULL JOIN v_estaciones e ON m.ID_ESTACION = e.ID_ESTACION WHERE m.FECHA = 20230515")

´´´

### Resultados
´´´bash
scala> sqlJ1.show(5)
+----+--------------+------+
|  ID|      ESTACION|  PM10|
+----+--------------+------+
|   2|CAMPO_DE_MARTE|153.39|
|   3|CAMPO_DE_MARTE|116.49|
| 947|CAMPO_DE_MARTE|102.09|
|1308|CAMPO_DE_MARTE|100.11|
|1353|CAMPO_DE_MARTE|122.73|
+----+--------------+------+
only showing top 5 rows


scala> sqlJ2.show(5)
+-----------+-----+
|   DISTRITO|PM2_5|
+-----------+-----+
|JESUS_MARIA|37.12|
|JESUS_MARIA|35.85|
|JESUS_MARIA|43.15|
|JESUS_MARIA|51.55|
|JESUS_MARIA|50.44|
+-----------+-----+
only showing top 5 rows


scala> sqlJ3.show(5)
+--------+----------+
|   FECHA|  ESTACION|
+--------+----------+
|20230515|CARABAYLLO|
|20230515|CARABAYLLO|
|20230515|CARABAYLLO|
|20230515|CARABAYLLO|
|20230515|CARABAYLLO|
+--------+----------+
only showing top 5 rows
´´´

## 5g. 3 GroupBy usando count
´´´bash
val sqlG1 = spark.sql("SELECT ID_ESTACION, HORA, COUNT(*) as Total FROM v_mediciones GROUP BY ID_ESTACION, HORA")
val sqlG2 = spark.sql("SELECT DISTRITO, COUNT(DISTINCT ID_ESTACION) as Num_Estaciones FROM v_estaciones GROUP BY DISTRITO")
val sqlG3 = spark.sql("SELECT FECHA_CORTE, COUNT(*) as Total FROM v_mediciones GROUP BY FECHA_CORTE")

´´´

### Resultados
´´´bash
scala> sqlG1.show(5)
+-----------+------+-----+
|ID_ESTACION|  HORA|Total|
+-----------+------+-----+
|          1|     0| 2979|
|          2|170000| 2644|
|          2|230000| 2667|
|          2| 40000| 2651|
|          1| 20000| 2976|
+-----------+------+-----+
only showing top 5 rows


scala> sqlG2.show(5)
+--------------------+--------------+
|            DISTRITO|Num_Estaciones|
+--------------------+--------------+
|         JESUS_MARIA|             1|
|         SANTA_ANITA|             1|
|VILLA_MARIA_DEL_T...|             1|
|SAN_JUAN_DE_LURIG...|             1|
|           SAN_BORJA|             1|
+--------------------+--------------+
only showing top 5 rows


scala> sqlG3.show(5)
+-----------+------+
|FECHA_CORTE| Total|
+-----------+------+
|   20240531|464638|
+-----------+------+
´´´

## 5h. 3 OrderBy combinando con filtros
´´´bash
val sqlO1 = spark.sql("SELECT * FROM v_mediciones WHERE NO2 > 40.0 ORDER BY NO2 DESC")
val sqlO2 = spark.sql("SELECT * FROM v_estaciones WHERE ALTITUD < 150 ORDER BY ALTITUD ASC")
val sqlO3 = spark.sql("SELECT ID, PM10 FROM v_mediciones WHERE FECHA = 20150101 AND PM10 > 0 ORDER BY HORA DESC")
´´´

### Resultados
´´´bash
scala> sqlO1.show(5)
+------+-----------+--------+------+-----+-----+-----+-----------+
|    ID|ID_ESTACION|   FECHA|  HORA| PM10|PM2_5|  NO2|FECHA_CORTE|
+------+-----------+--------+------+-----+-----+-----+-----------+
|206524|          3|20190923|200000| 93.4|15.91|231.2|   20240531|
|244874|          3|20240207|180000|21.04| 7.87|195.9|   20240531|
|241214|          3|20230908| 60000|25.45|23.45|194.2|   20240531|
|244873|          3|20240207|170000| 26.2| 9.25|186.4|   20240531|
|244802|          3|20240204|180000|17.92| 12.4|180.7|   20240531|
+------+-----------+--------+------+-----+-----+-----+-----------+
only showing top 5 rows


scala> sqlO2.show(5)
+--------------------+------------+---------+--------------------+--------+--------+--------+-------+-----------+
|            ESTACION|DEPARTAMENTO|PROVINCIA|            DISTRITO|  UBIGEO|LONGITUD| LATITUD|ALTITUD|ID_ESTACION|
+--------------------+------------+---------+--------------------+--------+--------+--------+-------+-----------+
|SAN_MARTIN_DE_PORRES|        LIMA|     LIMA|SAN_MARTIN_DE_PORRES|150135.0|-77.0845|-12.0089|   56.0|          6|
|      CAMPO_DE_MARTE|        LIMA|     LIMA|         JESUS_MARIA|150113.0|-77.0432|-12.0705|  117.0|          1|
|           SAN_BORJA|        LIMA|     LIMA|           SAN_BORJA|150130.0|-77.0077|-12.1086|  128.0|          4|
+--------------------+------------+---------+--------------------+--------+--------+--------+-------+-----------+


scala> sqlO3.show(5)
+------+------+
|    ID|  PM10|
+------+------+
| 82561| 41.86|
|495271|100.86|
|412729| 26.23|
|165103| 38.55|
|330187|  30.0|
+------+------+
only showing top 5 rows
´´´