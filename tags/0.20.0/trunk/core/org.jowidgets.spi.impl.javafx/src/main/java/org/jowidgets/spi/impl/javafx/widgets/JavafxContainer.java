/*
 * Copyright (c) 2012, David Bauknecht
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

package org.jowidgets.spi.impl.javafx.widgets;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.spi.impl.javafx.layout.LayoutPane;
import org.jowidgets.spi.impl.javafx.util.CursorConvert;
import org.jowidgets.spi.widgets.IContainerSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IFactory;
import org.tbee.javafx.scene.layout.MigPane;

public class JavafxContainer implements IContainerSpi {

	private final IGenericWidgetFactory factory;
	private final JavafxComponent componentDelegate;

	public JavafxContainer(final IGenericWidgetFactory factory) {
		this(factory, new LayoutPane());
	}

	public JavafxContainer(final IGenericWidgetFactory factory, final Pane pane) {
		this.factory = factory;
		componentDelegate = new JavafxComponent(pane);
	}

	@Override
	public Pane getUiReference() {
		return (Pane) componentDelegate.getUiReference();
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return componentDelegate.createPopupMenu();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		componentDelegate.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return componentDelegate.isEnabled();
	}

	@Override
	public void redraw() {
		componentDelegate.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		componentDelegate.setRedrawEnabled(enabled);
	}

	@Override
	public boolean requestFocus() {
		return componentDelegate.requestFocus();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		componentDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		componentDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return componentDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return componentDelegate.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		getUiReference().setCursor(CursorConvert.convert(cursor));
	}

	@Override
	public void setVisible(final boolean visible) {
		getUiReference().setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return getUiReference().isVisible();
	}

	@Override
	public Dimension getSize() {
		return new Dimension(
			(int) getUiReference().getLayoutBounds().getWidth(),
			(int) getUiReference().getLayoutBounds().getHeight());

	}

	@Override
	public void setSize(final Dimension size) {
		componentDelegate.setSize(size);
	}

	@Override
	public Position getPosition() {
		return componentDelegate.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		componentDelegate.setPosition(position);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		componentDelegate.addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		componentDelegate.removeComponentListener(componentListener);
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		componentDelegate.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		componentDelegate.removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		componentDelegate.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		componentDelegate.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener listener) {
		componentDelegate.addMouseListener(listener);
	}

	@Override
	public void removeMouseListener(final IMouseListener listener) {
		componentDelegate.removeMouseListener(listener);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		componentDelegate.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		componentDelegate.removePopupDetectionListener(listener);
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		Assert.paramNotNull(layoutDescriptor, "layout");

		if (layoutDescriptor instanceof ILayouter) {
			final ILayouter layouter = (ILayouter) layoutDescriptor;
			final Pane pane = getUiReference();
			if (pane instanceof LayoutPane) {
				((LayoutPane) pane).setLayouter(layouter);
			}
			else {
				createNewPane(new LayoutPaneFactory(layouter));
			}
		}
		else if (layoutDescriptor instanceof MigLayoutDescriptor) {
			final MigLayoutDescriptor migLayoutManager = (MigLayoutDescriptor) layoutDescriptor;
			createNewPane(new MigLayoutPaneFactory(migLayoutManager));
		}
		else {
			throw new IllegalArgumentException("Layout Descriptor of type '"
				+ layoutDescriptor.getClass().getName()
				+ "' is not supported");
		}
	}

	private void createNewPane(final IFactory<Pane> paneFactory) {

		final Pane pane = getUiReference();
		final Object userData = pane.getUserData();
		final Parent parent = pane.getParent();
		final Scene scene = pane.getScene();
		final ObservableList<Node> children = pane.getChildren();
		int index = 0;

		if (parent != null) {
			index = findIndexInParent(pane);
			((Pane) parent).getChildren().remove(pane);
		}

		//delegate creation of the new pane
		final Pane newPane = paneFactory.create();

		newPane.setUserData(userData);
		if (parent != null && index >= 0) {
			((Pane) parent).getChildren().add(index, newPane);
		}
		//if it was root add it again
		else if (scene != null) {
			scene.setRoot(newPane);
		}
		if (newPane instanceof MigPane) {
			for (final Node node : children) {
				((MigPane) newPane).add(node, (String) node.getUserData());
			}
		}
		else {
			newPane.getChildren().addAll(children);
		}
		componentDelegate.setComponent(newPane);
	}

	private int findIndexInParent(final Pane pane) {
		if (pane.getParent() != null) {
			for (final Node node : pane.getParent().getChildrenUnmodifiable()) {
				if (node == pane) {
					return pane.getParent().getChildrenUnmodifiable().indexOf(pane);
				}
			}
		}
		return -1;
	}

	@Override
	public void layoutBegin() {}

	@Override
	public void layoutEnd() {
		getUiReference().requestLayout();
		getUiReference().layout();
	}

	@Override
	public void removeAll() {
		getUiReference().getChildren().clear();
	}

	@Override
	public Rectangle getClientArea() {
		final Insets insets = getUiReference().getInsets();
		final int x = (int) insets.getLeft();
		final int y = (int) insets.getTop();
		final Dimension size = getSize();
		final int width = (int) (size.getWidth() - insets.getLeft() - insets.getRight());
		final int height = (int) (size.getHeight() - insets.getTop() - insets.getBottom());
		return new Rectangle(x, y, width, height);
	}

	@Override
	public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
		int width = clientAreaSize.getWidth();
		int height = clientAreaSize.getHeight();
		final Insets insets = getUiReference().getInsets();
		if (insets != null) {
			width = (int) (width + insets.getLeft() + insets.getRight());
			height = (int) (height + insets.getTop() + insets.getBottom());
		}
		return new Dimension(width, height);
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		final WIDGET_TYPE result = factory.create(getUiReference(), descriptor);
		addToContainer((Node) result.getUiReference(), layoutConstraints, index);
		setLayoutConstraints(result, layoutConstraints);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		final ICustomWidgetFactory customWidgetFactory = createCustomWidgetFactory();
		final WIDGET_TYPE result = creator.create(customWidgetFactory);
		addToContainer((Node) result.getUiReference(), layoutConstraints, index);
		setLayoutConstraints(result, layoutConstraints);
		return result;
	}

	private void addToContainer(final Node result, final Object layoutConstraints, final Integer index) {
		if (getUiReference() instanceof MigPane) {
			((MigPane) getUiReference()).add(result, (String) layoutConstraints);
		}
		else {
			if (index != null) {
				getUiReference().getChildren().add(index, result);
			}
			else {
				getUiReference().getChildren().add(result);
			}
		}
	}

	private ICustomWidgetFactory createCustomWidgetFactory() {
		return new ICustomWidgetFactory() {
			@Override
			public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE create(
				final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
				return factory.create(getUiReference(), descriptor);
			}
		};
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return getUiReference().getChildren().remove(control.getUiReference());
	}

	@Override
	public void setTabOrder(final List<? extends IControlCommon> tabOrder) {
		// TODO DB Auto-generated method stub
	}

	private void setLayoutConstraints(final IWidgetCommon widget, final Object layoutConstraints) {
		final Object object = widget.getUiReference();
		if (object instanceof Parent) {
			final Parent control = (Parent) object;
			control.setUserData(layoutConstraints);
		}
		else {
			throw new IllegalArgumentException("'"
				+ Parent.class
				+ "' excpected, but '"
				+ object.getClass().getName()
				+ "' found.");
		}
	}

	private final class LayoutPaneFactory implements IFactory<Pane> {

		private final ILayouter layouter;

		private LayoutPaneFactory(final ILayouter layouter) {
			this.layouter = layouter;
		}

		@Override
		public Pane create() {
			final LayoutPane result = new LayoutPane();
			result.setLayouter(layouter);
			return result;
		}
	}

	private final class MigLayoutPaneFactory implements IFactory<Pane> {

		private final MigLayoutDescriptor descriptor;

		private MigLayoutPaneFactory(final MigLayoutDescriptor descriptor) {
			this.descriptor = descriptor;
		}

		@Override
		public Pane create() {
			return new MigPane(
				descriptor.getLayoutConstraints(),
				descriptor.getColumnConstraints(),
				descriptor.getRowConstraints());
		}
	}

}
