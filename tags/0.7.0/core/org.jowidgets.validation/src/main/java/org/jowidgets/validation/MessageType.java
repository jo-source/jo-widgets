/*
 * Copyright (c) 2011, Michael Grossmann
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

public enum MessageType {

	/**
	 * Input is valid
	 */
	OK(true),

	/**
	 * Input is valid but some info should be given to the user (e.g. at initial state)
	 */
	INFO(true),

	/**
	 * Input is valid but seems to be unusual
	 */
	WARNING(true),

	/**
	 * Input not valid, but user did not make any mistake yet
	 * (e.g. field is mandatory and empty or input could be completed to an valid input)
	 */
	INFO_ERROR(false),

	/**
	 * Input is not valid
	 */
	ERROR(false);

	private final boolean valid;

	private MessageType(final boolean valid) {
		this.valid = valid;
	}

	public boolean isValid() {
		return valid;
	}

	/**
	 * Checks if the severity of this type is equal or worse
	 * than the severity of the given type.
	 * 
	 * @param message The type to check against
	 * 
	 * @return True if the severity the of this type is equal or worse
	 *         than the severity of the given type, false otherwise
	 */
	public boolean equalOrWorse(final MessageType messageType) {
		Assert.paramNotNull(messageType, "messageType");
		return (this == messageType || worse(messageType));
	}

	/**
	 * Checks if the severity of this type is worse
	 * than the severity of the given type.
	 * 
	 * @param message The type to check against
	 * 
	 * @return True if the severity the of this type is worse
	 *         than the severity of the given type, false otherwise
	 */
	public boolean worse(final MessageType messageType) {
		Assert.paramNotNull(messageType, "messageType");
		if (this == OK) {
			return false;
		}
		if (this == INFO) {
			return messageType == OK;
		}
		else if (this == WARNING) {
			return messageType == INFO || messageType == OK;
		}
		else if (this == INFO_ERROR) {
			return messageType == INFO || messageType == OK || messageType == WARNING;
		}
		else if (this == ERROR) {
			return messageType == INFO || messageType == OK || messageType == WARNING || messageType == INFO_ERROR;
		}
		else {
			throw new IllegalArgumentException("MessageType '" + messageType + "' is not known");
		}
	}
}
