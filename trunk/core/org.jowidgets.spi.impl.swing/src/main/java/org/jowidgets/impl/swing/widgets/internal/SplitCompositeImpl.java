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

package org.jowidgets.impl.swing.widgets.internal;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jowidgets.common.types.SplitResizePolicy;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.swing.util.BorderConvert;
import org.jowidgets.impl.swing.util.SplitOrientationConvert;
import org.jowidgets.impl.swing.widgets.SwingContainer;
import org.jowidgets.impl.swing.widgets.SwingComponent;
import org.jowidgets.impl.swing.widgets.internal.base.JoSplitPane;
import org.jowidgets.spi.widgets.ICompositeSpi;
import org.jowidgets.spi.widgets.ISplitCompositeSpi;
import org.jowidgets.spi.widgets.setup.ISplitCompositeSetupSpi;

public class SplitCompositeImpl extends SwingComponent implements ISplitCompositeSpi {

	private final ICompositeSpi first;
	private final ICompositeSpi second;

	public SplitCompositeImpl(final IGenericWidgetFactory factory, final ISplitCompositeSetupSpi setup) {
		super(new JoSplitPane(SplitOrientationConvert.convert(setup.getOrientation()), setup.getWeight(), getResizeWeight(setup)));

		final JPanel content1 = new JPanel();
		final JPanel content2 = new JPanel();

		content1.setBorder(BorderConvert.convert(setup.getFirstBorder()));
		content2.setBorder(BorderConvert.convert(setup.getSecondBorder()));

		first = new SwingContainer(factory, content1);
		second = new SwingContainer(factory, content2);

		first.setLayout(setup.getFirstLayout());
		second.setLayout(setup.getSecondLayout());

		final JSplitPane splitPane = getUiReference();

		splitPane.setLeftComponent(content1);
		splitPane.setRightComponent(content2);

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
