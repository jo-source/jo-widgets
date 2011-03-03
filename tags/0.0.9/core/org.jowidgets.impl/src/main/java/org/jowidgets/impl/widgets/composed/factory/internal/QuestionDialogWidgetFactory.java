/*
 * Copyright (c) 2010, Michael Grossmann
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
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.widgets.composed.factory.internal;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IQuestionDialog;
import org.jowidgets.api.widgets.blueprint.IDialogBluePrint;
import org.jowidgets.api.widgets.descriptor.IQuestionDialogDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.impl.widgets.composed.QuestionDialogWidget;

public class QuestionDialogWidgetFactory implements IWidgetFactory<IQuestionDialog, IQuestionDialogDescriptor> {

	private final IGenericWidgetFactory genericWidgetFactory;

	public QuestionDialogWidgetFactory(final IGenericWidgetFactory genericWidgetFactory) {
		this.genericWidgetFactory = genericWidgetFactory;
	}

	@Override
	public IQuestionDialog create(final Object parentUiReference, final IQuestionDialogDescriptor descriptor) {
		final IDialogBluePrint dialogBp = Toolkit.getBluePrintFactory().dialog().setTitle(descriptor.getTitle());
		dialogBp.setIcon(descriptor.getTitleIcon()).setResizable(false).setCloseable(false);
		final IFrame dialogWidget = genericWidgetFactory.create(parentUiReference, dialogBp);

		if (dialogWidget == null) {
			throw new IllegalStateException("Could not create widget with descriptor interface class '"
				+ IQuestionDialogDescriptor.class
				+ "' from '"
				+ IGenericWidgetFactory.class.getName()
				+ "'");
		}

		return new QuestionDialogWidget(dialogWidget, descriptor);
	}
}
