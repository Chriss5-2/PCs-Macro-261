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
                        IntWritable num_cop = new IntWritable(Integer.parseInt(SingleCountryData[6].trim()));
                        String txt = SingleCountryData[0].trim()+"-"+SingleCountryData[2].trim();
                        //(2022-NACIMIENTO, 4)
                        output.collect(new Text(txt), num_cop);
                    } catch(NumberFormatException e){
                        
                    }
                }
		//output.collect(new Text(SingleCountryData[0]+"-"+SingleCountryData[1]), one);
	}
}
