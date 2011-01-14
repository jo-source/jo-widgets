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

import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarToggleButton;
import org.jowidgets.api.widgets.descriptor.setup.IItemSetup;
import org.jowidgets.impl.widgets.common.wrapper.ToolBarToggleButtonSpiWrapper;
import org.jowidgets.spi.widgets.IToolBarToggleButtonSpi;

public class ToolBarToggleButtonImpl extends ToolBarToggleButtonSpiWrapper implements IToolBarToggleButton {

	private final IToolBar parent;

	public ToolBarToggleButtonImpl(
		final IToolBar parent,
		final IToolBarToggleButtonSpi toolBarToggleButtonSpi,
		final IItemSetup setup) {
		super(toolBarToggleButtonSpi);

		this.parent = parent;

		setText(setup.getText());
		setToolTipText(setup.getToolTipText());
		setIcon(setup.getIcon());

	}

	@Override
	public IToolBar getParent() {
		return parent;
	}

}
