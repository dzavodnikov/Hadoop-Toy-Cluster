# Hadoop and HBase Toy Cluster

This is Toy Cluster with Hadoop and HBase nodes. Created for experiments.

![Hadoop MapReduce](https://blog.sqlauthority.com/i/b/mapreduce.jpg)

It includes following services:

-   Apache [Zookeeper 3.9.0](https://zookeeper.apache.org/doc/r3.9.0/)
-   Apache [Hadoop 3.3.6](https://hadoop.apache.org/docs/r3.3.6/)
-   [Azkaban 4.0.0](https://azkaban.readthedocs.io/en/latest/)
-   Apache HBase 2.6.0 (Reference Guide exists only for [version 2.4](https://hbase.apache.org/2.4/book.html))

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

## Run code examples on cluster

1.  Go to [examples](./examples) folder.

2.  Deploy examples:

    ```sh
    $ ./deploy.sh
    ```

    > Note:
    > This script build examples and upload them to local Maven cache (`~/.m2/repository`).
    > Then it download them and push to the cluster.

3.  Run some file:

    ```sh
    $ ./mapreduce.sh
    ```

    or

    ```sh
    $ ./hbase.sh
    ```

## Run code examples in Azkaban

1.  Go to [examples](./examples) folder.

2.  Deploy examples:

    ```sh
    $ ./deploy.sh
    ```

    > Note:
    > This script build examples and upload them to local Maven cache (`~/.m2/repository`).
    > Then it download them and push to the cluster.

3.  Upload input data:

    ```sh
    $ ./wc_input.sh
    ```

4.  Go to [azkaban](./azkaban) directory and create ZIP archive:

    ```sh
    $ ./azkaban-arch.sh
    ```

    It will generate `azkaban-jobs.zip`.

5.  Go to [Azkaban](http://localhost:8081/) and login (username/password are `azkaban/azkaban`).

6.  Click on "Create Project" button.

7.  Input "Name" and "Description". After that click on "Create Project" button.

8.  Click on "Upload" and select generated ZIP-file. After that click on "Upload" button.

9.  Click on "Execute Flow". Update "Flow Parameters" if needed
    (you are can see all parameters in `shared.properties` file).

10. Click on "Execute" button, then press "Continue" button.

    > Note:
    > To get "Job Logs" click on "Job List" and find "Details" link in latest column of the table.

11. Download output data:

    ```sh
    $ ./wc_output.sh
    ```

## License

Distributed under MIT License.
