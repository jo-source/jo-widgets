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
package org.jowidgets.impl.convert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.i18n.api.MessageReplacer;
import org.jowidgets.tools.converter.AbstractConverter;
import org.jowidgets.util.Assert;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

final class DefaultDateConverter extends AbstractConverter<Date> implements IConverter<Date> {

    private final DateFormat dateFormat;
    private final ITextMask textMask;
    private final String formatHint;

    DefaultDateConverter(final DateFormat dateFormat, final ITextMask textMask, final String formatHint) {
        Assert.paramNotNull(dateFormat, "dateFormat");
        this.dateFormat = dateFormat;
        this.textMask = textMask;

        if (formatHint != null) {
            this.formatHint = formatHint;
        }
        else if (dateFormat instanceof SimpleDateFormat) {
            this.formatHint = ((SimpleDateFormat) dateFormat).toPattern();
        }
        else {
            this.formatHint = null;
        }
    }

    @Override
    public Date convertToObject(final String string) {
        if (string != null) {
            try {
                return dateFormat.parse(string);
            }
            catch (final ParseException e) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public String convertToString(final Date value) {
        if (value != null) {
            return dateFormat.format(value);
        }
        return "";
    }

    @Override
    public IValidator<String> getStringValidator() {
        return new IValidator<String>() {
            @Override
            public IValidationResult validate(final String input) {
                if (input != null && !input.trim().isEmpty() && (textMask == null || !textMask.getPlaceholder().equals(input))) {
                    try {
                        dateFormat.parse(input);
                    }
                    catch (final ParseException e) {
                        if (formatHint != null) {
                            return ValidationResult.error(MessageReplacer.replace("Must have the format '%1'", formatHint));
                        }
                        else {
                            return ValidationResult.error("Is not a valid date or time");
                        }
                    }
                }
                return ValidationResult.ok();
            }
        };
    }

    @Override
    public ITextMask getMask() {
        return textMask;
    }

}
