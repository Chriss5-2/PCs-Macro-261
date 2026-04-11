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
                if(!"CANT_COPIAS_EMITIDAS".equals(SingleCountryData[6].trim()) && "nacimiento".equals(SingleCountryData[2].trim().toLowerCase())){
                    try{
                        IntWritable num_actas = new IntWritable(Integer.parseInt(SingleCountryData[6]));
                        String text = SingleCountryData[0].trim()+"-"+SingleCountryData[5].trim();
                        output.collect(new Text(text), num_actas);
                    } catch(NumberFormatException e){
                        
                    }
                }
		//output.collect(new Text(SingleCountryData[0]+"-"+SingleCountryData[1]), one);
	}
}
