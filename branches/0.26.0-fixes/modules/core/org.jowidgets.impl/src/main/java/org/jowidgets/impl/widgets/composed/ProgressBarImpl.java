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
package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IProgressBar;
import org.jowidgets.api.widgets.descriptor.setup.IProgressBarSetup;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.widgets.IProgressBarCommon;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.spi.ISpiBluePrintFactory;
import org.jowidgets.impl.spi.SpiBluePrintFactory;
import org.jowidgets.impl.spi.blueprint.IProgressBarBluePrintSpi;
import org.jowidgets.impl.widgets.basic.ProgressBarCommonToControl;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.widgets.IProgressBarSpi;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;

public class ProgressBarImpl extends ControlWrapper implements IProgressBar {

	private final IComposite composite;
	private final IProgressBarCommon indeterminateProgressBar;
	private final IProgressBarCommon progressBar;

	private int maximum;
	private int minimum;
	private int progress;
	private boolean isIndeterminate;

	public ProgressBarImpl(final IComposite composite, final IProgressBarSetup setup, final IWidgetFactorySpi widgetsFactorySpi) {

		super(composite);

		this.composite = composite;

		final String componentLayoutConstraints;
		if (setup.getOrientation() == Orientation.HORIZONTAL) {
			componentLayoutConstraints = "growx, growy, hidemode 3";
		}
		else {
			componentLayoutConstraints = "growy, growx, hidemode 3";
		}

		final ILayoutDescriptor layoutDescriptor;
		if (setup.getOrientation() == Orientation.HORIZONTAL) {
			layoutDescriptor = new MigLayoutDescriptor("0[grow]0", "0[grow]0");
		}
		else {
			layoutDescriptor = new MigLayoutDescriptor("0[grow]0", "0[grow][grow]0");
		}

		this.composite.setLayout(layoutDescriptor);

		final ISpiBluePrintFactory spiBpf = new SpiBluePrintFactory();

		final IProgressBarBluePrintSpi intermediateProgressBarBp = spiBpf.progressBar();
		intermediateProgressBarBp.setSetup(setup).setIndeterminate(true);

		final IProgressBarBluePrintSpi progressBarBp = spiBpf.progressBar();
		progressBarBp.setSetup(setup).setIndeterminate(false);

		this.indeterminateProgressBar = composite.add(new ICustomWidgetCreator<ProgressBarCommonToControl>() {
			@Override
			public ProgressBarCommonToControl create(final ICustomWidgetFactory widgetFactory) {
				final IProgressBarSpi progressBarSpi = widgetsFactorySpi.createProgressBar(
						getUiReference(),
						intermediateProgressBarBp);
				return new ProgressBarCommonToControl(progressBarSpi);
			}
		},
				componentLayoutConstraints);

		this.progressBar = composite.add(new ICustomWidgetCreator<ProgressBarCommonToControl>() {
			@Override
			public ProgressBarCommonToControl create(final ICustomWidgetFactory widgetFactory) {
				final IProgressBarSpi progressBarSpi = widgetsFactorySpi.createProgressBar(getUiReference(), progressBarBp);
				return new ProgressBarCommonToControl(progressBarSpi);
			}
		}, componentLayoutConstraints);

		this.minimum = setup.getMinimum();
		progressBar.setMinimum(this.minimum);

		this.maximum = setup.getMaximum();
		progressBar.setMaximum(this.maximum);

		this.progress = setup.getProgress();
		progressBar.setProgress(progress);

		setIndeterminatState(setup.isIndeterminate());

		ColorSettingsInvoker.setColors(setup, this);
		VisibiliySettingsInvoker.setVisibility(setup, this);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		indeterminateProgressBar.setEnabled(enabled);
		progressBar.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return indeterminateProgressBar.isEnabled();
	}

	@Override
	public int getMinimum() {
		return minimum;
	}

	@Override
	public int getMaximum() {
		return maximum;
	}

	@Override
	public int getProgress() {
		return progress;
	}

	@Override
	public void setIndeterminate(final boolean indeterminate) {
		if (this.isIndeterminate != indeterminate) {
			composite.layoutBegin();
			setIndeterminatState(indeterminate);
			composite.layoutEnd();
		}
	}

	private void setIndeterminatState(final boolean indeterminate) {
		this.isIndeterminate = indeterminate;
		if (indeterminate) {
			indeterminateProgressBar.setVisible(true);
			progressBar.setVisible(false);
		}
		else {
			progressBar.setVisible(true);
			this.indeterminateProgressBar.setVisible(false);
		}
	}

	@Override
	public boolean isIndeterminate() {
		return isIndeterminate;
	}

	@Override
	public void setMinimum(final int min) {
		this.minimum = min;
		progressBar.setMinimum(min);
		if (isIndeterminate) {
			setIndeterminate(false);
		}
	}

	@Override
	public void setMaximum(final int max) {
		this.maximum = max;
		progressBar.setMaximum(max);
		if (isIndeterminate) {
			setIndeterminate(false);
		}
	}

	@Override
	public void setProgress(final int progress) {
		progressBar.setProgress(progress);
		if (isIndeterminate) {
			setIndeterminate(false);
		}
	}

	@Override
	public void setFinished() {
		indeterminateProgressBar.setProgress(maximum);
		progressBar.setProgress(maximum);
		if (isIndeterminate) {
			setIndeterminate(false);
		}
	}

	@Override
	public boolean isFinished() {
		return maximum == progress;
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		indeterminateProgressBar.setForegroundColor(colorValue);
		progressBar.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		indeterminateProgressBar.setBackgroundColor(colorValue);
		progressBar.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		if (isIndeterminate) {
			return indeterminateProgressBar.getForegroundColor();
		}
		else {
			return progressBar.getForegroundColor();
		}
	}

	@Override
	public IColorConstant getBackgroundColor() {
		if (isIndeterminate) {
			return indeterminateProgressBar.getBackgroundColor();
		}
		else {
			return progressBar.getBackgroundColor();
		}
	}

	@Override
	public void setCursor(final Cursor cursor) {
		indeterminateProgressBar.setCursor(cursor);
		progressBar.setCursor(cursor);
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenu) {
		//TODO MG this might not work, popup must be set on progressBar and indeterminateProgressBar also. 
		//For that, progressBar and indeterminateProgressBar must be api widgets 
		composite.setPopupMenu(popupMenu);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		composite.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		composite.removePopupDetectionListener(listener);
	}

	@Override
	public void setToolTipText(final String toolTip) {
		composite.setToolTipText(toolTip);
		indeterminateProgressBar.setToolTipText(toolTip);
		progressBar.setToolTipText(toolTip);
	}

}
