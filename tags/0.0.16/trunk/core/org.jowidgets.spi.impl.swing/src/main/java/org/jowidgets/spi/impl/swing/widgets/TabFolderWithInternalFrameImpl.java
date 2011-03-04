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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.jowidgets.common.types.TabPlacement;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.swing.widgets.TabItemImpl.TabComponent;
import org.jowidgets.spi.widgets.ITabFolderSpi;
import org.jowidgets.spi.widgets.ITabItemSpi;
import org.jowidgets.spi.widgets.setup.ITabFolderSetupSpi;
import org.jowidgets.spi.widgets.setup.ITabItemSetupSpi;
import org.jowidgets.util.Assert;
import org.jowidgets.util.TypeCast;

public class TabFolderWithInternalFrameImpl extends SwingControl implements ITabFolderSpi {

	private final IGenericWidgetFactory widgetFactory;
	private final boolean tabsCloseable;
	private final List<TabItemImpl> items;
	private final JTabbedPane tabbedPane;

	public TabFolderWithInternalFrameImpl(final IGenericWidgetFactory widgetFactory, final ITabFolderSetupSpi setup) {
		super(new JInternalFrame());

		this.tabbedPane = new JTabbedPane();
		this.items = new LinkedList<TabItemImpl>();

		//avoid that internal frame could be dragged 
		//TODO MG this may work not for all LookAndFeels
		final JInternalFrame internalFrame = getUiReference();
		final InternalFrameUI internalFrameUI = internalFrame.getUI();
		if (internalFrameUI instanceof BasicInternalFrameUI) {
			final BasicInternalFrameUI ui = (BasicInternalFrameUI) getUiReference().getUI();
			final Component northPane = ui.getNorthPane();
			for (final MouseMotionListener listener : northPane.getListeners(MouseMotionListener.class)) {
				northPane.removeMouseMotionListener(listener);
			}
		}

		internalFrame.setFrameIcon(null);
		internalFrame.getContentPane().setLayout(new BorderLayout());
		internalFrame.setVisible(true);
		internalFrame.setResizable(false);
		internalFrame.getContentPane().add(BorderLayout.CENTER, tabbedPane);

		//set the title to the selected tab
		this.tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				setSelectedTitle();
			}
		});

		//fix the fat border for internal frame (e.g in win7)
		final Border border = getUiReference().getBorder();
		final Border newBorder = new Border() {

			@Override
			public void paintBorder(
				final Component c,
				final Graphics g,
				final int x,
				final int y,
				final int width,
				final int height) {
				border.paintBorder(c, g, x, y, width, height);
			}

			@Override
			public boolean isBorderOpaque() {
				return border.isBorderOpaque();
			}

			@Override
			public Insets getBorderInsets(final Component c) {
				final Insets boderInsets = border.getBorderInsets(c);
				return new Insets(boderInsets.top, 4, 4, 4);
			}
		};
		getUiReference().setBorder(newBorder);

		this.tabsCloseable = setup.isTabsCloseable();
		this.widgetFactory = widgetFactory;

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		if (setup.getTabPlacement() == TabPlacement.TOP) {
			tabbedPane.setTabPlacement(JTabbedPane.TOP);
		}
		else if (setup.getTabPlacement() == TabPlacement.BOTTOM) {
			tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		}
		else {
			throw new IllegalArgumentException("TabPlacement '" + setup.getTabPlacement() + "' is not known");
		}
	}

	private void setSelectedTitle() {
		final int selectedIndex = tabbedPane.getSelectedIndex();
		if (selectedIndex != -1) {
			final Component selectedTabComponent = tabbedPane.getTabComponentAt(selectedIndex);
			if (selectedTabComponent instanceof TabComponent) {
				final JLabel label = ((TabComponent) selectedTabComponent).getLabel();
				getUiReference().setTitle(label.getText());
				getUiReference().setFrameIcon(label.getIcon());
			}
			else {
				getUiReference().setTitle(null);
				getUiReference().setFrameIcon(null);
			}
		}
		else {
			getUiReference().setTitle(null);
			getUiReference().setFrameIcon(null);
		}
	}

	@Override
	public JInternalFrame getUiReference() {
		return (JInternalFrame) super.getUiReference();
	}

	@Override
	public void removeItem(final int index) {
		getUiReference().remove(index);
		items.remove(index);
		setSelectedTitle();
	}

	@Override
	public ITabItemSpi addItem(final ITabItemSetupSpi setup) {
		final TabItemImpl result = new TabItemImpl(widgetFactory, tabbedPane, tabsCloseable);
		items.add(result);
		setSelectedTitle();
		return result;
	}

	@Override
	public ITabItemSpi addItem(final int index, final ITabItemSetupSpi setup) {
		final TabItemImpl result = new TabItemImpl(widgetFactory, tabbedPane, tabsCloseable, Integer.valueOf(index));
		items.add(index, result);
		setSelectedTitle();
		return result;
	}

	@Override
	public void setSelectedItem(final int index) {
		tabbedPane.setSelectedIndex(index);
		setSelectedTitle();
	}

	@Override
	public int getSelectedIndex() {
		return tabbedPane.getSelectedIndex();
	}

	@Override
	public void detachItem(final ITabItemSpi item) {
		Assert.paramNotNull(item, "item");
		final TabItemImpl itemImpl = TypeCast.toType(item, TabItemImpl.class);
		final int itemIndex = items.indexOf(itemImpl);
		if (itemIndex == -1) {
			throw new IllegalArgumentException("Item is not attached to this folder");
		}
		if (itemImpl.isDetached()) {
			throw new IllegalArgumentException("Item is already detached");
		}

		items.remove(itemIndex);
		itemImpl.detach();
		setSelectedTitle();
	}

	@Override
	public void attachItem(final ITabItemSpi item) {
		Assert.paramNotNull(item, "item");
		final TabItemImpl itemImpl = TypeCast.toType(item, TabItemImpl.class);
		if (!itemImpl.isDetached()) {
			throw new IllegalArgumentException("Item is not detached");
		}
		itemImpl.attach(tabbedPane, tabsCloseable, null);
		items.add(itemImpl);
		setSelectedTitle();
	}

	@Override
	public void attachItem(final int index, final ITabItemSpi item) {
		Assert.paramNotNull(item, "item");
		final TabItemImpl itemImpl = TypeCast.toType(item, TabItemImpl.class);
		if (!itemImpl.isDetached()) {
			throw new IllegalArgumentException("Item is not detached");
		}
		itemImpl.attach(tabbedPane, tabsCloseable, Integer.valueOf(index));
		items.add(index, itemImpl);
		setSelectedTitle();
	}

}
