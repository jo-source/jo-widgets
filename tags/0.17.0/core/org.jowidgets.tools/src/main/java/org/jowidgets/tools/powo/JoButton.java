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

package org.jowidgets.tools.powo;

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.descriptor.IButtonDescriptor;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.controller.IActionListener;

public class JoButton extends Control<IButton, IButtonBluePrint> implements IButton {

	private final Set<IActionListener> actionListeners;

	private IAction action;

	public JoButton(final IImageConstant icon, final String text) {
		this(Toolkit.getBluePrintFactory().button().setIcon(icon).setText(text));
	}

	public JoButton(final IImageConstant icon) {
		this(Toolkit.getBluePrintFactory().button().setIcon(icon));
	}

	public JoButton(final String text) {
		this(bluePrint(text));
	}

	public JoButton(final String text, final String tooltipText) {
		this(bluePrint(text, tooltipText));
	}

	public JoButton(final IButtonDescriptor descriptor) {
		super(Toolkit.getBluePrintFactory().button().setSetup(descriptor));
		this.actionListeners = new HashSet<IActionListener>();
	}

	@Override
	void initialize(final IButton widget) {
		super.initialize(widget);
		if (action != null) {
			widget.setAction(action);
		}
		for (final IActionListener actionListener : actionListeners) {
			widget.addActionListener(actionListener);
		}
	}

	@Override
	public void setMarkup(final Markup markup) {
		if (isInitialized()) {
			getWidget().setMarkup(markup);
		}
		else {
			getBluePrint().setMarkup(markup);
		}
	}

	@Override
	public void setFontSize(final int size) {
		if (isInitialized()) {
			getWidget().setFontSize(size);
		}
		else {
			getBluePrint().setFontSize(size);
		}
	}

	@Override
	public void setFontName(final String fontName) {
		if (isInitialized()) {
			getWidget().setFontName(fontName);
		}
		else {
			getBluePrint().setFontName(fontName);
		}
	}

	@Override
	public void setText(final String text) {
		if (isInitialized()) {
			getWidget().setText(text);
		}
		else {
			getBluePrint().setText(text);
		}
	}

	@Override
	public String getText() {
		if (isInitialized()) {
			return getWidget().getText();
		}
		else {
			return getBluePrint().getText();
		}
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		if (isInitialized()) {
			getWidget().setIcon(icon);
		}
		else {
			getBluePrint().setIcon(icon);
		}
	}

	@Override
	public void setAction(final IAction action) {
		if (isInitialized()) {
			getWidget().setAction(action);
		}
		else {
			this.action = action;
		}
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (isInitialized()) {
			getWidget().setEnabled(enabled);
		}
		else {
			getBluePrint().setEnabled(enabled);
		}
	}

	@Override
	public void addActionListener(final IActionListener actionListener) {
		if (isInitialized()) {
			getWidget().addActionListener(actionListener);
		}
		else {
			actionListeners.add(actionListener);
		}
	}

	@Override
	public void removeActionListener(final IActionListener actionListener) {
		if (isInitialized()) {
			getWidget().removeActionListener(actionListener);
		}
		else {
			actionListeners.remove(actionListener);
		}
	}

	public static IButtonBluePrint bluePrint() {
		return Toolkit.getBluePrintFactory().button();
	}

	public static IButtonBluePrint bluePrint(final String text) {
		return Toolkit.getBluePrintFactory().button().setText(text);
	}

	public static IButtonBluePrint bluePrint(final String text, final String tooltipText) {
		return Toolkit.getBluePrintFactory().button().setText(text).setToolTipText(tooltipText);
	}

}
