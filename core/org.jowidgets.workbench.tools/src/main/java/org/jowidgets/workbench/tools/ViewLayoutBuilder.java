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
	public IViewLayoutBuilder setLabel(final String label) {
		return builder.setLabel(label);
	}

	@Override
	public IViewLayoutBuilder setTooltip(final String toolTiptext) {
		return builder.setTooltip(toolTiptext);
	}

	@Override
	public IViewLayoutBuilder setIcon(final IImageConstant icon) {
		return builder.setIcon(icon);
	}

	@Override
	public IViewLayoutBuilder setId(final String id) {
		return builder.setId(id);
	}

	@Override
	public IViewLayoutBuilder setHidden(final boolean hidden) {
		return builder.setHidden(hidden);
	}

	@Override
	public IViewLayoutBuilder setDetachable(final boolean detachable) {
		return builder.setDetachable(detachable);
	}

	@Override
	public IViewLayoutBuilder setFolderWhitelist(final List<String> folderWhiteList) {
		return builder.setFolderWhitelist(folderWhiteList);
	}

	@Override
	public IViewLayoutBuilder setFolderWhitelist(final String... folderWhiteList) {
		return builder.setFolderWhitelist(folderWhiteList);
	}

	@Override
	public IViewLayoutBuilder addToFolderWhitelist(final String folder) {
		return builder.addToFolderWhitelist(folder);
	}

	@Override
	public IViewLayoutBuilder setFolderBlacklist(final List<String> folderBlackList) {
		return builder.setFolderBlacklist(folderBlackList);
	}

	@Override
	public IViewLayoutBuilder setFolderBlacklist(final String... folderBlackList) {
		return builder.setFolderBlacklist(folderBlackList);
	}

	@Override
	public IViewLayoutBuilder addToFolderBlacklist(final String folder) {
		return builder.addToFolderBlacklist(folder);
	}

	@Override
	public IViewLayout build() {
		return builder.build();
	}

	public static IViewLayoutBuilder builder() {
		return WorkbenchToolkit.getLayoutBuilderFactory().view();
	}

	public static IViewLayoutBuilder builder(final String id) {
		return builder().setId(id);
	}

}
