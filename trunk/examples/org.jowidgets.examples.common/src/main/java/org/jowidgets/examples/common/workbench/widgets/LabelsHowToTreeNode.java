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

package org.jowidgets.examples.common.workbench.widgets;

import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractComponentNode;
import org.jowidgets.examples.common.workbench.widgets.views.LabelKitchensinkView;
import org.jowidgets.examples.common.workbench.widgets.views.LabelWithIconView;
import org.jowidgets.examples.common.workbench.widgets.views.LabelWithTextAndIconView;
import org.jowidgets.examples.common.workbench.widgets.views.LabelWithTextView;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IComponentNodeContext;

public class LabelsHowToTreeNode extends AbstractComponentNode {

	public static final String ID = LabelsHowToTreeNode.class.getName();

	public LabelsHowToTreeNode() {
		super(ID, "Labels", null, SilkIcons.FOLDER);
	}

	@Override
	public void onContextInitialize(final IComponentNodeContext context) {
		context.add(new SingleViewTreeNode(LabelWithTextView.class, LabelWithTextView.ID, LabelWithTextView.DEFAULT_LABEL));

		context.add(new SingleViewTreeNode(LabelWithIconView.class, LabelWithIconView.ID, LabelWithIconView.DEFAULT_LABEL));

		context.add(new SingleViewTreeNode(
			LabelWithTextAndIconView.class,
			LabelWithTextAndIconView.ID,
			LabelWithTextAndIconView.DEFAULT_LABEL));

		context.add(new SingleViewTreeNode(
			LabelKitchensinkView.class,
			LabelKitchensinkView.ID,
			LabelKitchensinkView.DEFAULT_LABEL));

	}

	@Override
	public IComponent createComponent(final IComponentContext context) {
		return null;
	}

}
