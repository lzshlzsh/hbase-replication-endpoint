/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tencent.cloud.oceanus.hbase.replication;

import com.alibaba.fastjson2.JSON;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.ClusterConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.KafkaClusterConnection;
import org.apache.hadoop.hbase.replication.regionserver.HBaseInterClusterReplicationEndpoint;
import org.apache.hadoop.hbase.replication.regionserver.ReplicationSinkManager;
import org.apache.hadoop.hbase.wal.WAL.Entry;
import org.apache.yetus.audience.InterfaceAudience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * A {@link org.apache.hadoop.hbase.replication.ReplicationEndpoint} implementation for replicating
 * to Kafka.
 */
@InterfaceAudience.Private
public class KafkaInterClusterReplicationEndpoint extends HBaseInterClusterReplicationEndpoint {
    private static final Logger LOG =
            LoggerFactory.getLogger(KafkaInterClusterReplicationEndpoint.class);

    //////////////////////////////////////////////////////////////////////////////////////////////////
    // mock hbase inter cluster replication specific methods of HBaseReplicationEndpoint

    @Override
    protected void doStart() {
        try {
            notifyStarted();
        } catch (Exception e) {
            notifyFailed(e);
        }
    }

    @Override
    public synchronized UUID getPeerUUID() {
        return UUID.randomUUID();
    }

    @Override
    public synchronized List<ServerName> getRegionServers() {
        return Collections.emptyList();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    // mock hbase inter cluster replication specific methods of HBaseInterClusterReplicationEndpoint

    @Override
    protected Connection createConnection(Configuration conf) throws IOException {
        final Configuration fakeConfig = new Configuration(conf);
        fakeConfig.set(
                ClusterConnection.HBASE_CLIENT_CONNECTION_IMPL,
                KafkaClusterConnection.class.getName());
        return super.createConnection(conf);
    }

    @Override
    protected ReplicationSinkManager createReplicationSinkManager(Connection conn) {
        return new KafkaReplicationSinkManager(
                (ClusterConnection) conn,
                this.ctx.getPeerId(),
                this,
                HBaseConfiguration.create(ctx.getConfiguration()));
    }

    @Override
    public void init(Context context) throws IOException {
        super.init(context);
        // TODO: get kafka configuration, such as boostrap-server and topic-name
    }

    @Override
    protected int replicateEntries(List<Entry> entries, int batchIndex, int timeout)
            throws IOException {

        // TODO: produce WAL to kafka topic
        entries.forEach(entry -> LOG.info("{}", JSON.toJSONString(entry)));

        return batchIndex;
    }
}
