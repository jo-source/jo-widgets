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

import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.spi.widgets.IToolBarContainerItemSpi;
import org.tbee.javafx.scene.layout.MigPane;

public class ToolBarContainerItemImpl extends JavafxContainer implements IToolBarContainerItemSpi {

	public ToolBarContainerItemImpl(final IGenericWidgetFactory factory, final Pane pane) {
		super(factory, pane);
		pane.setStyle("-fx-alignment: baseline-center;");
	}

	@Override
	public MigPane getUiReference() {
		return (MigPane) super.getUiReference();
	}

	@Override
	public void setText(final String text) {}

	@Override
	public void setToolTipText(final String text) {
		final Tooltip tool = new Tooltip(text);
		if (text == null || text.isEmpty()) {
			Tooltip.uninstall(getUiReference(), tool);
		}
		else {
			Tooltip.install(getUiReference(), tool);
		}
	}

	@Override
	public void setIcon(final IImageConstant icon) {}

	@Override
	public Position getPosition() {
		return new Position((int) getUiReference().getLayoutX(), (int) getUiReference().getLayoutY());
	}

	@Override
	public Dimension getSize() {
		return new Dimension((int) getUiReference().getWidth(), (int) getUiReference().getHeight());
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		if (layoutDescriptor instanceof MigLayoutDescriptor) {
			final MigLayoutDescriptor des = (MigLayoutDescriptor) layoutDescriptor;
			final MigPane migPane = new MigPane(des.getLayoutConstraints(), des.getColumnConstraints(), des.getRowConstraints());
			final LC layoutConstraints = migPane.getLayoutConstraints();
			final AC columnConstraints = migPane.getColumnConstraints();
			final AC rowConstraints = migPane.getRowConstraints();
			getUiReference().setLayoutConstraints(layoutConstraints);
			getUiReference().setColumnConstraints(columnConstraints);
			getUiReference().setRowConstraints(rowConstraints);
		}
	}
}
