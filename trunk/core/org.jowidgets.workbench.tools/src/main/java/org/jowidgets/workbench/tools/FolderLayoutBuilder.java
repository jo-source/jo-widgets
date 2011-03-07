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
import org.jowidgets.workbench.api.IFolderLayout;
import org.jowidgets.workbench.api.IViewLayout;
import org.jowidgets.workbench.toolkit.api.IFolderLayoutBuilder;
import org.jowidgets.workbench.toolkit.api.IViewLayoutBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

public class FolderLayoutBuilder implements IFolderLayoutBuilder {

	private final IFolderLayoutBuilder builder;

	public FolderLayoutBuilder() {
		this(builder());
	}

	public FolderLayoutBuilder(final String id) {
		this(builder(id));
	}

	public FolderLayoutBuilder(final IFolderLayoutBuilder builder) {
		super();
		this.builder = builder;
	}

	@Override
	public IFolderLayoutBuilder setId(final String id) {
		return builder.setId(id);
	}

	@Override
	public IFolderLayoutBuilder setLabel(final String label) {
		return builder.setLabel(label);
	}

	@Override
	public IFolderLayoutBuilder setTooltip(final String toolTiptext) {
		return builder.setTooltip(toolTiptext);
	}

	@Override
	public IFolderLayoutBuilder setIcon(final IImageConstant icon) {
		return builder.setIcon(icon);
	}

	@Override
	public IFolderLayoutBuilder setGroupId(final String groupId) {
		return builder.setGroupId(groupId);
	}

	@Override
	public IFolderLayoutBuilder setDetachable(final boolean detachable) {
		return builder.setDetachable(detachable);
	}

	@Override
	public IFolderLayoutBuilder setViewsCloseable(final boolean viewsCloseable) {
		return builder.setViewsCloseable(viewsCloseable);
	}

	@Override
	public IFolderLayoutBuilder setViews(final List<? extends IViewLayout> views) {
		return builder.setViews(views);
	}

	@Override
	public IFolderLayoutBuilder setViews(final IViewLayout... views) {
		return builder.setViews(views);
	}

	@Override
	public IFolderLayoutBuilder addView(final IViewLayout view) {
		return builder.addView(view);
	}

	@Override
	public IFolderLayoutBuilder setViews(final IViewLayoutBuilder... viewsBuilder) {
		return builder.setViews(viewsBuilder);
	}

	@Override
	public IFolderLayoutBuilder addView(final IViewLayoutBuilder viewBuilder) {
		return builder.addView(viewBuilder);
	}

	@Override
	public IFolderLayout build() {
		return builder.build();
	}

	public static IFolderLayoutBuilder builder() {
		return WorkbenchToolkit.getLayoutBuilderFactory().folder();
	}

	public static IFolderLayoutBuilder builder(final String id) {
		return builder().setId(id);
	}

}
