/*
 * Copyright (c) 2011, H.Westphal
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

package org.jowidgets.addons.map.swt;

import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.jowidgets.addons.map.common.IAvailableCallback;
import org.jowidgets.addons.map.common.IMap;
import org.jowidgets.addons.map.common.IViewChangeListener;
import org.jowidgets.addons.map.common.impl.GoogleEarth;
import org.jowidgets.addons.map.common.widget.IMapWidget;
import org.jowidgets.addons.map.common.widget.IMapWidgetBlueprint;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;

final class SwtGoogleEarthWidget implements IMapWidget {

	private final IMap map;
	private final IControl joControl;

	SwtGoogleEarthWidget(final Composite parent, final IMapWidgetBlueprint descriptor, final String apiKey) {
		map = new GoogleEarth(parent, apiKey);
		joControl = Toolkit.getWidgetWrapperFactory().createComposite(map);
		VisibiliySettingsInvoker.setVisibility(descriptor, this);
		ColorSettingsInvoker.setColors(descriptor, this);
	}

	@Override
	public void setLanguage(final String language) {
		map.setLanguage(language);
	}

	@Override
	public void initialize(final IAvailableCallback callback) {
		map.initialize(callback);
	}

	@Override
	public boolean isInitialized() {
		return map.isInitialized();
	}

	@Override
	public boolean isAvailable() {
		return map.isAvailable();
	}

	@Override
	public void addViewChangeListener(final IViewChangeListener listener) {
		map.addViewChangeListener(listener);
	}

	@Override
	public boolean removeViewChangeListener(final IViewChangeListener listener) {
		return map.removeViewChangeListener(listener);
	}

	@Override
	public Set<Class<?>> getSupportedDesignationClasses() {
		return map.getSupportedDesignationClasses();
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		joControl.addKeyListener(listener);
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		joControl.addFocusListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		joControl.addMouseListener(mouseListener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		joControl.addComponentListener(componentListener);
	}

	@Override
	public Object getUiReference() {
		return joControl.getUiReference();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		joControl.addPopupDetectionListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		joControl.removeKeyListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		joControl.removeFocusListener(listener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		joControl.removeMouseListener(mouseListener);
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		joControl.setLayoutConstraints(layoutConstraints);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		joControl.removeComponentListener(componentListener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		joControl.removePopupDetectionListener(listener);
	}

	@Override
	public void setParent(final IContainer parent) {
		joControl.setParent(parent);
	}

	@Override
	public void setToolTipText(final String toolTip) {
		joControl.setToolTipText(toolTip);
	}

	@Override
	public Object getLayoutConstraints() {
		return joControl.getLayoutConstraints();
	}

	@Override
	public Dimension getMinSize() {
		return joControl.getMinSize();
	}

	@Override
	public IContainer getParent() {
		return joControl.getParent();
	}

	@Override
	public boolean isReparentable() {
		return joControl.isReparentable();
	}

	@Override
	public Dimension getPreferredSize() {
		return joControl.getPreferredSize();
	}

	@Override
	public void setMinSize(final Dimension minSize) {
		joControl.setMinSize(minSize);
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return joControl.createPopupMenu();
	}

	@Override
	public Dimension getMaxSize() {
		return joControl.getMaxSize();
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenu) {
		joControl.setPopupMenu(popupMenu);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		joControl.setEnabled(enabled);
	}

	@Override
	public void setPreferredSize(final Dimension preferredSize) {
		joControl.setPreferredSize(preferredSize);
	}

	@Override
	public boolean isEnabled() {
		return joControl.isEnabled();
	}

	@Override
	public void setMaxSize(final Dimension maxSize) {
		joControl.setMaxSize(maxSize);
	}

	@Override
	public Position toScreen(final Position localPosition) {
		return joControl.toScreen(localPosition);
	}

	@Override
	public Position toLocal(final Position screenPosition) {
		return joControl.toLocal(screenPosition);
	}

	@Override
	public void setSize(final int width, final int height) {
		joControl.setSize(width, height);
	}

	@Override
	public void redraw() {
		joControl.redraw();
	}

	@Override
	public void setPosition(final int x, final int y) {
		joControl.setPosition(x, y);
	}

	@Override
	public Position fromComponent(final IComponentCommon component, final Position componentPosition) {
		return joControl.fromComponent(component, componentPosition);
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		joControl.setRedrawEnabled(enabled);
	}

	@Override
	public Position toComponent(final Position componentPosition, final IComponentCommon component) {
		return joControl.toComponent(componentPosition, component);
	}

	@Override
	public boolean requestFocus() {
		return joControl.requestFocus();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		joControl.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		joControl.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return joControl.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return joControl.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		joControl.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		joControl.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return joControl.isVisible();
	}

	@Override
	public Dimension getSize() {
		return joControl.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		joControl.setSize(size);
	}

	@Override
	public Position getPosition() {
		return joControl.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		joControl.setPosition(position);
	}

}
