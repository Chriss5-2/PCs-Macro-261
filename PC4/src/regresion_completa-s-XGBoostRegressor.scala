import org.apache.spark.sql.functions._
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{VectorAssembler, StandardScaler}
import org.apache.spark.ml.regression.{LinearRegression, RandomForestRegressor, GBTRegressor}
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.tuning.{ParamGridBuilder, CrossValidator}
import org.apache.spark.ml.PipelineModel

// 1. Cargar y limpiar datos
val dfRaw = spark.read.option("header", "true").option("inferSchema", "true").csv("IGP_IonosferaRadarDigisonda_2013-2026_Dataset.csv")
val dfClean = dfRaw.na.drop(Seq("FREC_MAX_F2", "ALT_F", "ALT_MAX_DENS"))

// 2. Preparar variable objetivo y features
val dfReg = dfClean.withColumnRenamed("FREC_MAX_F2", "label")
val assembler = new VectorAssembler().setInputCols(Array("HORA_UTC", "ANIO", "ALT_F", "ALT_MAX_DENS")).setOutputCol("rawFeatures")
val scaler = new StandardScaler().setInputCol("rawFeatures").setOutputCol("features").setWithStd(true).setWithMean(true)

// 3. División de datos 
val Array(trainingData, testData) = dfReg.randomSplit(Array(0.7, 0.3), seed = 1234)

// 4. Instanciar Modelos (Nativos de Spark que no crashean en WSL2)
val lr = new LinearRegression().setLabelCol("label").setFeaturesCol("features")
val rfr = new RandomForestRegressor().setLabelCol("label").setFeaturesCol("features")
val gbt = new GBTRegressor().setLabelCol("label").setFeaturesCol("features").setMaxIter(5) 
// val xgb = new XGBoostRegressor().setLabelCol("label").setFeaturesCol("features") // Omitido por incompatibilidad de red en WSL2

// 5. Pipelines
val pipeLR = new Pipeline().setStages(Array(assembler, scaler, lr))
val pipeRFR = new Pipeline().setStages(Array(assembler, scaler, rfr))
val pipeGBT = new Pipeline().setStages(Array(assembler, scaler, gbt))

// 6. Evaluador y Grillas
val evaluatorReg = new RegressionEvaluator().setLabelCol("label").setPredictionCol("prediction")

val gridLR = new ParamGridBuilder().addGrid(lr.regParam, Array(0.1)).build()
val gridRFR = new ParamGridBuilder().addGrid(rfr.numTrees, Array(10)).build()
val gridGBT = new ParamGridBuilder().addGrid(gbt.maxDepth, Array(3)).build()

val cvLR = new CrossValidator().setEstimator(pipeLR).setEvaluator(evaluatorReg.setMetricName("rmse")).setEstimatorParamMaps(gridLR).setNumFolds(2)
val cvRFR = new CrossValidator().setEstimator(pipeRFR).setEvaluator(evaluatorReg.setMetricName("rmse")).setEstimatorParamMaps(gridRFR).setNumFolds(2)
val cvGBT = new CrossValidator().setEstimator(pipeGBT).setEvaluator(evaluatorReg.setMetricName("rmse")).setEstimatorParamMaps(gridGBT).setNumFolds(2)

// 7. Entrenamiento Seguro
println("Entrenando Regresión Lineal...")
val modelLR = cvLR.fit(trainingData)

println("Entrenando Random Forest...")
val modelRFR = cvRFR.fit(trainingData)

println("Entrenando GBT...")
val modelGBT = cvGBT.fit(trainingData)

// 8. Predicciones seguras
val predLR = modelLR.transform(testData)
val predRFR = modelRFR.transform(testData)
val predGBT = modelGBT.transform(testData)

// 9. Función de Métricas
def getRegMetrics(predictions: org.apache.spark.sql.DataFrame, modelName: String): Unit = {
  val rmse = evaluatorReg.setMetricName("rmse").evaluate(predictions)
  val mse = evaluatorReg.setMetricName("mse").evaluate(predictions)
  val mae = evaluatorReg.setMetricName("mae").evaluate(predictions)
  val r2 = evaluatorReg.setMetricName("r2").evaluate(predictions)
  println(f"$modelName%-20s | $rmse%.4f | $mse%.4f | $mae%.4f | $r2%.4f")
}

// 10. Imprimir Tabla de Resultados Final
println("\n=== TABLA DE MÉTRICAS: REGRESIÓN ===")
println("Modelo               | RMSE   | MSE    | MAE    | R^2")
println("---------------------------------------------------------")
getRegMetrics(predLR, "Linear Regression")
getRegMetrics(predRFR, "Random Forest Reg.")
getRegMetrics(predGBT, "GBT Regressor")
println("XGBoost Regressor    | --- Omitido por limitación NAT en WSL2 ---")
println("=========================================================\n")

// 11. Extraer Pérdidas (Obligando al optimizador iterativo l-bfgs para evitar el 0.0)
val lrGrafico = new LinearRegression().setLabelCol("label").setFeaturesCol("features").setMaxIter(15).setSolver("l-bfgs")
val pipeGrafico = new Pipeline().setStages(Array(assembler, scaler, lrGrafico))
val modeloGrafico = pipeGrafico.fit(trainingData)
val historyGrafico = modeloGrafico.stages.last.asInstanceOf[org.apache.spark.ml.regression.LinearRegressionModel].summary.objectiveHistory

println("\n--- Lista de loss_regresion ---")
println("loss_regresion = [" + historyGrafico.mkString(", ") + "]\n")