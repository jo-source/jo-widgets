/*
 * Copyright (c) 2013, Michael Grossmann
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

import java.util.Collection;
import java.util.List;

import org.jowidgets.api.controller.IContainerListener;
import org.jowidgets.api.controller.IContainerRegistry;
import org.jowidgets.api.controller.IExpandListener;
import org.jowidgets.api.controller.IListenerFactory;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IExpandComposite;
import org.jowidgets.api.widgets.ILabel;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarButton;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ILabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IToolBarButtonBluePrint;
import org.jowidgets.api.widgets.descriptor.setup.IExpandCompositeSetup;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Insets;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.tools.controller.ExpandObservable;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;

public final class ExpandCompositeImpl extends ControlWrapper implements IExpandComposite {

	private final ExpandObservable expandObservable;
	private final IComposite header;
	private final IToolBar toolBar;
	private final ILabel label;
	private final IComposite labelContainer;
	private final IToolBarButton button;
	private final IComposite content;

	private IControl customHeader;

	public ExpandCompositeImpl(final IComposite composite, final IExpandCompositeSetup setup) {
		super(composite);

		this.expandObservable = new ExpandObservable(setup.isExpanded());

		composite.setLayout(new MigLayoutDescriptor("hidemode 2, wrap", "0[grow, 0::]0", "0[]0[grow, 0::]0"));

		final ICompositeBluePrint headerBp = BPF.composite().setBackgroundColor(setup.getHeaderBackgroundColor());
		if (setup.getHeaderBorder()) {
			headerBp.setBorder();
		}
		this.header = composite.add(headerBp, "growx, w 0::");

		final Insets insets = setup.getInsets();
		final String left = "" + insets.getLeft();
		final String right = "" + insets.getRight();
		final String top = "" + insets.getTop();
		final String bottom = "" + insets.getBottom();
		header.setLayout(new MigLayoutDescriptor(left + "[grow, 0::][]" + right, top + "[]" + bottom));

		final ILabelBluePrint labelBp = BPF.label().setIcon(setup.getIcon()).setText(setup.getText());
		labelBp.setMarkup(setup.getTextMarkup()).setForegroundColor(setup.getTextColor());
		this.labelContainer = header.add(BPF.composite(), "growx, w 0::");
		labelContainer.setLayout(new MigLayoutDescriptor("hidemode 3", "0[grow, 0::]0", "0[]0"));
		this.label = labelContainer.add(labelBp, "growx, w 0::");
		final ICustomWidgetCreator<? extends IControl> customHeaderCreator = setup.getCustomHeader();
		if (customHeaderCreator != null) {
			customHeader = labelContainer.add(customHeaderCreator, "growx, w 0::");
			label.setVisible(false);
		}

		this.toolBar = header.add(BPF.toolBar().setBackgroundColor(setup.getHeaderBackgroundColor()));
		final IToolBarButtonBluePrint buttonBp = BPF.toolBarButton();
		if (setup.isExpanded()) {
			buttonBp.setIcon(IconsSmall.EXPAND_UP);
		}
		else {
			buttonBp.setIcon(IconsSmall.EXPAND_DOWN);
		}
		this.button = toolBar.addItem(buttonBp);

		final ICompositeBluePrint contentBp = BPF.composite();
		if (setup.getContentBorder()) {
			contentBp.setBorder();
		}
		this.content = composite.add(contentBp, "growx, growy, w 0::, h 0::");
		content.setVisible(setup.isExpanded());

		button.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				toggle();
			}
		});

		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, content);
	}

	@Override
	protected IComposite getWidget() {
		return (IComposite) super.getWidget();
	}

	private void toggle() {
		content.setVisible(!content.isVisible());
		if (content.isVisible()) {
			button.setIcon(IconsSmall.EXPAND_UP);
		}
		else {
			button.setIcon(IconsSmall.EXPAND_DOWN);
		}
		expandObservable.fireExpandedChanged(content.isVisible());
	}

	@Override
	public void setEnabled(final boolean enabled) {
		toolBar.setVisible(enabled);
	}

	@Override
	public void setText(final String text) {
		label.setText(text);
	}

	@Override
	public String getText() {
		return label.getText();
	}

	@Override
	public void setCustomHeader(final ICustomWidgetCreator<? extends IControl> header) {
		getWidget().layoutBegin();
		if (customHeader != null) {
			labelContainer.remove(customHeader);
			customHeader = null;
		}
		if (header != null) {
			customHeader = labelContainer.add(header, "growx, w 0::");
			label.setVisible(false);
		}
		else {
			label.setVisible(true);
		}
		getWidget().layoutEnd();
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		label.setIcon(icon);
	}

	@Override
	public IImageConstant getIcon() {
		return label.getIcon();
	}

	@Override
	public void setTextMarkup(final Markup markup) {
		label.setMarkup(markup);
	}

	@Override
	public void setTextColor(final IColorConstant color) {
		label.setForegroundColor(color);
	}

	@Override
	public IColorConstant getTextColor() {
		return label.getForegroundColor();
	}

	@Override
	public void setHeaderBackgroundColor(final IColorConstant color) {
		header.setBackgroundColor(color);
		toolBar.setBackgroundColor(color);
	}

	@Override
	public IColorConstant getHeaderBackgroundColor() {
		return header.getBackgroundColor();
	}

	@Override
	public void setContentBackgroundColor(final IColorConstant color) {
		content.setBackgroundColor(color);
	}

	@Override
	public IColorConstant getContentBackgroundColor() {
		return content.getBackgroundColor();
	}

	@Override
	public void setExpanded(final boolean expanded) {
		if (content.isVisible() != expanded) {
			toggle();
		}
	}

	@Override
	public boolean isExpanded() {
		return content.isVisible();
	}

	@Override
	public void addExpandListener(final IExpandListener listener) {
		expandObservable.addExpandListener(listener);
	}

	@Override
	public void removeExpandListener(final IExpandListener listener) {
		expandObservable.removeExpandListener(listener);
	}

	@Override
	public void addContainerListener(final IContainerListener listener) {
		content.addContainerListener(listener);
	}

	@Override
	public void removeContainerListener(final IContainerListener listener) {
		content.removeContainerListener(listener);
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		content.setLayout(layoutDescriptor);
	}

	@Override
	public void layoutBegin() {
		content.layoutBegin();
	}

	@Override
	public void layoutEnd() {
		content.layoutEnd();
	}

	@Override
	public void removeAll() {
		content.removeAll();
	}

	@Override
	public Rectangle getClientArea() {
		return content.getClientArea();
	}

	@Override
	public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
		return content.computeDecoratedSize(clientAreaSize);
	}

	@Override
	public <LAYOUT_TYPE extends ILayouter> LAYOUT_TYPE setLayout(final ILayoutFactory<LAYOUT_TYPE> layoutFactory) {
		return content.setLayout(layoutFactory);
	}

	@Override
	public void layout() {
		content.layout();
	}

	@Override
	public void addContainerRegistry(final IContainerRegistry registry) {
		content.addContainerRegistry(registry);
	}

	@Override
	public void removeContainerRegistry(final IContainerRegistry registry) {
		content.removeContainerRegistry(registry);
	}

	@Override
	public List<IControl> getChildren() {
		return content.getChildren();
	}

	@Override
	public boolean remove(final IControl control) {
		return content.remove(control);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final int index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return content.add(index, descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final int index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		return content.add(index, creator, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return content.add(descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		return content.add(creator, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
		return content.add(descriptor);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final ICustomWidgetCreator<WIDGET_TYPE> creator) {
		return content.add(creator);
	}

	@Override
	public void setTabOrder(final Collection<? extends IControl> tabOrder) {
		content.setTabOrder(tabOrder);
	}

	@Override
	public void setTabOrder(final IControl... controls) {
		content.setTabOrder(controls);
	}

	@Override
	public void addComponentListenerRecursive(final IListenerFactory<IComponentListener> listenerFactory) {
		content.addComponentListenerRecursive(listenerFactory);
	}

	@Override
	public void removeComponentListenerRecursive(final IListenerFactory<IComponentListener> listenerFactory) {
		content.removeComponentListenerRecursive(listenerFactory);
	}

	@Override
	public void addFocusListenerRecursive(final IListenerFactory<IFocusListener> listenerFactory) {
		content.addFocusListenerRecursive(listenerFactory);
	}

	@Override
	public void removeFocusListenerRecursive(final IListenerFactory<IFocusListener> listenerFactory) {
		content.removeFocusListenerRecursive(listenerFactory);
	}

	@Override
	public void addKeyListenerRecursive(final IListenerFactory<IKeyListener> listenerFactory) {
		content.addKeyListenerRecursive(listenerFactory);
	}

	@Override
	public void removeKeyListenerRecursive(final IListenerFactory<IKeyListener> listenerFactory) {
		content.removeKeyListenerRecursive(listenerFactory);
	}

	@Override
	public void addMouseListenerRecursive(final IListenerFactory<IMouseListener> listenerFactory) {
		content.addMouseListenerRecursive(listenerFactory);
	}

	@Override
	public void removeMouseListenerRecursive(final IListenerFactory<IMouseListener> listenerFactory) {
		content.removeMouseListenerRecursive(listenerFactory);
	}

	@Override
	public void addPopupDetectionListenerRecursive(final IListenerFactory<IPopupDetectionListener> listenerFactory) {
		content.addPopupDetectionListenerRecursive(listenerFactory);
	}

	@Override
	public void removePopupDetectionListenerRecursive(final IListenerFactory<IPopupDetectionListener> listenerFactory) {
		content.removePopupDetectionListenerRecursive(listenerFactory);
	}

}
