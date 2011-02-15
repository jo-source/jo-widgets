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

package org.jowidgets.impl.base.delegate;

import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IItemModelListener;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.impl.widgets.common.wrapper.invoker.IItemSpiInvoker;
import org.jowidgets.util.Assert;
import org.jowidgets.util.NullCompatibleEquivalence;

public class ItemDelegate {

	private final IItemSpiInvoker widget;
	private final IItemModelListener itemModelListener;

	private String text;
	private String toolTipText;
	private IImageConstant icon;
	private Accelerator accelerator;
	private Character mnemonic;
	private boolean enabled;

	private IItemModel model;

	public ItemDelegate(final IItemSpiInvoker widget, final IItemModel model) {
		Assert.paramNotNull(model, "model");

		this.itemModelListener = new IItemModelListener() {
			@Override
			public void itemChanged() {
				updateFromModel();
			}
		};

		this.widget = widget;
		this.model = model;
		this.enabled = true;
		updateFromModel();
	}

	public IItemSpiInvoker getWidget() {
		return widget;
	}

	public IItemModel getModel() {
		return model;
	}

	public void setText(final String text) {
		setTextValue(text);
		unRegisterModel();
		model.setText(text);
		registerModel();
	}

	public void setToolTipText(final String toolTipText) {
		setToolTipTextValue(toolTipText);
		unRegisterModel();
		model.setToolTipText(toolTipText);
		registerModel();
	}

	public void setIcon(final IImageConstant icon) {
		setIconValue(icon);
		unRegisterModel();
		model.setIcon(icon);
		registerModel();
	}

	public void setAccelerator(final Accelerator accelerator) {
		setAcceleratorValue(accelerator);
		unRegisterModel();
		model.setAccelerator(accelerator);
		registerModel();
	}

	public void setMnemonic(final Character mnemonic) {
		setMnemonicValue(mnemonic);
		unRegisterModel();
		model.setMnemonic(mnemonic);
		registerModel();
	}

	public void setEnabled(final boolean enabled) {
		setEnabledValue(enabled);
		unRegisterModel();
		model.setEnabled(enabled);
		registerModel();
	}

	public String getText() {
		return text;
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public IImageConstant getIcon() {
		return icon;
	}

	public Accelerator getAccelerator() {
		return accelerator;
	}

	public Character getMnemonic() {
		return mnemonic;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setModel(final IItemModel model) {
		if (this.model != null) {
			model.removeItemModelListener(itemModelListener);
		}
		this.model = model;
		updateFromModel();
		registerModel();
	}

	protected final void registerModel() {
		model.addItemModelListener(itemModelListener);
	}

	protected final void unRegisterModel() {
		model.removeItemModelListener(itemModelListener);
	}

	protected void updateFromModel() {
		setTextValue(model.getText());
		setToolTipTextValue(model.getToolTipText());
		setIconValue(model.getIcon());
		setAcceleratorValue(model.getAccelerator());
		setMnemonicValue(model.getMnemonic());
		setEnabledValue(model.isEnabled());
	}

	private void setTextValue(final String text) {
		if (!NullCompatibleEquivalence.equals(this.text, text)) {
			this.text = text;
			getWidget().setText(text);
		}
	}

	private void setToolTipTextValue(final String toolTipText) {
		if (!NullCompatibleEquivalence.equals(this.toolTipText, toolTipText)) {
			this.toolTipText = toolTipText;
			getWidget().setToolTipText(toolTipText);
		}
	}

	private void setIconValue(final IImageConstant icon) {
		if (model.getIcon() != icon) {
			this.icon = icon;
			getWidget().setIcon(icon);
		}
	}

	private void setAcceleratorValue(final Accelerator accelerator) {
		if (!NullCompatibleEquivalence.equals(this.accelerator, accelerator)) {
			this.accelerator = accelerator;
			getWidget().setAccelerator(accelerator);
		}
	}

	private void setMnemonicValue(final Character mnemonic) {
		if (!NullCompatibleEquivalence.equals(this.mnemonic, mnemonic)) {
			this.mnemonic = mnemonic;
			getWidget().setMnemonic(mnemonic);
		}
	}

	private void setEnabledValue(final boolean enabled) {
		if (this.enabled != enabled) {
			this.enabled = enabled;
			getWidget().setEnabled(enabled);
		}
	}

}
