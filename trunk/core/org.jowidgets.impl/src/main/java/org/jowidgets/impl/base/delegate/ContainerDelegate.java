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

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.spi.widgets.IContainerSpi;
import org.jowidgets.util.Assert;

public class ContainerDelegate {

	private final IContainerSpi containerWidget;
	private final IContainer widget;
	private final List<IControl> children;

	public ContainerDelegate(final IContainerSpi containerWidget, final IContainer widget) {
		Assert.paramNotNull(containerWidget, "containerWidget");
		Assert.paramNotNull(widget, "widget");
		this.containerWidget = containerWidget;
		this.widget = widget;
		this.children = new LinkedList<IControl>();
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
		final WIDGET_TYPE result = containerWidget.add(index, descriptor, layoutConstraints);
		if (result instanceof IControl) {
			((IControl) result).setParent(widget);
		}
		if (index != null) {
			children.add(index.intValue(), result);
		}
		else {
			children.add(result);
		}
		return result;
	}

	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final Integer index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		final WIDGET_TYPE result = containerWidget.add(index, creator, layoutConstraints);
		if (result instanceof IControl) {
			((IControl) result).setParent(widget);
		}
		if (index != null) {
			children.add(index.intValue(), result);
		}
		else {
			children.add(result);
		}
		return result;
	}

	public List<IControl> getChildren() {
		return new LinkedList<IControl>(children);
	}

	public void removeAll() {
		children.clear();
		containerWidget.removeAll();
	}

	public boolean remove(final IControl control) {
		Assert.paramNotNull(control, "control");
		if (children.contains(control)) {
			boolean removed = containerWidget.remove(control);
			removed = removed && children.remove(control);
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

}
