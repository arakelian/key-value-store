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
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.arakelian.store.feature.HasId;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

public abstract class AbstractStore<T extends HasId> implements Store<T> {
    protected final StoreConfig<T> config;

    public AbstractStore(final StoreConfig<T> config) {
        this.config = config;
    }

    /*
     * (non-Javadoc)
     * @see com.arakelian.dao.Dao#getAll(java.util.Collection)
     */
    @Override
    public List<T> getAll(final Collection<String> ids) {
        if (ids == null || ids.size() == 0) {
            return Collections.<T> emptyList();
        }

        // we need a List of ids to partition
        final List<String> idList;
        if (ids instanceof List) {
            idList = (List<String>) ids;
        } else {
            idList = Lists.newArrayList(ids);
        }

        // fetch records in groups of X
        List<T> result = null;
        for (final List<String> partition : Lists.partition(idList, config.getPartitionSize())) {
            result = doGetAll(result, partition);
        }

        // make sure we always return non-null list
        if (result == null) {
            return Collections.<T> emptyList();
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.arakelian.dao.Dao#getAll(java.lang.String[])
     */
    @Override
    public List<T> getAll(final String... ids) {
        if (ids == null || ids.length == 0) {
            return Collections.<T> emptyList();
        }

        // delegate to internal method that partitions the list into smaller groups and aggregates
        // the result
        return getAll(Lists.newArrayList(ids));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this) //
                .add("table", config.getTable()) //
                .add("clazz", config.getClazz()) //
                .toString();
    }

    /**
     * Internal method that gets all values with the given ids, and appends them to the given list.
     *
     * @param result
     *            list to return results in, or null if a list has not yet been created
     * @param ids
     *            list of ids, guaranteed not to contain nulls or empty values
     * @return existing list or new list if existing list was null
     */
    protected abstract List<T> doGetAll(List<T> result, final List<String> ids);

    /**
     * Returns an array of non-empty ids from the given list of ids or values.
     *
     * @param idsOrValues
     *            list of ids and/or values
     * @return array of non-empty ids
     */
    protected Object[] idsOf(final List<?> idsOrValues) {
        // convert list to array that we can mutate
        final Object[] ids = idsOrValues.toArray();

        // mutate array to contain only non-empty ids
        int length = 0;
        for (int i = 0; i < ids.length;) {
            final Object p = ids[i++];
            if (p instanceof HasId) {
                // only use values with ids that are non-empty
                final String id = ((HasId) p).getId();
                if (!StringUtils.isEmpty(id)) {
                    ids[length++] = id;
                }
            } else if (p instanceof String) {
                // only use ids that are non-empty
                final String id = p.toString();
                if (!StringUtils.isEmpty(id)) {
                    ids[length++] = id;
                }
            } else if (p != null) {
                throw new StoreException("Invalid id or value of type " + p);
            }
        }

        // no ids in array
        if (length == 0) {
            return null;
        }

        // some ids in array
        if (length != ids.length) {
            final Object[] tmp = new Object[length];
            System.arraycopy(ids, 0, tmp, 0, length);
            return tmp;
        }

        // array was full
        return ids;
    }
}
