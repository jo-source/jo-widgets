/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.tools.widgets.base;

import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.descriptor.ICompositeDescriptor;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.tools.widgets.wrapper.AbstractInputControl;
import org.jowidgets.util.Assert;

/**
 * A AbstractCompositeInputControl can be used to encapsulate a IInputControl implementation that uses a composite
 * to add the inner controls.
 */
public abstract class AbstractCompositeInputControl<VALUE_TYPE> extends AbstractInputControl<VALUE_TYPE> implements
        IInputControl<VALUE_TYPE> {

    /**
     * Creates a new instance
     * 
     * @param parent The parent container where to add the control to
     */
    public AbstractCompositeInputControl(final IContainer parent) {
        this(parent, BPF.composite(), null);
    }

    /**
     * Creates a new instance
     * 
     * @param parent The parent container where to add the control to
     * @param index The index where to add the control into the containers child list
     */
    public AbstractCompositeInputControl(final IContainer parent, final int index) {
        this(parent, index, BPF.composite(), null);
    }

    /**
     * Creates a new instance
     * 
     * @param parent The parent container where to add the control to
     * @param descriptor The descriptor that will be used to create the composite
     */
    public AbstractCompositeInputControl(final IContainer parent, final ICompositeDescriptor descriptor) {
        this(parent, descriptor, null);
    }

    /**
     * Creates a new instance
     * 
     * @param parent The parent container where to add the control to
     * @param index The index where to add the control into the containers child list
     * @param descriptor The descriptor that will be used to create the composite
     */
    public AbstractCompositeInputControl(final IContainer parent, final int index, final ICompositeDescriptor descriptor) {
        this(parent, index, descriptor, null);
    }

    /**
     * Creates a new instance
     * 
     * @param parent The parent container where to add the control to
     * @param layoutConstraints The layout constraints that will be used for the control
     */
    public AbstractCompositeInputControl(final IContainer parent, final Object layoutConstraints) {
        this(parent, BPF.composite(), layoutConstraints);
    }

    /**
     * Creates a new instance
     * 
     * @param parent The parent container where to add the control to
     * @param index The index where to add the control into the containers child list
     * @param layoutConstraints The layout constraints that will be used for the control
     */
    public AbstractCompositeInputControl(final IContainer parent, final int index, final Object layoutConstraints) {
        this(parent, index, BPF.composite(), layoutConstraints);
    }

    /**
     * Creates a new instance
     * 
     * @param parent The parent container where to add the control to
     * @param descriptor The descriptor that will be used to create the composite
     * @param layoutConstraints The layout constraints that will be used for the control
     */
    public AbstractCompositeInputControl(
        final IContainer parent,
        final ICompositeDescriptor descriptor,
        final Object layoutConstraints) {
        super(Assert.getParamNotNull(parent, "parent").add(descriptor, layoutConstraints));
    }

    /**
     * Creates a new instance
     * 
     * @param parent The parent container where to add the control to
     * @param index The index where to add the control into the containers child list
     * @param descriptor The descriptor that will be used to create the composite
     * @param layoutConstraints The layout constraints that will be used for the control
     */
    public AbstractCompositeInputControl(
        final IContainer parent,
        final int index,
        final ICompositeDescriptor descriptor,
        final Object layoutConstraints) {
        super(Assert.getParamNotNull(parent, "parent").add(index, descriptor, layoutConstraints));
    }

    /**
     * Gets the composite where the content of this control can be added
     * 
     * @return The composite, never null
     */
    protected IComposite getComposite() {
        return (IComposite) super.getWidget();
    }

}
