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
import org.jo.widgets.api.convert.impl.DefaultObjectStringConverter;
import org.jo.widgets.api.convert.impl.DefaultTypeConverter;
import org.jo.widgets.api.image.IImageConstant;
import org.jo.widgets.api.look.Border;
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
import org.jo.widgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jo.widgets.api.widgets.blueprint.factory.ICoreBluePrintFactory;
import org.jo.widgets.api.widgets.content.IInputContentCreator;
import org.jo.widgets.util.Assert;

public final class BluePrintFactory implements IBluePrintFactory {

	private final ICoreBluePrintFactory coreFactory;

	public BluePrintFactory() {
		this(new CoreBluePrintFactory());
	}

	public BluePrintFactory(final ICoreBluePrintFactory coreFactory) {
		Assert.paramNotNull(coreFactory, "coreFactory");
		this.coreFactory = coreFactory;
	}

	@Override
	public IRootWindowBluePrint rootWindow() {
		return coreFactory.rootWindow();
	}

	@Override
	public IDialogBluePrint dialog() {
		return coreFactory.dialog();
	}

	@Override
	public ICompositeBluePrint composite() {
		return coreFactory.composite();
	}

	@Override
	public IScrollCompositeBluePrint scrollComposite() {
		return coreFactory.scrollComposite();
	}

	@Override
	public ITextLabelBluePrint textLabel() {
		return coreFactory.textLabel();
	}

	@Override
	public ILabelBluePrint label() {
		return coreFactory.label();
	}

	@Override
	public IValidationLabelBluePrint validationLabel() {
		return coreFactory.validationLabel();
	}

	@Override
	public IIconBluePrint icon() {
		return coreFactory.icon();
	}

	@Override
	public ISeparatorBluePrint separator() {
		return coreFactory.separator();
	}

	@Override
	public ITextSeparatorBluePrint textSeparator() {
		return coreFactory.textSeparator();
	}

	@Override
	public ITextFieldBluePrint textField() {
		return coreFactory.textField();
	}

	@Override
	public IButtonBluePrint button() {
		return coreFactory.button();
	}

	@Override
	public ICheckBoxBluePrint checkBox() {
		return coreFactory.checkBox();
	}

	@Override
	public IToggleButtonBluePrint toggleButton() {
		return coreFactory.toggleButton();
	}

	@Override
	public <INPUT_TYPE> IInputCompositeBluePrint<INPUT_TYPE> inputComposite(final IInputContentCreator<INPUT_TYPE> contentCreator) {
		return coreFactory.inputComposite(contentCreator);
	}

	@Override
	public <INPUT_TYPE> IInputDialogBluePrint<INPUT_TYPE> inputDialog(final IInputContentCreator<INPUT_TYPE> contentCreator) {
		return coreFactory.inputDialog(contentCreator);
	}

	@Override
	public <INPUT_TYPE> IInputFieldBluePrint<INPUT_TYPE> inputField(final IConverter<INPUT_TYPE> converter) {
		return coreFactory.inputField(converter);
	}

	@Override
	public <INPUT_TYPE> IComboBoxBluePrint<INPUT_TYPE> comboBox(final IConverter<INPUT_TYPE> converter) {
		return coreFactory.comboBox(converter);
	}

	@Override
	public <INPUT_TYPE> IComboBoxSelectionBluePrint<INPUT_TYPE> comboBoxSelection(
		final IObjectStringConverter<INPUT_TYPE> objectStringConverter) {
		return coreFactory.comboBoxSelection(objectStringConverter);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////input fields
	// here///////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public IInputFieldBluePrint<String> inputFieldString() {
		return coreFactory.inputField(DefaultTypeConverter.STRING_CONVERTER);
	}

	@Override
	public IInputFieldBluePrint<Long> inputFieldLongNumber() {
		return coreFactory.inputField(DefaultTypeConverter.LONG_CONVERTER);
	}

	@Override
	public IInputFieldBluePrint<Short> inputFieldShortNumber() {
		return coreFactory.inputField(DefaultTypeConverter.SHORT_CONVERTER);
	}

	@Override
	public IInputFieldBluePrint<Integer> inputFieldIntegerNumber() {
		return coreFactory.inputField(DefaultTypeConverter.INTEGER_CONVERTER);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////some convenience methods starting
	// here///////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public IRootWindowBluePrint rootWindow(final String title) {
		return rootWindow().setTitle(title);
	}

	@Override
	public IRootWindowBluePrint rootWindow(final String title, final IImageConstant icon) {
		return rootWindow(title).setIcon(icon);
	}

	@Override
	public IDialogBluePrint dialog(final String title) {
		return dialog().setTitle(title);
	}

	@Override
	public IDialogBluePrint dialog(final String title, final IImageConstant icon) {
		return dialog(title).setIcon(icon);
	}

	@Override
	public ICompositeBluePrint compositeWithBorder() {
		return composite().setBorder();
	}

	@Override
	public ICompositeBluePrint composite(final String borderTitle) {
		return composite().setBorder(borderTitle);
	}

	@Override
	public IScrollCompositeBluePrint scrollCompositeWithBorder() {
		return scrollComposite().setBorder();
	}

	@Override
	public IScrollCompositeBluePrint scrollComposite(final String borderTitle) {
		return scrollComposite().setBorder(new Border(borderTitle));
	}

	@Override
	public IButtonBluePrint button(final String text) {
		return button().setText(text);
	}

	@Override
	public IButtonBluePrint button(final String text, final String toolTipText) {
		return button(text).setToolTipText(toolTipText);
	}

	@Override
	public IIconBluePrint icon(final IImageConstant icon) {
		return icon().setIcon(icon);
	}

	@Override
	public ILabelBluePrint label(final IImageConstant icon) {
		return label().setIcon(icon);
	}

	@Override
	public ILabelBluePrint label(final IImageConstant icon, final String text) {
		return label().setIcon(icon).setText(text);
	}

	@Override
	public ILabelBluePrint label(final IImageConstant icon, final String text, final String toolTiptext) {
		return label().setIcon(icon).setText(text).setToolTipText(toolTiptext);
	}

	@Override
	public ITextLabelBluePrint textLabel(final String text) {
		return textLabel().setText(text);
	}

	@Override
	public ITextLabelBluePrint textLabel(final String text, final String tooltipText) {
		return textLabel(text).setToolTipText(tooltipText);
	}

	@Override
	public ITextSeparatorBluePrint textSeparator(final String text) {
		return textSeparator().setText(text);
	}

	@Override
	public ITextSeparatorBluePrint textSeparator(final String text, final String tooltipText) {
		return textSeparator(text).setToolTipText(tooltipText);
	}

	@Override
	public IComboBoxBluePrint<String> comboBox() {
		return comboBox(DefaultTypeConverter.STRING_CONVERTER);
	}

	@Override
	public IComboBoxBluePrint<String> comboBox(final String... elements) {
		return comboBox().setElements(elements);
	}

	@Override
	public IComboBoxSelectionBluePrint<String> comboBoxSelection() {
		return comboBoxSelection(DefaultTypeConverter.STRING_CONVERTER);
	}

	@Override
	public IComboBoxSelectionBluePrint<String> comboBoxSelection(final String... elements) {
		return comboBoxSelection().setElements(elements);
	}

	@Override
	public <ENUM_TYPE extends Enum<?>> IComboBoxSelectionBluePrint<ENUM_TYPE> comboBoxSelection(final ENUM_TYPE... enumValues) {
		final IObjectStringConverter<ENUM_TYPE> converter = DefaultObjectStringConverter.getInstance();
		return comboBoxSelection(converter).setElements(enumValues);
	}
}
