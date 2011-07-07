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

public interface IValidationMessage {

	/**
	 * @return The message type, never null
	 */
	MessageType getType();

	/**
	 * @return The message text, may be null
	 */
	String getText();

	/**
	 * The context where the validation message has been occurred,
	 * e.g. the property or bean label
	 * 
	 * @return The context, may be null
	 */
	String getContext();

	/**
	 * Creates a validation message that has the given context
	 * 
	 * @param context The context to set on the result
	 * 
	 * @return A new validation message with the changed context
	 */
	IValidationMessage withContext(String context);

	/**
	 * Checks if the severity of this message is equal or worse
	 * than the severity of the given message.
	 * 
	 * @param message The message to check against
	 * 
	 * @return True if the severity the of this message is equal or worse
	 *         than the severity of the given message, false otherwise
	 */
	boolean equalOrWorse(final IValidationMessage message);

	/**
	 * Checks if the severity of this message is worse
	 * than the severity of the given message.
	 * 
	 * @param message The message to check against
	 * 
	 * @return True if the severity the of this message is worse
	 *         than the severity of the given message, false otherwise
	 */
	boolean worse(final IValidationMessage message);

}
