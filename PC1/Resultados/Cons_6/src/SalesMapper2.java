package SalesCountry;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

public class SalesMapper2 extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {

		String valueString = value.toString();
		
                if (valueString.contains("CANT_COPIAS_EMITIDAS")) return;
		
                if (valueString.contains("\t")) { 
                    // Es la salida del Job 1 (Formato: Año \t Promedio)
                    String[] parts = valueString.split("\t");
                    // Emitimos Clave: Año | Valor: Marcador PROM + el promedio
                    output.collect(new Text(parts[0].trim()), new Text("PROM:" + parts[1].trim()));
                    
                } else{
                    String[] parts = valueString.split(",");
                    if(parts.length>6){
                        String anio = parts[0].trim();
                        String provincia = parts[5].trim();
                        String cantidad = parts[6].trim();
                        
                        output.collect(new Text(anio), new Text("DATA:"+provincia+":"+cantidad));
                    }
                }
	}
}
