/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.validation;

public final class ValidationResult {

    private static final IValidationResult OK = new ValidationResultImpl();

    private ValidationResult() {}

    /**
     * Creates a new validation result.
     * 
     * This method does the same as the method {@link #ok()}
     * 
     * @return A validation result constant
     */
    public static IValidationResult create() {
        return OK;
    }

    /**
     * Creates a new validation result with one message of MessageType OK
     * 
     * @return The ok validation result constant
     */
    public static IValidationResult ok() {
        return OK;
    }

    /**
     * Creates a new validation result for a given validation message
     * 
     * @param message The message to create the result for, must not be null
     * 
     * @return A new validation result, never null
     */
    public static IValidationResult create(final IValidationMessage message) {
        Assert.paramNotNull(message, "message");
        return create().withMessage(message);
    }

    /**
     * Creates a new validation result with one info message
     * 
     * @param text The message text of the message the result consists of
     * 
     * @return A new validation result, never null
     */
    public static IValidationResult info(final String text) {
        return create().withInfo(text);
    }

    /**
     * Creates a new validation result with one warning message
     * 
     * @param text The message text of the message the result consists of
     * 
     * @return A new validation result, never null
     */
    public static IValidationResult warning(final String text) {
        return create().withWarning(text);
    }

    /**
     * Creates a new validation result with one info error message
     * 
     * @param text The message text of the message the result consists of
     * 
     * @return A new validation result, never null
     */
    public static IValidationResult infoError(final String text) {
        return create().withInfoError(text);
    }

    /**
     * Creates a new validation result with one error message
     * 
     * @param text The message text of the message the result consists of
     * 
     * @return A new validation result, never null
     */
    public static IValidationResult error(final String text) {
        return create().withError(text);
    }

    /**
     * Creates a new validation result with one info message
     * 
     * @param context The context of the message the result consists of
     * @param text The message text of the message the result consists of
     * 
     * @return A new validation result, never null
     */
    public static IValidationResult info(final String context, final String text) {
        return create().withWarning(context, text);
    }

    /**
     * Creates a new validation result with one warning message
     * 
     * @param context The context of the message the result consists of
     * @param text The message text of the message the result consists of
     * 
     * @return A new validation result, never null
     */
    public static IValidationResult warning(final String context, final String text) {
        return create().withWarning(context, text);
    }

    /**
     * Creates a new validation result with one info error message
     * 
     * @param context The context of the message the result consists of
     * @param text The message text of the message the result consists of
     * 
     * @return A new validation result, never null
     */
    public static IValidationResult infoError(final String context, final String text) {
        return create().withInfoError(context, text);
    }

    /**
     * Creates a new validation result with one error message
     * 
     * @param context The context of the message the result consists of
     * @param text The message text of the message the result consists of
     * 
     * @return A new validation result, never null
     */
    public static IValidationResult error(final String context, final String text) {
        return create().withError(context, text);
    }

    /**
     * Creates a new validation result builder
     * 
     * @return A new validation result builder
     */
    public static IValidationResultBuilder builder() {
        return new ValidationResultBuilder();
    }

}
