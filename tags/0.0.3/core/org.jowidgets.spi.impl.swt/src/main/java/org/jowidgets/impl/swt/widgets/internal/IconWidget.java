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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.util.ColorSettingsInvoker;
import org.jowidgets.impl.swt.color.IColorCache;
import org.jowidgets.impl.swt.image.SwtImageRegistry;
import org.jowidgets.impl.swt.widgets.SwtWidget;
import org.jowidgets.spi.widgets.IIconSpi;
import org.jowidgets.spi.widgets.setup.IIconSetupSpi;

public class IconWidget extends SwtWidget implements IIconSpi {

	private final SwtImageRegistry imageRegistry;

	public IconWidget(
		final IColorCache colorCache,
		final SwtImageRegistry imageRegistry,
		final Object parentUiReference,
		final IIconSetupSpi setup) {

		super(colorCache, new Label((Composite) parentUiReference, SWT.NONE));
		this.imageRegistry = imageRegistry;

		setIcon(setup.getIcon());
		ColorSettingsInvoker.setColors(setup, this);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		final Image oldImage = getUiReference().getImage();
		final Image newImage = imageRegistry.getImage(icon);

		if (oldImage != newImage) {
			getUiReference().setImage(newImage);
		}
	}

	@Override
	public Label getUiReference() {
		return (Label) super.getUiReference();
	}

}
