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

import java.util.LinkedList;

import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.util.Assert;

// TODO LG generate unique id
// TODO LG remove default identifier
public final class TestToolUtilities {

	private static final String ELEMENT_SEPARATOR = "/";

	public TestToolUtilities() {}

	public String createWidgetID(final IWidget widget, final String identifier) {
		Assert.paramNotNull(widget, "widget");
		if (identifier.isEmpty()) {
			throw new IllegalArgumentException("An empty identifier is not allowed!");
		}
		final LinkedList<IWidget> widgetList = new LinkedList<IWidget>();
		widgetList.add(widget);
		IWidget parent = widget.getParent();
		while (parent != null) {
			widgetList.add(parent);
			parent = parent.getParent();
		}
		final StringBuilder sb = new StringBuilder();
		while (!widgetList.isEmpty()) {
			if (widgetList.size() > 1) {
				final IWidget tmpWidget = widgetList.getLast();
				if (tmpWidget instanceof IContainer) {
					final IContainer tmpContainer = (IContainer) tmpWidget;
					final IWidget childWidget = widgetList.get(widgetList.size() - 2);
					final int childPosition = tmpContainer.getChildren().indexOf(childWidget);
					sb.append(widgetList.getLast().getClass().getSimpleName());
					sb.append(ELEMENT_SEPARATOR);
					if (childPosition != -1) {
						sb.append(childPosition);
						sb.append("_");
					}
					widgetList.removeLast();
				}
				else {
					sb.append(widgetList.getLast().getClass().getSimpleName());
					sb.append(ELEMENT_SEPARATOR);
					widgetList.removeLast();

				}
			}
			else {
				sb.append(widgetList.getLast().getClass().getSimpleName());
				sb.append(ELEMENT_SEPARATOR);
				widgetList.removeLast();
			}
		}
		//sb.append(widget.getClass().getSimpleName());
		//sb.append(ELEMENT_SEPARATOR);
		if (identifier.isEmpty()) {
			sb.append("test");
		}
		else {
			sb.append(identifier);
		}
		return sb.toString();
	}
}
