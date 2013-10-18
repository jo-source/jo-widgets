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

package org.jowidgets.spi.impl.swing.common.widgets;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.spi.impl.controller.PopupDetectionObservable;
import org.jowidgets.spi.impl.swing.common.widgets.base.JoArrowButton;
import org.jowidgets.spi.widgets.IToolBarPopupButtonSpi;

public class ToolBarPopupButtonImpl extends ToolBarButtonImpl implements IToolBarPopupButtonSpi {

	private final PopupDetectionObservable popupDetectionObservable;
	private final JoArrowButton arrowButton;

	public ToolBarPopupButtonImpl(final JButton button, final JoArrowButton arrowButton) {
		super(button);

		this.popupDetectionObservable = new PopupDetectionObservable();
		this.arrowButton = arrowButton;

		adjustArrowSize();

		button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(final MouseEvent e) {
				arrowButton.getModel().setSelected(false);
			}

			@Override
			public void mousePressed(final MouseEvent e) {
				arrowButton.getModel().setSelected(true);
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				arrowButton.getModel().setRollover(false);
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
				arrowButton.getModel().setRollover(true);
			}

		});

		arrowButton.addMouseListener(new MouseAdapter() {

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
		arrowButton.setEnabled(enabled);
	}

	@Override
	public void setToolTipText(final String text) {
		super.setToolTipText(text);
		arrowButton.setToolTipText(text);
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
		arrowButton.setMaximumSize(new Dimension(
			JoArrowButton.ARROW_ICON.getIconWidth() + 4,
			getUiReference().getMaximumSize().height));
	}

}
