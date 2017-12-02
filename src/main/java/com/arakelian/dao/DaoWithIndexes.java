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

import java.util.List;

import com.arakelian.dao.feature.HasId;

public interface DaoWithIndexes<T extends HasId> extends Dao<T> {
	/**
	 * Deletes a set of values using a secondary index. The secondary index is queried using the
	 * arguments provided.
	 *
	 * @param indexName
	 *            secondary index name
	 * @param args
	 *            arguments to secondary index query
	 */
	public void deleteBy(final String indexName, final Object... args);

	/**
	 * Returns a list of values using a secondary index. The secondary index is queried using the
	 * arguments provided.
	 *
	 * If no matching values are found, this method will return an empty list.
	 *
	 * @param indexName
	 *            secondary index name
	 * @param args
	 *            arguments to secondary index query
	 * @return list of values, or null if not found
	 */
	public List<T> getAllBy(final String indexName, final Object... args);

	/**
	 * Returns a value using a secondary index. The secondary index is queried using the arguments
	 * provided.
	 *
	 * If no matching value is found, this method will return null.
	 *
	 * @param indexName
	 *            secondary index name
	 * @param args
	 *            arguments to secondary index query
	 * @return value that matches given arguments, or null if not found
	 */
	public T getBy(final String indexName, final Object... args);
}
