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

package org.jowidgets.spi.impl.mask;

import org.jowidgets.common.mask.ITextMask;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public final class TextMaskMatcherTest {

	private ITextMask textMask;
	private TextMaskMatcher matcher;

	@Before
	public void setUp() {
		final TextMaskBuilder textMaskBuilder = new TextMaskBuilder();
		textMaskBuilder.addCharacterMask("[0-3]");
		textMaskBuilder.addNumericMask('_');
		textMaskBuilder.addDelimiter('-');
		textMaskBuilder.addCharacterMask("[0-1]");
		textMaskBuilder.addNumericMask('_');
		textMaskBuilder.addDelimiter('-');
		textMaskBuilder.addNumericMask('_');
		textMaskBuilder.addNumericMask('_');
		textMaskBuilder.addNumericMask('_');
		textMaskBuilder.addNumericMask('_');
		//textMaskBuilder.setMode(TextMaskMode.PARTITIAL_MASK);

		textMask = textMaskBuilder.build();

		matcher = new TextMaskMatcher(textMask);
	}

	@After
	public void tearDown() {
		textMask = null;
		matcher = null;
	}

	@Test
	public void test1() {
		final MatchResult result = matcher.match(0, "1-1-1234");
		Assert.assertTrue(result.isMatching());
		Assert.assertTrue(result.getMaskIndex(0) == 1);
		Assert.assertTrue(result.getMaskIndex(1) == 2);
		Assert.assertTrue(result.getMaskIndex(2) == 4);
		Assert.assertTrue(result.getMaskIndex(3) == 5);
		Assert.assertTrue(result.getMaskIndex(4) == 6);
		Assert.assertTrue(result.getMaskIndex(5) == 7);
		Assert.assertTrue(result.getMaskIndex(6) == 8);
		Assert.assertTrue(result.getMaskIndex(7) == 9);
		Assert.assertTrue(result.getLastMatch() == 7);
	}

	@Test
	public void test2() {
		final MatchResult result = matcher.match(0, "1-3-1972");
		Assert.assertTrue(result.isMatching());
		Assert.assertTrue(result.getMaskIndex(0) == 1);
		Assert.assertTrue(result.getMaskIndex(1) == 2);
		Assert.assertTrue(result.getMaskIndex(2) == 4);
		Assert.assertTrue(result.getMaskIndex(3) == 5);
		Assert.assertTrue(result.getMaskIndex(4) == 6);
		Assert.assertTrue(result.getMaskIndex(5) == 7);
		Assert.assertTrue(result.getMaskIndex(6) == 8);
		Assert.assertTrue(result.getMaskIndex(7) == 9);
		Assert.assertTrue(result.getLastMatch() == 7);
	}

	@Test
	public void test3() {
		final MatchResult result = matcher.match(0, "4-4-1234");
		Assert.assertTrue(result.isMatching());
		Assert.assertTrue(result.getMaskIndex(0) == 1);
		Assert.assertTrue(result.getMaskIndex(1) == 2);
		Assert.assertTrue(result.getMaskIndex(2) == 4);
		Assert.assertTrue(result.getMaskIndex(3) == 5);
		Assert.assertTrue(result.getMaskIndex(4) == 6);
		Assert.assertTrue(result.getMaskIndex(5) == 7);
		Assert.assertTrue(result.getMaskIndex(6) == 8);
		Assert.assertTrue(result.getMaskIndex(7) == 9);
		Assert.assertTrue(result.getLastMatch() == 7);
	}

	@Test
	public void test4() {
		final MatchResult result = matcher.match(0, "14-12-1234");
		Assert.assertTrue(result.isMatching());
		Assert.assertTrue(result.getMaskIndex(0) == 0);
		Assert.assertTrue(result.getMaskIndex(1) == 1);
		Assert.assertTrue(result.getMaskIndex(2) == 2);
		Assert.assertTrue(result.getMaskIndex(3) == 3);
		Assert.assertTrue(result.getMaskIndex(4) == 4);
		Assert.assertTrue(result.getMaskIndex(5) == 5);
		Assert.assertTrue(result.getMaskIndex(6) == 6);
		Assert.assertTrue(result.getMaskIndex(7) == 7);
		Assert.assertTrue(result.getMaskIndex(8) == 8);
		Assert.assertTrue(result.getMaskIndex(9) == 9);
		Assert.assertTrue(result.getLastMatch() == 9);
	}

	@Test
	public void test5() {
		final MatchResult result = matcher.match(0, "4-24-1234");
		Assert.assertFalse(result.isMatching());
		Assert.assertTrue(result.getMaskIndex(0) == 1);
		Assert.assertTrue(result.getMaskIndex(1) == 2);
		Assert.assertTrue(result.getMaskIndex(2) == 4);
		Assert.assertTrue(result.getMaskIndex(3) == -1);
		Assert.assertTrue(result.getMaskIndex(4) == -1);
		Assert.assertTrue(result.getMaskIndex(5) == -1);
		Assert.assertTrue(result.getMaskIndex(6) == -1);
		Assert.assertTrue(result.getMaskIndex(7) == -1);
		Assert.assertTrue(result.getMaskIndex(8) == -1);
		Assert.assertTrue(result.getLastMatch() == 2);
	}

	@Test
	public void test6() {
		final MatchResult result = matcher.match(0, "44-24-1234");
		Assert.assertFalse(result.isMatching());
		Assert.assertTrue(result.getMaskIndex(0) == 1);
		Assert.assertTrue(result.getMaskIndex(1) == -1);
		Assert.assertTrue(result.getMaskIndex(2) == -1);
		Assert.assertTrue(result.getMaskIndex(3) == -1);
		Assert.assertTrue(result.getMaskIndex(4) == -1);
		Assert.assertTrue(result.getMaskIndex(5) == -1);
		Assert.assertTrue(result.getMaskIndex(6) == -1);
		Assert.assertTrue(result.getMaskIndex(7) == -1);
		Assert.assertTrue(result.getMaskIndex(8) == -1);
		Assert.assertTrue(result.getLastMatch() == 0);
	}

}
