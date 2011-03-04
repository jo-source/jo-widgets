/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.tools.powo;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IProgressBar;
import org.jowidgets.api.widgets.blueprint.IProgressBarBluePrint;
import org.jowidgets.api.widgets.descriptor.IProgressBarDescriptor;

public class JoProgressBar extends ChildWidget<IProgressBar, IProgressBarBluePrint> implements IProgressBar {

	public JoProgressBar() {
		super(Toolkit.getBluePrintFactory().progressBar());
	}

	public JoProgressBar(final boolean indeterminate) {
		super(Toolkit.getBluePrintFactory().progressBar().setIndeterminate(indeterminate));
	}

	public JoProgressBar(final int maximum) {
		super(Toolkit.getBluePrintFactory().progressBar(maximum));
	}

	public JoProgressBar(final int minimum, final int maximum) {
		super(Toolkit.getBluePrintFactory().progressBar(minimum, maximum));
	}

	public JoProgressBar(final IProgressBarDescriptor descriptor) {
		super(Toolkit.getBluePrintFactory().progressBar().setSetup(descriptor));
	}

	@Override
	public void setMinimum(final int min) {
		if (isInitialized()) {
			getWidget().setMinimum(min);
		}
		else {
			getBluePrint().setMinimum(min);
		}
	}

	@Override
	public void setMaximum(final int max) {
		if (isInitialized()) {
			getWidget().setMaximum(max);
		}
		else {
			getBluePrint().setMaximum(max);
		}
	}

	@Override
	public int getMinimum() {
		if (isInitialized()) {
			return getWidget().getMinimum();
		}
		else {
			return getBluePrint().getMinimum();
		}
	}

	@Override
	public int getMaximum() {
		if (isInitialized()) {
			return getWidget().getMaximum();
		}
		else {
			return getBluePrint().getMaximum();
		}
	}

	@Override
	public void setIndeterminate(final boolean indeterminate) {
		if (isInitialized()) {
			getWidget().setIndeterminate(indeterminate);
		}
		else {
			getBluePrint().setIndeterminate(indeterminate);
		}
	}

	@Override
	public boolean isIndeterminate() {
		if (isInitialized()) {
			return getWidget().isIndeterminate();
		}
		else {
			return getBluePrint().isIndeterminate();
		}
	}

	@Override
	public void setProgress(final int progress) {
		if (isInitialized()) {
			getWidget().setProgress(progress);
		}
		else {
			getBluePrint().setProgress(progress);
		}
	}

	@Override
	public int getProgress() {
		if (isInitialized()) {
			return getWidget().getProgress();
		}
		else {
			return getBluePrint().getProgress();
		}
	}

	@Override
	public void setFinished() {
		checkInitialized();
		getWidget().setFinished();
	}

	@Override
	public boolean isFinished() {
		checkInitialized();
		return getWidget().isFinished();
	}

	public static IProgressBarBluePrint bluePrint() {
		return Toolkit.getBluePrintFactory().progressBar();
	}

	public static IProgressBarBluePrint bluePrint(final int maximum) {
		return Toolkit.getBluePrintFactory().progressBar(maximum);
	}

}
