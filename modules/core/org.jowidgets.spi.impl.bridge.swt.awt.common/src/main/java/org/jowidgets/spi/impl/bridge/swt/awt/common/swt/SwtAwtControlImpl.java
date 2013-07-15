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
package org.jowidgets.spi.impl.bridge.swt.awt.common.swt;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;

import javax.swing.JApplet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.spi.impl.swt.common.widgets.SwtControl;

class SwtAwtControlImpl extends SwtControl implements ISwtAwtControlSpi {

	private final JApplet applet;

	public SwtAwtControlImpl(final Object parentUiReference) {
		super(createComposite(parentUiReference));

		final Composite innerComposite = new Composite(getUiReference(), SWT.EMBEDDED | SWT.NO_BACKGROUND);
		innerComposite.setLayout(new FillLayout());

		final Frame frame = SWT_AWT.new_Frame(innerComposite);
		this.applet = new JApplet();
		frame.add(applet);
		applet.setLayout(new BorderLayout());
	}

	private static Composite createComposite(final Object parentUiReference) {
		if (parentUiReference instanceof Composite) {
			final Composite result = new Composite((Composite) parentUiReference, SWT.NONE);
			result.setLayout(new FillLayout());
			result.addControlListener(new SwtAwtResizeListener());
			return result;
		}
		else {
			throw new IllegalArgumentException("parentUiReference must be a swt composite");
		}
	}

	@Override
	public Composite getUiReference() {
		return (Composite) super.getUiReference();
	}

	@Override
	public Container getAwtContainer() {
		return applet;
	}

	private static final class SwtAwtResizeListener extends ControlAdapter {

		private Rectangle lastRectangle;

		@Override
		public void controlResized(final ControlEvent event) {
			final Composite composite = (Composite) event.widget;
			final Rectangle rectangle = composite.getClientArea();
			if (lastRectangle != null) {
				final int dy = rectangle.height - lastRectangle.height;
				final int dx = rectangle.width - lastRectangle.width;
				if (dx > 0 || dy > 0) {
					final GC graphicContext = new GC(composite);
					try {
						graphicContext.fillRectangle(rectangle.x, lastRectangle.height, rectangle.width, dy);
						graphicContext.fillRectangle(lastRectangle.width, rectangle.y, dx, rectangle.height);
					}
					finally {
						graphicContext.dispose();
					}
				}
			}
			lastRectangle = rectangle;
		}
	}
}
