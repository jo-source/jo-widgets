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
package org.jowidgets.impl.spi;

import org.jowidgets.impl.base.blueprint.factory.AbstractBluePrintFactory;
import org.jowidgets.impl.spi.blueprint.IButtonBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ICheckBoxBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IComboBoxBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IComboBoxSelectionBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ICompositeBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IDialogBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IFrameBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IIconBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IProgressBarBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IScrollCompositeBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ISeparatorBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ISplitCompositeBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ITabFolderBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ITabItemBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ITableBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ITextAreaBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ITextFieldBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ITextLabelBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IToggleButtonBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ITreeBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.defaults.registry.SpiDefaultsInitializerRegistry;
import org.jowidgets.impl.widgets.common.blueprint.convenience.registry.CommonSetupConvenienceRegistry;

public class SpiBluePrintFactory extends AbstractBluePrintFactory implements ISpiBluePrintFactory {

	public SpiBluePrintFactory() {
		super(new CommonSetupConvenienceRegistry(), new SpiDefaultsInitializerRegistry());
	}

	@Override
	public IFrameBluePrintSpi frame() {
		return createProxy(IFrameBluePrintSpi.class);
	}

	@Override
	public IDialogBluePrintSpi dialog() {
		return createProxy(IDialogBluePrintSpi.class);
	}

	@Override
	public ICompositeBluePrintSpi composite() {
		return createProxy(ICompositeBluePrintSpi.class);
	}

	@Override
	public IScrollCompositeBluePrintSpi scrollComposite() {
		return createProxy(IScrollCompositeBluePrintSpi.class);
	}

	@Override
	public ISplitCompositeBluePrintSpi splitComposite() {
		return createProxy(ISplitCompositeBluePrintSpi.class);
	}

	@Override
	public ITextLabelBluePrintSpi textLabel() {
		return createProxy(ITextLabelBluePrintSpi.class);
	}

	@Override
	public IIconBluePrintSpi icon() {
		return createProxy(IIconBluePrintSpi.class);
	}

	@Override
	public ISeparatorBluePrintSpi separator() {
		return createProxy(ISeparatorBluePrintSpi.class);
	}

	@Override
	public ITextFieldBluePrintSpi textField() {
		return createProxy(ITextFieldBluePrintSpi.class);
	}

	@Override
	public ITextAreaBluePrintSpi textArea() {
		return createProxy(ITextAreaBluePrintSpi.class);
	}

	@Override
	public IButtonBluePrintSpi button() {
		return createProxy(IButtonBluePrintSpi.class);
	}

	@Override
	public ICheckBoxBluePrintSpi checkBox() {
		return createProxy(ICheckBoxBluePrintSpi.class);
	}

	@Override
	public IToggleButtonBluePrintSpi toggleButton() {
		return createProxy(IToggleButtonBluePrintSpi.class);
	}

	@Override
	public IComboBoxBluePrintSpi comboBox() {
		return createProxy(IComboBoxBluePrintSpi.class);
	}

	@Override
	public IComboBoxSelectionBluePrintSpi comboBoxSelection() {
		return createProxy(IComboBoxSelectionBluePrintSpi.class);
	}

	@Override
	public IProgressBarBluePrintSpi progressBar() {
		return createProxy(IProgressBarBluePrintSpi.class);
	}

	@Override
	public ITabFolderBluePrintSpi tabFolder() {
		return createProxy(ITabFolderBluePrintSpi.class);
	}

	@Override
	public ITabItemBluePrintSpi tabItem() {
		return createProxy(ITabItemBluePrintSpi.class);
	}

	@Override
	public ITreeBluePrintSpi tree() {
		return createProxy(ITreeBluePrintSpi.class);
	}

	@Override
	public ITableBluePrintSpi table() {
		return createProxy(ITableBluePrintSpi.class);
	}

}
