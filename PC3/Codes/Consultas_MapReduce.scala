import org.apache.spark.sql.SparkSession
import org.apache.spark.util.StatCounter

object ConsultasMapReduce {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Laboratorio 3 - MapReduce")
      .master("local[*]")
      .getOrCreate()

    // Carga de datos limpios
    val rddMediciones = spark.sparkContext.textFile("mediciones_clean.csv")
    val headerMediciones = rddMediciones.first()
    val dataMediciones = rddMediciones.filter(row => row != headerMediciones).map(_.split(","))

    val rddEstaciones = spark.sparkContext.textFile("estaciones_clean.csv")
    val headerEstaciones = rddEstaciones.first()
    val dataEstaciones = rddEstaciones.filter(row => row != headerEstaciones).map(_.split(","))

    // -------------------------------------------------------------------------
    // PREGUNTA 3a: Agrupar por tipo (Estación) y hallar Max y Min de PM10 [cite: 9]
    // -------------------------------------------------------------------------
    val pm10_rdd = dataMediciones.filter(r => r(4).toDouble != -99.9).map(r => (r(1), r(4).toDouble))
    
    val maxPM10PorEstacion = pm10_rdd.reduceByKey((a, b) => math.max(a, b))
    val minPM10PorEstacion = pm10_rdd.reduceByKey((a, b) => math.min(a, b))

    println("=== Salida 3a: Máximos de PM10 por ID de Estación ===")
    maxPM10PorEstacion.collect().foreach(println)
    println("=== Salida 3a: Mínimos de PM10 por ID de Estación ===")
    minPM10PorEstacion.collect().foreach(println)

    // -------------------------------------------------------------------------
    // PREGUNTA 3b: Promedio, media, desviación, mayor y menor de PM2.5 [cite: 10]
    // -------------------------------------------------------------------------
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

    // -------------------------------------------------------------------------
    // PREGUNTA 3c: 3 Consultas con decimales con triple MapReduce cada una [cite: 12]
    // -------------------------------------------------------------------------
    
    // Consulta C1: Promedio ponderado de NO2 (Multiplicado por un factor decimal de 1.15) por Estación
    val c1 = dataMediciones.filter(r => r(6).toDouble != -99.9)                // MapReduce 1: Filter
               .map(r => (r(1), (r(6).toDouble * 1.15, 1)))                     // MapReduce 2: Transformación decimal
               .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))               // MapReduce 3: Agregación masiva
               .mapValues(v => v._1 / v._2)
    
    // Consulta C2: Suma de la carga contaminante total (PM10 + PM2.5) ponderada por año
    val c2 = dataMediciones.filter(r => r(4).toDouble != -99.9 && r(5).toDouble != -99.9) // MapReduce 1
               .map(r => (r(2).substring(0, 4), r(4).toDouble + (r(5).toDouble * 0.85)))  // MapReduce 2
               .reduceByKey(_ + _)                                                        // MapReduce 3

    // Consulta C3: Ratio promedio decimal entre PM2.5 y PM10 por estación
    val c3 = dataMediciones.filter(r => r(4).toDouble > 0 && r(5).toDouble != -99.9) // MapReduce 1
               .map(r => (r(1), (r(5).toDouble / r(4).toDouble, 1)))                 // MapReduce 2
               .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))                     // MapReduce 3
               .mapValues(v => v._1 / v._2)

    println("=== Salida 3c: Consulta C1 ==="); c1.collect().foreach(println)
    println("=== Salida 3c: Consulta C2 ==="); c2.collect().foreach(println)
    println("=== Salida 3c: Consulta C3 ==="); c3.collect().foreach(println)

    spark.stop()
  }
}