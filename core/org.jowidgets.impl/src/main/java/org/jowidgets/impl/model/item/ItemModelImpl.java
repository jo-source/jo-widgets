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
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IItemModelListener;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.types.Modifier;

class ItemModelImpl implements IItemModel {

	private final Set<IItemModelListener> menuItemModelListeners;
	private String id;

	private String text;
	private String toolTipText;
	private IImageConstant icon;
	private Accelerator accelerator;
	private Character mnemonic;
	private boolean enabled;

	protected ItemModelImpl() {
		this(null, null, null, null, null, null, true);
	}

	protected ItemModelImpl(
		final String id,
		final String text,
		final String toolTipText,
		final IImageConstant icon,
		final Accelerator accelerator,
		final Character mnemonic,
		final boolean enabled) {

		this.menuItemModelListeners = new HashSet<IItemModelListener>();

		if (id == null) {
			this.id = UUID.randomUUID().toString();
		}
		else {
			this.id = id;
		}
		this.text = text;
		this.toolTipText = toolTipText;
		this.icon = icon;
		this.accelerator = accelerator;
		this.mnemonic = mnemonic;
		this.enabled = enabled;
	}

	@Override
	public IItemModel createCopy() {
		final ItemModelImpl result = new ItemModelImpl();
		result.setContent(this);
		return result;
	}

	protected void setContent(final IItemModel source) {
		this.id = source.getId();
		this.text = source.getText();
		this.toolTipText = source.getToolTipText();
		this.icon = source.getIcon();
		this.accelerator = source.getAccelerator();
		this.mnemonic = source.getMnemonic();
		this.enabled = source.isEnabled();
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
		fireItemChanged();
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
	public void setAccelerator(final char key, final Modifier... modifier) {
		setAccelerator(new Accelerator(key, modifier));
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
		final Set<IItemModelListener> brokenListeners = new HashSet<IItemModelListener>();
		for (final IItemModelListener listener : new LinkedList<IItemModelListener>(menuItemModelListeners)) {
			try {
				listener.itemChanged(this);
			}
			catch (final RuntimeException e) {
				//This is just a workaround for the case, that widgets will be
				//disposed in swt directly so the widget api does not get any information about that
				//If the model changes, an swt exception will be thrown then.
				//TODO MG fix this workaround
				if ("org.eclipse.swt.SWTException".equals(e.getClass().getName())) {
					brokenListeners.add(listener);
				}
				else {
					throw e;
				}
			}
		}
		menuItemModelListeners.removeAll(brokenListeners);
	}

	@Override
	public String toString() {
		return "ItemModel [id=" + id + ", text=" + text + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ItemModelImpl other = (ItemModelImpl) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		}
		else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
