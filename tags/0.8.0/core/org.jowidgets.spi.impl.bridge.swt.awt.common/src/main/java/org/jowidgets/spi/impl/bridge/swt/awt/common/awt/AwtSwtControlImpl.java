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
package org.jowidgets.spi.impl.bridge.swt.awt.common.awt;

import java.awt.Canvas;
import java.awt.Container;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.SwingUtilities;

import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.spi.impl.bridge.swt.awt.common.application.BridgedSwtAwtApplicationRunner;
import org.jowidgets.spi.impl.swing.common.widgets.SwingControl;
import org.jowidgets.util.Assert;

class AwtSwtControlImpl extends SwingControl implements IAwtSwtControlSpi {

	private Composite composite;

	public AwtSwtControlImpl(final Object parentUiReference) {
		super(new Canvas());
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalArgumentException("The AwtSwtControl must be created in the event dispatching thread of swing");
		}

		Assert.paramHasType(parentUiReference, Container.class, "parentUiReference");
		final Container parentSwtContainer = (Container) parentUiReference;
		if (!parentSwtContainer.isDisplayable()) {
			synchronized (parentSwtContainer.getTreeLock()) {
				parentSwtContainer.addNotify();
			}
		}

		//TODO MG the composite will not be created before the SwingContainer impl has added this control to its container
		//unfortunately this will be happen AFTER the whole widgets was created. 
		getUiReference().addHierarchyListener(new HierarchyListener() {
			@Override
			public void hierarchyChanged(final HierarchyEvent e) {
				if (getUiReference().getParent() != null && composite == null) {
					final Display currentDisplay = Display.getCurrent();
					if (currentDisplay != null) {
						final Shell shell = SWT_AWT.new_Shell(currentDisplay, getUiReference());
						shell.setLayout(new FillLayout());
						composite = shell;
					}
					else {
						throw new IllegalStateException("This thread has no swt display. "
							+ "To ensure that the awt event dispatching thread has a display, e.g. the class "
							+ "'"
							+ BridgedSwtAwtApplicationRunner.class.getName()
							+ "' could be used, or, if no application "
							+ "runner should be used, use the single ui thread pattern implemented there.");
					}
				}
			}
		});

	}

	@Override
	public Canvas getUiReference() {
		return (Canvas) super.getUiReference();
	}

	@Override
	public Composite getSwtComposite() {
		return composite;
	}

}
