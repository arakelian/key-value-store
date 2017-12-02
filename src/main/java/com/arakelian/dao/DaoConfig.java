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

import org.immutables.value.Value;

import com.arakelian.dao.event.DaoListener;
import com.arakelian.dao.feature.HasId;
import com.arakelian.dao.feature.HasSoftDeletes;
import com.arakelian.dao.feature.HasTimestamp;

@Value.Style(get = { "is*", "get*" })
public interface DaoConfig<T extends HasId> {
	public Class<T> getClazz();

	public List<DaoListener<T>> getListeners();

	@Value.Default
	public default int getPartitionSize() {
		return 10;
	}

	public String getTable();

	@Value.Default
	public default boolean isSoftDeletes() {
		return HasSoftDeletes.class.isAssignableFrom(getClazz());
	}

	@Value.Default
	public default boolean isTimestamps() {
		return HasTimestamp.class.isAssignableFrom(getClazz());
	}
}
