C:\>hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_2/var_2
26/04/11 17:03:55 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 17:03:55 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 17:03:55 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
26/04/11 17:03:56 INFO mapred.FileInputFormat: Total input files to process : 1
26/04/11 17:03:56 INFO mapreduce.JobSubmitter: number of splits:2
26/04/11 17:03:56 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1775930134904_0010
26/04/11 17:03:56 INFO impl.YarnClientImpl: Submitted application application_1775930134904_0010
26/04/11 17:03:56 INFO mapreduce.Job: The url to track the job: http://LAPTOP-C7BQJMK9:8088/proxy/application_1775930134904_0010/
26/04/11 17:03:56 INFO mapreduce.Job: Running job: job_1775930134904_0010
26/04/11 17:04:03 INFO mapreduce.Job: Job job_1775930134904_0010 running in uber mode : false
26/04/11 17:04:03 INFO mapreduce.Job:  map 0% reduce 0%
26/04/11 17:04:11 INFO mapreduce.Job:  map 100% reduce 0%
26/04/11 17:04:17 INFO mapreduce.Job:  map 100% reduce 100%
26/04/11 17:04:17 INFO mapreduce.Job: Job job_1775930134904_0010 completed successfully
26/04/11 17:04:17 INFO mapreduce.Job: Counters: 49
        File System Counters
                FILE: Number of bytes read=827052
                FILE: Number of bytes written=2066084
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
                HDFS: Number of bytes read=4375305
                HDFS: Number of bytes written=295
                HDFS: Number of read operations=9
                HDFS: Number of large read operations=0
                HDFS: Number of write operations=2
        Job Counters
                Launched map tasks=2
                Launched reduce tasks=1
                Data-local map tasks=2
                Total time spent by all maps in occupied slots (ms)=10367
                Total time spent by all reduces in occupied slots (ms)=3895
                Total time spent by all map tasks (ms)=10367
                Total time spent by all reduce tasks (ms)=3895
                Total vcore-milliseconds taken by all map tasks=10367
                Total vcore-milliseconds taken by all reduce tasks=3895
                Total megabyte-milliseconds taken by all map tasks=10615808
                Total megabyte-milliseconds taken by all reduce tasks=3988480
        Map-Reduce Framework
                Map input records=78930
                Map output records=75186
                Map output bytes=676674
                Map output materialized bytes=827058
                Input split bytes=194
                Combine input records=0
                Combine output records=0
                Reduce input groups=6
                Reduce shuffle bytes=827058
                Reduce input records=75186
                Reduce output records=6
                Spilled Records=150372
                Shuffled Maps =2
                Failed Shuffles=0
                Merged Map outputs=2
                GC time elapsed (ms)=171
                CPU time spent (ms)=3589
                Physical memory (bytes) snapshot=812871680
                Virtual memory (bytes) snapshot=965206016
                Total committed heap usage (bytes)=537395200
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
                Bytes Written=295