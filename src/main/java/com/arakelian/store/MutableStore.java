package com.arakelian.store;

import java.util.Collection;

import com.arakelian.store.feature.HasId;

public interface MutableStore<T extends HasId> extends Store<T> {
    /**
     * Deletes the value in the data store uniquely identified by the given identifier, if it
     * exists.
     *
     * @param id
     *            uniquely identifes the value
     */
    public void delete(final String id);

    /**
     * Deletes the given value from the data store
     *
     * @param value
     *            value to delete
     */
    public void delete(final T value);

    /**
     * Deletes the given values from the data store
     *
     * @param values
     *            collection of values
     */
    public void deleteAll(final Collection<T> values);

    /**
     * Deletes the values from the data store with the given ids
     *
     * @param ids
     *            uniquely identifies the values to be deleted
     */
    public void deleteAll(final String... ids);

    /**
     * Stores the given value in the data store.
     *
     * @param value
     *            value to store
     */
    public void put(T value);

    /**
     * Stores the given values in the data store.
     *
     * @param values
     *            values to store
     */
    public void putAll(Collection<T> values);

    /**
     * Stores the given values in the data store.
     *
     * @param values
     *            values to store
     */
    public void putAll(T[] values);
}
