import org.apache.spark.sql.functions._
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{VectorAssembler, StringIndexer, MinMaxScaler}
import org.apache.spark.ml.classification.{DecisionTreeClassifier, RandomForestClassifier, LogisticRegression, NaiveBayes}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.tuning.{ParamGridBuilder, CrossValidator}

// 1. Cargar y limpiar datos
val dfRaw = spark.read.option("header", "true").option("inferSchema", "true").csv("IGP_IonosferaRadarDigisonda_2013-2026_Dataset.csv")
val dfClean = dfRaw.na.drop(Seq("FREC_MAX_F2", "ALT_F", "ALT_MAX_DENS"))

// 2. Crear variable objetivo (Target) y quitar la original para evitar fugas de datos
val dfClass = dfClean.withColumn("NIVEL_ION", 
  when(col("FREC_MAX_F2") < 5.62, "Baja")
  .when(col("FREC_MAX_F2") > 9.10, "Alta")
  .otherwise("Normal")
).drop("FREC_MAX_F2")

// 3. Preparar Features
val labelIndexer = new StringIndexer().setInputCol("NIVEL_ION").setOutputCol("label")
val assembler = new VectorAssembler().setInputCols(Array("HORA_UTC", "ANIO", "ALT_F", "ALT_MAX_DENS")).setOutputCol("rawFeatures")
val scaler = new MinMaxScaler().setInputCol("rawFeatures").setOutputCol("features")

// 4. División de datos
val Array(trainingData, testData) = dfClass.randomSplit(Array(0.8, 0.2), seed = 1234)

// 5. Instanciar Modelos
val dt = new DecisionTreeClassifier().setLabelCol("label").setFeaturesCol("features")
val rf = new RandomForestClassifier().setLabelCol("label").setFeaturesCol("features")
val lr = new LogisticRegression().setLabelCol("label").setFeaturesCol("features").setMaxIter(10)
val nb = new NaiveBayes().setLabelCol("label").setFeaturesCol("rawFeatures")

// 6. Pipelines
val pipeDT = new Pipeline().setStages(Array(labelIndexer, assembler, scaler, dt))
val pipeRF = new Pipeline().setStages(Array(labelIndexer, assembler, scaler, rf))
val pipeLR = new Pipeline().setStages(Array(labelIndexer, assembler, scaler, lr))
val pipeNB = new Pipeline().setStages(Array(labelIndexer, assembler, nb))

// 7. Grid-Search y Cross Validation
val evaluator = new MulticlassClassificationEvaluator().setLabelCol("label").setPredictionCol("prediction")

// Grillas de parámetros (reducidas para ejecución rápida)
val gridDT = new ParamGridBuilder().addGrid(dt.maxDepth, Array(5, 10)).build()
val gridRF = new ParamGridBuilder().addGrid(rf.numTrees, Array(10, 20)).build()
val gridLR = new ParamGridBuilder().addGrid(lr.regParam, Array(0.1, 0.01)).build()
val gridNB = new ParamGridBuilder().addGrid(nb.smoothing, Array(1.0)).build()

val cvDT = new CrossValidator().setEstimator(pipeDT).setEvaluator(evaluator.setMetricName("accuracy")).setEstimatorParamMaps(gridDT).setNumFolds(3)
val cvRF = new CrossValidator().setEstimator(pipeRF).setEvaluator(evaluator.setMetricName("accuracy")).setEstimatorParamMaps(gridRF).setNumFolds(3)
val cvLR = new CrossValidator().setEstimator(pipeLR).setEvaluator(evaluator.setMetricName("accuracy")).setEstimatorParamMaps(gridLR).setNumFolds(3)
val cvNB = new CrossValidator().setEstimator(pipeNB).setEvaluator(evaluator.setMetricName("accuracy")).setEstimatorParamMaps(gridNB).setNumFolds(3)

// 8. Entrenamiento
println("Entrenando modelos de clasificación")
println("Entrenando DECISION TREE")
val modelDT = cvDT.fit(trainingData)
println("Entrenando RANDOM FOREST")
val modelRF = cvRF.fit(trainingData)
println("Entrenando REGRESION LOGISTICA")
val modelLR = cvLR.fit(trainingData)
println("Entrenando NAIVE BAYES")
val modelNB = cvNB.fit(trainingData)

// 9. Predicciones
val predDT = modelDT.transform(testData)
val predRF = modelRF.transform(testData)
val predLR = modelLR.transform(testData)
val predNB = modelNB.transform(testData)

// 10. Función para extraer todas las métricas
def getMetrics(predictions: org.apache.spark.sql.DataFrame, modelName: String): Unit = {
  val acc = evaluator.setMetricName("accuracy").evaluate(predictions)
  val prec = evaluator.setMetricName("weightedPrecision").evaluate(predictions)
  val rec = evaluator.setMetricName("weightedRecall").evaluate(predictions)
  val f1 = evaluator.setMetricName("f1").evaluate(predictions)
  println(f"$modelName%-20s | $acc%.4f   | $prec%.4f    | $rec%.4f | $f1%.4f")
}

// 11. Imprimir Tabla de Resultados
println("\n=== TABLA DE MÉTRICAS: CLASIFICACIÓN ===")
println("Modelo               | Accuracy | Precision | Recall | F1-Score")
println("---------------------------------------------------------------")
getMetrics(predDT, "Decision Tree")
getMetrics(predRF, "Random Forest")
getMetrics(predLR, "Regresión Logística")
getMetrics(predNB, "Naive Bayes")
println("===============================================================\n")

// Extraer historial de Regresión Logística
val lrModelFinalClass = modelLR.bestModel.asInstanceOf[org.apache.spark.ml.PipelineModel].stages.last.asInstanceOf[org.apache.spark.ml.classification.LogisticRegressionModel]
val lossHistoryLRClass = lrModelFinalClass.summary.objectiveHistory

println("\n--- Listado de loss_clasificacion ---")
println("loss_clasificacion = [" + lossHistoryLRClass.mkString(", ") + "]")