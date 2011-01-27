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

package org.jowidgets.examples.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

public final class SwingToolBarExperiments {

	private SwingToolBarExperiments() {}

	public static void main(final String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				final JFrame frame = new JFrame();

				final JMenuBar menuBar = new JMenuBar();
				final JMenu menu1 = new JMenu("File");
				menuBar.add(menu1);
				frame.setJMenuBar(menuBar);

				frame.setLayout(new MigLayout("", "0[grow]0", "2[]2[grow][]0"));

				final JToolBar toolBar = new JToolBar("Toolbar");
				toolBar.setFloatable(false);

				final JButton toolButton = new JButton();
				toolButton.setIcon(icon2Image(UIManager.getIcon("OptionPane.informationIcon")));
				toolButton.setToolTipText("Button 1");
				toolButton.setFocusable(false);
				toolBar.add(toolButton);

				final JButton toolButton2 = new JButton();
				toolButton2.setIcon(icon2Image(UIManager.getIcon("OptionPane.questionIcon")));
				toolButton2.setToolTipText("Button 2");
				toolButton2.setFocusable(false);
				toolBar.add(toolButton2);

				final JTextField textField = new JTextField();
				toolBar.add(textField);
				textField.setMinimumSize(new Dimension(200, 20));
				textField.setMaximumSize(new Dimension(200, 100));
				textField.setSize(200, textField.getSize().height);

				final JButton toolButton3 = new JButton("My botton 3");
				toolButton3.setIcon(icon2Image(UIManager.getIcon("OptionPane.questionIcon")));
				toolButton3.setFocusable(false);
				toolBar.add(toolButton3);

				//final JPanel buttonPanel = new JPanel();

				final JButton buttonLeft = new JButton();
				buttonLeft.setFocusable(false);
				buttonLeft.setIcon(icon2Image(UIManager.getIcon("OptionPane.questionIcon")));

				final JToolBar subToolBar = new JToolBar();
				subToolBar.setFloatable(false);

				final JButton buttonRight = new JButton();
				buttonRight.setFocusable(false);
				final Icon arrowIcon = new ArrowIcon();
				buttonRight.setMaximumSize(new Dimension(arrowIcon.getIconWidth() + 6, buttonLeft.getMaximumSize().height));
				buttonRight.setIcon(arrowIcon);

				toolBar.add(buttonLeft);
				toolBar.add(buttonRight);
				//toolBar.add(subToolBar);

				final JToggleButton buttonX = new JToggleButton("test");

				toolBar.add(buttonX);
				buttonX.setFocusable(false);

				buttonLeft.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseReleased(final MouseEvent e) {
						buttonRight.getModel().setSelected(false);
					}

					@Override
					public void mousePressed(final MouseEvent e) {
						buttonRight.getModel().setSelected(true);
					}

					@Override
					public void mouseExited(final MouseEvent e) {
						buttonRight.getModel().setRollover(false);
					}

					@Override
					public void mouseEntered(final MouseEvent e) {
						buttonRight.getModel().setRollover(true);
					}

				});

				buttonRight.addMouseListener(new MouseAdapter() {

					@Override
					public void mousePressed(final MouseEvent e) {
						final JPopupMenu popupMenu = new JPopupMenu();
						popupMenu.add(new JMenuItem("Item1"));
						popupMenu.add(new JMenuItem("Item2"));
						popupMenu.add(new JMenuItem("Item2"));
						final Point menuPosition = buttonLeft.getLocation();

						popupMenu.show(toolBar, menuPosition.x, menuPosition.y + buttonLeft.getHeight());
					}

					@Override
					public void mouseExited(final MouseEvent e) {
						buttonLeft.getModel().setRollover(false);
					}

					@Override
					public void mouseEntered(final MouseEvent e) {
						buttonLeft.getModel().setRollover(true);
					}

				});

				final JTextArea textArea = new JTextArea();
				textArea.setBorder(BorderFactory.createEtchedBorder());

				final JPanel statusBar = new JPanel();

				frame.getContentPane().add(toolBar, "growx, wrap");
				frame.getContentPane().add(textArea, "growx, growy, wrap");
				frame.getContentPane().add(statusBar, "growx, h 20:20:20, wrap");

				frame.setSize(800, 600);
				frame.setVisible(true);

			}
		});
	}

	private static Icon icon2Image(final Icon icon) {
		final BufferedImage bufferedImage = new BufferedImage(
			icon.getIconWidth(),
			icon.getIconHeight(),
			BufferedImage.TYPE_INT_ARGB);
		icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
		return new ImageIcon(bufferedImage.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
	}

	private static class ArrowIcon implements Icon {

		private static final int ICON_HEIGHT = 4;
		private static final int ICON_WIDTH = 2 * ICON_HEIGHT + 1;

		@Override
		public int getIconWidth() {
			return ICON_WIDTH;
		}

		@Override
		public int getIconHeight() {
			return ICON_HEIGHT;
		}

		@Override
		public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
			final AbstractButton b = (AbstractButton) c;
			final ButtonModel m = b.getModel();
			final int w = getIconWidth() - 2;
			final int h = ICON_HEIGHT;

			g.translate(x, y);

			g.setColor(UIManager.getColor(m.isEnabled() ? "controlText" : "textInactiveText"));
			for (int i = 0; i < h; i++) {
				g.drawLine(i + 1, i, w - i, i);
			}

			g.translate(-x, -y);
		}
	}
}
