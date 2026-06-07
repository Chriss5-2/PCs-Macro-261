import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object ConsultasSparkSQL {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Laboratorio 3 - Spark SQL")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    // Carga de DataFrames
    val dfMediciones = spark.read.option("header", "true").option("inferSchema", "true").csv("mediciones_clean.csv")
    val dfEstaciones = spark.read.option("header", "true").option("inferSchema", "true").csv("estaciones_clean.csv")

    // --- 5a. 3 Consultas usando Filter con 2 o más columnas [cite: 18] ---
    val f1 = dfMediciones.filter($"PM10" > 120.0 && $"PM2_5" > 50.0)
    val f2 = dfMediciones.filter($"FECHA" >= 20220101 && $"NO2" =!= -99.9)
    val f3 = dfEstaciones.filter($"ALTITUD" > 100.0 && $"DISTRITO" === "CARABAYLLO")

    // --- 5b. 3 Consultas usando groupBy y count [cite: 19] ---
    val gb1 = dfMediciones.groupBy("ID_ESTACION").count()
    val gb2 = dfEstaciones.groupBy("DISTRITO").count()
    val gb3 = dfMediciones.groupBy("FECHA").count()

    // --- 5c. 1 Consulta con promedio y ordenamiento decreciente [cite: 20] ---
    val promOrd = dfMediciones.filter($"PM10" =!= -99.9)
      .groupBy("ID_ESTACION")
      .agg(avg("PM10").alias("Promedio_PM10"))
      .orderBy(desc("Promedio_PM10"))

    // --- 5d. 3 Consultas con Join en diversos tipos [cite: 21] ---
    val j1 = dfMediciones.join(dfEstaciones, Seq("ID_ESTACION"), "inner")
    val j2 = dfMediciones.join(dfEstaciones, Seq("ID_ESTACION"), "left")
    val j3 = dfEstaciones.join(dfMediciones, Seq("ID_ESTACION"), "right")

    // --- 5e. 3 Consultas utilizando org.apache.spark.sql.functions._ [cite: 22] ---
    val func1 = dfMediciones.withColumn("PM10_Redondeado", round($"PM10", 1))
    val func2 = dfMediciones.withColumn("Evaluacion_Aire", when($"PM2_5" > 50.0, "Malo").otherwise("Aceptable"))
    val func3 = dfEstaciones.select(upper($"ESTACION").alias("ESTACION_UPPER"), $"DISTRITO")

    // Registrar vistas temporales para SQL nativo [cite: 23]
    dfMediciones.createOrReplaceTempView("v_mediciones")
    dfEstaciones.createOrReplaceTempView("v_estaciones")

    // --- 5f. 3 Consultas de Selección utilizando Join en SQL (diferentes a 5d) [cite: 24] ---
    val sqlJ1 = spark.sql("SELECT m.ID, e.ESTACION, m.PM10 FROM v_mediciones m INNER JOIN v_estaciones e ON m.ID_ESTACION = e.ID_ESTACION WHERE m.PM10 > 100")
    val sqlJ2 = spark.sql("SELECT e.DISTRITO, m.PM2_5 FROM v_estaciones e LEFT JOIN v_mediciones m ON e.ID_ESTACION = m.ID_ESTACION AND m.PM2_5 > 0")
    val sqlJ3 = spark.sql("SELECT m.FECHA, e.ESTACION FROM v_mediciones m FULL JOIN v_estaciones e ON m.ID_ESTACION = e.ID_ESTACION WHERE m.FECHA = 20230515")

    // --- 5g. 3 Consultas GroupBy usando count en SQL (diferentes a 5b) [cite: 25] ---
    val sqlG1 = spark.sql("SELECT ID_ESTACION, HORA, COUNT(*) as Total FROM v_mediciones GROUP BY ID_ESTACION, HORA")
    val sqlG2 = spark.sql("SELECT DISTRITO, COUNT(DISTINCT ID_ESTACION) as Num_Estaciones FROM v_estaciones GROUP BY DISTRITO")
    val sqlG3 = spark.sql("SELECT FECHA_CORTE, COUNT(*) as Total FROM v_mediciones GROUP BY FECHA_CORTE")

    // --- 5h. 3 Consultas OrderBy combinado con filtros en SQL [cite: 26] ---
    val sqlO1 = spark.sql("SELECT * FROM v_mediciones WHERE NO2 > 40.0 ORDER BY NO2 DESC")
    val sqlO2 = spark.sql("SELECT * FROM v_estaciones WHERE ALTITUD < 150 ORDER BY ALTITUD ASC")
    val sqlO3 = spark.sql("SELECT ID, PM10 FROM v_mediciones WHERE FECHA = 20150101 AND PM10 > 0 ORDER BY HORA DESC")

    spark.stop()
  }
}