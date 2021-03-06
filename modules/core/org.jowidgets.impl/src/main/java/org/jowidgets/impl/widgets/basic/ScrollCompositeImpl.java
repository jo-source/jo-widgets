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
import org.jowidgets.api.controller.IParentListener;
import org.jowidgets.api.dnd.IDragSource;
import org.jowidgets.api.dnd.IDropTarget;
import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IScrollBar;
import org.jowidgets.api.widgets.IScrollComposite;
import org.jowidgets.api.widgets.descriptor.setup.IScrollCompositeSetup;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.impl.base.delegate.ContainerDelegate;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.dnd.DragSourceImpl;
import org.jowidgets.impl.dnd.DropTargetImpl;
import org.jowidgets.impl.dnd.ImmutableDropSelection;
import org.jowidgets.impl.widgets.common.wrapper.AbstractScrollCompositeSpiWrapper;
import org.jowidgets.spi.widgets.IScrollCompositeSpi;
import org.jowidgets.tools.widgets.invoker.ColorSettingsInvoker;
import org.jowidgets.tools.widgets.invoker.LayoutSettingsInvoker;
import org.jowidgets.tools.widgets.invoker.VisibiliySettingsInvoker;
import org.jowidgets.util.Assert;

public class ScrollCompositeImpl extends AbstractScrollCompositeSpiWrapper implements IScrollComposite {

    private final ControlDelegate controlDelegate;
    private final ContainerDelegate containerDelegate;
    private final IDragSource dragSource;
    private final IDropTarget dropTarget;
    private final IScrollBar horizonalScrollBar;
    private final IScrollBar verticalScrollBar;

    private Dimension minSize;

    public ScrollCompositeImpl(final IScrollCompositeSpi containerWidgetSpi, final IScrollCompositeSetup setup) {
        super(containerWidgetSpi);
        this.verticalScrollBar = new ScrollBarImpl(containerWidgetSpi.getVerticalScrollBar());
        this.horizonalScrollBar = new ScrollBarImpl(containerWidgetSpi.getHorizontalScrollBar());
        this.controlDelegate = new ControlDelegate(containerWidgetSpi, this);
        this.containerDelegate = new ContainerDelegate(containerWidgetSpi, this);
        this.dragSource = new DragSourceImpl(containerWidgetSpi.getDragSource());
        this.dropTarget = new DropTargetImpl(containerWidgetSpi.getDropTarget(), new ImmutableDropSelection(this));
        VisibiliySettingsInvoker.setVisibility(setup, this);
        ColorSettingsInvoker.setColors(setup, this);
        LayoutSettingsInvoker.setLayout(setup, this);
    }

    @Override
    public IControl getRoot() {
        return (IControl) super.getRoot();
    }

    @Override
    public <LAYOUT_TYPE extends ILayouter> LAYOUT_TYPE setLayout(final ILayoutFactory<LAYOUT_TYPE> layoutFactory) {
        Assert.paramNotNull(layoutFactory, "layoutFactory");
        final LAYOUT_TYPE result = layoutFactory.create(this);
        setLayout(result);
        return result;
    }

    @Override
    public void setMinSize(final Dimension minSize) {
        this.minSize = minSize;
    }

    @Override
    public Dimension getMinSize() {
        if (minSize != null) {
            return minSize;
        }
        else {
            return Dimension.MIN;
        }
    }

    @Override
    public IScrollBar getHorizontalScrollBar() {
        return horizonalScrollBar;
    }

