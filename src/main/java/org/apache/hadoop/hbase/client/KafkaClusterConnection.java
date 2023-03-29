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

package org.apache.hadoop.hbase.client;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.RegionLocations;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.backoff.ClientBackoffPolicy;
import org.apache.hadoop.hbase.ipc.RpcControllerFactory;
import org.apache.hadoop.hbase.shaded.protobuf.generated.AdminProtos;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ClientProtos;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

/** Mock ClusterConnection to replicate to kafka. */
public class KafkaClusterConnection implements ClusterConnection {
    @Override
    public boolean isMasterRunning()
            throws MasterNotRunningException, ZooKeeperConnectionException {
        return false;
    }

    @Override
    public boolean isTableAvailable(TableName tableName, byte[][] splitKeys) throws IOException {
        return false;
    }

    @Override
    public boolean isTableEnabled(TableName tableName) throws IOException {
        return false;
    }

    @Override
    public boolean isTableDisabled(TableName tableName) throws IOException {
        return false;
    }

    @Override
    public TableState getTableState(TableName tableName) throws IOException {
        return null;
    }

    @Override
    public HRegionLocation locateRegion(TableName tableName, byte[] row) throws IOException {
        return null;
    }

    @Override
    public void cacheLocation(TableName tableName, RegionLocations location) {}

    @Override
    public void clearRegionCache(TableName tableName) {}

    @Override
    public void deleteCachedRegionLocation(HRegionLocation location) {}

    @Override
    public HRegionLocation relocateRegion(TableName tableName, byte[] row) throws IOException {
        return null;
    }

    @Override
    public RegionLocations relocateRegion(TableName tableName, byte[] row, int replicaId)
            throws IOException {
        return null;
    }

    @Override
    public void updateCachedLocations(
            TableName tableName,
            byte[] regionName,
            byte[] rowkey,
            Object exception,
            ServerName source) {}

    @Override
    public HRegionLocation locateRegion(byte[] regionName) throws IOException {
        return null;
    }

    @Override
    public List<HRegionLocation> locateRegions(TableName tableName) throws IOException {
        return null;
    }

    @Override
    public List<HRegionLocation> locateRegions(
            TableName tableName, boolean useCache, boolean offlined) throws IOException {
        return null;
    }

    @Override
    public RegionLocations locateRegion(
            TableName tableName, byte[] row, boolean useCache, boolean retry) throws IOException {
        return null;
    }

    @Override
    public RegionLocations locateRegion(
            TableName tableName, byte[] row, boolean useCache, boolean retry, int replicaId)
            throws IOException {
        return null;
    }

    @Override
    public MasterKeepAliveConnection getMaster() throws IOException {
        return null;
    }

    @Override
    public AdminProtos.AdminService.BlockingInterface getAdminForMaster() throws IOException {
        return null;
    }

    @Override
    public AdminProtos.AdminService.BlockingInterface getAdmin(ServerName serverName)
            throws IOException {
        return null;
    }

    @Override
    public ClientProtos.ClientService.BlockingInterface getClient(ServerName serverName)
            throws IOException {
        return null;
    }

    @Override
    public HRegionLocation getRegionLocation(TableName tableName, byte[] row, boolean reload)
            throws IOException {
        return null;
    }

    @Override
    public void clearCaches(ServerName sn) {}

    @Override
    public NonceGenerator getNonceGenerator() {
        return null;
    }

    @Override
    public AsyncProcess getAsyncProcess() {
        return null;
    }

    @Override
    public RpcRetryingCallerFactory getNewRpcRetryingCallerFactory(Configuration conf) {
        return null;
    }

    @Override
    public RpcRetryingCallerFactory getRpcRetryingCallerFactory() {
        return null;
    }

    @Override
    public RpcControllerFactory getRpcControllerFactory() {
        return null;
    }

    @Override
    public ConnectionConfiguration getConnectionConfiguration() {
        return null;
    }

    @Override
    public ServerStatisticTracker getStatisticsTracker() {
        return null;
    }

    @Override
    public ClientBackoffPolicy getBackoffPolicy() {
        return null;
    }

    @Override
    public MetricsConnection getConnectionMetrics() {
        return null;
    }

    @Override
    public boolean hasCellBlockSupport() {
        return false;
    }

    @Override
    public Configuration getConfiguration() {
        return null;
    }

    @Override
    public BufferedMutator getBufferedMutator(TableName tableName) throws IOException {
        return null;
    }

    @Override
    public BufferedMutator getBufferedMutator(BufferedMutatorParams params) throws IOException {
        return null;
    }

    @Override
    public RegionLocator getRegionLocator(TableName tableName) throws IOException {
        return null;
    }

    @Override
    public void clearRegionLocationCache() {}

    @Override
    public Admin getAdmin() throws IOException {
        return null;
    }

    @Override
    public void close() throws IOException {}

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public TableBuilder getTableBuilder(TableName tableName, ExecutorService pool) {
        return null;
    }

    @Override
    public void abort(String why, Throwable e) {}

    @Override
    public boolean isAborted() {
        return false;
    }
}
