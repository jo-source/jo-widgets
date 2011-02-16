/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.tools.model.item;

import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IItemModelListener;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.types.Modifier;

abstract class AbstractItemModelWrapper implements IItemModel {

	private final IItemModel itemModel;

	public AbstractItemModelWrapper(final IItemModel itemModel) {
		super();
		this.itemModel = itemModel;
	}

	public IItemModel getItemModel() {
		return itemModel;
	}

	@Override
	public String getId() {
		return itemModel.getId();
	}

	@Override
	public String getText() {
		return itemModel.getText();
	}

	@Override
	public String getToolTipText() {
		return itemModel.getToolTipText();
	}

	@Override
	public IImageConstant getIcon() {
		return itemModel.getIcon();
	}

	@Override
	public Accelerator getAccelerator() {
		return itemModel.getAccelerator();
	}

	@Override
	public Character getMnemonic() {
		return itemModel.getMnemonic();
	}

	@Override
	public boolean isEnabled() {
		return itemModel.isEnabled();
	}

	@Override
	public void setId(final String id) {
		itemModel.setId(id);
	}

	@Override
	public void setText(final String text) {
		itemModel.setText(text);
	}

	@Override
	public void setToolTipText(final String toolTipText) {
		itemModel.setToolTipText(toolTipText);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		itemModel.setIcon(icon);
	}

	@Override
	public void setAccelerator(final Accelerator accelerator) {
		itemModel.setAccelerator(accelerator);
	}

	@Override
	public void setAccelerator(final char key, final Modifier... modifier) {
		itemModel.setAccelerator(key, modifier);
	}

	@Override
	public void setMnemonic(final Character mnemonic) {
		itemModel.setMnemonic(mnemonic);
	}

	@Override
	public void setMnemonic(final char mnemonic) {
		itemModel.setMnemonic(mnemonic);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		itemModel.setEnabled(enabled);
	}

	@Override
	public void addItemModelListener(final IItemModelListener listener) {
		itemModel.addItemModelListener(listener);
	}

	@Override
	public void removeItemModelListener(final IItemModelListener listener) {
		itemModel.removeItemModelListener(listener);
	}

}
