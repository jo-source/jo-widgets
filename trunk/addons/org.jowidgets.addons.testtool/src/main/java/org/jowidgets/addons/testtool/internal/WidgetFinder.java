/*
 * Copyright (c) 2011, Lukas Gross
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

package org.jowidgets.addons.testtool.internal;

import java.util.Collection;
import java.util.Iterator;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.common.widgets.IWidgetCommon;

// TODO LG use Property to identify widget
// TODO LG remove supressWarnings
public class WidgetFinder {

	private final TestToolUtilities utilities;

	public WidgetFinder() {
		this.utilities = new TestToolUtilities();
	}

	@SuppressWarnings("unused")
	public IWidgetCommon findWidgetByID(final Collection<IWidgetCommon> widgets, final String id) {
		final String[] elements = id.split(TestToolUtilities.ELEMENT_SEPARATOR);
		final String e = elements[elements.length - 1];
		String clazz = "";
		String property = "";
		if (e.contains(TestToolUtilities.CHILD_INDEX_SEPARATOR)) {
			clazz = e.substring(
					e.indexOf(TestToolUtilities.CHILD_INDEX_SEPARATOR) + 1,
					e.indexOf(TestToolUtilities.PROPERTY_SEPARATOR));
		}
		else {
			clazz = e.substring(0, e.indexOf(TestToolUtilities.PROPERTY_SEPARATOR));
		}
		property = e.substring(e.indexOf(TestToolUtilities.PROPERTY_SEPARATOR) + 1, e.length());
		final Iterator<IWidgetCommon> iterator = widgets.iterator();
		while (iterator.hasNext()) {
			final IWidgetCommon item = iterator.next();
			if (clazz.equals(item.getClass().getSimpleName())) {
				if (utilities.createWidgetID((IWidget) item).equals(id)) {
					return item;
				}
			}
		}
		Toolkit.getMessagePane().showWarning("No Widget found! Please select the missing Widget.");
		return null;
	}
}
