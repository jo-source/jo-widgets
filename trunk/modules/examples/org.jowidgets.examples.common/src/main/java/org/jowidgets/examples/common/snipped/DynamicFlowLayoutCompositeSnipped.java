/*
 * Copyright (c) 2015, grossmann
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
package org.jowidgets.examples.common.snipped;

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.layout.DynamicFlowLayoutConstraints;
import org.jowidgets.api.layout.FillLayout;
import org.jowidgets.api.layout.IDynamicFlowLayoutConstraints;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IDynamicFlowLayoutComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class DynamicFlowLayoutCompositeSnipped implements IApplication {

    @Override
    public void start(final IApplicationLifecycle lifecycle) {

        //create the root frame
        final IFrame frame = Toolkit.createRootFrame(BPF.frame("Dynamic flow layout composite snipped"), lifecycle);
        frame.setLayout(FillLayout.get());

        //create a dynamic flow layout composite
        final IDynamicFlowLayoutComposite composite = frame.add(BPF.dynamicFlowLayoutComposite().setOrientation(
                Orientation.VERTICAL));

        //add a combobox
        final IDynamicFlowLayoutConstraints const1 = DynamicFlowLayoutConstraints.builder().build();
        composite.addLast(BPF.comboBoxSelection("Hans Martin Schleier", "Dog", "Cat", "Mouse").autoCompletionOff(), const1);

        //add a growing space
        final IDynamicFlowLayoutConstraints const2 = DynamicFlowLayoutConstraints.builder()
                .growHeight()
                .minHeight(0)
                .preferredHeight(0)
                .growWidth()
                .minWidth(0)
                .preferredWidth(0)
                .build();
        composite.addLast(BPF.textLabel(), const2);

        //add checkboxes
        final IDynamicFlowLayoutConstraints const3 = DynamicFlowLayoutConstraints.builder().build();
        final IComposite checkBoxContainer = composite.addLast(BPF.composite(), const3);

        checkBoxContainer.setLayout(new MigLayoutDescriptor("wrap", "0[]0", "0[]0[]0"));
        checkBoxContainer.add(BPF.checkBox().setText("Foo1"));

        checkBoxContainer.setLayout(new MigLayoutDescriptor("wrap", "0[]0", "0[]0[]0"));
        checkBoxContainer.add(BPF.checkBox().setText("Foo2"));

        //now insert a addon button at the first position
        final IDynamicFlowLayoutConstraints const4 = DynamicFlowLayoutConstraints.builder()
                .useWidthOfElementAt(0)
                .useHeightOfElementAt(0)
                .build();
        composite.addLast(BPF.button().setIcon(IconsSmall.DISK).setRemoveEmptyBorder(true), const4);

        //set the root frame visible
        frame.setVisible(true);
    }
}
