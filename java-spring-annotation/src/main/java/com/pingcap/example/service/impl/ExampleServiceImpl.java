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

package com.pingcap.example.service.impl;

import com.google.code.ssm.api.ParameterValueKeyProvider;
import com.google.code.ssm.api.ReadThroughSingleCache;
import com.pingcap.example.dao.ExampleBean;
import com.pingcap.example.dao.ExampleRepository;
import com.pingcap.example.service.ExampleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ExampleServiceImpl implements ExampleService {
    @Autowired
    private ExampleRepository exampleRepository;

    @Override
    public ExampleBean setAndGetExample(Long id) {
        exampleRepository.save(new ExampleBean(id));
        exampleRepository.flush();
        return exampleRepository.findById(id).orElse(null);
    }

    @Override
    @ReadThroughSingleCache(namespace = "example", expiration = 300)
    public ExampleBean setAndGetWithCacheExample(@ParameterValueKeyProvider Long id) {
        return setAndGetExample(id);
    }
}
