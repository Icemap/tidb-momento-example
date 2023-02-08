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

package com.pingcap.example.dao;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * it's core entity in hibernate
 * @Table appoint to table name
 */
@Entity
@Table(name = "example")
public class ExampleBean implements Serializable {
    public ExampleBean() {
    }

    public ExampleBean(Long id) {
        this.id = id;
    }

    @PreUpdate
    public void preUpdate() {
        updateTime = new Date();
    }

    @PrePersist
    public void prePersist() {
        updateTime = new Date();
    }

    @Id
    private Long id;

    @Column(name = "update_time")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
