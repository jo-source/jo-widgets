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

package org.jowidgets.tools.powo;

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.widgets.IActionMenuItem;
import org.jowidgets.api.widgets.blueprint.builder.IAccelerateableMenuItemSetupBuilder;
import org.jowidgets.api.widgets.descriptor.setup.IAccelerateableMenuItemSetup;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

class ActionMenuItem<WIDGET_TYPE extends IActionMenuItem, BLUE_PRINT_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE> & IAccelerateableMenuItemSetupBuilder<?> & IAccelerateableMenuItemSetup> extends
		MenuItem<WIDGET_TYPE, BLUE_PRINT_TYPE> implements IActionMenuItem {

	private final Set<IActionListener> actionListeners;
	private IAction action;

	ActionMenuItem(final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
		this.actionListeners = new HashSet<IActionListener>();
	}

	@Override
	void initialize(final WIDGET_TYPE widget) {
		super.initialize(widget);
		for (final IActionListener listener : actionListeners) {
			getWidget().addActionListener(listener);
		}
		if (action != null) {
			getWidget().setAction(action);
		}
	}

	@Override
	public void addActionListener(final IActionListener actionListener) {
		if (isInitialized()) {
			getWidget().addActionListener(actionListener);
		}
		else {
			this.actionListeners.add(actionListener);
		}
	}

	@Override
	public void removeActionListener(final IActionListener actionListener) {
		if (isInitialized()) {
			getWidget().removeActionListener(actionListener);
		}
		else {
			this.actionListeners.remove(actionListener);
		}
	}

	@Override
	public void setAccelerator(final Accelerator accelerator) {
		if (isInitialized()) {
			getWidget().setAccelerator(accelerator);
		}
		else {
			getBluePrint().setAccelerator(accelerator);
		}
	}

	@Override
	public void setAction(final IAction action) {
		if (isInitialized()) {
			getWidget().setAction(action);
		}
		else {
			this.action = action;
		}
	}

	@Override
	public void setModel(final IActionItemModel model) {
		// TODO MG model support
	}

	@Override
	public IActionItemModel getModel() {
		return (IActionItemModel) super.getModel();
	}

}
