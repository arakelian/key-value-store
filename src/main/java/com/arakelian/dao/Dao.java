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

package com.arakelian.dao;

import java.util.Collection;
import java.util.List;

import com.arakelian.dao.feature.HasId;

public interface Dao<T extends HasId> {
	/**
	 * Deletes the value in the data store uniquely identified by the given identifier, if it exists.
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
	 * Returns record with the given id, or null
	 *
	 * @param id
	 *            record id
	 * @return record with given id, or null if not found
	 */
	public T get(String id);

	/**
	 * Returns a list of all records with the given ids. If there are no matching records, this method
	 * returns an empty list.
	 *
	 * @param ids
	 *            list of ids
	 * @return a list of all records with the given ids, or an empty list
	 */
	public List<T> getAll(Collection<String> ids);

	/**
	 * Returns a list of all records with the given ids. If there are no matching records, this method
	 * returns an empty list.
	 *
	 * @param ids
	 *            list of ids
	 * @return a list of all records with the given ids, or an empty list
	 */
	public List<T> getAll(String... ids);

	/**
	 * Returns the table name.
	 *
	 * @return table name
	 */
	public String getTable();

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
