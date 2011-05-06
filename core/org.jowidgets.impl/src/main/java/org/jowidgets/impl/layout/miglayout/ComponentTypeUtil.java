/*
 * Copyright (c) 2011, nimoll
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

package org.jowidgets.impl.layout.miglayout;

import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.ICheckBox;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IIcon;
import org.jowidgets.api.widgets.IInputContainer;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.ILabel;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.IProgressBar;
import org.jowidgets.api.widgets.IScrollComposite;
import org.jowidgets.api.widgets.ISplitComposite;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.ITextArea;
import org.jowidgets.api.widgets.IToggleButton;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarButton;
import org.jowidgets.api.widgets.IToolBarPopupButton;
import org.jowidgets.api.widgets.IToolBarToggleButton;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.IValidationLabel;
import org.jowidgets.impl.layout.miglayout.common.ComponentWrapper;

final class ComponentTypeUtil {

	private ComponentTypeUtil() {

	}

	static int getType(final IComponent component) {

		if (isContainer(component)) {
			return ComponentWrapper.TYPE_CONTAINER;
		}
		if (isButton(component)) {
			return ComponentWrapper.TYPE_BUTTON;
		}
		if (isLabel(component)) {
			return ComponentWrapper.TYPE_LABEL;
		}
		if (isTextField(component)) {
			return ComponentWrapper.TYPE_TEXT_FIELD;
		}
		if (isTextArea(component)) {
			return ComponentWrapper.TYPE_TEXT_AREA;
		}
		if (isList(component)) {
			return ComponentWrapper.TYPE_LIST;
		}
		if (isTable(component)) {
			return ComponentWrapper.TYPE_TABLE;
		}
		if (isScrollPane(component)) {
			return ComponentWrapper.TYPE_SCROLL_PANE;
		}
		if (isImage(component)) {
			return ComponentWrapper.TYPE_IMAGE;
		}
		if (isPanel(component)) {
			return ComponentWrapper.TYPE_PANEL;
		}
		if (isComboBox(component)) {
			return ComponentWrapper.TYPE_COMBO_BOX;
		}
		if (isCheckBox(component)) {
			return ComponentWrapper.TYPE_CHECK_BOX;
		}
		if (isProgressBar(component)) {
			return ComponentWrapper.TYPE_PROGRESS_BAR;
		}
		if (isTree(component)) {
			return ComponentWrapper.TYPE_TREE;
		}

		if (isSlider(component)) {
			return ComponentWrapper.TYPE_SLIDER;
		}
		if (isSpinner(component)) {
			return ComponentWrapper.TYPE_SPINNER;
		}
		if (isScrollBar(component)) {
			return ComponentWrapper.TYPE_SCROLL_BAR;
		}

		if (isSeparator(component)) {
			return ComponentWrapper.TYPE_SEPARATOR;
		}

		return ComponentWrapper.TYPE_UNKNOWN;
	}

	private static boolean isSeparator(final IComponent component) {
		// TODO NM find a way to detect separator
		//		if (component instanceof IControl) {
		//			IControl control = (IControl) component;
		//		}
		return false;
	}

	private static boolean isScrollBar(final IComponent component) {
		return false;
	}

	private static boolean isTree(final IComponent component) {
		return (component instanceof ITree);
	}

	private static boolean isProgressBar(final IComponent component) {
		return (component instanceof IProgressBar);
	}

	private static boolean isCheckBox(final IComponent component) {
		return (component instanceof ICheckBox);
	}

	private static boolean isSpinner(final IComponent component) {
		return false;
	}

	private static boolean isSlider(final IComponent component) {
		return false;
	}

	private static boolean isComboBox(final IComponent component) {
		return (component instanceof IComboBox);
	}

	private static boolean isPanel(final IComponent component) {
		return false;
	}

	private static boolean isImage(final IComponent component) {
		return (component instanceof IIcon);
	}

	private static boolean isScrollPane(final IComponent component) {
		return (component instanceof IScrollComposite);
	}

	private static boolean isTable(final IComponent component) {
		return (component instanceof ITable);
	}

	private static boolean isList(final IComponent component) {
		return false;
	}

	private static boolean isButton(final IComponent component) {
		return ((component instanceof IButton)
			|| (component instanceof IToggleButton)
			|| (component instanceof IToolBarButton)
			|| (component instanceof IToolBarPopupButton) || (component instanceof IToolBarToggleButton));
	}

	private static boolean isContainer(final IComponent component) {
		return ((component instanceof IFrame)
			|| (component instanceof IInputContainer)
			|| (component instanceof IContainer)
			|| (component instanceof ITabFolder)
			|| (component instanceof IToolBar)
			|| (component instanceof ISplitComposite) || (component instanceof IMenuBar));
	}

	private static boolean isLabel(final IComponent component) {
		return ((component instanceof ILabel) || (component instanceof IValidationLabel));
	}

	private static boolean isTextField(final IComponent component) {
		return ((component instanceof IInputField) || (component instanceof IInputControl));
	}

	private static boolean isTextArea(final IComponent component) {
		return (component instanceof ITextArea);
	}

}
