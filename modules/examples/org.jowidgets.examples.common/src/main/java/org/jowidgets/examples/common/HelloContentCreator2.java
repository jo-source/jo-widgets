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
package org.jowidgets.examples.common;

import org.jowidgets.api.convert.IConverterProvider;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.IToolkit;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.blueprint.ICheckBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.api.widgets.blueprint.IToggleButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.validation.tools.MandatoryValidator;

public class HelloContentCreator2 implements IInputContentCreator<String> {

    @Override
    public void createContent(final IInputContentContainer widgetContainer) {
        widgetContainer.setLayout(new MigLayoutDescriptor("[grow]", "[][][][]"));

        final IToolkit toolkit = Toolkit.getInstance();

        final IBluePrintFactory bpF = toolkit.getBluePrintFactory();
        final IConverterProvider converter = toolkit.getConverterProvider();

        IToggleButtonBluePrint toggleButtonBluePrint = bpF.toggleButton();
        toggleButtonBluePrint.setText("Text").setToolTipText("ToolTiptext").alignCenter();
        widgetContainer.add("Text", toggleButtonBluePrint, "wrap, growx");

        toggleButtonBluePrint = bpF.toggleButton();
        toggleButtonBluePrint.setText("Text2").setToolTipText("ToolTiptext").setIcon(IconsSmall.OK).alignCenter();
        widgetContainer.add("Text2", toggleButtonBluePrint, "wrap, growx");

        final ICheckBoxBluePrint checkBoxBp = bpF.checkBox().setText("Checkbox").setToolTipText("Tooltip");
        widgetContainer.add("Checkbox", checkBoxBp, "wrap, growx");

        final IComboBoxSelectionBluePrint<String> comboBoxBp1 = bpF.comboBoxSelection(" ", "one", "two", "three");
        widgetContainer.add("cBox1", comboBoxBp1, "wrap, growx");

        final IComboBoxBluePrint<String> comboBoxBp2 = bpF.comboBox("red", "green", "blue");
        final IComboBox<String> cBox2 = widgetContainer.add("cBox2", comboBoxBp2, "wrap, growx");
        cBox2.addValidator(new MandatoryValidator<String>("mandatory"));

        final IComboBoxBluePrint<Long> comboBoxBp3 = bpF.comboBox(converter.longNumber());
        comboBoxBp3.setElements(Long.valueOf(23456), Long.valueOf(15468), Long.valueOf(5345519));
        final IComboBox<Long> cBox3 = widgetContainer.add("cBox3", comboBoxBp3, "wrap, growx");
        cBox3.addValidator(new MandatoryValidator<Long>("mandatory"));
    }

    @Override
    public void setValue(final String content) {}

    @Override
    public String getValue() {
        return null;
    }

}
