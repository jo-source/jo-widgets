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
package org.jowidgets.spi.impl.swing.common.widgets.base;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.jowidgets.spi.impl.swing.common.options.SwingOptions;

public class JoSplitPane extends JSplitPane {

	private static final long serialVersionUID = 5117985872625236987L;

	private boolean initialized = false;
	private final double dividerWeight;
	private final double resizeWeight;

	public JoSplitPane(final int orientation, final double dividerWeigth, final double resizeWeight) {
		super(orientation);
		this.initialized = false;
		this.dividerWeight = dividerWeigth;
		this.resizeWeight = resizeWeight;
		setOneTouchExpandable(SwingOptions.isOneTouchExpandableSplits());
	}

	@Override
	public void setDividerSize(int newSize) {
		if (SwingOptions.isOneTouchExpandableSplits()) {
			newSize = Math.max(newSize, 8);
		}
		super.setDividerSize(newSize);
	}

	@Override
	public void doLayout() {
		if (!initialized && this.isDisplayable()) {
			final boolean horizontalInitialized = getOrientation() == JSplitPane.HORIZONTAL_SPLIT && getSize().width != 0;
			final boolean verticalInitialized = getOrientation() == JSplitPane.VERTICAL_SPLIT && getSize().height != 0;
			if (horizontalInitialized || verticalInitialized) {
				initialized = true;
				setDividerLocation(dividerWeight);
				setResizeWeight(resizeWeight);
			}
		}
		super.doLayout();
	}

	@Override
	public void updateUI() {
		super.updateUI();

		final SplitPaneUI splitPaneUI = getUI();
		this.setBorder(BorderFactory.createEmptyBorder());
		if (splitPaneUI instanceof BasicSplitPaneUI) {
			final BasicSplitPaneUI basicUI = (BasicSplitPaneUI) splitPaneUI;
			basicUI.getDivider().setBorder(BorderFactory.createEmptyBorder());
		}
	}

}
