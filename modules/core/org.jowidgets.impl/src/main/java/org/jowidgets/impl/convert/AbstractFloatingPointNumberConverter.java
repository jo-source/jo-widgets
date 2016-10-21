/*
 * Copyright (c) 2011, Nikolaus Moll
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

import java.text.DecimalFormat;
import java.text.ParsePosition;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.tools.converter.AbstractConverter;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.StringUtils;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

abstract class AbstractFloatingPointNumberConverter<NUMBER_TYPE extends Number> extends AbstractConverter<NUMBER_TYPE>
        implements IConverter<NUMBER_TYPE> {

    private static final IMessage MUST_NOT_CONTAIN_MULTIPLE_SEPARATORS = Messages
            .getMessage("AbstractFloatingPointNumberConverter.must_not_contain_multiple_separators");

    private static final IMessage MUST_BE_A_VALID_DECIMAL_NUMBER = Messages
            .getMessage("AbstractFloatingPointNumberConverter.must_be_a_valid_decimal_number");

    private final DecimalFormat decimalFormat;
    private final String formatHint;
    private final String acceptingRegExp;
    private final String decimalSeparatorRegEx;
    private final String groupingSeparatorRegEx;
    private final IValidator<String> stringValidator;

    AbstractFloatingPointNumberConverter(final DecimalFormat decimalFormat, final String formatHint) {
        Assert.paramNotNull(decimalFormat, "decimalFormat");
        this.decimalFormat = decimalFormat;
        this.formatHint = formatHint;
        this.decimalSeparatorRegEx = getDecimalSeparatorRegEx(decimalFormat);
        this.groupingSeparatorRegEx = getGroupingSeparatorRegEx(decimalFormat);
        this.acceptingRegExp = createAcceptionRegEx(decimalFormat, decimalSeparatorRegEx, groupingSeparatorRegEx);
        this.stringValidator = new StringValidator();
    }

    private static String createAcceptionRegEx(
        final DecimalFormat decimalFormat,
        final String decimalSeparatorRegEx,
        final String groupingSeparatorRegEx) {
        return "-?([0-9]?" + groupingSeparatorRegEx + "?" + decimalSeparatorRegEx + "?E?)*";
    }

    private static String getDecimalSeparatorRegEx(final DecimalFormat decimalFormat) {
        // Check, if the decimal separator is a special Char, add \\ to the char
        final char decimalSeparator = decimalFormat.getDecimalFormatSymbols().getDecimalSeparator();
        if (isSpecialChar(decimalSeparator)) {
            return "\\" + decimalSeparator;
        }
        else {
            return String.valueOf(decimalSeparator);
        }
    }

    private static String getGroupingSeparatorRegEx(final DecimalFormat decimalFormat) {
        // Check, if the grouping separator is a special Char, add \\ to the char
        final char groupingSeparator = decimalFormat.getDecimalFormatSymbols().getGroupingSeparator();
        if (isSpecialChar(groupingSeparator)) {
            return "\\" + groupingSeparator;
        }
        else {
            return String.valueOf(groupingSeparator);
        }
    }

    private static boolean isSpecialChar(final char c) {
        // 160 = ' ' (france) ( alt 255) , 46 = '.' 
        if (c == 160 | c == 46) {
            return true;
        }
        return false;
    }

    abstract NUMBER_TYPE convert(Number number);

    @Override
    public NUMBER_TYPE convertToObject(final String string) {
        return convertToObjectWithoutGroupingSeparators(removeGroupingSeparators(string));
    }

    private NUMBER_TYPE convertToObjectWithoutGroupingSeparators(final String string) {
        try {
            final ParsePosition pos = new ParsePosition(0);
            final Number result = decimalFormat.parse(string, pos);
            if (result == null || pos.getIndex() < string.length() || pos.getErrorIndex() != -1) {
                return null;
            }
            return convert(result);
        }
        catch (final NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String convertToString(final NUMBER_TYPE value) {
        if (value != null) {
            return decimalFormat.format(value);
        }
        return null;
    }

    @Override
    public final IValidator<String> getStringValidator() {
        return stringValidator;
    }

    private String removeGroupingSeparators(final String tanga) {
        if (EmptyCheck.isEmpty(tanga)) {
            return tanga;
        }
        else {
            return tanga.replaceAll(groupingSeparatorRegEx, "");
        }
    }

    @Override
    public String getAcceptingRegExp() {
        return acceptingRegExp;
    }

    private final class StringValidator implements IValidator<String> {
        @Override
        public IValidationResult validate(final String input) {
            return validateWithoutGroupingSeparators(removeGroupingSeparators(input));
        }

        private IValidationResult validateWithoutGroupingSeparators(final String input) {
            if (input != null && !input.trim().isEmpty()) {
                if (StringUtils.countMatches(input, decimalSeparatorRegEx) > 1) {
                    return ValidationResult.error(MUST_NOT_CONTAIN_MULTIPLE_SEPARATORS.get());
                }
                else if (hasParseError(input)) {
                    if (formatHint != null) {
                        return ValidationResult.error(formatHint);
                    }
                    else {
                        return ValidationResult.error(MUST_BE_A_VALID_DECIMAL_NUMBER.get());
                    }
                }
            }
            return ValidationResult.ok();
        }

        private boolean hasParseError(final String tanga) {
            final ParsePosition pos = new ParsePosition(0);
            final Number number = decimalFormat.parse(tanga, pos);
            return number == null || pos.getIndex() < tanga.length() || pos.getErrorIndex() != -1;
        }

    }

}
