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

package org.jowidgets.unit.tools.converter;

import java.math.BigDecimal;

import org.jowidgets.unit.api.IUnitValue;
import org.jowidgets.unit.tools.StaticUnitProvider;
import org.jowidgets.unit.tools.UnitValue;
import org.jowidgets.unit.tools.units.HertzUnitSet;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.MessageType;
import org.junit.Test;

import junit.framework.Assert;

public class LongDoubleUnitConverterTest {

    private static final BigDecimal NUMBER_10 = BigDecimal.valueOf(10);

    @Test
    public void testLongDoubleUnitConverter() {
        final LongDoubleUnitConverter converter = new LongDoubleUnitConverter(HertzUnitSet.GH);
        final UnitValue<Double> unitValue = new UnitValue<Double>(1.005d, HertzUnitSet.GH);

        final Long baseValueOfUnitValue = converter.toBaseValue(unitValue);
        Assert.assertEquals(1005000000, baseValueOfUnitValue.longValue());

        final IUnitValue<Double> unitValueConvertedBack = converter.toUnitValue(baseValueOfUnitValue);
        Assert.assertEquals(unitValue, unitValueConvertedBack);
    }

    @Test
    public void testDoubleUnitConverter() {
        final DoubleUnitConverter converter = new DoubleUnitConverter(HertzUnitSet.GH);
        final UnitValue<Double> unitValue = new UnitValue<Double>(1.005d, HertzUnitSet.GH);
        final Double baseValueOfUnitValue = converter.toBaseValue(unitValue);
        final IUnitValue<Double> unitValueConvertedBack = converter.toUnitValue(baseValueOfUnitValue);
        Assert.assertEquals(unitValue, unitValueConvertedBack);
    }

    @Test
    public void testConvertGigaHerz() {
        final LongDoubleUnitConverter converter = new LongDoubleUnitConverter(HertzUnitSet.GH);
        final IValidator<IUnitValue<Double>> validator = converter.getValidator();
        long baseValue = 1L;
        BigDecimal unitValueDecimal = new BigDecimal("0.000000001");

        for (int i = 0; i < 14; i++) {
            final IUnitValue<Double> unitValue = new UnitValue<Double>(unitValueDecimal.doubleValue(), HertzUnitSet.GH);
            final Long baseValueLong = Long.valueOf(baseValue);

            Assert.assertEquals(unitValue, converter.toUnitValue(baseValueLong));
            Assert.assertEquals(baseValueLong, converter.toBaseValue(unitValue));
            Assert.assertTrue(validator.validate(unitValue).isOk());

            unitValueDecimal = unitValueDecimal.multiply(NUMBER_10);
            baseValue = baseValue * 10;
        }
    }

    @Test
    public void testConvertMegaHerz() {
        final LongDoubleUnitConverter converter = new LongDoubleUnitConverter(HertzUnitSet.MH);
        final IValidator<IUnitValue<Double>> validator = converter.getValidator();
        long baseValue = 1L;
        BigDecimal unitValueDecimal = new BigDecimal("0.000001");

        for (int i = 0; i < 14; i++) {
            final IUnitValue<Double> unitValue = new UnitValue<Double>(unitValueDecimal.doubleValue(), HertzUnitSet.MH);
            final Long baseValueLong = Long.valueOf(baseValue);

            Assert.assertEquals(unitValue, converter.toUnitValue(baseValueLong));
            Assert.assertEquals(baseValueLong, converter.toBaseValue(unitValue));
            Assert.assertTrue(validator.validate(unitValue).isOk());

            unitValueDecimal = unitValueDecimal.multiply(NUMBER_10);
            baseValue = baseValue * 10;
        }
    }

    @Test
    public void testConvertKiloHerz() {
        final LongDoubleUnitConverter converter = new LongDoubleUnitConverter(HertzUnitSet.KH);
        final IValidator<IUnitValue<Double>> validator = converter.getValidator();
        long baseValue = 1L;
        BigDecimal unitValueDecimal = new BigDecimal("0.001");

        for (int i = 0; i < 14; i++) {
            final IUnitValue<Double> unitValue = new UnitValue<Double>(unitValueDecimal.doubleValue(), HertzUnitSet.KH);
            final Long baseValueLong = Long.valueOf(baseValue);

            Assert.assertEquals(unitValue, converter.toUnitValue(baseValueLong));
            Assert.assertEquals(baseValueLong, converter.toBaseValue(unitValue));
            Assert.assertTrue(validator.validate(unitValue).isOk());

            unitValueDecimal = unitValueDecimal.multiply(NUMBER_10);
            baseValue = baseValue * 10;
        }
    }

    @Test
    public void testConvertHerz() {
        final LongDoubleUnitConverter converter = new LongDoubleUnitConverter(HertzUnitSet.H);
        final IValidator<IUnitValue<Double>> validator = converter.getValidator();
        long baseValue = 1L;
        BigDecimal unitValueDecimal = new BigDecimal("1.0");

        for (int i = 0; i < 14; i++) {
            final IUnitValue<Double> unitValue = new UnitValue<Double>(unitValueDecimal.doubleValue(), HertzUnitSet.H);
            final Long baseValueLong = Long.valueOf(baseValue);

            Assert.assertEquals(unitValue, converter.toUnitValue(baseValueLong));
            Assert.assertEquals(baseValueLong, converter.toBaseValue(unitValue));
            Assert.assertTrue(validator.validate(unitValue).isOk());

            unitValueDecimal = unitValueDecimal.multiply(NUMBER_10);
            baseValue = baseValue * 10;
        }
    }

