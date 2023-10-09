# Hadoop and HBase Toy Cluster

This is Toy Cluster with Hadoop and HBase nodes. Created for experiments.

![Hadoop MapReduce](https://blog.sqlauthority.com/i/b/mapreduce.jpg)

It includes following services:

-   [Apache Zookeeper 3.9.0](https://zookeeper.apache.org/doc/r3.9.0/)
-   [Apache Hadoop 3.3.6](https://hadoop.apache.org/docs/r3.3.6/)
-   [Azkaban 4.0.0](https://azkaban.readthedocs.io/)
-   [Apache HBase 2.4](https://hbase.apache.org/2.4/book.html)

> Note:
> Current versions of [Hadoop](https://cwiki.apache.org/confluence/display/HADOOP/Hadoop+Java+Versions/) and
> HBase uses [Java 8](https://hbase.apache.org/book.html#basic.prerequisites).

## Run

Just execute:

```sh
    % docker-compose up -d
```

To stop:

```sh
    % docker-compose down
```

Current state of all nodes will be saved at [data](/data) directory.

## Web UIs

-   `toy-master1`:
    -   Hadoop HDFS [NameNode](http://localhost:9870/explorer.html)
    -   Hadoop YARN [ResourceManager](http://localhost:8088/cluster/nodes/)
    -   Hadoop [MapReduce JobHistory Server](http://localhost:19888/jobhistory/app)
    -   Hadoop [Azkaban](http://localhost:8081/) (username/password is `azkaban/azkaban`)
    -   HBase Master [Web UI](http://localhost:16010/)
    -   HBase Thrift2 [Web UI](http://localhost:9091/)
    -   HBase REST [Web UI](http://localhost:8091/)
-   `toy-worker1`:
    -   Hadoop YARN [NodeManager](http://localhost:8043/)
    -   HBase RegionServer [Web UI](http://localhost:16031/)
-   `toy-worker2`:
    -   Hadoop YARN [NodeManager](http://localhost:8044/)
    -   HBase RegionServer [Web UI](http://localhost:16032/)
-   `toy-worker3`:
    -   Hadoop YARN [NodeManager](http://localhost:8045/)
    -   HBase RegionServer [Web UI](http://localhost:16033/)

## Debug

-   [logs](/logs) directory will be created automatically and will contain logs for Hadoop/HBase services.
-   To check what processes are runs connect to container (for example `toy-master1`) and execute:
    ```sh
        % docker exec -it toy-master1 ps aux
    ```
-   To check ports that listened by systems connect to container (for example `toy-worker1`) and execute:
    ```sh
        % docker exec -it toy-worker1 netstat -lntup
    ```
-   To access MapReduce from console use:
    ```sh
        % docker exec -it toy-master1 hadoop -help
    ```
-   To kill application that hands up find it's job ID first:
    ```sh
        % docker exec -it toy-master1 hadoop job -list
    ```
    and kill the job:
    ```sh
        % docker exec -it toy-master1 hadoop job -kill <JOB_ID>
    ```
-   To access HDFS from console use:
    ```sh
        % docker exec -it toy-master1 hdfs dfs -help
    ```
-   To access HBase from console use:
    ```sh
        % docker exec -it toy-master1 hbase shell
    ```
    Type `exit` to close connection.

## License

Distributed under MIT License.
