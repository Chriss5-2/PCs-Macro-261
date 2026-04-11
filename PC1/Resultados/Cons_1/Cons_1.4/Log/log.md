C:\>hadoop jar C:\PC-1.0.jar SalesCountry.SalesCountryDriver /input_pc1 /output_pc1/cons_1/cons_1.4
26/04/11 15:30:03 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 15:30:03 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
26/04/11 15:30:04 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
26/04/11 15:30:04 INFO mapred.FileInputFormat: Total input files to process : 1
26/04/11 15:30:04 INFO mapreduce.JobSubmitter: number of splits:2
26/04/11 15:30:04 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1775930134904_0005
26/04/11 15:30:04 INFO impl.YarnClientImpl: Submitted application application_1775930134904_0005
26/04/11 15:30:04 INFO mapreduce.Job: The url to track the job: http://LAPTOP-C7BQJMK9:8088/proxy/application_1775930134904_0005/
26/04/11 15:30:04 INFO mapreduce.Job: Running job: job_1775930134904_0005
26/04/11 15:30:13 INFO mapreduce.Job: Job job_1775930134904_0005 running in uber mode : false
26/04/11 15:30:13 INFO mapreduce.Job:  map 0% reduce 0%
26/04/11 15:30:20 INFO mapreduce.Job:  map 100% reduce 0%
26/04/11 15:30:27 INFO mapreduce.Job:  map 100% reduce 100%
26/04/11 15:30:27 INFO mapreduce.Job: Job job_1775930134904_0005 completed successfully
26/04/11 15:30:27 INFO mapreduce.Job: Counters: 49
        File System Counters
                FILE: Number of bytes read=2042082
                FILE: Number of bytes written=4496132
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
                HDFS: Number of bytes read=4375305
                HDFS: Number of bytes written=3730
                HDFS: Number of read operations=9
                HDFS: Number of large read operations=0
                HDFS: Number of write operations=2
        Job Counters
                Launched map tasks=2
                Launched reduce tasks=1
                Data-local map tasks=2
                Total time spent by all maps in occupied slots (ms)=10416
                Total time spent by all reduces in occupied slots (ms)=3976
                Total time spent by all map tasks (ms)=10416
                Total time spent by all reduce tasks (ms)=3976
                Total vcore-milliseconds taken by all map tasks=10416
                Total vcore-milliseconds taken by all reduce tasks=3976
                Total megabyte-milliseconds taken by all map tasks=10665984
                Total megabyte-milliseconds taken by all reduce tasks=4071424
        Map-Reduce Framework
                Map input records=78930
                Map output records=78929
                Map output bytes=1884218
                Map output materialized bytes=2042088
                Input split bytes=194
                Combine input records=0
                Combine output records=0
                Reduce input groups=59
                Reduce shuffle bytes=2042088
                Reduce input records=78929
                Reduce output records=59
                Spilled Records=157858
                Shuffled Maps =2
                Failed Shuffles=0
                Merged Map outputs=2
                GC time elapsed (ms)=148
                CPU time spent (ms)=4588
                Physical memory (bytes) snapshot=817029120
                Virtual memory (bytes) snapshot=970887168
                Total committed heap usage (bytes)=532152320
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
                Bytes Written=3730