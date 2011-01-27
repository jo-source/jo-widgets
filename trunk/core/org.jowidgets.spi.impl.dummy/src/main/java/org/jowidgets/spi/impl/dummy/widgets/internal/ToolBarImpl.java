/*
 * Copyright (c) 2011, Lukas Gross
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

package org.jowidgets.spi.impl.dummy.widgets.internal;

import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.dummy.dummyui.UIDToolBar;
import org.jowidgets.spi.impl.dummy.dummyui.UIDToolBarButton;
import org.jowidgets.spi.impl.dummy.dummyui.UIDToolBarContainerItem;
import org.jowidgets.spi.impl.dummy.dummyui.UIDToolBarPopupButton;
import org.jowidgets.spi.impl.dummy.dummyui.UIDToolBarSeparator;
import org.jowidgets.spi.impl.dummy.dummyui.UIDToolBarToggleButton;
import org.jowidgets.spi.impl.dummy.image.DummyImageRegistry;
import org.jowidgets.spi.impl.dummy.widgets.DummyControl;
import org.jowidgets.spi.widgets.IToolBarButtonSpi;
import org.jowidgets.spi.widgets.IToolBarContainerItemSpi;
import org.jowidgets.spi.widgets.IToolBarItemSpi;
import org.jowidgets.spi.widgets.IToolBarPopupButtonSpi;
import org.jowidgets.spi.widgets.IToolBarSpi;
import org.jowidgets.spi.widgets.IToolBarToggleButtonSpi;

public class ToolBarImpl extends DummyControl implements IToolBarSpi {

	private final IGenericWidgetFactory factory;

	public ToolBarImpl(final DummyImageRegistry registry, final IGenericWidgetFactory factory) {
		super(new UIDToolBar());
		this.factory = factory;
	}

	@Override
	public UIDToolBar getUiReference() {
		return (UIDToolBar) super.getUiReference();
	}

	@Override
	public void pack() {
		// Nothing to do
	}

	@Override
	public void remove(final int index) {
		getUiReference().remove(index);
	}

	@Override
	public IToolBarButtonSpi addToolBarButton(final Integer index) {
		final UIDToolBarButton button = new UIDToolBarButton();
		if (index != null) {
			getUiReference().add(button, index.intValue());
		}
		else {
			getUiReference().add(button);
		}
		return new ToolBarButtonImpl(new UIDToolBarButton());
	}

	@Override
	public IToolBarToggleButtonSpi addToolBarToggleButton(final Integer index) {
		final UIDToolBarToggleButton button = new UIDToolBarToggleButton();
		if (index != null) {
			getUiReference().add(button, index.intValue());
		}
		else {
			getUiReference().add(button);
		}
		return new ToolBarToggleButtonImpl(button);
	}

	@Override
	public IToolBarPopupButtonSpi addToolBarPopupButton(final Integer index) {
		final UIDToolBarPopupButton button = new UIDToolBarPopupButton();
		if (index != null) {
			getUiReference().add(button, index.intValue());
		}
		else {
			getUiReference().add(button);
		}
		return new ToolBarPopupButtonImpl(button);
	}

	@Override
	public IToolBarContainerItemSpi addToolBarContainer(final Integer index) {
		final UIDToolBarContainerItem container = new UIDToolBarContainerItem();
		if (index != null) {
			getUiReference().add(container, index.intValue());
		}
		else {
			getUiReference().add(container);
		}
		return new ToolBarContainerItemImpl(factory, container);
	}

	@Override
	public IToolBarItemSpi addSeparator(final Integer index) {
		final UIDToolBarSeparator separator = new UIDToolBarSeparator(Orientation.VERTICAL);
		if (index != null) {
			getUiReference().add(separator, index.intValue());
		}
		else {
			getUiReference().add(separator);
		}
		return new ToolBarSeparatorImpl(separator);
	}
}
