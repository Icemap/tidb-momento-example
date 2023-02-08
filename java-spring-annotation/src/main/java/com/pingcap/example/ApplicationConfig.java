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

package com.pingcap.example;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ApplicationConfig
 *
 * @author Icemap
 * @date 2023/1/31
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="application")
public class ApplicationConfig {

    private String momentoToken = "";
    private String momentoName = "";

    public String getMomentoToken() {
        return momentoToken;
    }

    public void setMomentoToken(String momentoToken) {
        this.momentoToken = momentoToken;
    }

    public String getMomentoName() {
        return momentoName;
    }

    public void setMomentoName(String momentoName) {
        this.momentoName = momentoName;
    }
}
