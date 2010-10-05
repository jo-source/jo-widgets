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
package org.jo.widgets.api.widgets.blueprint.factory;

import org.jo.widgets.api.image.IImageConstant;
import org.jo.widgets.api.widgets.blueprint.IButtonBluePrint;
import org.jo.widgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jo.widgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jo.widgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jo.widgets.api.widgets.blueprint.IDialogBluePrint;
import org.jo.widgets.api.widgets.blueprint.IIconBluePrint;
import org.jo.widgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jo.widgets.api.widgets.blueprint.ILabelBluePrint;
import org.jo.widgets.api.widgets.blueprint.IRootWindowBluePrint;
import org.jo.widgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jo.widgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jo.widgets.api.widgets.blueprint.ITextSeparatorBluePrint;

public interface IBluePrintFactory extends ICoreBluePrintFactory {

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////// some input fields
	// here/////////////////////////////////////////////////7
	// ///////////////////////////////////////////////////////////////////////////////////////////////

	IInputFieldBluePrint<String> inputFieldString();

	IInputFieldBluePrint<Long> inputFieldLongNumber();

	IInputFieldBluePrint<Short> inputFieldShortNumber();

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////some convenience methods starting
	// here///////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////

	IRootWindowBluePrint rootWindow(final String title);

	IRootWindowBluePrint rootWindow(final String title, final IImageConstant icon);

	IDialogBluePrint dialog(final String title);

	IDialogBluePrint dialog(final String title, final IImageConstant icon);

	ICompositeBluePrint compositeWithBorder();

	ICompositeBluePrint composite(final String borderTitle);

	IScrollCompositeBluePrint scrollCompositeWithBorder();

	IScrollCompositeBluePrint scrollComposite(final String borderTitle);

	IButtonBluePrint button(final String text);

	IButtonBluePrint button(final String text, final String toolTipText);

	IIconBluePrint icon(final IImageConstant icon);

	ILabelBluePrint label(final IImageConstant icon);

	ILabelBluePrint label(final IImageConstant icon, final String text);

	ILabelBluePrint label(final IImageConstant icon, final String text, final String toolTiptext);

	ITextLabelBluePrint textLabel(final String text);

	ITextLabelBluePrint textLabel(final String text, final String tooltipText);

	ITextSeparatorBluePrint textSeparator(final String text);

	ITextSeparatorBluePrint textSeparator(final String text, final String tooltipText);

	IComboBoxBluePrint<String> comboBox();

	IComboBoxBluePrint<String> comboBox(String... elements);

	IComboBoxSelectionBluePrint<String> comboBoxSelection();

	IComboBoxSelectionBluePrint<String> comboBoxSelection(String... elements);
}
