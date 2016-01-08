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

import java.util.Collection;

import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.LayoutCallback;
import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IMouseMotionListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.spi.dnd.IDragSourceSpi;
import org.jowidgets.spi.dnd.IDropTargetSpi;
import org.jowidgets.spi.impl.swt.common.dnd.ImmutableDropSelection;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;
import org.jowidgets.spi.impl.swt.common.util.PositionConvert;
import org.jowidgets.spi.impl.swt.common.util.ScrollBarSettingsConvert;
import org.jowidgets.spi.impl.swt.common.widgets.base.ScrollRootComposite;
import org.jowidgets.spi.impl.swt.common.widgets.base.SwtCompositeFactory;
import org.jowidgets.spi.impl.widgets.DummyScrollBar;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.IScrollBarSpi;
import org.jowidgets.spi.widgets.IScrollCompositeSpi;
import org.jowidgets.spi.widgets.setup.IScrollCompositeSetupSpi;
import org.jowidgets.util.Assert;

public class ScrollCompositeImpl implements IScrollCompositeSpi {

    private final SwtComposite outerContainer;
    private final SwtContainer innerContainer;
    private final Composite scrolledRoot;
    private final ScrolledComposite scrolledComposite;
    private final IScrollBarSpi verticalScrollBar;
    private final IScrollBarSpi horizontalScrollBar;

    public ScrollCompositeImpl(
        final IGenericWidgetFactory factory,
        final Object parentUiReference,
        final IScrollCompositeSetupSpi setup,
        final SwtImageRegistry imageRegistry) {

        final MigLayout growingMigLayout = new MigLayout("", "0[grow, 0::]0", "0[grow, 0::]0");
        final String growingCellConstraints = "grow, w 0::,h 0::";

        this.scrolledRoot = new ScrollRootComposite((Composite) parentUiReference, SWT.NONE);
        scrolledRoot.setBackgroundMode(SWT.INHERIT_FORCE);

        this.outerContainer = new SwtComposite(factory, scrolledRoot, new ImmutableDropSelection(this), imageRegistry);
        scrolledRoot.setLayout(growingMigLayout);

        this.scrolledComposite = new ScrolledComposite(scrolledRoot, ScrollBarSettingsConvert.convert(setup)) {
            @Override
            public Point computeSize(final int wHint, final int hHint, final boolean changed) {
                return innerContainer.getUiReference().computeSize(wHint, hHint, changed);
            }
        };
        scrolledComposite.setLayoutData(growingCellConstraints);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setAlwaysShowScrollBars(setup.isAlwaysShowBars());
        scrolledComposite.setBackgroundMode(SWT.INHERIT_FORCE);

        final ScrollBar verticalBar = scrolledComposite.getVerticalBar();
        if (verticalBar != null) {
            this.verticalScrollBar = new ScrollBarImpl(verticalBar);
        }
        else {
            this.verticalScrollBar = new DummyScrollBar();
        }

        final ScrollBar horizontalBar = scrolledComposite.getHorizontalBar();
        if (horizontalBar != null) {
            this.horizontalScrollBar = new ScrollBarImpl(horizontalBar);
        }
        else {
            this.horizontalScrollBar = new DummyScrollBar();
        }

        final Composite contentComposite = SwtCompositeFactory.create(scrolledComposite, SWT.NONE);
        contentComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
        contentComposite.setLayoutData(growingCellConstraints);

        final MigLayout contentLayout = new MigLayout("", "0[grow, 0::]0", "0[grow, 0::]0");
        contentComposite.setLayout(contentLayout);

        scrolledComposite.setContent(contentComposite);

        final Composite innerComposite = SwtCompositeFactory.create(contentComposite, SWT.NONE);
        innerComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
        innerComposite.setLayoutData(growingCellConstraints);

        this.innerContainer = new SwtContainer(factory, innerComposite, imageRegistry);

        contentLayout.addLayoutCallback(new LayoutCallback() {
            @Override
            public void correctBounds(final ComponentWrapper comp) {
                final Point size = contentComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
                scrolledComposite.setMinSize(size.x, size.y);
            }
        });
    }

    @Override
    public final void setLayout(final ILayoutDescriptor layout) {
        innerContainer.setLayout(layout);
    }

    @Override
    public Composite getUiReference() {
        return outerContainer.getUiReference();
    }

    @Override
    public IScrollBarSpi getHorizontalScrollBar() {
        return horizontalScrollBar;
    }

    @Override
    public IScrollBarSpi getVerticalScrollBar() {
        return verticalScrollBar;
    }

    @Override
    public Rectangle getClientArea() {
        return innerContainer.getClientArea();
    }

