/*
 * Copyright (c) 2012, grossmann, waheckma
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

package org.jowidgets.addons.widgets.office.impl.ole;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jowidgets.addons.widgets.office.api.IOfficeControl;
import org.jowidgets.addons.widgets.office.api.IOfficeControlSetupBuilder;
import org.jowidgets.addons.widgets.ole.api.IOleAutomation;
import org.jowidgets.addons.widgets.ole.api.IOleContext;
import org.jowidgets.addons.widgets.ole.document.api.IOleDocument;
import org.jowidgets.addons.widgets.ole.document.tools.OleDocumentWrapper;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.util.IMutableValueListener;
import org.jowidgets.util.IValueChangedEvent;
import org.jowidgets.util.concurrent.DaemonThreadFactory;
import org.jowidgets.util.event.ChangeObservable;
import org.jowidgets.util.event.IChangeListener;

class OfficeControlImpl extends OleDocumentWrapper implements IOfficeControl {

	private final IOleDocument oleDocument;
	private final ChangeObservable changeObservable;
	private boolean currentDirtyState;
	private boolean newDirtyState;
	private final ScheduledExecutorService executorService;
	private ScheduledFuture<?> scheduleAtFixedRate;
	private final AtomicBoolean running;
	private boolean toolbarVisible;

	public OfficeControlImpl(final IOleDocument oleDocument, final IOfficeControlSetupBuilder<?> setup) {
		super(oleDocument);
		this.oleDocument = oleDocument;
		this.changeObservable = new ChangeObservable();
		this.executorService = Executors.newScheduledThreadPool(1, new DaemonThreadFactory());
		this.toolbarVisible = setup.getToolbarVisible();
		this.running = new AtomicBoolean(false);
		if (oleDocument != null) {
			oleDocument.getOleControl().addFocusListener(new DirtyListener());
			oleDocument.getOleControl().getContext().addMutableValueListener(new ContextListener());
		}
	}

	private void setToolbarVisibility() {
		if (!toolbarVisible && (oleDocument != null)) {
			final IOleContext context = oleDocument.getOleControl().getContext().getValue();
			if (context != null) {
				final IOleAutomation commandBars = (IOleAutomation) context.getAutomation().getProperty("CommandBars");
				if (commandBars != null) {
					final int commandBarsCount = (Integer) commandBars.getProperty("Count");
					for (int i = 0; i < commandBarsCount; i++) {
						final IOleAutomation commandBarAutomation = (IOleAutomation) context.getAutomation().getProperty(
								"CommandBars",
								i);
						if (commandBarAutomation != null) {
							commandBarAutomation.setProperty("Visible", toolbarVisible);
							commandBarAutomation.dispose();
						}
					}
					commandBars.dispose();
				}
			}
		}

	}

	@Override
	public void setToolbarVisible(final boolean visible) {
		if (toolbarVisible != visible) {
			toolbarVisible = visible;
			setToolbarVisibility();
		}
	}

	@Override
	public void addDocumentChangeListener(final IChangeListener changeListener) {
		changeObservable.addChangeListener(changeListener);
	}

	private class DirtyListener implements IFocusListener {

		@Override
		public void focusGained() {
			if (!running.get()) {
				checkDirtyState();
			}
		}

		@Override
		public void focusLost() {
			running.set(false);
			scheduleAtFixedRate.cancel(true);
		}
	}

	private void checkDirtyState() {
		running.set(true);
		final IUiThreadAccess access = Toolkit.getUiThreadAccess();
		scheduleAtFixedRate = executorService.scheduleAtFixedRate((new Runnable() {
			@Override
			public void run() {
				access.invokeLater(new Runnable() {
					@Override
					public void run() {
						final IOleContext value = oleDocument.getOleControl().getContext().getValue();
						if (value != null) {
							newDirtyState = value.isDirty();
						}
						else {
							running.set(false);
						}
					}
				});

				if (currentDirtyState != newDirtyState) {
					currentDirtyState = newDirtyState;
					changeObservable.fireChangedEvent();
				}
				if (!running.get()) {
					scheduleAtFixedRate.cancel(true);
				}
			}
		}), 0, 1000, TimeUnit.MILLISECONDS);
	}

	private class ContextListener implements IMutableValueListener<IOleContext> {

		@Override
		public void changed(final IValueChangedEvent<IOleContext> event) {
			setToolbarVisibility();
		}
	}

	@Override
	public void removeDocumentChangeListener(final IChangeListener changeListener) {
		changeObservable.removeChangeListener(changeListener);
	}

}
