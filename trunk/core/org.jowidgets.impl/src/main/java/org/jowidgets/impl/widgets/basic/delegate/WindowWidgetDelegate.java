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

package org.jowidgets.impl.widgets.basic.delegate;

import org.jowidgets.api.look.AutoCenterPolicy;
import org.jowidgets.api.look.AutoPackPolicy;
import org.jowidgets.api.widgets.descriptor.setup.IWindowSetup;
import org.jowidgets.common.look.Dimension;
import org.jowidgets.common.look.Position;
import org.jowidgets.common.look.Rectangle;
import org.jowidgets.common.widgets.IWindowWidgetCommon;

public class WindowWidgetDelegate {

	private final AutoCenterPolicy autoCenterPolicy;
	private final AutoPackPolicy autoPackPolicy;
	private final IWindowWidgetCommon windowWidgetCi;
	private boolean wasVisible;

	public WindowWidgetDelegate(final IWindowWidgetCommon windowWidgetCi, final IWindowSetup setup) {
		this.autoCenterPolicy = setup.getAutoCenterPolicy();
		this.autoPackPolicy = setup.getAutoPackPolicy();
		this.windowWidgetCi = windowWidgetCi;
		this.wasVisible = false;
	}

	public void centerLocation() {
		final Rectangle parentBounds = windowWidgetCi.getParentBounds();
		final Dimension parentSize = parentBounds.getSize();
		final Position parentPosition = parentBounds.getPosition();

		final Dimension size = windowWidgetCi.getSize();

		final int posX = parentPosition.getX() + ((parentSize.getWidth() - size.getWidth()) / 2);
		final int posY = parentPosition.getY() + ((parentSize.getHeight() - size.getHeight()) / 2);

		windowWidgetCi.setPosition(new Position(posX, posY));
	}

	public void setVisible(final boolean visible) {
		if (visible) {
			if (AutoPackPolicy.ALLWAYS == autoPackPolicy) {
				windowWidgetCi.pack();
			}
			else if (!wasVisible && AutoPackPolicy.ONCE == autoPackPolicy) {
				windowWidgetCi.pack();
			}
			if (AutoCenterPolicy.ALLWAYS == autoCenterPolicy) {
				centerLocation();
			}
			else if (!wasVisible && AutoCenterPolicy.ONCE == autoCenterPolicy) {
				centerLocation();
			}
			wasVisible = true;
		}
		windowWidgetCi.setVisible(visible);
	}

}
