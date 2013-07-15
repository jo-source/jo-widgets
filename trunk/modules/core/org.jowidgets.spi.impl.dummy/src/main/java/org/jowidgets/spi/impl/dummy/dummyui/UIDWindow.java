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

package org.jowidgets.spi.impl.dummy.dummyui;

import org.jowidgets.common.types.Dimension;

public class UIDWindow extends UIDContainer {

	private UIDImage iconImage;
	private final UIDWindow parent;
	private boolean maximized;
	private boolean iconfied;

	public UIDWindow(final UIDWindow parent) {
		super();
		this.parent = parent;
		setVisible(false);
	}

	public void setIconImage(final UIDImage image) {
		this.iconImage = image;
	}

	public UIDImage getIconImage() {
		return iconImage;
	}

	public UIDContainer getParent() {
		return parent;
	}

	public void pack() {}

	public void dispose() {
		if (isVisible()) {
			setVisible(false);
		}
		fireWindowClosed();
	}

	public void setMinSize(final Dimension minSize) {
		// TODO LG implement setMinSize()
	}

	public void setTitle(final String title) {

	}

	public void setMaximized(final boolean maximized) {
		this.maximized = maximized;
	}

	public boolean isMaximized() {
		return maximized;
	}

	public void setIconfied(final boolean iconfied) {
		this.iconfied = iconfied;
	}

	public boolean isIconfied() {
		return iconfied;
	}

}
