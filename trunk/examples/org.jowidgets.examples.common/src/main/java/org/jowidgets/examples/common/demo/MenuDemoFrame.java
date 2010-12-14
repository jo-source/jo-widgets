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

package org.jowidgets.examples.common.demo;

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IActionMenuItem;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.blueprint.IActionMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.tools.powo.JoFrame;

public class MenuDemoFrame extends JoFrame {

	private static final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

	public MenuDemoFrame() {
		super(bluePrint("Menu demo").autoPackOff());

		final IPopupMenu popupMenu = createPopupMenu();

		final IActionMenuItemBluePrint item1Bp = bpf.menuItem().setText("Item1").setToolTipText("This is item 1");
		item1Bp.setIcon(IconsSmall.INFO).setAccelerator(new Accelerator('T', Modifier.CTRL)).setMnemonic('t');
		final IActionMenuItem item1 = popupMenu.addMenuItem(item1Bp);

		final IActionMenuItemBluePrint item2Bp = bpf.menuItem().setText("The Second Item");
		item2Bp.setToolTipText("This is the second item");
		item2Bp.setIcon(IconsSmall.WARNING).setAccelerator(new Accelerator('H', Modifier.ALT)).setMnemonic('m');
		final IActionMenuItem item2 = popupMenu.addMenuItem(item2Bp);

		final IActionMenuItemBluePrint item3Bp = bpf.menuItem().setText("The Third Item");
		item3Bp.setToolTipText("This is the third item");
		item3Bp.setIcon(IconsSmall.WARNING).setAccelerator(new Accelerator('I', Modifier.SHIFT)).setMnemonic('e');
		final IActionMenuItem item3 = popupMenu.addMenuItem(1, item3Bp);

		item1.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				System.out.println("Item1");
			}
		});

		item2.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				System.out.println("Item2");
			}
		});

		item3.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				System.out.println("Item3");
			}
		});

		addPopupDetectionListener(new IPopupDetectionListener() {

			@Override
			public void popupDetected(final Position position) {
				popupMenu.show(position);
			}
		});
	}

}
