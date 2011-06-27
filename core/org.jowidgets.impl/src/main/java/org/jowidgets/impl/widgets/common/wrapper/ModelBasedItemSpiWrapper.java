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

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.IItemCommon;
import org.jowidgets.impl.base.delegate.ItemDelegate;
import org.jowidgets.spi.widgets.IItemSpi;

public class ModelBasedItemSpiWrapper extends WidgetSpiWrapper implements IItemCommon {

	private final ItemDelegate itemDelegate;

	public ModelBasedItemSpiWrapper(final IItemSpi component, final ItemDelegate itemDelegate) {
		super(component);
		this.itemDelegate = itemDelegate;
	}

	@Override
	public IItemSpi getWidget() {
		return (IItemSpi) super.getWidget();
	}

	protected ItemDelegate getItemDelegate() {
		return itemDelegate;
	}

	@Override
	public void setText(final String text) {
		itemDelegate.setText(text);
	}

	@Override
	public void setToolTipText(final String toolTipText) {
		itemDelegate.setToolTipText(toolTipText);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		itemDelegate.setIcon(icon);
	}

	@Override
	public String getText() {
		return itemDelegate.getText();
	}

	@Override
	public String getToolTipText() {
		return itemDelegate.getToolTipText();
	}

	public IImageConstant getIcon() {
		return itemDelegate.getIcon();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		itemDelegate.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return itemDelegate.isEnabled();
	}

}
