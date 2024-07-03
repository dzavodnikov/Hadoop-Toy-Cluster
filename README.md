# Hadoop and HBase Toy Cluster

This is Toy Cluster with Hadoop and HBase nodes. Created for experiments.

![Hadoop MapReduce](https://blog.sqlauthority.com/i/b/mapreduce.jpg)

It includes following services:

-   Apache [Zookeeper 3.9.4](https://zookeeper.apache.org/doc/r3.9.4/)
-   Apache [Hadoop 3.4.2](https://hadoop.apache.org/docs/r3.4.2/)
-   Apache HBase 2.6.4 (Reference Guide exists only for [version 2.4](https://hbase.apache.org/2.4/book.html))

Hadoop requires [Java 11](https://cwiki.apache.org/confluence/display/HADOOP/Hadoop+Java+Versions).

## Run

Just execute:

```sh
$ ./start.sh
```

Current state of all nodes will be saved at [data](./data) directory.

Directory [logs](./logs) will be created automatically and will contain logs for all services.

Shared data between host machine and cluster nodes can be done through [shared](./shared) directory.

To stop:

```sh
$ ./stop.sh
```

_Note:_ If you want clean-up all working data just execute `$ ./clean.sh` script.

## Web UIs

-   `toy-master1`:
    -   HDFS [NameNode](http://localhost:9870/explorer.html)
    -   YARN [ResourceManager](http://localhost:8088/cluster/nodes/)
    -   MapReduce JobHistory [Server](http://localhost:19888/jobhistory/app/)
    -   HBase Master [Web UI](http://localhost:16010/)
    -   HBase Thrift2 [Web UI](http://localhost:9091/)
    -   HBase REST [Web UI](http://localhost:8091/)
-   `toy-worker1`:
    -   YARN [NodeManager](http://localhost:8142/)
    -   HBase RegionServer [Web UI](http://localhost:16130/)
-   `toy-worker2`:
    -   YARN [NodeManager](http://localhost:8242/)
    -   HBase RegionServer [Web UI](http://localhost:16230/)
-   `toy-worker3`:
    -   YARN [NodeManager](http://localhost:8342/)
    -   HBase RegionServer [Web UI](http://localhost:16330/)

## License

Distributed under MIT License.
