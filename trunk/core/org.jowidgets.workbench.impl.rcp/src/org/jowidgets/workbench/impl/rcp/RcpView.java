/*
 * Copyright (c) 2011, M. Grossmann, M. Woelker, H. Westphal
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of jo-widgets.org nor the
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

package org.jowidgets.workbench.impl.rcp;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;

public final class RcpView implements IView {

	private final String viewId;

	public RcpView(final String viewId) {
		this.viewId = viewId;
	}

	@Override
	public void onActiveStateChanged(final boolean active) {}

	@Override
	public void onVisibleStateChanged(final boolean visible) {}

	@Override
	public void onClose(final IVetoable vetoable) {}

	@Override
	public String getLabel() {
		return null;
	}

	@Override
	public String getTooltip() {
		return null;
	}

	@Override
	public IImageConstant getIcon() {
		return null;
	}

	@Override
	public void initialize(final IViewContext context) {}

	@Override
	public String getId() {
		return viewId;
	}

	@Override
	public void onHiddenStateChanged(final boolean hidden) {}

	@Override
	public IMenuModel createToolBarMenu() {
		return null;
	}

	@Override
	public IToolBarModel createToolBar() {
		return null;
	}

}
