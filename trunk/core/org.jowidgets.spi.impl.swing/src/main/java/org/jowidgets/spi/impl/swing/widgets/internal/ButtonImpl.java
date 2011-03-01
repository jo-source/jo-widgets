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
package org.jowidgets.spi.impl.swing.widgets.internal;

import java.awt.AWTException;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import javax.swing.JButton;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Markup;
import org.jowidgets.spi.impl.swing.image.SwingImageRegistry;
import org.jowidgets.spi.impl.swing.util.AlignmentConvert;
import org.jowidgets.spi.impl.swing.util.FontProvider;
import org.jowidgets.spi.widgets.setup.IButtonSetupSpi;
import org.jowidgets.test.spi.widgets.IButtonUiSpi;

public class ButtonImpl extends AbstractActionControl implements IButtonUiSpi {

	public ButtonImpl(final IButtonSetupSpi setup) {
		super(new JButton());

		setText(setup.getText());
		setToolTipText(setup.getToolTipText());
		setIcon(setup.getIcon());

		setMarkup(setup.getMarkup());

		final Insets insets = getUiReference().getInsets();
		getUiReference().setMargin(new Insets(insets.top, insets.bottom, insets.bottom, insets.bottom));

		getUiReference().setHorizontalAlignment(AlignmentConvert.convert(setup.getAlignment()));

		getUiReference().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				fireActionPerformed();
			}

		});
	}

	@Override
	public JButton getUiReference() {
		return (JButton) super.getUiReference();
	}

	@Override
	public void setText(final String text) {
		getUiReference().setText(text);
	}

	@Override
	public void setToolTipText(final String text) {
		getUiReference().setToolTipText(text);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		getUiReference().setIcon(SwingImageRegistry.getInstance().getImageIcon(icon));
	}

	@Override
	public void setMarkup(final Markup markup) {
		final JButton button = getUiReference();
		button.setFont(FontProvider.deriveFont(button.getFont(), markup));
	}

	@Override
	public void requestFocus() {
		getUiReference().requestFocusInWindow();
	}

	@Override
	public boolean isTestable() {
		return true;
	}

	@Override
	public void push() {
		Robot robo = null;
		try {
			robo = new Robot();
		}
		catch (final AWTException e) {
			// CHECKSTYLE:OFF
			e.printStackTrace();
			// CHECKSTYLE:ON
			return;
		}
		final JButton button = getUiReference();
		robo.mouseMove(
				button.getLocationOnScreen().x + button.getWidth() / 2,
				button.getLocationOnScreen().y + button.getHeight() / 2);
		robo.mousePress(InputEvent.BUTTON1_MASK);
		robo.mouseRelease(InputEvent.BUTTON1_MASK);
	}

}
