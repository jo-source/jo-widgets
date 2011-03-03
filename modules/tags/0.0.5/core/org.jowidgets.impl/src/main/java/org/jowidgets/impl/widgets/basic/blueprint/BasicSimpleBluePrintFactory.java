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
package org.jowidgets.impl.widgets.basic.blueprint;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.ICheckBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.IIconBluePrint;
import org.jowidgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ISeparatorBluePrint;
import org.jowidgets.api.widgets.blueprint.ISplitCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IToggleButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenienceRegistry;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultsInitializerRegistry;
import org.jowidgets.api.widgets.blueprint.factory.IBasicSimpleBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.IButtonDescriptor;
import org.jowidgets.api.widgets.descriptor.ICheckBoxDescriptor;
import org.jowidgets.api.widgets.descriptor.IComboBoxDescriptor;
import org.jowidgets.api.widgets.descriptor.IComboBoxSelectionDescriptor;
import org.jowidgets.api.widgets.descriptor.ICompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.IDialogDescriptor;
import org.jowidgets.api.widgets.descriptor.IFrameDescriptor;
import org.jowidgets.api.widgets.descriptor.IIconDescriptor;
import org.jowidgets.api.widgets.descriptor.IScrollCompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.ISeparatorDescriptor;
import org.jowidgets.api.widgets.descriptor.ISplitCompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextFieldDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jowidgets.api.widgets.descriptor.IToggleButtonDescriptor;
import org.jowidgets.impl.base.blueprint.factory.AbstractBluePrintFactory;

public class BasicSimpleBluePrintFactory extends AbstractBluePrintFactory implements IBasicSimpleBluePrintFactory {

	public BasicSimpleBluePrintFactory(
		final ISetupBuilderConvenienceRegistry setupBuilderConvenienceRegistry,
		final IDefaultsInitializerRegistry defaultInitializerRegistry) {
		super(setupBuilderConvenienceRegistry, defaultInitializerRegistry);
	}

	@Override
	public final IFrameBluePrint frame() {
		return createProxy(IFrameBluePrint.class, IFrameDescriptor.class);
	}

	@Override
	public final IDialogBluePrint dialog() {
		return createProxy(IDialogBluePrint.class, IDialogDescriptor.class);
	}

	@Override
	public final ICompositeBluePrint composite() {
		return createProxy(ICompositeBluePrint.class, ICompositeDescriptor.class);
	}

	@Override
	public final IScrollCompositeBluePrint scrollComposite() {
		return createProxy(IScrollCompositeBluePrint.class, IScrollCompositeDescriptor.class);
	}

	@Override
	public ISplitCompositeBluePrint splitComposite() {
		return createProxy(ISplitCompositeBluePrint.class, ISplitCompositeDescriptor.class);
	}

	@Override
	public final ITextLabelBluePrint textLabel() {
		return createProxy(ITextLabelBluePrint.class, ITextLabelDescriptor.class);
	}

	@Override
	public final IIconBluePrint icon() {
		return createProxy(IIconBluePrint.class, IIconDescriptor.class);
	}

	@Override
	public final ISeparatorBluePrint separator() {
		return createProxy(ISeparatorBluePrint.class, ISeparatorDescriptor.class);
	}

	@Override
	public final ITextFieldBluePrint textField() {
		return createProxy(ITextFieldBluePrint.class, ITextFieldDescriptor.class);
	}

	@Override
	public final IButtonBluePrint button() {
		return createProxy(IButtonBluePrint.class, IButtonDescriptor.class);
	}

	@Override
	public final ICheckBoxBluePrint checkBox() {
		return createProxy(ICheckBoxBluePrint.class, ICheckBoxDescriptor.class);
	}

	@Override
	public final IToggleButtonBluePrint toggleButton() {
		return createProxy(IToggleButtonBluePrint.class, IToggleButtonDescriptor.class);
	}

	@Override
	public final <INPUT_TYPE> IComboBoxBluePrint<INPUT_TYPE> comboBox(final IConverter<INPUT_TYPE> converter) {

		final IComboBoxBluePrint<INPUT_TYPE> result = createProxy(IComboBoxBluePrint.class, IComboBoxDescriptor.class);

		return result.setObjectStringConverter(converter).setStringObjectConverter(converter);
	}

	@Override
	public final <INPUT_TYPE> IComboBoxSelectionBluePrint<INPUT_TYPE> comboBoxSelection(
		final IObjectStringConverter<INPUT_TYPE> objectStringConverter) {

		final IComboBoxSelectionBluePrint<INPUT_TYPE> result = createProxy(
				IComboBoxSelectionBluePrint.class,
				IComboBoxSelectionDescriptor.class);

		return result.setObjectStringConverter(objectStringConverter);
	}

}
