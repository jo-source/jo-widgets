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

package org.jowidgets.impl.swing.widgets.internal;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.swing.widgets.SwingControl;
import org.jowidgets.spi.widgets.IToolBarButtonSpi;
import org.jowidgets.spi.widgets.IToolBarContainerItemSpi;
import org.jowidgets.spi.widgets.IToolBarItemSpi;
import org.jowidgets.spi.widgets.IToolBarPopupButtonSpi;
import org.jowidgets.spi.widgets.IToolBarSpi;
import org.jowidgets.spi.widgets.IToolBarToggleButtonSpi;

public class ToolBarImpl extends SwingControl implements IToolBarSpi {

	private final IGenericWidgetFactory factory;

	public ToolBarImpl(final IGenericWidgetFactory factory) {
		super(new JToolBar());
		this.factory = factory;

		getUiReference().setFloatable(false);
	}

	@Override
	public JToolBar getUiReference() {
		return (JToolBar) super.getUiReference();
	}

	@Override
	public IToolBarButtonSpi addToolBarButton(final Integer index) {
		final JButton button = new JButton();

		if (index != null) {
			getUiReference().add(button, index.intValue());
		}
		else {
			getUiReference().add(button);
		}
		return new ToolBarButtonImpl(button);
	}

	@Override
	public IToolBarToggleButtonSpi addToolBarToggleButton(final Integer index) {
		final JToggleButton button = new JToggleButton();

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
		final JButton button = new JButton();
		final JButton buttonArrow = new JButton();

		if (index != null) {
			getUiReference().add(buttonArrow, index.intValue());
			getUiReference().add(button, index.intValue());
		}
		else {
			getUiReference().add(button);
			getUiReference().add(buttonArrow);
		}
		return new ToolBarPopupButtonImpl(button, buttonArrow);
	}

	@Override
	public IToolBarContainerItemSpi addToolBarContainer(final Integer index) {
		final JPanel panel = new JPanel();

		if (index != null) {
			getUiReference().add(panel, index.intValue());
		}
		else {
			getUiReference().add(panel);
		}
		return new ToolBarContainerItemImpl(factory, panel);
	}

	@Override
	public IToolBarItemSpi addSeparator(final Integer index) {
		final JToolBar.Separator separator = new JToolBar.Separator();

		if (index != null) {
			getUiReference().add(separator, index.intValue());
		}
		else {
			getUiReference().add(separator);
		}
		return new IToolBarItemSpi() {

			@Override
			public void setToolTipText(final String text) {}

			@Override
			public void setText(final String text) {}

			@Override
			public void setIcon(final IImageConstant icon) {}

			@Override
			public void setEnabled(final boolean enabled) {
				separator.setEnabled(enabled);
			}

			@Override
			public boolean isEnabled() {
				return separator.isEnabled();
			}

			@Override
			public Object getUiReference() {
				return separator;
			}
		};
	}

	@Override
	public void remove(final int index) {
		getUiReference().remove(index);
	}

	@Override
	public void pack() {
		//nothing to do
	}

}
