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

package org.jowidgets.spi.impl.swt.common.widgets;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.widgets.IToolBarButtonSpi;
import org.jowidgets.spi.widgets.IToolBarContainerItemSpi;
import org.jowidgets.spi.widgets.IToolBarItemSpi;
import org.jowidgets.spi.widgets.IToolBarPopupButtonSpi;
import org.jowidgets.spi.widgets.IToolBarSpi;
import org.jowidgets.spi.widgets.IToolBarToggleButtonSpi;
import org.jowidgets.spi.widgets.setup.IToolBarSetupSpi;

public class ToolBarImpl extends SwtControl implements IToolBarSpi {

	private final IGenericWidgetFactory factory;

	private final Map<ToolItem, ToolBarContainerItemImpl> containerMap;

	public ToolBarImpl(final IGenericWidgetFactory factory, final Object parentUiReference, final IToolBarSetupSpi setup) {
		super(new ToolBar((Composite) parentUiReference, getStyle(setup)));
		this.factory = factory;
		this.containerMap = new HashMap<ToolItem, ToolBarContainerItemImpl>();
	}

	@Override
	public ToolBar getUiReference() {
		return (ToolBar) super.getUiReference();
	}

	@Override
	public void remove(final int index) {
		final ToolItem item = getUiReference().getItem(index);
		if (item != null && !item.isDisposed()) {
			containerMap.remove(item);
			item.dispose();
		}
	}

	@Override
	public IToolBarButtonSpi addToolBarButton(final Integer index) {
		ToolItem toolItem = null;
		if (index != null) {
			toolItem = new ToolItem(getUiReference(), SWT.PUSH, index.intValue());
		}
		else {
			toolItem = new ToolItem(getUiReference(), SWT.PUSH);
		}
		return new ToolBarButtonImpl(toolItem);
	}

	@Override
	public IToolBarToggleButtonSpi addToolBarToggleButton(final Integer index) {
		ToolItem toolItem = null;
		if (index != null) {
			toolItem = new ToolItem(getUiReference(), SWT.CHECK, index.intValue());
		}
		else {
			toolItem = new ToolItem(getUiReference(), SWT.CHECK);
		}
		return new ToolBarToggleButtonImpl(toolItem);
	}

	@Override
	public IToolBarPopupButtonSpi addToolBarPopupButton(final Integer index) {
		ToolItem toolItem = null;
		if (index != null) {
			toolItem = new ToolItem(getUiReference(), SWT.DROP_DOWN, index.intValue());
		}
		else {
			toolItem = new ToolItem(getUiReference(), SWT.DROP_DOWN);
		}
		return new ToolBarPopupButtonImpl(toolItem);
	}

	@Override
	public IToolBarContainerItemSpi addToolBarContainer(final Integer index) {
		ToolItem toolItem = null;
		if (index != null) {
			toolItem = new ToolItem(getUiReference(), SWT.SEPARATOR, index.intValue());
		}
		else {
			toolItem = new ToolItem(getUiReference(), SWT.SEPARATOR);
		}
		final ToolBarContainerItemImpl result = new ToolBarContainerItemImpl(toolItem, getUiReference(), factory);
		containerMap.put(toolItem, result);
		return result;
	}

	@Override
	public IToolBarItemSpi addSeparator(final Integer index) {
		ToolItem toolItem = null;
		if (index != null) {
			toolItem = new ToolItem(getUiReference(), SWT.SEPARATOR, index.intValue());
		}
		else {
			toolItem = new ToolItem(getUiReference(), SWT.SEPARATOR);
		}
		return new ToolBarItemImpl(toolItem);
	}

	@Override
	public void pack() {
		for (final ToolBarContainerItemImpl container : containerMap.values()) {
			container.pack();
		}
		getUiReference().pack();
	}

	private static int getStyle(final IToolBarSetupSpi setup) {
		int result = SWT.FLAT | SWT.WRAP | SWT.RIGHT;
		if (Orientation.VERTICAL == setup.getOrientation()) {
			result = result | SWT.VERTICAL;
		}
		else if (Orientation.HORIZONTAL == setup.getOrientation()) {
			result = result | SWT.HORIZONTAL;
		}
		else {
			throw new IllegalArgumentException("Orientation '" + setup.getOrientation() + "' is not known.");
		}
		return result;
	}
}
