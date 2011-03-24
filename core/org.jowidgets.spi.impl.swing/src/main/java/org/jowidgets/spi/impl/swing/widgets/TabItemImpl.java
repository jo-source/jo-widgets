/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.spi.impl.swing.widgets;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;

import net.miginfocom.swing.MigLayout;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.image.IconsSmallCommon;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.spi.impl.controler.PopupDetectionObservable;
import org.jowidgets.spi.impl.controler.TabItemObservableSpi;
import org.jowidgets.spi.impl.swing.image.SwingImageRegistry;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.ITabItemSpi;

public class TabItemImpl extends TabItemObservableSpi implements ITabItemSpi {

	private final JPanel tabContentContainer;
	private final SwingContainer swingContainer;
	private final PopupDetectionObservable tabPopupDetectionObservable;
	private final Set<PopupMenuImpl> tabPopupMenus;
	private final ChangeListener tabSelectionListener;

	private JTabbedPane parentTabbedPane;
	private TabComponent tabComponent;
	private boolean detached;
	private String text;
	private String toolTipText;
	private IImageConstant icon;

	public TabItemImpl(final IGenericWidgetFactory factory, final JTabbedPane parentFolder, final boolean closeable) {
		this(factory, parentFolder, closeable, null);
	}

	public TabItemImpl(
		final IGenericWidgetFactory genericWidgetFactory,
		final JTabbedPane parentTabbedPane,
		final boolean closeable,
		final Integer index) {

		super();

		this.parentTabbedPane = parentTabbedPane;

		this.tabPopupDetectionObservable = new PopupDetectionObservable();
		this.tabPopupMenus = new HashSet<PopupMenuImpl>();

		this.tabContentContainer = new JPanel();
		this.tabContentContainer.setLayout(new MigLayout("", "[]", "[]"));

		if (index != null) {
			parentTabbedPane.add(tabContentContainer, index.intValue());
		}
		else {
			parentTabbedPane.add(tabContentContainer);
		}

		this.swingContainer = new SwingContainer(genericWidgetFactory, tabContentContainer);

		this.tabComponent = new TabComponent(closeable);
		parentTabbedPane.setTabComponentAt(getIndex(), tabComponent);

		this.tabSelectionListener = new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				if (getParentTabbedPane().getSelectedComponent() == tabContentContainer) {
					fireSelected();
				}
			}
		};

		parentTabbedPane.addChangeListener(tabSelectionListener);
	}

	@Override
	public void addTabPopupDetectionListener(final IPopupDetectionListener listener) {
		tabPopupDetectionObservable.addPopupDetectionListener(listener);
	}

	@Override
	public void removeTabPopupDetectionListener(final IPopupDetectionListener listener) {
		tabPopupDetectionObservable.removePopupDetectionListener(listener);
	}

	@Override
	public IPopupMenuSpi createTabPopupMenu() {
		final PopupMenuImpl result = new PopupMenuImpl(tabComponent);
		tabPopupMenus.add(result);
		return result;
	}

	public void detach() {
		detached = true;
		parentTabbedPane.remove(tabContentContainer);
		parentTabbedPane.removeChangeListener(tabSelectionListener);
	}

	public void attach(final JTabbedPane parentTabbedPane, final boolean closeable, final Integer index) {
		this.detached = false;
		this.parentTabbedPane = parentTabbedPane;
		this.parentTabbedPane.addChangeListener(tabSelectionListener);

		if (index != null) {
			parentTabbedPane.add(tabContentContainer, index.intValue());
		}
		else {
			parentTabbedPane.add(tabContentContainer);
		}

		this.tabComponent = new TabComponent(closeable);

		for (final PopupMenuImpl popupMenu : tabPopupMenus) {
			popupMenu.setParent(tabComponent);
		}

		setText(text);
		setToolTipText(toolTipText);
		setIcon(icon);
		parentTabbedPane.setTabComponentAt(getIndex(), tabComponent);
	}

	private JTabbedPane getParentTabbedPane() {
		return parentTabbedPane;
	}

	public boolean isDetached() {
		return detached;
	}

	@Override
	public boolean isReparentable() {
		return true;
	}

	@Override
	public void setText(final String text) {
		this.text = text;
		tabComponent.setText(text);
	}

	@Override
	public void setToolTipText(final String toolTipText) {
		this.toolTipText = toolTipText;
		tabComponent.setToolTipText(toolTipText);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		this.icon = icon;
		tabComponent.setIcon(icon);
	}

	private int getIndex() {
		for (int i = 0; i < parentTabbedPane.getComponentCount(); i++) {
			if (parentTabbedPane.getComponentAt(i) == tabContentContainer) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object getUiReference() {
		return swingContainer.getUiReference();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		swingContainer.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return swingContainer.isEnabled();
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return swingContainer.add(descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> factory,
		final Object layoutConstraints) {
		return swingContainer.add(factory, layoutConstraints);
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return swingContainer.remove(control);
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return swingContainer.createPopupMenu();
	}

	@Override
	public void redraw() {
		swingContainer.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		swingContainer.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		swingContainer.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return swingContainer.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return swingContainer.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		swingContainer.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		swingContainer.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return swingContainer.isVisible();
	}

	@Override
	public Dimension getSize() {
		return swingContainer.getSize();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		swingContainer.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		swingContainer.removePopupDetectionListener(listener);
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		swingContainer.setLayout(layoutDescriptor);
	}

	@Override
	public void layoutBegin() {
		swingContainer.layoutBegin();
	}

	@Override
	public void layoutEnd() {
		swingContainer.layoutEnd();
	}

	@Override
	public void removeAll() {
		swingContainer.removeAll();
	}

	protected String getText() {
		return text;
	}

	public final class TabComponent extends JPanel {

		private static final long serialVersionUID = 7620592636356667921L;
		private final JLabel label;
		private final TabButton tabButton;
		private boolean rollover;

		private TabComponent(final boolean closeable) {
			setOpaque(false);
			setBorder(BorderFactory.createEmptyBorder());
			this.rollover = false;
			this.label = new JLabel();
			this.label.setOpaque(false);
			if (closeable) {
				setLayout(new MigLayout("", "0[]10[]0", "0[]0"));
				add(label);
				this.tabButton = new TabButton();
				add(tabButton);
			}
			else {
				this.tabButton = null;
				setLayout(new MigLayout("", "0[]0", "0[]0"));
				add(label);
			}

			final MouseListener mouseListener = new MouseAdapter() {
				@Override
				public void mousePressed(final MouseEvent e) {
					final int i = parentTabbedPane.indexOfTabComponent(TabComponent.this);
					if (i != -1) {
						if (parentTabbedPane.getSelectedIndex() != i) {
							parentTabbedPane.setSelectedIndex(i);
						}
					}
					if (e.isPopupTrigger()) {
						tabPopupDetectionObservable.firePopupDetected(new Position(e.getX(), e.getY()));
					}
				}

				@Override
				public void mouseReleased(final MouseEvent e) {
					if (e.isPopupTrigger()) {
						tabPopupDetectionObservable.firePopupDetected(new Position(e.getX(), e.getY()));
					}
				}

				@Override
				public void mouseEntered(final MouseEvent e) {
					rollover = true;
					repaintTabButton();
				}

				@Override
				public void mouseExited(final MouseEvent e) {
					rollover = false;
					repaintTabButton();
				}

			};

			addMouseListener(mouseListener);
			label.addMouseListener(mouseListener);
		}

		private void repaintTabButton() {
			if (tabButton != null) {
				tabButton.repaint();
			}
		}

		public void setText(final String text) {
			label.setText(text);
			if (tabButton != null) {
				tabButton.setText(text);
			}
		}

		@Override
		public void setToolTipText(final String text) {
			label.setToolTipText(text);
		}

		public void setIcon(final IImageConstant icon) {
			label.setIcon(SwingImageRegistry.getInstance().getImageIcon(icon));
		}

		public boolean isRollover() {
			return rollover;
		}

		public JLabel getLabel() {
			return label;
		}

	}

	private final class TabButton extends JButton {

		private static final long serialVersionUID = 1171212461380220004L;

		private static final String CLOSE_LABEL = "Close"; //TODO I18N

		public TabButton() {

			setPreferredSize(new java.awt.Dimension(16, 16));
			setMargin(new Insets(0, 0, 0, 0));
			setUI(new BasicButtonUI());
			setContentAreaFilled(false);
			setFocusable(false);
			setRolloverEnabled(true);
			setBorder(BorderFactory.createEmptyBorder());

			setToolTipText(CLOSE_LABEL);

			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					final int i = parentTabbedPane.indexOfTabComponent(tabComponent);
					if (i != -1) {
						final boolean veto = fireOnClose();
						if (!veto) {
							parentTabbedPane.remove(i);
						}
						//else{do nothing}
					}
				}
			});

		}

		@Override
		public void setText(final String text) {
			setToolTipText(CLOSE_LABEL + " " + text);
		}

		@Override
		public void updateUI() {}

		@Override
		protected void paintComponent(final Graphics g) {
			super.paintComponent(g);
			if (getModel().isRollover()
				|| tabComponent.isRollover()
				|| parentTabbedPane.getSelectedComponent() == tabContentContainer) {
				if (getModel().isPressed()) {
					g.translate(1, 1);
				}
				if (getModel().isRollover()) {
					SwingImageRegistry.getInstance().getImageIcon(IconsSmallCommon.CLOSE_MOUSEOVER).paintIcon(this, g, 0, 0);
				}
				else {
					SwingImageRegistry.getInstance().getImageIcon(IconsSmallCommon.CLOSE).paintIcon(this, g, 0, 0);
				}
			}
		}

	}
}
