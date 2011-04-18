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

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.blueprint.builder.IComponentSetupBuilder;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.common.widgets.controler.IComponentListener;
import org.jowidgets.common.widgets.controler.IFocusListener;
import org.jowidgets.common.widgets.controler.IKeyListener;
import org.jowidgets.common.widgets.controler.IMouseListener;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

class Component<WIDGET_TYPE extends IComponent, BLUE_PRINT_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE> & IComponentSetupBuilder<?>> extends
		Widget<WIDGET_TYPE, BLUE_PRINT_TYPE> implements IComponent {

	private Cursor cursor;
	private final Set<IPopupDetectionListener> popupDetectionListeners;
	private final Set<IFocusListener> focusListeners;
	private final Set<IKeyListener> keyListeners;
	private final Set<IMouseListener> mouseListeners;
	private final Set<IComponentListener> componentListners;
	private final Set<JoPopupMenu> popupMenus;

	private IMenuModel popupMenu;

	Component(final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
		this.popupDetectionListeners = new HashSet<IPopupDetectionListener>();
		this.focusListeners = new HashSet<IFocusListener>();
		this.keyListeners = new HashSet<IKeyListener>();
		this.mouseListeners = new HashSet<IMouseListener>();
		this.componentListners = new HashSet<IComponentListener>();
		this.popupMenus = new HashSet<JoPopupMenu>();
	}

	@Override
	void initialize(final WIDGET_TYPE widget) {
		super.initialize(widget);
		if (cursor != null) {
			widget.setCursor(cursor);
		}
		if (popupMenu != null) {
			getWidget().setPopupMenu(popupMenu);
		}
		for (final JoPopupMenu joPopupMenu : popupMenus) {
			joPopupMenu.initialize(createPopupMenu());
		}
		for (final IPopupDetectionListener listener : popupDetectionListeners) {
			widget.addPopupDetectionListener(listener);
		}
		for (final IFocusListener focusListener : focusListeners) {
			widget.addFocusListener(focusListener);
		}
		for (final IKeyListener keyListener : keyListeners) {
			widget.addKeyListener(keyListener);
		}
		for (final IMouseListener mouseListener : mouseListeners) {
			widget.addMouseListener(mouseListener);
		}
		for (final IComponentListener componentListener : componentListners) {
			widget.addComponentListener(componentListener);
		}

		popupDetectionListeners.clear();
		focusListeners.clear();
		keyListeners.clear();
		mouseListeners.clear();
		componentListners.clear();
	}

	public final void addPopupMenu(final JoPopupMenu popupMenu) {
		if (isInitialized()) {
			popupMenu.initialize(createPopupMenu());
		}
		else {
			popupMenus.add(popupMenu);
		}
	}

	@Override
	public final void setForegroundColor(final IColorConstant colorValue) {
		if (isInitialized()) {
			getWidget().setForegroundColor(colorValue);
		}
		else {
			getBluePrint().setForegroundColor(colorValue);
		}
	}

	@Override
	public final void setBackgroundColor(final IColorConstant colorValue) {
		if (isInitialized()) {
			getWidget().setBackgroundColor(colorValue);
		}
		else {
			getBluePrint().setBackgroundColor(colorValue);
		}
	}

	@Override
	public void setVisible(final boolean visible) {
		if (isInitialized()) {
			getWidget().setVisible(visible);
		}
		else {
			getBluePrint().setVisible(visible);
		}
	}

	@Override
	public void setCursor(final Cursor cursor) {
		if (isInitialized()) {
			getWidget().setCursor(cursor);
		}
		else {
			this.cursor = cursor;
		}
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		if (isInitialized()) {
			getWidget().addPopupDetectionListener(listener);
		}
		else {
			popupDetectionListeners.add(listener);
		}
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		if (isInitialized()) {
			getWidget().removePopupDetectionListener(listener);
		}
		else {
			popupDetectionListeners.remove(listener);
		}
	}

	@Override
	public boolean requestFocus() {
		if (isInitialized()) {
			return getWidget().requestFocus();
		}
		else {
			return false;
		}
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		if (isInitialized()) {
			getWidget().addFocusListener(listener);
		}
		else {
			focusListeners.add(listener);
		}
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		if (isInitialized()) {
			getWidget().removeFocusListener(listener);
		}
		else {
			focusListeners.remove(listener);
		}
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		if (isInitialized()) {
			getWidget().addKeyListener(listener);
		}
		else {
			keyListeners.add(listener);
		}
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		if (isInitialized()) {
			getWidget().removeKeyListener(listener);
		}
		else {
			keyListeners.remove(listener);
		}
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		if (isInitialized()) {
			getWidget().addMouseListener(mouseListener);
		}
		else {
			mouseListeners.add(mouseListener);
		}
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		if (isInitialized()) {
			getWidget().removeMouseListener(mouseListener);
		}
		else {
			mouseListeners.remove(mouseListener);
		}
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		if (isInitialized()) {
			getWidget().addComponentListener(componentListener);
		}
		else {
			componentListners.add(componentListener);
		}
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		if (isInitialized()) {
			getWidget().removeComponentListener(componentListener);
		}
		else {
			componentListners.remove(componentListener);
		}
	}

	@Override
	public IPopupMenu createPopupMenu() {
		if (isInitialized()) {
			return getWidget().createPopupMenu();
		}
		else {
			final JoPopupMenu result = new JoPopupMenu();
			popupMenus.add(result);
			return result;
		}
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenu) {
		if (isInitialized()) {
			getWidget().setPopupMenu(popupMenu);
		}
		else {
			this.popupMenu = popupMenu;
		}
	}

	@Override
	public final void redraw() {
		if (isInitialized()) {
			getWidget().redraw();
		}
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		if (isInitialized()) {
			getWidget().setRedrawEnabled(enabled);
		}
	}

	@Override
	public final boolean isVisible() {
		checkInitialized();
		return getWidget().isVisible();
	}

	@Override
	public IColorConstant getForegroundColor() {
		checkInitialized();
		return getWidget().getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		checkInitialized();
		return getWidget().getBackgroundColor();
	}

	@Override
	public Dimension getSize() {
		checkInitialized();
		return getWidget().getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		checkInitialized();
		getWidget().setSize(size);
	}

	@Override
	public Position getPosition() {
		checkInitialized();
		return getWidget().getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		checkInitialized();
		getWidget().setPosition(position);
	}

	@Override
	public Position toScreen(final Position localPosition) {
		checkInitialized();
		return getWidget().toScreen(localPosition);
	}

	@Override
	public Position toLocal(final Position screenPosition) {
		checkInitialized();
		return getWidget().toLocal(screenPosition);
	}

	@Override
	public Position fromComponent(final IComponentCommon component, final Position componentPosition) {
		checkInitialized();
		return getWidget().fromComponent(component, componentPosition);
	}

	@Override
	public Position toComponent(final Position componentPosition, final IComponentCommon component) {
		checkInitialized();
		return getWidget().toComponent(componentPosition, component);
	}

	@Override
	public boolean isReparentable() {
		checkInitialized();
		return getWidget().isReparentable();
	}

}
