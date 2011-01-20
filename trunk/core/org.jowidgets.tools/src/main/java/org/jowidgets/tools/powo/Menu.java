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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.widgets.IActionMenuItem;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IMenuItem;
import org.jowidgets.common.widgets.builder.ISetupBuilder;
import org.jowidgets.common.widgets.controler.IMenuListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

class Menu<WIDGET_TYPE extends IMenu, BLUE_PRINT_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE> & ISetupBuilder<?>> extends
		Widget<WIDGET_TYPE, BLUE_PRINT_TYPE> implements IMenu {

	private final Set<IMenuListener> menuListeners;
	private final List<Widget<?, ?>> preItems;
	private final JoWidgetFactory widgetFactory;

	Menu(final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
		this.menuListeners = new HashSet<IMenuListener>();
		this.preItems = new LinkedList<Widget<?, ?>>();
		this.widgetFactory = new JoWidgetFactory();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	void initialize(final WIDGET_TYPE widget) {
		super.initialize(widget);
		for (final Widget preWidget : preItems) {
			final IMenuItem newItem = widget.addItem(preWidget.getDescriptor());
			preWidget.initialize(newItem);
		}
		for (final IMenuListener listener : menuListeners) {
			getWidget().addMenuListener(listener);
		}
	}

	@Override
	public void addMenuListener(final IMenuListener listener) {
		if (isInitialized()) {
			getWidget().addMenuListener(listener);
		}
		else {
			menuListeners.add(listener);
		}
	}

	@Override
	public void removeMenuListener(final IMenuListener listener) {
		if (isInitialized()) {
			getWidget().removeMenuListener(listener);
		}
		else {
			menuListeners.remove(listener);
		}
	}

	@SuppressWarnings({"rawtypes"})
	@Override
	public List<IMenuItem> getChildren() {
		if (isInitialized()) {
			return getWidget().getChildren();
		}
		else {
			final List<IMenuItem> result = new LinkedList<IMenuItem>();
			for (final Widget preItem : preItems) {
				result.add((IMenuItem) preItem);
			}
			return result;
		}
	}

	@Override
	public boolean remove(final IMenuItem item) {
		if (isInitialized()) {
			return getWidget().remove(item);
		}
		else {
			return preItems.remove(item);
		}
	}

	@Override
	public void removeAll() {
		if (isInitialized()) {
			getWidget().removeAll();
		}
		else {
			preItems.clear();
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public <RESULT_WIDGET_TYPE extends IMenuItem> RESULT_WIDGET_TYPE addItem(
		final IWidgetDescriptor<? extends RESULT_WIDGET_TYPE> descriptor) {
		if (isInitialized()) {
			return getWidget().addItem(descriptor);
		}
		else {
			final Widget powo = widgetFactory.create(descriptor);
			preItems.add(powo);
			return (RESULT_WIDGET_TYPE) powo;
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public <RESULT_WIDGET_TYPE extends IMenuItem> RESULT_WIDGET_TYPE addItem(
		final int index,
		final IWidgetDescriptor<? extends RESULT_WIDGET_TYPE> descriptor) {

		if (isInitialized()) {
			return getWidget().addItem(index, descriptor);
		}
		else {
			final Widget powo = widgetFactory.create(descriptor);
			preItems.add(index, powo);
			return (RESULT_WIDGET_TYPE) powo;
		}
	}

	@Override
	public IMenuItem addSeparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMenuItem addSeparator(final int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IActionMenuItem addAction(final IAction action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IActionMenuItem addAction(final int index, final IAction action) {
		// TODO Auto-generated method stub
		return null;
	}

}
