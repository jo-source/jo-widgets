/*
 * Copyright (c) 2017, grossmann
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

package org.jowidgets.api.test;

import java.util.Locale;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.validation.IValidationConditionListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import junit.framework.Assert;

public class DoubleFieldTest {

    private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

    private IFrame rootFrame;
    private IInputField<Double> inputField;

    @Before
    public void setUp() {
        rootFrame = Toolkit.createRootFrame(BPF.frame());
        final IConverter<Double> doubleConverter = Toolkit.getConverterProvider().doubleNumber(Locale.GERMANY);
        final IInputFieldBluePrint<Double> inputFieldBp = BPF.inputField(doubleConverter);
        inputField = rootFrame.add(inputFieldBp);
        rootFrame.setVisible(true);
    }

    @After
    public void tearDown() {
        rootFrame.dispose();
    }

    @Test
    public void testSetValue() {
        final IInputListener inputListener = Mockito.mock(IInputListener.class);
        final IValidationConditionListener validationListener = Mockito.mock(IValidationConditionListener.class);

        inputField.addInputListener(inputListener);
        inputField.addValidationConditionListener(validationListener);

        final Double value = Double.valueOf(1.0);
        inputField.setValue(value);

        Assert.assertEquals(value, inputField.getValue());
        final InOrder inOrder = Mockito.inOrder(inputListener, validationListener);
        inOrder.verify(inputListener, Mockito.times(1)).inputChanged();
        inOrder.verify(validationListener, Mockito.times(1)).validationConditionsChanged();
    }

    @Test
    public void testSetText() {
        final IInputListener inputListener = Mockito.mock(IInputListener.class);
        final IValidationConditionListener validationListener = Mockito.mock(IValidationConditionListener.class);

        inputField.addInputListener(inputListener);
        inputField.addValidationConditionListener(validationListener);

        final String text = "1,25";
        inputField.setText(text);

        Assert.assertEquals(Double.valueOf(1.25d), inputField.getValue());
        Assert.assertEquals(text, inputField.getText());
        final InOrder inOrder = Mockito.inOrder(inputListener, validationListener);
        inOrder.verify(inputListener, Mockito.times(1)).inputChanged();
        inOrder.verify(validationListener, Mockito.times(1)).validationConditionsChanged();
    }

}
