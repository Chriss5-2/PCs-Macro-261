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
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class EPReducer {

    // ==========================================================
    // REDUCER JOB 1: Suma los casos y cuenta los registros
    // ==========================================================
    public static class ReducerJob1 extends Reducer<Text, Text, Text, Text> {
        private Text resultadoAcumulado = new Text();

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            long sumaCasos = 0;
            long conteoRegistros = 0;

            for (Text val : values) {
                String[] partes = val.toString().split(",");
                sumaCasos += (long) Double.parseDouble(partes[0].trim());
                conteoRegistros += (long) Double.parseDouble(partes[1].trim());
            }
            
            resultadoAcumulado.set(sumaCasos + "," + conteoRegistros);
            context.write(key, resultadoAcumulado);
        }
    }

    // ==========================================================
    // REDUCER JOB 2: Realiza la división exacta con decimales
    // ==========================================================
    public static class ReducerJob2 extends Reducer<Text, Text, Text, DoubleWritable> {
        private DoubleWritable promedioSalida = new DoubleWritable();

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context) {
            for (Text val : values) {
                try {
                    String strVal = val.toString().trim();
                    if (!strVal.contains(",")) continue;

                    String[] partes = strVal.split(",");
                    // Verificamos que existan las dos partes (Suma y Conteo)
                    if (partes.length >= 2) {
                        double suma = Double.parseDouble(partes[0].trim());
                        double conteo = Double.parseDouble(partes[1].trim());

                        if (conteo > 0) {
                            double promedio = suma / conteo;
                            double promedioRedondeado = Math.round(promedio * 1000.0) / 1000.0;
                            promedioSalida.set(promedioRedondeado);
                            context.write(key, promedioSalida);
                        }
                    }
                } catch (Exception e) {
                    // Si el número viene corrupto, lo ignora y sigue
                }
            }
        }
    }
}