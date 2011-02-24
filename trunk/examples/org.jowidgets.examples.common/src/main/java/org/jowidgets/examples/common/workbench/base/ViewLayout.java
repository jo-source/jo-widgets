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

package org.jowidgets.examples.common.workbench.base;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IViewLayout;

public class ViewLayout implements IViewLayout {

	private final String id;
	private final String label;
	private final String tooltip;
	private final IImageConstant icon;
	private final boolean hidden;
	private final boolean detachable;
	private final List<String> folderWhitelist;
	private final List<String> folderBlacklist;

	public ViewLayout(final String id, final String label, final String tooltip, final IImageConstant icon) {
		this(id, label, tooltip, icon, false, true, new LinkedList<String>(), new LinkedList<String>());
	}

	public ViewLayout(final String id, final String label, final IImageConstant icon) {
		this(id, label, null, icon, false, true, new LinkedList<String>(), new LinkedList<String>());
	}

	public ViewLayout(final String id, final String label) {
		this(id, label, null, null, false, true, new LinkedList<String>(), new LinkedList<String>());
	}

	public ViewLayout(
		final String id,
		final String label,
		final String tooltip,
		final IImageConstant icon,
		final boolean hidden,
		final boolean detachable,
		final List<String> folderWhitelist,
		final List<String> folderBlacklist) {

		Assert.paramNotEmpty(id, "id");
		Assert.paramNotEmpty(label, "label");
		Assert.paramNotNull(folderWhitelist, "folderWhitelist");
		Assert.paramNotNull(folderBlacklist, "folderBlacklist");

		this.id = id;
		this.label = label;
		this.tooltip = tooltip;
		this.icon = icon;
		this.hidden = hidden;
		this.detachable = detachable;
		this.folderWhitelist = folderWhitelist;
		this.folderBlacklist = folderBlacklist;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getTooltip() {
		return tooltip;
	}

	@Override
	public IImageConstant getIcon() {
		return icon;
	}

	@Override
	public boolean isHidden() {
		return hidden;
	}

	@Override
	public boolean isDetachable() {
		return detachable;
	}

	@Override
	public List<String> getFolderWhitelist() {
		return new LinkedList<String>(folderWhitelist);
	}

	@Override
	public List<String> getFolderBlacklist() {
		return new LinkedList<String>(folderBlacklist);
	}

}
