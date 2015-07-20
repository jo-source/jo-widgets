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

public interface IValidationResultBuilder {

    /**
     * Adds a message
     * 
     * @param message The message to add, must not be null
     * 
     * @return This builder
     */
    IValidationResultBuilder addMessage(final IValidationMessage message);

    /**
     * Adds a info message
     * 
     * @param message The message text of the message to add
     * 
     * @return This builder
     */
    IValidationResultBuilder addInfo(final String text);

    /**
     * Adds a warning message
     * 
     * @param message The message text of the message to add
     * 
     * @return This builder
     */
    IValidationResultBuilder addWarning(final String text);

    /**
     * Adds a info error message
     * 
     * @param message The message text of the message to add
     * 
     * @return This builder
     */
    IValidationResultBuilder addInfoError(final String text);

    /**
     * Adds a error message
     * 
     * @param message The message text of the message to add
     * 
     * @return This builder
     */
    IValidationResultBuilder addError(final String text);

    /**
     * Adds a info message
     * 
     * @param context The context of the message to add
     * @param text The message text of the message to add
     * 
     * @return This builder
     */
    IValidationResultBuilder addInfo(final String context, final String text);

    /**
     * Adds a warning message
     * 
     * @param context The context of the message to add
     * @param text The message text of the message to add
     * 
     * @return This builder
     */
    IValidationResultBuilder addWarning(final String context, final String text);

    /**
     * Adds a info error message
     * 
     * @param context The context of the message to add
     * @param text The message text of the message to add
     * 
     * @return This builder
     */
    IValidationResultBuilder addInfoError(final String context, final String text);

    /**
     * Adds a error message
     * 
     * @param context The context of the message to add
     * @param text The message text of the message to add
     * 
     * @return This builder
     */
    IValidationResultBuilder addError(final String context, final String text);

    /**
     * Adds all messages of a result the new result
     * 
     * @param result The result to add, must not be null
     * 
     * @return This builder
     */
    IValidationResultBuilder addResult(final IValidationResult result);

    /**
     * Creates a new validation result
     * 
     * @return A new validation result, never null
     */
    IValidationResult build();

}
