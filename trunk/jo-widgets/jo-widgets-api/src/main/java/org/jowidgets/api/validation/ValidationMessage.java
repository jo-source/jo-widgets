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
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.api.validation;

import org.jowidgets.util.Assert;

public final class ValidationMessage {

	public static final ValidationMessage OK_MESSAGE = new ValidationMessage(ValidationMessageType.OK, ""); //$NON-NLS-1$

	private final ValidationMessageType type;

	private final String messageText;

	private final String context;

	public ValidationMessage(final ValidationMessageType type, final String messageText) {
		this(type, messageText, null);
	}

	public ValidationMessage(final ValidationMessageType type, final String messageText, final String context) {
		super();
		Assert.paramNotNull(type, "type"); //$NON-NLS-1$
		if (ValidationMessageType.OK.equals(type)) {
			Assert.paramNotNull(messageText, "messageText"); //$NON-NLS-1$
		}
		else {
			Assert.paramNotEmpty(messageText, "messageText"); //$NON-NLS-1$
		}
		this.type = type;
		this.messageText = messageText;
		this.context = context;
	}

	public ValidationMessageType getType() {
		return type;
	}

	public String getMessageText() {
		return messageText;
	}

	public String getContext() {
		return context;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((messageText == null) ? 0 : messageText.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ValidationMessage other = (ValidationMessage) obj;
		if (context == null) {
			if (other.context != null) {
				return false;
			}
		}
		else if (!context.equals(other.context)) {
			return false;
		}
		if (messageText == null) {
			if (other.messageText != null) {
				return false;
			}
		}
		else if (!messageText.equals(other.messageText)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

}
