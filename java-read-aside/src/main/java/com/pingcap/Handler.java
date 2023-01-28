// Copyright 2022 PingCAP, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.pingcap;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mysql.cj.jdbc.MysqlDataSource;
import momento.sdk.SimpleCacheClient;
import momento.sdk.messages.CacheGetResponse;
import momento.sdk.messages.CacheGetStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handler
 *
 * @author Icemap
 * @date 2023/1/17
 */
public class Handler implements RequestHandler<HandlerRequest, String> {
    private LambdaLogger logger;

    private static final String MOMENTO_CACHE_NAME = "tidb_cache";
    private static final String MOMENTO_KEY = "tidb_time";
    private static final int MOMENTO_DEFAULT_ITEM_TTL_SECONDS = 60;

    @Override
    public String handleRequest(HandlerRequest req, Context context) {
        this.logger = context.getLogger();
        // Create a momento cache client, use the try-with-resource
        // grammar to ensure that the client is closed after use
        logger.log("create momento cache client");
        try (SimpleCacheClient client = SimpleCacheClient.builder(
                req.getMomentoAuthToken(), MOMENTO_DEFAULT_ITEM_TTL_SECONDS).build()) {
            // 1. Get cached time from the momento
            logger.log("get cached time from momento");
            String cachedTime = getTimeFromMomento(client);
            if (cachedTime != null) {
                return cachedTime;
            }

            // 2. If it's not exist cache in the momento, request the TiDB
            logger.log("request the TiDB");
            String databaseTime = getTimeFromTiDB(req.getTidbJDBCStr());
            if (databaseTime != null) {
                // 3. Set the time to the momento
                logger.log("set the time to the momento");
                setTimeToMomento(client, databaseTime);
                return databaseTime;
            }
        }

        return "Got something wrong";
    }

    private Boolean setTimeToMomento(SimpleCacheClient client, String tidbTime) {
        try {
            client.set(MOMENTO_CACHE_NAME, MOMENTO_KEY, tidbTime);
            return true;
        } catch (Exception e) {
            logger.log("Set time to momento failed, error message: " + e);
            return false;
        }
    }

    private String getTimeFromMomento(SimpleCacheClient client) {
        CacheGetResponse response = client.get(MOMENTO_CACHE_NAME, MOMENTO_KEY);
        if (response.status().equals(CacheGetStatus.MISS)) {
            return null;
        }

        return response.string().orElse(null);
    }


    private String getTimeFromTiDB(String jdbcStr) {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(jdbcStr);

        try (Connection connection = mysqlDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT NOW()");
            ResultSet res = preparedStatement.executeQuery();
            if(res.next()) {
                return res.getString(1);
            } else {
                logger.log("Select database time but get an empty result set.");
            }
        } catch (SQLException e) {
            logger.log("Select database time but get an error:"+ e);
        }

        return null;
    }
}
