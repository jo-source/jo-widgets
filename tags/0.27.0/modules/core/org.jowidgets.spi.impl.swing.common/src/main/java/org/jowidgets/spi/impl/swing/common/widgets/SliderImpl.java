/*
 * Copyright (c) 2012, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.spi.impl.swing.common.widgets;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jowidgets.common.types.Orientation;
import org.jowidgets.spi.widgets.ISliderSpi;
import org.jowidgets.spi.widgets.setup.ISliderSetupSpi;
import org.jowidgets.util.Assert;

public final class SliderImpl extends AbstractInputControl implements ISliderSpi {

	public SliderImpl(final ISliderSetupSpi setup) {
		super(new JSlider(getOrientation(setup)));

		final JSlider slider = getUiReference();

		slider.setMinimum(setup.getMinimum());
		slider.setValue(setup.getMinimum());
		slider.setMaximum(setup.getMaximum());
		slider.setMajorTickSpacing(setup.getTickSpacing());
		slider.setPaintTicks(setup.getRenderTicks());

		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				fireInputChanged(getSelection());
			}
		});
	}

	private static int getOrientation(final ISliderSetupSpi setup) {
		Assert.paramNotNull(setup.getOrientation(), "setup.getOrientation()");
		if (setup.getOrientation() == Orientation.HORIZONTAL) {
			return JSlider.HORIZONTAL;
		}
		else if (setup.getOrientation() == Orientation.VERTICAL) {
			return JSlider.VERTICAL;
		}
		else {
			throw new IllegalArgumentException("The orientation '" + setup.getOrientation() + "' is not supported");
		}
	}

	@Override
	public JSlider getUiReference() {
		return (JSlider) super.getUiReference();
	}

	@Override
	public int getSelection() {
		return getUiReference().getValue();
	}

	@Override
	public void setSelection(final int value) {
		getUiReference().setValue(value);
	}

	@Override
	public void setEditable(final boolean editable) {
		getUiReference().setEnabled(editable);
	}

}
