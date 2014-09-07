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

package org.jowidgets.spi.impl.swt.common.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ArmEvent;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolTip;
import org.jowidgets.common.widgets.controller.IMenuListener;
import org.jowidgets.spi.impl.controller.MenuObservable;
import org.jowidgets.spi.widgets.IActionMenuItemSpi;
import org.jowidgets.spi.widgets.IMenuItemSpi;
import org.jowidgets.spi.widgets.IMenuSpi;
import org.jowidgets.spi.widgets.ISelectableMenuItemSpi;
import org.jowidgets.spi.widgets.ISubMenuSpi;

public class SwtMenu implements IMenuSpi {

	private final Menu menu;
	private ToolTip toolTip;

	private ArmListener lastArmListener;

	private final MenuObservable menuObservable;

	public SwtMenu(final Menu menu) {
		this.menuObservable = new MenuObservable();

		this.menu = menu;

		try {
			this.toolTip = new ToolTip(menu.getShell(), SWT.NONE);
		}
		catch (final NoClassDefFoundError error) {
			//TODO MG rwt has no tooltip, may use a window instead. 
			//(New rwt version supports tooltips)
		}

		if (toolTip != null) {
			menu.addMenuListener(new MenuListener() {

				@Override
				public void menuShown(final MenuEvent e) {
					menuObservable.fireMenuActivated();
				}

				@Override
				public void menuHidden(final MenuEvent e) {
					toolTip.setVisible(false);
					lastArmListener = null;
					menuObservable.fireMenuDeactivated();
				}

			});
		}
	}

	@Override
	public Menu getUiReference() {
		return menu;
	}

	@Override
	public void remove(final int index) {
		final MenuItem item = menu.getItem(index);
		if (item != null && !item.isDisposed()) {
			item.dispose();
		}
	}

	@Override
	public void setEnabled(final boolean enabled) {
		menu.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return menu.isEnabled();
	}

	@Override
	public ISubMenuSpi addSubMenu(final Integer index) {
		MenuItem subMenuItem = null;
		if (index != null) {
			subMenuItem = new MenuItem(menu, SWT.CASCADE, index.intValue());
		}
		else {
			subMenuItem = new MenuItem(menu, SWT.CASCADE);
		}

		final Menu subMenu = new Menu(menu.getShell(), SWT.DROP_DOWN);
		subMenuItem.setMenu(subMenu);
		return createSubMenu(subMenuItem, subMenu);
	}

	@Override
	public IActionMenuItemSpi addActionItem(final Integer index) {
		MenuItem menuItem = null;
		if (index != null) {
			menuItem = new MenuItem(menu, SWT.PUSH, index.intValue());
		}
		else {
			menuItem = new MenuItem(menu, SWT.PUSH);
		}
		return createActionItem(menuItem);
	}

	@Override
	public ISelectableMenuItemSpi addCheckedItem(final Integer index) {
		MenuItem menuItem = null;
		if (index != null) {
			menuItem = new MenuItem(menu, SWT.CHECK, index.intValue());
		}
		else {
			menuItem = new MenuItem(menu, SWT.CHECK);
		}
		return createSelectableItem(menuItem);
	}

	@Override
	public ISelectableMenuItemSpi addRadioItem(final Integer index) {
		MenuItem menuItem = null;
		if (index != null) {
			menuItem = new MenuItem(menu, SWT.RADIO, index.intValue());
		}
		else {
			menuItem = new MenuItem(menu, SWT.RADIO);
		}
		return createSelectableItem(menuItem);
	}

	@Override
	public IMenuItemSpi addSeparator(final Integer index) {
		MenuItem menuItem = null;
		if (index != null) {
			menuItem = new MenuItem(menu, SWT.SEPARATOR, index.intValue());
		}
		else {
			menuItem = new MenuItem(menu, SWT.SEPARATOR);
		}
		return new MenuItemImpl(menuItem);
	}

	private ActionMenuItemImpl createActionItem(final MenuItem menuItem) {
		final ActionMenuItemImpl result = new ActionMenuItemImpl(menuItem);
		installTooltip(menuItem, result);
		return result;
	}

	private SelectableMenuItemImpl createSelectableItem(final MenuItem menuItem) {
		final SelectableMenuItemImpl result = new SelectableMenuItemImpl(menuItem);
		installTooltip(menuItem, result);
		return result;
	}

	private SubMenuImpl createSubMenu(final MenuItem subMenuItem, final Menu subMenu) {
		final SubMenuImpl result = new SubMenuImpl(subMenuItem, subMenu);
		installTooltip(subMenuItem, result);
		return result;
	}

	private void installTooltip(final MenuItem menuItem, final IToolTipTextProvider toolTipTextProvider) {
		final ArmListener armListener = new ArmListener() {

			@Override
			public void widgetArmed(final ArmEvent e) {
				final boolean wasVisible = toolTip.isVisible();
				toolTip.setVisible(false);

				final String toolTipText = toolTipTextProvider.getToolTipText();
				final boolean hasToolTip = toolTipText != null && !toolTipText.isEmpty();

				if (wasVisible) {
					if (hasToolTip) {
						showToolTip(toolTipText);
					}
				}
				else if (hasToolTip) {
					lastArmListener = this;
					final ArmListener thisListener = this;
					Display.getCurrent().timerExec(400, new Runnable() {

						@Override
						public void run() {
							if (lastArmListener == thisListener) {
								showToolTip(toolTipText);
							}
						}
					});
				}

			}
		};

		if (toolTip != null) {
			menuItem.addArmListener(armListener);
		}
	}

	private void showToolTip(final String message) {
		toolTip.setMessage(message);
		final Point location = Display.getCurrent().getCursorLocation();
		toolTip.setLocation(location.x + 16, location.y + 16);
		toolTip.setVisible(true);
	}

	@Override
	public void addMenuListener(final IMenuListener listener) {
		menuObservable.addMenuListener(listener);
	}

	@Override
	public void removeMenuListener(final IMenuListener listener) {
		menuObservable.removeMenuListener(listener);
	}

}
