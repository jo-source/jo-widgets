/*
 * Copyright (c) 2010, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
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

package org.jowidgets.impl.widgets.basic;

import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITextArea;
import org.jowidgets.api.widgets.descriptor.setup.ITextAreaSetup;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.common.wrapper.InputControlSpiWrapper;
import org.jowidgets.spi.widgets.ITextAreaSpi;

public class TextAreaImpl extends InputControlSpiWrapper implements ITextArea {

	private final ControlDelegate controlDelegate;

	public TextAreaImpl(final ITextAreaSpi textAreaSpi, final ITextAreaSetup setup) {
		super(textAreaSpi);

		this.controlDelegate = new ControlDelegate();

		if (setup.getText() != null) {
			setText(setup.getText());
		}

		setEditable(setup.isEditable());

		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);
	}

	@Override
	public ITextAreaSpi getWidget() {
		return (ITextAreaSpi) super.getWidget();
	}

	@Override
	public IContainer getParent() {
		return controlDelegate.getParent();
	}

	@Override
	public void setParent(final IContainer parent) {
		controlDelegate.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return controlDelegate.isReparentable();
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return new PopupMenuImpl(getWidget().createPopupMenu(), this);
	}

	@Override
	public String getText() {
		return getWidget().getText();
	}

	@Override
	public void setText(final String text) {
		getWidget().setText(text);
	}

	@Override
	public String getTooltipText() {
		return getWidget().getTooltipText();
	}

	@Override
	public void setTooltipText(final String tooltipText) {
		getWidget().setTooltipText(tooltipText);
	}

	@Override
	public void setSelection(final int start, final int end) {
		getWidget().setSelection(start, end);
	}

	@Override
	public void setCaretPosition(final int pos) {
		getWidget().setCaretPosition(pos);
	}

	@Override
	public int getCaretPosition() {
		return getWidget().getCaretPosition();
	}

	@Override
	public void selectAll() {
		final String text = getText();
		if (text != null) {
			setSelection(0, text.length());
		}
	}

	@Override
	public void scrollToCaretPosition() {
		getWidget().scrollToCaretPosition();
	}

}
