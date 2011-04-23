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

package org.jowidgets.spi.impl.swt.widgets;

import org.eclipse.swt.widgets.Composite;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.swt.util.BorderToComposite;
import org.jowidgets.spi.impl.swt.widgets.base.JoSashForm;
import org.jowidgets.spi.widgets.ICompositeSpi;
import org.jowidgets.spi.widgets.ISplitCompositeSpi;
import org.jowidgets.spi.widgets.setup.ISplitCompositeSetupSpi;

public class SplitCompositeImpl extends SwtControl implements ISplitCompositeSpi {

	private final ICompositeSpi first;
	private final ICompositeSpi second;
	private final int dividerSize;
	private final Orientation orientation;

	public SplitCompositeImpl(
		final IGenericWidgetFactory factory,
		final Object parentUiReference,
		final ISplitCompositeSetupSpi setup) {
		super(new JoSashForm((Composite) parentUiReference, setup));

		final JoSashForm sashForm = getUiReference();

		final Composite content1 = BorderToComposite.convert(sashForm, setup.getFirstBorder());
		final Composite content2 = BorderToComposite.convert(sashForm, setup.getSecondBorder());
		first = new SwtComposite(factory, content1);
		second = new SwtComposite(factory, content2);

		first.setLayout(setup.getFirstLayout());
		second.setLayout(setup.getSecondLayout());

		this.dividerSize = setup.getDividerSize() + 1;
		this.orientation = setup.getOrientation();

		sashForm.setWeight(setup.getWeight());
		sashForm.setSashWidth(dividerSize);
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
	public JoSashForm getUiReference() {
		return (JoSashForm) super.getUiReference();
	}

	@Override
	public Dimension getMinSize() {
		final Dimension firstMinSize = getFirst().getMinSize();
		final Dimension secondMinSize = getSecond().getMinSize();

		final int width = firstMinSize.getWidth() + secondMinSize.getWidth();
		final int height = firstMinSize.getHeight() + secondMinSize.getHeight();

		if (Orientation.HORIZONTAL == orientation) {
			return new Dimension(width, height + dividerSize);
		}
		else {
			return new Dimension(width + dividerSize, height);
		}

	}

}
