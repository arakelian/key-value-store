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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.arakelian.store.event.StoreListener;
import com.arakelian.store.feature.HasId;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

public abstract class AbstractMutableStore<T extends HasId> extends AbstractStore<T>
        implements MutableStore<T> {
    public AbstractMutableStore(final StoreConfig<T> config) {
        super(config);
    }

    /*
     * (non-Javadoc)
     * @see com.arakelian.dao.Dao#delete(java.lang.String)
     */
    @Override
    public void delete(final String id) {
        if (!StringUtils.isEmpty(id)) {
            doDelete(id);
            notifyDeleted(id);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.arakelian.dao.Dao#delete(com.arakelian.dao.feature.HasId)
     */
    @Override
    public void delete(final T value) {
        if (value != null) {
            final String id = value.getId();
            if (!StringUtils.isEmpty(id)) {
                doDelete(id);
                notifyDeleted(value);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.arakelian.dao.Dao#deleteAll(java.util.Collection)
     */
    @Override
    public void deleteAll(final Collection<T> values) {
        if (values == null || values.size() == 0) {
            return;
        }

        // we need a List of values to partition
        final List<T> valuesList;
        if (values instanceof List) {
            valuesList = (List<T>) values;
        } else {
            valuesList = Lists.newArrayList(values);
        }

        // process values in groups of <partition size>
        for (final List<T> partition : Lists.partition(valuesList, config.getPartitionSize())) {
            doDeleteAllValues(partition);
            for (final T value : partition) {
                notifyDeleted(value);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.arakelian.dao.Dao#deleteAll(java.lang.Object[])
     */
    @Override
    public void deleteAll(final String... ids) {
        if (ids == null || ids.length == 0) {
            return;
        }

        // process ids in groups of <partition size>
        final ArrayList<String> list = Lists.newArrayList(ids);
        for (final List<String> partition : Lists.partition(list, config.getPartitionSize())) {
            doDeleteAllIds(partition);
            for (final String id : partition) {
                notifyDeleted(id);
            }
        }
    }

    @Override
    public void put(final T value) {
        if (value == null) {
            return;
        }

        // make sure bean has id
        final String id = value.getId();
        if (StringUtils.isEmpty(id)) {
            throw new StoreException("Id not specified for " + value);
        }

        // defer to actual implementation
        doPut(value);

        // notify listeners
        for (final StoreListener<T> listener : config.getListeners()) {
            listener.put(value);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.arakelian.dao.Dao#putAll(java.util.Collection)
     */
    @Override
    public void putAll(final Collection<T> values) {
        if (values == null || values.size() == 0) {
            return;
        }
        for (final T value : values) {
            put(value);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.arakelian.dao.Dao#putAll(com.arakelian.dao.feature.HasId[])
     */
    @Override
    public void putAll(final T[] values) {
        if (values == null || values.length == 0) {
            return;
        }
        for (final T value : values) {
            put(value);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this) //
                .add("table", config.getTable()) //
                .add("clazz", config.getClazz()) //
                .toString();
    }

    /**
     * Internal method to delete a value identified by the given id.
     *
     * @param id
     *            uniquely identifies the value, guaranteed to be non-empty
     */
    protected abstract void doDelete(final String id);

    /**
     * Internal method to delete the given values.
     *
     * @param ids
     *            list of ids to delete, already partitioned into a small list
     */
    protected abstract void doDeleteAllIds(final List<String> ids);

    /**
     * Internal method to delete the given values.
     *
     * @param values
     *            list of values to delete, already partitioned into a small list
     */
    protected abstract void doDeleteAllValues(final List<T> values);

    /**
     * Internal method that gets all values with the given ids, and appends them to the given list.
     *
     * @param result
     *            list to return results in, or null if a list has not yet been created
     * @param ids
     *            list of ids, guaranteed not to contain nulls or empty values
     * @return existing list or new list if existing list was null
     */
    @Override
    protected abstract List<T> doGetAll(List<T> result, final List<String> ids);

    /**
     * Internal method that stores the given value.
     *
     * @param value
     *            value to be stored
     */
    protected abstract void doPut(final T value);

    protected void notifyDeleted(final String id) {
        if (!StringUtils.isEmpty(id)) {
            for (final StoreListener<T> listener : config.getListeners()) {
                listener.delete(id);
            }
        }
    }

    protected void notifyDeleted(final T value) {
        if (value != null && !StringUtils.isEmpty(value.getId())) {
            for (final StoreListener<T> listener : config.getListeners()) {
                listener.delete(value);
            }
        }
    }
}
