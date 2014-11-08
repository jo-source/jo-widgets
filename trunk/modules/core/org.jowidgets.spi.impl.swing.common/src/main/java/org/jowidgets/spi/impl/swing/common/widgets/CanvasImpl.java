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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.descriptor.setup.ICanvasSetupCommon;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.controller.IPaintEventSpi;
import org.jowidgets.spi.graphics.IGraphicContextSpi;
import org.jowidgets.spi.graphics.IPaintListenerSpi;
import org.jowidgets.spi.graphics.IPaintObservableSpi;
import org.jowidgets.spi.impl.controller.PaintEventSpiImpl;
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

	@Override
	public void scroll(
		final int sourceX,
		final int sourceY,
		final int sourceWidth,
		final int sourceHeight,
		final int destinationX,
		final int destinationY) {

		final Dimension size = getUiReference().getSize();
		final Graphics graphics = getUiReference().getGraphics();

		//copy the area to scroll
		graphics.copyArea(sourceX, sourceY, sourceWidth, sourceHeight, destinationX - sourceX, destinationY - sourceY);

		//clear the area outside the scroll destination
		//left border
		graphics.clearRect(0, 0, destinationX, size.height);
		//right border
		graphics.clearRect(destinationX + sourceWidth, 0, size.width - (destinationX + sourceWidth), size.height);
		//top border
		graphics.clearRect(destinationX, 0, sourceWidth, destinationY);
		//bottom border
		graphics.clearRect(destinationX, destinationY + sourceHeight, sourceWidth, size.height - (destinationY + sourceHeight));
	}

	@Override
	public void redraw(final int x, final int y, final int width, final int height) {
		getUiReference().repaint(x, y, width, height);
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
			final IGraphicContextSpi graphicContext = new GraphicContextSpiImpl((Graphics2D) g, bounds);
			final IPaintEventSpi paintEvent;
			if (g.getClipBounds() != null) {
				paintEvent = new PaintEventSpiImpl(graphicContext, RectangleConvert.convert(g.getClipBounds()));
			}
			else {
				paintEvent = new PaintEventSpiImpl(graphicContext);
			}
			paintObservable.firePaint(paintEvent);
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
