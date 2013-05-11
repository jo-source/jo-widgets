/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.impl.widgets.basic;

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.controller.IParentListener;
import org.jowidgets.api.graphics.IPaintListener;
import org.jowidgets.api.widgets.ICanvas;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.descriptor.ICanvasDescriptor;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.event.PaintObservable;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.basic.graphics.GraphicContextAdapter;
import org.jowidgets.impl.widgets.common.wrapper.AbstractCanvasSpiWrapper;
import org.jowidgets.spi.graphics.IGraphicContextSpi;
import org.jowidgets.spi.graphics.IPaintListenerSpi;
import org.jowidgets.spi.widgets.ICanvasSpi;

public final class CanvasImpl extends AbstractCanvasSpiWrapper implements ICanvas {

	private final ControlDelegate controlDelegate;
	private final PaintObservable paintObservable;

	public CanvasImpl(final ICanvasSpi canvasSpi, final ICanvasDescriptor descriptor) {
		super(canvasSpi);

		this.controlDelegate = new ControlDelegate(canvasSpi, this);
		this.paintObservable = new PaintObservable();

		VisibiliySettingsInvoker.setVisibility(descriptor, this);
		ColorSettingsInvoker.setColors(descriptor, this);

		canvasSpi.addPaintListener(new IPaintListenerSpi() {
			@Override
			public void paint(final IGraphicContextSpi gc) {
				paintObservable.firePaint(new GraphicContextAdapter(gc));
			}
		});
	}

	@Override
	public IContainer getParent() {
		return controlDelegate.getParent();
	}

	@Override
	public void setParent(final IContainer parent) {
		controlDelegate.setParent(parent);
	}

	@Override
	public void addParentListener(final IParentListener<IContainer> listener) {
		controlDelegate.addParentListener(listener);
	}

	@Override
	public void removeParentListener(final IParentListener<IContainer> listener) {
		controlDelegate.removeParentListener(listener);
	}

	@Override
	public boolean isReparentable() {
		return controlDelegate.isReparentable();
	}

	@Override
	public void addDisposeListener(final IDisposeListener listener) {
		controlDelegate.addDisposeListener(listener);
	}

	@Override
	public void removeDisposeListener(final IDisposeListener listener) {
		controlDelegate.removeDisposeListener(listener);
	}

	@Override
	public boolean isDisposed() {
		return controlDelegate.isDisposed();
	}

	@Override
	public void dispose() {
		controlDelegate.dispose();
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return controlDelegate.createPopupMenu();
	}

	@Override
	public void addPaintListener(final IPaintListener listener) {
		paintObservable.addPaintListener(listener);
	}

	@Override
	public void removePaintListener(final IPaintListener listener) {
		paintObservable.removePaintListener(listener);
	}

}
