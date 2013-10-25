/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.impl.base.delegate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jowidgets.api.controller.IContainerRegistry;
import org.jowidgets.api.controller.IListenerFactory;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;

final class RecursiveListenenerManager<LISTENER_TYPE> {

	private final IContainer container;
	private final IListenerFactory<LISTENER_TYPE> listenerFactory;
	private final IListenerRegistrationDelegate<LISTENER_TYPE> listenerRegistrationDelegate;
	private final IContainerRegistry containerRegistry;
	private final LISTENER_TYPE rootListener;
	private final Map<IControl, LISTENER_TYPE> createdListeners;

	RecursiveListenenerManager(
		final IContainer container,
		final IListenerFactory<LISTENER_TYPE> listenerFactory,
		final IListenerRegistrationDelegate<LISTENER_TYPE> listenerRegistrationDelegate) {
		this.container = container;
		this.listenerFactory = listenerFactory;
		this.listenerRegistrationDelegate = listenerRegistrationDelegate;
		this.containerRegistry = new ContainerRegistry();
		this.createdListeners = new HashMap<IControl, LISTENER_TYPE>();

		this.rootListener = listenerFactory.create(container);
		if (rootListener != null) {
			listenerRegistrationDelegate.addListener(container, rootListener);
		}
		container.addContainerRegistry(containerRegistry);
	}

	void dispose() {
		container.removeContainerRegistry(containerRegistry);
		if (rootListener != null) {
			listenerRegistrationDelegate.removeListener(container, rootListener);
		}
		for (final Entry<IControl, LISTENER_TYPE> entry : createdListeners.entrySet()) {
			listenerRegistrationDelegate.removeListener(entry.getKey(), entry.getValue());
		}
		createdListeners.clear();
	}

	private class ContainerRegistry implements IContainerRegistry {

		@Override
		public void register(final IControl control) {
			if (createdListeners.get(control) == null) {
				final LISTENER_TYPE listener = listenerFactory.create(control);
				if (listener != null) {
					listenerRegistrationDelegate.addListener(control, listener);
					createdListeners.put(control, listener);
				}
			}
			//else should never occur //TODO MG handle else (or think about it)
		}

		@Override
		public void unregister(final IControl control) {
			final LISTENER_TYPE listener = createdListeners.get(control);
			if (listener != null) {
				listenerRegistrationDelegate.removeListener(control, listener);
			}
		}

	}

	interface IListenerRegistrationDelegate<LISTENER_TYPE> {

		void addListener(IComponent component, LISTENER_TYPE listener);

		void removeListener(IComponent component, LISTENER_TYPE listener);

	}

}
