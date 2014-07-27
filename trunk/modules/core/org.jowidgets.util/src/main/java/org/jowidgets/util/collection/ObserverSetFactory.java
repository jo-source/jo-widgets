/*
 * Copyright (c) 2014, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.util.collection;

import org.jowidgets.util.Assert;
import org.jowidgets.util.collection.IObserverSetFactory.Strategy;

public final class ObserverSetFactory {

	private static final IObserverSetFactory INSTANCE = createInstance();

	private ObserverSetFactory() {}

	private static IObserverSetFactory createInstance() {
		return new DefaultObserverSetFactory();
	}

	public static IObserverSetFactory getInstance() {
		return INSTANCE;
	}

	public static Strategy getDefaultStrategy() {
		return getInstance().getDefaultStrategy();
	}

	public static <OBSERVER_TYPE> IObserverSet<OBSERVER_TYPE> create() {
		return getInstance().create();
	}

	public static <OBSERVER_TYPE> IObserverSet<OBSERVER_TYPE> create(final Strategy strategy) {
		Assert.paramNotNull(strategy, "strategy");
		return getInstance().create(strategy);
	}

	private static final class DefaultObserverSetFactory implements IObserverSetFactory {

		private static final Strategy DEFAULT_STRATEGY = Strategy.HIGH_PERFORMANCE;

		@Override
		public Strategy getDefaultStrategy() {
			return DEFAULT_STRATEGY;
		}

		@Override
		public <OBSERVER_TYPE> IObserverSet<OBSERVER_TYPE> create() {
			return create(DEFAULT_STRATEGY);
		}

		@Override
		public <OBSERVER_TYPE> IObserverSet<OBSERVER_TYPE> create(final Strategy strategy) {
			Assert.paramNotNull(strategy, "strategy");
			if (Strategy.HIGH_PERFORMANCE.equals(strategy)) {
				return new ObserverSetPerformanceStrategyImpl<OBSERVER_TYPE>();
			}
			else if (Strategy.LOW_MEMORY.equals(strategy)) {
				return new ObserverSetMemoryStrategyImpl<OBSERVER_TYPE>();
			}
			else {
				throw new IllegalArgumentException("Stragegy '" + strategy + "' is not known");
			}
		}

	}
}
