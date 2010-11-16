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

import org.jowidgets.api.widgets.ICompositeWidget;
import org.jowidgets.api.widgets.IProgressBarWidget;
import org.jowidgets.api.widgets.descriptor.setup.IProgressBarSetup;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.util.ColorSettingsInvoker;
import org.jowidgets.common.widgets.IWidget;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.spi.ISpiBluePrintFactory;
import org.jowidgets.impl.spi.SpiBluePrintFactory;
import org.jowidgets.impl.spi.blueprint.IProgressBarBluePrintSpi;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.widgets.IProgressBarWidgetSpi;

public class ProgressBarWidget implements IProgressBarWidget {

	private final ICompositeWidget compositeWidget;
	private final IProgressBarWidgetSpi indeterminateProgressBar;
	private final IProgressBarWidgetSpi progressBar;

	private int maximum;
	private int minimum;
	private int progress;
	private boolean isIndeterminate;

	public ProgressBarWidget(
		final ICompositeWidget compositeWidget,
		final IProgressBarSetup setup,
		final IWidgetFactorySpi widgetsFactorySpi) {

		this.compositeWidget = compositeWidget;

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

		this.compositeWidget.setLayout(layoutDescriptor);

		final ISpiBluePrintFactory spiBpf = new SpiBluePrintFactory();

		final IProgressBarBluePrintSpi intermediateProgressBarBp = spiBpf.progressBar();
		intermediateProgressBarBp.setSetup(setup).setIndeterminate(true);

		final IProgressBarBluePrintSpi progressBarBp = spiBpf.progressBar();
		progressBarBp.setSetup(setup).setIndeterminate(false);

		this.indeterminateProgressBar = compositeWidget.add(new ICustomWidgetFactory<IProgressBarWidgetSpi>() {

			@Override
			public IProgressBarWidgetSpi create(
				final IWidget parent,
				final IWidgetFactory<IProgressBarWidgetSpi, IWidgetDescriptor<? extends IProgressBarWidgetSpi>> widgetFactory) {
				return widgetsFactorySpi.createProgressBar(compositeWidget, intermediateProgressBarBp);
			}
		}, componentLayoutConstraints);

		this.progressBar = compositeWidget.add(new ICustomWidgetFactory<IProgressBarWidgetSpi>() {

			@Override
			public IProgressBarWidgetSpi create(
				final IWidget parent,
				final IWidgetFactory<IProgressBarWidgetSpi, IWidgetDescriptor<? extends IProgressBarWidgetSpi>> widgetFactory) {
				return widgetsFactorySpi.createProgressBar(compositeWidget, progressBarBp);
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
	public IWidget getParent() {
		return compositeWidget.getParent();
	}

	@Override
	public Object getUiReference() {
		return compositeWidget.getUiReference();
	}

	@Override
	public void redraw() {
		compositeWidget.redraw();
	}

	@Override
	public void setVisible(final boolean visible) {
		compositeWidget.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return compositeWidget.isVisible();
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
			compositeWidget.layoutBegin();
			setIndeterminatState(indeterminate);
			compositeWidget.layoutEnd();
			compositeWidget.redraw();
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

}
