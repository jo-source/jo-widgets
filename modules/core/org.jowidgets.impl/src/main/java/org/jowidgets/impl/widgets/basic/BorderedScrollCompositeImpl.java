/*
 * Copyright (c) 2015, Michael
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

import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IScrollComposite;
import org.jowidgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jowidgets.api.widgets.descriptor.IScrollCompositeDescriptor;
import org.jowidgets.common.types.Border;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.tools.widgets.wrapper.CompositeWrapper;

public final class BorderedScrollCompositeImpl extends CompositeWrapper implements IScrollComposite {

    private final IScrollComposite scrollComposite;

    public BorderedScrollCompositeImpl(final IComposite composite, final IScrollCompositeDescriptor descriptor) {
        super(composite);
        final IScrollCompositeBluePrint scrollCompositeBp = BPF.scrollComposite();
        scrollCompositeBp.setSetup(descriptor);
        scrollCompositeBp.setBorder((Border) null);

        composite.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0"));
        this.scrollComposite = composite.add(scrollCompositeBp, "growx, growy, w 0::, h 0::");
    }

    @Override
    public Rectangle getClientArea() {
        return scrollComposite.getClientArea();
    }

    @Override
    public void setLayout(final ILayoutDescriptor layoutDescriptor) {
        scrollComposite.setLayout(layoutDescriptor);
    }

    @Override
    public <LAYOUT_TYPE extends ILayouter> LAYOUT_TYPE setLayout(final ILayoutFactory<LAYOUT_TYPE> layoutFactory) {
        return scrollComposite.setLayout(layoutFactory);
    }

    @Override
    public void removeAll() {
        scrollComposite.removeAll();
    }

    @Override
    public List<IControl> getChildren() {
        return scrollComposite.getChildren();
    }

    @Override
    public boolean remove(final IControl control) {
        return scrollComposite.remove(control);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final int index,
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
        final Object layoutConstraints) {
        return scrollComposite.add(index, descriptor, layoutConstraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final int index,
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
        return scrollComposite.add(index, descriptor);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final int index,
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final Object layoutConstraints) {
        return scrollComposite.add(index, creator, layoutConstraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final int index, final ICustomWidgetCreator<WIDGET_TYPE> creator) {
        return scrollComposite.add(index, creator);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
        final Object layoutConstraints) {
        return scrollComposite.add(descriptor, layoutConstraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final Object layoutConstraints) {
        return scrollComposite.add(creator, layoutConstraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
        return scrollComposite.add(descriptor);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final ICustomWidgetCreator<WIDGET_TYPE> creator) {
        return scrollComposite.add(creator);
    }

    @Override
    public void setTabOrder(final Collection<? extends IControl> tabOrder) {
        scrollComposite.setTabOrder(tabOrder);
    }

    @Override
    public void setTabOrder(final IControl... controls) {
        scrollComposite.setTabOrder(controls);
    }

}
