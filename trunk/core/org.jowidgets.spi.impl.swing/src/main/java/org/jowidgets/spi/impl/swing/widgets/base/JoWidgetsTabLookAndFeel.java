/*
 * Copyright (c) 2011, Nikolaus Moll
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

package org.jowidgets.spi.impl.swing.widgets.base;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import org.jowidgets.spi.impl.swing.widgets.TabItemImpl.TabComponent;

public class JoWidgetsTabLookAndFeel extends BasicTabbedPaneUI {
	private static final Color SELECTED_COLOR = Color.WHITE; // background color of selected tab
	private static final int ARC_SIZE = 2;
	private static final Color ARROW_COLOR = new Color(80, 80, 80);

	private int firstVisibleTab = 0;
	private int lastVisibleTab = 0;

	private PopupToolbar popupToolbar;
	private JoWidgetsTabbedPaneLayout layout;

	public JoWidgetsTabLookAndFeel() {
		super();
	}

	@Override
	protected void installDefaults() {
		// set defaults before parent is querying them
		tabInsets = UIManager.getInsets("TabbedPane.tabInsets");
		UIManager.getDefaults().put("TabbedPane.selected", SELECTED_COLOR);
		UIManager.getDefaults().put("TabbedPane.selectedTabPadInsets", tabInsets);
		UIManager.getDefaults().put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0));

		super.installDefaults();
		lightHighlight = Color.gray;
		// think about resetting defaults here
	}

	/**
	 * Add toolbar(s)
	 */
	@Override
	protected void installComponents() {
		if (popupToolbar == null) {
			popupToolbar = new PopupToolbar();
			tabPane.add(popupToolbar);
		}
		super.installComponents();
	}

	/**
	 * Add toolbar(s)
	 */
	@Override
	protected void uninstallComponents() {
		if (popupToolbar != null) {
			tabPane.remove(popupToolbar);
			popupToolbar = null;
		}
		super.uninstallComponents();
	}

	/**
	 * Draw roundes borders
	 */
	@Override
	protected void paintTabBorder(
		final Graphics g,
		final int tabPlacement,
		final int tabIndex,
		final int x,
		final int y,
		final int w,
		final int h,
		final boolean isSelected) {
		g.setColor(lightHighlight);

		final boolean isAfterSelected = isAfterSelected(tabIndex);

		final boolean hasLeftArc = isSelected || isFirstVisibleTab(tabIndex);
		final boolean hasRightArc = isSelected;

		if (tabPlacement == TOP) {
			final int y1 = y;
			final int y2 = y + h;
			final int x1 = x;
			final int x2 = x + w;
			if (hasLeftArc) {
				g.drawLine(x1, y1 + ARC_SIZE, x1 + ARC_SIZE, y1); // arc
				g.drawLine(x1, y1 + ARC_SIZE, x1, y2); // left down
				if (!isFirstVisibleTab(tabIndex)) {
					g.drawLine(x1, y1, x1 + ARC_SIZE, y1); // top connector
				}
			}
			else {
				g.drawLine(x1, y1, x1 + ARC_SIZE, y1); // top connector
				if (!isAfterSelected) {
					g.drawLine(x1, y1, x1, y2);
				}
			}
			if (hasRightArc) {
				g.drawLine(x2 - ARC_SIZE, y1, x2, y1 + ARC_SIZE); // arc
				g.drawLine(x2, y1 + ARC_SIZE, x2, y2); // right down
			}
			else {
				if (isLastVisibleTab(tabIndex)) {
					g.drawLine(x2, y1, x2, y2);
				}
			}

			g.drawLine(x1 + ARC_SIZE, y1, x2 - ARC_SIZE, y1); // top line              
			g.drawLine(x2 - ARC_SIZE, y1, x2, y1); // top connector
		}
		else { // BOTTOM
			final int y1 = y;
			final int y2 = y + h - 1;
			final int x1 = x;
			final int x2 = x + w;

			if (hasLeftArc) {
				g.drawLine(x1, y2 - ARC_SIZE, x1 + ARC_SIZE, y2); // arc
				g.drawLine(x1, y2 - ARC_SIZE, x1, y1); // left down
				if (!isFirstVisibleTab(tabIndex)) {
					g.drawLine(x1, y2, x1 + ARC_SIZE, y2); // bottom connector
				}
			}
			else {
				g.drawLine(x1, y2, x1 + ARC_SIZE, y2); // bottom connector
				if (!isAfterSelected) {
					g.drawLine(x1, y1, x1, y2);
				}
			}
			if (hasRightArc) {
				g.drawLine(x2 - ARC_SIZE, y2, x2, y2 - ARC_SIZE); // arc
				g.drawLine(x2, y2 - ARC_SIZE, x2, y1); // right down
			}
			else {
				if (isLastVisibleTab(tabIndex)) {
					g.drawLine(x2, y1, x2, y2);
				}
			}

			g.drawLine(x1 + ARC_SIZE, y2, x2 - ARC_SIZE, y2); // bottom line              
			g.drawLine(x2 - ARC_SIZE, y2, x2, y2); // bottom connector
		}
	}

	/**
	 * Overridden because parent method draws a line out of the tabbed panes bounds when
	 * the selected tab has the x ordinate 0
	 */
	@Override
	protected void paintContentBorderTopEdge(
		final Graphics g,
		final int tabPlacement,
		final int selectedIndex,
		final int x,
		final int y,
		final int w,
		final int h) {
		g.setColor(lightHighlight);
		if (tabPlacement == BOTTOM || selectedIndex < 0) {
			g.drawLine(x, y, x + w - 2, y);
		}
		else {
			final Rectangle bounds = getTabBounds(selectedIndex, calcRect);
			g.drawLine(x, y, bounds.x, y);
			if (bounds.x + bounds.width < x + w - 2) {
				g.drawLine(bounds.x + bounds.width, y, x + w - 2, y);
			}
		}
	}

	/**
	 * Overridden because parent method draws a line out of the tabbed panes bounds when
	 * the selected tab has the x ordinate 0
	 */
	@Override
	protected void paintContentBorderBottomEdge(
		final Graphics g,
		final int tabPlacement,
		final int selectedIndex,
		final int x,
		final int y,
		final int w,
		final int h) {
		g.setColor(lightHighlight);
		final int lineY = y + h - 2;
		if (tabPlacement == TOP || selectedIndex < 0) {
			g.drawLine(x, lineY, x + w - 2, lineY);
		}
		else {
			final Rectangle bounds = getTabBounds(selectedIndex, calcRect);
			g.drawLine(x, lineY, bounds.x, lineY);
			if (bounds.x + bounds.width < x + w - 2) {
				g.drawLine(bounds.x + bounds.width, lineY, x + w - 2, lineY);
			}
		}
	}

	/**
	 * Overriden to draw rounded borders and additional line if tab count is 0
	 */
	@Override
	public void paint(final Graphics g, final JComponent c) {
		super.paint(g, c);
		final int tabPlacement = tabPane.getTabPlacement();
		final Insets insets = tabPane.getInsets();
		if (tabPlacement == TOP) {
			if (tabPane.getTabCount() > 0) {
				final Rectangle lastTab = rects[lastVisibleTab];

				final int edgeX1 = Math.max(insets.left, lastTab.x + lastTab.width);
				final int edgeX2 = (tabPane.getWidth() - 1) - insets.right - insets.left;
				final int edgeY1 = lastTab.y;
				final int edgeY2 = edgeY1 + lastTab.height;

				g.setColor(lightHighlight);
				g.drawLine(edgeX1, edgeY1, edgeX2 - ARC_SIZE, edgeY1); // top
				g.drawLine(edgeX2 - ARC_SIZE, edgeY1, edgeX2, edgeY1 + ARC_SIZE); // arc
				g.drawLine(edgeX2, edgeY1 + ARC_SIZE, edgeX2, edgeY2); // right

				// TODO NM find a way to determine if necessary
				// shadow linux
				//g.setColor(darkShadow);
				//g.drawLine(edgeX1 + edgeX2 - 1, edgeY1, edgeX1 + edgeX2 + 1, edgeY1 + 2);
				//g.drawLine(edgeX1 + edgeX2 + 1, edgeY1 + 3, edgeX1 + edgeX2 + 1, edgeY1 + edgeY2 + 2); // right
			}
			else {
				// empty tabpane
				final int lineY = insets.top + calculateTabHeight() + 2;
				final int w = tabPane.getWidth() - insets.right - insets.left;
				g.setColor(lightHighlight);
				g.drawLine(insets.left, lineY, w, lineY);

				g.setColor(tabPane.getBackground());
				final int arcY = insets.top;
				g.fillRect(insets.left, arcY, w, ARC_SIZE + 1);

				g.setColor(lightHighlight);
				g.drawLine(insets.left + ARC_SIZE, arcY, w - 2 * ARC_SIZE, arcY);
				g.drawLine(insets.left, arcY + ARC_SIZE, insets.left + ARC_SIZE, arcY);
				g.drawLine(tabPane.getWidth() - insets.right - insets.left - ARC_SIZE - 1, arcY, tabPane.getWidth()
					- insets.right
					- insets.left
					- 1, arcY + ARC_SIZE);

				// shadow in top right corner
				//g.setColor(darkShadow);
				//g.drawLine(tabPane.getWidth() - insets.right - insets.left - ARC_SIZE, arcY, tabPane.getWidth()
				//	- insets.right
				//	- insets.left, arcY + ARC_SIZE);
			}
		}
		else { // BOTTOM
			if (tabPane.getTabCount() > 0) {
				final Rectangle lastTab = rects[lastVisibleTab];

				final int edgeX1 = Math.max(insets.left, lastTab.x + lastTab.width);
				final int edgeX2 = (tabPane.getWidth() - 1) - insets.right - insets.left;
				final int edgeY1 = lastTab.y;
				final int edgeY2 = edgeY1 + lastTab.height - 1;

				g.setColor(lightHighlight);
				g.drawLine(edgeX1, edgeY2, edgeX2 - ARC_SIZE, edgeY2); // bottoom
				g.drawLine(edgeX2 - ARC_SIZE, edgeY2, edgeX2, edgeY2 - ARC_SIZE); // arc
				g.drawLine(edgeX2, edgeY2 - ARC_SIZE, edgeX2, edgeY1); // right

				// TODO NM find a way to determine if necessary
			}
			else {
				// empty tabpane
				final int lineY = tabPane.getHeight() - insets.top - calculateTabHeight() - 4; // TODO NM why 4?
				final int w = tabPane.getWidth() - insets.right - insets.left;
				g.setColor(lightHighlight);
				g.drawLine(insets.left, lineY, w, lineY);

				g.setColor(tabPane.getBackground());
				final int arcY = tabPane.getHeight() - insets.bottom - 2;
				g.fillRect(insets.left, arcY - ARC_SIZE, w, ARC_SIZE + 1);

				g.setColor(lightHighlight);
				g.drawLine(insets.left + ARC_SIZE, arcY, w - 2 * ARC_SIZE, arcY);
				g.drawLine(insets.left, arcY - ARC_SIZE, insets.left + ARC_SIZE, arcY);
				g.drawLine(tabPane.getWidth() - insets.right - insets.left - ARC_SIZE - 1, arcY, tabPane.getWidth()
					- insets.right
					- insets.left
					- 1, arcY - ARC_SIZE);

				// shadow in bottom right corner
				//	g.setColor(darkShadow);
				//	g.drawLine(tabPane.getWidth() - insets.right - insets.left - ARC_SIZE, arcY, tabPane.getWidth()
				//		- insets.right
				//		- insets.left, arcY - ARC_SIZE);
			}
		}
	}

	/**
	 * Overridden to draw selected tab color
	 */
	@Override
	protected void paintTabBackground(
		final Graphics g,
		final int tabPlacement,
		final int tabIndex,
		final int x,
		final int y,
		final int w,
		final int h,
		final boolean isSelected) {
		g.setColor(getCurrentBackgroundColor(tabIndex, isSelected));

		if (tabPlacement == TOP) {
			g.fillRect(x + 1, y + 1, w - 1, h - 1);
		}
		else { // BOTTOM
			g.fillRect(x + 1, y, w - 1, h - 1);
		}
	}

	/**
	 * Create the overridden layout manager
	 */
	@Override
	protected LayoutManager createLayoutManager() {
		layout = new JoWidgetsTabbedPaneLayout();
		return layout;
	}

	/**
	 * Overrides the vertical label shift
	 */
	@Override
	protected int getTabLabelShiftY(final int tabPlacement, final int tabIndex, final boolean isSelected) {
		if (tabPlacement == TOP) {
			return 1;
		}
		else {
			return -1;
		}
	}

	/**
	 * Overridden to avoid foucs decoration
	 */
	@Override
	protected void paintFocusIndicator(
		final Graphics g,
		final int tabPlacement,
		final Rectangle[] rects,
		final int tabIndex,
		final Rectangle iconRect,
		final Rectangle textRect,
		final boolean isSelected) {}

	private Color getCurrentBackgroundColor(final int tabIndex, final boolean isSelected) {
		if (isSelected) {
			return SELECTED_COLOR;
		}
		return tabPane.getBackgroundAt(tabIndex);
	}

	private boolean isFirstVisibleTab(final int tabIndex) {
		// this might be changed if a list of visible tabs is introduced
		return tabIndex == firstVisibleTab;
	}

	private boolean isLastVisibleTab(final int tabIndex) {
		// this might be changed if a list of visible tabs is introduced
		return tabIndex == lastVisibleTab;
	}

	private int calculateTabHeight() {
		final Insets tabInsets = getTabInsets(0, 0);
		return getFontMetrics().getHeight() + tabInsets.top + tabInsets.bottom + 2;
	}

	private boolean isAfterSelected(final int tabIndex) {
		final Rectangle boundsSelected = rects[tabPane.getSelectedIndex()];
		final Rectangle boundsTab = rects[tabIndex];

		return (boundsTab.x == boundsSelected.x + boundsSelected.width);
	}

	private class JoWidgetsTabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout {
		private boolean allTabsVisible = true;

		private boolean isTabVisible(final int index) {
			// this might be chaned if visible tab list is introduced
			return allTabsVisible || ((index >= firstVisibleTab) && (index <= lastVisibleTab));
		}

		@Override
		protected void padSelectedTab(final int tabPlacement, final int selectedIndex) {
			// do not pad selected tab
		}

		@Override
		protected void calculateTabRects(final int tabPlacement, final int tabCount) {
			final FontMetrics metrics = getFontMetrics();
			final Dimension size = tabPane.getSize();
			final Insets insets = tabPane.getInsets();
			final Insets tabAreaInsets = getTabAreaInsets(tabPlacement);

			tabRunOverlay = getTabRunOverlay(tabPlacement);
			selectedRun = -1;
			maxTabHeight = calculateMaxTabHeight(tabPlacement);
			maxTabWidth = 0;

			if (tabCount == 0) {
				return;
			}
			runCount = 1;
			tabRuns[0] = 0;

			final int y;
			if (tabPlacement == TOP) {
				y = insets.top + tabAreaInsets.top;
			}
			else {
				y = size.height - insets.bottom - tabAreaInsets.bottom - maxTabHeight - 1;
			}

			int nextX = insets.left + tabAreaInsets.left;
			for (int i = 0; i < tabCount; i++) {
				final Rectangle rect = rects[i];
				rect.x = nextX;
				rect.y = y;
				rect.width = calculateTabWidth(tabPlacement, i, metrics);
				rect.height = maxTabHeight;

				maxTabWidth = Math.max(maxTabWidth, rect.width);
				nextX += rect.width;
			}

			ensureVisibility(tabCount, tabPane.getSelectedIndex());
		}

		private void ensureVisibility(final int tabCount, final int selectedIndex) {
			final Insets insets = tabPane.getInsets();
			final Dimension size = tabPane.getSize();
			final int availableWidth = size.width - (insets.right + tabAreaInsets.right) - ARC_SIZE - PopupToolbar.VERTICAL_GAP;

			allTabsVisible = true;
			firstVisibleTab = 0;
			for (int i = 0; i < tabCount; i++) {
				if (rects[i].x != 2 + insets.left && rects[i].x + rects[i].width > availableWidth) {
					// crop tabs
					allTabsVisible = false;
					break;
				}
				lastVisibleTab = i;
			}

			if (allTabsVisible) {
				return;
			}

			if (selectedIndex > lastVisibleTab) {
				// shift tabs
				lastVisibleTab = selectedIndex;

				int currentWidth = rects[selectedIndex].x + rects[selectedIndex].width;
				while ((currentWidth > availableWidth) && (firstVisibleTab <= selectedIndex)) {
					currentWidth -= rects[firstVisibleTab].width;
					firstVisibleTab++;
				}
			}

			int currentX = insets.left;
			for (int i = 0; i < tabCount; i++) {
				if ((i < firstVisibleTab) || (i > lastVisibleTab)) {
					// make invisible
					rects[i].x = Integer.MAX_VALUE;
					continue;
				}

				rects[i].x = currentX;
				currentX += rects[i].width;
			}
		}

		/**
		 * Overriden to fix the toolbar (button) geometry
		 */
		@Override
		public void layoutContainer(final Container parent) {
			super.layoutContainer(parent);

			final Dimension itemSize = popupToolbar.getPreferredSize();
			final Rectangle tabPaneBounds = tabPane.getBounds();
			final Insets insets = tabPane.getInsets();
			final int tabPlacement = tabPane.getTabPlacement();

			itemSize.height = Math.min(itemSize.height, maxTabHeight - 2);

			if (!allTabsVisible) {
				// TODO NM list of visible rects... improve
				int buttonX = insets.left + PopupToolbar.VERTICAL_GAP;
				for (int r = 0; r < rects.length; r++) {
					if ((rects[r].x >= 0) && (rects[r].x < Integer.MAX_VALUE)) {
						buttonX += rects[r].width;
					}
				}

				final int tabHeight = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);

				final int buttonY;
				if (tabPlacement == TOP) {
					buttonY = insets.top + Math.max(1, tabHeight - itemSize.height);
				}
				else {
					buttonY = tabPaneBounds.height - insets.bottom - tabHeight;
				}

				popupToolbar.setBounds(buttonX, buttonY, itemSize.width, itemSize.height);
			}
			popupToolbar.setVisible(!allTabsVisible);
		}
	}

	private final class PopupToolbar extends JToolBar implements UIResource {
		private static final int VERTICAL_GAP = 5;

		private static final long serialVersionUID = -3220252938453253069L;
		private final PopupButton popupButton;

		private PopupToolbar() {
			setFloatable(false);
			setBorderPainted(false);
			setMargin(new Insets(0, 0, 0, 0));
			setOpaque(true);
			popupButton = new PopupButton();
			popupButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					createPopupMenu();
				}

			});

			this.add(popupButton);
		}

		@Override
		public void paint(final Graphics g) {
			paintChildren(g);
		}

		protected void createPopupMenu() {
			final int tabCount = tabPane.getTabCount();
			final JPopupMenu popupMenu = new JPopupMenu();

			for (int i = 0; i < tabCount; i++) {
				final JMenuItem item = new JMenuItem(tabPane.getTitleAt(i));
				if (tabPane.getTabComponentAt(i) instanceof TabComponent) {
					final TabComponent c = (TabComponent) tabPane.getTabComponentAt(i);
					item.setIcon(c.getLabel().getIcon());
				}
				if (!layout.isTabVisible(i)) {
					item.setFont(item.getFont().deriveFont(Font.BOLD));
				}

				final int index = i;
				item.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(final ActionEvent e) {
						tabPane.setSelectedIndex(index);
					}

				});

				popupMenu.add(item);
			}
			popupMenu.show(popupButton, 0, popupButton.getHeight());
		}

		@SuppressWarnings("unused")
		protected void createPopupWindow() {
			final Point location = popupButton.getLocationOnScreen();
			location.y = location.y + popupButton.getHeight();
			final int tabCount = tabPane.getTabCount();
			final JWindow popupWindow = new JWindow(getFrame(tabPane.getTopLevelAncestor()));
			popupWindow.setLocation(location);

			final JToolBar tabList = new JToolBar(JToolBar.VERTICAL);
			tabList.setFloatable(false);

			final JTextField filter = new JTextField("Filter...");
			tabList.add(filter);
			tabList.addSeparator();

			for (int i = 0; i < tabCount; i++) {
				final JButton button = new JButton(tabPane.getTitleAt(i));
				if (tabPane.getTabComponentAt(i) instanceof TabComponent) {
					final TabComponent c = (TabComponent) tabPane.getTabComponentAt(i);
					button.setIcon(c.getLabel().getIcon());
				}
				if (!layout.isTabVisible(i)) {
					button.setFont(button.getFont().deriveFont(Font.BOLD));
				}

				final int index = i;
				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(final ActionEvent e) {
						tabPane.setSelectedIndex(index);
						popupWindow.setVisible(false);
					}

				});

				tabList.add(button);
			}
			popupWindow.add(tabList);
			popupWindow.addWindowFocusListener(new WindowFocusListener() {

				@Override
				public void windowGainedFocus(final WindowEvent e) {}

				@Override
				public void windowLostFocus(final WindowEvent e) {
					popupWindow.setVisible(false);
				}

			});
			popupWindow.pack();
			popupWindow.toFront();
			popupWindow.setVisible(true);
			popupWindow.requestFocusInWindow();
		}

		protected Frame getFrame(final Component c) {
			if (c == null) {
				return null;
			}
			if (c instanceof Frame) {
				return (Frame) c;
			}
			return getFrame(c.getParent());
		}
	}

	private final class PopupButton extends JButton {
		private static final long serialVersionUID = 634813498872739540L;

		private boolean allowBorder;

		private PopupButton() {
			allowBorder = false;
			setPreferredSize(new Dimension(24, 20));
			setMaximumSize(new Dimension(24, 20));

			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseEntered(final MouseEvent e) {
					allowBorder = true;
				}

				@Override
				public void mouseExited(final MouseEvent e) {
					allowBorder = false;
				}

			});
		}

		@Override
		public void paint(final Graphics g) {
			super.paint(g);
			final Insets insets = getInsets();
			final Dimension size = getSize();
			final int width = size.width - insets.left - insets.right;
			final int height = size.height - insets.top - insets.bottom;

			g.setColor(getBackground());

			if (allowBorder) {
				g.fillRect(insets.left, insets.top, width, height);
			}
			else {
				g.fillRect(0, 0, size.width, size.height);
			}

			g.setColor(ARROW_COLOR);
			drawFilledArrow(insets.left, insets.top, g);

			g.setFont(new Font("SansSerif", Font.PLAIN, 9));
			final int tabCount = tabPane.getTabCount();
			final int invisibleCount = firstVisibleTab + (tabCount - 1) - lastVisibleTab;
			final String text = String.valueOf(invisibleCount);

			g.drawString(text, insets.left + 8, insets.top + height);
			// right aligned: insets.left + width - g.getFontMetrics().stringWidth(text)
		}

		@SuppressWarnings("unused")
		private void drawEmptyArrow(final int x, final int y, final Graphics g) {
			g.drawLine(x, y, x, y + 7);
			g.drawLine(x + 1, y, x + 4, y + 3);
			g.drawLine(x + 1, y + 7, x + 4, y + 4);
		}

		private void drawFilledArrow(final int x, final int y, final Graphics g) {
			for (int i = 0; i < 4; i++) {
				g.drawLine(x + i, y + i, x + i, y + 7 - i);
			}
		}

		@SuppressWarnings("unused")
		private void drawDoubleArrow(final int x, final int y, final Graphics g) {
			drawSimpleArrow(x, y, g);
			drawSimpleArrow(x + 3, y, g);
		}

		private void drawSimpleArrow(final int x, final int y, final Graphics g) {
			g.drawLine(x, y, x + 2, y + 2);
			g.drawLine(x + 1, y, x + 3, y + 2);
			g.drawLine(x, y + 4, x + 2, y + 2);
			g.drawLine(x + 1, y + 4, x + 3, y + 2);
		}
	}
}
