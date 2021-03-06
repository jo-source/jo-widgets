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
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IPopupDialog;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.descriptor.setup.IPopupDialogSetup;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.controller.IWindowListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.impl.base.delegate.ContainerDelegate;
import org.jowidgets.impl.base.delegate.DisplayDelegate;
import org.jowidgets.impl.widgets.common.wrapper.AbstractComponentSpiWrapper;
import org.jowidgets.spi.widgets.IPopupDialogSpi;
import org.jowidgets.tools.controller.WindowAdapter;
import org.jowidgets.tools.widgets.invoker.ColorSettingsInvoker;
import org.jowidgets.tools.widgets.invoker.LayoutSettingsInvoker;
import org.jowidgets.tools.widgets.invoker.VisibiliySettingsInvoker;
import org.jowidgets.util.Assert;

public class PopupDialogImpl extends AbstractComponentSpiWrapper implements IPopupDialog {

    private final DisplayDelegate displayDelegate;
    private final ContainerDelegate containerDelegate;

    private ILayouter layouter;

    public PopupDialogImpl(final IPopupDialogSpi widget, final IPopupDialogSetup setup) {
        super(widget);
        this.displayDelegate = new DisplayDelegate();
        this.containerDelegate = new ContainerDelegate(widget, this);
        ColorSettingsInvoker.setColors(setup, this);
        VisibiliySettingsInvoker.setVisibility(setup, widget);
        LayoutSettingsInvoker.setLayout(setup, this);

        if (setup.isAutoDispose()) {
            final IWindowListener windowListener = new WindowAdapter() {
                @Override
                public void windowDeactivated() {
                    removeWindowListener(this);
                    dispose();
                }
            };

            getWidget().addWindowListener(windowListener);
        }
    }

    @Override
    public IPopupDialogSpi getWidget() {
        return (IPopupDialogSpi) super.getWidget();
    }

    @Override
    public boolean isDisposed() {
        return containerDelegate.isDisposed();
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
    public void dispose() {
        containerDelegate.dispose();
        getWidget().dispose();
    }

    @Override
    public <LAYOUT_TYPE extends ILayouter> LAYOUT_TYPE setLayout(final ILayoutFactory<LAYOUT_TYPE> layoutFactory) {
        Assert.paramNotNull(layoutFactory, "layoutFactory");
        final LAYOUT_TYPE result = layoutFactory.create(this);
        setLayout(result);
        return result;
    }

    @Override
    public void setLayout(final ILayoutDescriptor layoutDescriptor) {
        Assert.paramNotNull(layoutDescriptor, "layoutDescriptor");
        if (layoutDescriptor instanceof ILayouter) {
            this.layouter = (ILayouter) layoutDescriptor;
        }
        getWidget().setLayout(layoutDescriptor);
    }

    @Override
    public void pack() {
        if (layouter != null) {
            setSize(layouter.getPreferredSize());
        }
        else {
            getWidget().pack();
        }
    }

    @Override
    public void layout() {
        layoutBegin();
        layoutEnd();
    }

    @Override
    public void layoutBegin() {
        getWidget().layoutBegin();
    }

    @Override
    public void layoutEnd() {
        getWidget().layoutEnd();
    }

    @Override
    public void layoutLater() {
        containerDelegate.layoutLater();
    }

    @Override
    public Rectangle getClientArea() {
        return getWidget().getClientArea();
    }

    @Override
    public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
        return getWidget().computeDecoratedSize(clientAreaSize);
    }

    @Override
    public void addWindowListener(final IWindowListener listener) {
        getWidget().addWindowListener(listener);
    }

    @Override
    public void removeWindowListener(final IWindowListener listener) {
        getWidget().removeWindowListener(listener);
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
    public IPopupMenu createPopupMenu() {
        return containerDelegate.createPopupMenu();
    }

}
