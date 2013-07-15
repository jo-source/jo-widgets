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

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPaneBuilder;
import javafx.scene.layout.Pane;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.widgets.IContainerSpi;
import org.jowidgets.spi.widgets.ISplitCompositeSpi;
import org.jowidgets.spi.widgets.setup.ISplitCompositeSetupSpi;

public class SplitCompositeImpl extends JavafxControl implements ISplitCompositeSpi {

	private final JavafxContainer first;
	private final JavafxContainer second;

	private final int dividerSize;
	private final Orientation orientation;

	public SplitCompositeImpl(final IGenericWidgetFactory factory, final ISplitCompositeSetupSpi setup) {
		super(SplitPaneBuilder.create().orientation(
				setup.getOrientation() == Orientation.HORIZONTAL
						? javafx.geometry.Orientation.HORIZONTAL : javafx.geometry.Orientation.VERTICAL).build());

		this.dividerSize = setup.getDividerSize();
		this.orientation = setup.getOrientation();

		first = new JavafxSplitContainer(factory, getUiReference());
		second = new JavafxSplitContainer(factory, getUiReference());
		getUiReference().getItems().addAll(first.getUiReference(), second.getUiReference());
		first.setLayout(setup.getFirstLayout());
		second.setLayout(setup.getSecondLayout());

		getUiReference().widthProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(final Observable paramObservable) {
				getUiReference().setDividerPosition(0, setup.getWeight());
			}
		});
		getUiReference().heightProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(final Observable paramObservable) {
				getUiReference().setDividerPosition(0, setup.getWeight());
			}
		});

	}

	@Override
	public IContainerSpi getFirst() {
		return first;
	}

	@Override
	public IContainerSpi getSecond() {
		return second;
	}

	@Override
	public void setMinSizes(final Dimension firstMinSize, final Dimension secondMinSize) {
		setMinSize(first.getUiReference(), firstMinSize, false);
		setMinSize(second.getUiReference(), secondMinSize, true);
	}

	private void setMinSize(final Pane pane, final Dimension minSize, final boolean second) {
		if (minSize != null) {
			int width = minSize.getWidth();
			int height = minSize.getHeight();
			if (second) {
				if (Orientation.VERTICAL == orientation) {
					height = height + dividerSize;
				}
				else {
					width = width + dividerSize;
				}
			}
			pane.setMinSize(width, height);
		}
		else {
			pane.setMinSize(-1, -1);
		}
	}

	@Override
	public SplitPane getUiReference() {
		return (SplitPane) super.getUiReference();
	}

}
