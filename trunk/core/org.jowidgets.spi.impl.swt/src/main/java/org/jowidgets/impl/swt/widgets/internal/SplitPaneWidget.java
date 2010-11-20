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

package org.jowidgets.impl.swt.widgets.internal;

import org.eclipse.swt.widgets.Composite;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.swt.color.IColorCache;
import org.jowidgets.impl.swt.widgets.SwtContainerWidget;
import org.jowidgets.impl.swt.widgets.SwtWidget;
import org.jowidgets.impl.swt.widgets.internal.base.JoSashForm;
import org.jowidgets.spi.widgets.IContainerWidgetSpi;
import org.jowidgets.spi.widgets.ISplitContainerWidgetSpi;
import org.jowidgets.spi.widgets.setup.ISplitContainerSetupSpi;

public class SplitPaneWidget extends SwtWidget implements ISplitContainerWidgetSpi {

	private final IContainerWidgetSpi first;
	private final IContainerWidgetSpi second;

	public SplitPaneWidget(
		final IGenericWidgetFactory factory,
		final IColorCache colorCache,
		final Object parentUiReference,
		final ISplitContainerSetupSpi setup) {

		super(colorCache, new JoSashForm((Composite) parentUiReference, setup));

		final JoSashForm sashForm = getUiReference();

		first = new SwtContainerWidget(factory, colorCache, sashForm.getFirst());
		second = new SwtContainerWidget(factory, colorCache, sashForm.getSecond());

		first.setLayout(setup.getFirstLayout());
		second.setLayout(setup.getSecondLayout());

		//		super(colorCache, new SashForm((Composite) parent.getUiReference(), OrientationConvert.convert(setup.getOrientation())));
		//
		//		final SashForm sashForm = getUiReference();
		//
		//		final Composite content1 = BorderToComposite.convert(sashForm, setup.getFirstBorder());
		//		final Composite content2 = BorderToComposite.convert(sashForm, setup.getSecondBorder());
		//
		//		first = new SwtContainerWidget(factory, colorCache, content1);
		//		second = new SwtContainerWidget(factory, colorCache, content2);
		//
		//		first.setLayout(setup.getFirstLayout());
		//		second.setLayout(setup.getSecondLayout());
		//
		//		sashForm.setWeights(getWeights(setup.getWeight()));
		//		sashForm.setSashWidth(setup.getDividerSize() + 1);
	}

	//	private int[] getWeights(final double weight) {
	//		final int firstWeigth = (int) (weight * 1000);
	//		final int secondWeigth = (int) (1000 - weight * 1000);
	//		return new int[] {firstWeigth, secondWeigth};
	//	}

	@Override
	public IContainerWidgetSpi getFirst() {
		return first;
	}

	@Override
	public IContainerWidgetSpi getSecond() {
		return second;
	}

	@Override
	public JoSashForm getUiReference() {
		return (JoSashForm) super.getUiReference();
	}

}
