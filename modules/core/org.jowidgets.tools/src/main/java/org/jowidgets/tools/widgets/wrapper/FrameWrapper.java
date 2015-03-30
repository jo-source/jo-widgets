/*
 * Copyright (c) 2015, grossmann
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

import java.util.List;

import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IDisplay;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.controller.IWindowListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

public class FrameWrapper extends ContainerWrapper implements IFrame {

    private final IFrame original;

    public FrameWrapper(final IFrame original) {
        super(original);
        this.original = original;
    }

    @Override
    public void addWindowListener(final IWindowListener listener) {
        original.addWindowListener(listener);
    }

    @Override
    public void setParent(final IWindow parent) {
        original.setParent(parent);
    }

    @Override
    public void setMinSize(final Dimension minSize) {
        original.setMinSize(minSize);
    }

    @Override
    public IMenuBar createMenuBar() {
        return original.createMenuBar();
    }

    @Override
    public void setTitle(final String title) {
        original.setTitle(title);
    }

    @Override
    public void removeWindowListener(final IWindowListener listener) {
        original.removeWindowListener(listener);
    }

    @Override
    public void setMaximized(final boolean maximized) {
        original.setMaximized(maximized);
    }

    @Override
    public Rectangle getParentBounds() {
        return original.getParentBounds();
    }

    @Override
    public boolean isMaximized() {
        return original.isMaximized();
    }

    @Override
    public void centerLocation() {
        original.centerLocation();
    }

    @Override
    public void setIconfied(final boolean iconfied) {
        original.setIconfied(iconfied);
    }

    @Override
    public boolean isIconfied() {
        return original.isIconfied();
    }

    @Override
    public IMenuBarModel getMenuBarModel() {
        return original.getMenuBarModel();
    }

    @Override
    public <WIDGET_TYPE extends IDisplay, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
        final DESCRIPTOR_TYPE descriptor) {
        return original.createChildWindow(descriptor);
    }

    @Override
    public IWindow getParent() {
        return original.getParent();
    }

    @Override
    public void pack() {
        original.pack();
    }

    @Override
    public void setMenuBar(final IMenuBarModel model) {
        original.setMenuBar(model);
    }

    @Override
    public List<IDisplay> getChildWindows() {
        return original.getChildWindows();
    }

    @Override
    public void setDefaultButton(final IButton button) {
        original.setDefaultButton(button);
    }

    @Override
    public void setMinSize(final int width, final int height) {
        original.setMinSize(width, height);
    }

    @Override
    public void setMinPackSize(final Dimension size) {
        original.setMinPackSize(size);
    }

    @Override
    public void setMaxPackSize(final Dimension size) {
        original.setMaxPackSize(size);
    }

}
