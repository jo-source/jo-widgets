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
package org.jowidgets.impl.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IDisplayCommon;
import org.jowidgets.common.widgets.controler.IWindowListener;
import org.jowidgets.common.widgets.controler.impl.WindowObservable;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.swt.color.IColorCache;
import org.jowidgets.impl.swt.image.SwtImageRegistry;
import org.jowidgets.impl.swt.util.DimensionConvert;
import org.jowidgets.impl.swt.util.PositionConvert;
import org.jowidgets.spi.widgets.IWindowSpi;

public class SwtWindowWidget extends SwtContainerWidget implements IWindowSpi {

	private final WindowObservable windowObservableDelegate;

	private boolean programaticDispose;

	public SwtWindowWidget(
		final IGenericWidgetFactory factory,
		final IColorCache colorCache,
		final Shell window,
		final boolean isCloseable) {
		super(factory, colorCache, window);

		this.programaticDispose = false;
		this.windowObservableDelegate = new WindowObservable();

		getUiReference().addShellListener(new ShellListener() {

			@Override
			public void shellActivated(final ShellEvent e) {
				windowObservableDelegate.fireWindowActivated();
			}

			@Override
			public void shellDeactivated(final ShellEvent e) {
				windowObservableDelegate.fireWindowDeactivated();
			}

			@Override
			public void shellIconified(final ShellEvent e) {
				windowObservableDelegate.fireWindowIconified();
			}

			@Override
			public void shellDeiconified(final ShellEvent e) {
				windowObservableDelegate.fireWindowDeiconified();
			}

			@Override
			public void shellClosed(final ShellEvent e) {
				//Do not close the shell when 'X' is pressed, except
				//dispose method was invoked
				if (!programaticDispose) {
					e.doit = false;
					if (isCloseable) {
						getUiReference().setVisible(false);
					}
					else {
						return;
					}
				}
				windowObservableDelegate.fireWindowClosed();
			}
		});

		getUiReference().setBackgroundMode(SWT.INHERIT_DEFAULT);
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
		final boolean wasVisible = isVisible();
		if (visible) {
			getUiReference().open();
		}
		else {
			getUiReference().setVisible(false);
			if (wasVisible) {
				windowObservableDelegate.fireWindowClosed();
			}
		}
	}

	@Override
	public void dispose() {
		programaticDispose = true;
		if (isVisible()) {
			setVisible(false);
		}
		getUiReference().dispose();
		programaticDispose = false;
	}

	@Override
	public boolean isVisible() {
		return getUiReference().isVisible();
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
	public Rectangle getParentBounds() {
		final Shell shell = getUiReference();
		final Composite parentShell = shell.getParent();

		if (parentShell == null) {
			final org.eclipse.swt.graphics.Rectangle clientArea = Display.getCurrent().getPrimaryMonitor().getClientArea();
			return new Rectangle(new Position(clientArea.x, clientArea.y), new Dimension(clientArea.width, clientArea.height));
		}
		else {
			final Point location = parentShell.getLocation();
			final Point size = parentShell.getSize();
			return new Rectangle(new Position(location.x, location.y), new Dimension(size.x, size.y));
		}
	}

	@Override
	public <WIDGET_TYPE extends IDisplayCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
		final DESCRIPTOR_TYPE descriptor) {
		return getGenericWidgetFactory().create(getUiReference(), descriptor);
	}

	@Override
	public void addWindowListener(final IWindowListener listener) {
		windowObservableDelegate.addWindowListener(listener);
	}

	@Override
	public void removeWindowListener(final IWindowListener listener) {
		windowObservableDelegate.removeWindowListener(listener);
	}

}
