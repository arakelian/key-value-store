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

package com.arakelian.dao.json;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.arakelian.core.utils.MoreStringUtils;
import com.arakelian.dao.feature.HasId;
import com.arakelian.jackson.utils.JacksonUtils;

public class JacksonDaoObjectMapperTest {
	public enum Gender {
		MALE, FEMALE;
	}

	public static class Person implements HasId {
		private String id;

		private String name;

		private Gender gender;

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Person other = (Person) obj;
			if (gender != other.gender) {
				return false;
			}
			if (id == null) {
				if (other.id != null) {
					return false;
				}
			} else if (!id.equals(other.id)) {
				return false;
			}
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			return true;
		}

		public final Gender getGender() {
			return gender;
		}

		@Override
		public final String getId() {
			return id;
		}

		public final String getName() {
			return name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (gender == null ? 0 : gender.hashCode());
			result = prime * result + (id == null ? 0 : id.hashCode());
			result = prime * result + (name == null ? 0 : name.hashCode());
			return result;
		}

		public final void setGender(final Gender gender) {
			this.gender = gender;
		}

		public final void setId(final String id) {
			this.id = id;
		}

		public final void setName(final String name) {
			this.name = name;
		}
	}

	@Test
	public void testMapper() throws IOException {
		final JacksonDaoObjectMapper<Person> mapper = new JacksonDaoObjectMapper<>(Person.class,
				JacksonUtils.getObjectMapper());
		final Person expected = new Person();
		expected.setId(MoreStringUtils.uuid());
		expected.setName("Greg Arakelian");
		expected.setGender(Gender.MALE);
		final String json = mapper.writeValueAsString(expected);
		final Person actual = mapper.readValue(json);
		Assert.assertEquals(expected, actual);
	}
}
