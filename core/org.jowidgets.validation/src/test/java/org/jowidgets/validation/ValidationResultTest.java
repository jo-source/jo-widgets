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

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class ValidationResultTest {

	@Test
	public void testOkResult() {
		final IValidationResult ok = ValidationResult.ok();
		Assert.assertEquals(ok.getWorstFirst().getType(), MessageType.OK);
	}

	@Test
	public void testResult() {
		final IValidationResult result = ValidationResult.create();
		Assert.assertEquals(result.getWorstFirst().getType(), MessageType.OK);
		Assert.assertTrue(result.isValid());

		testEmptyResultList(result.getAll());
		testEmptyResultList(result.getErrors());
		testEmptyResultList(result.getInfoErrors());
		testEmptyResultList(result.getWarnings());

		IValidationResult withAdded = result.withWarning("TEXT");
		Assert.assertTrue(withAdded.getWorstFirst().getType() == MessageType.WARNING);
		Assert.assertTrue(withAdded.getAll().size() == 1);
		Assert.assertTrue(withAdded.getErrors().size() == 0);
		Assert.assertTrue(withAdded.getInfoErrors().size() == 0);
		Assert.assertTrue(withAdded.getWarnings().size() == 1);

		withAdded = withAdded.withWarning("TEXT2");
		Assert.assertTrue(withAdded.isValid());
		Assert.assertTrue(withAdded.getWorstFirst().getType() == MessageType.WARNING);
		Assert.assertTrue("TEXT".equals(withAdded.getWorstFirst().getMessage()));
		Assert.assertTrue(withAdded.getAll().size() == 2);
		Assert.assertTrue(withAdded.getErrors().size() == 0);
		Assert.assertTrue(withAdded.getInfoErrors().size() == 0);
		Assert.assertTrue(withAdded.getWarnings().size() == 2);

		withAdded = withAdded.withInfoError("TEXT3");
		Assert.assertFalse(withAdded.isValid());
		Assert.assertTrue(withAdded.getWorstFirst().getType() == MessageType.INFO_ERROR);
		Assert.assertTrue(withAdded.getAll().size() == 3);
		Assert.assertTrue(withAdded.getErrors().size() == 0);
		Assert.assertTrue(withAdded.getInfoErrors().size() == 1);
		Assert.assertTrue(withAdded.getWarnings().size() == 2);

		withAdded = withAdded.withWarning("TEXT4");
		Assert.assertFalse(withAdded.isValid());
		Assert.assertTrue(withAdded.getWorstFirst().getType() == MessageType.INFO_ERROR);
		Assert.assertTrue(withAdded.getAll().size() == 4);
		Assert.assertTrue(withAdded.getErrors().size() == 0);
		Assert.assertTrue(withAdded.getInfoErrors().size() == 1);
		Assert.assertTrue(withAdded.getWarnings().size() == 3);

		withAdded = withAdded.withError("TEXT5");
		Assert.assertFalse(withAdded.isValid());
		Assert.assertTrue(withAdded.getWorstFirst().getType() == MessageType.ERROR);
		Assert.assertTrue("TEXT5".equals(withAdded.getWorstFirst().getMessage()));
		Assert.assertTrue(withAdded.getAll().size() == 5);
		Assert.assertTrue(withAdded.getErrors().size() == 1);
		Assert.assertTrue(withAdded.getInfoErrors().size() == 1);
		Assert.assertTrue(withAdded.getWarnings().size() == 3);

		testContextNull(withAdded.getAll());

		IValidationResult result2 = ValidationResult.create();
		result2 = result2.withError("TEXT6");
		result2 = result2.withInfoError("TEXT7");
		result2 = result2.withWarning("TEXT8");

		IValidationResult withResult = withAdded.withResult(result2);
		Assert.assertFalse(withResult.isValid());
		Assert.assertTrue(withResult.getWorstFirst().getType() == MessageType.ERROR);
		Assert.assertTrue("TEXT5".equals(withResult.getWorstFirst().getMessage()));
		Assert.assertTrue(withResult.getAll().size() == 8);
		Assert.assertTrue(withResult.getErrors().size() == 2);
		Assert.assertTrue(withResult.getInfoErrors().size() == 2);
		Assert.assertTrue(withResult.getWarnings().size() == 4);

		withResult = withAdded.withResult("NEW_CONTEXT", result2);
		assertContext(withResult.getAll(), "NEW_CONTEXT");
		Assert.assertFalse(withResult.isValid());
		Assert.assertTrue(withResult.getWorstFirst().getType() == MessageType.ERROR);
		Assert.assertTrue("TEXT5".equals(withResult.getWorstFirst().getMessage()));
		Assert.assertTrue(withResult.getAll().size() == 8);
		Assert.assertTrue(withResult.getErrors().size() == 2);
		Assert.assertTrue(withResult.getInfoErrors().size() == 2);
		Assert.assertTrue(withResult.getWarnings().size() == 4);

		final IValidationResult withContext = withAdded.withContext("NEW_CONTEXT");
		assertContext(withContext.getAll(), "NEW_CONTEXT");

		final IValidationResult newWithContext = withContext.withContext("NEW_CONTEXT");
		Assert.assertTrue(withContext == newWithContext);

	}

	private void testContextNull(final List<IValidationMessage> messages) {
		for (final IValidationMessage message : messages) {
			Assert.assertNull(message.getContext());
		}
	}

	private void assertContext(final List<IValidationMessage> messages, final String context) {
		for (final IValidationMessage message : messages) {
			Assert.assertEquals(message.getContext(), context);
		}
	}

	private void testEmptyResultList(final List<IValidationMessage> messages) {
		Assert.assertNotNull(messages);
		Assert.assertTrue(messages.size() == 0);
	}

	@Test(expected = RuntimeException.class)
	public void testReadonlyLists() {
		final IValidationResult result = ValidationResult.create();
		result.getAll().add(ValidationMessage.error("text"));
	}

}
