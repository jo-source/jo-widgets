/*
 * Copyright (c) 2015, Michael Grossmann
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
package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.layout.IDynamicFlowLayoutConstraints;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IDynamicFlowLayoutComposite;
import org.jowidgets.api.widgets.descriptor.setup.IDynamicFlowLayoutCompositeSetup;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.Assert;

public final class DynamicFlowLayoutCompositeImpl extends ControlWrapper implements IDynamicFlowLayoutComposite {

    public DynamicFlowLayoutCompositeImpl(final IComposite composite, final IDynamicFlowLayoutCompositeSetup setup) {
        super(composite);
        Assert.paramNotNull(setup.getOrientation(), "setup.getOrientation()");
    }

    @Override
    protected IComposite getWidget() {
        return (IComposite) super.getWidget();
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE addFirst(
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
        final IDynamicFlowLayoutConstraints constraints) {
        return addFirst(getCreatorForDescriptor(descriptor), constraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE addLast(
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
        final IDynamicFlowLayoutConstraints constraints) {
        return addLast(getCreatorForDescriptor(descriptor), constraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final int index,
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
        final IDynamicFlowLayoutConstraints constraints) {
        return add(index, getCreatorForDescriptor(descriptor), constraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE addFirst(
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final IDynamicFlowLayoutConstraints constraints) {
        return add(0, creator, constraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE addLast(
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final IDynamicFlowLayoutConstraints constraints) {
        return add(getChildrenCount(), creator, constraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final int index,
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final IDynamicFlowLayoutConstraints constraints) {
        return addImpl(index, creator, constraints);
    }

    @Override
    public int getChildrenCount() {
        return getWidget().getChildren().size();
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
    public void layout() {
        getWidget().layout();
    }

    private <WIDGET_TYPE extends IControl> ICustomWidgetCreator<WIDGET_TYPE> getCreatorForDescriptor(
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
        return new ICustomWidgetCreator<WIDGET_TYPE>() {
            @Override
            public WIDGET_TYPE create(final ICustomWidgetFactory widgetFactory) {
                return widgetFactory.create(descriptor);
            }
        };
    }

    private <WIDGET_TYPE extends IControl> WIDGET_TYPE addImpl(
        final int index,
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final IDynamicFlowLayoutConstraints constraints) {
        final WIDGET_TYPE result = getWidget().add(index, creator, getMigCellConstraints(constraints));
        return result;
    }

    private String getMigCellConstraints(final IDynamicFlowLayoutConstraints constraints) {
        //TODO 
        return "";
    }

    @SuppressWarnings("unused")
    private String getMigLayoutConstraints(final IDynamicFlowLayoutConstraints constraints) {
        //TODO 
        return "";
    }
}
