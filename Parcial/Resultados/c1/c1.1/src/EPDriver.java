/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package epmaccro.epmacro;

/**
 *
 * @author chris
 */
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class EPDriver {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Uso: EPDriver <ruta_entrada_HDFS> <ruta_salida_HDFS>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        Path inputDir = new Path(args[0]);
        // Carpeta temporal intermedia en HDFS
        Path tempDir = new Path("/temp_mr_parcial_c1"); 
        Path outputDir = new Path(args[1]);

        // ==========================================
        // CONFIGURACIÓN Y LANZAMIENTO DEL JOB 1
        // ==========================================
        Job job1 = Job.getInstance(conf, "Consulta 1 - Fase 1: Suma y Conteo");
        job1.setJarByClass(EPDriver.class);
        
        // Apuntamos a las clases internas estáticas
        job1.setMapperClass(EPMapper.MapperJob1.class);
        job1.setReducerClass(EPReducer.ReducerJob1.class);
        
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(Text.class);
        
        FileInputFormat.addInputPath(job1, inputDir);
        FileOutputFormat.setOutputPath(job1, tempDir);

        // Esperamos a que el Job 1 termine exitosamente antes de lanzar el 2
        if (job1.waitForCompletion(true)) {
            
            // ==========================================
            // CONFIGURACIÓN Y LANZAMIENTO DEL JOB 2
            // ==========================================
            Job job2 = Job.getInstance(conf, "Consulta 1 - Fase 2: Promedio Decimal");
            job2.setJarByClass(EPDriver.class);
            
            job2.setMapperClass(EPMapper.MapperJob2.class);
            job2.setReducerClass(EPReducer.ReducerJob2.class);
            
            job2.setMapOutputKeyClass(Text.class);
            job2.setMapOutputValueClass(Text.class);
            
            job2.setOutputKeyClass(Text.class);
            job2.setOutputValueClass(DoubleWritable.class);
            
            // La entrada del Job 2 es la carpeta temporal del Job 1
            FileInputFormat.addInputPath(job2, tempDir);
            FileOutputFormat.setOutputPath(job2, outputDir);

            // Retorna 0 si todo sale bien, o 1 si falla
            System.exit(job2.waitForCompletion(true) ? 0 : 1);
        } else {
            System.exit(1);
        }
    }
}