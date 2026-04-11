C:\>hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_2
26/04/11 16:39:35 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 16:39:35 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 16:39:36 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
26/04/11 16:39:36 INFO mapred.FileInputFormat: Total input files to process : 1
26/04/11 16:39:36 INFO mapreduce.JobSubmitter: number of splits:2
26/04/11 16:39:36 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1775930134904_0008
26/04/11 16:39:36 INFO impl.YarnClientImpl: Submitted application application_1775930134904_0008
26/04/11 16:39:36 INFO mapreduce.Job: The url to track the job: http://LAPTOP-C7BQJMK9:8088/proxy/application_1775930134904_0008/
26/04/11 16:39:36 INFO mapreduce.Job: Running job: job_1775930134904_0008
26/04/11 16:39:44 INFO mapreduce.Job: Job job_1775930134904_0008 running in uber mode : false
26/04/11 16:39:44 INFO mapreduce.Job:  map 0% reduce 0%
26/04/11 16:39:51 INFO mapreduce.Job:  map 100% reduce 0%
26/04/11 16:39:58 INFO mapreduce.Job:  map 100% reduce 100%
26/04/11 16:39:58 INFO mapreduce.Job: Job job_1775930134904_0008 completed successfully
26/04/11 16:39:58 INFO mapreduce.Job: Counters: 49
        File System Counters
                FILE: Number of bytes read=1437874
                FILE: Number of bytes written=3287710
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
                HDFS: Number of bytes read=4375305
                HDFS: Number of bytes written=3435
                HDFS: Number of read operations=9
                HDFS: Number of large read operations=0
                HDFS: Number of write operations=2
        Job Counters
                Launched map tasks=2
                Launched reduce tasks=1
                Data-local map tasks=2
                Total time spent by all maps in occupied slots (ms)=9516
                Total time spent by all reduces in occupied slots (ms)=4146
                Total time spent by all map tasks (ms)=9516
                Total time spent by all reduce tasks (ms)=4146
                Total vcore-milliseconds taken by all map tasks=9516
                Total vcore-milliseconds taken by all reduce tasks=4146
                Total megabyte-milliseconds taken by all map tasks=9744384
                Total megabyte-milliseconds taken by all reduce tasks=4245504
        Map-Reduce Framework
                Map input records=78930
                Map output records=78929
                Map output bytes=1280010
                Map output materialized bytes=1437880
                Input split bytes=194
                Combine input records=0
                Combine output records=0
                Reduce input groups=59
                Reduce shuffle bytes=1437880
                Reduce input records=78929
                Reduce output records=59
                Spilled Records=157858
                Shuffled Maps =2
                Failed Shuffles=0
                Merged Map outputs=2
                GC time elapsed (ms)=183
                CPU time spent (ms)=4090
                Physical memory (bytes) snapshot=802766848
                Virtual memory (bytes) snapshot=958349312
                Total committed heap usage (bytes)=540016640
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
                Bytes Written=3435