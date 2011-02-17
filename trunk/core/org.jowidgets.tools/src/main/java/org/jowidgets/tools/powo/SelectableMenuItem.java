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

import org.jowidgets.api.model.item.ISelectableMenuItemModel;
import org.jowidgets.api.widgets.ISelectableMenuItem;
import org.jowidgets.api.widgets.blueprint.builder.ISelectableItemSetupBuilder;
import org.jowidgets.api.widgets.descriptor.setup.ISelectableItemSetup;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.widgets.controler.IItemStateListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

class SelectableMenuItem<WIDGET_TYPE extends ISelectableMenuItem, BLUE_PRINT_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE> & ISelectableItemSetupBuilder<?> & ISelectableItemSetup> extends
		MenuItem<WIDGET_TYPE, BLUE_PRINT_TYPE> implements ISelectableMenuItem {

	private final Set<IItemStateListener> itemListeners;

	SelectableMenuItem(final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
		this.itemListeners = new HashSet<IItemStateListener>();
	}

	@Override
	void initialize(final WIDGET_TYPE widget) {
		super.initialize(widget);
		for (final IItemStateListener listener : itemListeners) {
			getWidget().addItemListener(listener);
		}
	}

	@Override
	public void addItemListener(final IItemStateListener listener) {
		if (isInitialized()) {
			getWidget().addItemListener(listener);
		}
		else {
			itemListeners.add(listener);
		}
	}

	@Override
	public void removeItemListener(final IItemStateListener listener) {
		if (isInitialized()) {
			getWidget().removeItemListener(listener);
		}
		else {
			itemListeners.remove(listener);
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
	public boolean isSelected() {
		if (isInitialized()) {
			return getWidget().isSelected();
		}
		else {
			return getBluePrint().isSelected();
		}
	}

	@Override
	public void setSelected(final boolean selected) {
		if (isInitialized()) {
			getWidget().setSelected(selected);
		}
		else {
			getBluePrint().setSelected(selected);
		}
	}

	@Override
	public void setModel(final ISelectableMenuItemModel model) {
		checkInitialized();
		getWidget().setModel(model);
	}

	@Override
	public ISelectableMenuItemModel getModel() {
		checkInitialized();
		return getWidget().getModel();
	}

}
