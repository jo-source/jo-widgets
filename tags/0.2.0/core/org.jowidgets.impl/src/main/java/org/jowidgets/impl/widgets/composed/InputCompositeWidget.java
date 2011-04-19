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
package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.validation.IValidator;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IInputComposite;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IValidationLabel;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.descriptor.setup.IInputCompositeSetup;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.common.widgets.controler.IComponentListener;
import org.jowidgets.common.widgets.controler.IFocusListener;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.IKeyListener;
import org.jowidgets.common.widgets.controler.IMouseListener;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.composed.internal.InputContentContainer;

public class InputCompositeWidget<INPUT_TYPE> implements IInputComposite<INPUT_TYPE> {

	private final IComposite parentComposite;
	private final IComposite composite;
	private final InputContentContainer<INPUT_TYPE> contentContainer;
	private final IValidationLabel validationLabel;
	private final boolean isAutoResetValidation;

	public InputCompositeWidget(final IComposite composite, final IInputCompositeSetup<INPUT_TYPE> setup) {
		super();

		this.isAutoResetValidation = setup.isAutoResetValidation();

		this.parentComposite = composite;
		this.parentComposite.setLayout(new MigLayoutDescriptor("0[grow]0", "0[grow]0"));

		final ICompositeBluePrint compositeBp = Toolkit.getBluePrintFactory().composite().setBorder(setup.getBorder());

		this.composite = parentComposite.add(compositeBp, "growx, growy, h 0::, w 0::");

		if (setup.getValidationLabel() != null) {
			this.composite.setLayout(new MigLayoutDescriptor("0[grow]0", "0[][grow][]0"));
			validationLabel = this.composite.add(setup.getValidationLabel(), "h 18::, wrap");// TODO MG use hide instead
		}
		else {
			validationLabel = null;
			this.composite.setLayout(new MigLayoutDescriptor("0[grow]0", "0[grow][]0"));
		}

		this.contentContainer = new InputContentContainer<INPUT_TYPE>(
			this.composite,
			setup.getContentCreator(),
			setup.isContentScrolled(),
			setup.getContentBorder(),
			setup.getValue());

		if (setup.getValidator() != null) {
			contentContainer.addValidator(setup.getValidator());
		}

		if (validationLabel != null) {
			validationLabel.registerInputWidget(contentContainer);
		}

	}

	@Override
	public void setEditable(final boolean editable) {
		contentContainer.setEditable(editable);
		if (isAutoResetValidation && validationLabel != null) {
			validationLabel.setEnabled(editable);
		}
	}

	@Override
	public boolean isMandatory() {
		return contentContainer.isMandatory();
	}

	@Override
	public void setMandatory(final boolean mandatory) {
		contentContainer.setMandatory(mandatory);
	}

	@Override
	public void addValidator(final IValidator<INPUT_TYPE> validator) {
		contentContainer.addValidator(validator);
	}

	@Override
	public void resetValidation() {
		if (validationLabel != null) {
			validationLabel.resetValidation();
		}
	}

	@Override
	public boolean isEmpty() {
		return contentContainer.isEmpty();
	}

	@Override
	public IContainer getParent() {
		return parentComposite.getParent();
	}

	@Override
	public void setParent(final IContainer parent) {
		parentComposite.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return parentComposite.isReparentable();
	}

	@Override
	public Object getUiReference() {
		return parentComposite.getUiReference();
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		parentComposite.setLayoutConstraints(layoutConstraints);
	}

	@Override
	public Object getLayoutConstraints() {
		return parentComposite.getLayoutConstraints();
	}

	@Override
	public void setVisible(final boolean visible) {
		parentComposite.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return parentComposite.isVisible();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		parentComposite.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return parentComposite.isEnabled();
	}

	@Override
	public Dimension getSize() {
		return parentComposite.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		parentComposite.setSize(size);
	}

	@Override
	public Position getPosition() {
		return parentComposite.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		parentComposite.setPosition(position);
	}

	@Override
	public Position toScreen(final Position localPosition) {
		return composite.toScreen(localPosition);
	}

	@Override
	public Position toLocal(final Position screenPosition) {
		return composite.toLocal(screenPosition);
	}

	@Override
	public Position fromComponent(final IComponentCommon component, final Position componentPosition) {
		return composite.fromComponent(component, componentPosition);
	}

	@Override
	public Position toComponent(final Position componentPosition, final IComponentCommon component) {
		return composite.toComponent(componentPosition, component);
	}

	@Override
	public void redraw() {
		contentContainer.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		contentContainer.setRedrawEnabled(enabled);
	}

	@Override
	public void setCursor(final Cursor cursor) {
		contentContainer.setCursor(cursor);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		composite.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		composite.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return composite.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return composite.getBackgroundColor();
	}

	@Override
	public void setValue(final INPUT_TYPE value) {
		contentContainer.setValue(value);
		if (isAutoResetValidation) {
			resetValidation();
		}
	}

	@Override
	public INPUT_TYPE getValue() {
		return contentContainer.getValue();
	}

	@Override
	public ValidationResult validate() {
		return contentContainer.validate();
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		contentContainer.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		contentContainer.removeInputListener(listener);
	}

	@Override
	public boolean requestFocus() {
		return composite.requestFocus();
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		composite.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		composite.removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		composite.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		composite.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		composite.addMouseListener(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		composite.removeMouseListener(mouseListener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		composite.addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		composite.removeComponentListener(componentListener);
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return composite.createPopupMenu();
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenu) {
		composite.setPopupMenu(popupMenu);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		composite.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		composite.removePopupDetectionListener(listener);
	}

}
