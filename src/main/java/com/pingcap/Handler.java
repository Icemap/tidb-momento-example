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
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * InsertHandler
 *
 * @author Icemap
 * @date 2023/1/17
 */
public class Handler implements RequestHandler<String, String> {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    @Override
    public String handleRequest(String jdbcStr, Context context) {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(jdbcStr);

        try (Connection connection = mysqlDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT NOW()");
            ResultSet res = preparedStatement.executeQuery();
            if(res.next()) {
                return res.getString(1);
            } else {
                logger.error("Select database time but get an empty result set.");
            }
        } catch (SQLException e) {
            logger.error("Select database time but get an error: {}", e.toString());
        }

        return null;
    }
}
