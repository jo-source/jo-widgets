/*
 * Copyright (c) 2014, MGrossmann
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

package org.jowidgets.impl.command;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.IDefaultActionBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;

abstract class AbstractDefaultActionBuilder implements IDefaultActionBuilder {

	private String text;

	private final IActionBuilder original;

	AbstractDefaultActionBuilder() {
		this.original = Toolkit.getActionBuilderFactory().create();
	}

	IActionBuilder getOriginal() {
		return original;
	}

	@Override
	public final IDefaultActionBuilder setText(final String text) {
		original.setText(text);
		this.text = text;
		return this;
	}

	@Override
	public final IDefaultActionBuilder setToolTipText(final String toolTipText) {
		original.setToolTipText(toolTipText);
		return this;
	}

	@Override
	public final IDefaultActionBuilder setIcon(final IImageConstant icon) {
		original.setIcon(icon);
		return this;
	}

	@Override
	public final IDefaultActionBuilder setMnemonic(final Character mnemonic) {
		original.setMnemonic(mnemonic);
		return this;
	}

	@Override
	public final IDefaultActionBuilder setMnemonic(final char mnemonic) {
		original.setMnemonic(mnemonic);
		return this;
	}

	@Override
	public final IDefaultActionBuilder setAccelerator(final Accelerator accelerator) {
		original.setAccelerator(accelerator);
		return this;
	}

	@Override
	public final IDefaultActionBuilder setAccelerator(final char key, final Modifier... modifier) {
		original.setAccelerator(key, modifier);
		return this;
	}

	@Override
	public final IDefaultActionBuilder setAccelerator(final VirtualKey virtualKey, final Modifier... modifier) {
		original.setAccelerator(virtualKey, modifier);
		return this;
	}

	@Override
	public IAction build() {
		return doBuild(original);
	}

	String getText() {
		return text;
	}

	protected abstract IAction doBuild(IActionBuilder superBuilder);

}
