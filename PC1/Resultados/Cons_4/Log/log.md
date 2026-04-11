C:\>hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_4
26/04/11 18:08:17 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 18:08:17 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 18:08:18 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
26/04/11 18:08:18 INFO mapred.FileInputFormat: Total input files to process : 1
26/04/11 18:08:18 INFO mapreduce.JobSubmitter: number of splits:2
26/04/11 18:08:18 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1775930134904_0013
26/04/11 18:08:18 INFO impl.YarnClientImpl: Submitted application application_1775930134904_0013
26/04/11 18:08:18 INFO mapreduce.Job: The url to track the job: http://LAPTOP-C7BQJMK9:8088/proxy/application_1775930134904_0013/
26/04/11 18:08:18 INFO mapreduce.Job: Running job: job_1775930134904_0013
26/04/11 18:08:26 INFO mapreduce.Job: Job job_1775930134904_0013 running in uber mode : false
26/04/11 18:08:26 INFO mapreduce.Job:  map 0% reduce 0%
26/04/11 18:08:32 INFO mapreduce.Job:  map 100% reduce 0%
26/04/11 18:08:39 INFO mapreduce.Job:  map 100% reduce 100%
26/04/11 18:08:39 INFO mapreduce.Job: Job job_1775930134904_0013 completed successfully
26/04/11 18:08:39 INFO mapreduce.Job: Counters: 49
        File System Counters
                FILE: Number of bytes read=3645644
                FILE: Number of bytes written=7703229
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
                HDFS: Number of bytes read=4375305
                HDFS: Number of bytes written=3561898
                HDFS: Number of read operations=9
                HDFS: Number of large read operations=0
                HDFS: Number of write operations=2
        Job Counters
                Launched map tasks=2
                Launched reduce tasks=1
                Data-local map tasks=2
                Total time spent by all maps in occupied slots (ms)=8704
                Total time spent by all reduces in occupied slots (ms)=3587
                Total time spent by all map tasks (ms)=8704
                Total time spent by all reduce tasks (ms)=3587
                Total vcore-milliseconds taken by all map tasks=8704
                Total vcore-milliseconds taken by all reduce tasks=3587
                Total megabyte-milliseconds taken by all map tasks=8912896
                Total megabyte-milliseconds taken by all reduce tasks=3673088
        Map-Reduce Framework
                Map input records=78930
                Map output records=41870
                Map output bytes=3561898
                Map output materialized bytes=3645650
                Input split bytes=194
                Combine input records=0
                Combine output records=0
                Reduce input groups=32
                Reduce shuffle bytes=3645650
                Reduce input records=41870
                Reduce output records=41870
                Spilled Records=83740
                Shuffled Maps =2
                Failed Shuffles=0
                Merged Map outputs=2
                GC time elapsed (ms)=129
                CPU time spent (ms)=3763
                Physical memory (bytes) snapshot=831557632
                Virtual memory (bytes) snapshot=987160576
                Total committed heap usage (bytes)=538968064
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
                Bytes Written=3561898