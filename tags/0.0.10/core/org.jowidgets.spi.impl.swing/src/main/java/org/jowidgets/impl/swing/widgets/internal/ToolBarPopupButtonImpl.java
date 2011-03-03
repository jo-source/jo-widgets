/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.impl.swing.widgets.internal;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.UIManager;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.controler.impl.PopupDetectionObservable;
import org.jowidgets.spi.widgets.IToolBarPopupButtonSpi;

public class ToolBarPopupButtonImpl extends ToolBarButtonImpl implements IToolBarPopupButtonSpi {

	private static final Icon ARROW_ICON = new ArrowIcon();

	private final PopupDetectionObservable popupDetectionObservable;
	private final JButton buttonArrow;

	public ToolBarPopupButtonImpl(final JButton button, final JButton buttonArrow) {
		super(button);

		this.popupDetectionObservable = new PopupDetectionObservable();
		this.buttonArrow = buttonArrow;

		buttonArrow.setFocusable(false);
		buttonArrow.setIcon(ARROW_ICON);
		adjustArrowSize();

		button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(final MouseEvent e) {
				buttonArrow.getModel().setSelected(false);
			}

			@Override
			public void mousePressed(final MouseEvent e) {
				buttonArrow.getModel().setSelected(true);
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				buttonArrow.getModel().setRollover(false);
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
				buttonArrow.getModel().setRollover(true);
			}

		});

		buttonArrow.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				final Point menuPosition = button.getLocation();
				popupDetectionObservable.firePopupDetected(new Position(menuPosition.x, menuPosition.y + button.getHeight()));
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				button.getModel().setRollover(false);
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
				button.getModel().setRollover(true);
			}

		});

	}

	@Override
	public void setText(final String text) {
		super.setText(text);
		adjustArrowSize();
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		super.setIcon(icon);
		adjustArrowSize();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		buttonArrow.setEnabled(enabled);
	}

	@Override
	public void setToolTipText(final String text) {
		super.setToolTipText(text);
		buttonArrow.setToolTipText(text);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionObservable.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionObservable.removePopupDetectionListener(listener);
	}

	private void adjustArrowSize() {
		buttonArrow.setMaximumSize(new Dimension(ARROW_ICON.getIconWidth() + 4, getUiReference().getMaximumSize().height));
	}

	private static class ArrowIcon implements Icon {

		private static final int HEIGHT = 4;
		private static final int WIDTH = 10;

		@Override
		public int getIconWidth() {
			return WIDTH;
		}

		@Override
		public int getIconHeight() {
			return HEIGHT;
		}

		@Override
		public void paintIcon(final Component component, final Graphics graphics, final int x, final int y) {
			final AbstractButton button = (AbstractButton) component;
			final ButtonModel buttonModel = button.getModel();

			final int width = WIDTH - 2;
			final int height = HEIGHT;

			graphics.translate(x, y);

			String colorUI = null;
			if (buttonModel.isEnabled()) {
				colorUI = "controlText";
			}
			else {
				colorUI = "textInactiveText";
			}
			graphics.setColor(UIManager.getColor(colorUI));

			for (int i = 0; i < height; i++) {
				graphics.drawLine(i + 1, i, width - i, i);
			}
			graphics.translate(-x, -y);
		}
	}
}
