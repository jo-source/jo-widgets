/*
 * Copyright (c) 2016, grossmann
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

package org.jowidgets.impl.convert;

import java.util.Locale;

import org.jowidgets.api.convert.IConverter;
import org.junit.Test;

import junit.framework.Assert;

public class FloatingNumberConverterTest {

    public static final DefaultConverterProvider CONVERTER_PROVIDER = new DefaultConverterProvider();

    @Test
    public void testDoubleNumberConverterGerman() {
        final IConverter<Double> converter = CONVERTER_PROVIDER.getConverterFromLocale(Locale.GERMANY, Double.class);
        Assert.assertEquals(13.1834d, converter.convertToObject("13,1834"));
        Assert.assertEquals(-13.1834d, converter.convertToObject("-13,1834"));
        Assert.assertEquals(32234234.1834d, converter.convertToObject("32.234.234,1834"));
        Assert.assertEquals(null, converter.convertToObject("13,18,3"));
        Assert.assertEquals(0.18d, converter.convertToObject(",18"));
        Assert.assertEquals(0.1856d, converter.convertToObject("..,18..56"));
        Assert.assertEquals(1.23E123d, converter.convertToObject("1,23E123"));
        Assert.assertEquals(1.223E123d, converter.convertToObject("12,23E122"));
        Assert.assertEquals(null, converter.convertToObject("1,23E"));
    }

    @Test
    public void testDoubleNumberConverterUS() {
        final IConverter<Double> converter = CONVERTER_PROVIDER.getConverterFromLocale(Locale.US, Double.class);
        Assert.assertEquals(13.1834d, converter.convertToObject("13.1834"));
        Assert.assertEquals(-13.1834d, converter.convertToObject("-13.1834"));
        Assert.assertEquals(32234234.1834d, converter.convertToObject("32,234,234.1834"));
        Assert.assertEquals(null, converter.convertToObject("13.18.3"));
        Assert.assertEquals(0.18d, converter.convertToObject(".18"));
        Assert.assertEquals(0.1856d, converter.convertToObject(",,.18,,56"));
        Assert.assertEquals(1.23E123d, converter.convertToObject("1.23E123"));
        Assert.assertEquals(1.223E123d, converter.convertToObject("12.23E122"));
        Assert.assertEquals(null, converter.convertToObject("1.23E"));
    }

    @Test
    public void testDoubleNumberConverterValidationGerman() {
        final IConverter<Double> converter = CONVERTER_PROVIDER.getConverterFromLocale(Locale.GERMANY, Double.class);
        Assert.assertTrue(converter.getStringValidator().validate("13,1834").isValid());
        Assert.assertTrue(converter.getStringValidator().validate("-13,1834").isValid());
        Assert.assertTrue(converter.getStringValidator().validate("32.234.234,1834").isValid());
        Assert.assertFalse(converter.getStringValidator().validate("13,18,3").isValid());
        Assert.assertTrue(converter.getStringValidator().validate(",18").isValid());
        Assert.assertTrue(converter.getStringValidator().validate("..,18..56").isValid());
        Assert.assertFalse(converter.getStringValidator().validate("1,23E").isValid());
    }

    @Test
    public void testDoubleNumberConverterValidationUS() {
        final IConverter<Double> converter = CONVERTER_PROVIDER.getConverterFromLocale(Locale.US, Double.class);
        Assert.assertTrue(converter.getStringValidator().validate("13.1834").isValid());
        Assert.assertTrue(converter.getStringValidator().validate("-13.1834").isValid());
        Assert.assertTrue(converter.getStringValidator().validate("32,234,234.1834").isValid());
        Assert.assertFalse(converter.getStringValidator().validate("13.18.3").isValid());
        Assert.assertTrue(converter.getStringValidator().validate(".18").isValid());
        Assert.assertTrue(converter.getStringValidator().validate(",,.18,,56").isValid());
        Assert.assertFalse(converter.getStringValidator().validate("1.23E").isValid());
    }

    @Test
    public void testDoubleNumberConverterVerifyGerman() {
        final IConverter<Double> converter = CONVERTER_PROVIDER.getConverterFromLocale(Locale.GERMANY, Double.class);
        final String acceptingRegExp = converter.getAcceptingRegExp();

        Assert.assertTrue("13,1834".matches(acceptingRegExp));
        Assert.assertTrue("-13,1834".matches(acceptingRegExp));
        Assert.assertTrue("32.234.234,1834".matches(acceptingRegExp));
        Assert.assertTrue("13,18,3".matches(acceptingRegExp));
        Assert.assertTrue(",18".matches(acceptingRegExp));
        Assert.assertTrue("..,18..56".matches(acceptingRegExp));
        Assert.assertTrue("1,23E123".matches(acceptingRegExp));

        Assert.assertFalse("1,23e123".matches(acceptingRegExp));
    }

    @Test
    public void testDoubleNumberConverterVerifyUS() {
        final IConverter<Double> converter = CONVERTER_PROVIDER.getConverterFromLocale(Locale.GERMANY, Double.class);
        final String acceptingRegExp = converter.getAcceptingRegExp();

        Assert.assertTrue("13.1834".matches(acceptingRegExp));
        Assert.assertTrue("-13.1834".matches(acceptingRegExp));
        Assert.assertTrue("32,234,234.1834".matches(acceptingRegExp));
        Assert.assertTrue("13.18.3".matches(acceptingRegExp));
        Assert.assertTrue(".18".matches(acceptingRegExp));
        Assert.assertTrue(",,.18,,56".matches(acceptingRegExp));
        Assert.assertTrue("1.23E123".matches(acceptingRegExp));

        Assert.assertFalse("1.23e123".matches(acceptingRegExp));
    }

}
