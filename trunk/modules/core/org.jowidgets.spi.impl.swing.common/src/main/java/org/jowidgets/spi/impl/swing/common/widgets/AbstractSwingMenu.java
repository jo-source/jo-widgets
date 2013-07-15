/*
 * Copyright (c) 2010, grossmann, Benjamin Marstaller, Nikolaus Moll
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

package org.jowidgets.spi.impl.swing.common.widgets;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JRadioButtonMenuItem;

import org.jowidgets.spi.widgets.IActionMenuItemSpi;
import org.jowidgets.spi.widgets.IMenuItemSpi;
import org.jowidgets.spi.widgets.IMenuSpi;
import org.jowidgets.spi.widgets.ISelectableMenuItemSpi;
import org.jowidgets.spi.widgets.ISubMenuSpi;

public abstract class AbstractSwingMenu extends SwingWidget implements IMenuSpi {

	private final List<JoSwingButtonGroup> radioGroups;

	public AbstractSwingMenu(final Container component) {
		super(component);
		this.radioGroups = new ArrayList<AbstractSwingMenu.JoSwingButtonGroup>();
		this.radioGroups.add(new JoSwingButtonGroup(null, new ButtonGroup()));
	}

	@Override
	public Container getUiReference() {
		return (Container) super.getUiReference();
	}

	@Override
	public void remove(final int index) {
		getUiReference().remove(index);
		if (getUiReference() instanceof JComponent) {
			final JComponent jComponent = (JComponent) getUiReference();
			jComponent.revalidate();
			jComponent.repaint();
		}
	}

	@Override
	public IMenuItemSpi addSeparator(final Integer index) {
		final SeparatorMenuItemImpl result = new SeparatorMenuItemImpl();
		radioGroups.add(new JoSwingButtonGroup(result, new ButtonGroup()));
		addItem(index, result);
		return result;
	}

	@Override
	public IActionMenuItemSpi addActionItem(final Integer index) {
		final ActionMenuItemImpl result = new ActionMenuItemImpl();
		addItem(index, result);
		return result;
	}

	@Override
	public ISelectableMenuItemSpi addCheckedItem(final Integer index) {
		final SelectableMenuItemImpl result = new SelectableMenuItemImpl(new JCheckBoxMenuItem());
		addItem(index, result);
		return result;
	}

	@Override
	public ISelectableMenuItemSpi addRadioItem(final Integer index) {
		final JRadioButtonMenuItem radioItem = new JRadioButtonMenuItem();
		final ButtonGroup radioGroup = findRadioGroup(index);
		radioGroup.add(radioItem);
		final SelectableMenuItemImpl result = new SelectableMenuItemImpl(radioItem);
		addItem(index, result);
		return result;
	}

	@Override
	public ISubMenuSpi addSubMenu(final Integer index) {
		final SubMenuImpl result = new SubMenuImpl();
		addItem(index, result);
		return result;
	}

	private void addItem(final Integer index, final SwingWidget item) {
		if (index != null) {
			getUiReference().add(item.getUiReference(), index.intValue());
		}
		else {
			getUiReference().add(item.getUiReference());
		}
	}

	private ButtonGroup findRadioGroup(final Integer index) {
		final int safeIndex;
		if (index != null) {
			safeIndex = index.intValue();
		}
		else {
			safeIndex = getUiReference().getComponentCount();
		}
		ButtonGroup result = radioGroups.get(0).getButtonGroup();

		if (radioGroups.size() != 1) {
			for (final JoSwingButtonGroup joButtonGroup : radioGroups) {
				final SeparatorMenuItemImpl separator = joButtonGroup.getSeparator();
				if (separator == null) {
					continue;
				}
				int componentIndex = 0;
				final int componentCount = getUiReference().getComponentCount();
				for (int i = 0; i < componentCount; i++) {
					if (getUiReference().getComponent(i) == separator.getUiReference()) {
						componentIndex = i;
						break;
					}
				}
				if (componentIndex >= safeIndex) {
					break;
				}
				result = joButtonGroup.getButtonGroup();
			}
		}
		return result;
	}

	private static class JoSwingButtonGroup {

		private final SeparatorMenuItemImpl separator;

		private final ButtonGroup buttonGroup;

		/**
		 * @param separatorIndex
		 * @param separator
		 * @param buttonGroup
		 */
		public JoSwingButtonGroup(final SeparatorMenuItemImpl separator, final ButtonGroup buttonGroup) {
			this.separator = separator;
			this.buttonGroup = buttonGroup;
		}

		protected SeparatorMenuItemImpl getSeparator() {
			return separator;
		}

		protected ButtonGroup getButtonGroup() {
			return buttonGroup;
		}

	}
}
