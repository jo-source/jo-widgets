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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class JoWidgetsTabLookAndFeel extends BasicTabbedPaneUI {

	private static final int ARC = 2;

	private final Color selectedColor = Color.WHITE; // background color of selected tab
	private TabPopupSupport tabPopup;

	public JoWidgetsTabLookAndFeel() {
		super();
	}

	@Override
	protected void installComponents() {
		if (tabPopup == null) {
			tabPopup = new TabPopupSupport(tabPane.getTabPlacement());
		}
		super.installComponents();
	}

	@Override
	protected void uninstallComponents() {
		if (tabPopup != null) {
			tabPane.remove(tabPopup.popupButton);
			tabPopup = null;
		}
		super.uninstallComponents();
	}

	@Override
	protected void installDefaults() {
		UIManager.getDefaults().put("TabbedPane.selected", selectedColor);
		UIManager.getDefaults().put("TabbedPane.selectedTabPadInsets", new Insets(0, 0, 0, 0));
		UIManager.getDefaults().put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0));

		super.installDefaults();
		tabAreaInsets = new Insets(0, 0, 0, 0);
		lightHighlight = Color.gray;
	}

	private Rectangle getTopRight() {
		if (tabPane.getTabCount() > 0) {
			Rectangle result = rects[0];
			for (int i = 1; i < rects.length; i++) {
				if (rects[i].x + rects[i].width >= result.x + result.width && rects[i].y <= result.y) {
					result = rects[i];
				}
			}
			return result;
		}

		final Insets insets = tabPane.getInsets();
		return new Rectangle(insets.left, insets.top, 0, 0);
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

	private boolean isFirstInRow(final int tabIndex) {
		if (tabIndex < 1) {
			return true;
		}
		return (rects[tabIndex].x != rects[tabIndex - 1].x + rects[tabIndex - 1].width);
	}

	private boolean isLastInRow(final int tabIndex) {
		if (tabIndex > tabPane.getTabCount() - 2) {
			return true;
		}
		return (rects[tabIndex].x + rects[tabIndex].width != rects[tabIndex + 1].x);
	}

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

		final boolean hasLeftArc = isSelected || isFirstInRow(tabIndex);
		final boolean hasRightArc = isSelected;

		switch (tabPlacement) {
			case BOTTOM:
				// TODO NM implement
				g.drawLine(x, y, x, y + h - 4); // left
				g.drawLine(x, y + h - 3, x + 2, y + h - 1);
				g.drawLine(x + 2, y + h - 1, x + w - 4, y + h - 1); // bottom              
				g.drawLine(x + w - 3, y + h - 1, x + w - 1, y + h - 3);
				g.drawLine(x + w - 1, y, x + w - 1, y + h - 3); // right
				break;
			case TOP:
			default:
				final int y1 = y;
				final int y2 = y + h;
				final int x1 = x;
				final int x2 = x + w;
				if (hasLeftArc) {
					g.drawLine(x1, y1 + ARC, x1 + ARC, y1); // arc
					g.drawLine(x1, y1 + ARC, x1, y2); // left down
					if (!isFirstInRow(tabIndex)) {
						g.drawLine(x1, y1, x1 + ARC, y1); // top connector
					}
				}
				else {
					g.drawLine(x1, y1, x1 + ARC, y1); // top connector
					if (!isAfterSelected) {
						g.drawLine(x1, y1, x1, y2);
					}
				}
				if (hasRightArc) {
					g.drawLine(x2 - ARC, y1, x2, y1 + ARC); // arc
					g.drawLine(x2, y1 + ARC, x2, y2); // right down
				}
				else {
					if (isLastInRow(tabIndex)) {
						g.drawLine(x2, y1, x2, y2);
					}
				}

				g.drawLine(x1 + ARC, y1, x2 - ARC, y1); // top line              
				g.drawLine(x2 - ARC, y1, x2, y1); // top connector
		}
	}

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
		if (isSelected) {
			g.setColor(selectedColor);
		}
		else {
			g.setColor(tabPane.getBackgroundAt(tabIndex));
		}

		if (tabPlacement == TOP) {
			g.fillRect(x + 1, y + 1, w - 1, h - 1);
		}
		else {
			g.fillRect(x + 1, y, w - 1, h - 1);
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

	@Override
	public void paint(final Graphics g, final JComponent c) {
		super.paint(g, c);
		final int tabPlacement = tabPane.getTabPlacement();
		final Insets insets = tabPane.getInsets();
		if (tabPlacement == TOP) {
			if (tabPane.getTabCount() > 0) {
				final Rectangle lastTab = getTopRight();

				final int edgeX1 = Math.max(insets.left, lastTab.x + lastTab.width);
				final int edgeX2 = (tabPane.getWidth() - 1) - insets.right - insets.left;
				final int edgeY1 = lastTab.y;
				final int edgeY2 = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);

				g.setColor(lightHighlight);
				g.drawLine(edgeX1, edgeY1, edgeX2 - ARC, edgeY1); // top
				g.drawLine(edgeX2 - ARC, edgeY1, edgeX2, edgeY1 + ARC); // arc
				g.drawLine(edgeX2, edgeY1 + ARC, edgeX2, edgeY2); // right

				// TODO NM check if necessary
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
				g.fillRect(insets.left, arcY, w, ARC + 1);

				g.setColor(lightHighlight);
				g.drawLine(insets.left + ARC, arcY, w - 2 * ARC, arcY);
				g.drawLine(insets.left, arcY + ARC, insets.left + ARC, arcY);
				g.drawLine(tabPane.getWidth() - insets.right - insets.left - ARC - 1, arcY, tabPane.getWidth()
					- insets.right
					- insets.left
					- 1, arcY + ARC);

				g.setColor(darkShadow);
				g.drawLine(tabPane.getWidth() - insets.right - insets.left - ARC, arcY, tabPane.getWidth()
					- insets.right
					- insets.left, arcY + ARC);
			}
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

	@Override
	protected LayoutManager createLayoutManager() {
		return new JoWidgetsTabbedPaneLayout();
	}

	public class JoWidgetsTabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout {

		@Override
		protected void calculateTabRects(final int tabPlacement, final int tabCount) {
			final FontMetrics metrics = getFontMetrics();
			final Dimension size = tabPane.getSize();
			final Insets insets = tabPane.getInsets();
			final Insets tabAreaInsets = getTabAreaInsets(tabPlacement);
			final int availableWidth = size.width
				- (insets.right + tabAreaInsets.right)
				- tabPopup.popupButton.getPreferredSize().width
				- ARC
				- 5;

			tabRunOverlay = getTabRunOverlay(tabPlacement);
			selectedRun = -1;
			maxTabHeight = calculateMaxTabHeight(tabPlacement);
			maxTabWidth = 0;

			final int y;
			if (tabPlacement == BOTTOM) {
				y = size.height - insets.bottom - tabAreaInsets.bottom - maxTabHeight;
			}
			else {
				y = insets.top + tabAreaInsets.top;
			}

			if (tabCount == 0) {
				return;
			}

			Rectangle rect;
			int nextX = insets.left + tabAreaInsets.left;
			runCount = 1;
			tabRuns[0] = 0;
			for (int i = 0; i < tabCount; i++) {
				rect = rects[i];
				rect.x = nextX;
				rect.y = y;
				rect.width = calculateTabWidth(tabPlacement, i, metrics);
				rect.height = maxTabHeight/* - 2 */;

				maxTabWidth = Math.max(maxTabWidth, rect.width);
				nextX += rect.width;
			}

			for (int i = 0; i < tabCount; i++) {
				if (rects[i].x != 2 + insets.left && rects[i].x + rects[i].width > availableWidth) {
					rects[i].x = Integer.MAX_VALUE;
				}
			}
		}

		@Override
		public void layoutContainer(final Container parent) {
			super.layoutContainer(parent);

			// fix button geometry
			final int tabPlacement = tabPane.getTabPlacement();
			final Insets insets = tabPane.getInsets();

			final Dimension buttonSize = tabPopup.popupButton.getPreferredSize();
			final Rectangle tabPaneBounds = tabPane.getBounds();

			boolean visible = false;
			// final int tabCount = 2;
			// final int currentTabWidth = rects[tabCount - 1].x + rects[tabCount - 1].width;
			// if (currentTabWidth > tw) {
			visible = true;
			// }

			// TODO NM list of visible rects... improve
			int buttonX = 5;
			for (int r = 0; r < rects.length; r++) {
				if ((rects[r].x >= 0) && (rects[r].x < Integer.MAX_VALUE)) {
					buttonX += rects[r].width;
				}
			}

			final int tabHeight = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);

			final int buttonY;
			if (tabPlacement == TOP) {
				buttonY = insets.top + tabHeight - buttonSize.height - 1;
			}
			else {
				buttonY = tabPaneBounds.height - insets.bottom - tabHeight;
			}

			tabPopup.popupButton.setVisible(visible);
			tabPopup.popupButton.setBorder(null);
			if (visible) {
				tabPopup.popupButton.setBounds(buttonX, buttonY, buttonSize.width, buttonSize.height);
			}
		}
	}

	@Override
	protected JButton createScrollButton(final int direction) {
		return super.createScrollButton(direction);
	}

	private class TabPopupSupport { // implements ChangeListener
		private final JButton popupButton;

		TabPopupSupport(final int tabPlacement) {
			popupButton = createScrollButton(EAST);
			popupButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {

				}

			});
			tabPane.add(popupButton);
			popupButton.setMaximumSize(new Dimension(16, 16));
		}
	}
}
