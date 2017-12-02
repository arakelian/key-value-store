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

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;

import com.arakelian.core.utils.ExecutorUtils;
import com.arakelian.dao.event.DaoEvent.Action;
import com.arakelian.dao.feature.HasId;
import com.google.common.base.Preconditions;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class DaoEventPublisher<T extends HasId> implements DaoListener<T>, Closeable {
	/** Optional disruptor we create, start and shutdown **/
	private final Disruptor<DaoEvent<T>> disruptor;

	/** Ring buffer we publish to **/
	private final RingBuffer<DaoEvent<T>> ringBuffer;

	/** We can only be closed once **/
	private final AtomicBoolean closed = new AtomicBoolean();

	@SafeVarargs
	public DaoEventPublisher(final int ringBufferSize, final EventHandler<DaoEvent<T>>... handlers) {
		// start disruptor that receives DAO events and forwards to consumers
		this.disruptor = new Disruptor<>( //
				new DaoEventFactory<T>(), //
				ringBufferSize, //
				ExecutorUtils.newThreadFactory(DaoEventPublisher.class, false), //
				ProducerType.SINGLE, //
				new BlockingWaitStrategy());
		disruptor.handleEventsWith(handlers);
		disruptor.start();

		// get ring buffer we publish to
		this.ringBuffer = disruptor.getRingBuffer();
	}

	public DaoEventPublisher(final RingBuffer<DaoEvent<T>> ringBuffer) {
		this.disruptor = null;
		this.ringBuffer = ringBuffer;
	}

	@Override
	public void close() {
		if (closed.compareAndSet(false, true)) {
			if (disruptor != null) {
				this.disruptor.shutdown();
			}
		}
	}

	@Override
	public void delete(final String id) {
		Preconditions.checkArgument(!StringUtils.isEmpty(id), "id must be non-empty");
		final long sequence = ringBuffer.next();
		try {
			final DaoEvent<T> event = ringBuffer.get(sequence);
			event.reset();
			event.setAction(Action.DELETE);
			event.setId(id);
		} finally {
			ringBuffer.publish(sequence);
		}
	}

	@Override
	public void delete(final T value) {
		Preconditions.checkArgument(value != null, "value must be non-null");
		final long sequence = ringBuffer.next();
		try {
			final DaoEvent<T> event = ringBuffer.get(sequence);
			event.reset();
			event.setAction(Action.DELETE);
			event.setId(value.getId());
			event.setValue(value);
		} finally {
			ringBuffer.publish(sequence);
		}
	}

	@Override
	public void put(final T value) {
		Preconditions.checkArgument(value != null, "value must be non-null");
		final long sequence = ringBuffer.next();
		try {
			final DaoEvent<T> event = ringBuffer.get(sequence);
			event.reset();
			event.setAction(Action.PUT);
			event.setId(value.getId());
			event.setValue(value);
		} finally {
			ringBuffer.publish(sequence);
		}
	}
}
