# Practica Calificada 4
## Integrantes
-
-
-

# Dataset
El dataset a usar es una recolección de datos de la ionósfera para analizar el comportamiento de este [dataset](../IGP_IonosferaRadarDigisonda_2013-2026_Dataset.csv)

## summary.py
Al ejecutar el script [summary.py](../summary.py) podemos notar datos que describen el CSV a usar
´´´bash
(PC4) PS C:\Users\chris\Downloads\Macrodatos_26-1\PCs-Macro-261\PC4> python .\summary.py
========= Columnas del dataset =========
Index(['FECHA_CORTE', 'UBIGEO', 'ANIO', 'DEPARTAMENTO', 'PROVINCIA',
       'DISTRITO', 'FECHA_UTC', 'HORA_UTC', 'FREC_MAX_F2', 'ALT_F',
       'ALT_MAX_DENS'],
      dtype='str')

========= Información general del dataset =========
<class 'pandas.DataFrame'>
RangeIndex: 379994 entries, 0 to 379993
Data columns (total 11 columns):
 #   Column        Non-Null Count   Dtype  
---  ------        --------------   -----  
 0   FECHA_CORTE   379994 non-null  int64  
 1   UBIGEO        379994 non-null  int64  
 2   ANIO          379994 non-null  int64  
 3   DEPARTAMENTO  379994 non-null  str    
 4   PROVINCIA     379994 non-null  str    
 5   DISTRITO      379994 non-null  str    
 6   FECHA_UTC     379994 non-null  int64  
 7   HORA_UTC      379994 non-null  int64  
 8   FREC_MAX_F2   298736 non-null  float64
 9   ALT_F         298736 non-null  float64
 10  ALT_MAX_DENS  284099 non-null  float64
dtypes: float64(3), int64(5), str(3)
memory usage: 31.9 MB
None

========= Estadísticas descriptivas del dataset =========
       FECHA_CORTE    UBIGEO           ANIO     FECHA_UTC       HORA_UTC    FREC_MAX_F2          ALT_F   ALT_MAX_DENS
count     379994.0  379994.0  379994.000000  3.799940e+05  379994.000000  298736.000000  298736.000000  284099.000000
mean    20260504.0  150118.0    2017.702382  2.017769e+07  117304.136907       7.409137     254.769277     319.422314
std            0.0       0.0       3.462153  3.461096e+04   69306.902384       2.426175      59.408942      59.885377
min     20260504.0  150118.0    2013.000000  2.013010e+07       0.000000       1.900000      66.000000     129.600000
25%     20260504.0  150118.0    2015.000000  2.015051e+07   55500.000000       5.620000     213.000000     275.500000
50%     20260504.0  150118.0    2017.000000  2.017111e+07  115400.000000       7.350000     237.000000     310.500000
75%     20260504.0  150118.0    2020.000000  2.020052e+07  180000.000000       9.100000     287.000000     354.400000
max     20260504.0  150118.0    2026.000000  2.026043e+07  235500.000000      15.990000     720.260000     719.620000
´´´


Los modelos de clasificacion están implementados en el archivo [clasificacion_completa.scala](../src/clasificacion_completa.scala) 

Los modelos de regresión están implementados en el archivo [regresion_completa-sin_XGBoost.scala](../src/regresion_completa-s-XGBoostRegressor.scala)

En los modelos de regresión, en la rúbrica se indica debemos de implementar los algoritmos SVM y MLP para realizar la regresión, pero Spark ML solo implementa LinearSVC y MultilayerPerceptronClassifier para la clasificacion, por lo que no posee implementaciones de estos algoritmos para SVR y MLP Regressor, por lo que se optó por reemplazar esos dos modelos de regresión con los modelos
- Random Forest Regressor
- Gradient-Boosted Trees (GBTRegressor)

Como manera alternativa.

Además para la regresión, para usar XGBoost debemos de descargarlo al acceder a spark, por lo que ingresaremos a spark-shell con el siguiente comando:
´´´bash
spark-shell --packages ml.dmlc:xgboost4j-spark_2.12:1.3.1
´´´

