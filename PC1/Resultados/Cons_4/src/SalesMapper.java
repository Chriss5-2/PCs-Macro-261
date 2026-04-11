package SalesCountry;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

public class SalesMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
	private final static IntWritable one = new IntWritable(1);
        private int nroMes(String mes){
            switch(mes.toUpperCase().trim()){
                case "ENERO": return 1;
                case "FEBRERO": return 2;
                case "MARZO": return 3;
                case "ABRIL": return 4;
                case "MAYO": return 5;
                case "JUNIO": return 6;
                case "JULIO": return 7;
                case "AGOSTO": return 8;
                case "SETIEMBRE":
                case "SEPTIEMBRE": return 9;
                case "OCTUBRE": return 10;
                case "NOVIEMBRE": return 11;
                case "DICIEMBRE": return 12;
                default: return 0;
            }
        }

        public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {

		String valueString = value.toString();
		String[] SingleCountryData = valueString.split(",");
                if(!"CANT_COPIAS_EMITIDAS".equals(SingleCountryData[6].trim())){
                    try{
                        int anio = Integer.parseInt(SingleCountryData[0].trim());
                        int nmes = nroMes(SingleCountryData[1].trim());
                        
                        int fechaRegistro = (anio*100)+nmes;
                        
                        int inicio=202304;
                        int fin=202511;
                        if(fechaRegistro>=inicio && fechaRegistro<=fin){
                            output.collect(new Text("Dentro del Rango("+anio+"-"+SingleCountryData[1].trim()+"):"), value);
                        }
                    }catch(NumberFormatException e){}
                }
		//output.collect(new Text(SingleCountryData[0]+"-"+SingleCountryData[1]), one);
	}
}