    @Test
    public void testValidationWarnings() {

        final LongDoubleUnitConverter converter = new LongDoubleUnitConverter(HertzUnitSet.MH);
        final IValidator<IUnitValue<Double>> validator = converter.getValidator();

        IUnitValue<Double> unitValue = new UnitValue<Double>(1.0, HertzUnitSet.H);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(1), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(1.2, HertzUnitSet.H);
        Assert.assertEquals(MessageType.WARNING, validator.validate(unitValue).getWorstFirst().getType());
        Assert.assertEquals(Long.valueOf(1), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(1.2, HertzUnitSet.KH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(1200), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(1.23, HertzUnitSet.KH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(1230), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(1.234, HertzUnitSet.KH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(1234), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(1.2345, HertzUnitSet.KH);
        Assert.assertEquals(MessageType.WARNING, validator.validate(unitValue).getWorstFirst().getType());
        Assert.assertEquals(Long.valueOf(1234), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(1.2345, HertzUnitSet.MH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(1234500), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(1.23456, HertzUnitSet.MH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(1234560), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(1.234567, HertzUnitSet.MH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(1234567), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(1.2345678, HertzUnitSet.MH);
        Assert.assertEquals(MessageType.WARNING, validator.validate(unitValue).getWorstFirst().getType());
        Assert.assertEquals(Long.valueOf(1234567), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(1.2345678, HertzUnitSet.GH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(1234567800), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(1.23456789, HertzUnitSet.GH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(1234567890), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(1.234567891, HertzUnitSet.GH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(1234567891), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(1.2345678911, HertzUnitSet.GH);
        Assert.assertEquals(MessageType.WARNING, validator.validate(unitValue).getWorstFirst().getType());
        Assert.assertEquals(Long.valueOf(1234567891), converter.toBaseValue(unitValue));
    }

    @Test
    public void testValidationErrors() {

        final LongDoubleUnitConverter converter = new LongDoubleUnitConverter(HertzUnitSet.MH);
        final IValidator<IUnitValue<Double>> validator = converter.getValidator();

        IUnitValue<Double> unitValue = new UnitValue<Double>(Double.valueOf(9223000000000000000d), HertzUnitSet.H);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(9223000000000000000L), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(-9223000000000000000d), HertzUnitSet.H);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(-9223000000000000000L), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(9300000000000000000d), HertzUnitSet.H);
        Assert.assertEquals(MessageType.ERROR, validator.validate(unitValue).getWorstFirst().getType());
        Assert.assertNull(converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(-9300000000000000000d), HertzUnitSet.H);
        Assert.assertEquals(MessageType.ERROR, validator.validate(unitValue).getWorstFirst().getType());
        Assert.assertNull(converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(9223000000000000d), HertzUnitSet.KH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(9223000000000000000L), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(-9223000000000000d), HertzUnitSet.KH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(-9223000000000000000L), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(9300000000000000d), HertzUnitSet.KH);
        Assert.assertEquals(MessageType.ERROR, validator.validate(unitValue).getWorstFirst().getType());
        Assert.assertNull(converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(-9300000000000000d), HertzUnitSet.KH);
        Assert.assertEquals(MessageType.ERROR, validator.validate(unitValue).getWorstFirst().getType());
        Assert.assertNull(converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(9223000000000d), HertzUnitSet.MH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(9223000000000000000L), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(-9223000000000d), HertzUnitSet.MH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(-9223000000000000000L), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(9300000000000d), HertzUnitSet.MH);
        Assert.assertEquals(MessageType.ERROR, validator.validate(unitValue).getWorstFirst().getType());
        Assert.assertNull(converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(-9300000000000d), HertzUnitSet.MH);
        Assert.assertEquals(MessageType.ERROR, validator.validate(unitValue).getWorstFirst().getType());
        Assert.assertNull(converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(9223000000d), HertzUnitSet.GH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(9223000000000000000L), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(-9223000000d), HertzUnitSet.GH);
        Assert.assertTrue(validator.validate(unitValue).isOk());
        Assert.assertEquals(Long.valueOf(-9223000000000000000L), converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(9300000000d), HertzUnitSet.GH);
        Assert.assertEquals(MessageType.ERROR, validator.validate(unitValue).getWorstFirst().getType());
        Assert.assertNull(converter.toBaseValue(unitValue));

        unitValue = new UnitValue<Double>(Double.valueOf(-9300000000d), HertzUnitSet.GH);
        Assert.assertEquals(MessageType.ERROR, validator.validate(unitValue).getWorstFirst().getType());
        Assert.assertNull(converter.toBaseValue(unitValue));

    }

    @Test
    public void testUnconvertibeSubstitude() {

        final Long unconvertibeSubstitude = Long.valueOf(-1L);

        final LongDoubleUnitConverter converter = new LongDoubleUnitConverter(
            new StaticUnitProvider<Long>(HertzUnitSet.H),
            unconvertibeSubstitude);
        final IValidator<IUnitValue<Double>> validator = converter.getValidator();

        final IUnitValue<Double> unitValue = new UnitValue<Double>(Double.valueOf(-9300000000d), HertzUnitSet.GH);
        Assert.assertEquals(MessageType.ERROR, validator.validate(unitValue).getWorstFirst().getType());
        final Long baseValue = converter.toBaseValue(unitValue);
        Assert.assertNotNull(baseValue);
        Assert.assertEquals(unconvertibeSubstitude, baseValue);

    }

}
