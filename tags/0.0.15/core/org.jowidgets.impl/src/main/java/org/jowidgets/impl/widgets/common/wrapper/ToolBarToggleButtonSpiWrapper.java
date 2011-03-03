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

package org.jowidgets.impl.widgets.common.wrapper;

import org.jowidgets.api.model.item.ICheckedItemModel;
import org.jowidgets.common.widgets.IToolBarToggleButtonCommon;
import org.jowidgets.common.widgets.controler.IItemStateListener;
import org.jowidgets.impl.base.delegate.SelectableItemDelegate;
import org.jowidgets.spi.widgets.IToolBarToggleButtonSpi;

public class ToolBarToggleButtonSpiWrapper extends ToolBarItemSpiWrapper implements IToolBarToggleButtonCommon {

	public ToolBarToggleButtonSpiWrapper(final IToolBarToggleButtonSpi component, final SelectableItemDelegate itemDelegate) {
		super(component, itemDelegate);
		component.addItemListener(new IItemStateListener() {
			@Override
			public void itemStateChanged() {
				getModel().setSelected(component.isSelected());
			}
		});
	}

	@Override
	public IToolBarToggleButtonSpi getWidget() {
		return (IToolBarToggleButtonSpi) super.getWidget();
	}

	@Override
	protected SelectableItemDelegate getItemDelegate() {
		return (SelectableItemDelegate) super.getItemDelegate();
	}

	public ICheckedItemModel getModel() {
		return (ICheckedItemModel) getItemDelegate().getModel();
	}

	@Override
	public void addItemListener(final IItemStateListener listener) {
		getWidget().addItemListener(listener);
	}

	@Override
	public void removeItemListener(final IItemStateListener listener) {
		getWidget().removeItemListener(listener);
	}

	@Override
	public void setSelected(final boolean selected) {
		getItemDelegate().setSelected(selected);
	}

	@Override
	public boolean isSelected() {
		return getItemDelegate().isSelected();
	}

}
