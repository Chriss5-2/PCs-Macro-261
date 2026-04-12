package SalesCountry;
import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class SalesMapper2 extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
    public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
        String line = value.toString();
        // Saltamos cabecera y el archivo de promedios del Job 1 si se cruzara
        if (line.contains("CANT_COPIAS_EMITIDAS") || line.contains("\t")) return;

        String[] data = line.split(",");
        try {
            // Clave: 2023-FEBRERO
            String anioMes = data[0].trim() + "-" + data[1].trim();
            // Valor: ACTA:CANTIDAD (Ej: DEFUNCION:45)
            String actaCant = data[2].trim().toUpperCase() + ":" + data[6].trim();
            
            output.collect(new Text(anioMes), new Text(actaCant));
        } catch (Exception e) {}
    }
}