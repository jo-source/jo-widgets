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

import java.util.List;

//CHECKSTYLE:OFF
public class TestPlayer {

	//private final WidgetFinder finder;

	public TestPlayer() {
		//this.finder = new WidgetFinder();
	}

	public void replayTest(final List<TestDataObject> list, final boolean headlessEnv) {
		//		if (headlessEnv) {
		//			for (final TestDataObject obj : list) {
		//				final IWidgetCommon widget = finder.findWidgetByID(widgetRegistry, obj.getId());
		//								executeAction(widget, obj.getAction());
		//			}
		//		}
		//		else {
		//			// TODO LG what needs to be thread safe?
		//			for (final TestDataObject obj : list) {
		//				final IWidgetCommon widget = finder.findWidgetByID(widgetRegistry, obj.getId());
		//				// TODO LG create Thread and execute Action!
		//								moveMouseToWidget(widget);
		//								executeAction(widget, obj.getAction());
		//				 Toolkit.getUiThreadAccess().invokeLater(runnable);
		//			}
		//		}
	}

	//	private void executeAction(final IWidgetCommon widget, final UserAction action) {
	//		if (widget instanceof IButtonUi) {
	//			final IButtonUi button = (IButtonUi) widget;
	//			switch (action) {
	//				case CLICK:
	//					button.push();
	//					break;
	//				default:
	//					break;
	//			}
	//		}
	//	}
	//
	//	private void moveMouseToWidget(final IWidgetCommon targetWidget) {
	//		// TODO LG calculate target position and move mouse
	//	}
}
