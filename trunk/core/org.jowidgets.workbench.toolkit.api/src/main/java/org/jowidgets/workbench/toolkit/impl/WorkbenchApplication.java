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

package org.jowidgets.workbench.toolkit.impl;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;

class WorkbenchApplication extends ComponentNodeContainer implements IWorkbenchApplication {

	private final IWorkbenchApplicationModel model;

	WorkbenchApplication(final IWorkbenchApplicationModel model) {
		super(model);
		this.model = model;
	}

	@Override
	public void onContextInitialize(final IWorkbenchApplicationContext context) {
		super.initialize(context);
	}

	@Override
	public String getId() {
		return model.getId();
	}

	@Override
	public String getLabel() {
		return model.getLabel();
	}

	@Override
	public String getTooltip() {
		return model.getTooltip();
	}

	@Override
	public IImageConstant getIcon() {
		return model.getIcon();
	}

	@Override
	public void onActiveStateChanged(final boolean active) {
		if (model.getLifecycleCallback() != null) {
			model.getLifecycleCallback().onActiveStateChanged(active);
		}
	}

	@Override
	public void onVisibleStateChanged(final boolean visible) {
		if (model.getLifecycleCallback() != null) {
			model.getLifecycleCallback().onVisibleStateChanged(visible);
		}
	}

	@Override
	public void onClose(final IVetoable vetoable) {
		if (model.getLifecycleCallback() != null) {
			model.getLifecycleCallback().onClose(vetoable);
		}
	}

}
