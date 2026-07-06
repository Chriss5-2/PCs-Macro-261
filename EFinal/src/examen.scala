import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.classification.{RandomForestClassifier, MultilayerPerceptronClassifier}
import org.apache.spark.ml.regression.{RandomForestRegressor, LinearRegression}
import org.apache.spark.ml.evaluation.{MulticlassClassificationEvaluator, RegressionEvaluator}

// ==========================================
// INICIO DEL CONTADOR DE TIEMPO
// ==========================================
val tiempoInicio = System.currentTimeMillis()

// 1. INICIALIZAR SPARK Y LEER DATOS
val spark = SparkSession.builder()
  .appName("Examen_Final_ONP")
  .master("spark://192.168.56.10:7077") 
  .getOrCreate()

spark.sparkContext.setLogLevel("ERROR")
println("--- LEYENDO DATOS DESDE HDFS ---")
val df_bruto = spark.read.option("header", "true").option("inferSchema", "true").csv("hdfs://192.168.56.10:9000/examen_onp/Afiliados*.csv")

val df_limpio = df_bruto.na.drop(Seq("edadact", "nro_semanas", "remuneracion", "montos_aportes", "aportante", "sexoact"))
  .withColumn("remuneracion", $"remuneracion".cast("Double"))
  .withColumn("montos_aportes", $"montos_aportes".cast("Double"))
  .withColumn("edadact", $"edadact".cast("Double"))
  .withColumn("nro_semanas", $"nro_semanas".cast("Double"))

// 2. GENERACIÓN DE ARCHIVOS JSON
println("--- GENERANDO ARCHIVOS JSON EN HDFS ---")
df_limpio.filter($"aportante" === 1).write.mode("overwrite").json("hdfs://192.168.56.10:9000/examen_onp/json_activos")
df_limpio.filter($"aportante" === 0).write.mode("overwrite").json("hdfs://192.168.56.10:9000/examen_onp/json_inactivos")

// 3. CONSULTAS SQL SOBRE LOS JSON
println("--- EJECUTANDO CONSULTAS SPARK SQL ---")
val df_activos_json = spark.read.json("hdfs://192.168.56.10:9000/examen_onp/json_activos")
df_activos_json.createOrReplaceTempView("activos")

val sql1 = spark.sql("SELECT dpto AS Departamento, ROUND(AVG(remuneracion), 2) AS Promedio_Sueldo FROM activos GROUP BY dpto ORDER BY Promedio_Sueldo DESC LIMIT 5")
println("Consulta 1: Top 5 Departamentos con mayor Sueldo Promedio")
sql1.show()

val sql2 = spark.sql("SELECT sexoact AS Sexo, ROUND(SUM(montos_aportes), 2) AS Total_Aportes FROM activos GROUP BY sexoact")
println("Consulta 2: Total de aportes divididos por Sexo")
sql2.show()

// 4. MACHINE LEARNING: PREPARACIÓN
println("--- PREPARANDO DATOS PARA MACHINE LEARNING ---")
val assemblerReg = new VectorAssembler().setInputCols(Array("edadact", "nro_semanas", "montos_aportes")).setOutputCol("features")
val dataReg = assemblerReg.transform(df_limpio).select("features", "remuneracion")

val assemblerClas = new VectorAssembler().setInputCols(Array("edadact", "sexoact", "nro_semanas")).setOutputCol("features")
val dataClas = assemblerClas.transform(df_limpio).select(col("features"), col("aportante").cast("Double").alias("label"))

val Array(trainReg, testReg) = dataReg.randomSplit(Array(0.8, 0.2), seed = 1234L)
val Array(trainClas, testClas) = dataClas.randomSplit(Array(0.8, 0.2), seed = 1234L)

// 5. REGRESIÓN 
println("--- MODELOS DE REGRESIÓN ---")
val evalRMSE = new RegressionEvaluator().setLabelCol("remuneracion").setPredictionCol("prediction").setMetricName("rmse")
val evalR2 = new RegressionEvaluator().setLabelCol("remuneracion").setPredictionCol("prediction").setMetricName("r2")

val lr = new LinearRegression().setFeaturesCol("features").setLabelCol("remuneracion").setMaxIter(10)
val lrModel = lr.fit(trainReg)
val lrPreds = lrModel.transform(testReg)
println(s"Regresion Lineal - RMSE: ${evalRMSE.evaluate(lrPreds)}, R2: ${evalR2.evaluate(lrPreds)}")

val rfReg = new RandomForestRegressor().setFeaturesCol("features").setLabelCol("remuneracion").setNumTrees(10)
val rfRegModel = rfReg.fit(trainReg)
val rfRegPreds = rfRegModel.transform(testReg)
println(s"Random Forest Regressor - RMSE: ${evalRMSE.evaluate(rfRegPreds)}, R2: ${evalR2.evaluate(rfRegPreds)}")

// 6. CLASIFICACIÓN
println("--- MODELOS DE CLASIFICACIÓN ---")
val evalAcc = new MulticlassClassificationEvaluator().setLabelCol("label").setPredictionCol("prediction").setMetricName("accuracy")

val rfClas = new RandomForestClassifier().setLabelCol("label").setFeaturesCol("features").setNumTrees(10)
val rfClasModel = rfClas.fit(trainClas)
val rfClasPreds = rfClasModel.transform(testClas)
println(s"Random Forest Classifier - Accuracy: ${evalAcc.evaluate(rfClasPreds)}")

val layers = Array[Int](3, 5, 4, 2)
val mlp = new MultilayerPerceptronClassifier().setLayers(layers).setBlockSize(128).setSeed(1234L).setMaxIter(100)
val mlpModel = mlp.fit(trainClas)
val mlpPreds = mlpModel.transform(testClas)
println(s"Multilayer Perceptron - Accuracy: ${evalAcc.evaluate(mlpPreds)}")

spark.stop()

// ==========================================
// FIN DEL CONTADOR DE TIEMPO Y CÁLCULO
// ==========================================
val tiempoFin = System.currentTimeMillis()
val tiempoTotalSegundos = (tiempoFin - tiempoInicio) / 1000.0

println("==================================================")
println(s"TIEMPO TOTAL DE EJECUCIÓN: $tiempoTotalSegundos segundos")
println("==================================================")