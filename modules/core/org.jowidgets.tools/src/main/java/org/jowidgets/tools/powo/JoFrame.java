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

package org.jowidgets.tools.powo;

import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.descriptor.setup.IFrameSetup;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.tools.controller.WindowAdapter;
import org.jowidgets.util.Assert;

/**
 * @deprecated The idea of POWO's (Plain Old Widget Object's) has not been established.
 *             For that, POWO's will no longer be supported and may removed completely in middle term.
 *             Feel free to move them to your own open source project.
 */
@Deprecated
public class JoFrame extends Window<IFrame, IFrameBluePrint> implements IFrame {

    private JoMenuBar menuBar;
    private IMenuBarModel menuBarModel;
    private IButton defaultButton;
    private Boolean maximized;
    private Boolean iconfied;

    JoFrame(final IFrame widget) {
        super(bluePrint(), widget);
        Assert.paramNotNull(widget, "widget");
    }

    public JoFrame() {
        super(Toolkit.getBluePrintFactory().frame());
    }

    public JoFrame(final String title) {
        super(Toolkit.getBluePrintFactory().frame(title));
    }

    public JoFrame(final String title, final IImageConstant icon) {
        super(Toolkit.getBluePrintFactory().frame(title, icon));
    }

    public JoFrame(final IFrameSetup setup) {
        super(Toolkit.getBluePrintFactory().frame().setSetup(setup));
    }

    public void finishLifecycleOnClose(final IApplicationLifecycle lifecycle) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed() {
                lifecycle.finish();
            }
        });
    }

    public final void setMenuBar(final JoMenuBar menuBar) {
        Assert.paramNotNull(menuBar, "menuBar");
        if (isInitialized()) {
            menuBar.initialize(createMenuBar());
        }
        else {
            if (menuBarModel != null) {
                throw new UnsupportedOperationException("This frame has already a menu bar model and is not yet initialized. "
                    + "Uninitialized JoFrame's must not have a JoMenuBar and a menu model at the same time. This might be "
                    + "supported in future releases.");
            }
            this.menuBar = menuBar;
        }
    }

    @Override
    void initialize(final IFrame widget) {
        super.initialize(widget);
        if (menuBar != null) {
            menuBar.initialize(createMenuBar());
        }
        if (menuBarModel != null) {
            widget.setMenuBar(menuBarModel);
        }
        if (defaultButton != null) {
            widget.setDefaultButton(defaultButton);
        }
        if (maximized != null) {
            widget.setMaximized(maximized.booleanValue());
        }
        if (iconfied != null) {
            widget.setIconfied(iconfied.booleanValue());
        }
    }

    @Override
    public void setMaximized(final boolean maximized) {
        if (isInitialized()) {
            getWidget().setMaximized(maximized);
        }
        else {
            this.maximized = Boolean.valueOf(maximized);
        }
    }

    @Override
    public boolean isMaximized() {
        if (isInitialized()) {
            return getWidget().isMaximized();
        }
        else {
            return maximized != null ? maximized.booleanValue() : false;
        }
    }

    @Override
    public void setIconfied(final boolean iconfied) {
        if (isInitialized()) {
            getWidget().setIconfied(iconfied);
        }
        else {
            this.iconfied = Boolean.valueOf(iconfied);
        }
    }

    @Override
    public boolean isIconfied() {
        if (isInitialized()) {
            return getWidget().isIconfied();
        }
        else {
            return iconfied != null ? iconfied.booleanValue() : false;
        }
    }

    @Override
    public IMenuBar createMenuBar() {
        if (isInitialized()) {
            return getWidget().createMenuBar();
        }
        else {
            if (menuBarModel != null) {
                throw new UnsupportedOperationException("This frame has already a menu bar model and is not yet initialized. "
                    + "Uninitialized JoFrame's must not have a JoMenuBar and a menu model at the same time. This might be "
                    + "supported in future releases.");
            }
            menuBar = new JoMenuBar();
            return menuBar;
        }
    }

    @Override
    public IMenuBarModel getMenuBarModel() {
        if (isInitialized()) {
            return getWidget().getMenuBarModel();
        }
        else {
            if (menuBarModel == null) {
                setMenuBar(Toolkit.getModelFactoryProvider().getItemModelFactory().menuBar());
            }
            return menuBarModel;
        }
    }

    @Override
    public void setMenuBar(final IMenuBarModel menuBarModel) {
        if (isInitialized()) {
            getWidget().setMenuBar(menuBarModel);
        }
        else {
            if (menuBar != null) {
                throw new UnsupportedOperationException(
                    "This frame has already a menu bar (JoMenuBar) and is not yet initialized. "
                        + "Uninitialized JoFrame's must not have a JoMenuBar and a menu model at the same time. This might be "
                        + "supported in future releases.");
            }
            this.menuBarModel = menuBarModel;
        }
    }

    @Override
    public void setDefaultButton(final IButton defaultButton) {
        if (isInitialized()) {
            getWidget().setDefaultButton(defaultButton);
        }
        else {
            this.defaultButton = defaultButton;
        }
    }

    @Override
    public void setTitle(final String title) {
        if (isInitialized()) {
            getWidget().setDefaultButton(defaultButton);
        }
        else {
            getBluePrint().setTitle(title);
        }
    }

    @Override
    public void setMinSize(final Dimension minSize) {
        getWidget().setMinSize(minSize);
    }

    @Override
    public void setMinSize(final int width, final int height) {
        getWidget().setMinSize(width, height);
    }

    public static JoFrame toJoFrame(final IFrame widget) {
        Assert.paramNotNull(widget, "widget");
        if (widget instanceof JoFrame) {
            return (JoFrame) widget;
        }
        else {
            return new JoFrame(widget);
        }
    }

    public static IFrameBluePrint bluePrint() {
        return Toolkit.getBluePrintFactory().frame();
    }

    public static IFrameBluePrint bluePrint(final String title) {
        return Toolkit.getBluePrintFactory().frame(title);
    }

}
