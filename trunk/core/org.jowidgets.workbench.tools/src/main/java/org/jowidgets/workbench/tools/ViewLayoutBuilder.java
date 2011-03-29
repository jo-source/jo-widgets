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

package org.jowidgets.workbench.tools;

import java.util.List;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.workbench.api.IViewLayout;
import org.jowidgets.workbench.api.ViewScope;
import org.jowidgets.workbench.toolkit.api.IViewLayoutBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

public class ViewLayoutBuilder implements IViewLayoutBuilder {

	private final IViewLayoutBuilder builder;

	public ViewLayoutBuilder() {
		this(builder());
	}

	public ViewLayoutBuilder(final String id) {
		this(builder(id));
	}

	public ViewLayoutBuilder(final IViewLayoutBuilder builder) {
		super();
		this.builder = builder;
	}

	@Override
	public final IViewLayoutBuilder setLabel(final String label) {
		builder.setLabel(label);
		return this;
	}

	@Override
	public final IViewLayoutBuilder setTooltip(final String toolTiptext) {
		builder.setTooltip(toolTiptext);
		return this;
	}

	@Override
	public final IViewLayoutBuilder setIcon(final IImageConstant icon) {
		builder.setIcon(icon);
		return this;
	}

	@Override
	public final IViewLayoutBuilder setId(final String id) {
		builder.setId(id);
		return this;
	}

	@Override
	public final IViewLayoutBuilder setHidden(final boolean hidden) {
		builder.setHidden(hidden);
		return this;
	}

	@Override
	public final IViewLayoutBuilder setDetachable(final boolean detachable) {
		builder.setDetachable(detachable);
		return this;
	}

	@Override
	public IViewLayoutBuilder setScope(final ViewScope scope) {
		builder.setScope(scope);
		return this;
	}

	@Override
	public final IViewLayoutBuilder setFolderWhitelist(final List<String> folderWhiteList) {
		builder.setFolderWhitelist(folderWhiteList);
		return this;
	}

	@Override
	public final IViewLayoutBuilder setFolderWhitelist(final String... folderWhiteList) {
		builder.setFolderWhitelist(folderWhiteList);
		return this;
	}

	@Override
	public final IViewLayoutBuilder addToFolderWhitelist(final String folder) {
		builder.addToFolderWhitelist(folder);
		return this;
	}

	@Override
	public final IViewLayoutBuilder setFolderBlacklist(final List<String> folderBlackList) {
		builder.setFolderBlacklist(folderBlackList);
		return this;
	}

	@Override
	public final IViewLayoutBuilder setFolderBlacklist(final String... folderBlackList) {
		builder.setFolderBlacklist(folderBlackList);
		return this;
	}

	@Override
	public final IViewLayoutBuilder addToFolderBlacklist(final String folder) {
		builder.addToFolderBlacklist(folder);
		return this;
	}

	@Override
	public final IViewLayout build() {
		return builder.build();
	}

	public static IViewLayoutBuilder builder() {
		return WorkbenchToolkit.getLayoutBuilderFactory().view();
	}

	public static IViewLayoutBuilder builder(final String id) {
		return builder().setId(id);
	}

}
