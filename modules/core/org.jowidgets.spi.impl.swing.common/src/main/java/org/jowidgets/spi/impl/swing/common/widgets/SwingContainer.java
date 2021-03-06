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
package org.jowidgets.spi.impl.swing.common.widgets;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.Collection;

import net.miginfocom.swing.MigLayout;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IMouseMotionListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.spi.impl.swing.common.layout.LayoutManagerImpl;
import org.jowidgets.spi.impl.swing.common.util.DecorationCalc;
import org.jowidgets.spi.impl.swing.common.widgets.util.ChildRemover;
import org.jowidgets.spi.widgets.IContainerSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.util.Assert;

public class SwingContainer implements IContainerSpi {

    private final IGenericWidgetFactory factory;
    private final SwingComponent swingComponentDelegate;

    private Container container;

    public SwingContainer(final IGenericWidgetFactory factory, final Container container) {
        Assert.paramNotNull(factory, "factory");
        Assert.paramNotNull(container, "container");

        this.factory = factory;
        this.container = container;
        this.swingComponentDelegate = new SwingComponent(container);
    }

    public void setContainer(final Container container) {
        this.container = container;
        swingComponentDelegate.setComponent(container);
    }

    @Override
    public final void setLayout(final ILayoutDescriptor layout) {
        Assert.paramNotNull(layout, "layout");
        if (layout instanceof MigLayoutDescriptor) {
            final MigLayoutDescriptor migLayoutManager = (MigLayoutDescriptor) layout;
            container.setLayout(new MigLayout(
                migLayoutManager.getLayoutConstraints(),
                migLayoutManager.getColumnConstraints(),
                migLayoutManager.getRowConstraints()));
        }
        else if (layout instanceof ILayouter) {
            container.setLayout(new LayoutManagerImpl(getUiReference(), (ILayouter) layout));
        }
        else {
            throw new IllegalArgumentException("Layout Descriptor of type '" + layout.getClass().getName() + "' is not supported");
        }
    }

    @Override
    public Container getUiReference() {
        return container;
    }

    @Override
    public void redraw() {
        swingComponentDelegate.redraw();
    }

    @Override
    public void setRedrawEnabled(final boolean enabled) {
        swingComponentDelegate.setRedrawEnabled(enabled);
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        swingComponentDelegate.setForegroundColor(colorValue);
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        swingComponentDelegate.setBackgroundColor(colorValue);
    }

    @Override
    public IColorConstant getForegroundColor() {
        return swingComponentDelegate.getForegroundColor();
    }

    @Override
    public IColorConstant getBackgroundColor() {
        return swingComponentDelegate.getBackgroundColor();
    }

    @Override
    public void setCursor(final Cursor cursor) {
        swingComponentDelegate.setCursor(cursor);
    }

    @Override
    public void setVisible(final boolean visible) {
        swingComponentDelegate.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return swingComponentDelegate.isVisible();
    }

    @Override
    public void setEnabled(final boolean enabled) {
        swingComponentDelegate.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return swingComponentDelegate.isEnabled();
    }

    @Override
    public Rectangle getClientArea() {
        return DecorationCalc.getClientArea(getUiReference());
    }

