/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.types.QuestionResult;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IQuestionDialog;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.setup.IQuestionDialogSetup;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IButtonCommon;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.tools.controller.WindowAdapter;

public class QuestionDialogImpl implements IQuestionDialog {

	private final IFrame dialogWidget;
	private IButtonCommon defaultButton;
	private boolean wasVisible;
	private QuestionResult result;

	public QuestionDialogImpl(final IFrame dialogWidget, final IQuestionDialogSetup setup) {
		this.wasVisible = false;
		this.dialogWidget = dialogWidget;

		final IBluePrintFactory bpF = new BluePrintFactory();

		if (setup.getIcon() != null) {
			this.dialogWidget.setLayout(new MigLayoutDescriptor("[]20[grow]", "15[][]"));
			this.dialogWidget.add(bpF.icon(setup.getIcon()), "");
		}
		else {
			this.dialogWidget.setLayout(new MigLayoutDescriptor("[grow]", "15[][]"));
		}

		this.dialogWidget.add(bpF.textLabel(setup.getText(), setup.getToolTipText()), "wrap");

		// buttons
		final IComposite buttonBar = dialogWidget.add(bpF.composite(), "span, align center");
		buttonBar.setLayout(new MigLayoutDescriptor("[][][]", "[]"));

		final String buttonCellConstraints = "w 80::, sg bg";

		result = setup.getDefaultResult();

		final IButtonCommon yesButton = buttonBar.add(setup.getYesButton(), buttonCellConstraints);
		yesButton.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				result = QuestionResult.YES;
				dialogWidget.setVisible(false);
			}
		});
		if (setup.getDefaultResult() == null) {
			result = QuestionResult.YES;
		}
		if (QuestionResult.YES == result) {
			defaultButton = yesButton;
		}

		if (setup.getNoButton() != null) {
			final IButtonCommon noButton = buttonBar.add(setup.getNoButton(), buttonCellConstraints);
			noButton.addActionListener(new IActionListener() {

				@Override
				public void actionPerformed() {
					result = QuestionResult.NO;
					dialogWidget.setVisible(false);
				}
			});

			if (setup.getDefaultResult() == null) {
				result = QuestionResult.NO;
			}
			if (QuestionResult.NO == result) {
				defaultButton = noButton;
			}
		}

		if (setup.getCancelButton() != null) {
			final IButtonCommon cancelButton = buttonBar.add(setup.getCancelButton(), buttonCellConstraints);
			cancelButton.addActionListener(new IActionListener() {

				@Override
				public void actionPerformed() {
					result = QuestionResult.CANCEL;
					dialogWidget.setVisible(false);
				}
			});

			if (setup.getDefaultResult() == null) {
				result = QuestionResult.CANCEL;
			}
			if (QuestionResult.CANCEL == result) {
				defaultButton = cancelButton;
			}

		}

		dialogWidget.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated() {
				defaultButton.requestFocus();
			}
		});

		ColorSettingsInvoker.setColors(setup, this);
	}

	@Override
	public Object getUiReference() {
		return dialogWidget.getUiReference();
	}

	@Override
	public void redraw() {
		dialogWidget.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		dialogWidget.setRedrawEnabled(enabled);
	}

	@Override
	public IWindow getParent() {
		return dialogWidget.getParent();
	}

	@Override
	public void setParent(final IWindow parent) {
		dialogWidget.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return dialogWidget.isReparentable();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		dialogWidget.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		dialogWidget.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return dialogWidget.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return dialogWidget.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		dialogWidget.setCursor(cursor);
	}

	@Override
	public QuestionResult question() {
		if (!wasVisible) {
			wasVisible = true;
			dialogWidget.setVisible(true);
			//ui block until user closes the dialog

			//after that dispose the message dialog
			dialogWidget.dispose();
		}
		else {
			throw new IllegalStateException("A message dialog can only be shown once!");
		}
		return result;
	}

	@Override
	public void setVisible(final boolean visible) {
		if (visible) {
			question();
		}
		else {
			dialogWidget.dispose();
		}
	}

	@Override
	public boolean isVisible() {
		return dialogWidget.isVisible();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		dialogWidget.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return dialogWidget.isEnabled();
	}

	@Override
	public Dimension getSize() {
		return dialogWidget.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		dialogWidget.setSize(size);
	}

	@Override
	public void setSize(final int width, final int height) {
		dialogWidget.setSize(width, height);
	}

	@Override
	public void setPosition(final int x, final int y) {
		dialogWidget.setPosition(x, y);
	}

	@Override
	public Position getPosition() {
		return dialogWidget.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		dialogWidget.setPosition(position);
	}

	@Override
	public Position toScreen(final Position localPosition) {
		return dialogWidget.toScreen(localPosition);
	}

	@Override
	public Position toLocal(final Position screenPosition) {
		return dialogWidget.toLocal(screenPosition);
	}

	@Override
	public Position fromComponent(final IComponentCommon component, final Position componentPosition) {
		return dialogWidget.fromComponent(component, componentPosition);
	}

	@Override
	public Position toComponent(final Position componentPosition, final IComponentCommon component) {
		return dialogWidget.toComponent(componentPosition, component);
	}

	@Override
	public boolean requestFocus() {
		return dialogWidget.requestFocus();
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		dialogWidget.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		dialogWidget.removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		dialogWidget.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		dialogWidget.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		dialogWidget.addMouseListener(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		dialogWidget.removeMouseListener(mouseListener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		dialogWidget.addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		dialogWidget.removeComponentListener(componentListener);
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return dialogWidget.createPopupMenu();
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenu) {
		dialogWidget.setPopupMenu(popupMenu);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		dialogWidget.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		dialogWidget.removePopupDetectionListener(listener);
	}

}
