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

import java.io.Serializable;

final class ValidationMessageImpl implements IValidationMessage, Serializable {

	private static final long serialVersionUID = 6091301415212441351L;

	private final MessageType type;
	private final String message;
	private final String context;

	ValidationMessageImpl(final MessageType type, final String context, final String message) {
		Assert.paramNotNull(type, "type");
		this.type = type;
		this.context = context;
		this.message = message;
	}

	@Override
	public MessageType getType() {
		return type;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getContext() {
		return context;
	}

	@Override
	public IValidationMessage withContext(final String context) {
		if (NullCompatibleEquivalence.equals(this.context, context)) {
			return this;
		}
		else {
			return new ValidationMessageImpl(this.type, context, this.message);
		}
	}

	@Override
	public boolean equalOrWorse(final IValidationMessage message) {
		Assert.paramNotNull(message, "message");
		return type.equalOrWorse(message.getType());
	}

	@Override
	public boolean worse(final IValidationMessage message) {
		Assert.paramNotNull(message, "message");
		return type.worse(message.getType());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		final ValidationMessageImpl other = (ValidationMessageImpl) obj;
		if (context == null) {
			if (other.context != null) {
				return false;
			}
		}
		else if (!context.equals(other.context)) {
			return false;
		}
		if (message == null) {
			if (other.message != null) {
				return false;
			}
		}
		else if (!message.equals(other.message)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ValidationMessageImpl [type=" + type + ", message=" + message + ", context=" + context + "]";
	}

}
