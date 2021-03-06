/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.tools.widgets.wrapper;

import org.jowidgets.api.controller.IShowingStateListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IMouseMotionListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;

public class ComponentWrapper extends WidgetWrapper implements IComponent {

    public ComponentWrapper(final IComponent widget) {
        super(widget);
    }

    @Override
    protected IComponent getWidget() {
        return (IComponent) super.getWidget();
    }

    @Override
    public void redraw() {
        getWidget().redraw();
    }

    @Override
    public void setRedrawEnabled(final boolean enabled) {
        getWidget().setRedrawEnabled(enabled);
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        getWidget().setForegroundColor(colorValue);
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        getWidget().setBackgroundColor(colorValue);
    }

    @Override
    public IColorConstant getForegroundColor() {
        return getWidget().getForegroundColor();
    }

    @Override
    public IColorConstant getBackgroundColor() {
        return getWidget().getBackgroundColor();
    }

    @Override
    public void setCursor(final Cursor cursor) {
        getWidget().setCursor(cursor);
    }

    @Override
    public void setVisible(final boolean visible) {
        getWidget().setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return getWidget().isVisible();
    }

    @Override
    public boolean isShowing() {
        return getWidget().isShowing();
    }

    @Override
    public Dimension getSize() {
        return getWidget().getSize();
    }

    @Override
    public void setSize(final Dimension size) {
        getWidget().setSize(size);
    }

    @Override
    public void setSize(final int width, final int height) {
        getWidget().setSize(width, height);
    }

    @Override
    public void setPosition(final int x, final int y) {
        getWidget().setPosition(x, y);
    }

    @Override
    public Position getPosition() {
        return getWidget().getPosition();
    }

    @Override
    public void setPosition(final Position position) {
        getWidget().setPosition(position);
    }

    @Override
    public Rectangle getBounds() {
        return getWidget().getBounds();
    }

    @Override
    public void setBounds(final Rectangle bounds) {
        getWidget().setBounds(bounds);
    }

    @Override
    public Position toScreen(final Position localPosition) {
        return getWidget().toScreen(localPosition);
    }

    @Override
    public Position toLocal(final Position screenPosition) {
        return getWidget().toLocal(screenPosition);
    }

    @Override
    public Position fromComponent(final IComponentCommon component, final Position componentPosition) {
        return getWidget().fromComponent(component, componentPosition);
    }

    @Override
    public Position toComponent(final Position componentPosition, final IComponentCommon component) {
        return getWidget().toComponent(componentPosition, component);
    }

    @Override
    public boolean isReparentable() {
        return getWidget().isReparentable();
    }

    @Override
    public boolean requestFocus() {
        return getWidget().requestFocus();
    }

    @Override
    public boolean hasFocus() {
        return getWidget().hasFocus();
    }

    @Override
    public void addKeyListener(final IKeyListener listener) {
        getWidget().addKeyListener(listener);
    }

    @Override
    public void removeKeyListener(final IKeyListener listener) {
        getWidget().removeKeyListener(listener);
    }

    @Override
    public void addMouseListener(final IMouseListener mouseListener) {
        getWidget().addMouseListener(mouseListener);
    }

    @Override
    public void removeMouseListener(final IMouseListener mouseListener) {
        getWidget().removeMouseListener(mouseListener);
    }

    @Override
    public void addMouseMotionListener(final IMouseMotionListener listener) {
        getWidget().addMouseMotionListener(listener);
    }

    @Override
    public void removeMouseMotionListener(final IMouseMotionListener mouseListener) {
        getWidget().removeMouseMotionListener(mouseListener);
    }

    @Override
    public void addComponentListener(final IComponentListener componentListener) {
        getWidget().addComponentListener(componentListener);
    }

    @Override
    public void removeComponentListener(final IComponentListener componentListener) {
        getWidget().removeComponentListener(componentListener);
    }

    @Override
    public void addFocusListener(final IFocusListener listener) {
        getWidget().addFocusListener(listener);
    }

    @Override
    public void removeFocusListener(final IFocusListener listener) {
        getWidget().removeFocusListener(listener);
    }

    @Override
    public void addPopupDetectionListener(final IPopupDetectionListener listener) {
        getWidget().addPopupDetectionListener(listener);
    }

    @Override
    public void removePopupDetectionListener(final IPopupDetectionListener listener) {
        getWidget().removePopupDetectionListener(listener);
    }

    @Override
    public void addShowingStateListener(final IShowingStateListener listener) {
        getWidget().addShowingStateListener(listener);

    }

    @Override
    public void removeShowingStateListener(final IShowingStateListener listener) {
        getWidget().removeShowingStateListener(listener);
    }

    @Override
    public IPopupMenu createPopupMenu() {
        return getWidget().createPopupMenu();
    }

    @Override
    public void setPopupMenu(final IMenuModel menuModel) {
        getWidget().setPopupMenu(menuModel);
    }

}
