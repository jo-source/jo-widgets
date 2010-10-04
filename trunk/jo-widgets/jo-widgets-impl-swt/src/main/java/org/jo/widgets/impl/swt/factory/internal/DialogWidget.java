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
package org.jo.widgets.impl.swt.factory.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jo.widgets.api.look.AutoCenterPolicy;
import org.jo.widgets.api.look.AutoPackPolicy;
import org.jo.widgets.api.util.ColorSettingsInvoker;
import org.jo.widgets.api.widgets.IDialogWidget;
import org.jo.widgets.api.widgets.IWidget;
import org.jo.widgets.api.widgets.descriptor.base.IBaseDialogDescriptor;
import org.jo.widgets.api.widgets.factory.IGenericWidgetFactory;
import org.jo.widgets.impl.swt.internal.color.IColorCache;
import org.jo.widgets.impl.swt.internal.image.SwtImageRegistry;
import org.jo.widgets.impl.swt.widgets.SwtWindowWidget;

public class DialogWidget extends SwtWindowWidget implements IDialogWidget {

	private final IWidget parent;
	private final IBaseDialogDescriptor<?> descriptor;
	private boolean wasVisible;

	public DialogWidget(
		final IGenericWidgetFactory factory,
		final IColorCache colorCache,
		final SwtImageRegistry imageRegistry,
		final IWidget parent,
		final IBaseDialogDescriptor<?> descriptor) {
		super(factory, colorCache, new Shell((Shell) parent.getUiReference(), SWT.DIALOG_TRIM
			| SWT.APPLICATION_MODAL
			| SWT.RESIZE), descriptor.getAutoPackPolicy());

		this.parent = parent;
		this.descriptor = descriptor;
		this.wasVisible = false;

		if (descriptor.getTitle() != null) {
			getUiReference().setText(descriptor.getTitle());
		}

		setLayout(descriptor.getLayout());
		setIcon(imageRegistry, descriptor.getIcon());
		ColorSettingsInvoker.setColors(descriptor, this);
	}

	@Override
	public IWidget getParent() {
		return parent;
	}

	@Override
	public void centerLocation() {
		final Shell shell = getUiReference();
		final Composite parentShell = shell.getParent();

		Point parentSize = null;

		int x = 0;
		int y = 0;

		if (parentShell == null) {
			final Rectangle clientArea = Display.getCurrent().getClientArea();
			parentSize = new Point(clientArea.height, clientArea.width);
		}
		else {
			parentSize = parentShell.getSize();
			x = parentShell.getLocation().x;
			y = parentShell.getLocation().y;
		}

		shell.setLocation(x + ((parentSize.x - shell.getSize().x) / 2), y + ((parentSize.y - shell.getSize().y) / 2));

	}

	@Override
	public void setVisible(final boolean visible) {
		if (visible) {

			if (AutoPackPolicy.ALLWAYS.equals(descriptor.getAutoPackPolicy())) {
				pack();
			}
			else if (!wasVisible && AutoPackPolicy.ONCE.equals(descriptor.getAutoPackPolicy())) {
				pack();
			}
			if (AutoCenterPolicy.ALLWAYS.equals(descriptor.getAutoCenterPolicy())) {
				centerLocation();
			}
			else if (!wasVisible && AutoCenterPolicy.ONCE.equals(descriptor.getAutoCenterPolicy())) {
				centerLocation();
			}
			wasVisible = true;

			getUiReference().setVisible(true);

			// do not dispose the shell on closing
			getUiReference().addShellListener(new ShellAdapter() {

				@Override
				public void shellClosed(final ShellEvent e) {
					e.doit = false;
					setVisible(false);
				}

			});

			final Shell shell = getUiReference();
			final Display display = shell.getDisplay();

			while (!shell.isDisposed() && shell.isVisible()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}

		}
		else {
			getUiReference().setVisible(false);
		}
	}

}
