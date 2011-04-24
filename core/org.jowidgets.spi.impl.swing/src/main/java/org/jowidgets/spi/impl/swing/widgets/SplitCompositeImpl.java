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

package org.jowidgets.spi.impl.swing.widgets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.SplitResizePolicy;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.swing.util.BorderConvert;
import org.jowidgets.spi.impl.swing.util.DimensionConvert;
import org.jowidgets.spi.impl.swing.util.SplitOrientationConvert;
import org.jowidgets.spi.impl.swing.widgets.base.JoSplitPane;
import org.jowidgets.spi.widgets.ICompositeSpi;
import org.jowidgets.spi.widgets.ISplitCompositeSpi;
import org.jowidgets.spi.widgets.setup.ISplitCompositeSetupSpi;

public class SplitCompositeImpl extends SwingControl implements ISplitCompositeSpi {

	private final ICompositeSpi first;
	private final ICompositeSpi second;

	private final JPanel firstPanel;
	private final JPanel secondPanel;

	public SplitCompositeImpl(final IGenericWidgetFactory factory, final ISplitCompositeSetupSpi setup) {
		super(new JoSplitPane(SplitOrientationConvert.convert(setup.getOrientation()), setup.getWeight(), getResizeWeight(setup)));

		this.firstPanel = new JPanel();
		this.secondPanel = new JPanel();

		firstPanel.setBorder(BorderConvert.convert(setup.getFirstBorder()));
		secondPanel.setBorder(BorderConvert.convert(setup.getSecondBorder()));

		first = new SwingComposite(factory, firstPanel);
		second = new SwingComposite(factory, secondPanel);

		first.setLayout(setup.getFirstLayout());
		second.setLayout(setup.getSecondLayout());

		final JSplitPane splitPane = getUiReference();

		splitPane.setLeftComponent(firstPanel);
		splitPane.setRightComponent(secondPanel);

		splitPane.setBorder(BorderFactory.createEmptyBorder());
		splitPane.setDividerSize(setup.getDividerSize());
	}

	@Override
	public ICompositeSpi getFirst() {
		return first;
	}

	@Override
	public ICompositeSpi getSecond() {
		return second;
	}

	@Override
	public void setClientAreaMinSizes(final Dimension firstMinSize, final Dimension secondMinSize) {
		firstPanel.setMinimumSize(DimensionConvert.convert(firstMinSize));
		secondPanel.setMinimumSize(DimensionConvert.convert(secondMinSize));
	}

	@Override
	public JSplitPane getUiReference() {
		return (JSplitPane) super.getUiReference();
	}

	private static double getResizeWeight(final ISplitCompositeSetupSpi setup) {
		final SplitResizePolicy resizePolicy = setup.getResizePolicy();
		if (resizePolicy == SplitResizePolicy.RESIZE_FIRST) {
			return 1.0;
		}
		else if (resizePolicy == SplitResizePolicy.RESIZE_SECOND) {
			return 0.0;
		}
		else if (resizePolicy == SplitResizePolicy.RESIZE_BOTH) {
			return setup.getWeight();
		}
		else {
			throw new IllegalArgumentException(SplitResizePolicy.class.getSimpleName() + "'" + resizePolicy + "' is not known.");
		}
	}

}
