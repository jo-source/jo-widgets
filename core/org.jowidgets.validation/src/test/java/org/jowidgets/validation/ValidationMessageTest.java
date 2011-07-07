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

import junit.framework.Assert;

import org.junit.Test;

public class ValidationMessageTest {

	@Test
	public void testOkMessage() {
		final IValidationMessage message = ValidationMessage.ok();
		Assert.assertNotNull(message);
	}

	@Test
	public void testErrorMessage() {
		final String messageText = "MESSAGE";
		final String context = "CONTEXT";

		IValidationMessage message = ValidationMessage.error(messageText);
		Assert.assertEquals(messageText, message.getMessage());
		Assert.assertNull(message.getContext());

		testWithContext(message);

		message = ValidationMessage.error(context, messageText);
		Assert.assertEquals(messageText, message.getMessage());
		Assert.assertEquals(context, message.getContext());
	}

	@Test
	public void testInfoErrorMessage() {
		final String messageText = "MESSAGE";
		final String context = "CONTEXT";

		IValidationMessage message = ValidationMessage.infoError(messageText);
		Assert.assertEquals(messageText, message.getMessage());
		Assert.assertNull(message.getContext());

		testWithContext(message);

		message = ValidationMessage.infoError(context, messageText);
		Assert.assertEquals(messageText, message.getMessage());
		Assert.assertEquals(context, message.getContext());
	}

	@Test
	public void testWarningMessage() {
		final String messageText = "MESSAGE";
		final String context = "CONTEXT";

		IValidationMessage message = ValidationMessage.warning(messageText);
		Assert.assertEquals(messageText, message.getMessage());
		Assert.assertNull(message.getContext());

		testWithContext(message);

		message = ValidationMessage.warning(context, messageText);
		Assert.assertEquals(messageText, message.getMessage());
		Assert.assertEquals(context, message.getContext());
	}

	@Test
	public void testWorse() {
		final IValidationMessage ok = ValidationMessage.ok();
		final IValidationMessage warning = ValidationMessage.warning("Text");
		final IValidationMessage infoError = ValidationMessage.infoError("Text");
		final IValidationMessage error = ValidationMessage.error("Text");

		Assert.assertTrue(ok.equalOrWorse(ok));
		Assert.assertFalse(ok.worse(ok));
		Assert.assertTrue(ok.equalOrWorse(warning));
		Assert.assertTrue(ok.worse(warning));
		Assert.assertTrue(ok.equalOrWorse(infoError));
		Assert.assertTrue(ok.worse(infoError));
		Assert.assertTrue(ok.equalOrWorse(error));
		Assert.assertTrue(ok.worse(error));

		Assert.assertFalse(warning.equalOrWorse(ok));
		Assert.assertFalse(warning.worse(ok));
		Assert.assertTrue(warning.equalOrWorse(warning));
		Assert.assertFalse(warning.worse(warning));
		Assert.assertTrue(warning.equalOrWorse(infoError));
		Assert.assertTrue(warning.worse(infoError));
		Assert.assertTrue(warning.equalOrWorse(error));
		Assert.assertTrue(warning.worse(error));

		Assert.assertFalse(infoError.equalOrWorse(ok));
		Assert.assertFalse(infoError.worse(ok));
		Assert.assertFalse(infoError.equalOrWorse(warning));
		Assert.assertFalse(infoError.worse(warning));
		Assert.assertTrue(infoError.equalOrWorse(infoError));
		Assert.assertFalse(infoError.worse(infoError));
		Assert.assertTrue(infoError.equalOrWorse(error));
		Assert.assertTrue(infoError.worse(error));

		Assert.assertFalse(error.equalOrWorse(ok));
		Assert.assertFalse(error.worse(ok));
		Assert.assertFalse(error.equalOrWorse(warning));
		Assert.assertFalse(error.worse(warning));
		Assert.assertFalse(error.equalOrWorse(infoError));
		Assert.assertFalse(error.worse(infoError));
		Assert.assertTrue(error.equalOrWorse(error));
		Assert.assertFalse(error.worse(error));
	}

	private void testWithContext(final IValidationMessage message) {
		final String oldContext = message.getContext();
		final String oldMessageText = message.getMessage();
		final MessageType oldMessageType = message.getType();
		final String newContext = "NEW_CONTEXT";

		final IValidationMessage messageWithNewContext = message.withContext(newContext);
		Assert.assertEquals(oldContext, message.getContext());
		Assert.assertEquals(oldMessageText, message.getMessage());
		Assert.assertEquals(oldMessageType, message.getType());

		Assert.assertEquals(newContext, messageWithNewContext.getContext());
		Assert.assertEquals(oldMessageText, messageWithNewContext.getMessage());
		Assert.assertEquals(oldMessageType, messageWithNewContext.getType());

		final IValidationMessage messageWithSameContext = messageWithNewContext.withContext(newContext);
		Assert.assertTrue(messageWithSameContext == messageWithNewContext);
	}

}
