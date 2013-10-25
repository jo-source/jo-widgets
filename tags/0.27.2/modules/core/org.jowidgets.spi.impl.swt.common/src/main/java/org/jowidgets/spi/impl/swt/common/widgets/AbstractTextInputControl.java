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
package org.jowidgets.spi.impl.swt.common.widgets;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.jowidgets.common.types.InputChangeEventPolicy;
import org.jowidgets.spi.widgets.ITextControlSpi;

public abstract class AbstractTextInputControl extends AbstractInputControl implements ITextControlSpi {

	public AbstractTextInputControl(final Control control) {
		super(control);
	}

	protected void registerTextControl(final Text textControl, final InputChangeEventPolicy policy) {
		if (policy == InputChangeEventPolicy.ANY_CHANGE) {
			textControl.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(final ModifyEvent e) {
					fireInputChanged(textControl.getText());
				}
			});
		}
		else if (policy == InputChangeEventPolicy.EDIT_FINISHED) {
			textControl.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(final FocusEvent e) {
					fireInputChanged(textControl.getText());
				}
			});
			textControl.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetDefaultSelected(final SelectionEvent e) {
					fireInputChanged(textControl.getText());
				}
			});

		}
		else {
			throw new IllegalArgumentException("InputChangeEventPolicy '" + policy + "' is not known.");
		}
	}

}
