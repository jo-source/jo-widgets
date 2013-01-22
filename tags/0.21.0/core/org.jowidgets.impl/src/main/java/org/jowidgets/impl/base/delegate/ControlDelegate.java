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

package org.jowidgets.impl.base.delegate;

import org.jowidgets.api.controller.IParentListener;
import org.jowidgets.api.controller.IParentObservable;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.spi.widgets.IControlSpi;
import org.jowidgets.tools.controller.ParentObservable;

public class ControlDelegate extends DisposableDelegate implements IParentObservable<IContainer> {

	private final IControl control;
	private final PopupMenuCreationDelegate popupMenuCreationDelegate;
	private final ParentObservable<IContainer> parentObservable;

	private IContainer parent;
	private boolean onRemoveByDispose;

	public ControlDelegate(final IControlSpi controlSpi, final IControl control) {
		super();
		this.control = control;
		this.popupMenuCreationDelegate = new PopupMenuCreationDelegate(controlSpi, control);
		this.parentObservable = new ParentObservable<IContainer>();
		this.onRemoveByDispose = false;
	}

	public IContainer getParent() {
		return parent;
	}

	public void setParent(final IComponent parent) {
		if (this.parent == null) {
			if (parent instanceof IContainer) {
				final IContainer parentContainer = (IContainer) parent;
				this.parent = parentContainer;
				parentObservable.fireParentChanged(null, parentContainer);
			}
			else {
				throw new IllegalArgumentException("Parent must be instance of '" + IContainer.class.getName() + "'");
			}
		}
		else if (!isReparentable()) {
			throw new IllegalStateException("Widget is not reparentable");
		}

	}

	public boolean isReparentable() {
		//TODO MG will be implemented later
		return false;
	}

	@Override
	public void dispose() {
		if (!isDisposed()) {
			if (parent != null && parent.getChildren().contains(control) && !onRemoveByDispose) {
				onRemoveByDispose = true;
				parent.remove(control); //this will invoke dispose by the parent container
				onRemoveByDispose = false;
			}
			else {
				popupMenuCreationDelegate.dispose();
				super.dispose();
			}
		}
	}

	public IPopupMenu createPopupMenu() {
		return popupMenuCreationDelegate.createPopupMenu();
	}

	@Override
	public void addParentListener(final IParentListener<IContainer> listener) {
		parentObservable.addParentListener(listener);
	}

	@Override
	public void removeParentListener(final IParentListener<IContainer> listener) {
		parentObservable.removeParentListener(listener);
	}
}
