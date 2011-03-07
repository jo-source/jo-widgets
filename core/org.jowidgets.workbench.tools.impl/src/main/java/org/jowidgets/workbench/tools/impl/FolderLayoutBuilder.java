/*
 * Copyright (c) 2011, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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

package org.jowidgets.workbench.tools.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IFolderLayout;
import org.jowidgets.workbench.api.IViewLayout;
import org.jowidgets.workbench.tools.api.IFolderLayoutBuilder;
import org.jowidgets.workbench.tools.api.IViewLayoutBuilder;

class FolderLayoutBuilder extends WorkbenchPartBuilder<IFolderLayoutBuilder> implements IFolderLayoutBuilder {

	private final List<IViewLayout> views;

	private String id;
	private String groupId;
	private boolean detachable;
	private boolean viewsCloseable;

	FolderLayoutBuilder() {
		super();
		this.views = new LinkedList<IViewLayout>();
	}

	@Override
	public IFolderLayoutBuilder setId(final String id) {
		Assert.paramNotEmpty(id, "id");
		this.id = id;
		return this;
	}

	@Override
	public IFolderLayoutBuilder setGroupId(final String groupId) {
		Assert.paramNotEmpty(groupId, "groupId");
		this.groupId = groupId;
		return this;
	}

	@Override
	public IFolderLayoutBuilder setDetachable(final boolean detachable) {
		this.detachable = detachable;
		return this;
	}

	@Override
	public IFolderLayoutBuilder setViewsCloseable(final boolean viewsCloseable) {
		this.viewsCloseable = viewsCloseable;
		return this;
	}

	@Override
	public IFolderLayoutBuilder setViews(final List<? extends IViewLayout> views) {
		Assert.paramNotNull(views, "views");
		this.views.clear();
		this.views.addAll(views);
		return this;
	}

	@Override
	public IFolderLayoutBuilder setViews(final IViewLayout... views) {
		Assert.paramNotNull(views, "views");
		return setViews(Arrays.asList(views));
	}

	@Override
	public IFolderLayoutBuilder addView(final IViewLayout view) {
		Assert.paramNotNull(view, "view");
		this.views.add(view);
		return this;
	}

	@Override
	public IFolderLayoutBuilder setViews(final IViewLayoutBuilder... viewsBuilder) {
		Assert.paramNotNull(viewsBuilder, "viewsBuilder");
		this.views.clear();
		for (final IViewLayoutBuilder viewLayoutBuilder : viewsBuilder) {
			this.views.add(viewLayoutBuilder.build());
		}
		return this;
	}

	@Override
	public IFolderLayoutBuilder addView(final IViewLayoutBuilder viewBuilder) {
		return addView(viewBuilder.build());
	}

	@Override
	public IFolderLayout build() {
		return new FolderLayout(id, groupId, getLabel(), getTooltip(), getIcon(), views, detachable, viewsCloseable);
	}

}
