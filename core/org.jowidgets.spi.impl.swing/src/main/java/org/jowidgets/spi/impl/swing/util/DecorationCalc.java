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
package org.jowidgets.spi.impl.swing.util;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;

import org.jowidgets.common.types.Rectangle;

public final class DecorationCalc {

	private DecorationCalc() {};

	public static Rectangle getClientArea(final Container container) {
		final Insets insets = container.getInsets();
		final int x = insets.left;
		final int y = insets.top;
		final Dimension size = container.getSize();
		final int width = (int) (size.getWidth() - insets.left - insets.right);
		final int height = (int) (size.getHeight() - insets.top - insets.bottom);
		return new Rectangle(x, y, width, height);
	}

	public static org.jowidgets.common.types.Dimension computeDecoratedSize(
		final Container container,
		final org.jowidgets.common.types.Dimension clientAreaSize) {

		int width = clientAreaSize.getWidth();
		int height = clientAreaSize.getHeight();

		final Insets insets = container.getInsets();
		if (insets != null) {
			width = width + insets.left + insets.right;
			height = height + insets.top + insets.bottom;
		}

		JMenuBar menuBar = null;

		if (container instanceof JFrame) {
			menuBar = ((JFrame) container).getJMenuBar();
		}
		else if (container instanceof JDialog) {
			menuBar = ((JDialog) container).getJMenuBar();
		}

		if (menuBar != null) {
			height = height + menuBar.getPreferredSize().height;
		}

		if (container instanceof JTabbedPane) {
			@SuppressWarnings("unused")
			final JTabbedPane tabbedPane = (JTabbedPane) container;
			// TODO NM add tab height here
		}

		return new org.jowidgets.common.types.Dimension(width, height);
	}

}
