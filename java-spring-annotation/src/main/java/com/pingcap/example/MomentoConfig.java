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

import com.google.code.ssm.Cache;
import com.google.code.ssm.CacheFactory;
import com.google.code.ssm.config.AbstractSSMConfiguration;
import com.google.code.ssm.providers.momento.MomentoAddressProvider;
import com.google.code.ssm.providers.momento.MomentoCacheClientFactory;
import com.google.code.ssm.providers.momento.MomentoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MomentoConfig
 *
 * @author Icemap
 * @date 2023/1/31
 */
@Configuration
public class MomentoConfig extends AbstractSSMConfiguration {
    @Autowired
    ApplicationConfig applicationConfig;

    @Bean
    @Override
    public CacheFactory defaultMemcachedClient() {
        final MomentoConfiguration conf = new MomentoConfiguration();
        conf.setConsistentHashing(true);
        conf.setDefaultTtl(300);
        conf.setCacheName(applicationConfig.getMomentoName());
        conf.setMomentoAuthToken(applicationConfig.getMomentoToken());
        final CacheFactory cacheFactory = new CacheFactory();
        cacheFactory.setCacheClientFactory(new MomentoCacheClientFactory());
        // Use a MomentoAddressProvider to be explicit
        cacheFactory.setAddressProvider(new MomentoAddressProvider());
        cacheFactory.setConfiguration(conf);

        return cacheFactory;
    }
}
