package SalesCountry;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

public class SalesMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
	//private final static IntWritable one = new IntWritable(1);
        public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {

		String valueString = value.toString();
		String[] SingleCountryData = valueString.split(",");
                if(!"CANT_COPIAS_EMITIDAS".equals(SingleCountryData[6].trim())){
                    try{
                        String clave = SingleCountryData[0].trim()+"-"+SingleCountryData[1].trim();
                        String valor = SingleCountryData[5].trim()+":"+SingleCountryData[6].trim();
                        output.collect(new Text(clave), new Text(valor));
                    } catch(NumberFormatException e){
                        
                    }
                }
		//output.collect(new Text(SingleCountryData[0]+"-"+SingleCountryData[1]), one);
	}
}
