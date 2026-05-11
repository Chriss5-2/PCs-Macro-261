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

        // ==========================================================
        // CONFIGURACIÓN DEL JOB: Red Neuronal (Sección 3)
        // ==========================================================
        Job jobRN = Job.getInstance(conf, "Red Neuronal - Gradiente Descendente");
        jobRN.setJarByClass(EPDriver.class);

        jobRN.setMapperClass(EPMapper.MapperRedNeuronal.class);
        jobRN.setReducerClass(EPReducer.ReducerRedNeuronal.class);

        jobRN.setOutputKeyClass(Text.class);
        jobRN.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(jobRN, inputDir);
        FileOutputFormat.setOutputPath(jobRN, outputDir);

        System.exit(jobRN.waitForCompletion(true) ? 0 : 1);
    }
}