/*
 * Copyright (c) 2010, Michael Grossmann, Nikolaus Moll
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
package org.jowidgets.impl.convert;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IConverterProvider;
import org.jowidgets.api.convert.IObjectLabelConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.convert.IStringObjectConverter;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.tools.converter.AbstractObjectLabelConverter;
import org.jowidgets.tools.converter.Converter;
import org.jowidgets.unit.api.IUnit;
import org.jowidgets.unit.api.IUnitConverter;
import org.jowidgets.util.Assert;

public final class DefaultConverterProvider implements IConverterProvider {

    private static final Locale DEFAULT_LOCALE = Locale.US;

    private static final IMessage MUST_BE_A_FLOATING_POINT_NUMBER = Messages
            .getMessage("DefaultConverterProvider.MustBeFloatingPointNumber");

    private final IObjectStringConverter<Object> toStringConverter;
    private final IObjectStringConverter<String> passwordPresentationConverter;
    private final IObjectStringConverter<IUnit> unitConverter;
    private final Map<Locale, Map<Class<?>, IConverter<?>>> converters;

    private Map<Locale, IConverter<Date>> defaultDateConverter;
    private Map<Locale, IConverter<Date>> defaultTimeConverter;
    private Map<Locale, IConverter<Date>> defaultDateTimeConverter;
    private Map<Locale, IConverter<Boolean>> booleanLongConverters;
    private Map<Locale, IConverter<Boolean>> booleanShortConverters;

    public DefaultConverterProvider() {
        toStringConverter = new DefaultObjectStringConverter();
        this.passwordPresentationConverter = new PasswordPresentationConverter();
        this.unitConverter = new UnitConverter();
        converters = new HashMap<Locale, Map<Class<?>, IConverter<?>>>();
        register(String.class, new DefaultStringConverter());

        final IConverter<Long> longNumber = new DefaultLongConverter();
        register(Long.class, longNumber);
        register(long.class, longNumber);

        final IConverter<Integer> integerNumber = new DefaultIntegerConverter();
        register(Integer.class, integerNumber);
        register(int.class, integerNumber);

        final IConverter<Short> shortNumber = new DefaultShortConverter();
        register(Short.class, shortNumber);
        register(short.class, shortNumber);

        final IConverter<Boolean> booleanLong = new DefaultYesNoConverterLong();
        register(Boolean.class, booleanLong);
        register(boolean.class, booleanLong);

        final DecimalFormat decimalFormatUsDouble = createDecimalFormatForLocale(
                Locale.US,
                DOUBLE_MIN_FRACTION_DIGITS_DEFAULT,
                DOUBLE_MAX_FRACTION_DIGITS_DEFAULT);

        final IConverter<Double> doubleNumberUS = new DefaultDoubleConverter(
            decimalFormatUsDouble,
            "Must be a valid floating point number");
        register(Double.class, doubleNumberUS);
        register(double.class, doubleNumberUS);

        final DecimalFormat decimalFormatUsFloat = createDecimalFormatForLocale(
                Locale.US,
                FLOAT_MIN_FRACTION_DIGITS_DEFAULT,
                FLOAT_MAX_FRACTION_DIGITS_DEFAULT);

        final IConverter<Float> floatNumberUS = new DefaultFloatConverter(
            decimalFormatUsFloat,
            "Must be a valid floating point number");
        register(Float.class, floatNumberUS);
        register(float.class, floatNumberUS);

        final DecimalFormat decimalFormatDeDouble = createDecimalFormatForLocale(
                Locale.GERMAN,
                DOUBLE_MIN_FRACTION_DIGITS_DEFAULT,
                DOUBLE_MAX_FRACTION_DIGITS_DEFAULT);

        final IConverter<Double> doubleNumberDE = new DefaultDoubleConverter(
            decimalFormatDeDouble,
            "Muss eine gültige Kommazahl sein");
        register(Locale.GERMANY, Double.class, doubleNumberDE);
        register(Locale.GERMANY, double.class, doubleNumberDE);
        register(Locale.GERMAN, Double.class, doubleNumberDE);
        register(Locale.GERMAN, double.class, doubleNumberDE);

        final DecimalFormat decimalFormatDeFloat = createDecimalFormatForLocale(
                Locale.GERMAN,
                FLOAT_MIN_FRACTION_DIGITS_DEFAULT,
                FLOAT_MAX_FRACTION_DIGITS_DEFAULT);

        final IConverter<Float> floatNumberDE = new DefaultFloatConverter(
            decimalFormatDeFloat,
            "Muss eine gültige Kommazahl sein");
        register(Locale.GERMANY, Float.class, floatNumberDE);
        register(Locale.GERMANY, float.class, floatNumberDE);
        register(Locale.GERMAN, Float.class, floatNumberDE);
        register(Locale.GERMAN, float.class, floatNumberDE);
    }

    private DecimalFormat createDecimalFormatForLocale(final Locale locale, final int minDigits, final int maxDigits) {
        final DecimalFormat formatOfLocale = (DecimalFormat) DecimalFormat.getInstance(locale);
        return createDecimalFormatCloneWithFractions(formatOfLocale, minDigits, maxDigits);
    }

    private DecimalFormat createDecimalFormatCloneWithFractions(
        final DecimalFormat decimalFormat,
        final int minDigits,
        final int maxDigits) {
        Assert.getParamNotNull(decimalFormat, "decimalFormat");
        final DecimalFormat result = (DecimalFormat) decimalFormat.clone();
        result.setMinimumFractionDigits(minDigits);
        result.setMaximumFractionDigits(maxDigits);
        return result;
    }

    @Override
    public <OBJECT_TYPE> void register(
        final Locale locale,
        final Class<? extends OBJECT_TYPE> type,
        final IConverter<OBJECT_TYPE> converter) {
        final Map<Class<?>, IConverter<?>> map;
        if (converters.containsKey(locale)) {
            map = converters.get(locale);
        }
        else {
            map = new HashMap<Class<?>, IConverter<?>>();
            converters.put(locale, map);
        }

        map.put(type, converter);
    }

    @Override
    public <OBJECT_TYPE> void register(final Class<? extends OBJECT_TYPE> type, final IConverter<OBJECT_TYPE> converter) {
        // remove type from all locales and register to default locale 
        for (final Entry<Locale, Map<Class<?>, IConverter<?>>> entry : converters.entrySet()) {
            if (entry.getValue().containsKey(type)) {
                entry.getValue().remove(type);
            }
        }

        register(DEFAULT_LOCALE, type, converter);
    }

    @Override
    public void registerDefaultDateConverter(final Locale locale, final IConverter<Date> converter) {
        Assert.paramNotNull(locale, "locale");
        if (converter != null) {
            getDefaultDateConverters().put(locale, converter);
        }
        else {
            getDefaultDateConverters().remove(locale);
        }
    }

    @Override
    public void registerDefaultTimeConverter(final Locale locale, final IConverter<Date> converter) {
        Assert.paramNotNull(locale, "locale");
        if (converter != null) {
            getDefaultTimeConverters().put(locale, converter);
        }
        else {
            getDefaultTimeConverters().remove(locale);
        }
    }

    @Override
    public void registerDefaultDateTimeConverter(final Locale locale, final IConverter<Date> converter) {
        Assert.paramNotNull(locale, "locale");
        if (converter != null) {
            getDefaultDateTimeConverters().put(locale, converter);
        }
        else {
            getDefaultDateTimeConverters().remove(locale);
        }
    }

    @Override
    public void registerDefaultBooleanLongConverter(final Locale locale, final IConverter<Boolean> converter) {
        Assert.paramNotNull(locale, "locale");
        if (converter != null) {
            getDefaultBooleanLongConverters().put(locale, converter);
        }
        else {
            getDefaultBooleanLongConverters().remove(locale);
        }
    }

    @Override
    public void registerDefaultBooleanShortConverter(final Locale locale, final IConverter<Boolean> converter) {
        Assert.paramNotNull(locale, "locale");
        if (converter != null) {
            getDefaultBooleanShortConverters().put(locale, converter);
        }
        else {
            getDefaultBooleanShortConverters().remove(locale);
        }
    }

    private Map<Locale, IConverter<Boolean>> getDefaultBooleanLongConverters() {
        if (booleanLongConverters == null) {
            booleanLongConverters = new HashMap<Locale, IConverter<Boolean>>();
            final IConverter<Boolean> booleanLong = new DefaultYesNoConverterLong();
            booleanLongConverters.put(DEFAULT_LOCALE, booleanLong);
            booleanLongConverters.put(DEFAULT_LOCALE, booleanLong);
        }
        return booleanLongConverters;
    }

    private Map<Locale, IConverter<Boolean>> getDefaultBooleanShortConverters() {
        if (booleanShortConverters == null) {
            booleanShortConverters = new HashMap<Locale, IConverter<Boolean>>();
            final IConverter<Boolean> booleanYesNoShort = new DefaultYesNoConverterShort();
            booleanShortConverters.put(DEFAULT_LOCALE, booleanYesNoShort);
            booleanShortConverters.put(DEFAULT_LOCALE, booleanYesNoShort);
        }
        return booleanShortConverters;
    }

    @Override
    public <OBJECT_TYPE> IConverter<OBJECT_TYPE> mapConverter(
        final Map<? extends OBJECT_TYPE, String> objectToString,
        final Map<String, ? extends OBJECT_TYPE> stringToObject,
        final String hint) {
        Assert.paramNotNull(objectToString, "objectToString");
        Assert.paramNotNull(stringToObject, "stringToObject");
        return new Converter<OBJECT_TYPE>(
            new ObjectStringMapConverter<OBJECT_TYPE>(objectToString),
            new StringObjectMapConverter<OBJECT_TYPE>(stringToObject, hint));
    }

    @Override
    public <OBJECT_TYPE> IObjectStringConverter<OBJECT_TYPE> mapConverter(final Map<OBJECT_TYPE, String> objectToString) {
        Assert.paramNotNull(objectToString, "objectToString");
        return new ObjectStringMapConverter<OBJECT_TYPE>(objectToString);
    }

    @Override
    public <OBJECT_TYPE> IConverter<OBJECT_TYPE> getConverter(final Class<? extends OBJECT_TYPE> type) {
        IConverter<OBJECT_TYPE> result = getConverterFromLocale(Locale.getDefault(), type);
        if (result == null) {
            result = getConverterFromLocale(DEFAULT_LOCALE, type);
        }
        if (result == null && type.isEnum()) {
            return getEnumConverter(type);
        }
        return result;
    }

    private <OBJECT_TYPE> IConverter<OBJECT_TYPE> getEnumConverter(final Class<? extends OBJECT_TYPE> type) {
        final Map<OBJECT_TYPE, String> objectToString = new HashMap<OBJECT_TYPE, String>();
        final Map<String, OBJECT_TYPE> stringToObject = new HashMap<String, OBJECT_TYPE>();
        for (final OBJECT_TYPE constant : type.getEnumConstants()) {
            final String tanga = constant.toString();
            objectToString.put(constant, tanga);
            stringToObject.put(tanga, constant);
            stringToObject.put(tanga.toLowerCase(), constant);
            stringToObject.put(tanga.toUpperCase(), constant);
        }
        return new Converter<OBJECT_TYPE>(
            new ObjectStringMapConverter<OBJECT_TYPE>(objectToString),
            new StringObjectMapConverter<OBJECT_TYPE>(stringToObject, null));
    }

    @SuppressWarnings("unchecked")
    public <OBJECT_TYPE> IConverter<OBJECT_TYPE> getConverterFromLocale(
        final Locale locale,
        final Class<? extends OBJECT_TYPE> type) {
        IConverter<OBJECT_TYPE> result = null;
        if (converters.containsKey(locale)) {
            final Map<Class<?>, IConverter<?>> map = converters.get(locale);
            if (map.containsKey(type)) {
                result = (IConverter<OBJECT_TYPE>) map.get(type);
            }
            if (result == null) {
                if (type == Date.class) {
                    return (IConverter<OBJECT_TYPE>) date();
                }
                if (type == Boolean.class) {
                    return (IConverter<OBJECT_TYPE>) boolLong();
                }
            }

        }

        return result;
    }

    @Override
    public <OBJECT_TYPE> IObjectStringConverter<OBJECT_TYPE> getObjectStringConverter(final Class<? extends OBJECT_TYPE> type) {
        final IConverter<OBJECT_TYPE> converter = getConverter(type);

        if (converter != null) {
            return converter;
        }
        else {
            return toStringConverter();
        }
    }

    @Override
    public <OBJECT_TYPE> IObjectLabelConverter<OBJECT_TYPE> getObjectLabelConverter(final Class<? extends OBJECT_TYPE> type) {
        final IObjectStringConverter<OBJECT_TYPE> objStringConverter = getObjectStringConverter(type);

        return new AbstractObjectLabelConverter<OBJECT_TYPE>() {

            @Override
            public String convertToString(final OBJECT_TYPE value) {
                return objStringConverter.convertToString(value);
            }

            @Override
            public String getDescription(final OBJECT_TYPE value) {
                return objStringConverter.getDescription(value);
            }

        };
    }

    @Override
    public <OBJECT_TYPE> IStringObjectConverter<OBJECT_TYPE> getStringObjectConverter(final Class<? extends OBJECT_TYPE> type) {
        return getConverter(type);
    }

    @Override
    public IConverter<String> string() {
        return getConverter(String.class);
    }

    @Override
    public IConverter<Long> longNumber() {
        return getConverter(Long.class);
    }

    @Override
    public IConverter<Integer> integerNumber() {
        return getConverter(Integer.class);
    }

    @Override
    public IConverter<Short> shortNumber() {
        return getConverter(Short.class);
    }

    @Override
    public IConverter<Double> doubleNumber() {
        return getConverter(Double.class);
    }

    @Override
    public IConverter<Double> doubleNumber(final DecimalFormat decimalFormat, final String formatHint) {
        return new DefaultDoubleConverter(decimalFormat, formatHint);
    }

    @Override
    public IConverter<Double> doubleNumber(final int minFractionDigits, final int maxFractionDigits) {
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumFractionDigits(minFractionDigits);
        decimalFormat.setMaximumFractionDigits(maxFractionDigits);
        return doubleNumber(minFractionDigits, maxFractionDigits, WARN_ON_ROUNDING_DEFAULT);
    }

    @Override
    public IConverter<Double> doubleNumber(
        final DecimalFormat decimalFormat,
        final String formatHint,
        final boolean warnOnRounding) {
        return new DefaultDoubleConverter(decimalFormat, formatHint, warnOnRounding);
    }

    @Override
    public IConverter<Double> doubleNumber(
        final int minFractionDigits,
        final int maxFractionDigits,
        final boolean warnOnRounding) {
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumFractionDigits(minFractionDigits);
        decimalFormat.setMaximumFractionDigits(maxFractionDigits);
        return new DefaultDoubleConverter(decimalFormat, MUST_BE_A_FLOATING_POINT_NUMBER.get(), warnOnRounding);
    }

    @Override
    public IConverter<Double> doubleNumber(final Locale locale, final int minFractionDigits, final int maxFractionDigits) {
        return doubleNumber(locale, minFractionDigits, maxFractionDigits, WARN_ON_ROUNDING_DEFAULT);
    }

    @Override
    public IConverter<Double> doubleNumber(
        final Locale locale,
        final int minFractionDigits,
        final int maxFractionDigits,
        final boolean warnOnRounding) {
        final DecimalFormat decimalFormat = createDecimalFormatForLocale(locale, minFractionDigits, maxFractionDigits);
        return new DefaultDoubleConverter(decimalFormat, MUST_BE_A_FLOATING_POINT_NUMBER.get(), warnOnRounding);
    }

    @Override
    public IConverter<Boolean> boolLong() {
        IConverter<Boolean> converter = getDefaultBooleanLongConverters().get(Locale.getDefault());
        if (converter == null) {
            converter = getDefaultBooleanLongConverters().get(DEFAULT_LOCALE);
        }
        return converter;
    }

    @Override
    public IConverter<Boolean> boolShort() {
        IConverter<Boolean> converter = getDefaultBooleanShortConverters().get(Locale.getDefault());
        if (converter == null) {
            converter = getDefaultBooleanShortConverters().get(DEFAULT_LOCALE);
        }
        return converter;
    }

    @Override
    public IConverter<Date> date(final DateFormat dateFormat, final String formatHint, final ITextMask textMask) {
        return new DefaultDateConverter(dateFormat, textMask, formatHint);
    }

    @Override
    public IConverter<Date> date(final DateFormat dateFormat, final String formatHint) {
        return new DefaultDateConverter(dateFormat, null, formatHint);
    }

    @Override
    public IConverter<Date> date() {
        IConverter<Date> converter = getDefaultDateConverters().get(Locale.getDefault());
        if (converter == null) {
            converter = getDefaultDateConverters().get(DEFAULT_LOCALE);
        }
        return converter;
    }

    @Override
    public IConverter<Date> time() {
        IConverter<Date> converter = getDefaultTimeConverters().get(Locale.getDefault());
        if (converter == null) {
            converter = getDefaultTimeConverters().get(DEFAULT_LOCALE);
        }
        return converter;
    }

    @Override
    public IConverter<Date> dateTime() {
        IConverter<Date> converter = getDefaultDateTimeConverters().get(Locale.getDefault());
        if (converter == null) {
            converter = getDefaultDateTimeConverters().get(DEFAULT_LOCALE);
        }
        return converter;
    }

    @Override
    public <BASE_VALUE_TYPE, UNIT_VALUE_TYPE> IObjectStringConverter<BASE_VALUE_TYPE> unitValueConverter(
        final IUnitConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> unitConverter,
        final Class<? extends UNIT_VALUE_TYPE> unitValueType) {
        Assert.paramNotNull(unitConverter, "unitConverter");
        Assert.paramNotNull(unitValueType, "unitValueType");
        return unitValueConverter(unitConverter, getObjectStringConverter(unitValueType));
    }

    @Override
    public <BASE_VALUE_TYPE, UNIT_VALUE_TYPE> IObjectStringConverter<BASE_VALUE_TYPE> unitValueConverter(
        final IUnitConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE> unitConverter,
        final IObjectStringConverter<UNIT_VALUE_TYPE> unitValueConverter) {
        Assert.paramNotNull(unitConverter, "unitConverter");
        Assert.paramNotNull(unitValueConverter, "unitValueConverter");

        return new UnitObjectStringConverter<BASE_VALUE_TYPE, UNIT_VALUE_TYPE>(unitConverter, unitValueConverter);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <OBJECT_TYPE> IObjectStringConverter<OBJECT_TYPE> toStringConverter() {
        return (IObjectStringConverter<OBJECT_TYPE>) toStringConverter;
    }

    @Override
    public IObjectStringConverter<String> passwordPresentationConverter() {
        return passwordPresentationConverter;
    }

    @Override
    public IObjectStringConverter<IUnit> unitConverter() {
        return unitConverter;
    }

    private Map<Locale, IConverter<Date>> getDefaultDateConverters() {
        if (defaultDateConverter == null) {
            this.defaultDateConverter = new HashMap<Locale, IConverter<Date>>();

            final DateFormat dateFormatUS = new SimpleDateFormat("MM/dd/yyyy");
            dateFormatUS.setLenient(false);
            defaultDateConverter.put(DEFAULT_LOCALE, date(dateFormatUS, "MM/DD/YYYY", TextMaskProvider.dateMaskUS()));

            final DateFormat dateFormatDE = new SimpleDateFormat("dd-MM-yyyy");
            dateFormatDE.setLenient(false);
            final IConverter<Date> converterDE = date(dateFormatDE, "DD-MM-YYYY", TextMaskProvider.dateMaskDE());

            defaultDateConverter.put(Locale.GERMANY, converterDE);
            defaultDateConverter.put(Locale.GERMAN, converterDE);
        }
        return defaultDateConverter;
    }

    private Map<Locale, IConverter<Date>> getDefaultTimeConverters() {
        if (defaultTimeConverter == null) {
            this.defaultTimeConverter = new HashMap<Locale, IConverter<Date>>();

            final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            dateFormat.setLenient(false);
            final IConverter<Date> converter = date(dateFormat, "HH:MM:SS", TextMaskProvider.timeMask());

            defaultTimeConverter.put(DEFAULT_LOCALE, converter);
            defaultTimeConverter.put(Locale.GERMAN, converter);
            defaultTimeConverter.put(Locale.GERMANY, converter);
        }
        return defaultTimeConverter;
    }

    private Map<Locale, IConverter<Date>> getDefaultDateTimeConverters() {
        if (defaultDateTimeConverter == null) {
            this.defaultDateTimeConverter = new HashMap<Locale, IConverter<Date>>();

            final DateFormat dateFormatUS = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            dateFormatUS.setLenient(false);
            defaultDateTimeConverter
                    .put(DEFAULT_LOCALE, date(dateFormatUS, "MM/DD/YYYY HH:MM:SS", TextMaskProvider.dateTimeMaskUS()));

            final DateFormat dateFormatDE = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            dateFormatDE.setLenient(false);
            final IConverter<Date> germanConverter = date(dateFormatDE, "DD-MM-YYYY HH:MM:SS", TextMaskProvider.dateTimeMaskDE());

            defaultDateTimeConverter.put(Locale.GERMAN, germanConverter);
            defaultDateTimeConverter.put(Locale.GERMANY, germanConverter);
        }
        return defaultDateTimeConverter;
    }
}