# Resultados
## Clasificacion
´´´bash
scala> :load clasificacion_completa.scala
Loading clasificacion_completa.scala...
import org.apache.spark.sql.functions._
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{VectorAssembler, StringIndexer, MinMaxScaler}
import org.apache.spark.ml.classification.{DecisionTreeClassifier, RandomForestClassifier, LogisticRegression, NaiveBayes}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.tuning.{ParamGridBuilder, CrossValidator}
dfRaw: org.apache.spark.sql.DataFrame = [FECHA_CORTE: int, UBIGEO: int ... 9 more fields]
dfClean: org.apache.spark.sql.DataFrame = [FECHA_CORTE: int, UBIGEO: int ... 9 more fields]
dfClass: org.apache.spark.sql.DataFrame = [FECHA_CORTE: int, UBIGEO: int ... 9 more fields]
labelIndexer: org.apache.spark.ml.feature.StringIndexer = strIdx_b89bf8c1f7e9
assembler: org.apache.spark.ml.feature.VectorAssembler = VectorAssembler: uid=vecAssembler_8b939a403f4d, handleInvalid=error, numInputCols=4
scaler: org.apache.spark.ml.feature.MinMaxScaler = minMaxScal_904340362c1c
trainingData: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] = [FECHA_CORTE: int, UBIGEO: int ... 9 more fields]
testData: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] = [FECHA_CORTE: int, UBIGEO: int ... 9 more fields]
dt: org.apache.spark.ml.classification.DecisionTreeClassifier = dtc_b87fab2737c7
rf: org.apache.spark.ml.classification.RandomForestClassifier = rfc_9a012916c175
lr: org.apache.spark.ml.classification.LogisticRegression = logreg_8e345fe6a0ad
nb: org.apache.spark.ml.classification.NaiveBayes = nb_069d5e4d016b
pipeDT: org.apache.spark.ml.Pipeline = pipeline_27b319784525
pipeRF: org.apache.spark.ml.Pipeline = pipeline_cdbf7c20162c
pipeLR: org.apache.spark.ml.Pipeline = pipeline_1f9585a608b8
pipeNB: org.apache.spark.ml.Pipeline = pipeline_73442f88ec52
evaluator: org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator = MulticlassClassificationEvaluator: uid=mcEval_d3433a3db6ef, metricName=f1, metricLabel=0.0, beta=1.0, eps=1.0E-15
gridDT: Array[org.apache.spark.ml.param.ParamMap] =
Array({
        dtc_b87fab2737c7-maxDepth: 5
}, {
        dtc_b87fab2737c7-maxDepth: 10
})
gridRF: Array[org.apache.spark.ml.param.ParamMap] =
Array({
        rfc_9a012916c175-numTrees: 10
}, {
        rfc_9a012916c175-numTrees: 20
})
gridLR: Array[org.apache.spark.ml.param.ParamMap] =
Array({
        logreg_8e345fe6a0ad-regParam: 0.1
}, {
        logreg_8e345fe6a0ad-regParam: 0.01
})
gridNB: Array[org.apache.spark.ml.param.ParamMap] =
Array({
        nb_069d5e4d016b-smoothing: 1.0
})
cvDT: org.apache.spark.ml.tuning.CrossValidator = cv_dda12547ea7e
cvRF: org.apache.spark.ml.tuning.CrossValidator = cv_4098afca8384
cvLR: org.apache.spark.ml.tuning.CrossValidator = cv_31786f6dbadb
cvNB: org.apache.spark.ml.tuning.CrossValidator = cv_eb65ed889ea1
Entrenando modelos de clasificación
Entrenando DECISION TREE
modelDT: org.apache.spark.ml.tuning.CrossValidatorModel = CrossValidatorModel: uid=cv_dda12547ea7e, bestModel=pipeline_27b319784525, numFolds=3
Entrenando RANDOM FOREST
modelRF: org.apache.spark.ml.tuning.CrossValidatorModel = CrossValidatorModel: uid=cv_4098afca8384, bestModel=pipeline_cdbf7c20162c, numFolds=3
Entrenando REGRESION LOGISTICA
modelLR: org.apache.spark.ml.tuning.CrossValidatorModel = CrossValidatorModel: uid=cv_31786f6dbadb, bestModel=pipeline_1f9585a608b8, numFolds=3
Entrenando NAIVE BAYES
modelNB: org.apache.spark.ml.tuning.CrossValidatorModel = CrossValidatorModel: uid=cv_eb65ed889ea1, bestModel=pipeline_73442f88ec52, numFolds=3
predDT: org.apache.spark.sql.DataFrame = [FECHA_CORTE: int, UBIGEO: int ... 15 more fields]
predRF: org.apache.spark.sql.DataFrame = [FECHA_CORTE: int, UBIGEO: int ... 15 more fields]
predLR: org.apache.spark.sql.DataFrame = [FECHA_CORTE: int, UBIGEO: int ... 15 more fields]
predNB: org.apache.spark.sql.DataFrame = [FECHA_CORTE: int, UBIGEO: int ... 14 more fields]
getMetrics: (predictions: org.apache.spark.sql.DataFrame, modelName: String)Unit

