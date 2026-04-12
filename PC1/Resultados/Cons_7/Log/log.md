C:\>hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_7
------ Iniciando Job1: Calculando Promedios...
26/04/11 22:52:46 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 22:52:46 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 22:52:47 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
26/04/11 22:52:47 INFO mapred.FileInputFormat: Total input files to process : 1
26/04/11 22:52:47 INFO mapreduce.JobSubmitter: number of splits:2
26/04/11 22:52:47 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1775930134904_0031
26/04/11 22:52:47 INFO impl.YarnClientImpl: Submitted application application_1775930134904_0031
26/04/11 22:52:47 INFO mapreduce.Job: The url to track the job: http://LAPTOP-C7BQJMK9:8088/proxy/application_1775930134904_0031/
26/04/11 22:52:47 INFO mapreduce.Job: Running job: job_1775930134904_0031
26/04/11 22:52:55 INFO mapreduce.Job: Job job_1775930134904_0031 running in uber mode : false
26/04/11 22:52:55 INFO mapreduce.Job:  map 0% reduce 0%
26/04/11 22:53:03 INFO mapreduce.Job:  map 100% reduce 0%
26/04/11 22:53:10 INFO mapreduce.Job:  map 100% reduce 100%
26/04/11 22:53:10 INFO mapreduce.Job: Job job_1775930134904_0031 completed successfully
26/04/11 22:53:11 INFO mapreduce.Job: Counters: 49
        File System Counters
                FILE: Number of bytes read=1711520
                FILE: Number of bytes written=3835518
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
                HDFS: Number of bytes read=4375305
                HDFS: Number of bytes written=558
                HDFS: Number of read operations=9
                HDFS: Number of large read operations=0
                HDFS: Number of write operations=2
        Job Counters
                Launched map tasks=2
                Launched reduce tasks=1
                Data-local map tasks=2
                Total time spent by all maps in occupied slots (ms)=12978
                Total time spent by all reduces in occupied slots (ms)=4351
                Total time spent by all map tasks (ms)=12978
                Total time spent by all reduce tasks (ms)=4351
                Total vcore-milliseconds taken by all map tasks=12978
                Total vcore-milliseconds taken by all reduce tasks=4351
                Total megabyte-milliseconds taken by all map tasks=13289472
                Total megabyte-milliseconds taken by all reduce tasks=4455424
        Map-Reduce Framework
                Map input records=78930
                Map output records=78929
                Map output bytes=1553656
                Map output materialized bytes=1711526
                Input split bytes=194
                Combine input records=0
                Combine output records=0
                Reduce input groups=19
                Reduce shuffle bytes=1711526
                Reduce input records=78929
                Reduce output records=19
                Spilled Records=157858
                Shuffled Maps =2
                Failed Shuffles=0
                Merged Map outputs=2
                GC time elapsed (ms)=160
                CPU time spent (ms)=4105
                Physical memory (bytes) snapshot=807329792
                Virtual memory (bytes) snapshot=963006464
                Total committed heap usage (bytes)=539492352
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
                Bytes Written=558
------ Job 1 finalizado. Iniciando Job 2: Clasificando...
26/04/11 22:53:11 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 22:53:11 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 22:53:11 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
26/04/11 22:53:11 INFO mapred.FileInputFormat: Total input files to process : 2
26/04/11 22:53:11 INFO mapreduce.JobSubmitter: number of splits:3
26/04/11 22:53:11 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1775930134904_0032
26/04/11 22:53:11 INFO impl.YarnClientImpl: Submitted application application_1775930134904_0032
26/04/11 22:53:11 INFO mapreduce.Job: The url to track the job: http://LAPTOP-C7BQJMK9:8088/proxy/application_1775930134904_0032/
26/04/11 22:53:11 INFO mapreduce.Job: Running job: job_1775930134904_0032
26/04/11 22:53:24 INFO mapreduce.Job: Job job_1775930134904_0032 running in uber mode : false
26/04/11 22:53:24 INFO mapreduce.Job:  map 0% reduce 0%
26/04/11 22:53:33 INFO mapreduce.Job:  map 100% reduce 0%
26/04/11 22:53:39 INFO mapreduce.Job:  map 100% reduce 100%
26/04/11 22:53:39 INFO mapreduce.Job: Job job_1775930134904_0032 completed successfully
26/04/11 22:53:39 INFO mapreduce.Job: Counters: 50
        File System Counters
                FILE: Number of bytes read=2149385
                FILE: Number of bytes written=4847503
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
                HDFS: Number of bytes read=4376527
                HDFS: Number of bytes written=4995
                HDFS: Number of read operations=13
                HDFS: Number of large read operations=0
                HDFS: Number of write operations=2
        Job Counters
                Killed map tasks=1
                Launched map tasks=3
                Launched reduce tasks=1
                Data-local map tasks=3
                Total time spent by all maps in occupied slots (ms)=20267
                Total time spent by all reduces in occupied slots (ms)=3541
                Total time spent by all map tasks (ms)=20267
                Total time spent by all reduce tasks (ms)=3541
                Total vcore-milliseconds taken by all map tasks=20267
                Total vcore-milliseconds taken by all reduce tasks=3541
                Total megabyte-milliseconds taken by all map tasks=20753408
                Total megabyte-milliseconds taken by all reduce tasks=3625984
        Map-Reduce Framework
                Map input records=78949
                Map output records=78929
                Map output bytes=1991521
                Map output materialized bytes=2149397
                Input split bytes=300
                Combine input records=0
                Combine output records=0
                Reduce input groups=59
                Reduce shuffle bytes=2149397
                Reduce input records=78929
                Reduce output records=59
                Spilled Records=157858
                Shuffled Maps =3
                Failed Shuffles=0
                Merged Map outputs=3
                GC time elapsed (ms)=380
                CPU time spent (ms)=5134
                Physical memory (bytes) snapshot=1108393984
                Virtual memory (bytes) snapshot=1299136512
                Total committed heap usage (bytes)=735051776
        Shuffle Errors
                BAD_ID=0
                CONNECTION=0
                IO_ERROR=0
                WRONG_LENGTH=0
                WRONG_MAP=0
                WRONG_REDUCE=0
        File Input Format Counters
                Bytes Read=4375669
        File Output Format Counters
                Bytes Written=4995
------ Proceso completado
