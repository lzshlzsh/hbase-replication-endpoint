# 背景

  目前，Flink 社区 [1] 和云厂商 [2] 对于 HBase 作为数据源时，只支持批模式和维表，暂无增量数据读取方案。HBase WAL 日志类似 MySQL binlog，记录了 HBase 表的变更数据，但没有提供直接读取 WAL 日志的接口，Flink 直接读取 WAL 日志暂时不可行。这里采用自定义 HBase ReplicationEndpoint 的方案，引入中间存储，间接实现 Flink 读取 HBase 增量数据。

  HBase 可以基于 WAL 复制 Cluster Replication[3] 功能将实时写入的数据从主集群复制到备份集群，且 HBase 支持自定义 ReplicationEndpoint，因此我们可以通过自定义 ReplicationEndpoint 来把增量数据推送至其它存储（例如 Kafka），业界有相关的实践[4]。WAL 推送 Kafa 后，我们可以通过 flink-connector-kafka 来消费 kafka 中的 WAL 日志，从而实现读取 HBase 增量数据的目的。

# 自定义 HBase ClusterReplicationEndpoint

本示例的自定义 ClusterReplicationEndpoint 参考自[4]，并做了简化，通过直接继承自 HBaseInterClusterReplicationEndpoint，并 mock 一些方法来实现。这里暂未实现写 kafka 的功能，只是在日志中输出 WAL 信息。

1. 编译项目

编译后得到 hbase-replication-endpoint-1.0.0.jar 包，放入 `$HBASE_HOME/lib` 目录下。

```Shell
mvn pacakge
```

2. HBase 表创建表

hbase shell 中执行

```Shell
create 'person', {NAME=>'info',REPLICATION_SCOPE => '1'}
```

> 注意：REPLICATION_SCOPE 属于设置为 1，表示开启复制

3. 创建复制链路

hbase shell 中执行

```JavaScript
add_peer '111', CLUSTER_KEY => 'localhost:2181:/fake_hbase', ENDPOINT_CLASSNAME => 'com.tencent.cloud.oceanus.hbase.replication.KafkaInterClusterReplicationEndpoint', SERIAL => true, CONFIG => {"bootstrap-servers" => "127.0.0.1:9092", "topic" => "test" }, TABLE_CFS => { "person" => []}
```

> 注意：
>
> - HBase 2 必须设置 CLUSTER_KEY，可设置为一个假的 zookeeper 路径
> - SERIAL 设置为 true，开启串行复制

4. 往示例表中写入数据、删除数据，观察 RegionServer 日志输出

hbase shell 中执行

```Shell
put 'person', '1', 'info:c1', 'v1'
delete 'person', '1', 'info:c1'
```

日志输出如下

![img](https://pmj352yxkl.feishu.cn/space/api/box/stream/download/asynccode/?code=MTM1YTdhOWRjYTA2NWI5NzU2ZmRjMDE2YzBkNTRhMjZfcmJ6cmc1cmoyb3VtNndBZFBXMTJhWkhxMmJlOEN3bEpfVG9rZW46VnpOUGJGWmNvb2tzbVl4eTVxWGNQY1VJbktoXzE2ODAwNzU2NTk6MTY4MDA3OTI1OV9WNA)

# 参考资料

- [1] Flink [HBase SQL Connector](https://nightlies.apache.org/flink/flink-docs-master/docs/connectors/table/hbase/)
- [2] [阿里云数据库HBase](https://help.aliyun.com/document_detail/607370.htm?spm=a2c4g.11186623.0.0.47e8168fX7m4vi)
- [3] HBase [Cluster Replication](https://hbase.apache.org/book.html#_cluster_replication)
- [4] [HBase WAL日志数据实时增量推送至Kafka](https://cloud.tencent.com/developer/article/1988529)