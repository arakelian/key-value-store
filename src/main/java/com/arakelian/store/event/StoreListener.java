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

package com.arakelian.store.event;

import com.arakelian.store.Store;
import com.arakelian.store.feature.HasId;

/**
 * Listener that receives notification from a {@link Store} when values are stored or deleted.
 *
 * @param <T>
 *            type of value managed by the Dao
 */
public interface StoreListener<T extends HasId> {
    /**
     * Notifies listener that value with given id was deleted from data store. If data store doesn't
     * use soft deletes, this may be the last reference we have to this identifier.
     *
     * @param id
     *            uniquely identifies that value that was deleted
     */
    public void delete(String id);

    /**
     * Notifies listener that value was deleted from data store. If data store doesn't use soft
     * deletes, this may be the last reference we have to the value.
     *
     * @param value
     *            value that was deleted
     */
    public void delete(T value);

    /**
     * Notifies listener that value was put to data store.
     *
     * @param value
     *            value that was stored
     */
    public void put(T value);
}
