/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of the jo-widgets.org nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
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
package org.jo.widgets.api.widgets.blueprint.factory.impl;

import org.jo.widgets.api.convert.IConverter;
import org.jo.widgets.api.convert.IObjectStringConverter;
import org.jo.widgets.api.widgets.blueprint.IButtonBluePrint;
import org.jo.widgets.api.widgets.blueprint.ICheckBoxBluePrint;
import org.jo.widgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jo.widgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jo.widgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jo.widgets.api.widgets.blueprint.IDialogBluePrint;
import org.jo.widgets.api.widgets.blueprint.IIconBluePrint;
import org.jo.widgets.api.widgets.blueprint.IInputCompositeBluePrint;
import org.jo.widgets.api.widgets.blueprint.IInputDialogBluePrint;
import org.jo.widgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jo.widgets.api.widgets.blueprint.ILabelBluePrint;
import org.jo.widgets.api.widgets.blueprint.IRootWindowBluePrint;
import org.jo.widgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jo.widgets.api.widgets.blueprint.ISeparatorBluePrint;
import org.jo.widgets.api.widgets.blueprint.ITextFieldBluePrint;
import org.jo.widgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jo.widgets.api.widgets.blueprint.ITextSeparatorBluePrint;
import org.jo.widgets.api.widgets.blueprint.IToggleButtonBluePrint;
import org.jo.widgets.api.widgets.blueprint.IValidationLabelBluePrint;
import org.jo.widgets.api.widgets.blueprint.factory.ICoreBluePrintFactory;
import org.jo.widgets.api.widgets.blueprint.factory.impl.proxy.BluePrintProxyProvider;
import org.jo.widgets.api.widgets.content.IInputContentCreator;
import org.jo.widgets.api.widgets.descriptor.IButtonDescriptor;
import org.jo.widgets.api.widgets.descriptor.ICheckBoxDescriptor;
import org.jo.widgets.api.widgets.descriptor.IComboBoxDescriptor;
import org.jo.widgets.api.widgets.descriptor.IComboBoxSelectionDescriptor;
import org.jo.widgets.api.widgets.descriptor.ICompositeDescriptor;
import org.jo.widgets.api.widgets.descriptor.IDialogDescriptor;
import org.jo.widgets.api.widgets.descriptor.IIconDescriptor;
import org.jo.widgets.api.widgets.descriptor.IInputCompositeDescriptor;
import org.jo.widgets.api.widgets.descriptor.IInputDialogDescriptor;
import org.jo.widgets.api.widgets.descriptor.IInputFieldDescriptor;
import org.jo.widgets.api.widgets.descriptor.ILabelDescriptor;
import org.jo.widgets.api.widgets.descriptor.IRootWindowDescriptor;
import org.jo.widgets.api.widgets.descriptor.IScrollCompositeDescriptor;
import org.jo.widgets.api.widgets.descriptor.ISeparatorDescriptor;
import org.jo.widgets.api.widgets.descriptor.ITextFieldDescriptor;
import org.jo.widgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jo.widgets.api.widgets.descriptor.ITextSeparatorDescriptor;
import org.jo.widgets.api.widgets.descriptor.IToggleButtonDescriptor;
import org.jo.widgets.api.widgets.descriptor.IValidationLabelDescriptor;
import org.jo.widgets.util.Assert;

public final class CoreBluePrintFactory implements ICoreBluePrintFactory {

	public CoreBluePrintFactory() {
	}

	@Override
	public IRootWindowBluePrint rootWindow() {
		return new BluePrintProxyProvider<IRootWindowBluePrint>(
				IRootWindowBluePrint.class, IRootWindowDescriptor.class)
				.getBluePrint();
	}

	@Override
	public IDialogBluePrint dialog() {
		return new BluePrintProxyProvider<IDialogBluePrint>(
				IDialogBluePrint.class, IDialogDescriptor.class).getBluePrint();
	}

	@Override
	public ICompositeBluePrint composite() {
		return new BluePrintProxyProvider<ICompositeBluePrint>(
				ICompositeBluePrint.class, ICompositeDescriptor.class)
				.getBluePrint();
	}

	@Override
	public IScrollCompositeBluePrint scrollComposite() {
		return new BluePrintProxyProvider<IScrollCompositeBluePrint>(
				IScrollCompositeBluePrint.class,
				IScrollCompositeDescriptor.class).getBluePrint();
	}

	@Override
	public ITextLabelBluePrint textLabel() {
		return new BluePrintProxyProvider<ITextLabelBluePrint>(
				ITextLabelBluePrint.class, ITextLabelDescriptor.class)
				.getBluePrint();
	}

	@Override
	public ILabelBluePrint label() {
		return new BluePrintProxyProvider<ILabelBluePrint>(
				ILabelBluePrint.class, ILabelDescriptor.class).getBluePrint();
	}

