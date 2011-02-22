/*
 * Copyright (c) 2011, M. Grossmann, M. Woelker, H. Westphal
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of jo-widgets.org nor the
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

package org.jowidgets.workbench.legacy.impl.rcp.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Position;
import org.jowidgets.workbench.legacy.api.ITrayItem;
import org.jowidgets.workbench.legacy.api.IWorkbench;
import org.jowidgets.workbench.legacy.impl.rcp.internal.util.ImageHelper;

public final class WorkbenchTrayItem implements ITrayItem {

	private final Shell shell;
	private final IPopupMenu menu;
	private TrayItem trayItem;

	public WorkbenchTrayItem(final IFrame frame, final IWorkbench workbench) {
		shell = (Shell) frame.getUiReference();
		menu = frame.createPopupMenu();
		final Tray tray = shell.getDisplay().getSystemTray();
		if (tray != null) {
			trayItem = new TrayItem(tray, SWT.NONE);
			setIcon(workbench.getIcon());
			trayItem.setToolTipText(workbench.getTooltip());
			trayItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetDefaultSelected(final SelectionEvent e) {
					if (shell.isVisible()) {
						shell.setVisible(false);
						return;
					}
					shell.setVisible(true);
					shell.setActive();
				}
			});
			trayItem.addMenuDetectListener(new MenuDetectListener() {
				@Override
				public void menuDetected(final MenuDetectEvent e) {
					if (!menu.getChildren().isEmpty()) {
						final Point position = shell.toControl(e.display.getCursorLocation());
						menu.show(new Position(position.x, position.y));
					}
				}
			});
		}
	}

	@Override
	public IMenu getMenu() {
		return menu;
	}

	@Override
	public void setIcon(final IImageConstant imageKey) {
		if (trayItem != null && !trayItem.isDisposed()) {
			trayItem.setImage(ImageHelper.getImage(
					imageKey,
					PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW)));
		}

	}

	@Override
	public void setTooltip(final String tooltip) {
		if (trayItem != null && !trayItem.isDisposed()) {
			trayItem.setToolTipText(tooltip);
		}
	}

	@Override
	public void showInfo(final String title, final String message) {
		showMessage(SWT.ICON_INFORMATION, title, message);
	}

	@Override
	public void showWarning(final String title, final String message) {
		showMessage(SWT.ICON_WARNING, title, message);
	}

	@Override
	public void showError(final String title, final String message) {
		showMessage(SWT.ICON_ERROR, title, message);
	}

	private void showMessage(final int style, final String title, final String message) {
		if (trayItem != null && !trayItem.isDisposed()) {
			final ToolTip currentTip = trayItem.getToolTip();
			if (currentTip != null && !currentTip.isDisposed()) {
				currentTip.dispose();
			}
			final ToolTip tip = new ToolTip(shell, SWT.BALLOON | style);
			tip.setText(title);
			tip.setMessage(message);
			trayItem.setToolTip(tip);
			tip.setVisible(true);
		}
	}
}