    @Override
    public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
        Assert.paramNotNull(clientAreaSize, "clientAreaSize");
        return DecorationCalc.computeDecoratedSize(container, clientAreaSize);
    }

    @Override
    public Dimension getSize() {
        return swingComponentDelegate.getSize();
    }

    @Override
    public void setSize(final Dimension size) {
        swingComponentDelegate.setSize(size);
    }

    @Override
    public Position getPosition() {
        return swingComponentDelegate.getPosition();
    }

    @Override
    public void setPosition(final Position position) {
        swingComponentDelegate.setPosition(position);
    }

    @Override
    public IPopupMenuSpi createPopupMenu() {
        return swingComponentDelegate.createPopupMenu();
    }

    @Override
    public boolean requestFocus() {
        return swingComponentDelegate.requestFocus();
    }

    @Override
    public void addFocusListener(final IFocusListener listener) {
        swingComponentDelegate.addFocusListener(listener);
    }

    @Override
    public void removeFocusListener(final IFocusListener listener) {
        swingComponentDelegate.removeFocusListener(listener);
    }

    @Override
    public void addKeyListener(final IKeyListener listener) {
        swingComponentDelegate.addKeyListener(listener);
    }

    @Override
    public void removeKeyListener(final IKeyListener listener) {
        swingComponentDelegate.removeKeyListener(listener);
    }

    @Override
    public void addMouseListener(final IMouseListener mouseListener) {
        swingComponentDelegate.addMouseListener(mouseListener);
    }

    @Override
    public void removeMouseListener(final IMouseListener mouseListener) {
        swingComponentDelegate.removeMouseListener(mouseListener);
    }

    @Override
    public void addMouseMotionListener(final IMouseMotionListener listener) {
        swingComponentDelegate.addMouseMotionListener(listener);
    }

    @Override
    public void removeMouseMotionListener(final IMouseMotionListener listener) {
        swingComponentDelegate.removeMouseMotionListener(listener);
    }

    @Override
    public void addComponentListener(final IComponentListener componentListener) {
        swingComponentDelegate.addComponentListener(componentListener);
    }

    @Override
    public void removeComponentListener(final IComponentListener componentListener) {
        swingComponentDelegate.removeComponentListener(componentListener);
    }

    @Override
    public void addPopupDetectionListener(final IPopupDetectionListener listener) {
        swingComponentDelegate.addPopupDetectionListener(listener);
    }

    @Override
    public void removePopupDetectionListener(final IPopupDetectionListener listener) {
        swingComponentDelegate.removePopupDetectionListener(listener);
    }

    @Override
    public void setTabOrder(final Collection<? extends IControlCommon> tabOrder) {
        if (tabOrder == null) {
            getUiReference().setFocusTraversalPolicy(null);
            getUiReference().setFocusTraversalPolicyProvider(false);
        }
        else {
            getUiReference().setFocusTraversalPolicy(new TabListTraversalPolicy(tabOrder));
            getUiReference().setFocusTraversalPolicyProvider(true);
        }
    }

    @Override
    public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
        final Integer index,
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
        final Object cellConstraints) {
        final WIDGET_TYPE result = factory.create(getUiReference(), descriptor);
        addToContainer(index, result, cellConstraints);
        return result;
    }

    @Override
    public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
        final Integer index,
        final ICustomWidgetCreator<WIDGET_TYPE> customWidgetCreator,
        final Object cellConstraints) {
        final ICustomWidgetFactory customWidgetFactory = createCustomWidgetFactory();
        final WIDGET_TYPE result = customWidgetCreator.create(customWidgetFactory);
        addToContainer(index, result, cellConstraints);
        return result;
    }

    public void setToolTipText(final String toolTip) {
        swingComponentDelegate.setToolTipText(toolTip);
    }

    private ICustomWidgetFactory createCustomWidgetFactory() {
        return new ICustomWidgetFactory() {
            @Override
            public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE create(
                final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
                return factory.create(getUiReference(), descriptor);
            }
        };
    }

    @Override
    public boolean remove(final IControlCommon control) {
        return ChildRemover.removeChild(container, (Component) control.getUiReference());
    }

    @Override
    public void removeAll() {
        container.removeAll();
    }

    @Override
    public void layoutBegin() {
        //do nothing here (swing does not flicker like swt)
    }

    @Override
    public void layoutEnd() {
        container.doLayout();
        redraw();
    }

    protected IGenericWidgetFactory getGenericWidgetFactory() {
        return factory;
    }

    private void addToContainer(final Integer index, final IWidgetCommon widget, final Object cellConstraints) {
        if (cellConstraints != null) {
            if (index != null) {
                container.add((Component) (widget.getUiReference()), cellConstraints, index.intValue());
            }
            else {
                container.add((Component) (widget.getUiReference()), cellConstraints);
            }
        }
        else {
            if (index != null) {
                container.add((Component) (widget.getUiReference()), index.intValue());
            }
            else {
                container.add((Component) (widget.getUiReference()));
            }
        }
    }

    private final class TabListTraversalPolicy extends FocusTraversalPolicy {
        private final ArrayList<Component> tabOrder;

        private TabListTraversalPolicy(final Collection<? extends IControlCommon> tabOrder) {
            this.tabOrder = new ArrayList<Component>(tabOrder.size());
            for (final IControlCommon control : tabOrder) {
                this.tabOrder.add((Component) control.getUiReference());
            }
        }

        @Override
        public Component getComponentAfter(final Container aContainer, final Component aComponent) {
            final int index = tabOrder.indexOf(aComponent) + 1;
            if (index >= tabOrder.size()) {
                return getFirstComponent(aContainer);
            }
            else {
                return tabOrder.get(index);
            }
        }

        @Override
        public Component getComponentBefore(final Container aContainer, final Component aComponent) {
            final int index = tabOrder.indexOf(aComponent) - 1;
            if (index < 0) {
                return getLastComponent(aContainer);
            }
            else {
                return tabOrder.get(index);
            }
        }

        @Override
        public Component getFirstComponent(final Container aContainer) {
            return tabOrder.get(0);
        }

        @Override
        public Component getLastComponent(final Container aContainer) {
            return tabOrder.get(tabOrder.size() - 1);
        }

        @Override
        public Component getDefaultComponent(final Container aContainer) {
            return getFirstComponent(aContainer);
        }

    }
}
