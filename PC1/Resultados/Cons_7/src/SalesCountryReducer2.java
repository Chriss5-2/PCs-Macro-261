package SalesCountry;
import java.io.*;
import java.util.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.fs.*;

public class SalesCountryReducer2 extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
    private double promNac = 0;
    private double promDef = 0;

    private HashMap<String, Double> mapaModelos = new HashMap<>();

    @Override
    public void configure(JobConf job) {
        try {
            Path path = new Path("/output_pc1/cons_7_mid/part-00000");
            FileSystem fs = FileSystem.get(job);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\t"); 
                // Guarda por ejemplo: "2023-NACIMIENTO" -> 5400.5
                mapaModelos.put(p[0], Double.parseDouble(p[1]));
            }
            br.close();
        } catch (Exception e) { }
    }

    public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
        int totalNac = 0; int totalDef = 0;
        String anio = key.toString().split("-")[0]; // Extraemos el año de "2023-ENERO"

        while (values.hasNext()) {
            String[] data = values.next().toString().split(":");
            if (data[0].equals("NACIMIENTO")) totalNac += Integer.parseInt(data[1]);
            else if (data[0].equals("DEFUNCION")) totalDef += Integer.parseInt(data[1]);
        }

        // Obtenemos los promedios específicos para ese año desde el mapa
        double pNac = mapaModelos.getOrDefault(anio + "-NACIMIENTO", 0.0);
        double pDef = mapaModelos.getOrDefault(anio + "-DEFUNCION", 0.0);

        // Clasificación comparativa
        String perfil = "ESTABLE";
        if (totalNac < pNac && totalDef > pDef) perfil = "MES_CRITICO";
        else if (totalNac > pNac) perfil = "ALTA_NATALIDAD";
        else if (totalDef > pDef) perfil = "ALTA_MORTALIDAD";

        String reporte = String.format("Nac:%d (Prom:%s) | Def:%d (Prom:%s) | PERFIL:%s", 
                                        totalNac, (pNac == 0 ? "N/A" : String.format("%.1f", pNac)), 
                                        totalDef, (pDef == 0 ? "N/A" : String.format("%.1f", pDef)), perfil);

        output.collect(key, new Text(reporte));
    }
}