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
    // REDUCER SECCIÓN 2: Machine Learning - Clasificación KNN
    // ==========================================================
    public static class ReducerKNN extends Reducer<Text, Text, Text, Text> {
        @Override
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int K = 5;
            // Usamos un TreeMap para ordenar por distancia automáticamente
            TreeMap<Integer, List<String>> vecinosOrdenados = new TreeMap<>();

            for (Text val : values) {
                String[] partes = val.toString().split(",");
                if (partes.length == 2) {
                    int dist = Integer.parseInt(partes[0]);
                    String label = partes[1];
                    vecinosOrdenados.putIfAbsent(dist, new ArrayList<>());
                    vecinosOrdenados.get(dist).add(label);
                }
            }

            // Seleccionamos los K vecinos más cercanos
            List<String> topK = new ArrayList<>();
            int contador = 0;
            for (Map.Entry<Integer, List<String>> entry : vecinosOrdenados.entrySet()) {
                for (String label : entry.getValue()) {
                    topK.add(label);
                    contador++;
                    if (contador >= K) break;
                }
                if (contador >= K) break;
            }

            // Votación mayoritaria
            Map<String, Integer> votos = new HashMap<>();
            for (String label : topK) {
                votos.put(label, votos.getOrDefault(label, 0) + 1);
            }

            String prediccion = "";
            int maxVotos = -1;
            for (Map.Entry<String, Integer> entry : votos.entrySet()) {
                if (entry.getValue() > maxVotos) {
                    maxVotos = entry.getValue();
                    prediccion = entry.getKey();
                }
            }
            context.write(key, new Text("Diagnóstico Predicho: " + prediccion + " (Confianza: " + maxVotos + "/" + K + ")"));
        }
    }
}