=== TABLA DE MÉTRICAS: CLASIFICACIÓN ===
Modelo               | Accuracy | Precision | Recall | F1-Score
---------------------------------------------------------------
Decision Tree        | 0.7685   | 0.7691    | 0.7685 | 0.7685
Random Forest        | 0.7418   | 0.7454    | 0.7418 | 0.7409
Regresión Logística  | 0.5729   | 0.5826    | 0.5729 | 0.5583
Naive Bayes          | 0.4386   | 0.4420    | 0.4386 | 0.3873
===============================================================

lrModelFinalClass: org.apache.spark.ml.classification.LogisticRegressionModel = LogisticRegressionModel: uid=logreg_8e345fe6a0ad, numClasses=3, numFeatures=4
lossHistoryLRClass: Array[Double] = Array(1.0371916103595906, 0.8892437094603101, 0.866203591954148, 0.8597180458716372, 0.856679856159765, 0.8557007568144392, 0.8555525923151555, 0.8555270169475461, 0.8555260231039171, 0.8555259731842708, 0.8555259662609936)

--- Listado de loss_clasificacion ---
loss_clasificacion = [1.0371916103595906, 0.8892437094603101, 0.866203591954148, 0.8597180458716372, 0.856679856159765, 0.8557007568144392, 0.8555525923151555, 0.8555270169475461, 0.8555260231039171, 0.8555259731842708, 0.8555259662609936]
´´´

