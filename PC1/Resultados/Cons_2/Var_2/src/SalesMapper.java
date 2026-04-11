package SalesCountry;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

public class SalesMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
	private final static IntWritable one = new IntWritable(1);

        public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

		String valueString = value.toString();
		String[] SingleCountryData = valueString.split(",");
                if(!"CANT_COPIAS_EMITIDAS".equals(SingleCountryData[6].trim())){
                    try{
                        int num_cop = Integer.parseInt(SingleCountryData[6].trim());
                        // Aplicando Filtro de Outliers
                        // Lo hago entre el rango de 0 a 100, ya que el promedio no supera esa cantidad
                        // Así que usaremos solo los datos que estén en ese rango
                        if(num_cop>0 && num_cop<=100){
                            String text = SingleCountryData[0].trim();
                            output.collect(new Text(text), new IntWritable(num_cop));
                        }
                    } catch(NumberFormatException e){
                        
                    }
                }
		//output.collect(new Text(SingleCountryData[0]+"-"+SingleCountryData[1]), one);
	}
}
