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

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jowidgets.api.controller.IContainerListener;
import org.jowidgets.api.controller.IContainerRegistry;
import org.jowidgets.api.controller.IListenerFactory;
import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.blueprint.builder.IContainerSetupBuilder;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.util.Assert;
import org.jowidgets.util.Tuple;

class Container<WIDGET_TYPE extends IContainer, BLUE_PRINT_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE> & IContainerSetupBuilder<?>> extends
		Component<WIDGET_TYPE, BLUE_PRINT_TYPE> implements IContainer {

	@SuppressWarnings("rawtypes")
	private final List<Tuple<Widget, Object>> preWidgets;
	private final JoWidgetFactory widgetFactory;
	private final Set<IContainerListener> containerListeners;
	private final Set<IContainerRegistry> containerChildrenRegistries;
	private final Set<IListenerFactory<IComponentListener>> componentListenerFactories;
	private final Set<IListenerFactory<IFocusListener>> focusListenerFactories;
	private final Set<IListenerFactory<IKeyListener>> keyListenerFactories;
	private final Set<IListenerFactory<IMouseListener>> mouseListenerFactories;
	private final Set<IListenerFactory<IPopupDetectionListener>> popupDetectionListenerFactories;

	private List<? extends IControl> tabOrder;

	@SuppressWarnings("rawtypes")
	Container(final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
		this.preWidgets = new LinkedList<Tuple<Widget, Object>>();
		this.widgetFactory = new JoWidgetFactory();
		this.containerListeners = new LinkedHashSet<IContainerListener>();
		this.containerChildrenRegistries = new LinkedHashSet<IContainerRegistry>();
		this.componentListenerFactories = new LinkedHashSet<IListenerFactory<IComponentListener>>();
		this.focusListenerFactories = new LinkedHashSet<IListenerFactory<IFocusListener>>();
		this.keyListenerFactories = new LinkedHashSet<IListenerFactory<IKeyListener>>();
		this.mouseListenerFactories = new LinkedHashSet<IListenerFactory<IMouseListener>>();
		this.popupDetectionListenerFactories = new LinkedHashSet<IListenerFactory<IPopupDetectionListener>>();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	void initialize(final WIDGET_TYPE widget) {
		super.initialize(widget);
		for (final IContainerListener containerListener : containerListeners) {
			widget.addContainerListener(containerListener);
		}
		for (final IContainerRegistry registry : containerChildrenRegistries) {
			widget.addContainerRegistry(registry);
		}
		for (final IListenerFactory<IComponentListener> factory : componentListenerFactories) {
			widget.addComponentListenerRecursive(factory);
		}
		for (final IListenerFactory<IFocusListener> factory : focusListenerFactories) {
			widget.addFocusListenerRecursive(factory);
		}
		for (final IListenerFactory<IKeyListener> factory : keyListenerFactories) {
			widget.addKeyListenerRecursive(factory);
		}
		for (final IListenerFactory<IMouseListener> factory : mouseListenerFactories) {
			widget.addMouseListenerRecursive(factory);
		}
		for (final IListenerFactory<IPopupDetectionListener> factory : popupDetectionListenerFactories) {
			widget.addPopupDetectionListenerRecursive(factory);
		}
		for (final Tuple<Widget, Object> preWidgetTuple : preWidgets) {
			final Widget preWidget = preWidgetTuple.getFirst();
			final Object layoutConstraints = preWidgetTuple.getSecond();
			final IControl newWidget = widget.add(preWidget.getDescriptor(), layoutConstraints);
			preWidget.initialize(newWidget);
		}

		if (tabOrder != null) {
			widget.setTabOrder(tabOrder);
			tabOrder = null;
		}
	}

	public final <P_WIDGET_TYPE extends Component<? extends IControl, ?>> P_WIDGET_TYPE add(final P_WIDGET_TYPE widget) {
		return add(widget, "");
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public final <P_WIDGET_TYPE extends Component<? extends IControl, ?>> P_WIDGET_TYPE add(
		final P_WIDGET_TYPE widget,
		final Object layoutConstraints) {
		if (isInitialized()) {
			final IComponent newWidget = getWidget().add(widget.getDescriptor(), layoutConstraints);
			final Component rawWidget = widget;
			rawWidget.initialize(newWidget);
			return widget;
		}
		else {
			preWidgets.add(new Tuple<Widget, Object>(widget, layoutConstraints));
			return widget;
		}
	}

	public final JoLabel addLabel(final String text) {
		final JoLabel label = new JoLabel(text);
		add(label);
		return label;
	}

	public final JoLabel addLabel(final String text, final Object layoutConstraints) {
		final JoLabel label = new JoLabel(text);
		add(label, layoutConstraints);
		return label;
	}

	public final JoLabel addLabel(final String text, final String tooltipText, final Object layoutConstraints) {
		final JoLabel label = new JoLabel(text, tooltipText);
		add(label, layoutConstraints);
		return label;
	}

	public final JoLabel addLabel(
		final IImageConstant icon,
		final String text,
		final String tooltipText,
		final Object layoutConstraints) {

		final JoLabel label = new JoLabel(icon, text, tooltipText);
		add(label, layoutConstraints);
		return label;
	}

	@Override
	public void setTabOrder(final List<? extends IControl> tabOrder) {
		if (isInitialized()) {
			getWidget().setTabOrder(tabOrder);
		}
		else {
			this.tabOrder = tabOrder;
		}
	}

	@Override
	public void addContainerListener(final IContainerListener listener) {
		if (isInitialized()) {
			getWidget().addContainerListener(listener);
		}
		else {
			containerListeners.add(listener);
		}
	}

	@Override
	public void removeContainerListener(final IContainerListener listener) {
		if (isInitialized()) {
			getWidget().removeContainerListener(listener);
		}
		else {
			containerListeners.remove(listener);
		}
	}

	@Override
	public void addContainerRegistry(final IContainerRegistry registry) {
		if (isInitialized()) {
			getWidget().addContainerRegistry(registry);
		}
		else {
			containerChildrenRegistries.add(registry);
		}
	}

	@Override
	public void removeContainerRegistry(final IContainerRegistry registry) {
		if (isInitialized()) {
			getWidget().removeContainerRegistry(registry);
		}
		else {
			containerChildrenRegistries.remove(registry);
		}
	}

	@Override
	public void addComponentListenerRecursive(final IListenerFactory<IComponentListener> listenerFactory) {
		if (isInitialized()) {
			getWidget().addComponentListenerRecursive(listenerFactory);
		}
		else {
			componentListenerFactories.add(listenerFactory);
		}
	}

	@Override
	public void removeComponentListenerRecursive(final IListenerFactory<IComponentListener> listenerFactory) {
		if (isInitialized()) {
			getWidget().removeComponentListenerRecursive(listenerFactory);
		}
		else {
			componentListenerFactories.remove(listenerFactory);
		}
	}

	@Override
	public void addFocusListenerRecursive(final IListenerFactory<IFocusListener> listenerFactory) {
		if (isInitialized()) {
			getWidget().addFocusListenerRecursive(listenerFactory);
		}
		else {
			focusListenerFactories.add(listenerFactory);
		}
	}

	@Override
	public void removeFocusListenerRecursive(final IListenerFactory<IFocusListener> listenerFactory) {
		if (isInitialized()) {
			getWidget().removeFocusListenerRecursive(listenerFactory);
		}
		else {
			focusListenerFactories.remove(listenerFactory);
		}
	}

	@Override
	public void addKeyListenerRecursive(final IListenerFactory<IKeyListener> listenerFactory) {
		if (isInitialized()) {
			getWidget().addKeyListenerRecursive(listenerFactory);
		}
		else {
			keyListenerFactories.add(listenerFactory);
		}
	}

	@Override
	public void removeKeyListenerRecursive(final IListenerFactory<IKeyListener> listenerFactory) {
		if (isInitialized()) {
			getWidget().removeKeyListenerRecursive(listenerFactory);
		}
		else {
			keyListenerFactories.remove(listenerFactory);
		}
	}

	@Override
	public void addMouseListenerRecursive(final IListenerFactory<IMouseListener> listenerFactory) {
		if (isInitialized()) {
			getWidget().addMouseListenerRecursive(listenerFactory);
		}
		else {
			mouseListenerFactories.add(listenerFactory);
		}
	}

	@Override
	public void removeMouseListenerRecursive(final IListenerFactory<IMouseListener> listenerFactory) {
		if (isInitialized()) {
			getWidget().removeMouseListenerRecursive(listenerFactory);
		}
		else {
			mouseListenerFactories.remove(listenerFactory);
		}
	}

	@Override
	public void addPopupDetectionListenerRecursive(final IListenerFactory<IPopupDetectionListener> listenerFactory) {
		if (isInitialized()) {
			getWidget().addPopupDetectionListenerRecursive(listenerFactory);
		}
		else {
			popupDetectionListenerFactories.add(listenerFactory);
		}
	}

	@Override
	public void removePopupDetectionListenerRecursive(final IListenerFactory<IPopupDetectionListener> listenerFactory) {
		if (isInitialized()) {
			getWidget().removePopupDetectionListenerRecursive(listenerFactory);
		}
		else {
			popupDetectionListenerFactories.remove(listenerFactory);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public <M_WIDGET_TYPE extends IControl> M_WIDGET_TYPE add(
		final int index,
		final IWidgetDescriptor<? extends M_WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		if (isInitialized()) {
			return getWidget().add(index, descriptor, layoutConstraints);
		}
		else {
			final Widget powo = widgetFactory.create(descriptor);
			preWidgets.add(index, new Tuple<Widget, Object>(powo, layoutConstraints));
			return (M_WIDGET_TYPE) powo;
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public final <M_WIDGET_TYPE extends IControl> M_WIDGET_TYPE add(
		final IWidgetDescriptor<? extends M_WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		if (isInitialized()) {
			return getWidget().add(descriptor, layoutConstraints);
		}
		else {
			final Widget powo = widgetFactory.create(descriptor);
			preWidgets.add(new Tuple<Widget, Object>(powo, layoutConstraints));
			return (M_WIDGET_TYPE) powo;
		}
	}

	@Override
	public <M_WIDGET_TYPE extends IControl> M_WIDGET_TYPE add(final IWidgetDescriptor<? extends M_WIDGET_TYPE> descriptor) {
		return add(descriptor, null);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<IControl> getChildren() {
		if (isInitialized()) {
			return getWidget().getChildren();
		}
		else {
			final List<IControl> result = new LinkedList<IControl>();
			for (final Tuple<Widget, Object> preWidgetTuple : preWidgets) {
				result.add((IControl) preWidgetTuple.getFirst());
			}
			return result;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean remove(final IControl control) {
		if (isInitialized()) {
			return getWidget().remove(control);
		}
		else {
			Assert.paramNotNull(control, "control");
			Tuple<Widget, Object> foundControl = null;
			for (final Tuple<Widget, Object> preWidgetTuple : preWidgets) {
				if (control == preWidgetTuple.getFirst()) {
					foundControl = preWidgetTuple;
					break;
				}
			}
			if (foundControl != null) {
				final boolean removed = preWidgets.remove(foundControl);
				if (removed) {
					return true;
				}
				else {
					throw new IllegalStateException(
						"Control could not be removed from pre widget list. This seems to be a bug. Please report this.");
				}
			}
			else {
				return false;
			}
		}
	}

	@Override
	public boolean isReparentable() {
		checkInitialized();
		return getWidget().isReparentable();
	}

	@Override
	public final void setLayout(final ILayoutDescriptor layoutDescriptor) {
		if (isInitialized()) {
			getWidget().setLayout(layoutDescriptor);
		}
		else {
			getBluePrint().setLayout(layoutDescriptor);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <LAYOUT_TYPE extends ILayouter> LAYOUT_TYPE setLayout(final ILayoutFactory<LAYOUT_TYPE> layoutFactory) {
		if (isInitialized()) {
			return getWidget().setLayout(layoutFactory);
		}
		else {
			final ILayouter result = layoutFactory.create(this);
			getBluePrint().setLayout(result);
			return (LAYOUT_TYPE) result;
		}
	}

	@Override
	public final void removeAll() {
		if (isInitialized()) {
			getWidget().removeAll();
		}
		else {
			preWidgets.clear();
		}
	}

	@Override
	public Rectangle getClientArea() {
		checkInitialized();
		return getWidget().getClientArea();
	}

	@Override
	public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
		checkInitialized();
		return getWidget().computeDecoratedSize(clientAreaSize);
	}

	@Override
	public void layout() {
		checkInitialized();
		getWidget().layout();
	}

	@Override
	public final void layoutBegin() {
		checkInitialized();
		getWidget().layoutBegin();
	}

	@Override
	public final void layoutEnd() {
		checkInitialized();
		getWidget().layoutEnd();
	}

	@Override
	public <M_WIDGET_TYPE extends IControl> M_WIDGET_TYPE add(
		final int index,
		final ICustomWidgetCreator<M_WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		checkInitialized();
		return getWidget().add(index, creator, layoutConstraints);
	}

	@Override
	public final <M_WIDGET_TYPE extends IControl> M_WIDGET_TYPE add(
		final ICustomWidgetCreator<M_WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		checkInitialized();
		return getWidget().add(creator, layoutConstraints);
	}

	@Override
	public <M_WIDGET_TYPE extends IControl> M_WIDGET_TYPE add(final ICustomWidgetCreator<M_WIDGET_TYPE> creator) {
		return add(creator, null);
	}

}