En caso de WSL existen problemas para ejecutar el algoritmo de regresión XGBoost por lo que hemos creado una version sin ejecutar ese algoritmo el cual se llama [regresion_completa-s-XGBoost](../src/regresion_completa-s-XGBoostRegressor.scala) y otro que si incluye pero tendría que probarse directamente con spark en Windows [regresion_completa-c-XGBoost](../src/regresion_completa-s-XGBoostRegressor.scala)
## Regresion
´´´bash
scala> :load regresion_completa-s-XGBoostRegressor.scala
Loading regresion_completa-s-XGBoostRegressor.scala...
import org.apache.spark.sql.functions._
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{VectorAssembler, StandardScaler}
import org.apache.spark.ml.regression.{LinearRegression, RandomForestRegressor, GBTRegressor}
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.tuning.{ParamGridBuilder, CrossValidator}
import org.apache.spark.ml.PipelineModel
dfRaw: org.apache.spark.sql.DataFrame = [FECHA_CORTE: int, UBIGEO: int ... 9 more fields]
dfClean: org.apache.spark.sql.DataFrame = [FECHA_CORTE: int, UBIGEO: int ... 9 more fields]
dfReg: org.apache.spark.sql.DataFrame = [FECHA_CORTE: int, UBIGEO: int ... 9 more fields]
assembler: org.apache.spark.ml.feature.VectorAssembler = VectorAssembler: uid=vecAssembler_dd6dc4cb44f2, handleInvalid=error, numInputCols=4
scaler: org.apache.spark.ml.feature.StandardScaler = stdScal_0f562adb4d32
trainingData: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] = [FECHA_CORTE: int, UBIGEO: int ... 9 more fields]
testData: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] = [FECHA_CORTE: int, UBIGEO: int ... 9 more fields]
lr: org.apache.spark.ml.regression.LinearRegression = linReg_f72563d4fa80
rfr: org.apache.spark.ml.regression.RandomForestRegressor = rfr_6fdc583bea58
gbt: org.apache.spark.ml.regression.GBTRegressor = gbtr_876702d4d88d
pipeLR: org.apache.spark.ml.Pipeline = pipeline_e57901030745
pipeRFR: org.apache.spark.ml.Pipeline = pipeline_411790a480fd
pipeGBT: org.apache.spark.ml.Pipeline = pipeline_e2fc1b23d7f8
evaluatorReg: org.apache.spark.ml.evaluation.RegressionEvaluator = RegressionEvaluator: uid=regEval_7f1736db6350, metricName=rmse, throughOrigin=false
gridLR: Array[org.apache.spark.ml.param.ParamMap] =
Array({
        linReg_f72563d4fa80-regParam: 0.1
})
gridRFR: Array[org.apache.spark.ml.param.ParamMap] =
Array({
        rfr_6fdc583bea58-numTrees: 10
})
gridGBT: Array[org.apache.spark.ml.param.ParamMap] =
Array({
        gbtr_876702d4d88d-maxDepth: 3
})
cvLR: org.apache.spark.ml.tuning.CrossValidator = cv_8e0ab88abea9
cvRFR: org.apache.spark.ml.tuning.CrossValidator = cv_8474716b99b2
cvGBT: org.apache.spark.ml.tuning.CrossValidator = cv_ba9080ca9405
Entrenando Regresión Lineal...
26/06/22 15:34:12 WARN InstanceBuilder$NativeLAPACK: Failed to load implementation from:dev.ludovic.netlib.lapack.JNILAPACK
modelLR: org.apache.spark.ml.tuning.CrossValidatorModel = CrossValidatorModel: uid=cv_8e0ab88abea9, bestModel=pipeline_e57901030745, numFolds=2
Entrenando Random Forest...
modelRFR: org.apache.spark.ml.tuning.CrossValidatorModel = CrossValidatorModel: uid=cv_8474716b99b2, bestModel=pipeline_411790a480fd, numFolds=2
Entrenando GBT...
modelGBT: org.apache.spark.ml.tuning.CrossValidatorModel = CrossValidatorModel: uid=cv_ba9080ca9405, bestModel=pipeline_e2fc1b23d7f8, numFolds=2
predLR: org.apache.spark.sql.DataFrame = [FECHA_CORTE: int, UBIGEO: int ... 12 more fields]
predRFR: org.apache.spark.sql.DataFrame = [FECHA_CORTE: int, UBIGEO: int ... 12 more fields]
predGBT: org.apache.spark.sql.DataFrame = [FECHA_CORTE: int, UBIGEO: int ... 12 more fields]
getRegMetrics: (predictions: org.apache.spark.sql.DataFrame, modelName: String)Unit

=== TABLA DE MÉTRICAS: REGRESIÓN ===
Modelo               | RMSE   | MSE    | MAE    | R^2
---------------------------------------------------------
Linear Regression    | 1.9191 | 3.6828 | 1.5481 | 0.3802
Random Forest Reg.   | 1.3662 | 1.8666 | 1.0928 | 0.6859
GBT Regressor        | 1.4913 | 2.2239 | 1.1973 | 0.6257
XGBoost Regressor    | --- Omitido por limitación NAT en WSL2 ---
=========================================================

lrGrafico: org.apache.spark.ml.regression.LinearRegression = linReg_380df8d9b5d2
pipeGrafico: org.apache.spark.ml.Pipeline = pipeline_f25563b7c1dc
modeloGrafico: org.apache.spark.ml.PipelineModel = pipeline_f25563b7c1dc
historyGrafico: Array[Double] = Array(0.49999748927922333, 0.436759981611607, 0.31605871451812534, 0.3137551766030895, 0.3122770070808429, 0.3121787321778962, 0.3121480124157871, 0.3121480084787035, 0.3121480084201449, 0.312148008419911)

--- Lista de loss_regresion ---
loss_regresion = [0.49999748927922333, 0.436759981611607, 0.31605871451812534, 0.3137551766030895, 0.3122770070808429, 0.3121787321778962, 0.3121480124157871, 0.3121480084787035, 0.3121480084201449, 0.312148008419911]
´´´