	@Override
	public IValidationLabelBluePrint validationLabel() {
		return new BluePrintProxyProvider<IValidationLabelBluePrint>(
				IValidationLabelBluePrint.class,
				IValidationLabelDescriptor.class).getBluePrint();
	}

	@Override
	public IIconBluePrint icon() {
		return new BluePrintProxyProvider<IIconBluePrint>(IIconBluePrint.class,
				IIconDescriptor.class).getBluePrint();
	}

	@Override
	public ISeparatorBluePrint separator() {
		return new BluePrintProxyProvider<ISeparatorBluePrint>(
				ISeparatorBluePrint.class, ISeparatorDescriptor.class)
				.getBluePrint();
	}

	@Override
	public ITextSeparatorBluePrint textSeparator() {
		return new BluePrintProxyProvider<ITextSeparatorBluePrint>(
				ITextSeparatorBluePrint.class, ITextSeparatorDescriptor.class)
				.getBluePrint();
	}

	@Override
	public ITextFieldBluePrint textField() {
		return new BluePrintProxyProvider<ITextFieldBluePrint>(
				ITextFieldBluePrint.class, ITextFieldDescriptor.class)
				.getBluePrint();
	}

	@Override
	public IButtonBluePrint button() {
		return new BluePrintProxyProvider<IButtonBluePrint>(
				IButtonBluePrint.class, IButtonDescriptor.class).getBluePrint();
	}

	@Override
	public ICheckBoxBluePrint checkBox() {
		return new BluePrintProxyProvider<ICheckBoxBluePrint>(
				ICheckBoxBluePrint.class, ICheckBoxDescriptor.class)
				.getBluePrint();
	}

	@Override
	public IToggleButtonBluePrint toggleButton() {
		return new BluePrintProxyProvider<IToggleButtonBluePrint>(
				IToggleButtonBluePrint.class, IToggleButtonDescriptor.class)
				.getBluePrint();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <INPUT_TYPE> IInputDialogBluePrint<INPUT_TYPE> inputDialog(
			final IInputContentCreator<INPUT_TYPE> contentCreator) {
		Assert.paramNotNull(contentCreator, "contentCreator");

		final IInputDialogBluePrint<INPUT_TYPE> inputDialogBluePrint;
		inputDialogBluePrint = (IInputDialogBluePrint<INPUT_TYPE>) new BluePrintProxyProvider(
				IInputDialogBluePrint.class, IInputDialogDescriptor.class)
				.getBluePrint();

		inputDialogBluePrint.setContentCreator(contentCreator);
		return inputDialogBluePrint;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <INPUT_TYPE> IInputCompositeBluePrint<INPUT_TYPE> inputComposite(
			final IInputContentCreator<INPUT_TYPE> contentCreator) {
		Assert.paramNotNull(contentCreator, "contentCreator");

		final IInputCompositeBluePrint<INPUT_TYPE> inputCompositeBluePrint = (IInputCompositeBluePrint<INPUT_TYPE>) new BluePrintProxyProvider(
				IInputCompositeBluePrint.class, IInputCompositeDescriptor.class)
				.getBluePrint();

		inputCompositeBluePrint.setContentCreator(contentCreator);
		return inputCompositeBluePrint;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField(
			final IConverter<INPUT_TYPE> converter) {
		Assert.paramNotNull(converter, "converter");

		final IInputFieldBluePrint<INPUT_TYPE> result = (IInputFieldBluePrint<INPUT_TYPE>) new BluePrintProxyProvider(
				IInputFieldBluePrint.class, IInputFieldDescriptor.class)
				.getBluePrint();
		return result.setConverter(converter);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <INPUT_TYPE> IComboBoxBluePrint<INPUT_TYPE> comboBox(
			final IConverter<INPUT_TYPE> converter) {

		final IComboBoxBluePrint<INPUT_TYPE> result = (IComboBoxBluePrint<INPUT_TYPE>) new BluePrintProxyProvider(
				IComboBoxBluePrint.class, IComboBoxDescriptor.class)
				.getBluePrint();

		return result.setObjectStringConverter(converter)
				.setStringObjectConverter(converter);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <INPUT_TYPE> IComboBoxSelectionBluePrint<INPUT_TYPE> comboBoxSelection(
			final IObjectStringConverter<INPUT_TYPE> objectStringConverter) {

		final IComboBoxSelectionBluePrint<INPUT_TYPE> result = (IComboBoxSelectionBluePrint<INPUT_TYPE>) new BluePrintProxyProvider(
				IComboBoxSelectionBluePrint.class,
				IComboBoxSelectionDescriptor.class).getBluePrint();

		return result.setObjectStringConverter(objectStringConverter);
	}

}
