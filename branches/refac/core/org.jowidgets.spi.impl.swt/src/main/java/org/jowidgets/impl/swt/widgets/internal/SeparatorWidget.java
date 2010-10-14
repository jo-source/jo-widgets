/*
 * Copyright (c) 2010, Michael Grossmann
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
package org.jowidgets.impl.swt.widgets.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jowidgets.api.util.ColorSettingsInvoker;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.impl.swt.color.IColorCache;
import org.jowidgets.impl.swt.util.OrientationConvert;
import org.jowidgets.impl.swt.widgets.SwtWidget;
import org.jowidgets.spi.widgets.IWidgetSpi;
import org.jowidgets.spi.widgets.descriptor.setup.ISeparatorSetupSpi;

public class SeparatorWidget extends SwtWidget implements IWidgetSpi {

	public SeparatorWidget(final IColorCache colorCache, final IWidget parent, final ISeparatorSetupSpi<?> descriptor) {
		super(colorCache, createSeparator(parent, descriptor));
		ColorSettingsInvoker.setColors(descriptor, this);
	}

	@Override
	public Label getUiReference() {
		return (Label) super.getUiReference();
	}

	private static Label createSeparator(final IWidget parent, final ISeparatorSetupSpi<?> descriptor) {
		final int orientation = OrientationConvert.convert(descriptor.getOrientation());
		return new Label((Composite) parent.getUiReference(), SWT.SEPARATOR | orientation);
	}

}
