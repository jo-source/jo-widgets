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

import org.jowidgets.addons.widgets.office.api.IOfficeControl;
import org.jowidgets.addons.widgets.office.api.IOfficeControlSetupBuilder;
import org.jowidgets.addons.widgets.ole.api.IOleAutomation;
import org.jowidgets.addons.widgets.ole.api.IOleControl;
import org.jowidgets.addons.widgets.ole.document.api.IOleDocument;
import org.jowidgets.addons.widgets.ole.document.tools.OleDocumentWrapper;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.util.concurrent.DaemonThreadFactory;
import org.jowidgets.util.event.ChangeObservable;
import org.jowidgets.util.event.IChangeListener;

final class OfficeControlImpl extends OleDocumentWrapper implements IOfficeControl {

	private static final String COUNT = "Count";
	private static final String VISIBLE = "Visible";
	private static final String COMMAND_BARS = "CommandBars";

	private final ChangeObservable dirtyStateChangeObservable;
	private final ScheduledExecutorService executorService;
	private final DirtyCheckRunnable dirtyCheckRunnable;
	private final int dirtyCheckInterval;
	private final boolean toolbarVisible;

	private ScheduledFuture<?> dirtyCheckSchedule;

	OfficeControlImpl(final IOleDocument oleDocument, final IOfficeControlSetupBuilder<?> setup) {
		super(oleDocument);

		this.dirtyStateChangeObservable = new ChangeObservable();
		this.executorService = Executors.newScheduledThreadPool(1, new DaemonThreadFactory());
		this.toolbarVisible = setup.getToolbarVisible();
		this.dirtyCheckInterval = setup.getDirtyCheckIntervalMs();
		this.dirtyCheckRunnable = new DirtyCheckRunnable();

		oleDocument.addDocumentChangeListener(new DocumenChangetListener());
		oleDocument.getOleControl().addFocusListener(new DirtyListener());

		setToolbarVisibility();

		setEnabled(false);
	}

	private void setToolbarVisibility() {
		if (!toolbarVisible) {
			final IOleControl oleControl = getOleControl();
			final IOleAutomation commandBars = oleControl.getAutomation().getProperty(COMMAND_BARS);
			if (commandBars != null) {
				final Integer commandBarsCount = commandBars.getProperty(COUNT);
				if (commandBarsCount != null) {
					for (int i = 0; i < commandBarsCount.intValue(); i++) {
						final IOleAutomation commandBar = oleControl.getAutomation().getProperty(COMMAND_BARS, i);
						if (commandBar != null) {
							commandBar.setProperty(VISIBLE, toolbarVisible);
							commandBar.dispose();
						}
					}
				}
				commandBars.dispose();
			}
		}
	}

	@Override
	public void addDirtyStateListener(final IChangeListener changeListener) {
		dirtyStateChangeObservable.addChangeListener(changeListener);
	}

	@Override
	public void removeDirtyStateListener(final IChangeListener changeListener) {
		dirtyStateChangeObservable.removeChangeListener(changeListener);
	}

	private class DirtyListener implements IFocusListener {
		@Override
		public void focusGained() {
			startDirtyChecking();
		}

		@Override
		public void focusLost() {
			stopDirtyChecking();
		}
	}

	private void startDirtyChecking() {
		if (dirtyCheckSchedule == null) {
			dirtyCheckSchedule = executorService.scheduleAtFixedRate(
					dirtyCheckRunnable,
					0,
					dirtyCheckInterval,
					TimeUnit.MILLISECONDS);
		}
	}

	private void stopDirtyChecking() {
		if (dirtyCheckSchedule != null) {
			dirtyCheckSchedule.cancel(true);
			dirtyCheckSchedule = null;
		}
	}

	private final class DirtyCheckRunnable implements Runnable {

		private final IUiThreadAccess uiThreadAccess;
		private boolean currentDirtyState;

		private DirtyCheckRunnable() {
			this.uiThreadAccess = Toolkit.getUiThreadAccess();
			this.currentDirtyState = isDirty();
		}

		@Override
		public void run() {
			uiThreadAccess.invokeLater(new Runnable() {
				@Override
				public void run() {
					final boolean newDirtyState = getOleControl().isDirty();
					if (currentDirtyState != newDirtyState) {
						currentDirtyState = newDirtyState;
						dirtyStateChangeObservable.fireChangedEvent();
					}
				}
			});
		}
	}

	private class DocumenChangetListener implements IChangeListener {
		@Override
		public void changed() {
			setToolbarVisibility();
		}
	}

}
