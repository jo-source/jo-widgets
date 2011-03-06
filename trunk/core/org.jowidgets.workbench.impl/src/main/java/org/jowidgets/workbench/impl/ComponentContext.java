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

package org.jowidgets.workbench.impl;

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IComponentTreeNode;
import org.jowidgets.workbench.api.ILayout;
import org.jowidgets.workbench.api.IView;

public final class ComponentContext implements IComponentContext {

	private final Map<String, LayoutContext> layoutContexts;
	private final Map<String, ILayout> layouts;

	private final ComponentTreeNodeContext treeNodeContext;
	private final IComponent component;

	private LayoutContext currentLayoutContext;
	private String currentLayout;
	private boolean active;

	public ComponentContext(final IComponentTreeNode treeNode, final ComponentTreeNodeContext treeNodeContext) {

		this.layoutContexts = new HashMap<String, LayoutContext>();
		this.layouts = new HashMap<String, ILayout>();

		this.treeNodeContext = treeNodeContext;
		this.active = false;

		//this must be the last invocation in constructor
		//remark that component may be null for empty tree nodes (e.g folders)
		this.component = treeNode.createComponent(this);
	}

	@Override
	public void setLayout(final ILayout layout) {
		if (layout != null) {
			currentLayout = layout.getId();
			layouts.put(layout.getId(), layout);
		}
		else {
			currentLayout = null;
		}
		if (active) {
			showCurrentLayout();
		}
	}

	@Override
	public void resetLayout(final ILayout layout) {
		//TODO MG implement reset
	}

	@Override
	public void addLayout(final ILayout layout) {
		//TODO MG implement add layout
	}

	@Override
	public void removeView(final IView view) {
		// TODO MG implement remove view
	}

	public void activate() {
		active = true;
		showCurrentLayout();
		if (component != null) {
			component.onActivation();
		}
	}

	private void showCurrentLayout() {
		if (currentLayout != null) {
			getCurrentControlLazy().setVisible(true);
		}
		else {
			if (currentLayoutContext != null) {
				currentLayoutContext.setVisible(false);
			}
			getWorkbenchContext().setEmptyContentVisible(true);
		}
	}

	private LayoutContext getCurrentControlLazy() {
		if (currentLayoutContext == null) {
			if (currentLayout != null) {
				currentLayoutContext = layoutContexts.get(currentLayout);
				if (currentLayoutContext == null) {
					currentLayoutContext = new LayoutContext(
						getWorkbenchContext().getWorkbenchContentContainer(),
						layouts.get(currentLayout),
						this);
					layoutContexts.put(currentLayout, currentLayoutContext);
				}
			}
		}
		return currentLayoutContext;
	}

	public VetoHolder deactivate() {
		final VetoHolder vetoHolder = new VetoHolder();
		if (component != null) {
			component.onDeactivation(vetoHolder);
			if (!vetoHolder.hasVeto()) {
				hideCurrentLayout();
				active = false;
			}
		}
		return vetoHolder;
	}

	private void hideCurrentLayout() {
		if (currentLayoutContext != null) {
			currentLayoutContext.setVisible(false);
		}
		else {
			getWorkbenchContext().setEmptyContentVisible(false);
		}
	}

	protected IComponent getComponent() {
		return component;
	}

	@Override
	public ComponentTreeNodeContext getComponentTreeNodeContext() {
		return treeNodeContext;
	}

	@Override
	public WorkbenchApplicationContext getWorkbenchApplicationContext() {
		return treeNodeContext.getWorkbenchApplicationContext();
	}

	@Override
	public WorkbenchContext getWorkbenchContext() {
		return getWorkbenchApplicationContext().getWorkbenchContext();
	}

}
