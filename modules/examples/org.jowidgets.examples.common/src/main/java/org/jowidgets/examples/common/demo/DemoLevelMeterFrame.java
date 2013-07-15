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

package org.jowidgets.examples.common.demo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jowidgets.api.model.levelmeter.IMutableLevelMeterModel;
import org.jowidgets.api.model.levelmeter.MutableLevelMeterModel;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.powo.JoFrame;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.concurrent.DaemonThreadFactory;

public class DemoLevelMeterFrame extends JoFrame {

	private static final int NUMBER_OF_BARS = 10;

	public DemoLevelMeterFrame() {
		super("Level meter demo");

		final String migLayoutColumnDescriptor = getColumnDescriptor();

		setLayout(new MigLayoutDescriptor(migLayoutColumnDescriptor, "[grow, 0::]"));

		final IMutableLevelMeterModel[] models = new IMutableLevelMeterModel[NUMBER_OF_BARS];

		for (int i = 0; i < NUMBER_OF_BARS; i++) {
			models[i] = MutableLevelMeterModel.create();
			add(BPF.levelMeter(models[i]).setLetteringVisible(true), "growx, w 70!, growy, h 0::");
		}

		final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1, new DaemonThreadFactory());

		final IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();
		final Runnable modelChangeRunnable = new Runnable() {

			@Override
			public void run() {
				uiThreadAccess.invokeLater(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i < NUMBER_OF_BARS; i++) {
							final double oldLevel = models[i].getLevel();
							if (oldLevel == 0) {
								models[i].setLevel(Math.random());
							}
							else {
								final double variance = Math.random() / 10;
								double newLevel = oldLevel;
								double plusMinus = Math.random();
								if (oldLevel > 0.89) {
									plusMinus = 0.0;
								}
								if (oldLevel < 0.11) {
									plusMinus = 1.0;
								}
								if (plusMinus < 0.5) {
									newLevel = newLevel - variance;
								}
								else {
									newLevel = newLevel + variance;
								}
								models[i].setLevel(newLevel);
							}
						}
					}
				});
			}
		};

		threadPool.scheduleWithFixedDelay(modelChangeRunnable, 0, 100, TimeUnit.MILLISECONDS);

	}

	private String getColumnDescriptor() {
		final StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < NUMBER_OF_BARS; i++) {
			stringBuilder.append("[70!]");
		}
		return stringBuilder.toString();
	}
}
