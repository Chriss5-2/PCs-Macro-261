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
    // MAPPER JOB 1: Extrae Distrito + Diagnostico y el TOTAL
    // ==========================================================
    public static class MapperJob1 extends Mapper<LongWritable, Text, Text, Text> {
        private Text claveCompuesta = new Text();
        private Text valorSalida = new Text();

        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String linea = value.toString().trim();
            if (linea.isEmpty()) return;

            String[] campos = linea.split(";");

            // Validamos que tenga las columnas necesarias
            if (campos.length >= 13) {
                try {
                    // 🔥 CAMBIO AQUÍ: Ahora extraemos Diagnóstico (7) y Grupo de Edad (9)
                    String diagnostico = campos[7].trim();
                    String grupoEdad = campos[9].trim();
                    String totalCasosStr = campos[12].trim();

                    // Intentamos convertir a número para saltar la cabecera
                    double casos = Double.parseDouble(totalCasosStr);

                    if (!diagnostico.isEmpty() && !grupoEdad.isEmpty()) {
                        // La nueva llave compuesta será "Enfermedad | Grupo de Edad"
                        claveCompuesta.set(diagnostico + " | " + grupoEdad);
                        valorSalida.set(casos + ",1");
                        context.write(claveCompuesta, valorSalida);
                    }
                } catch (NumberFormatException e) {
                    // Ignora cabeceras de forma segura
                }
            }
        }
    }

    // ==========================================================
    // MAPPER JOB 2: Pasa los datos intermedios al cálculo final
    // ==========================================================
    // ==========================================================
    // MAPPER JOB 2: Pasa los datos intermedios al cálculo final
    // ==========================================================
    public static class MapperJob2 extends Mapper<LongWritable, Text, Text, Text> {
        private Text clave = new Text();
        private Text valor = new Text();

        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String linea = value.toString().trim();
            if (linea.isEmpty()) return;

            int indiceTab = linea.indexOf('\t');
            if (indiceTab != -1) {
                clave.set(linea.substring(0, indiceTab).trim());
                valor.set(linea.substring(indiceTab + 1).trim());
                context.write(clave, valor);
            } else {
                int ultimoEspacio = linea.lastIndexOf(' ');
                if (ultimoEspacio != -1) {
                    clave.set(linea.substring(0, ultimoEspacio).trim());
                    valor.set(linea.substring(ultimoEspacio + 1).trim());
                    context.write(clave, valor);
                }
            }
        }
    }
}