    @Override
    public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
        return outerContainer.computeDecoratedSize(clientAreaSize);
    }

    @Override
    public Dimension getMinSize() {
        return Dimension.MIN;
    }

    @Override
    public Dimension getPreferredSize() {
        return outerContainer.getPreferredSize();
    }

    @Override
    public Dimension getMaxSize() {
        return outerContainer.getMaxSize();
    }

    @Override
    public void setLayoutConstraints(final Object layoutConstraints) {
        outerContainer.setLayoutConstraints(layoutConstraints);
    }

    @Override
    public Object getLayoutConstraints() {
        return outerContainer.getLayoutConstraints();
    }

    @Override
    public void setVisible(final boolean visible) {
        outerContainer.setVisible(visible);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        outerContainer.setEnabled(enabled);
        innerContainer.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return outerContainer.isEnabled();
    }

    @Override
    public boolean isVisible() {
        return outerContainer.isVisible();
    }

    @Override
    public Dimension getSize() {
        return outerContainer.getSize();
    }

    @Override
    public void setSize(final Dimension size) {
        outerContainer.setSize(size);
    }

    @Override
    public Position getPosition() {
        return outerContainer.getPosition();
    }

    @Override
    public void setPosition(final Position position) {
        outerContainer.setPosition(position);
    }

    @Override
    public void redraw() {
        outerContainer.redraw();
    }

    @Override
    public void setRedrawEnabled(final boolean enabled) {
        outerContainer.setRedrawEnabled(enabled);
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        outerContainer.setForegroundColor(colorValue);
        innerContainer.setForegroundColor(colorValue);
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        outerContainer.setBackgroundColor(colorValue);
        innerContainer.setBackgroundColor(colorValue);
    }

    @Override
    public void setCursor(final Cursor cursor) {
        outerContainer.setCursor(cursor);
    }

    @Override
    public IColorConstant getForegroundColor() {
        return innerContainer.getForegroundColor();
    }

    @Override
    public IColorConstant getBackgroundColor() {
        return innerContainer.getBackgroundColor();
    }

    @Override
    public boolean requestFocus() {
        return innerContainer.requestFocus();
    }

    @Override
    public void addFocusListener(final IFocusListener listener) {
        innerContainer.addFocusListener(listener);
    }

    @Override
    public void removeFocusListener(final IFocusListener listener) {
        innerContainer.removeFocusListener(listener);
    }

    @Override
    public void addKeyListener(final IKeyListener listener) {
        innerContainer.addKeyListener(listener);
    }

    @Override
    public void removeKeyListener(final IKeyListener listener) {
        innerContainer.removeKeyListener(listener);
    }

    @Override
    public void addMouseListener(final IMouseListener mouseListener) {
        innerContainer.addMouseListener(mouseListener);
    }

    @Override
    public void removeMouseListener(final IMouseListener mouseListener) {
        innerContainer.removeMouseListener(mouseListener);
    }

    @Override
    public void addMouseMotionListener(final IMouseMotionListener listener) {
        innerContainer.addMouseMotionListener(listener);
    }

    @Override
    public void removeMouseMotionListener(final IMouseMotionListener listener) {
        innerContainer.addMouseMotionListener(listener);
    }

    @Override
    public void addComponentListener(final IComponentListener componentListener) {
        innerContainer.addComponentListener(componentListener);
    }

    @Override
    public void removeComponentListener(final IComponentListener componentListener) {
        innerContainer.removeComponentListener(componentListener);
    }

    @Override
    public IPopupMenuSpi createPopupMenu() {
        return innerContainer.createPopupMenu();
    }

    @Override
    public void addPopupDetectionListener(final IPopupDetectionListener listener) {
        innerContainer.addPopupDetectionListener(listener);
    }

    @Override
    public void removePopupDetectionListener(final IPopupDetectionListener listener) {
        innerContainer.removePopupDetectionListener(listener);
    }

    @Override
    public void setTabOrder(final Collection<? extends IControlCommon> tabOrder) {
        innerContainer.setTabOrder(tabOrder);
    }

    @Override
    public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
        final Integer index,
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
        final Object cellConstraints) {
        final WIDGET_TYPE result = innerContainer.add(index, descriptor, cellConstraints);
        return result;
    }

    @Override
    public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
        final Integer index,
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final Object cellConstraints) {
        return innerContainer.add(index, creator, cellConstraints);
    }

    @Override
    public boolean remove(final IControlCommon control) {
        return innerContainer.remove(control);
    }

    @Override
    public void layoutBegin() {
        outerContainer.layoutBegin();
    }

    @Override
    public void layoutEnd() {
        outerContainer.layoutEnd();
    }

    @Override
    public void removeAll() {
        innerContainer.removeAll();
    }

    @Override
    public void setToolTipText(final String toolTip) {
        innerContainer.setToolTipText(toolTip);
    }

    @Override
    public IDragSourceSpi getDragSource() {
        return outerContainer.getDragSource();
    }

    @Override
    public IDropTargetSpi getDropTarget() {
        return outerContainer.getDropTarget();
    }

    @Override
    public void setViewportPosition(final Position position) {
        Assert.paramNotNull(position, "position");
        scrolledComposite.setOrigin(PositionConvert.convert(position));
    }

    @Override
    public Position getViewportPosition() {
        return PositionConvert.convert(scrolledComposite.getOrigin());
    }

    @Override
    public Dimension getViewportSize() {
        final org.eclipse.swt.graphics.Rectangle clientArea = scrolledComposite.getClientArea();
        return new Dimension(clientArea.width, clientArea.height);
    }

}
