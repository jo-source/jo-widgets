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

package org.jowidgets.spi.impl.swing.widgets.internal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;

import net.miginfocom.swing.MigLayout;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.spi.impl.swing.image.SwingImageRegistry;
import org.jowidgets.spi.impl.swing.widgets.SwingContainer;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.ITabItemSpi;
import org.jowidgets.spi.widgets.controler.TabItemObservableSpi;
import org.jowidgets.util.Assert;
import org.jowidgets.util.TypeCast;

public class TabItemImpl extends TabItemObservableSpi implements ITabItemSpi {

	private final IGenericWidgetFactory genericWidgetFactory;

	private final JPanel tabContentContainer;
	private final JPanel tabContent;
	private final TabComponent tabComponent;
	private final JTabbedPane tabbedPane;

	private SwingContainer swingContainer;

	public TabItemImpl(final IGenericWidgetFactory factory, final JTabbedPane parentFolder, final boolean closeable) {
		this(factory, parentFolder, closeable, null);
	}

	public TabItemImpl(
		final IGenericWidgetFactory genericWidgetFactory,
		final JTabbedPane parentFolder,
		final boolean closeable,
		final Integer index) {

		super();

		this.genericWidgetFactory = genericWidgetFactory;
		this.tabbedPane = parentFolder;

		this.tabContentContainer = new JPanel();
		this.tabContentContainer.setLayout(new MigLayout("", "0[grow, 0::]0", "0[grow, 0::]0"));

		if (index != null) {
			tabbedPane.add(tabContentContainer, index.intValue());
		}
		else {
			tabbedPane.add(tabContentContainer);
		}

		this.tabContent = new JPanel();
		this.tabContent.setLayout(new MigLayout("", "[]", "[]"));

		attachContent(tabContent);

		this.tabComponent = new TabComponent(closeable);
		tabbedPane.setTabComponentAt(getIndex(), tabComponent);

		parentFolder.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				if (parentFolder.getSelectedComponent() == tabContentContainer) {
					fireSelected();
				}
			}
		});

	}

	@Override
	public Object detachContent() {
		tabContentContainer.removeAll();
		tabContentContainer.revalidate();
		tabContentContainer.repaint();
		return tabContent;
	}

	@Override
	public void attachContent(final Object content) {
		Assert.paramNotNull(content, "content");
		final JPanel panel = TypeCast.toType(content, JPanel.class);
		tabContentContainer.removeAll();
		tabContentContainer.add(panel, "growx, growy, w 0::, h 0::");
		tabContentContainer.revalidate();
		tabContentContainer.repaint();
		this.swingContainer = new SwingContainer(genericWidgetFactory, panel);
	}

	@Override
	public boolean isReparentable() {
		return true;
	}

	@Override
	public void setText(final String text) {
		tabComponent.setText(text);
	}

	@Override
	public void setToolTipText(final String text) {
		tabComponent.setToolTipText(text);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		tabComponent.setIcon(icon);
	}

	private int getIndex() {
		for (int i = 0; i < tabbedPane.getComponentCount(); i++) {
			if (tabbedPane.getComponentAt(i) == tabContentContainer) {
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

	private final class TabComponent extends JPanel {

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
					if (SwingUtilities.isLeftMouseButton(e)) {
						final int i = tabbedPane.indexOfTabComponent(TabComponent.this);
						if (i != -1) {
							if (tabbedPane.getSelectedIndex() != i) {
								tabbedPane.setSelectedIndex(i);
							}
						}
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

	}

	private final class TabButton extends JButton {

		private static final long serialVersionUID = 1171212461380220004L;

		public TabButton() {

			final int size = 17;
			setPreferredSize(new java.awt.Dimension(size, size));
			setToolTipText("close this tab");
			setUI(new BasicButtonUI());
			setContentAreaFilled(false);
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			setRolloverEnabled(true);

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(final MouseEvent e) {
					final Component component = e.getComponent();
					if (component instanceof AbstractButton) {
						final AbstractButton button = (AbstractButton) component;
						button.setBorderPainted(true);
					}
				}

				@Override
				public void mouseExited(final MouseEvent e) {
					final Component component = e.getComponent();
					if (component instanceof AbstractButton) {
						final AbstractButton button = (AbstractButton) component;
						button.setBorderPainted(false);
					}
				}
			});

			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					final int i = tabbedPane.indexOfTabComponent(tabComponent);
					if (i != -1) {
						final boolean veto = fireOnClose();
						if (!veto) {
							tabbedPane.remove(i);
						}
						//else{do nothing}
					}
				}
			});

		}

		@Override
		public void updateUI() {}

		@Override
		protected void paintComponent(final Graphics g) {
			super.paintComponent(g);

			if (getModel().isRollover() || tabComponent.isRollover() || tabbedPane.getSelectedComponent() == tabContentContainer) {

				final Graphics2D g2 = (Graphics2D) g.create();
				if (getModel().isPressed()) {
					g2.translate(1, 1);
				}
				g2.setStroke(new BasicStroke(3));
				g2.setColor(Color.BLACK);

				final int delta = 5;
				g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
				g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
				g2.dispose();
			}
		}

	}
}
