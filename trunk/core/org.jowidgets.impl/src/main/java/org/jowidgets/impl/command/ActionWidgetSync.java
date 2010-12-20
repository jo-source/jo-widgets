/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.impl.command;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionChangeListener;
import org.jowidgets.common.image.IImageConstant;

public class ActionWidgetSync {

	private final IAction action;
	private final IActionWidget actionWidget;
	private final IActionChangeListener actionChangeListener;

	private boolean active;

	private boolean textDirty;
	private boolean toolTipTextDirty;
	private boolean iconDirty;
	private boolean enabledDirty;

	private boolean currentEnabled;

	public ActionWidgetSync(final IAction action, final IActionWidget actionWidget) {
		super();

		this.active = false;

		this.textDirty = true;
		this.toolTipTextDirty = true;
		this.iconDirty = true;
		this.enabledDirty = true;

		this.action = action;
		this.actionWidget = actionWidget;

		this.actionChangeListener = new ActionChangeListener();

		//set text immediately to avoid that menu has wrong size when opened
		//set text should not be expensive normally
		setText(action.getText());

		if (action.getActionChangeObservable() != null) {
			action.getActionChangeObservable().addActionChangeListener(actionChangeListener);
		}

		//enable the item to ensure that accelerators works. Item maybe enabled.
		actionWidget.setEnabled(true);
		currentEnabled = true;
	}

	public void setActive(final boolean active) {
		if (active) {
			if (enabledDirty) {
				setEnabled(action.isEnabled());
			}
			if (textDirty) {
				setText(action.getText());
			}
			if (toolTipTextDirty) {
				setTooltipText(action.getToolTipText());
			}
			if (iconDirty) {
				setIcon(action.getIcon());
			}
			this.active = true;
		}
		else {
			this.active = false;
		}
	}

	public void dispose() {
		if (action.getActionChangeObservable() != null) {
			action.getActionChangeObservable().removeActionChangeListener(actionChangeListener);
		}
	}

	private void setText(final String text) {
		actionWidget.setText(text);
		textDirty = false;
	}

	private void setIcon(final IImageConstant icon) {
		actionWidget.setIcon(icon);
		iconDirty = false;
	}

	private void setEnabled(final boolean enabled) {
		actionWidget.setEnabled(enabled);
		enabledDirty = false;
		currentEnabled = enabled;
	}

	private void setTooltipText(final String tooltipText) {
		actionWidget.setToolTipText(tooltipText);
		toolTipTextDirty = false;
	}

	private class ActionChangeListener implements IActionChangeListener {

		@Override
		public void textChanged() {
			if (active) {
				setText(action.getText());
			}
			else {
				textDirty = true;
			}
		}

		@Override
		public void toolTipTextChanged() {
			if (active) {
				setTooltipText(action.getToolTipText());
			}
			else {
				toolTipTextDirty = true;
			}
		}

		@Override
		public void iconChanged() {
			if (active) {
				setIcon(action.getIcon());
			}
			else {
				iconDirty = true;
			}
		}

		@Override
		public void enabledChanged() {
			if (active) {
				setEnabled(action.isEnabled());
			}
			else {
				if (!currentEnabled) {
					//enable the item to ensure that accelerators works. Item maybe enabled.
					actionWidget.setEnabled(true);
					currentEnabled = true;
				}
				enabledDirty = true;
			}
		}
	}
}
