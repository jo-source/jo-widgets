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

import org.jowidgets.impl.spi.blueprint.IButtonBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ICheckBoxBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IComboBoxBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IComboBoxSelectionBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ICompositeBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IDialogBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IFrameBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IIconBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IScrollCompositeBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ISeparatorBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ITextFieldBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ITextLabelBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IToggleButtonBluePrintSpi;
import org.jowidgets.impl.spi.descriptor.IButtonDescriptorSpi;
import org.jowidgets.impl.spi.descriptor.ICheckBoxDescriptorSpi;
import org.jowidgets.impl.spi.descriptor.IComboBoxDescriptorSpi;
import org.jowidgets.impl.spi.descriptor.IComboBoxSelectionDescriptorSpi;
import org.jowidgets.impl.spi.descriptor.ICompositeDescriptorSpi;
import org.jowidgets.impl.spi.descriptor.IDialogDescriptorSpi;
import org.jowidgets.impl.spi.descriptor.IFrameDescriptorSpi;
import org.jowidgets.impl.spi.descriptor.IIconDescriptorSpi;
import org.jowidgets.impl.spi.descriptor.IScrollCompositeDescriptorSpi;
import org.jowidgets.impl.spi.descriptor.ISeparatorDescriptorSpi;
import org.jowidgets.impl.spi.descriptor.ITextFieldDescriptorSpi;
import org.jowidgets.impl.spi.descriptor.ITextLabelDescriptorSpi;
import org.jowidgets.impl.spi.descriptor.IToggleButtonDescriptorSpi;
import org.jowidgets.impl.widgets.blueprint.factory.AbstractBluePrintFactory;
import org.jowidgets.impl.widgets.common.blueprint.convenience.registry.CommonSetupConvenienceRegistry;
import org.jowidgets.impl.widgets.common.blueprint.defaults.registry.CommonDefaultsInitializerRegistry;

public class SpiBluePrintFactory extends AbstractBluePrintFactory implements ISpiBluePrintFactory {

	public SpiBluePrintFactory() {
		super(new CommonSetupConvenienceRegistry(), new CommonDefaultsInitializerRegistry());
	}

	@Override
	public IFrameBluePrintSpi frame() {
		return createProxy(IFrameBluePrintSpi.class, IFrameDescriptorSpi.class);
	}

	@Override
	public IDialogBluePrintSpi dialog() {
		return createProxy(IDialogBluePrintSpi.class, IDialogDescriptorSpi.class);
	}

	@Override
	public ICompositeBluePrintSpi composite() {
		return createProxy(ICompositeBluePrintSpi.class, ICompositeDescriptorSpi.class);
	}

	@Override
	public IScrollCompositeBluePrintSpi scrollComposite() {
		return createProxy(IScrollCompositeBluePrintSpi.class, IScrollCompositeDescriptorSpi.class);
	}

	@Override
	public ITextLabelBluePrintSpi textLabel() {
		return createProxy(ITextLabelBluePrintSpi.class, ITextLabelDescriptorSpi.class);
	}

	@Override
	public IIconBluePrintSpi icon() {
		return createProxy(IIconBluePrintSpi.class, IIconDescriptorSpi.class);
	}

	@Override
	public ISeparatorBluePrintSpi separator() {
		return createProxy(ISeparatorBluePrintSpi.class, ISeparatorDescriptorSpi.class);
	}

	@Override
	public ITextFieldBluePrintSpi textField() {
		return createProxy(ITextFieldBluePrintSpi.class, ITextFieldDescriptorSpi.class);
	}

	@Override
	public IButtonBluePrintSpi button() {
		return createProxy(IButtonBluePrintSpi.class, IButtonDescriptorSpi.class);
	}

	@Override
	public ICheckBoxBluePrintSpi checkBox() {
		return createProxy(ICheckBoxBluePrintSpi.class, ICheckBoxDescriptorSpi.class);
	}

	@Override
	public IToggleButtonBluePrintSpi toggleButton() {
		return createProxy(IToggleButtonBluePrintSpi.class, IToggleButtonDescriptorSpi.class);
	}

	@Override
	public <INPUT_TYPE> IComboBoxBluePrintSpi<INPUT_TYPE> comboBox() {
		return createProxy(IComboBoxBluePrintSpi.class, IComboBoxDescriptorSpi.class);
	}

	@Override
	public <INPUT_TYPE> IComboBoxSelectionBluePrintSpi<INPUT_TYPE> comboBoxSelection() {
		return createProxy(IComboBoxSelectionBluePrintSpi.class, IComboBoxSelectionDescriptorSpi.class);
	}

}
