/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package epmaccro.epmacro;

/**
 *
 * @author chris
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class EPReducer {

    // ==========================================================
    // REDUCER SECCIÓN 3: Red Neuronal (Actualización de Pesos)
    // ==========================================================
    public static class ReducerRedNeuronal extends Reducer<Text, Text, Text, Text> {
        double learningRate = 0.01; // Tasa de aprendizaje (Alpha)
        
        @Override
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            double sumaGradientes = 0;
            int conteo = 0;

            for (Text val : values) {
                sumaGradientes += Double.parseDouble(val.toString());
                conteo++;
            }

            // Promedio del gradiente en este lote de datos
            double gradientePromedio = sumaGradientes / conteo;

            // Aplicar el concepto de Gradiente Descendente
            double ajuste = learningRate * gradientePromedio;

            // Formatear salida para el informe
            String resultado = String.format("GradPromedio: %.4f | Ajuste a aplicar (Alpha=0.01): -%.4f", gradientePromedio, ajuste);
            context.write(key, new Text(resultado));
        }
    }
}