C:\>hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_8
------ Iniciando Job1: Calculando Promedios...
26/04/11 23:58:20 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 23:58:20 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 23:58:20 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
26/04/11 23:58:20 INFO mapred.FileInputFormat: Total input files to process : 1
26/04/11 23:58:21 INFO mapreduce.JobSubmitter: number of splits:2
26/04/11 23:58:21 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1775930134904_0045
26/04/11 23:58:21 INFO impl.YarnClientImpl: Submitted application application_1775930134904_0045
26/04/11 23:58:21 INFO mapreduce.Job: The url to track the job: http://LAPTOP-C7BQJMK9:8088/proxy/application_1775930134904_0045/
26/04/11 23:58:21 INFO mapreduce.Job: Running job: job_1775930134904_0045
26/04/11 23:58:28 INFO mapreduce.Job: Job job_1775930134904_0045 running in uber mode : false
26/04/11 23:58:28 INFO mapreduce.Job:  map 0% reduce 0%
26/04/11 23:58:34 INFO mapreduce.Job:  map 100% reduce 0%
26/04/11 23:58:40 INFO mapreduce.Job:  map 100% reduce 100%
26/04/11 23:58:40 INFO mapreduce.Job: Job job_1775930134904_0045 completed successfully
26/04/11 23:58:40 INFO mapreduce.Job: Counters: 49
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
                Total time spent by all maps in occupied slots (ms)=8089
                Total time spent by all reduces in occupied slots (ms)=3481
                Total time spent by all map tasks (ms)=8089
                Total time spent by all reduce tasks (ms)=3481
                Total vcore-milliseconds taken by all map tasks=8089
                Total vcore-milliseconds taken by all reduce tasks=3481
                Total megabyte-milliseconds taken by all map tasks=8283136
                Total megabyte-milliseconds taken by all reduce tasks=3564544
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
                GC time elapsed (ms)=129
                CPU time spent (ms)=3605
                Physical memory (bytes) snapshot=816041984
                Virtual memory (bytes) snapshot=973574144
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
26/04/11 23:58:40 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 23:58:40 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 23:58:40 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
26/04/11 23:58:40 INFO mapred.FileInputFormat: Total input files to process : 2
26/04/11 23:58:40 INFO mapreduce.JobSubmitter: number of splits:3
26/04/11 23:58:40 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1775930134904_0046
26/04/11 23:58:40 INFO impl.YarnClientImpl: Submitted application application_1775930134904_0046
26/04/11 23:58:40 INFO mapreduce.Job: The url to track the job: http://LAPTOP-C7BQJMK9:8088/proxy/application_1775930134904_0046/
26/04/11 23:58:40 INFO mapreduce.Job: Running job: job_1775930134904_0046
26/04/11 23:58:53 INFO mapreduce.Job: Job job_1775930134904_0046 running in uber mode : false
26/04/11 23:58:53 INFO mapreduce.Job:  map 0% reduce 0%
26/04/11 23:59:00 INFO mapreduce.Job:  map 100% reduce 0%
26/04/11 23:59:07 INFO mapreduce.Job:  map 100% reduce 100%
26/04/11 23:59:07 INFO mapreduce.Job: Job job_1775930134904_0046 completed successfully
26/04/11 23:59:07 INFO mapreduce.Job: Counters: 49
        File System Counters
                FILE: Number of bytes read=2149385
                FILE: Number of bytes written=4847527
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
                HDFS: Number of bytes read=4376527
                HDFS: Number of bytes written=4995
                HDFS: Number of read operations=13
                HDFS: Number of large read operations=0
                HDFS: Number of write operations=2
        Job Counters
                Launched map tasks=3
                Launched reduce tasks=1
                Data-local map tasks=3
                Total time spent by all maps in occupied slots (ms)=15365
                Total time spent by all reduces in occupied slots (ms)=3568
                Total time spent by all map tasks (ms)=15365
                Total time spent by all reduce tasks (ms)=3568
                Total vcore-milliseconds taken by all map tasks=15365
                Total vcore-milliseconds taken by all reduce tasks=3568
                Total megabyte-milliseconds taken by all map tasks=15733760
                Total megabyte-milliseconds taken by all reduce tasks=3653632
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
                GC time elapsed (ms)=267
                CPU time spent (ms)=4323
                Physical memory (bytes) snapshot=1122697216
                Virtual memory (bytes) snapshot=1307525120
                Total committed heap usage (bytes)=732430336
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
------ Job 2 finalizado. Iniciando Job 3: Predicción...
26/04/11 23:59:07 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 23:59:07 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 23:59:07 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
26/04/11 23:59:07 INFO mapred.FileInputFormat: Total input files to process : 2
26/04/11 23:59:07 INFO mapreduce.JobSubmitter: number of splits:3
26/04/11 23:59:07 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1775930134904_0047
26/04/11 23:59:07 INFO impl.YarnClientImpl: Submitted application application_1775930134904_0047
26/04/11 23:59:07 INFO mapreduce.Job: The url to track the job: http://LAPTOP-C7BQJMK9:8088/proxy/application_1775930134904_0047/
26/04/11 23:59:07 INFO mapreduce.Job: Running job: job_1775930134904_0047
26/04/11 23:59:19 INFO mapreduce.Job: Job job_1775930134904_0047 running in uber mode : false
26/04/11 23:59:19 INFO mapreduce.Job:  map 0% reduce 0%
26/04/11 23:59:26 INFO mapreduce.Job:  map 100% reduce 0%
26/04/11 23:59:32 INFO mapreduce.Job:  map 100% reduce 100%
26/04/11 23:59:33 INFO mapreduce.Job: Job job_1775930134904_0047 completed successfully
26/04/11 23:59:33 INFO mapreduce.Job: Counters: 49
        File System Counters
                FILE: Number of bytes read=3468
                FILE: Number of bytes written=555029
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
                HDFS: Number of bytes read=4380408
                HDFS: Number of bytes written=784
                HDFS: Number of read operations=12
                HDFS: Number of large read operations=0
                HDFS: Number of write operations=2
        Job Counters
                Launched map tasks=3
                Launched reduce tasks=1
                Data-local map tasks=3
                Total time spent by all maps in occupied slots (ms)=15758
                Total time spent by all reduces in occupied slots (ms)=3333
                Total time spent by all map tasks (ms)=15758
                Total time spent by all reduce tasks (ms)=3333
                Total vcore-milliseconds taken by all map tasks=15758
                Total vcore-milliseconds taken by all reduce tasks=3333
                Total megabyte-milliseconds taken by all map tasks=16136192
                Total megabyte-milliseconds taken by all reduce tasks=3412992
        Map-Reduce Framework
                Map input records=78989
                Map output records=59
                Map output bytes=3344
                Map output materialized bytes=3480
                Input split bytes=302
                Combine input records=0
                Combine output records=0
                Reduce input groups=1
                Reduce shuffle bytes=3480
                Reduce input records=59
                Reduce output records=9
                Spilled Records=118
                Shuffled Maps =3
                Failed Shuffles=0
                Merged Map outputs=3
                GC time elapsed (ms)=225
                CPU time spent (ms)=2400
                Physical memory (bytes) snapshot=1105481728
                Virtual memory (bytes) snapshot=1295351808
                Total committed heap usage (bytes)=751304704
        Shuffle Errors
                BAD_ID=0
                CONNECTION=0
                IO_ERROR=0
                WRONG_LENGTH=0
                WRONG_MAP=0
                WRONG_REDUCE=0
        File Input Format Counters
                Bytes Read=4380106
        File Output Format Counters
                Bytes Written=784
------ Proceso completado