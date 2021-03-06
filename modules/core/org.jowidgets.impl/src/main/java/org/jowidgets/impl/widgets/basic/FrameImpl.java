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

package org.jowidgets.impl.widgets.basic;

import java.util.Collection;
import java.util.List;

import org.jowidgets.api.controller.IContainerListener;
import org.jowidgets.api.controller.IContainerRegistry;
import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.controller.IListenerFactory;
import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IDisplay;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.descriptor.setup.IFrameSetup;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.impl.base.delegate.ContainerDelegate;
import org.jowidgets.impl.base.delegate.DisplayDelegate;
import org.jowidgets.impl.base.delegate.WindowDelegate;
import org.jowidgets.impl.widgets.common.wrapper.AbstractFrameSpiWrapper;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.tools.controller.ListModelAdapter;
import org.jowidgets.tools.controller.ShowingStateObservable;
import org.jowidgets.tools.widgets.invoker.ColorSettingsInvoker;
import org.jowidgets.tools.widgets.invoker.LayoutSettingsInvoker;
import org.jowidgets.tools.widgets.invoker.VisibiliySettingsInvoker;
import org.jowidgets.util.Assert;

public class FrameImpl extends AbstractFrameSpiWrapper implements IFrame {

    private final DisplayDelegate displayDelegate;
    private final WindowDelegate windowDelegate;
    private final ContainerDelegate containerDelegate;

    private IMenuBar menuBar;

    public FrameImpl(final IFrameSpi frameWidgetSpi, final IFrameSetup setup) {
        this(frameWidgetSpi, setup, false);
    }

    public FrameImpl(final IFrameSpi frameWidgetSpi, final IFrameSetup setup, final boolean wrapperMode) {
        super(frameWidgetSpi);
        this.displayDelegate = new DisplayDelegate();
        this.windowDelegate = new WindowDelegate(frameWidgetSpi, this, setup);
        this.containerDelegate = new ContainerDelegate(frameWidgetSpi, this);
        if (!wrapperMode) {
            ColorSettingsInvoker.setColors(setup, this);
            VisibiliySettingsInvoker.setVisibility(setup, frameWidgetSpi);
            LayoutSettingsInvoker.setLayout(setup, this);

            if (setup.getMinSize() != null) {
                setMinSize(setup.getMinSize());
            }
        }
    }

    @Override
    public void addDisposeListener(final IDisposeListener listener) {
        containerDelegate.addDisposeListener(listener);
    }

    @Override
    public void removeDisposeListener(final IDisposeListener listener) {
        containerDelegate.removeDisposeListener(listener);
    }

    @Override
    public void setMinPackSize(final Dimension size) {
        windowDelegate.setMinPackSize(size);
    }

    @Override
    public void setMaxPackSize(final Dimension size) {
        windowDelegate.setMaxPackSize(size);
    }

    @Override
    public void dispose() {
        if (!isDisposed()) {
            windowDelegate.dispose();
            if (menuBar != null) {
                menuBar.dispose();
            }
            containerDelegate.dispose();
            super.dispose();
        }
    }

    @Override
    public boolean isDisposed() {
        return containerDelegate.isDisposed();
    }

    @Override
    public <LAYOUT_TYPE extends ILayouter> LAYOUT_TYPE setLayout(final ILayoutFactory<LAYOUT_TYPE> layoutFactory) {
        Assert.paramNotNull(layoutFactory, "layoutFactory");
        final LAYOUT_TYPE result = layoutFactory.create(this);
        setLayout(result);
        return result;
    }

    @Override
    public void addContainerListener(final IContainerListener listener) {
        containerDelegate.addContainerListener(listener);
    }

    @Override
    public void removeContainerListener(final IContainerListener listener) {
        containerDelegate.removeContainerListener(listener);
    }

    @Override
    public void addContainerRegistry(final IContainerRegistry registry) {
        containerDelegate.addContainerRegistry(registry);
    }

    @Override
    public void removeContainerRegistry(final IContainerRegistry registry) {
        containerDelegate.removeContainerRegistry(registry);
    }

    @Override
    public void addComponentListenerRecursive(final IListenerFactory<IComponentListener> listenerFactory) {
        containerDelegate.addComponentListenerRecursive(listenerFactory);
    }

    @Override
    public void removeComponentListenerRecursive(final IListenerFactory<IComponentListener> listenerFactory) {
        containerDelegate.removeComponentListenerRecursive(listenerFactory);
    }

    @Override
    public void addFocusListenerRecursive(final IListenerFactory<IFocusListener> listenerFactory) {
        containerDelegate.addFocusListenerRecursive(listenerFactory);
    }

    @Override
    public void removeFocusListenerRecursive(final IListenerFactory<IFocusListener> listenerFactory) {
        containerDelegate.removeFocusListenerRecursive(listenerFactory);
    }

    @Override
    public void addKeyListenerRecursive(final IListenerFactory<IKeyListener> listenerFactory) {
        containerDelegate.addKeyListenerRecursive(listenerFactory);
    }

