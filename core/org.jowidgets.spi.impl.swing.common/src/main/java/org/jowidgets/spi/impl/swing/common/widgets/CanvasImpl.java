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
package org.jowidgets.spi.impl.swing.common.widgets;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.descriptor.setup.ICanvasSetupCommon;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.graphics.IPaintListenerSpi;
import org.jowidgets.spi.graphics.IPaintObservableSpi;
import org.jowidgets.spi.impl.controller.PaintObservable;
import org.jowidgets.spi.impl.swing.common.graphics.GraphicContextSpiImpl;
import org.jowidgets.spi.impl.swing.common.util.RectangleConvert;
import org.jowidgets.spi.widgets.ICanvasSpi;

public class CanvasImpl extends SwingComposite implements ICanvasSpi {

	public CanvasImpl(final IGenericWidgetFactory factory, final ICanvasSetupCommon setup) {
		super(factory, new CanvasPanel());
		getUiReference().setBackground(null);
	}

	@Override
	public CanvasPanel getUiReference() {
		return (CanvasPanel) super.getUiReference();
	}

	@Override
	public void addPaintListener(final IPaintListenerSpi listener) {
		getUiReference().addPaintListener(listener);
	}

	@Override
	public void removePaintListener(final IPaintListenerSpi listener) {
		getUiReference().removePaintListener(listener);
	}

	private static final class CanvasPanel extends JPanel implements IPaintObservableSpi {

		private static final long serialVersionUID = -6875610604989809218L;

		private final PaintObservable paintObservable;

		private CanvasPanel() {
			this.paintObservable = new PaintObservable();
		}

		@Override
		public void paint(final Graphics g) {
			final Rectangle bounds = RectangleConvert.convert(getBounds());
			paintObservable.firePaint(new GraphicContextSpiImpl((Graphics2D) g, bounds));
		}

		@Override
		public void addPaintListener(final IPaintListenerSpi listener) {
			paintObservable.addPaintListener(listener);
		}

		@Override
		public void removePaintListener(final IPaintListenerSpi listener) {
			paintObservable.removePaintListener(listener);
		}

	}
}
