/*
 * Copyright (c) 2010, grossmann, Lukas Gross
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

package org.jowidgets.spi.impl.dummy.dummyui;

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IActionObservable;
import org.jowidgets.common.widgets.controler.IComponentListener;
import org.jowidgets.common.widgets.controler.IComponentObservable;
import org.jowidgets.common.widgets.controler.IFocusListener;
import org.jowidgets.common.widgets.controler.IFocusObservable;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.IInputObservable;
import org.jowidgets.common.widgets.controler.IItemStateListener;
import org.jowidgets.common.widgets.controler.IItemStateObservable;
import org.jowidgets.common.widgets.controler.IKeyListener;
import org.jowidgets.common.widgets.controler.IKeyObservable;
import org.jowidgets.common.widgets.controler.IMenuListener;
import org.jowidgets.common.widgets.controler.IMenuObservable;
import org.jowidgets.common.widgets.controler.IMouseListener;
import org.jowidgets.common.widgets.controler.IMouseObservable;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.controler.IPopupDetectionObservable;
import org.jowidgets.common.widgets.controler.ITreeNodeListener;
import org.jowidgets.common.widgets.controler.ITreeNodeObservable;
import org.jowidgets.common.widgets.controler.IWindowListener;
import org.jowidgets.common.widgets.controler.IWindowObservable;
import org.jowidgets.spi.widgets.controler.ITabItemListenerSpi;
import org.jowidgets.spi.widgets.controler.ITabItemObservableSpi;
import org.jowidgets.spi.widgets.controler.ITreeSelectionListenerSpi;
import org.jowidgets.spi.widgets.controler.ITreeSelectionObservableSpi;

public class UIDObservable implements
		IActionObservable,
		IInputObservable,
		IWindowObservable,
		IItemStateObservable,
		IMenuObservable,
		IPopupDetectionObservable,
		ITreeNodeObservable,
		ITreeSelectionObservableSpi,
		ITabItemObservableSpi,
		IFocusObservable,
		IKeyObservable,
		IMouseObservable,
		IComponentObservable {

	private final Set<IInputListener> inputListeners;
	private final Set<IActionListener> actionListeners;
	private final Set<IWindowListener> windowListeners;
	private final Set<IItemStateListener> itemStateListeners;
	private final Set<IMenuListener> menuListeners;
	private final Set<IPopupDetectionListener> popupListeners;
	private final Set<ITabItemListenerSpi> tabItemListeners;
	private final Set<ITreeNodeListener> treeNodeListeners;
	private final Set<ITreeSelectionListenerSpi> treeSelectionListeners;
	private final Set<IFocusListener> focusListeners;
	private final Set<IKeyListener> keyListeners;
	private final Set<IMouseListener> mouseListeners;
	private final Set<IComponentListener> componentListeners;

	public UIDObservable() {
		super();
		this.inputListeners = new HashSet<IInputListener>();
		this.actionListeners = new HashSet<IActionListener>();
		this.windowListeners = new HashSet<IWindowListener>();
		this.itemStateListeners = new HashSet<IItemStateListener>();
		this.menuListeners = new HashSet<IMenuListener>();
		this.popupListeners = new HashSet<IPopupDetectionListener>();
		this.tabItemListeners = new HashSet<ITabItemListenerSpi>();
		this.treeNodeListeners = new HashSet<ITreeNodeListener>();
		this.treeSelectionListeners = new HashSet<ITreeSelectionListenerSpi>();
		this.focusListeners = new HashSet<IFocusListener>();
		this.keyListeners = new HashSet<IKeyListener>();
		this.mouseListeners = new HashSet<IMouseListener>();
		this.componentListeners = new HashSet<IComponentListener>();
	}

	@Override
	public void addActionListener(final IActionListener listener) {
		actionListeners.add(listener);
	}

	@Override
	public void removeActionListener(final IActionListener listener) {
		actionListeners.remove(listener);
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		inputListeners.add(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		inputListeners.remove(listener);
	}

	@Override
	public void addWindowListener(final IWindowListener listener) {
		windowListeners.add(listener);
	}

	@Override
	public void removeWindowListener(final IWindowListener listener) {
		windowListeners.remove(listener);
	}

	@Override
	public void addItemListener(final IItemStateListener listener) {
		itemStateListeners.add(listener);
	}

	@Override
	public void removeItemListener(final IItemStateListener listener) {
		itemStateListeners.remove(listener);
	}

	@Override
	public void addMenuListener(final IMenuListener listener) {
		menuListeners.add(listener);
	}

	@Override
	public void removeMenuListener(final IMenuListener listener) {
		menuListeners.remove(listener);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		popupListeners.add(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		popupListeners.remove(listener);
	}

	@Override
	public void addTabItemListener(final ITabItemListenerSpi listener) {
		tabItemListeners.add(listener);
	}

	@Override
	public void removeTabItemListener(final ITabItemListenerSpi listener) {
		tabItemListeners.remove(listener);
	}

	@Override
	public void addTreeNodeListener(final ITreeNodeListener listener) {
		treeNodeListeners.add(listener);
	}

	@Override
	public void removeTreeNodeListener(final ITreeNodeListener listener) {
		treeNodeListeners.remove(listener);
	}

	@Override
	public void addTreeSelectionListener(final ITreeSelectionListenerSpi listener) {
		treeSelectionListeners.add(listener);
	}

	@Override
	public void removeTreeSelectionListener(final ITreeSelectionListenerSpi listener) {
		treeSelectionListeners.remove(listener);
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		focusListeners.add(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		focusListeners.remove(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		keyListeners.add(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		keyListeners.remove(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		mouseListeners.add(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		mouseListeners.remove(mouseListener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		componentListeners.add(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		componentListeners.remove(componentListener);
	}

	public void fireActionPerformed() {
		for (final IActionListener listener : actionListeners) {
			listener.actionPerformed();
		}
	}

	public void fireItemStateChanged() {
		for (final IItemStateListener listener : itemStateListeners) {
			listener.itemStateChanged();
		}
	}

	public void fireInputChanged() {
		for (final IInputListener listener : inputListeners) {
			listener.inputChanged();
		}
	}

	public void firePopupDetected(final Position position) {
		for (final IPopupDetectionListener listener : popupListeners) {
			listener.popupDetected(position);
		}
	}

	void fireWindowActivated() {
		for (final IWindowListener listener : windowListeners) {
			listener.windowActivated();
		}
	}

	void fireWindowDeactivated() {
		for (final IWindowListener listener : windowListeners) {
			listener.windowDeactivated();
		}
	}

	void fireWindowIconified() {
		for (final IWindowListener listener : windowListeners) {
			listener.windowIconified();
		}
	}

	void fireWindowDeiconified() {
		for (final IWindowListener listener : windowListeners) {
			listener.windowDeiconified();
		}
	}

	void fireWindowClosed() {
		for (final IWindowListener listener : windowListeners) {
			listener.windowClosed();
		}
	}

	void fireMenuActivated() {
		for (final IMenuListener listener : menuListeners) {
			listener.menuActivated();
		}
	}

	void fireMenuDeactivated() {
		for (final IMenuListener listener : menuListeners) {
			listener.menuDeactivated();
		}
	}

	void fireTreeNodeSelectionChanged(final boolean selected) {
		for (final ITreeNodeListener listener : treeNodeListeners) {
			listener.selectionChanged(selected);
		}
	}

	void fireTreeNodeExpanded(final boolean expanded) {
		for (final ITreeNodeListener listener : treeNodeListeners) {
			listener.expandedChanged(expanded);
		}
	}

	void fireTreeSelectionChanged() {
		for (final ITreeSelectionListenerSpi listener : treeSelectionListeners) {
			listener.selectionChanged();
		}
	}

	void fireFocusGained() {
		for (final IFocusListener listener : focusListeners) {
			listener.focusGained();
		}
	}

	void fireFocusLost() {
		for (final IFocusListener listener : focusListeners) {
			listener.focusLost();
		}
	}
}
