/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.i18n.api;

import java.util.Locale;

/**
 * Holds the current user locale context.
 * 
 * There may be different implementations of this interface, e.g.
 * 
 * 1. A static value holder that holds the same user locale for any access
 * 2. A ThreadLocal value holder, if each thread may have its own user locale (e.g. in eclipse RWT)
 * 3. A SessionContextLocaleHolder, if each Session has its own user locale
 * ...
 * 
 * The locale holder can be injected with help of java services or set directly on the class {@link LocaleHolder}
 */
public interface ILocaleHolder {

    /**
     * Gets the current user locale.
     * 
     * @return The user locale to get, never null
     */
    Locale getUserLocale();

    /**
     * Sets the current user locale.
     * 
     * @param userLocale The user locale to set, may be null. If set to null, the default locale will be used
     */
    void setUserLocale(Locale userLocale);

    /**
     * Clears the current user locale
     */
    void clearUserLocale();

}
