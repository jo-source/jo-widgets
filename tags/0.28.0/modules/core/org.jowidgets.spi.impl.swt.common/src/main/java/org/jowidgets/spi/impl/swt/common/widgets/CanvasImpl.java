/*
 * Copyright (c) 2013, Michael Grossmann
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
package org.jowidgets.spi.impl.swt.common.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.graphics.IPaintListenerSpi;
import org.jowidgets.spi.impl.controller.PaintObservable;
import org.jowidgets.spi.impl.swt.common.graphics.GraphicContextSpiImpl;
import org.jowidgets.spi.widgets.ICanvasSpi;
import org.jowidgets.spi.widgets.setup.ICanvasSetupSpi;

public class CanvasImpl extends SwtComposite implements ICanvasSpi {

	private final PaintObservable paintObservable;

	public CanvasImpl(final IGenericWidgetFactory factory, final Object parentUiReference, final ICanvasSetupSpi setup) {
		super(factory, new Canvas((Composite) parentUiReference, SWT.DOUBLE_BUFFERED));
		getUiReference().setBackgroundMode(SWT.INHERIT_DEFAULT);
		this.paintObservable = new PaintObservable();

		getUiReference().addPaintListener(new PaintListener() {
			@Override
			public void paintControl(final PaintEvent e) {
				final Dimension size = getSize();
				final Rectangle bounds = new Rectangle(0, 0, size.getWidth(), size.getHeight());
				final GraphicContextSpiImpl gc = new GraphicContextSpiImpl(e.gc, bounds);
				paintObservable.firePaint(gc);
			}
		});

	}

	@Override
	public Canvas getUiReference() {
		return (Canvas) super.getUiReference();
	}

	@Override
	public void addPaintListener(final IPaintListenerSpi listener) {
		paintObservable.addPaintListener(listener);
	}

	@Override
	public void removePaintListener(final IPaintListenerSpi paintListener) {
		paintObservable.removePaintListener(paintListener);
	}

}
