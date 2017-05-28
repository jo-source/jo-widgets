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
import org.jowidgets.api.widgets.IUnitValueField;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.IUnitValueFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.unit.tools.converter.LongDoubleUnitConverter;
import org.jowidgets.unit.tools.units.HertzUnitSet;
import org.jowidgets.validation.IValidationMessage;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.MessageType;
import org.jowidgets.validation.ValidationResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class UnitValueFieldTest {

    private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

    private IFrame rootFrame;
    private IUnitValueField<Long, Double> unitField;

    @Before
    public void setUp() {

        rootFrame = Toolkit.createRootFrame(BPF.frame());

        final IConverter<Double> doubleConverter = Toolkit.getConverterProvider().doubleNumber(Locale.GERMANY);
        final IInputFieldBluePrint<Double> inputFieldBp = BPF.inputField(doubleConverter);
        final IUnitValueFieldBluePrint<Long, Double> unitFieldBp = BPF.unitValueField();
        unitFieldBp.setUnitValueInputField(inputFieldBp);
        unitFieldBp.setUnitSet(HertzUnitSet.instance());
        unitFieldBp.setDefaultUnit(HertzUnitSet.MH);
        unitFieldBp.setUnitConverter(new LongDoubleUnitConverter(HertzUnitSet.MH));
        unitField = rootFrame.add(unitFieldBp);

        rootFrame.setVisible(true);
    }

    @After
    public void tearDown() {
        rootFrame.dispose();
    }

    @Test
    public void testWarningOfUnderlyingInputField() {
        unitField.setText("1,2345678911234567");
        Assert.assertEquals(MessageType.WARNING, unitField.validate().getWorstFirst().getType());
    }

    @Test
    public void testErrorOfUnderlyingInputField() {
        unitField.setText("1,23,45678911234567");
        Assert.assertEquals(MessageType.ERROR, unitField.validate().getWorstFirst().getType());
    }

    @Test
    public void testErrorOfUnitConverter() {
        unitField.setUnit(HertzUnitSet.H);
        unitField.setText("10000000000000000000");
        Assert.assertEquals(MessageType.ERROR, unitField.validate().getWorstFirst().getType());
    }

    @Test
    public void testWarningOfUnitConverter() {
        unitField.setUnit(HertzUnitSet.H);
        unitField.setText("1,25");
        Assert.assertEquals(MessageType.WARNING, unitField.validate().getWorstFirst().getType());
    }

    @Test
    public void testAddValidator() {
        final IValidationResult fooError = ValidationResult.error("Foo");
        unitField.addValidator(new IValidator<Long>() {
            @Override
            public IValidationResult validate(final Long value) {
                return fooError;
            }
        });
        unitField.setText("1,2345678911234567");
        Assert.assertSame(fooError.getWorstFirst(), unitField.validate().getWorstFirst());
    }

    @Test
    public void testNativeValidationBeforeClientValidation() {
        final IValidationResult fooError = ValidationResult.error("Foo");
        unitField.addValidator(new IValidator<Long>() {
            @Override
            public IValidationResult validate(final Long value) {
                return fooError;
            }
        });
        unitField.setText("1,23,45678911234567");

        final IValidationMessage worstFirst = unitField.validate().getWorstFirst();
        Assert.assertEquals(MessageType.ERROR, worstFirst.getType());
        Assert.assertNotSame(fooError.getWorstFirst(), worstFirst);
    }

    @Test
    public void testUnitValidationErrorBeforeClientValidationError() {
        final IValidationResult fooError = ValidationResult.error("Foo");
        unitField.addValidator(new IValidator<Long>() {
            @Override
            public IValidationResult validate(final Long value) {
                return fooError;
            }
        });
        unitField.setUnit(HertzUnitSet.H);
        unitField.setText("10000000000000000000");

        final IValidationMessage worstFirst = unitField.validate().getWorstFirst();
        Assert.assertEquals(MessageType.ERROR, worstFirst.getType());
        Assert.assertNotSame(fooError.getWorstFirst(), worstFirst);
    }

    @Test
    public void testUnitValidationWarningBeforeClientValidationWarning() {
        final IValidationResult fooWarning = ValidationResult.warning("Foo");
        unitField.addValidator(new IValidator<Long>() {
            @Override
            public IValidationResult validate(final Long value) {
                return fooWarning;
            }
        });
        unitField.setUnit(HertzUnitSet.H);
        unitField.setText("1,25");

        final IValidationMessage worstFirst = unitField.validate().getWorstFirst();
        Assert.assertEquals(MessageType.WARNING, worstFirst.getType());
        Assert.assertNotSame(fooWarning.getWorstFirst(), worstFirst);
    }

    @Test
    public void testUnitValidationWarningAfterClientValidationError() {
        final IValidationResult fooError = ValidationResult.error("Foo");
        unitField.addValidator(new IValidator<Long>() {
            @Override
            public IValidationResult validate(final Long value) {
                return fooError;
            }
        });
        unitField.setUnit(HertzUnitSet.H);
        unitField.setText("1,25");

        final IValidationMessage worstFirst = unitField.validate().getWorstFirst();
        Assert.assertEquals(MessageType.ERROR, worstFirst.getType());
        Assert.assertSame(fooError.getWorstFirst(), worstFirst);
    }

    @Test
    public void testNativeValidationWarningAfterClientError() {
        final IValidationResult fooError = ValidationResult.error("Foo");
        unitField.addValidator(new IValidator<Long>() {
            @Override
            public IValidationResult validate(final Long value) {
                return fooError;
            }
        });
        unitField.setText("1,2345678911234567");

        final IValidationResult validationResult = unitField.validate();
        final IValidationMessage worstFirst = unitField.validate().getWorstFirst();
        Assert.assertEquals(MessageType.ERROR, worstFirst.getType());
        Assert.assertSame(fooError.getWorstFirst(), worstFirst);
        Assert.assertEquals(1, validationResult.getWarnings().size());
    }

}
