package SalesCountry;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

public class SalesCountryReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

	public void reduce(Text t_key, Iterator<Text> values, OutputCollector<Text,Text> output, Reporter reporter) throws IOException {
            HashMap<String, Integer> totalesPorProvincia = new HashMap<>();
            while(values.hasNext()){
                String[] datos = values.next().toString().split(":");
                try{    
                    String provincia = datos[0];
                    int cantidad = Integer.parseInt(datos[1]);

                    totalesPorProvincia.put(provincia, totalesPorProvincia.getOrDefault(provincia,0)+cantidad);
                }catch(NumberFormatException e){}
            }
            
            String provinciaGanadora ="";
            int minCopias=Integer.MAX_VALUE;
            
            for(Map.Entry<String, Integer> entry : totalesPorProvincia.entrySet()){
                if(entry.getValue() < minCopias){
                    minCopias = entry.getValue();
                    provinciaGanadora = entry.getKey();
                }
            }
            
            output.collect(t_key,new Text("La provincia con más trámites fue " + provinciaGanadora+" con "+minCopias));
	}
}
