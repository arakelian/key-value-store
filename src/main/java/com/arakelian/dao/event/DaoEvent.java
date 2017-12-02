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

package com.arakelian.dao.event;

import com.arakelian.dao.feature.HasId;
import com.google.common.base.MoreObjects;

public class DaoEvent<T extends HasId> {
	public static enum Action {
		PUT, DELETE;
	}

	private Action action;
	private String id;
	private T value;

	public final Action getAction() {
		return action;
	}

	public final String getId() {
		return id;
	}

	public final T getValue() {
		return value;
	}

	public void reset() {
		action = null;
		id = null;
		value = null;
	}

	public final void setAction(final Action action) {
		this.action = action;
	}

	public final void setId(final String id) {
		this.id = id;
	}

	public final void setValue(final T value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this) //
				.add("action", action) //
				.add("id", id) //
				.toString();
	}
}
