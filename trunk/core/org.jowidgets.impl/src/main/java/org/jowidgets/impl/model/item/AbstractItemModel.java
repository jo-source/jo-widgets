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

package org.jowidgets.impl.model.item;

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IItemModelListener;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;

public abstract class AbstractItemModel implements IItemModel {

	private final Set<IItemModelListener> menuItemModelListeners;
	private String id;

	private String text;
	private String toolTipText;
	private IImageConstant icon;
	private Accelerator accelerator;
	private Character mnemonic;
	private boolean enabled;

	public AbstractItemModel(
		final String id,
		final String text,
		final String toolTipText,
		final IImageConstant icon,
		final Accelerator accelerator,
		final Character mnemonic,
		final boolean enabled) {

		this.menuItemModelListeners = new HashSet<IItemModelListener>();

		this.id = id;
		this.text = text;
		this.toolTipText = toolTipText;
		this.icon = icon;
		this.accelerator = accelerator;
		this.mnemonic = mnemonic;
		this.enabled = enabled;
	}

	@Override
	public final String getId() {
		return id;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getToolTipText() {
		return toolTipText;
	}

	@Override
	public IImageConstant getIcon() {
		return icon;
	}

	@Override
	public Accelerator getAccelerator() {
		return accelerator;
	}

	@Override
	public Character getMnemonic() {
		return mnemonic;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setId(final String id) {
		this.id = id;
	}

	@Override
	public final void setText(final String text) {
		this.text = text;
		fireItemChanged();
	}

	@Override
	public final void setToolTipText(final String toolTipText) {
		this.toolTipText = toolTipText;
		fireItemChanged();
	}

	@Override
	public final void setIcon(final IImageConstant icon) {
		this.icon = icon;
		fireItemChanged();
	}

	@Override
	public final void setAccelerator(final Accelerator accelerator) {
		this.accelerator = accelerator;
		fireItemChanged();
	}

	@Override
	public void setMnemonic(final char mnemonic) {
		setMnemonic(Character.valueOf(mnemonic));
	}

	@Override
	public final void setMnemonic(final Character mnemonic) {
		this.mnemonic = mnemonic;
		fireItemChanged();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
		fireItemChanged();
	}

	@Override
	public final void addItemModelListener(final IItemModelListener listener) {
		menuItemModelListeners.add(listener);
	}

	@Override
	public final void removeItemModelListener(final IItemModelListener listener) {
		menuItemModelListeners.remove(listener);
	}

	protected final void fireItemChanged() {
		for (final IItemModelListener listener : menuItemModelListeners) {
			listener.itemChanged();
		}
	}

}
