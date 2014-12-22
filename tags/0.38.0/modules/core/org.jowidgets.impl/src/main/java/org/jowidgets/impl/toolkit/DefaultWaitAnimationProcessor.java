/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.impl.toolkit;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jowidgets.api.animation.IWaitAnimationProcessor;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.concurrent.DaemonThreadFactory;
import org.jowidgets.util.event.IChangeListener;

final class DefaultWaitAnimationProcessor implements IWaitAnimationProcessor {

	private static final String TEXT_0 = "";
	private static final String TEXT_1 = ".";
	private static final String TEXT_2 = "..";
	private static final String TEXT_3 = "...";

	private final Set<IChangeListener> changeListeners;
	private final IUiThreadAccess uiThreadAccess;
	private final ScheduledExecutorService threadPool;

	private ScheduledFuture<?> scheduledFuture;
	private int animationState;

	private IImageConstant icon;
	private String text;

	DefaultWaitAnimationProcessor() {
		super();
		this.uiThreadAccess = Toolkit.getUiThreadAccess();
		this.threadPool = Executors.newScheduledThreadPool(1, new DaemonThreadFactory());
		this.changeListeners = new HashSet<IChangeListener>();
		this.animationState = 0;
		this.icon = IconsSmall.WAIT_1;
		this.text = "";
	}

	@Override
	public void addChangeListener(final IChangeListener listener) {
		changeListeners.add(listener);
		listener.changed();
		if (changeListeners.size() == 1) {
			start();
		}
	}

	@Override
	public void removeChangeListener(final IChangeListener listener) {
		changeListeners.remove(listener);
		if (changeListeners.size() == 0) {
			stop();
		}
	}

	@Override
	public IImageConstant getWaitIcon() {
		return icon;
	}

	@Override
	public String getWaitText() {
		return text;
	}

	private void start() {
		if (scheduledFuture == null) {
			final Runnable labelAnimationRunnable = new Runnable() {
				@Override
				public void run() {
					nextStep();
				}
			};
			this.scheduledFuture = threadPool.scheduleAtFixedRate(labelAnimationRunnable, 500, 250, TimeUnit.MILLISECONDS);
		}
	}

	private void stop() {
		if (scheduledFuture != null) {
			scheduledFuture.cancel(false);
			scheduledFuture = null;
		}
	}

	private void nextStep() {
		if (animationState == 0) {
			icon = IconsSmall.WAIT_1;
			text = TEXT_0;
		}
		else if (animationState == 1) {
			icon = IconsSmall.WAIT_2;
			text = TEXT_1;
		}
		else if (animationState == 2) {
			icon = IconsSmall.WAIT_3;
			text = TEXT_2;
		}
		else if (animationState == 3) {
			icon = IconsSmall.WAIT_4;
			text = TEXT_3;
		}

		if (animationState == 3) {
			animationState = 0;
		}
		else {
			animationState++;
		}

		fireChangedEventLater();
	}

	private void fireChangedEventLater() {
		uiThreadAccess.invokeLater(new Runnable() {
			@Override
			public void run() {
				fireChangedEvent();
			}
		});
	}

	private void fireChangedEvent() {
		for (final IChangeListener listener : new LinkedList<IChangeListener>(changeListeners)) {
			listener.changed();
		}
	}

}
