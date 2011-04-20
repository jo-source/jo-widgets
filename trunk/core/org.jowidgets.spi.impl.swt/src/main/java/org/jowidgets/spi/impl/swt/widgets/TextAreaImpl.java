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
package org.jowidgets.spi.impl.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.jowidgets.spi.widgets.ITextAreaSpi;
import org.jowidgets.spi.widgets.setup.ITextAreaSetupSpi;

public class TextAreaImpl extends AbstractTextInputControl implements ITextAreaSpi {

	private final Text textArea;
	private final ScrolledComposite scrolledComposite;
	private final boolean isLineWrap;

	private int lastLineCount;

	public TextAreaImpl(final Object parentUiReference, final ITextAreaSetupSpi setup) {
		super(new ScrolledComposite((Composite) parentUiReference, getScrollCompositeStyle(setup)));

		lastLineCount = 0;
		isLineWrap = setup.isLineWrap();

		scrolledComposite = getUiReference();

		scrolledComposite.setLayoutData(new FillLayout());
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setAlwaysShowScrollBars(setup.isAlwaysShowBars());

		textArea = new Text(getUiReference(), getTextStyle(setup));
		if (setup.getMaxLength() != null) {
			textArea.setTextLimit(setup.getMaxLength().intValue());
		}

		scrolledComposite.setContent(textArea);

		registerTextControl(textArea);

		scrolledComposite.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(final ControlEvent e) {
				scrolledComposite.setMinSize(calcMinSize());
			}
		});

		textArea.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				checkScrollBars();
			}
		});
	}

	private Point calcMinSize() {
		if (isLineWrap) {
			final ScrollBar bar = scrolledComposite.getVerticalBar();
			int verticalSize = 0;
			if (bar != null) {
				verticalSize = bar.getSize().x + 10;
			}
			return textArea.computeSize(scrolledComposite.getSize().x - verticalSize, SWT.DEFAULT, false);
		}
		else {
			return textArea.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
		}
	}

	private void checkScrollBars() {
		if (!isLineWrap && lineCountChanged()) {
			scrolledComposite.setRedraw(false);
			scrolledComposite.setMinSize(calcMinSize());

			//TODO MG only scroll if its is really necessary
			scrollToCaretPosition();
			scrolledComposite.setRedraw(true);
		}
	}

	private boolean lineCountChanged() {
		try {
			if (lastLineCount != textArea.getLineCount()) {
				lastLineCount = textArea.getLineCount();
				return true;
			}
		}
		catch (final NoSuchMethodError e) {
			//RWT does not support getLineCount()
			return true;
		}
		return false;
	}

	@Override
	public ScrolledComposite getUiReference() {
		return (ScrolledComposite) super.getUiReference();
	}

	@Override
	public String getText() {
		return textArea.getText();
	}

	@Override
	public void setText(final String text) {
		if (text != null) {
			textArea.setText(text);
		}
		else {
			textArea.setText("");
		}
		checkScrollBars();
	}

	@Override
	public void setTooltipText(final String tooltipText) {
		textArea.setToolTipText(tooltipText);
	}

	@Override
	public void setSelection(final int start, final int end) {
		textArea.setSelection(start, end);
	}

	@Override
	public void setCaretPosition(final int pos) {
		textArea.setSelection(pos, pos);
	}

	@Override
	public int getCaretPosition() {
		return textArea.getCaretPosition();
	}

	@Override
	public void setEditable(final boolean editable) {
		textArea.setEditable(editable);
	}

	@Override
	public void scrollToCaretPosition() {
		final Point caretPosition = textArea.getCaretLocation();
		scrolledComposite.setOrigin(caretPosition.x - 20, caretPosition.y - 20);
	}

	private static int getScrollCompositeStyle(final ITextAreaSetupSpi setup) {
		int result = SWT.V_SCROLL;
		if (!setup.isLineWrap()) {
			result = result | SWT.H_SCROLL;
		}
		if (setup.hasBorder()) {
			result = result | SWT.BORDER;
		}
		return result;
	}

	private static int getTextStyle(final ITextAreaSetupSpi setup) {
		int result = SWT.MULTI;
		if (setup.isLineWrap()) {
			result = result | SWT.WRAP;
		}
		return result;
	}

}
