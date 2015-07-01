/*
 * Copyright (c) 2015, MGrossmann
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

package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.layout.FillLayout;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.descriptor.IButtonDescriptor;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IMouseMotionListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.tools.widgets.wrapper.CompositeWrapper;

public final class NoBorderButtonImpl extends CompositeWrapper implements IButton {

    private final IButton button;

    public NoBorderButtonImpl(final IComposite composite, final IButtonDescriptor descriptor) {
        super(composite);
        final IButtonBluePrint decoratedButtonBp = BPF.button().setSetup(descriptor);
        decoratedButtonBp.setRemoveEmptyBorder(false);

        composite.setLayout(FillLayout.builder().margin(-1).build());
        this.button = composite.add(decoratedButtonBp);
    }

    @Override
    public String getText() {
        return button.getText();
    }

    @Override
    public void setFontSize(final int size) {
        button.setFontSize(size);
    }

    @Override
    public void setFontName(final String fontName) {
        button.setFontName(fontName);
    }

    @Override
    public void setMarkup(final Markup markup) {
        button.setMarkup(markup);
    }

    @Override
    public void setText(final String text) {
        button.setText(text);
    }

    @Override
    public IImageConstant getIcon() {
        return button.getIcon();
    }

    @Override
    public void setIcon(final IImageConstant icon) {
        button.setIcon(icon);
    }

    @Override
    public void addActionListener(final IActionListener actionListener) {
        button.addActionListener(actionListener);
    }

    @Override
    public void removeActionListener(final IActionListener actionListener) {
        button.removeActionListener(actionListener);
    }

    @Override
    public void push() {
        button.push();
    }

    @Override
    public void setAction(final IAction action) {
        button.setAction(action);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        button.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return button.isEnabled();
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        button.setForegroundColor(colorValue);
    }

    @Override
    public IColorConstant getForegroundColor() {
        return button.getForegroundColor();
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        button.setBackgroundColor(colorValue);
    }

    @Override
    public IColorConstant getBackgroundColor() {
        return button.getBackgroundColor();
    }

    @Override
    public void setToolTipText(final String toolTip) {
        button.setToolTipText(toolTip);
    }

    @Override
    public void setCursor(final Cursor cursor) {
        button.setCursor(cursor);
    }

    @Override
    public IPopupMenu createPopupMenu() {
        return button.createPopupMenu();
    }

    @Override
    public void setPopupMenu(final IMenuModel popupMenu) {
        button.setPopupMenu(popupMenu);
    }

    @Override
    public void addPopupDetectionListener(final IPopupDetectionListener listener) {
        button.addPopupDetectionListener(listener);
    }

    @Override
    public void removePopupDetectionListener(final IPopupDetectionListener listener) {
        button.removePopupDetectionListener(listener);
    }

    @Override
    public boolean requestFocus() {
        return button.requestFocus();
    }

    @Override
    public boolean hasFocus() {
        return button.hasFocus();
    }

    @Override
    public void addFocusListener(final IFocusListener listener) {
        button.addFocusListener(listener);
    }

    @Override
    public void removeFocusListener(final IFocusListener listener) {
        button.removeFocusListener(listener);
    }

    @Override
    public void addKeyListener(final IKeyListener listener) {
        button.addKeyListener(listener);
    }

    @Override
    public void removeKeyListener(final IKeyListener listener) {
        button.removeKeyListener(listener);
    }

    @Override
    public void addMouseListener(final IMouseListener listener) {
        button.addMouseListener(listener);
    }

    @Override
    public void removeMouseListener(final IMouseListener listener) {
        button.removeMouseListener(listener);
    }

    @Override
    public void addMouseMotionListener(final IMouseMotionListener listener) {
        button.addMouseMotionListener(listener);
    }

    @Override
    public void removeMouseMotionListener(final IMouseMotionListener listener) {
        button.removeMouseMotionListener(listener);
    }

}
