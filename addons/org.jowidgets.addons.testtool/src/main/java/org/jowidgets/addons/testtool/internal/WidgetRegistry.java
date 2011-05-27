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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.common.widgets.IWidgetCommon;

public final class WidgetRegistry {
	private static final WidgetRegistry INSTANCE = new WidgetRegistry();
	private final Set<IWidgetCommon> widgetRegistry;

	private WidgetRegistry() {
		this.widgetRegistry = new HashSet<IWidgetCommon>();
	}

	public static WidgetRegistry getInstance() {
		return INSTANCE;
	}

	public synchronized Set<IWidgetCommon> getWidgets() {
		return widgetRegistry;
	}

	public synchronized void addWidget(final IWidgetCommon widget) {
		widgetRegistry.add(widget);
	}

	public synchronized void removeWidget(final IWidgetCommon widget) {
		removeChildWidgets(widget);
		widgetRegistry.remove(widget);
	}

	private void removeChildWidgets(final IWidgetCommon widget) {
		final List<IWidgetCommon> childs = getChildWidgets(widget);
		if (!childs.isEmpty()) {
			for (final IWidgetCommon child : childs) {
				widgetRegistry.remove(child);
			}
		}
	}

	private List<IWidgetCommon> getChildWidgets(final IWidgetCommon widget) {
		final List<IWidgetCommon> childs = new LinkedList<IWidgetCommon>();
		if (widget instanceof IContainer) {
			final IContainer con = (IContainer) widget;
			for (final IWidget tmp : con.getChildren()) {
				if (tmp instanceof IContainer) {
					childs.addAll(getChildWidgets(tmp));
				}
				else {
					childs.add(tmp);
				}
			}
		}
		return childs;
	}
}
