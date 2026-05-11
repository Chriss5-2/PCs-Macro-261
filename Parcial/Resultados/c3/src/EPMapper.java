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
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class EPMapper {

    // ==========================================================
    // MAPPER SECCIÓN 3: Red Neuronal (Gradiente Descendente - 1 Epoch)
    // ==========================================================
    public static class MapperRedNeuronal extends Mapper<LongWritable, Text, Text, Text> {
        // Pesos iniciales simulados de la Neurona
        double w1 = 0.5; // Peso para el Género
        double w2 = 0.5; // Peso para el Distrito
        double b = 0.1;  // Sesgo (Bias)

        @Override
        public void map(LongWritable key, Text value, Context context) {
            try {
                String linea = value.toString().trim();
                if (linea.isEmpty()) return;
                String[] campos = linea.split(";");
                
                if (campos.length >= 13) {
                    String distrito = campos[4].trim();
                    String genero = campos[8].trim();
                    String totalCasosStr = campos[12].trim();

                    // Ignorar cabecera
                    if (distrito.equalsIgnoreCase("Distrito_RH_Paciente")) return; 

                    // 1. Convertir a valores numéricos (Entradas de la Neurona)
                    double x1 = genero.equalsIgnoreCase("M") ? 1.0 : 0.0;
                    double x2 = distrito.equalsIgnoreCase("TACNA") ? 1.0 : 0.0;
                    double y_real = Double.parseDouble(totalCasosStr);

                    // 2. Forward Propagation (Predicción Lineal)
                    double y_pred = (w1 * x1) + (w2 * x2) + b;

                    // 3. Cálculo del Error
                    double error = y_pred - y_real;

                    // 4. Cálculo de Gradientes (Derivadas parciales)
                    double gradW1 = error * x1;
                    double gradW2 = error * x2;
                    double gradB = error;

                    // Emitir gradientes para que el Reducer los promedie
                    context.write(new Text("Peso_W1_Genero"), new Text(String.valueOf(gradW1)));
                    context.write(new Text("Peso_W2_Distrito"), new Text(String.valueOf(gradW2)));
                    context.write(new Text("Sesgo_Bias"), new Text(String.valueOf(gradB)));
                }
            } catch (Exception e) {}
        }
    }
}