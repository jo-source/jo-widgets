/*
 * Copyright (c) 2012, David Bauknecht
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

package org.jowidgets.spi.impl.javafx.widgets;

import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.widgets.IToolBarButtonSpi;
import org.jowidgets.spi.widgets.IToolBarContainerItemSpi;
import org.jowidgets.spi.widgets.IToolBarItemSpi;
import org.jowidgets.spi.widgets.IToolBarPopupButtonSpi;
import org.jowidgets.spi.widgets.IToolBarSpi;
import org.jowidgets.spi.widgets.IToolBarToggleButtonSpi;
import org.jowidgets.spi.widgets.setup.IToolBarSetupSpi;
import org.tbee.javafx.scene.layout.MigPane;

public class ToolBarImpl extends JavafxControl implements IToolBarSpi {

	private final IGenericWidgetFactory factory;

	public ToolBarImpl(final IGenericWidgetFactory factory, final IToolBarSetupSpi setup) {
		super(new ToolBar());
		this.factory = factory;

		if (Orientation.HORIZONTAL == setup.getOrientation()) {
			getUiReference().setOrientation(javafx.geometry.Orientation.HORIZONTAL);
		}
		else if (Orientation.VERTICAL == setup.getOrientation()) {
			getUiReference().setOrientation(javafx.geometry.Orientation.VERTICAL);
		}
		else {
			throw new IllegalArgumentException("Orientation '" + setup.getOrientation() + "' is not known.");
		}
	}

	@Override
	public ToolBar getUiReference() {
		return (ToolBar) super.getUiReference();
	}

	@Override
	public void pack() {
		// TODO DB Auto-generated method stub
	}

	@Override
	public void remove(final int index) {
		getUiReference().getItems().remove(index);
	}

	@Override
	public IToolBarButtonSpi addToolBarButton(final Integer index) {
		final Button button = new Button();
		if (index != null) {
			getUiReference().getItems().add(index.intValue(), button);
		}
		else {
			getUiReference().getItems().add(button);
		}
		return new ToolBarButtonImpl(button);
	}

	@Override
	public IToolBarToggleButtonSpi addToolBarToggleButton(final Integer index) {
		final ToggleButton button = new ToggleButton();
		if (index != null) {
			getUiReference().getItems().add(index.intValue(), button);
		}
		else {
			getUiReference().getItems().add(button);
		}
		return new ToolBarToggleButtonImpl(button);
	}

	@Override
	public IToolBarPopupButtonSpi addToolBarPopupButton(final Integer index) {
		final Button button = new Button();
		if (index != null) {
			getUiReference().getItems().add(index.intValue(), button);
		}
		else {
			getUiReference().getItems().add(button);
		}
		return new ToolBarPopupButtonImpl(button);
	}

	@Override
	public IToolBarContainerItemSpi addToolBarContainer(final Integer index) {
		final MigPane pane = new MigPane("", "0[grow]0", "0[grow]0");
		if (index != null) {
			getUiReference().getItems().add(index.intValue(), pane);
		}
		else {
			getUiReference().getItems().add(pane);
		}
		return new ToolBarContainerItemImpl(factory, pane);
	}

	@Override
	public IToolBarItemSpi addSeparator(final Integer index) {
		final Separator separator = new Separator();
		if (index != null) {
			getUiReference().getItems().add(index.intValue(), separator);
		}
		else {
			getUiReference().getItems().add(separator);
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
				separator.setDisable(!enabled);
			}

			@Override
			public boolean isEnabled() {
				return !separator.isDisabled();
			}

			@Override
			public Object getUiReference() {
				return separator;
			}

			@Override
			public Position getPosition() {
				return new Position((int) separator.getLayoutX(), (int) separator.getLayoutY());
			}

			@Override
			public Dimension getSize() {
				return new Dimension((int) separator.getWidth(), (int) separator.getHeight());
			}
		};

	}

}
