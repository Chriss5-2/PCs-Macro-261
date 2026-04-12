
package SalesCountry;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class SalesCountryDriver {
	public static void main(String[] args) {
		JobClient my_client = new JobClient();
		// Create a configuration object for the job
		JobConf job1 = new JobConf(SalesCountryDriver.class);

		// Set a name of the Job
		job1.setJobName("Job1_PromedioAnual");

		// Specify data type of output key and value
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(Text.class);
                job1.setMapOutputValueClass(IntWritable.class);
                
                // Almacenar resultados en ruta temporal
                Path tempPath = new Path("/output_pc1/cons_8_mid");
                FileInputFormat.setInputPaths(job1, new Path(args[0]));
                FileOutputFormat.setOutputPath(job1, tempPath);
                
                // Specify names of Mapper and Reducer Class		
                job1.setMapperClass(SalesCountry.SalesMapper.class);
		job1.setReducerClass(SalesCountry.SalesCountryReducer.class);

		// Specify formats of the data type of Input and output
		job1.setInputFormat(TextInputFormat.class);
		job1.setOutputFormat(TextOutputFormat.class);
                
                my_client.setConf(job1);
                // Ejecutar job 1 y esperar que termine
                System.out.println("------ Iniciando Job1: Calculando Promedios...");
                try {
			// Run the job 
			JobClient.runJob(job1);
		} catch (Exception e) {
			e.printStackTrace();
		}
                
                // Configurando job2
                JobConf job2 = new JobConf(SalesCountryDriver.class);

		// Set a name of the Job
		job2.setJobName("Job2_Calculo Final");

		// Specify data type of output key and value
		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Text.class);
                job2.setMapOutputValueClass(Text.class);
                
                job2.setMapperClass(SalesCountry.SalesMapper2.class);
		job2.setReducerClass(SalesCountry.SalesCountryReducer2.class);
                
                String entradasJob2 = args[0]+","+"/output_pc1/cons_8_mid";
                Path tempPath2 = new Path("/output_pc1/cons_8_final");
                FileInputFormat.setInputPaths(job2, entradasJob2);
		FileOutputFormat.setOutputPath(job2, tempPath2);
     
		my_client.setConf(job2);
                // Ejecutar job 1 y esperar que termine
                System.out.println("------ Job 1 finalizado. Iniciando Job 2: Clasificando...");
                
                try {
			// Run the job 
			JobClient.runJob(job2);
		} catch (Exception e) {
			e.printStackTrace();
		}

                
                // --- Configurando Job 3: Regresión y Predicción ---
                JobConf job3 = new JobConf(SalesCountryDriver.class);
                job3.setJobName("Job3_Regresion_2026");

                job3.setOutputKeyClass(Text.class);
                job3.setOutputValueClass(Text.class);

                job3.setMapperClass(SalesCountry.RegressionMapper.class);
                job3.setReducerClass(SalesCountry.RegressionReducer.class);

                // Entrada: La salida del Job 2 | Salida: Carpeta final cons_8
                //String entradasJob3 = args[0]+","+"/output_pc1/cons_8_final";
                FileInputFormat.setInputPaths(job3, args[0] + "," + "/output_pc1/cons_8_final"); 
                FileOutputFormat.setOutputPath(job3, new Path(args[1]));
                
                
                my_client.setConf(job3);
                // Ejecutar job 1 y esperar que termine
                System.out.println("------ Job 2 finalizado. Iniciando Job 3: Predicción...");
                
                try {
			// Run the job 
			JobClient.runJob(job3);
		} catch (Exception e) {
			e.printStackTrace();
		}

                
		System.out.println("------ Proceso completado");

		
		
	}
}
