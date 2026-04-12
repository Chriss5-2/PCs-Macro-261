package SalesCountry;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class RegressionMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
    private static final Pattern NAC_PATTERN = Pattern.compile("Nac:([0-9]+(?:\\.[0-9]+)?)\\s*\\(Prom:([0-9]+(?:\\.[0-9]+)?|N/A)\\)");
    private static final Pattern DEF_PATTERN = Pattern.compile("Def:([0-9]+(?:\\.[0-9]+)?)\\s*\\(Prom:([0-9]+(?:\\.[0-9]+)?|N/A)\\)");

    public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
        String line = value.toString();
        // Solo procesamos la salida del Job 2: "YYYY-MES\tNac:... | Def:..."
        String[] parts = line.split("\t");
        if(parts.length<2) return;

        String fecha = parts[0].trim();
        String reporte = parts[1].trim();

        Matcher nacMatcher = NAC_PATTERN.matcher(reporte);
        Matcher defMatcher = DEF_PATTERN.matcher(reporte);
        if (!nacMatcher.find() || !defMatcher.find()) return;

        String nac = nacMatcher.group(1);
        String promNac = nacMatcher.group(2);
        String def = defMatcher.group(1);
        String promDef = defMatcher.group(2);

        // Valor serializado: fecha|nac|def|promNac|promDef
        String serie = fecha + "|" + nac + "|" + def + "|" + promNac + "|" + promDef;
        output.collect(new Text("MODELO_REGRESION"), new Text(serie));
    }
}