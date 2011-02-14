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

import java.util.UUID;

import org.jowidgets.api.model.item.IItemModelBuilder;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;

public abstract class AbstractItemModelBuilder<INSTANCE_TYPE, ITEM_TYPE> implements IItemModelBuilder<INSTANCE_TYPE, ITEM_TYPE> {

	private String id;
	private String text;
	private String toolTipText;
	private IImageConstant icon;
	private Accelerator accelerator;
	private Character mnemonic;
	private boolean enabled;

	public AbstractItemModelBuilder() {
		this.id = UUID.randomUUID().toString();
		this.enabled = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public INSTANCE_TYPE setId(final String id) {
		this.id = id;
		return (INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public INSTANCE_TYPE setText(final String text) {
		this.text = text;
		return (INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public INSTANCE_TYPE setToolTipText(final String toolTipText) {
		this.toolTipText = toolTipText;
		return (INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public INSTANCE_TYPE setIcon(final IImageConstant icon) {
		this.icon = icon;
		return (INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public INSTANCE_TYPE setAccelerator(final Accelerator accelerator) {
		this.accelerator = accelerator;
		return (INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public INSTANCE_TYPE setMnemonic(final Character mnemonic) {
		this.mnemonic = mnemonic;
		return (INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public INSTANCE_TYPE setMnemonic(final char mnemonic) {
		this.mnemonic = Character.valueOf(mnemonic);
		return (INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public INSTANCE_TYPE setEnabled(final boolean enabled) {
		this.enabled = enabled;
		return (INSTANCE_TYPE) this;
	}

	protected String getId() {
		return id;
	}

	protected String getText() {
		return text;
	}

	protected String getToolTipText() {
		return toolTipText;
	}

	protected IImageConstant getIcon() {
		return icon;
	}

	protected Accelerator getAccelerator() {
		return accelerator;
	}

	protected Character getMnemonic() {
		return mnemonic;
	}

	protected boolean isEnabled() {
		return enabled;
	}

}
