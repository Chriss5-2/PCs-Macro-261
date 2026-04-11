package SalesCountry;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

public class SalesCountryReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, Text> {

	public void reduce(Text t_key, Iterator<IntWritable> values, OutputCollector<Text,Text> output, Reporter reporter) throws IOException {
                Text key = t_key;
                List<Integer> lista = new ArrayList<>();
		double sum = 0;
		while (values.hasNext()) {
			// replace type of value with the actual type of our value
			int val = values.next().get();
                        lista.add(val);
                        sum+=val;
			
		}
                
                int n = lista.size();
                if(n==0) return;
                
                // Promedio
                double avg = sum/n;
                
                // Mediana
                Collections.sort(lista);
                double mediana;
                if(n%2==0){
                    mediana = (lista.get(n/2) + lista.get(n/2-1))/2.0;
                }else{
                    mediana = lista.get(n/2);
                }
                
                // Desviación estándar
                double sumCua = 0;
                for(int x: lista){
                    sumCua += Math.pow(x-avg, 2);
                }
                double dsv = Math.sqrt(sumCua / n);
                
                String txt = String.format("Prom: %.2f | Mediana: %.2f | DesvStd: %.2f", avg, mediana, dsv);
                
		output.collect(key, new Text(txt));
	}
}
