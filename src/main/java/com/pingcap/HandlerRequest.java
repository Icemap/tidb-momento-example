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

/**
 * HandlerRequest
 *
 * @author Icemap
 * @date 2023/1/18
 */
public class HandlerRequest {
    private String tidbJDBCStr;
    private String momentoAuthToken;

    public String getTidbJDBCStr() {
        return tidbJDBCStr;
    }

    public void setTidbJDBCStr(String tidbJDBCStr) {
        this.tidbJDBCStr = tidbJDBCStr;
    }

    public String getMomentoAuthToken() {
        return momentoAuthToken;
    }

    public void setMomentoAuthToken(String momentoAuthToken) {
        this.momentoAuthToken = momentoAuthToken;
    }
}
