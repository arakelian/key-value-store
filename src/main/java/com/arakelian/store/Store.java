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

package com.arakelian.store;

import java.util.Collection;
import java.util.List;

import com.arakelian.store.feature.HasId;

public interface Store<T extends HasId> {
    /**
     * Returns record with the given id, or null
     *
     * @param id
     *            record id
     * @return record with given id, or null if not found
     */
    public T get(String id);

    /**
     * Returns a list of all records with the given ids. If there are no matching records, this
     * method returns an empty list.
     *
     * @param ids
     *            list of ids
     * @return a list of all records with the given ids, or an empty list
     */
    public List<T> getAll(Collection<String> ids);

    /**
     * Returns a list of all records with the given ids. If there are no matching records, this
     * method returns an empty list.
     *
     * @param ids
     *            list of ids
     * @return a list of all records with the given ids, or an empty list
     */
    public List<T> getAll(String... ids);
}
