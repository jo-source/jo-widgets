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

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jowidgets.api.controller.IContainerListener;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.spi.widgets.IContainerSpi;
import org.jowidgets.util.Assert;

public class ContainerDelegate extends DisposableDelegate {

	private final IContainerSpi containerSpi;
	private final IContainer container;
	private final List<IControl> children;
	private final PopupMenuCreationDelegate popupMenuCreationDelegate;
	private final Set<IContainerListener> containerListeners;

	private boolean onRemoveByDispose;

	public ContainerDelegate(final IContainerSpi containerSpi, final IContainer container) {
		Assert.paramNotNull(containerSpi, "containerWidget");
		Assert.paramNotNull(container, "widget");
		this.containerSpi = containerSpi;
		this.container = container;
		this.children = new LinkedList<IControl>();
		this.containerListeners = new LinkedHashSet<IContainerListener>();
		this.popupMenuCreationDelegate = new PopupMenuCreationDelegate(containerSpi, container);
		this.onRemoveByDispose = false;
	}

	public IPopupMenu createPopupMenu() {
		return popupMenuCreationDelegate.createPopupMenu();
	}

	@Override
	public void dispose() {
		if (!isDisposed()) {
			if (container instanceof IControl
				&& container.getParent() != null
				&& (((IControl) container).getParent()).getChildren().contains(container)
				&& !onRemoveByDispose) {

				onRemoveByDispose = true;
				(((IControl) container).getParent()).getChildren().remove(container); //this will invoke dispose by the parent container
				onRemoveByDispose = false;
			}
			else {
				popupMenuCreationDelegate.dispose();
				final List<IControl> childrenCopy = new LinkedList<IControl>(children);
				//clear the children to avoid that children will be removed
				//unnecessarily from its parent container on dispose invocation
				children.clear();
				for (final IControl child : childrenCopy) {
					child.dispose();
				}
				super.dispose();
			}
		}
	}

	public void addContainerListener(final IContainerListener listener) {
		containerListeners.add(listener);
	}

	public void removeContainerListener(final IContainerListener listener) {
		containerListeners.remove(listener);
	}

	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final int index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return add(Integer.valueOf(index), descriptor, layoutConstraints);
	}

	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final int index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		return add(Integer.valueOf(index), creator, layoutConstraints);
	}

	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return add(null, descriptor, layoutConstraints);
	}

	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		return add(null, creator, layoutConstraints);
	}

	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
		return add(descriptor, null);
	}

	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final ICustomWidgetCreator<WIDGET_TYPE> creator) {
		return add(creator, null);
	}

	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		final WIDGET_TYPE result = containerSpi.add(index, descriptor, layoutConstraints);
		result.setParent(container);
		if (index != null) {
			children.add(index.intValue(), result);
		}
		else {
			children.add(result);
		}
		fireAfterAdded(result);
		return result;
	}

	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final Integer index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		final WIDGET_TYPE result = containerSpi.add(index, creator, layoutConstraints);
		result.setParent(container);
		if (index != null) {
			children.add(index.intValue(), result);
		}
		else {
			children.add(result);
		}
		fireAfterAdded(result);
		return result;
	}

	public void setTabOrder(final List<? extends IControl> tabOrder) {
		containerSpi.setTabOrder(tabOrder);
	}

	public List<IControl> getChildren() {
		return new LinkedList<IControl>(children);
	}

	public void removeAll() {
		final List<IControl> childrenCopy = new LinkedList<IControl>(children);
		children.clear();
		for (final IControl child : childrenCopy) {
			fireBeforeRemove(child);
			child.dispose();
		}
		containerSpi.removeAll();
	}

	public boolean remove(final IControl control) {
		Assert.paramNotNull(control, "control");
		if (children.contains(control)) {
			fireBeforeRemove(control);
			boolean removed = children.remove(control);
			control.dispose();
			removed = removed && containerSpi.remove(control);
			if (removed) {
				return true;
			}
			else {
				throw new IllegalStateException(
					"Control could not be removed from spi container. This seems to be a bug. Please report this");
			}
		}
		else {
			return false;
		}
	}

	private void fireAfterAdded(final IControl control) {
		for (final IContainerListener listener : new LinkedList<IContainerListener>(containerListeners)) {
			listener.afterAdded(control);
		}
	}

	private void fireBeforeRemove(final IControl control) {
		for (final IContainerListener listener : new LinkedList<IContainerListener>(containerListeners)) {
			listener.beforeRemove(control);
		}
	}

}
