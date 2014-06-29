/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.tools.command;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IEnabledChecker;
import org.jowidgets.api.command.IEnabledState;
import org.jowidgets.util.Assert;
import org.jowidgets.util.event.IChangeListener;

public final class EnabledCheckerCompositeBuilder {

	private final List<IEnabledChecker> checkers;

	public EnabledCheckerCompositeBuilder() {
		this.checkers = new LinkedList<IEnabledChecker>();
	}

	public EnabledCheckerCompositeBuilder add(final IEnabledChecker checker) {
		Assert.paramNotNull(checker, "checker");
		checkers.add(checker);
		return this;
	}

	public IEnabledChecker build() {
		return new EnabledCheckerComposite(checkers);
	}

	private class EnabledCheckerComposite implements IEnabledChecker {

		private final List<IEnabledChecker> checkers;
		private final Set<IChangeListener> changeListeners;

		EnabledCheckerComposite(final List<IEnabledChecker> checkers) {
			this.checkers = checkers;
			this.changeListeners = new LinkedHashSet<IChangeListener>();

			final IChangeListener changeListener = new IChangeListener() {
				@Override
				public void changed() {
					for (final IChangeListener listener : changeListeners) {
						listener.changed();
					}
				}
			};

			for (final IEnabledChecker checker : checkers) {
				checker.addChangeListener(changeListener);
			}
		}

		@Override
		public IEnabledState getEnabledState() {
			for (final IEnabledChecker checker : checkers) {
				final IEnabledState enabledState = checker.getEnabledState();
				if (!enabledState.isEnabled()) {
					return enabledState;
				}
			}
			return EnabledState.ENABLED;
		}

		@Override
		public void addChangeListener(final IChangeListener listener) {
			Assert.paramNotNull(listener, "listener");
			changeListeners.add(listener);
		}

		@Override
		public void removeChangeListener(final IChangeListener listener) {
			Assert.paramNotNull(listener, "listener");
			changeListeners.remove(listener);
		}

	}
}
