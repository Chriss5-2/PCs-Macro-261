package SalesCountry;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

public class SalesCountryReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, Text> {

	public void reduce(Text t_key, Iterator<IntWritable> values, OutputCollector<Text,Text> output, Reporter reporter) throws IOException {
		Text key = t_key;
                
                double sum = 0;
                double can = 12.0;
                while(values.hasNext()){
                    sum+=values.next().get();
                }
                double avgMensual = sum/can;
                output.collect(key, new Text(String.valueOf(avgMensual)));
	}
}