    @Override
    public IScrollBar getVerticalScrollBar() {
        return verticalScrollBar;
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
    public IContainer getParent() {
        return controlDelegate.getParent();
    }

    @Override
    public void setParent(final IContainer parent) {
        controlDelegate.setParent(parent);
    }

    @Override
    public void addParentListener(final IParentListener<IContainer> listener) {
        controlDelegate.addParentListener(listener);
    }

    @Override
    public void removeParentListener(final IParentListener<IContainer> listener) {
        controlDelegate.removeParentListener(listener);
    }

    @Override
    public boolean isReparentable() {
        return controlDelegate.isReparentable();
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
    public boolean isDisposed() {
        return containerDelegate.isDisposed();
    }

    @Override
    public void dispose() {
        containerDelegate.dispose();
    }

    @Override
    public IPopupMenu createPopupMenu() {
        return containerDelegate.createPopupMenu();
    }

    @Override
    public IDragSource getDragSource() {
        return dragSource;
    }

    @Override
    public IDropTarget getDropTarget() {
        return dropTarget;
    }

    @Override
    public void setViewportPosition(final Position position) {
        getWidget().setViewportPosition(position);
    }

    @Override
    public void setViewportPosition(final int x, final int y) {
        getWidget().setViewportPosition(new Position(x, y));
    }

    @Override
    public Position getViewportPosition() {
        return getWidget().getViewportPosition();
    }

    @Override
    public Dimension getViewportSize() {
        return getWidget().getViewportSize();
    }

    @Override
    public void scrollToTop() {
        setViewportPosition(new Position(0, 0));
    }

    @Override
    public void scrollToBottom() {
        final Rectangle clientArea = getClientArea();
        setViewportPosition(new Position(0, clientArea.getHeight()));
    }

    @Override
    public void showControl(final IControl control) {
        Assert.paramNotNull(control, "control");
        if (control.isDisposed()) {
            throw new IllegalArgumentException("Control is disposed");
        }
        else if (!getChildren().contains(control)) {
            throw new IllegalArgumentException("The given control is not a child of this container");
        }
        scrollRectToVisible(control.getBounds());
    }

    @Override
    public void scrollRectToVisible(final Rectangle rectangle) {

        int shiftX = 0;
        int shiftY = 0;

        final Dimension size = getSize();

        shiftX = calculateShift(size.getWidth(), rectangle.getX(), rectangle.getWidth());
        shiftY = calculateShift(size.getHeight(), rectangle.getY(), rectangle.getHeight());

        if (shiftX != 0 || shiftY != 0) {

            final Position viewportPos = getViewportPosition();
            final Dimension viewportSize = getViewportSize();
            final Dimension viewSize = getClientArea().getSize();

            int viewportX = viewportPos.getX();
            int viewportY = viewportPos.getY();

            viewportX -= shiftX;
            viewportY -= shiftY;

            //fix viewportX
            if (viewportX + viewportSize.getWidth() > viewSize.getWidth()) {
                viewportX = Math.max(0, viewSize.getWidth() - viewportSize.getWidth());
            }
            else if (viewportX < 0) {
                viewportX = 0;
            }

            //fix viewportY
            if (viewportY + viewportSize.getHeight() > viewSize.getHeight()) {
                viewportY = Math.max(0, viewSize.getHeight() - viewportSize.getHeight());
            }
            else if (viewportY < 0) {
                viewportY = 0;
            }

            //change viewportPosiition if necessary
            if (viewportX != viewportPos.getX() || viewportY != viewportPos.getY()) {
                setViewportPosition(viewportX, viewportY);
            }
        }

    }

    private int calculateShift(final int parentSize, final int childPos, final int childSize) {
        if (childPos >= 0 && childSize + childPos <= parentSize) {
            return 0;
        }
        else if (childPos <= 0 && childSize + childPos >= parentSize) {
            return 0;
        }
        else if (childPos > 0 && childSize <= parentSize) {
            return -childPos + parentSize - childSize;
        }
        else if (childPos >= 0 && childSize >= parentSize) {
            return -childPos;
        }
        else if (childPos <= 0 && childSize <= parentSize) {
            return -childPos;
        }
        else if (childPos < 0 && childSize >= parentSize) {
            return -childPos + parentSize - childSize;
        }
        else {
            return 0;
        }
    }

}
