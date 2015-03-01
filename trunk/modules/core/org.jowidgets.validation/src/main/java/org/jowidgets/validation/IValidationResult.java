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
package org.jowidgets.validation;

import java.util.List;

/**
 * A validation result holds all validation messages of an validation.
 * 
 * A validation result is mutable, so messages can not be added or removed after creation.
 * 
 * Because ValidationMessages are designed imutable either, a validation result never changes after creation.
 */
public interface IValidationResult {

	/**
	 * Gets all validation messages as a unmodifiable list in order of their creation
	 * 
	 * @return All validation messages, may be empty but never null
	 */
	List<IValidationMessage> getAll();

	/**
	 * Gets all validation error messages as a unmodifiable list in order of their creation
	 * 
	 * @return All validation error messages, may be empty but never null
	 */
	List<IValidationMessage> getErrors();

	/**
	 * Gets all validation info error messages as a unmodifiable list in order of their creation
	 * 
	 * @return All validation info error messages, may be empty but never null
	 */
	List<IValidationMessage> getInfoErrors();

	/**
	 * Gets all validation warning messages as a unmodifiable list in order of their creation
	 * 
	 * @return All validation warning messages, may be empty but never null
	 */
	List<IValidationMessage> getWarnings();

	/**
	 * Gets all validation info messages as a unmodifiable list in order of their creation
	 * 
	 * @return All validation info messages, may be empty but never null
	 */
	List<IValidationMessage> getInfos();

	/**
	 * Gets the first message of the worst MessageType this result has.
	 * 
	 * In many cases only the first error is relevant and fill be shown up to the user
	 * 
	 * @return The first worst message, never null
	 */
	IValidationMessage getWorstFirst();

	/**
	 * Checks if this result is valid.
	 * 
	 * A result is valid if it has no error or info error messages.
	 * 
	 * Attention: Be careful not this mix up with the {@link #isOk()} Method.
	 * A result may be valid but not ok because it has warnings or infos.
	 * 
	 * @return True if valid, false otherwise
	 */
	boolean isValid();

	/**
	 * Returns true if the result has no messages or only messages with type ok.
	 * 
	 * Attention: Be careful not this mix up with the {@link #isValid()} Method.
	 * A result may be valid but not ok because it has warnings or infos.
	 * 
	 * @return The if the result is ok
	 */
	boolean isOk();

	/**
	 * Creates a new validation result based on this result with adding a new message.
	 * 
	 * Remark: This validation result remains unchanged by this operation, so the result must
	 * be assigned explicitly to an variable or constant.
	 * 
	 * @param message The message to add, must not be null
	 * 
	 * @return The new validation result, never null
	 */
	IValidationResult withMessage(final IValidationMessage message);

	/**
	 * Creates a new validation result based on this result with adding a error message.
	 * 
	 * Remark: This validation result remains unchanged by this operation, so the result must
	 * be assigned explicitly to an variable or constant.
	 * 
	 * @param message The text of the message to add
	 * 
	 * @return The new validation result, never null
	 */
	IValidationResult withError(final String text);

	/**
	 * Creates a new validation result based on this result with adding a info error message.
	 * 
	 * Remark: This validation result remains unchanged by this operation, so the result must
	 * be assigned explicitly to an variable or constant.
	 * 
	 * @param message The text of the message to add
	 * 
	 * @return The new validation result, never null
	 */
	IValidationResult withInfoError(final String text);

	/**
	 * Creates a new validation result based on this result with adding a warning message.
	 * 
	 * Remark: This validation result remains unchanged by this operation, so the result must
	 * be assigned explicitly to an variable or constant.
	 * 
	 * @param message The text of the message to add
	 * 
	 * @return The new validation result, never null
	 */
	IValidationResult withWarning(final String text);

	/**
	 * Creates a new validation result based on this result with adding a info message.
	 * 
	 * Remark: This validation result remains unchanged by this operation, so the result must
	 * be assigned explicitly to an variable or constant.
	 * 
	 * @param message The text of the message to add
	 * 
	 * @return The new validation result, never null
	 */
	IValidationResult withInfo(final String text);

	/**
	 * Creates a new validation result based on this result with adding a error message for a given context.
	 * 
	 * Remark: This validation result remains unchanged by this operation, so the result must
	 * be assigned explicitly to an variable or constant.
	 * 
	 * @param context The context of the message to add (the other contexts remains unchanged)
	 * @param message The text of the message to add
	 * 
	 * @return The new validation result, never null
	 */
	IValidationResult withError(final String context, final String text);

	/**
	 * Creates a new validation result based on this result with adding a info error message for a given context.
	 * 
	 * Remark: This validation result remains unchanged by this operation, so the result must
	 * be assigned explicitly to an variable or constant.
	 * 
	 * @param context The context of the message to add (the other contexts remains unchanged)
	 * @param message The text of the message to add
	 * 
	 * @return The new validation result, never null
	 */
	IValidationResult withInfoError(final String context, final String text);

	/**
	 * Creates a new validation result based on this result with adding a warning message for a given context.
	 * 
	 * Remark: This validation result remains unchanged by this operation, so the result must
	 * be assigned explicitly to an variable or constant.
	 * 
	 * @param context The context of the message to add (the other contexts remains unchanged)
	 * @param message The text of the message to add
	 * 
	 * @return The new validation result, never null
	 */
	IValidationResult withWarning(final String context, final String text);

	/**
	 * Creates a new validation result based on this result with adding a info message for a given context.
	 * 
	 * Remark: This validation result remains unchanged by this operation, so the result must
	 * be assigned explicitly to an variable or constant.
	 * 
	 * @param context The context of the message to add (the other contexts remains unchanged)
	 * @param message The text of the message to add
	 * 
	 * @return The new validation result, never null
	 */
	IValidationResult withInfo(final String context, final String text);

	/**
	 * Creates a new validation result based on this result with adding all messages of a given validation result
	 * 
	 * Remark: This validation result remains unchanged by this operation, so the result must
	 * be assigned explicitly to an variable or constant.
	 * 
	 * @param result The result to add
	 * 
	 * @return The new validation result, never null
	 */
	IValidationResult withResult(final IValidationResult result);

	/**
	 * Creates a new validation result based on this result with changing the context of all messages
	 * 
	 * Remark: This validation result remains unchanged by this operation, so the result must
	 * be assigned explicitly to an variable or constant.
	 * 
	 * @param result The context to set, may be null
	 * 
	 * @return The new validation result, never null
	 */
	IValidationResult withContext(final String context);

}
