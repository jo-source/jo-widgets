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
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jowidgets.common.mask.TextMaskMode;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.spi.impl.mask.TextMaskVerifierFactory;
import org.jowidgets.spi.impl.swt.threads.SwtUiThreadAccess;
import org.jowidgets.spi.impl.verify.InputVerifierHelper;
import org.jowidgets.spi.widgets.setup.ITextFieldSetupSpi;

public class TextFieldImpl extends AbstractTextInputControl {

	public TextFieldImpl(final Object parentUiReference, final ITextFieldSetupSpi setup) {
		super(createText(parentUiReference, setup.isPasswordPresentation()));

		final IInputVerifier maskVerifier = TextMaskVerifierFactory.create(this, setup.getMask(), new SwtUiThreadAccess());

		final IInputVerifier inputVerifier = InputVerifierHelper.getInputVerifier(maskVerifier, setup);
		if (inputVerifier != null) {
			addInputVerifier(inputVerifier);
		}

		if (setup.getMaxLength() != null) {
			getUiReference().setTextLimit(setup.getMaxLength().intValue());
		}

		registerTextControl(getUiReference());

		if (setup.getMask() != null && TextMaskMode.FULL_MASK == setup.getMask().getMode()) {
			setText(setup.getMask().getPlaceholder());
		}
	}

	@Override
	public Text getUiReference() {
		return (Text) super.getUiReference();
	}

	@Override
	public String getText() {
		return getUiReference().getText();
	}

	@Override
	public void setText(final String text) {
		if (text != null) {
			getUiReference().setText(text);
		}
		else {
			getUiReference().setText("");
		}
	}

	@Override
	public void setTooltipText(final String tooltipText) {
		getUiReference().setToolTipText(tooltipText);
	}

	@Override
	public void setSelection(final int start, final int end) {
		getUiReference().setSelection(start, end);
	}

	@Override
	public void setCaretPosition(final int pos) {
		getUiReference().setSelection(pos, pos);
	}

	@Override
	public int getCaretPosition() {
		return getUiReference().getCaretPosition();
	}

	@Override
	public void setEditable(final boolean editable) {
		getUiReference().setEditable(editable);
	}

	private void addInputVerifier(final IInputVerifier verifier) {
		getUiReference().addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(final VerifyEvent verifyEvent) {
				verifyEvent.doit = verifier.verify(
						getUiReference().getText(),
						verifyEvent.text,
						verifyEvent.start,
						verifyEvent.end);
			}
		});
	}

	private static Text createText(final Object parentUiReference, final boolean passwordPresentation) {
		if (passwordPresentation) {
			return new Text((Composite) parentUiReference, SWT.BORDER | SWT.PASSWORD);
		}
		else {
			return new Text((Composite) parentUiReference, SWT.BORDER);
		}
	}

}
