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

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITextArea;
import org.jowidgets.api.widgets.descriptor.setup.ITextAreaSetup;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.common.wrapper.AbstractControlSpiWrapper;
import org.jowidgets.spi.widgets.ITextAreaSpi;
import org.jowidgets.tools.controller.InputObservable;
import org.jowidgets.tools.validation.CompoundValidator;
import org.jowidgets.tools.validation.ValidationCache;
import org.jowidgets.tools.validation.ValidationCache.IValidationResultCreator;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidationResultBuilder;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public class TextAreaImpl extends AbstractControlSpiWrapper implements ITextArea {

	private final ControlDelegate controlDelegate;

	private final ValidationCache validationCache;
	private final CompoundValidator<String> compoundValidator;
	private final InputObservable inputObservable;

	private String lastUnmodiefiedText;

	public TextAreaImpl(final ITextAreaSpi textAreaSpi, final ITextAreaSetup setup) {
		super(textAreaSpi);

		this.controlDelegate = new ControlDelegate(textAreaSpi, this);

		this.inputObservable = new InputObservable();

		this.compoundValidator = new CompoundValidator<String>();
		this.validationCache = new ValidationCache(new IValidationResultCreator() {
			@Override
			public IValidationResult createValidationResult() {
				final IValidationResultBuilder builder = ValidationResult.builder();
				builder.addResult(compoundValidator.validate(getValue()));
				return builder.build();
			}
		});

		if (setup.getText() != null) {
			setText(setup.getText());
		}

		if (setup.getMarkup() != null) {
			setMarkup(setup.getMarkup());
		}
		if (setup.getFontSize() != null) {
			setFontSize(Integer.valueOf(setup.getFontSize()));
		}
		if (setup.getFontName() != null) {
			setFontName(setup.getFontName());
		}

		setEditable(setup.isEditable());

		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);

		textAreaSpi.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				inputObservable.fireInputChanged();
				validationCache.setDirty();
			}
		});

		lastUnmodiefiedText = textAreaSpi.getText();
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
	public void addDisposeListener(final IDisposeListener listener) {
		controlDelegate.addDisposeListener(listener);
	}

	@Override
	public void removeDisposeListener(final IDisposeListener listener) {
		controlDelegate.removeDisposeListener(listener);
	}

	@Override
	public boolean isDisposed() {
		return controlDelegate.isDisposed();
	}

	@Override
	public void dispose() {
		controlDelegate.dispose();
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return controlDelegate.createPopupMenu();
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
	public void setFontSize(final int size) {
		getWidget().setFontSize(size);
	}

	@Override
	public void setFontName(final String fontName) {
		getWidget().setFontName(fontName);
	}

	@Override
	public void setMarkup(final Markup markup) {
		getWidget().setMarkup(markup);
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

	@Override
	public void setEditable(final boolean editable) {
		getWidget().setEditable(editable);
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		inputObservable.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		inputObservable.removeInputListener(listener);
	}

	@Override
	public void addValidator(final IValidator<String> validator) {
		compoundValidator.addValidator(validator);
	}

	@Override
	public boolean hasModifications() {
		return !NullCompatibleEquivalence.equals(lastUnmodiefiedText, getWidget().getText());
	}

	@Override
	public void resetModificationState() {
		lastUnmodiefiedText = getWidget().getText();
	}

	@Override
	public void setValue(final String value) {
		getWidget().setText(value);
	}

	@Override
	public String getValue() {
		return getWidget().getText();
	}

	@Override
	public IValidationResult validate() {
		return validationCache.validate();
	}

	@Override
	public void addValidationConditionListener(final IValidationConditionListener listener) {
		validationCache.addValidationConditionListener(listener);
	}

	@Override
	public void removeValidationConditionListener(final IValidationConditionListener listener) {
		validationCache.removeValidationConditionListener(listener);
	}

}
