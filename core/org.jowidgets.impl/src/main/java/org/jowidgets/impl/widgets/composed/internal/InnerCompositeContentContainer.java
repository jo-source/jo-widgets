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
package org.jowidgets.impl.widgets.composed.internal;

import java.util.List;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IInputComponent;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.common.widgets.controler.IFocusListener;
import org.jowidgets.common.widgets.controler.IKeyListener;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.util.Assert;

public class InnerCompositeContentContainer implements IInputContentContainer {

	private final IInputContentContainer outerContainer;
	private final IComposite compositeWidget;

	public InnerCompositeContentContainer(final IInputContentContainer outerContainer, final IComposite compositeWidget) {
		Assert.paramNotNull(outerContainer, "outerContainer");
		this.outerContainer = outerContainer;
		this.compositeWidget = compositeWidget;
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		compositeWidget.setLayout(layoutDescriptor);
	}

	@Override
	public void layoutBegin() {
		compositeWidget.layoutBegin();
	}

	@Override
	public void layoutEnd() {
		compositeWidget.layoutEnd();
	}

	@Override
	public void removeAll() {
		compositeWidget.removeAll();
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {

		return compositeWidget.add(descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		return compositeWidget.add(creator, layoutConstraints);
	}

	@Override
	public boolean remove(final IControl control) {
		return compositeWidget.remove(control);
	}

	@Override
	public List<IControl> getChildren() {
		return compositeWidget.getChildren();
	}

	@Override
	public boolean isReparentable() {
		return compositeWidget.isReparentable();
	}

	@Override
	public IComponent getParent() {
		return compositeWidget.getParent();
	}

	@Override
	public Object getUiReference() {
		return compositeWidget.getUiReference();
	}

	@Override
	public void setVisible(final boolean visible) {
		compositeWidget.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return compositeWidget.isVisible();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		compositeWidget.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return compositeWidget.isEnabled();
	}

	@Override
	public Dimension getSize() {
		return compositeWidget.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		compositeWidget.setSize(size);
	}

	@Override
	public Position getPosition() {
		return compositeWidget.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		compositeWidget.setPosition(position);
	}

	@Override
	public Position toScreen(final Position localPosition) {
		return compositeWidget.toScreen(localPosition);
	}

	@Override
	public Position toLocal(final Position screenPosition) {
		return compositeWidget.toLocal(screenPosition);
	}

	@Override
	public Position fromComponent(final IComponentCommon component, final Position componentPosition) {
		return compositeWidget.fromComponent(component, componentPosition);
	}

	@Override
	public Position toComponent(final Position componentPosition, final IComponentCommon component) {
		return compositeWidget.toComponent(componentPosition, component);
	}

	@Override
	public void redraw() {
		compositeWidget.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		compositeWidget.setRedrawEnabled(enabled);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		compositeWidget.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		compositeWidget.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return compositeWidget.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return compositeWidget.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		compositeWidget.setCursor(cursor);
	}

	@Override
	public void registerInputWidget(final String contextLabel, final IInputComponent<?> inputWidget) {
		outerContainer.registerInputWidget(contextLabel, inputWidget);
	}

	@Override
	public void unRegisterInputWidget(final IInputComponent<?> inputWidget) {
		outerContainer.unRegisterInputWidget(inputWidget);
	}

	@Override
	public void addSubContent(final IInputContentCreator<?> subContentCreator, final Object layoutConstraints) {
		final ICompositeBluePrint compositeBp = new BluePrintFactory().composite();
		final IComposite innerComposite = add(compositeBp, layoutConstraints);
		final InnerCompositeContentContainer innerContainer = new InnerCompositeContentContainer(outerContainer, innerComposite);
		subContentCreator.createContent(innerContainer);
	}

	@Override
	public void fireInputChanged() {
		outerContainer.fireInputChanged();
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return compositeWidget.createPopupMenu();
	}

	@Override
	public boolean requestFocus() {
		return compositeWidget.requestFocus();
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		compositeWidget.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		compositeWidget.removeKeyListener(listener);
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		compositeWidget.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		compositeWidget.removeFocusListener(listener);
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenu) {
		compositeWidget.setPopupMenu(popupMenu);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		compositeWidget.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		compositeWidget.removePopupDetectionListener(listener);
	}

}