    @Override
    public void removeKeyListenerRecursive(final IListenerFactory<IKeyListener> listenerFactory) {
        containerDelegate.removeKeyListenerRecursive(listenerFactory);
    }

    @Override
    public void addMouseListenerRecursive(final IListenerFactory<IMouseListener> listenerFactory) {
        containerDelegate.addMouseListenerRecursive(listenerFactory);
    }

    @Override
    public void removeMouseListenerRecursive(final IListenerFactory<IMouseListener> listenerFactory) {
        containerDelegate.removeMouseListenerRecursive(listenerFactory);
    }

    @Override
    public void addPopupDetectionListenerRecursive(final IListenerFactory<IPopupDetectionListener> listenerFactory) {
        containerDelegate.addPopupDetectionListenerRecursive(listenerFactory);
    }

    @Override
    public void removePopupDetectionListenerRecursive(final IListenerFactory<IPopupDetectionListener> listenerFactory) {
        containerDelegate.removePopupDetectionListenerRecursive(listenerFactory);
    }

    @Override
    public void layoutLater() {
        containerDelegate.layoutLater();
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final int index,
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
        final Object layoutConstraints) {
        return containerDelegate.add(index, descriptor, layoutConstraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final int index,
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
        return containerDelegate.add(index, descriptor);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final int index,
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final Object layoutConstraints) {
        return containerDelegate.add(index, creator, layoutConstraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final int index, final ICustomWidgetCreator<WIDGET_TYPE> creator) {
        return containerDelegate.add(index, creator);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
        final Object layoutConstraints) {
        return containerDelegate.add(descriptor, layoutConstraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final Object layoutConstraints) {
        return containerDelegate.add(creator, layoutConstraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
        return containerDelegate.add(descriptor);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final ICustomWidgetCreator<WIDGET_TYPE> creator) {
        return containerDelegate.add(creator);
    }

    @Override
    public void setTabOrder(final Collection<? extends IControl> tabOrder) {
        containerDelegate.setTabOrder(tabOrder);
    }

    @Override
    public void setTabOrder(final IControl... controls) {
        containerDelegate.setTabOrder(controls);
    }

    @Override
    public List<IControl> getChildren() {
        return containerDelegate.getChildren();
    }

    @Override
    public void removeAll() {
        containerDelegate.removeAll();
    }

    @Override
    public boolean remove(final IControl control) {
        return containerDelegate.remove(control);
    }

    @Override
    public IWindow getParent() {
        return displayDelegate.getParent();
    }

    @Override
    public void setParent(final IWindow parent) {
        displayDelegate.setParent(parent);
    }

    @Override
    public boolean isReparentable() {
        return displayDelegate.isReparentable();
    }

    @Override
    public <WIDGET_TYPE extends IDisplay, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
        final DESCRIPTOR_TYPE descriptor) {
        return windowDelegate.createChildWindow(descriptor);
    }

    @Override
    public void centerLocation() {
        windowDelegate.centerLocation();
    }

    @Override
    public void setVisible(final boolean visible) {
        windowDelegate.setVisible(visible);
        final ShowingStateObservable showingStateObservable = getShowingStateObservableLazy();
        if (showingStateObservable != null) {
            showingStateObservable.fireShowingStateChanged(isShowing());
        }
    }

    @Override
    public List<IDisplay> getChildWindows() {
        return windowDelegate.getChildWindows();
    }

    @Override
    public void setPosition(final Position position) {
        windowDelegate.setPosition(position);
    }

    @Override
    public void setSize(final Dimension size) {
        windowDelegate.setSize(size);
    }

    @Override
    public void setMinSize(final int width, final int height) {
        setMinSize(new Dimension(width, height));
    }

    @Override
    public void setMinSize(final Dimension minSize) {
        Assert.paramNotNull(minSize, "minSize");
        getWidget().setMinSize(minSize);
    }

    @Override
    public void setDefaultButton(final IButton button) {
        getWidget().setDefaultButton(button);
    }

    @Override
    public IPopupMenu createPopupMenu() {
        return containerDelegate.createPopupMenu();
    }

    @Override
    public IMenuBar createMenuBar() {
        if (menuBar == null) {
            menuBar = new MenuBarImpl(getWidget().createMenuBar(), this);
        }
        return menuBar;
    }

    @Override
    public IMenuBarModel getMenuBarModel() {
        return createMenuBar().getModel();
    }

    @Override
    public void setMenuBar(final IMenuBarModel model) {
        Assert.paramNotNull(model, "model");
        if (model.getMenus().size() == 0) {
            if (this.menuBar != null && this.menuBar != model) {
                this.menuBar.removeAll();
            }
            final ListModelAdapter listener = new ListModelAdapter() {
                @Override
                public void afterChildAdded(final int index) {
                    createMenuBar().setModel(model);
                    model.removeListModelListener(this);
                }
            };
            model.addListModelListener(listener);
        }
        else {
            createMenuBar().setModel(model);
        }
    }

}
