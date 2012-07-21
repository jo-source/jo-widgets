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

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.spi.impl.bridge.swt.awt.common.awt.PeerObservablePanel.IPeerListener;
import org.jowidgets.spi.impl.swing.common.widgets.SwingControl;
import org.jowidgets.util.IMutableValue;
import org.jowidgets.util.MutableValue;

class AwtSwtControlImpl extends SwingControl implements IAwtSwtControlSpi {

	private final MutableValue<Composite> mutableValue;
	private final Shell backboneShell;
	private final Composite composite;

	private boolean initialized;
	private Shell bridgeShell;

	public AwtSwtControlImpl(final Object parentUiReference) {
		super(new PeerObservablePanel());

		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalArgumentException("The AwtSwtControl must be created in the event dispatching thread of swing");
		}

		this.initialized = false;
		this.backboneShell = new Shell(getCurrentDisplay());
		this.backboneShell.setLayout(new FillLayout());
		this.composite = new Composite(backboneShell, SWT.NONE);
		this.composite.setLayout(new FillLayout());
		this.mutableValue = new MutableValue<Composite>(composite);

		getUiReference().setLayout(new BorderLayout());
		getUiReference().addPeerListener(new IPeerListener() {

			@Override
			public void afterPeerAdd() {
				//If initialize will be invoked here, there is a flicker artifact
				//that will not occur if initialize will be invoked in the 
				//HierarchyListener
			}

			@Override
			public void beforePeerRemove() {
				onPeerRemoved();
			}

		});
		getUiReference().addHierarchyListener(new HierarchyListener() {
			@Override
			public void hierarchyChanged(final HierarchyEvent e) {
				if (!initialized && getUiReference().isDisplayable()) {
					onPeerAdded();
				}
			}
		});
	}

	private static Display getCurrentDisplay() {
		final Display currentDisplay = Display.getCurrent();
		if (currentDisplay != null) {
			return currentDisplay;
		}
		else {
			throw new IllegalStateException("This thread has no swt display. "
				+ "To ensure that the awt event dispatching thread has a display, e.g. the class "
				+ "'"
				+ "BridgedSwtAwtApplicationRunner"
				+ "' could be used, or, if no application "
				+ "runner should be used, use the single ui thread pattern implemented there.");
		}
	}

	private void onPeerAdded() {
		final Canvas canvas = new Canvas();
		getUiReference().removeAll();
		getUiReference().add(BorderLayout.CENTER, canvas);
		bridgeShell = SWT_AWT.new_Shell(getCurrentDisplay(), canvas);
		bridgeShell.setLayout(new FillLayout());
		composite.setParent(bridgeShell);
		initialized = true;
	}

	private void onPeerRemoved() {
		composite.setParent(backboneShell);
		tryToDispose(bridgeShell);
		bridgeShell = null;
		initialized = false;
	}

	@Override
	public PeerObservablePanel getUiReference() {
		return (PeerObservablePanel) super.getUiReference();
	}

	@Override
	public IMutableValue<Composite> getSwtComposite() {
		return mutableValue;
	}

	@Override
	public void dispose() {
		tryToDispose(composite);
		tryToDispose(backboneShell);
		tryToDispose(bridgeShell);
	}

	private static void tryToDispose(final Composite composite) {
		if (composite != null && !composite.isDisposed()) {
			composite.dispose();
		}
	}
}
