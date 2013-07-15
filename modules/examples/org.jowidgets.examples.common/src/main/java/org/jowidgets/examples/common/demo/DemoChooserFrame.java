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

import java.util.Date;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.ICalendar;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IPopupDialog;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.controller.IMouseButtonEvent;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controller.MouseAdapter;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.powo.JoFrame;

public class DemoChooserFrame extends JoFrame {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	private IPopupDialog popupDialog;

	private Date date;

	public DemoChooserFrame() {
		super("Chooser demo");

		addComponentListener(new IComponentListener() {

			@Override
			public void sizeChanged() {
				//CHECKSTYLE:OFF
				System.out.println(getSize());
				//CHECKSTYLE:ON
			}

			@Override
			public void positionChanged() {
				//CHECKSTYLE:OFF
				System.out.println(getPosition());
				//CHECKSTYLE:ON
			}
		});

		setLayout(new MigLayoutDescriptor("[grow]", "[][][]"));

		final IButton button1 = add(BPF.button("Open calendar popup..."), "wrap, sg bg");
		button1.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final IMouseButtonEvent event) {
				popupDialog = createChildWindow(BPF.popupDialog().setBorder(false));
				final Position buttonPos = button1.getPosition();
				popupDialog.setPosition(Toolkit.toScreen(buttonPos, DemoChooserFrame.this));
				setContent(popupDialog);
				popupDialog.pack();
				popupDialog.setVisible(true);
			}

		});

		final IButton button2 = add(BPF.button("Open / Close caledar popup"), "wrap, sg bg");
		button2.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final IMouseButtonEvent event) {
				popupDialog = createChildWindow(BPF.popupDialog().setAutoDispose(false).setBorder(false));
				final Dimension buttonSize = button2.getSize();
				final Position buttonPos = button2.getPosition();
				popupDialog.setBackgroundColor(Colors.WHITE);
				setContent(popupDialog);
				popupDialog.setPosition(Toolkit.toScreen(
						new Position(buttonPos.getX(), buttonPos.getY() + buttonSize.getHeight()),
						DemoChooserFrame.this));
				popupDialog.pack();
				popupDialog.setVisible(true);
			}

			@Override
			public void mouseReleased(final IMouseButtonEvent mouseEvent) {
				if (popupDialog != null) {
					popupDialog.dispose();
				}
			}

		});

		final IButton button3 = add(BPF.button("Open calendar dialog..."), "wrap, sg bg");
		button3.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final IMouseButtonEvent event) {
				final IFrame dialog = createChildWindow(BPF.dialog("Calendar dialog"));
				final Position buttonPos = button3.getPosition();
				dialog.setPosition(Toolkit.toScreen(buttonPos, DemoChooserFrame.this));
				final ICalendar calendar = setContent(dialog);
				dialog.pack();
				dialog.setMinSize(dialog.computeDecoratedSize(calendar.getMinSize()));
				dialog.setVisible(true);
			}

		});

	}

	private ICalendar setContent(final IContainer container) {

		container.setLayout(MigLayoutFactory.growingInnerCellLayout());
		final ICalendar calendar = container.add(BPF.calendar().setDate(this.date), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		calendar.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				//CHECKSTYLE:OFF
				System.out.println(calendar.getDate());
				date = calendar.getDate();
				//CHECKSTYLE:ON
			}
		});

		return calendar;
	}

}
