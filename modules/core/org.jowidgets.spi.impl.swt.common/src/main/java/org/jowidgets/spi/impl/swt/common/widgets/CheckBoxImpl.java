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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.jowidgets.common.types.Markup;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;
import org.jowidgets.spi.impl.swt.common.util.AlignmentConvert;
import org.jowidgets.spi.impl.swt.common.util.FontProvider;
import org.jowidgets.spi.widgets.ICheckBoxSpi;
import org.jowidgets.spi.widgets.setup.ICheckBoxSetupSpi;

public class CheckBoxImpl extends AbstractInputControl implements ICheckBoxSpi {

    public CheckBoxImpl(final Object parentUiReference, final ICheckBoxSetupSpi setup, final SwtImageRegistry imageRegistry) {
        this(new Button((Composite) parentUiReference, SWT.NONE | SWT.CHECK), setup, imageRegistry);
    }

    public CheckBoxImpl(final Button button, final ICheckBoxSetupSpi setup, final SwtImageRegistry imageRegistry) {
        super(button, imageRegistry);

        setText(setup.getText());
        setToolTipText(setup.getToolTipText());

        setMarkup(setup.getMarkup());

        getUiReference().setAlignment(AlignmentConvert.convert(setup.getAlignment()));

        getUiReference().addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(final Event event) {
                fireInputChanged(getUiReference().getSelection());
            }
        });
    }

    @Override
    public Button getUiReference() {
        return (Button) super.getUiReference();
    }

    @Override
    public void setEditable(final boolean editable) {
        getUiReference().setEnabled(editable);
    }

    @Override
    public void setMarkup(final Markup markup) {
        final Button button = this.getUiReference();
        final Font newFont = FontProvider.deriveFont(button.getFont(), markup);
        button.setFont(newFont);
    }

    @Override
    public void setFontSize(final int size) {
        getUiReference().setFont(FontProvider.deriveFont(getUiReference().getFont(), size));
    }

    @Override
    public void setFontName(final String fontName) {
        getUiReference().setFont(FontProvider.deriveFont(getUiReference().getFont(), fontName));
    }

    @Override
    public void setText(final String text) {
        if (text != null) {
            getUiReference().setText(text);
        }
        else {
            setText("");
        }
    }

    @Override
    public boolean isSelected() {
        return getUiReference().getSelection();
    }

    @Override
    public void setSelected(final boolean selected) {
        getUiReference().setSelection(selected);
        fireInputChanged(selected);
    }

}
