/*
 * Copyright (c) 2010 Michael Grossmann
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
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.swt.cursor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.util.Assert;

public final class CursorCache {

	private static Map<Cursor, org.eclipse.swt.graphics.Cursor> cursorMap;

	private CursorCache() {}

	public static org.eclipse.swt.graphics.Cursor getCursor(final Cursor cursor) {
		Assert.paramNotNull(cursor, "cursor");
		final org.eclipse.swt.graphics.Cursor result = getMap().get(cursor);

		if (result == null && cursor != Cursor.DEFAULT) {
			throw new IllegalArgumentException("Cursor '" + cursor + "' is unkwon");
		}

		return result;
	}

	private static Map<Cursor, org.eclipse.swt.graphics.Cursor> getMap() {
		if (cursorMap == null) {
			cursorMap = new HashMap<Cursor, org.eclipse.swt.graphics.Cursor>();
			cursorMap.put(Cursor.DEFAULT, null);
			cursorMap.put(Cursor.WAIT, new org.eclipse.swt.graphics.Cursor(Display.getDefault(), SWT.CURSOR_WAIT));
			cursorMap.put(Cursor.ARROW, new org.eclipse.swt.graphics.Cursor(Display.getDefault(), SWT.CURSOR_ARROW));
		}
		return cursorMap;
	}

}
