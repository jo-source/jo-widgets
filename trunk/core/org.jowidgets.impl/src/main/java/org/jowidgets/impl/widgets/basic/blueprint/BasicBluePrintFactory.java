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

import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.image.IImageConstant;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.IIconBluePrint;
import org.jowidgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenienceRegistry;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultsInitializerRegistry;
import org.jowidgets.api.widgets.blueprint.factory.IBasicBluePrintFactory;
import org.jowidgets.impl.convert.DefaultObjectStringConverter;
import org.jowidgets.impl.convert.DefaultTypeConverter;

public class BasicBluePrintFactory extends BasicSimpleBluePrintFactory implements IBasicBluePrintFactory {

	public BasicBluePrintFactory(
		final ISetupBuilderConvenienceRegistry setupBuilderConvenienceRegistry,
		final IDefaultsInitializerRegistry defaultInitializerRegistry) {
		super(setupBuilderConvenienceRegistry, defaultInitializerRegistry);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////some convenience methods starting here///////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public final IFrameBluePrint frame(final String title) {
		return frame().setTitle(title);
	}

	@Override
	public final IFrameBluePrint frame(final String title, final IImageConstant icon) {
		return frame(title).setIcon(icon);
	}

	@Override
	public final IDialogBluePrint dialog(final String title) {
		return dialog().setTitle(title);
	}

	@Override
	public final IDialogBluePrint dialog(final String title, final IImageConstant icon) {
		return dialog(title).setIcon(icon);
	}

	@Override
	public final ICompositeBluePrint compositeWithBorder() {
		return composite().setBorder();
	}

	@Override
	public final ICompositeBluePrint composite(final String borderTitle) {
		return composite().setBorder(borderTitle);
	}

	@Override
	public final IButtonBluePrint button(final String text) {
		return button().setText(text);
	}

	@Override
	public final IButtonBluePrint button(final String text, final String toolTipText) {
		return button(text).setToolTipText(toolTipText);
	}

	@Override
	public final IIconBluePrint icon(final IImageConstant icon) {
		return icon().setIcon(icon);
	}

	@Override
	public final ITextLabelBluePrint textLabel(final String text) {
		return textLabel().setText(text);
	}

	@Override
	public final ITextLabelBluePrint textLabel(final String text, final String tooltipText) {
		return textLabel(text).setToolTipText(tooltipText);
	}

	@Override
	public final IComboBoxBluePrint<String> comboBox() {
		return comboBox(DefaultTypeConverter.STRING_CONVERTER);
	}

	@Override
	public final IComboBoxBluePrint<String> comboBox(final String... elements) {
		return comboBox().setElements(elements);
	}

	@Override
	public final IComboBoxSelectionBluePrint<String> comboBoxSelection() {
		return comboBoxSelection(DefaultTypeConverter.STRING_CONVERTER);
	}

	@Override
	public final IComboBoxSelectionBluePrint<String> comboBoxSelection(final String... elements) {
		return comboBoxSelection().setElements(elements);
	}

	@Override
	public final <ENUM_TYPE extends Enum<?>> IComboBoxSelectionBluePrint<ENUM_TYPE> comboBoxSelection(
		final ENUM_TYPE... enumValues) {
		final IObjectStringConverter<ENUM_TYPE> converter = DefaultObjectStringConverter.getInstance();
		return comboBoxSelection(converter).setElements(enumValues);
	}

	@Override
	public final IScrollCompositeBluePrint scrollCompositeWithBorder() {
		return scrollComposite().setBorder();
	}

	@Override
	public final IScrollCompositeBluePrint scrollComposite(final String borderTitle) {
		return scrollComposite().setBorder(borderTitle);
	}

}
