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

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.IToolBarItem;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.test.api.widgets.IButtonUi;

//CHECKSTYLE:OFF
public class TestPlayer {

	private final WidgetFinder finder;

	public TestPlayer() {
		this.finder = new WidgetFinder();
	}

	public void replayTest(final List<TestDataObject> list, final int delay) {
		for (final TestDataObject obj : list) {
			final Thread thread = new Thread() {
				@Override
				public void run() {
					final IWidgetCommon widget = finder.findWidgetByID(WidgetRegistry.getInstance().getWidgets(), obj.getId());
					if (widget != null) {
						executeAction(widget, obj.getAction());
						try {
							Thread.sleep(delay);
						}
						catch (final InterruptedException e) {
						}
					}
					else {
						// TODO LG maybe a nice feature, when the user could select the missing widget.
						System.out.println("couldn't find widget with id: " + obj.getId());
					}
				}
			};
			// TODO LG stop threads when they are finished
			Toolkit.getUiThreadAccess().invokeLater(thread);
		}
	}

	private synchronized void executeAction(final IWidgetCommon widget, final UserAction action) {
		moveMouseToWidget(widget);
		if (widget instanceof IButtonUi) {
			final IButtonUi button = (IButtonUi) widget;
			switch (action) {
				case CLICK:
					System.out.println("press Button");
					button.push();
					break;
				default:
					System.out.println("the given user action is not supported for this widget.");
					break;
			}
		}
		else if (widget instanceof ITreeNode) {
			final ITreeNode node = (ITreeNode) widget;
			switch (action) {
				case EXPAND:
					System.out.println("expand tree item: " + node.getText());
					node.setSelected(true);
					node.setExpanded(true);
					break;
				case COLLAPSE:
					System.out.println("collapse tree item: " + node.getText());
					node.setSelected(true);
					node.setExpanded(false);
					break;
				case SELECT:
					System.out.println("select tree item: " + node.getText());
					node.setSelected(true);
					break;
				default:
					System.out.println("the given user action is not supported for this widget.");
					break;
			}
		}
		else if (widget instanceof IFrame) {
			final IFrame frame = (IFrame) widget;
			switch (action) {
				case CLOSE:
					WidgetRegistry.getInstance().removeWidget(frame);
					frame.setVisible(false);
					frame.dispose();
					System.out.println("frame closed");
					break;
				default:
					System.out.println("the given user action is not supported for this widget.");
					break;
			}
		}
		else if (widget instanceof IToolBarItem) {
			switch (action) {
				case CLICK:
					System.out.println("ToolBarButton pushed.");
					break;
				default:
					break;
			}
		}
		else if (widget instanceof ITabItem) {
			switch (action) {
				case CLICK:
					System.out.println("changing tab item selection");
					break;
				default:
					break;
			}
		}
		else {
			System.out.println("the given widget is not supported.");
		}
	}

	private void moveMouseToWidget(final IWidgetCommon targetWidget) {
		// TODO LG calculate target position and move mouse
		System.out.println("moving mouse to target widget");
	}
}
