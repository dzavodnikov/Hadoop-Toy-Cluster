# Hadoop and HBase Toy Cluster

This is Toy Cluster with Hadoop and HBase nodes. Created for experiments.

![Hadoop MapReduce](https://blog.sqlauthority.com/i/b/mapreduce.jpg)

It includes following services:

-   Apache [Zookeeper 3.9.3](https://zookeeper.apache.org/doc/r3.9.3/)
-   Apache [Hadoop 3.3.5](https://hadoop.apache.org/docs/r3.3.5/)
-   [Azkaban 4.0.0](https://azkaban.readthedocs.io/en/latest/)
-   Apache HBase 2.6.1 (Reference Guide exists only for [version 2.4](https://hbase.apache.org/2.4/book.html))

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
    -   [Azkaban](http://localhost:8081/) (username/password is `azkaban/azkaban`)
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

## Run code example

To execute local code examples run:

```sh
$ ./cmd_master1.sh
```

for connection to `master1` container (based on Oracle linux 9).

That compose file connect your current directory as `/project` and `~/.m2/` as `/root/.m2`,
so you can work with your local files and have all Maven settings and cache into the Docker container.

See scripts in [examples](./examples/) directory that help you to run code.

## License

Distributed under MIT License.
