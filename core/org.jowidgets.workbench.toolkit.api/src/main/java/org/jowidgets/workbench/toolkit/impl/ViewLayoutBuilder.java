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

package org.jowidgets.workbench.toolkit.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IViewLayout;
import org.jowidgets.workbench.api.ViewScope;
import org.jowidgets.workbench.toolkit.api.IViewLayoutBuilder;

class ViewLayoutBuilder extends WorkbenchPartBuilder<IViewLayoutBuilder> implements IViewLayoutBuilder {

	private String id;
	private boolean hidden;
	private boolean detachable;
	private ViewScope scope;
	private final List<String> folderWhiteList;
	private final List<String> folderBlackList;

	ViewLayoutBuilder() {
		super();
		this.hidden = false;
		this.detachable = true;
		this.scope = ViewScope.COMPONENT;
		this.folderWhiteList = new LinkedList<String>();
		this.folderBlackList = new LinkedList<String>();
	}

	@Override
	public IViewLayoutBuilder setId(final String id) {
		Assert.paramNotEmpty(id, "id");
		this.id = id;
		return this;
	}

	@Override
	public IViewLayoutBuilder setHidden(final boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	@Override
	public IViewLayoutBuilder setDetachable(final boolean detachable) {
		this.detachable = detachable;
		return this;
	}

	@Override
	public IViewLayoutBuilder setScope(final ViewScope scope) {
		Assert.paramNotNull(scope, "scope");
		this.scope = scope;
		return this;
	}

	@Override
	public IViewLayoutBuilder setFolderWhitelist(final List<String> folderWhiteList) {
		Assert.paramNotNull(folderWhiteList, "folderWhiteList");
		this.folderWhiteList.clear();
		this.folderWhiteList.addAll(folderWhiteList);
		return this;
	}

	@Override
	public IViewLayoutBuilder setFolderWhitelist(final String... folderWhiteList) {
		Assert.paramNotNull(folderWhiteList, "folderWhiteList");
		return setFolderWhitelist(Arrays.asList(folderWhiteList));
	}

	@Override
	public IViewLayoutBuilder addToFolderWhitelist(final String folder) {
		Assert.paramNotEmpty(folder, "folder");
		this.folderWhiteList.add(folder);
		return this;
	}

	@Override
	public IViewLayoutBuilder setFolderBlacklist(final List<String> folderBlackList) {
		Assert.paramNotNull(folderBlackList, "folderBlackList");
		this.folderBlackList.clear();
		this.folderBlackList.addAll(folderBlackList);
		return this;
	}

	@Override
	public IViewLayoutBuilder setFolderBlacklist(final String... folderBlackList) {
		Assert.paramNotNull(folderBlackList, "folderBlackList");
		return setFolderBlacklist(Arrays.asList(folderBlackList));
	}

	@Override
	public IViewLayoutBuilder addToFolderBlacklist(final String folder) {
		Assert.paramNotEmpty(folder, "folder");
		this.folderBlackList.add(folder);
		return this;
	}

	@Override
	public IViewLayout build() {
		return new ViewLayout(
			id,
			getLabel(),
			getTooltip(),
			getIcon(),
			hidden,
			detachable,
			scope,
			folderWhiteList,
			folderBlackList);
	}

}
