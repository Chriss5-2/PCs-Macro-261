C:\>hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_6
------ Iniciando Job1: Calculando Promedios...
26/04/11 19:45:34 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 19:45:34 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 19:45:35 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
26/04/11 19:45:35 INFO mapred.FileInputFormat: Total input files to process : 1
26/04/11 19:45:35 INFO mapreduce.JobSubmitter: number of splits:2
26/04/11 19:45:35 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1775930134904_0016
26/04/11 19:45:35 INFO impl.YarnClientImpl: Submitted application application_1775930134904_0016
26/04/11 19:45:35 INFO mapreduce.Job: The url to track the job: http://LAPTOP-C7BQJMK9:8088/proxy/application_1775930134904_0016/
26/04/11 19:45:35 INFO mapreduce.Job: Running job: job_1775930134904_0016
26/04/11 19:45:42 INFO mapreduce.Job: Job job_1775930134904_0016 running in uber mode : false
26/04/11 19:45:42 INFO mapreduce.Job:  map 0% reduce 0%
26/04/11 19:45:50 INFO mapreduce.Job:  map 100% reduce 0%
26/04/11 19:45:56 INFO mapreduce.Job:  map 100% reduce 100%
26/04/11 19:45:57 INFO mapreduce.Job: Job job_1775930134904_0016 completed successfully
26/04/11 19:45:57 INFO mapreduce.Job: Counters: 49
        File System Counters
                FILE: Number of bytes read=868225
                FILE: Number of bytes written=2148928
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
                HDFS: Number of bytes read=4375305
                HDFS: Number of bytes written=139
                HDFS: Number of read operations=9
                HDFS: Number of large read operations=0
                HDFS: Number of write operations=2
        Job Counters
                Launched map tasks=2
                Launched reduce tasks=1
                Data-local map tasks=2
                Total time spent by all maps in occupied slots (ms)=9604
                Total time spent by all reduces in occupied slots (ms)=4214
                Total time spent by all map tasks (ms)=9604
                Total time spent by all reduce tasks (ms)=4214
                Total vcore-milliseconds taken by all map tasks=9604
                Total vcore-milliseconds taken by all reduce tasks=4214
                Total megabyte-milliseconds taken by all map tasks=9834496
                Total megabyte-milliseconds taken by all reduce tasks=4315136
        Map-Reduce Framework
                Map input records=78930
                Map output records=78929
                Map output bytes=710361
                Map output materialized bytes=868231
                Input split bytes=194
                Combine input records=0
                Combine output records=0
                Reduce input groups=6
                Reduce shuffle bytes=868231
                Reduce input records=78929
                Reduce output records=6
                Spilled Records=157858
                Shuffled Maps =2
                Failed Shuffles=0
                Merged Map outputs=2
                GC time elapsed (ms)=155
                CPU time spent (ms)=3573
                Physical memory (bytes) snapshot=822034432
                Virtual memory (bytes) snapshot=974647296
                Total committed heap usage (bytes)=537919488
        Shuffle Errors
                BAD_ID=0
                CONNECTION=0
                IO_ERROR=0
                WRONG_LENGTH=0
                WRONG_MAP=0
                WRONG_REDUCE=0
        File Input Format Counters
                Bytes Read=4375111
        File Output Format Counters
                Bytes Written=139
------ Job 1 finalizado. Iniciando Job 2: Enlace de datos...
26/04/11 19:45:57 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 19:45:57 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 19:45:57 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
26/04/11 19:45:57 INFO mapred.FileInputFormat: Total input files to process : 2
26/04/11 19:45:57 INFO mapreduce.JobSubmitter: number of splits:3
26/04/11 19:45:57 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1775930134904_0017
26/04/11 19:45:57 INFO impl.YarnClientImpl: Submitted application application_1775930134904_0017
26/04/11 19:45:57 INFO mapreduce.Job: The url to track the job: http://LAPTOP-C7BQJMK9:8088/proxy/application_1775930134904_0017/
26/04/11 19:45:57 INFO mapreduce.Job: Running job: job_1775930134904_0017
26/04/11 19:46:09 INFO mapreduce.Job: Job job_1775930134904_0017 running in uber mode : false
26/04/11 19:46:09 INFO mapreduce.Job:  map 0% reduce 0%
26/04/11 19:46:17 INFO mapreduce.Job:  map 100% reduce 0%
26/04/11 19:46:24 INFO mapreduce.Job:  map 100% reduce 100%
26/04/11 19:46:24 INFO mapreduce.Job: Job job_1775930134904_0017 completed successfully
26/04/11 19:46:24 INFO mapreduce.Job: Counters: 50
        File System Counters
                FILE: Number of bytes read=1867259
                FILE: Number of bytes written=4283251
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
                HDFS: Number of bytes read=4375550
                HDFS: Number of bytes written=6125149
                HDFS: Number of read operations=12
                HDFS: Number of large read operations=0
                HDFS: Number of write operations=2
        Job Counters
                Killed map tasks=1
                Launched map tasks=3
                Launched reduce tasks=1
                Data-local map tasks=3
                Total time spent by all maps in occupied slots (ms)=16926
                Total time spent by all reduces in occupied slots (ms)=4821
                Total time spent by all map tasks (ms)=16926
                Total time spent by all reduce tasks (ms)=4821
                Total vcore-milliseconds taken by all map tasks=16926
                Total vcore-milliseconds taken by all reduce tasks=4821
                Total megabyte-milliseconds taken by all map tasks=17332224
                Total megabyte-milliseconds taken by all reduce tasks=4936704
        Map-Reduce Framework
                Map input records=78936
                Map output records=78935
                Map output bytes=1709383
                Map output materialized bytes=1867271
                Input split bytes=300
                Combine input records=0
                Combine output records=0
                Reduce input groups=6
                Reduce shuffle bytes=1867271
                Reduce input records=78935
                Reduce output records=78929
                Spilled Records=157870
                Shuffled Maps =3
                Failed Shuffles=0
                Merged Map outputs=3
                GC time elapsed (ms)=317
                CPU time spent (ms)=6368
                Physical memory (bytes) snapshot=1118466048
                Virtual memory (bytes) snapshot=1300688896
                Total committed heap usage (bytes)=732954624
        Shuffle Errors
                BAD_ID=0
                CONNECTION=0
                IO_ERROR=0
                WRONG_LENGTH=0
                WRONG_MAP=0
                WRONG_REDUCE=0
        File Input Format Counters
                Bytes Read=4375250
        File Output Format Counters
                Bytes Written=6125149
------ Proceso completado