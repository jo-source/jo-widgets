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

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IFolderLayout;
import org.jowidgets.workbench.api.IViewLayout;

final class FolderLayout extends WorkbenchPart implements IFolderLayout {

	private final String id;
	private final String groupId;
	private final List<? extends IViewLayout> views;
	private final boolean isDetachable;
	private final boolean viewsCloseable;

	FolderLayout(
		final String id,
		final String groupId,
		final String label,
		final String tooltip,
		final IImageConstant icon,
		final List<? extends IViewLayout> views,
		final boolean isDetachable,
		final boolean viewsCloseable) {
		super(label, tooltip, icon);

		Assert.paramNotEmpty(id, "id");
		Assert.paramNotNull(views, "views");

		this.id = id;
		if (groupId != null) {
			this.groupId = groupId;
		}
		else {
			this.groupId = id;
		}
		this.views = new LinkedList<IViewLayout>(views);
		this.isDetachable = isDetachable;
		this.viewsCloseable = viewsCloseable;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getGroupId() {
		return groupId;
	}

	@Override
	public List<? extends IViewLayout> getViews() {
		return new LinkedList<IViewLayout>(views);
	}

	@Override
	public boolean isDetachable() {
		return isDetachable;
	}

	@Override
	public boolean getViewsCloseable() {
		return viewsCloseable;
	}

}
