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

package org.jowidgets.tools.powo;

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.common.widgets.builder.ISetupBuilder;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.util.Assert;

class Widget<WIDGET_TYPE extends IWidget, BLUE_PRINT_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE> & ISetupBuilder<?>> implements
		IWidget {

	private final BLUE_PRINT_TYPE bluePrint;
	private final Set<IDisposeListener> disposeListeners;
	private WIDGET_TYPE widget;
	private Boolean enabled;
	private boolean disposed;

	Widget(final BLUE_PRINT_TYPE bluePrint) {
		this.bluePrint = bluePrint;
		this.disposeListeners = new HashSet<IDisposeListener>();
		this.disposed = false;
	}

	public final boolean isInitialized() {
		return widget != null;
	}

	void initialize(final WIDGET_TYPE widget) {
		Assert.paramNotNull(widget, "widget");
		checkNotInitialized();
		if (disposed) {
			throw new IllegalStateException("Widget is disposed!");
		}
		this.widget = widget;
		if (enabled != null) {
			widget.setEnabled(enabled.booleanValue());
		}
		for (final IDisposeListener listener : disposeListeners) {
			widget.addDisposeListener(listener);
		}
	}

	final IWidgetDescriptor<? extends WIDGET_TYPE> getDescriptor() {
		return bluePrint;
	}

	@Override
	public void addDisposeListener(final IDisposeListener listener) {
		Assert.paramNotNull(listener, "listener");
		if (isInitialized()) {
			widget.addDisposeListener(listener);
		}
		else {
			disposeListeners.add(listener);
		}
	}

	@Override
	public void removeDisposeListener(final IDisposeListener listener) {
		Assert.paramNotNull(listener, "listener");
		if (isInitialized()) {
			widget.removeDisposeListener(listener);
		}
		else {
			disposeListeners.remove(listener);
		}
	}

	@Override
	public void dispose() {
		if (isInitialized()) {
			widget.dispose();
		}
		else {
			for (final IDisposeListener listener : disposeListeners) {
				listener.onDispose();
			}
			this.disposed = true;
		}
	}

	@Override
	public boolean isDisposed() {
		if (isInitialized()) {
			return widget.isDisposed();
		}
		else {
			return disposed;
		}
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (isInitialized()) {
			widget.setEnabled(enabled);
		}
		else {
			this.enabled = Boolean.valueOf(enabled);
		}
	}

	@Override
	public boolean isEnabled() {
		if (isInitialized()) {
			return widget.isEnabled();
		}
		else {
			return enabled == null || enabled.booleanValue();
		}
	}

	@Override
	public final Object getUiReference() {
		checkInitialized();
		return widget.getUiReference();
	}

	@Override
	public IWidget getParent() {
		checkInitialized();
		return getWidget().getParent();
	}

	final BLUE_PRINT_TYPE getBluePrint() {
		return bluePrint;
	}

	final WIDGET_TYPE getWidget() {
		return widget;
	}

	final void checkInitialized() {
		if (!isInitialized()) {
			throw new WidgetNotInitializedException("Widget is not yet initialized (was not added to a parent)");
		}
	}

	final void checkNotInitialized() {
		if (isInitialized()) {
			throw new WidgetAlreadyInitializedException("Widget is already initialized (was already added to a parent)");
		}
	}

}
