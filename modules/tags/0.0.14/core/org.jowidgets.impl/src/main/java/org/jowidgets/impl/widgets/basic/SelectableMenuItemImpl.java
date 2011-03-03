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

package org.jowidgets.impl.widgets.basic;

import org.jowidgets.api.model.item.IMenuItemModel;
import org.jowidgets.api.model.item.ISelectableItemModel;
import org.jowidgets.api.model.item.ISelectableMenuItemModel;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.ISelectableMenuItem;
import org.jowidgets.api.widgets.descriptor.setup.ISelectableItemSetup;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.impl.base.delegate.SelectableItemDelegate;
import org.jowidgets.impl.widgets.common.wrapper.SelectableMenuItemSpiWrapper;
import org.jowidgets.spi.widgets.ISelectableMenuItemSpi;

public class SelectableMenuItemImpl extends SelectableMenuItemSpiWrapper implements ISelectableMenuItem {

	private final IMenu parent;

	public SelectableMenuItemImpl(
		final IMenu parent,
		final ISelectableMenuItemSpi actionMenuItemSpi,
		final ISelectableItemSetup setup,
		final SelectableItemDelegate itemDelegate) {
		super(actionMenuItemSpi, itemDelegate);

		this.parent = parent;

		setText(setup.getText());
		setToolTipText(setup.getToolTipText());
		setIcon(setup.getIcon());
		setSelected(setup.isSelected());

		if (setup.getAccelerator() != null) {
			setAccelerator(setup.getAccelerator());
		}

		if (setup.getMnemonic() != null) {
			setMnemonic(setup.getMnemonic().charValue());
		}
	}

	@Override
	public IMenu getParent() {
		return parent;
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		//Do not set the icon for selectable item, because they have a default icon
	}

	@Override
	public void setModel(final ISelectableMenuItemModel model) {
		super.getItemDelegate().setModel(model);
	}

	@Override
	public void setModel(final IMenuItemModel model) {
		if (model instanceof ISelectableMenuItemModel) {
			setModel((ISelectableMenuItemModel) model);
		}
		else {
			throw new IllegalArgumentException("Model must be a '" + ISelectableItemModel.class.getName() + "'");
		}
	}

	@Override
	public ISelectableMenuItemModel getModel() {
		return getItemDelegate().getModel();
	}

}
