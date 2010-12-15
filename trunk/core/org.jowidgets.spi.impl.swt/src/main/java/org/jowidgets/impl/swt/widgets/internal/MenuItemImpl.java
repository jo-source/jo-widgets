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
package org.jowidgets.impl.swt.widgets.internal;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MenuItem;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.widgets.controler.impl.ActionObservable;
import org.jowidgets.impl.swt.image.SwtImageRegistry;
import org.jowidgets.impl.swt.util.ModifierConvert;
import org.jowidgets.spi.widgets.IMenuItemSpi;

public class MenuItemImpl extends ActionObservable implements IMenuItemSpi, IToolTipTextProvider {

	private final MenuItem menuItem;
	private String text;
	private String tooltipText;
	private String acceleratorText;
	private Character mnemonic;

	public MenuItemImpl(final MenuItem menuItem) {
		this.menuItem = menuItem;

		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				fireActionPerformed();
			}
		});

	}

	@Override
	public MenuItem getUiReference() {
		return menuItem;
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		menuItem.setImage(SwtImageRegistry.getInstance().getImage(icon));
	}

	@Override
	public void setText(final String text) {
		this.text = text;
		setCombienedText();
	}

	@Override
	public void setToolTipText(final String tooltipText) {
		this.tooltipText = tooltipText;
	}

	@Override
	public String getToolTipText() {
		return tooltipText;
	}

	public void setAccelerator(final Accelerator accelerator) {
		try {
			final int modfifier = ModifierConvert.convert(accelerator.getModifier());
			getUiReference().setAccelerator(accelerator.getKey() + modfifier);
		}
		catch (final NoSuchMethodError error) {
			//TODO RWT does not support accelerators
			return;
		}
		this.acceleratorText = ModifierConvert.acceleratorText(accelerator.getModifier()) + accelerator.getKey();
		setCombienedText();

	}

	@Override
	public void setMnemonic(final char mnemonic) {
		this.mnemonic = Character.valueOf(mnemonic);
		setCombienedText();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		menuItem.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return menuItem.isEnabled();
	}

	private void setCombienedText() {
		final StringBuilder combiened = new StringBuilder();
		if (text != null) {
			combiened.append(text.replace("&", "&&"));
		}
		if (mnemonic != null) {
			final int index = combiened.indexOf(mnemonic.toString());
			if (index != -1) {
				combiened.insert(index, '&');
			}
		}
		if (acceleratorText != null) {
			combiened.append('\t');
			combiened.append(acceleratorText);
		}
		menuItem.setText(combiened.toString());
	}

}
