package SalesCountry;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

public class SalesCountryReducer2 extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

	public void reduce(Text t_key, Iterator<Text> values, OutputCollector<Text,Text> output, Reporter reporter) throws IOException {
                double promedioAnual = 0;
                List<String> listaProvincias = new ArrayList<>();
                
                while(values.hasNext()){
                    String val = values.next().toString();
                    if(val.startsWith("PROM:")){
                        promedioAnual = Double.parseDouble(val.split(":")[1]);
                    }else if(val.startsWith("DATA:")){
                        listaProvincias.add(val.substring(5));
                    }
                }
                
                for(String item : listaProvincias){
                    String[] datos = item.split(":");
                    String provincia = datos[0];
                    double cantidad = Double.parseDouble(datos[1]);
                    double desviacion = cantidad - promedioAnual;
                    
                    String txt = String.format("Cant: %.0f | PromAño: %.2f | Diferencia Decimal: %.2f", cantidad, promedioAnual, desviacion);
                    output.collect(new Text("Año: "+t_key+" - "+provincia), new Text(txt));
                }
	}
}
