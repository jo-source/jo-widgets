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

package org.jowidgets.workbench.impl.rcp.internal;

import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IComponentNodeContext;
import org.jowidgets.workbench.api.ILayout;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.api.IWorkbenchContext;
import org.jowidgets.workbench.impl.rcp.internal.part.PartSupport;

public final class ComponentContext implements IComponentContext {

	private final IComponentNodeContext componentTreeNodeContext;
	private ILayout perspective;
	private IComponent component;

	public ComponentContext(final IComponentNodeContext componentTreeNodeContext) {
		this.componentTreeNodeContext = componentTreeNodeContext;
	}

	@Override
	public IComponentNodeContext getComponentNodeContext() {
		return componentTreeNodeContext;
	}

	public ILayout getPerspective() {
		return perspective;
	}

	@Override
	public void setLayout(final ILayout layout) {
		if (perspective != layout) {
			perspective = layout;
			final WorkbenchContext workbenchContext = (WorkbenchContext) componentTreeNodeContext.getWorkbenchContext();
			if (component != null && workbenchContext.getCurrentComponent() == component) {
				// re-set perspective
				if (layout == null) {
					PartSupport.getInstance().showEmptyPerspective();
				}
				else {
					PartSupport.getInstance().showPerspective((ComponentNodeContext) componentTreeNodeContext, this, layout);
				}
			}
		}
	}

	@Override
	public void resetLayout(final ILayout layout) {
		PartSupport.getInstance().resetPerspective((ComponentNodeContext) componentTreeNodeContext, this, layout);
	}

	@Override
	public void removeView(final IView view) {
		PartSupport.getInstance().closeView(view);
	}

	@Override
	public IWorkbenchApplicationContext getWorkbenchApplicationContext() {
		return componentTreeNodeContext.getWorkbenchApplicationContext();
	}

	@Override
	public IWorkbenchContext getWorkbenchContext() {
		return getWorkbenchApplicationContext().getWorkbenchContext();
	}

	public void setComponent(final IComponent component) {
		this.component = component;
	}

	public IComponent getComponent() {
		return component;
	}

}
