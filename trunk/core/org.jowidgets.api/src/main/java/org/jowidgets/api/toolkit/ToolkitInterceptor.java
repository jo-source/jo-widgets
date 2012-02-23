/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.api.toolkit;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

import org.jowidgets.util.Assert;

public final class ToolkitInterceptor {

	private static CompositeToolkitInterceptorHolder compositeHolder;

	private ToolkitInterceptor() {}

	public static synchronized void registerTollkitInterceptorHolder(final IToolkitInterceptorHolder holder) {
		Assert.paramNotNull(holder, "holder");
		getCompositeHolder().add(holder);
	}

	private static synchronized CompositeToolkitInterceptorHolder getCompositeHolder() {
		if (compositeHolder == null) {
			compositeHolder = new CompositeToolkitInterceptorHolder();
			final ServiceLoader<IToolkitInterceptorHolder> serviceLoader = ServiceLoader.load(IToolkitInterceptorHolder.class);
			final Iterator<IToolkitInterceptorHolder> iterator = serviceLoader.iterator();
			while (iterator.hasNext()) {
				compositeHolder.add(iterator.next());
			}
		}
		return compositeHolder;
	}

	public static synchronized IToolkitInterceptor getInstance() {
		return getCompositeHolder().getToolkitInterceptor();
	}

	public static void onToolkitCreate(final IToolkit toolkit) {
		getInstance().onToolkitCreate(toolkit);
	}

	private static class CompositeToolkitInterceptorHolder implements IToolkitInterceptorHolder {

		private final List<IToolkitInterceptorHolder> holders;

		private final IToolkitInterceptor toolkitInterceptor;

		CompositeToolkitInterceptorHolder() {
			this.holders = new LinkedList<IToolkitInterceptorHolder>();

			this.toolkitInterceptor = new IToolkitInterceptor() {

				@Override
				public void onToolkitCreate(final IToolkit toolkit) {
					for (final IToolkitInterceptorHolder holder : holders) {
						holder.getToolkitInterceptor().onToolkitCreate(toolkit);
					}
				}
			};
		}

		void add(final IToolkitInterceptorHolder holder) {
			holders.add(holder);
			Collections.sort(holders, new Comparator<IToolkitInterceptorHolder>() {
				@Override
				public int compare(final IToolkitInterceptorHolder provider1, final IToolkitInterceptorHolder provider2) {
					if (provider1 != null && provider2 != null) {
						return provider1.getOrder() - provider2.getOrder();
					}
					return 0;
				}
			});
		}

		@Override
		public IToolkitInterceptor getToolkitInterceptor() {
			return toolkitInterceptor;
		}

		@Override
		public int getOrder() {
			return 0;
		}
	}

}
