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
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jo.widgets.impl.swt.widgets;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jo.widgets.api.image.IImageConstant;
import org.jo.widgets.api.look.AutoPackPolicy;
import org.jo.widgets.api.look.Dimension;
import org.jo.widgets.api.look.Position;
import org.jo.widgets.api.widgets.IWindowWidget;
import org.jo.widgets.api.widgets.descriptor.base.IBaseWidgetDescriptor;
import org.jo.widgets.api.widgets.factory.IGenericWidgetFactory;
import org.jo.widgets.impl.swt.internal.color.IColorCache;
import org.jo.widgets.impl.swt.internal.image.SwtImageRegistry;
import org.jo.widgets.impl.swt.util.DimensionConvert;
import org.jo.widgets.impl.swt.util.PositionConvert;

public class SwtWindowWidget extends SwtContainerWidget implements IWindowWidget {

	private final AutoPackPolicy autoPackPolicy;
	private boolean wasVisible;

	public SwtWindowWidget(final IGenericWidgetFactory factory, final IColorCache colorCache, final Shell window) {
		this(factory, colorCache, window, AutoPackPolicy.ONCE);
	}

	public SwtWindowWidget(
		final IGenericWidgetFactory factory,
		final IColorCache colorCache,
		final Shell window,
		final AutoPackPolicy autoPackPolicy) {

		super(factory, colorCache, window);

		this.autoPackPolicy = autoPackPolicy;
		this.wasVisible = false;
	}

	@Override
	public Shell getUiReference() {
		return (Shell) super.getUiReference();
	}

	@Override
	public void pack() {
		getUiReference().pack();
	}

	@Override
	public void setVisible(final boolean visible) {
		if (visible) {

			if (AutoPackPolicy.ALLWAYS.equals(autoPackPolicy)) {
				pack();
			}
			else if (!wasVisible && AutoPackPolicy.ONCE.equals(autoPackPolicy)) {
				pack();
			}
			wasVisible = true;

			getUiReference().open();

			final Shell shell = getUiReference();
			final Display display = shell.getDisplay();

			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}

		}
		else {
			getUiReference().close();
		}
	}

	@Override
	public final void setPosition(final Position position) {
		getUiReference().setLocation(PositionConvert.convert(position));
	}

	@Override
	public final Position getPosition() {
		return PositionConvert.convert(getUiReference().getLocation());
	}

	@Override
	public final void setSize(final Dimension size) {
		getUiReference().setSize(DimensionConvert.convert(size));
	}

	@Override
	public final Dimension getSize() {
		return DimensionConvert.convert(getUiReference().getSize());
	}

	public void setIcon(final SwtImageRegistry imageRegistry, final IImageConstant icon) {
		getUiReference().setImage(imageRegistry.getImage(icon));
	}

	@Override
	public <WIDGET_TYPE extends IWindowWidget, DESCRIPTOR_TYPE extends IBaseWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
		final DESCRIPTOR_TYPE descriptor) {
		return getGenericWidgetFactory().createChildWindow(this, descriptor);
	}

}
