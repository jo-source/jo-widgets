/*
 * Copyright (c) 2014, MGrossmann
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

package org.jowidgets.impl.command;

import org.jowidgets.api.command.ITreeExpansionActionBuilder;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.util.IFilter;

final class ExpandCollapseTreeActionBuilder extends TreeExpansionActionBuilder {

	private static final IMessage EXPAND_ALL_MESSAGE = Messages.getMessage("ExpandCollapseTreeActionBuilder.expandAllLabel");
	private static final IMessage EXPAND_ALL_BOUND_MESSAGE = Messages.getMessage("ExpandCollapseTreeActionBuilder.expandAllBoundLabel");

	ExpandCollapseTreeActionBuilder(final ITreeContainer tree) {
		super(tree, ExpansionMode.EXPAND_COLLAPSE);

		setText(EXPAND_ALL_MESSAGE.get());
		setBoundPivotLevelText(EXPAND_ALL_BOUND_MESSAGE.get());
		setIcon(IconsSmall.EXPAND_COLLAPSE_ALL);
	}

	@Override
	public ITreeExpansionActionBuilder addFilter(final IFilter<ITreeNode> filter) {
		throw new UnsupportedOperationException(
			"Filters are not supported for this action at the moment. Feel free to contribute a implementation");
	}

	@Override
	public ITreeExpansionActionBuilder setFilter(final IFilter<ITreeNode> filter) {
		throw new UnsupportedOperationException(
			"Filters are not supported for this action at the moment. Feel free to contribute a implementation");
	}
}
