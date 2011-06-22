/*
 * Copyright (c) 2011, M. Woelker, H. Westphal
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

import java.util.Collections;
import java.util.List;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.workbench.api.ClosePolicy;
import org.jowidgets.workbench.api.IViewLayout;
import org.jowidgets.workbench.api.ViewScope;

public final class RcpView implements IViewLayout {

	private final String viewId;
	private boolean detachable = true;

	public RcpView(final String viewId) {
		this.viewId = viewId;
	}

	@Override
	public String getId() {
		return viewId;
	}

	@Override
	public boolean isDetachable() {
		return detachable;
	}

	public void setDetachable(final boolean detachable) {
		this.detachable = detachable;
	}

	@Override
	public boolean isHidden() {
		return false;
	}

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
	public ViewScope getScope() {
		return ViewScope.WORKBENCH;
	}

	@Override
	public ClosePolicy getClosePolicy() {
		return null;
	}

	@Override
	public List<String> getFolderWhitelist() {
		return Collections.emptyList();
	}

	@Override
	public List<String> getFolderBlacklist() {
		return Collections.emptyList();
	}

}
