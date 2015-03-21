/*
 * Copyright (c) 2015, Michael Grossmann
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IMouseMotionListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.spi.dnd.IDragSourceSpi;
import org.jowidgets.spi.dnd.IDropTargetSpi;
import org.jowidgets.spi.widgets.ITextControlSpi;
import org.jowidgets.spi.widgets.setup.ITextFieldSetupSpi;

public class TransparentTextFieldImpl extends SwtControl implements ITextControlSpi {

    private final TextFieldImpl textField;

    public TransparentTextFieldImpl(final Object parentUiReference, final ITextFieldSetupSpi setup) {
        super(new Composite((Composite) parentUiReference, SWT.NONE));

        final Composite composite = getUiReference();

        composite.setBackgroundMode(SWT.INHERIT_FORCE);
        composite.setLayout(new FillLayout());

        this.textField = new TextFieldImpl(composite, setup);
    }

    @Override
    public Composite getUiReference() {
        return (Composite) super.getUiReference();
    }

    @Override
    public final void addInputListener(final IInputListener listener) {
        textField.addInputListener(listener);
    }

    @Override
    public final void removeInputListener(final IInputListener listener) {
        textField.removeInputListener(listener);
    }

    public void fireInputChanged(final Object value) {
        textField.fireInputChanged(value);
    }

    @Override
    public String getText() {
        return textField.getText();
    }

    @Override
    public void setText(final String text) {
        textField.setText(text);
    }

    @Override
    public void setFontSize(final int size) {
        textField.setFontSize(size);
    }

    @Override
    public void setFontName(final String fontName) {
        textField.setFontName(fontName);
    }

    @Override
    public void setMarkup(final Markup markup) {
        textField.setMarkup(markup);
    }

    @Override
    public void setSelection(final int start, final int end) {
        textField.setSelection(start, end);
    }

    @Override
    public void select() {
        textField.select();
    }

    @Override
    public void setCaretPosition(final int pos) {
        textField.setCaretPosition(pos);
    }

    @Override
    public int getCaretPosition() {
        return textField.getCaretPosition();
    }

    @Override
    public void setEditable(final boolean editable) {
        textField.setEditable(editable);
    }

    @Override
    public IDragSourceSpi getDragSource() {
        return textField.getDragSource();
    }

    @Override
    public IDropTargetSpi getDropTarget() {
        return textField.getDropTarget();
    }

    @Override
    public void setCursor(final Cursor cursor) {
        textField.setCursor(cursor);
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        super.setForegroundColor(colorValue);
        textField.setForegroundColor(colorValue);
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        super.setBackgroundColor(colorValue);
        textField.setBackgroundColor(colorValue);
    }

    @Override
    public boolean requestFocus() {
        return textField.requestFocus();
    }

    @Override
    public void addFocusListener(final IFocusListener listener) {
        textField.addFocusListener(listener);
        super.addFocusListener(listener);
    }

    @Override
    public void removeFocusListener(final IFocusListener listener) {
        textField.removeFocusListener(listener);
        super.removeFocusListener(listener);
    }

    @Override
    public void addKeyListener(final IKeyListener listener) {
        textField.addKeyListener(listener);
        super.addKeyListener(listener);
    }

    @Override
    public void removeKeyListener(final IKeyListener listener) {
        textField.removeKeyListener(listener);
        super.removeKeyListener(listener);
    }

    @Override
    public void addMouseListener(final IMouseListener mouseListener) {
        textField.addMouseListener(mouseListener);
        super.addMouseListener(mouseListener);
    }

    @Override
    public void removeMouseListener(final IMouseListener mouseListener) {
        textField.removeMouseListener(mouseListener);
        super.removeMouseListener(mouseListener);
    }

    @Override
    public void addMouseMotionListener(final IMouseMotionListener listener) {
        textField.addMouseMotionListener(listener);
        super.addMouseMotionListener(listener);
    }

    @Override
    public void removeMouseMotionListener(final IMouseMotionListener listener) {
        textField.removeMouseMotionListener(listener);
        super.removeMouseMotionListener(listener);
    }

    @Override
    public void addPopupDetectionListener(final IPopupDetectionListener listener) {
        textField.addPopupDetectionListener(listener);
        super.addPopupDetectionListener(listener);
    }

    @Override
    public void removePopupDetectionListener(final IPopupDetectionListener listener) {
        textField.removePopupDetectionListener(listener);
        super.removePopupDetectionListener(listener);
    }

    @Override
    public void setToolTipText(final String toolTip) {
        textField.setToolTipText(toolTip);
        super.setToolTipText(toolTip);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        textField.setEnabled(enabled);
        super.setEnabled(enabled);
    }

}
