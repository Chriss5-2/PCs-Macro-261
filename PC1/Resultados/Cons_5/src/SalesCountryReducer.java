package SalesCountry;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

public class SalesCountryReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, Text> {

	public void reduce(Text t_key, Iterator<IntWritable> values, OutputCollector<Text,Text> output, Reporter reporter) throws IOException {
                Text key = t_key;
		int max = Integer.MIN_VALUE;
                int min = Integer.MAX_VALUE;
                boolean hasData = false;
                
                while(values.hasNext()){
                    int actual = values.next().get();
                    if(actual>max) max=actual;
                    if(actual<min) min=actual;
                    hasData=true;
                }
                
                if(hasData){
                    String txt = "Max: "+max+"| Min: "+min;
                    output.collect(key, new Text(txt));
                }
	}
}
