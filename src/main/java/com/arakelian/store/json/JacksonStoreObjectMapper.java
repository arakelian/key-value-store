/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arakelian.store.json;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.arakelian.store.feature.HasId;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

public class JacksonStoreObjectMapper<T extends HasId> implements StoreObjectMapper<T> {
    private final Class<T> clazz;

    private final ObjectMapper objectMapper;

    public JacksonStoreObjectMapper(final Class<T> clazz, final ObjectMapper objectMapper) {
        Preconditions.checkArgument(clazz != null, "clazz must be non-null");
        Preconditions.checkArgument(objectMapper != null, "objectMapper must be non-null");
        this.clazz = clazz;
        this.objectMapper = objectMapper;
    }

    @Override
    public T readValue(final String value) throws IOException {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return StringUtils.isEmpty(value) ? null : objectMapper.readValue(value, clazz);
    }

    @Override
    public String writeValueAsString(final T value) throws IOException {
        Preconditions.checkArgument(value != null, "value must be non-null");
        return objectMapper.writeValueAsString(value);
    }
}
