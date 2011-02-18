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

import org.jowidgets.api.command.IAction;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.tools.controler.ActionObservable;

abstract class AbstractActionItemModelImpl extends ItemModelImpl {

	private final ActionObservable actionObservable;

	private IAction action;

	protected AbstractActionItemModelImpl() {
		this(null, null, null, null, null, null, true, null);
	}

	protected AbstractActionItemModelImpl(
		final String id,
		final String text,
		final String toolTipText,
		final IImageConstant icon,
		final Accelerator accelerator,
		final Character mnemonic,
		final boolean enabled,
		final IAction action) {
		super(id, text, toolTipText, icon, accelerator, mnemonic, enabled);

		this.actionObservable = new ActionObservable();
		this.action = action;
	}

	protected void setContent(final AbstractActionItemModelImpl source) {
		super.setContent(source);
		this.action = source.getAction();
	}

	public IAction getAction() {
		return action;
	}

	public void setAction(final IAction action) {
		this.action = action;
		fireItemChanged();
	}

	@Override
	public String getText() {
		if (action != null) {
			return action.getText();
		}
		else {
			return super.getText();
		}
	}

	@Override
	public String getToolTipText() {
		if (action != null) {
			return action.getToolTipText();
		}
		else {
			return super.getToolTipText();
		}
	}

	@Override
	public IImageConstant getIcon() {
		if (action != null) {
			return action.getIcon();
		}
		else {
			return super.getIcon();
		}
	}

	@Override
	public Accelerator getAccelerator() {
		if (action != null) {
			return action.getAccelerator();
		}
		else {
			return super.getAccelerator();
		}
	}

	@Override
	public Character getMnemonic() {
		if (action != null) {
			return action.getMnemonic();
		}
		else {
			return super.getMnemonic();
		}
	}

	@Override
	public boolean isEnabled() {
		if (action != null) {
			return action.isEnabled();
		}
		else {
			return super.isEnabled();
		}
	}

	public void addActionListener(final IActionListener listener) {
		actionObservable.addActionListener(listener);
	}

	public void removeActionListener(final IActionListener listener) {
		actionObservable.removeActionListener(listener);
	}

	public void actionPerformed() {
		actionObservable.fireActionPerformed();
	}

}
