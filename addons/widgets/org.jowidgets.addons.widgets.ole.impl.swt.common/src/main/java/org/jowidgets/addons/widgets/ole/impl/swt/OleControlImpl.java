/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.addons.widgets.ole.impl.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jowidgets.addons.widgets.ole.api.IOleControl;
import org.jowidgets.addons.widgets.ole.api.IOleSetupBuilder;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.spi.impl.swt.common.color.ColorCache;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.IMutableValue;
import org.jowidgets.util.IMutableValueListener;
import org.jowidgets.util.IValueChangedEvent;

class OleControlImpl extends ControlWrapper implements IOleControl {

	private final Boolean initialVisiblityState;
	private final IColorConstant initialBackgroundColor;
	private final IColorConstant initialForegroundColor;

	@SuppressWarnings("unused")
	private Composite swtOleFrame;

	OleControlImpl(final IControl control, final IMutableValue<Composite> swtCompositeValue, final IOleSetupBuilder<?> setup) {
		super(control);

		this.initialVisiblityState = setup.isVisible();
		this.initialBackgroundColor = setup.getBackgroundColor();
		this.initialForegroundColor = setup.getForegroundColor();

		swtCompositeValue.addMutableValueListener(new IMutableValueListener<Composite>() {
			@Override
			public void changed(final IValueChangedEvent<Composite> event) {
				swtCompositeChanged(event.getSource());
			}
		});

		swtCompositeChanged(swtCompositeValue);
	}

	private void swtCompositeChanged(final IMutableValue<Composite> swtCompositeValue) {
		if (swtCompositeValue.getValue() != null) {
			swtOleFrame = createOleFrame(swtCompositeValue.getValue());
		}
		else {
			swtOleFrame = null;
		}
	}

	Composite createOleFrame(final Composite swtComposite) {

		swtComposite.setLayout(new FillLayout());

		//TODO create the ole stuff here BEGIN
		final Composite result = new Composite(swtComposite, SWT.NONE);
		result.setLayout(new FillLayout());
		final Label label = new Label(result, SWT.NONE);
		label.setText("TODO implement OLE control");
		//TODO create the ole stuff here END

		if (initialVisiblityState != null) {
			setVisible(initialVisiblityState.booleanValue());
		}

		if (initialBackgroundColor != null) {
			result.setBackground(ColorCache.getInstance().getColor(initialBackgroundColor));
		}

		if (initialForegroundColor != null) {
			result.setForeground(ColorCache.getInstance().getColor(initialForegroundColor));
		}

		return result;
	}

}
