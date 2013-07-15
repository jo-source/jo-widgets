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

import java.util.Collection;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IInputDialog;
import org.jowidgets.api.widgets.blueprint.ICollectionInputControlBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.api.widgets.descriptor.ICollectionInputDialogDescriptor;
import org.jowidgets.api.widgets.descriptor.setup.ICollectionInputControlSetup;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.tools.layout.MigLayoutFactory;

public class CollectionInputDialogFactory<ELEMENT_TYPE> implements
		IWidgetFactory<IInputDialog<Collection<ELEMENT_TYPE>>, ICollectionInputDialogDescriptor<ELEMENT_TYPE>> {

	@Override
	public IInputDialog<Collection<ELEMENT_TYPE>> create(
		final Object parentUiReference,
		final ICollectionInputDialogDescriptor<ELEMENT_TYPE> descriptor) {

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		final IGenericWidgetFactory widgetFactory = Toolkit.getWidgetFactory();

		final IInputContentCreator<Collection<ELEMENT_TYPE>> inputCreatorBp = createContentCreator(
				bpf,
				descriptor.getCollectionInputControlSetup());

		final IInputDialogBluePrint<Collection<ELEMENT_TYPE>> dialogBp = bpf.inputDialog(inputCreatorBp);
		dialogBp.setSetup(descriptor).setContentCreator(inputCreatorBp);

		final IInputDialog<Collection<ELEMENT_TYPE>> dialogWidget = widgetFactory.create(parentUiReference, dialogBp);

		return dialogWidget;
	}

	private IInputContentCreator<Collection<ELEMENT_TYPE>> createContentCreator(
		final IBluePrintFactory bpf,
		final ICollectionInputControlSetup<ELEMENT_TYPE> setup) {

		final ICollectionInputControlBluePrint<ELEMENT_TYPE> inputControlBp = bpf.collectionInputControl(
				new ICustomWidgetCreator<IInputControl<ELEMENT_TYPE>>() {
					@Override
					public IInputControl<ELEMENT_TYPE> create(final ICustomWidgetFactory widgetFactory) {
						return setup.getElementWidgetCreator().create(widgetFactory);
					}
				}).setSetup(setup);

		return new IInputContentCreator<Collection<ELEMENT_TYPE>>() {

			private IInputControl<Collection<ELEMENT_TYPE>> control;

			@Override
			public void createContent(final IInputContentContainer container) {
				container.setLayout(MigLayoutFactory.growingInnerCellLayout());
				this.control = container.add(inputControlBp, MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
			}

			@Override
			public void setValue(final Collection<ELEMENT_TYPE> value) {
				control.setValue(value);
			}

			@Override
			public Collection<ELEMENT_TYPE> getValue() {
				return control.getValue();
			}

		};
	}
}
