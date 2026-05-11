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
    // MAPPER SECCIÓN 2: Machine Learning - Clasificación KNN
    // ==========================================================
    public static class MapperKNN extends Mapper<LongWritable, Text, Text, Text> {
        private Text outputKey = new Text("Perfil_Paciente_Objetivo");
        private Text outputValue = new Text();

        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String linea = value.toString().trim();
            if (linea.isEmpty()) return;
            String[] campos = linea.split(";");
            
            if (campos.length >= 13) {
                try {
                    // Definimos nuestro paciente a clasificar: TACNA, M, 18-29a
                    String targetDistrito = "TACNA";
                    String targetGenero = "M";
                    String targetEdad = "18-29a";

                    String distrito = campos[4].trim();
                    String diagnostico = campos[7].trim();
                    String genero = campos[8].trim();
                    String edad = campos[9].trim();

                    // Saltar cabecera
                    if (diagnostico.equalsIgnoreCase("Descripcion_Item")) return;

                    // Distancia de Hamming: contamos cuántos atributos NO coinciden
                    int distancia = 0;
                    if (!distrito.equals(targetDistrito)) distancia++;
                    if (!genero.equals(targetGenero)) distancia++;
                    if (!edad.equals(targetEdad)) distancia++;

                    // Enviamos: Clave fija, Valor: "distancia,diagnóstico"
                    outputValue.set(distancia + "," + diagnostico);
                    context.write(outputKey, outputValue);
                } catch (Exception e) {}
            }
        }
    }
}