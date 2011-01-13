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

package org.jowidgets.examples.swt;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public final class SwtToolBarExperiments {

	private SwtToolBarExperiments() {}

	public static void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);

		final Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);

		final MenuItem fileMenu = new MenuItem(menuBar, SWT.CASCADE);
		fileMenu.setText("File");

		shell.setLayout(new MigLayout("", "0[grow]0", "2[]2[grow][]0"));

		final ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		toolBar.setLayoutData("growx, wrap");

		final Text textArea = new Text(shell, SWT.BORDER | SWT.MULTI);
		textArea.setLayoutData("growx, growy");

		final ToolItem itemPush = new ToolItem(toolBar, SWT.PUSH);
		itemPush.setImage(getImage(Display.getDefault().getSystemImage(SWT.ICON_INFORMATION)));
		itemPush.setToolTipText("Item push text");

		final ToolItem itemCheck = new ToolItem(toolBar, SWT.CHECK);
		itemCheck.setText("CHECK item");

		new ToolItem(toolBar, SWT.SEPARATOR);

		final ToolItem itemRadio1 = new ToolItem(toolBar, SWT.RADIO);
		itemRadio1.setText("RADIO item 1");

		final ToolItem itemRadio2 = new ToolItem(toolBar, SWT.RADIO);
		itemRadio2.setText("RADIO item 2");

		final ToolItem itemSeparator = new ToolItem(toolBar, SWT.SEPARATOR);
		final Composite textComposite = new Composite(toolBar, SWT.NONE);
		textComposite.setLayout(new MigLayout("", "0[grow]0", "0[grow]0"));

		final Text text = new Text(textComposite, SWT.BORDER | SWT.SINGLE);
		text.setLayoutData("growx");

		itemSeparator.setWidth(200);
		itemSeparator.setControl(textComposite);

		final ToolItem itemDropDown = new ToolItem(toolBar, SWT.DROP_DOWN);
		itemDropDown.setText("DROP_DOWN item");
		itemDropDown.setImage(getImage(Display.getDefault().getSystemImage(SWT.ICON_INFORMATION)));
		itemDropDown.setToolTipText("Click here to see a drop down menu ...");

		toolBar.pack();

		final Menu menu = new Menu(shell, SWT.POP_UP);
		new MenuItem(menu, SWT.PUSH).setText("Menu item 1");
		new MenuItem(menu, SWT.PUSH).setText("Menu item 2");
		new MenuItem(menu, SWT.SEPARATOR);
		new MenuItem(menu, SWT.PUSH).setText("Menu item 3");

		itemDropDown.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (event.detail == SWT.ARROW) {
					final Rectangle bounds = itemDropDown.getBounds();
					final Point point = toolBar.toDisplay(bounds.x, bounds.y + bounds.height);
					menu.setLocation(point);
					menu.setVisible(true);
				}
			}
		});

		final Listener selectionListener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				final ToolItem item = (ToolItem) event.widget;
				System.out.println(item.getText() + " is selected");
				if ((item.getStyle() & SWT.RADIO) != 0 || (item.getStyle() & SWT.CHECK) != 0)
					System.out.println("Selection status: " + item.getSelection());
			}
		};

		itemPush.addListener(SWT.Selection, selectionListener);
		itemCheck.addListener(SWT.Selection, selectionListener);
		itemRadio1.addListener(SWT.Selection, selectionListener);
		itemRadio2.addListener(SWT.Selection, selectionListener);
		itemDropDown.addListener(SWT.Selection, selectionListener);

		//toolBar.pack();

		shell.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				final Rectangle clientArea = shell.getClientArea();
				toolBar.setSize(toolBar.computeSize(clientArea.width, SWT.DEFAULT));
			}
		});

		shell.setSize(800, 600);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

	private static Image getImage(final Image image) {
		return smoothScale(image, 16, 16);
	}

	private static Image smoothScale(final Image originalImage, final int width, final int height) {
		final ImageData imageData = originalImage.getImageData();

		//create a temporary image to scale with anti aliasing
		final Image scaledSmoothImageTmp = new Image(Display.getDefault(), width, height);
		final GC gc = new GC(scaledSmoothImageTmp);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(originalImage, 0, 0, imageData.width, imageData.height, 0, 0, width, height);

		//clone the image data of the scaled smooth image(
		//getImageData() is not enough because the fields could not be overridden)
		final ImageData scaledSmoothImageData = (ImageData) scaledSmoothImageTmp.getImageData().clone();

		//workaround, because drawImage does lost the mask data (tested with WinXP)
		//scale the image with the 'grainy' method and use only its alpha masks
		final ImageData scaledGrainyImageData = originalImage.getImageData().scaledTo(width, height);
		scaledSmoothImageData.maskPad = scaledGrainyImageData.maskPad;
		scaledSmoothImageData.maskData = scaledGrainyImageData.maskData;
		scaledSmoothImageData.alpha = scaledGrainyImageData.alpha;
		scaledSmoothImageData.alphaData = scaledGrainyImageData.alphaData;
		scaledSmoothImageData.transparentPixel = scaledGrainyImageData.transparentPixel;
		scaledSmoothImageData.type = scaledGrainyImageData.type;

		//dispose the temps
		gc.dispose();
		scaledSmoothImageTmp.dispose();

		//return the result
		return new Image(Display.getDefault(), scaledSmoothImageData);
	}
}
