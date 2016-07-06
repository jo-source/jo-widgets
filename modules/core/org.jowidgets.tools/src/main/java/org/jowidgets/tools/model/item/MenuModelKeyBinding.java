/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.tools.model.item;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionChangeObservable;
import org.jowidgets.api.command.IExceptionHandler;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.controller.IListenerFactory;
import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IMenuItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IKeyObservable;
import org.jowidgets.tools.controller.KeyAdapter;
import org.jowidgets.tools.controller.KeyObservable;
import org.jowidgets.tools.controller.ListModelAdapter;
import org.jowidgets.util.Assert;
import org.jowidgets.util.ITypedKey;

/**
 * This class helps to bind all actions of a given menu to theirs accelerator keys
 */
public final class MenuModelKeyBinding {

    private final Collection<IMenuModel> menus;
    private final IKeyObservable sourceKeyObservable;
    private final IWidget sourceWidget;

    private final IKeyListener keyListener;
    private final IListModelListener listModelListener;

    private final Map<Accelerator, IAction> actions;

    private Runnable actionsMapUpdater;

    /**
     * Creates a new binding for a menu and a component that contains the menu.
     * 
     * If the given component is a container, a recursive key lister will be added,
     * otherwise a 'normal' key listener will be used for the key binding.
     * 
     * If the component will be disposed, the binding will be disposed also
     * 
     * @param menu The menu which actions should be bound
     * @param component The component which receives the key events
     */
    public MenuModelKeyBinding(final IMenuModel menu, final IComponent component) {
        this(Collections.singleton(menu), component);
    }

    /**
     * Creates a new binding for a collection of menus and a component that contains the menu.
     * 
     * If the given component is a container, a recursive key lister will be added,
     * otherwise a 'normal' key listener will be used for the key binding.
     * 
     * If the component will be disposed, the binding will be disposed also
     * 
     * @param menu The menu which actions should be bound
     * @param component The component which receives the key events
     */
    public MenuModelKeyBinding(final Collection<? extends IMenuModel> menus, final IComponent component) {
        this(menus, createRecursiveKeyObservable(component), component);

        //dispose the binding if the component becomes disposed
        component.addDisposeListener(new IDisposeListener() {
            @Override
            public void onDispose() {
                dispose();
            }
        });
    }

    /**
     * Creates a new key binding for menu and a given key observable
     * 
     * @param menu The menu for the key binding
     * @param sourceKeyObservable The key observable that receives the key events
     * @param source The source widget that will be used for the execution context
     */
    public MenuModelKeyBinding(final IMenuModel menu, final IKeyObservable sourceKeyObservable, final IWidget source) {
        this(Collections.singleton(menu), sourceKeyObservable, source);
    }

    /**
     * Creates a new key binding for collection of menus and a given key observable
     * 
     * @param menu The menu for the key binding
     * @param sourceKeyObservable The key observable that receives the key events
     * @param source The source widget that will be used for the execution context
     */
    public MenuModelKeyBinding(
        final Collection<? extends IMenuModel> menus,
        final IKeyObservable sourceKeyObservable,
        final IWidget source) {
        Assert.paramNotNull(menus, "menus");
        Assert.paramNotNull(sourceKeyObservable, "sourceKeyObservable");
        Assert.paramNotNull(source, "source");

        this.menus = new LinkedList<IMenuModel>(menus);
        this.sourceKeyObservable = sourceKeyObservable;
        this.sourceWidget = source;

        this.actions = new HashMap<Accelerator, IAction>();

        this.keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(final IKeyEvent event) {
                final VirtualKey virtualKey = event.getVirtualKey();
                if (VirtualKey.UNDEFINED != virtualKey) {
                    final Accelerator pressedAccelerator = getAccelerator(event);
                    if (pressedAccelerator != null) {
                        final IAction action = actions.get(pressedAccelerator);
                        if (action != null && action.isEnabled()) {
                            final IExecutionContext executionContext = new ExecutionContext(action);
                            try {
                                action.execute(executionContext);
                            }
                            catch (final Exception e) {
                                try {
                                    final IExceptionHandler exceptionHandler = action.getExceptionHandler();
                                    if (exceptionHandler != null) {
                                        exceptionHandler.handleException(executionContext, e);
                                    }
                                    else {
                                        throw e;
                                    }
                                }
                                catch (final Exception e1) {
                                    final UncaughtExceptionHandler uncaughtExceptionHandler = Thread.currentThread()
                                            .getUncaughtExceptionHandler();
                                    if (uncaughtExceptionHandler != null) {
                                        uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };

        this.listModelListener = new ListModelAdapter() {

            @Override
            public void afterChildRemoved(final int index) {
                updateActionsMapLater();
            }

            @Override
            public void afterChildAdded(final int index) {
                updateActionsMapLater();
            }
        };

        updateActionsMap();
    }

    /**
     * Binds a collection of menus to a component
     * 
     * When bound all key events of the component (recursive, if component is a IContainer) will be
     * checked against the key accelerators of the menu and the actions will be performed if accelerator
     * matches.
     * 
     * If the component will be disposed, the binding will be disposed too
     * 
     * @param menus The menus to bind
     * @param component The component to bind to
     * 
     * @return The KeyBinding
     */
    public static MenuModelKeyBinding bind(final Collection<? extends IMenuModel> menus, final IComponent component) {
        return new MenuModelKeyBinding(menus, component);
    }

    /**
     * Binds a menu to a component
     * 
     * When bound all key events of the component (recursive, if component is a IContainer) will be
     * checked against the key accelerators of the menu and the actions will be performed if accelerator
     * matches.
     * 
     * If the component will be disposed, the binding will be disposed too
     * 
     * @param menus The menus to bind
     * @param component The component to bind to
     * 
     * @return The KeyBinding
     */
    public static MenuModelKeyBinding bind(final IMenuModel menu, final IComponent component) {
        return new MenuModelKeyBinding(menu, component);
    }

    private static IKeyObservable createRecursiveKeyObservable(final IComponent component) {
        Assert.paramNotNull(component, "component");
        final KeyObservable result = new KeyObservable();
        final IKeyListener keyListener = new IKeyListener() {

            @Override
            public void keyReleased(final IKeyEvent event) {
                result.fireKeyReleased(event);
            }

            @Override
            public void keyPressed(final IKeyEvent event) {
                result.fireKeyPressed(event);
            }
        };
        if (component instanceof IContainer) {
            addKeyListenerRecursive((IContainer) component, keyListener);
        }
        else {
            addKeyListener(component, keyListener);
        }
        return result;
    }

    private static void addKeyListenerRecursive(final IContainer container, final IKeyListener keyListener) {
        Assert.paramNotNull(container, "container");
        Assert.paramNotNull(keyListener, "keyListener");

        final IListenerFactory<IKeyListener> listenerFactory = new IListenerFactory<IKeyListener>() {
            @Override
            public IKeyListener create(final IComponent component) {
                return keyListener;
            }
        };

        container.addKeyListenerRecursive(listenerFactory);

        container.addDisposeListener(new IDisposeListener() {
            @Override
            public void onDispose() {
                container.removeKeyListenerRecursive(listenerFactory);
            }
        });
    }

    private static void addKeyListener(final IComponent component, final IKeyListener keyListener) {
        Assert.paramNotNull(component, "component");
        Assert.paramNotNull(keyListener, "keyListener");
        component.addKeyListener(keyListener);

        component.addDisposeListener(new IDisposeListener() {
            @Override
            public void onDispose() {
                component.removeKeyListener(keyListener);
            }
        });
    }

    private static Accelerator getAccelerator(final IKeyEvent event) {
        final VirtualKey virtualKey = event.getVirtualKey();
        final Set<Modifier> modifier = event.getModifier();
        if (virtualKey != null && virtualKey != VirtualKey.UNDEFINED) {
            return new Accelerator(virtualKey, modifier);
        }
        else {
            final Character character = event.getCharacter();
            if (character != null) {
                return new Accelerator(virtualKey, modifier);
            }
        }
        return null;
    }

    /**
     * Adds a menu to the key binding
     * 
     * @param menu
     */
    public void addMenu(final IMenuModel menu) {
        menus.add(menu);
        updateActionsMapLater();
    }

    /**
     * Disposes the key binding
     */
    public void dispose() {
        disposeActionsMap();
        sourceKeyObservable.removeKeyListener(keyListener);
    }

    private void updateActionsMapLater() {
        if (actionsMapUpdater == null) {
            actionsMapUpdater = new Runnable() {
                @Override
                public void run() {
                    updateActionsMap();
                    actionsMapUpdater = null;
                }
            };
            Toolkit.getUiThreadAccess().invokeLater(actionsMapUpdater);
        }
    }

    private void updateActionsMap() {
        actions.clear();
        sourceKeyObservable.removeKeyListener(keyListener);
        for (final IMenuModel menu : menus) {
            updateActionsMap(menu);
        }
        if (!actions.isEmpty()) {
            sourceKeyObservable.addKeyListener(keyListener);
        }
    }

    private void updateActionsMap(final IMenuModel menu) {
        for (final IMenuItemModel item : menu.getChildren()) {
            if (item instanceof IActionItemModel) {

                final IAction action = ((IActionItemModel) item).getAction();
                if (action != null) {
                    final Accelerator accelerator = action.getAccelerator();
                    if (accelerator != null) {
                        actions.put(accelerator, action);
                    }
                }
                else {
                    final Accelerator accelerator = item.getAccelerator();
                    if (accelerator != null) {
                        actions.put(accelerator, new ActionItemAction((IActionItemModel) item));
                    }
                }
            }
            else if (item instanceof IMenuModel) {
                updateActionsMap((IMenuModel) item);
            }
        }
        menu.addListModelListener(listModelListener);
    }

    private void disposeActionsMap() {
        actions.clear();
        for (final IMenuModel menu : menus) {
            disposeActionsMap(menu);
        }
    }

    private void disposeActionsMap(final IMenuModel menu) {
        menu.removeListModelListener(listModelListener);
        for (final IMenuItemModel item : menu.getChildren()) {
            if (item instanceof IMenuModel) {
                disposeActionsMap((IMenuModel) item);
            }
        }
    }

    private final class ExecutionContext implements IExecutionContext {

        private final IAction action;

        private ExecutionContext(final IAction action) {
            Assert.paramNotNull(action, "action");
            this.action = action;
        }

        @Override
        public <VALUE_TYPE> VALUE_TYPE getValue(final ITypedKey<VALUE_TYPE> key) {
            return null;
        }

        @Override
        public IAction getAction() {
            return action;
        }

        @Override
        public IWidget getSource() {
            return sourceWidget;
        }

    }

    private final class ActionItemAction implements IAction {

        private final IActionItemModel item;

        ActionItemAction(final IActionItemModel item) {
            Assert.paramNotNull(item, "item");
            this.item = item;
        }

        @Override
        public String getText() {
            return item.getText();
        }

        @Override
        public String getToolTipText() {
            return item.getToolTipText();
        }

        @Override
        public IImageConstant getIcon() {
            return item.getIcon();
        }

        @Override
        public Character getMnemonic() {
            return item.getMnemonic();
        }

        @Override
        public Accelerator getAccelerator() {
            return item.getAccelerator();
        }

        @Override
        public boolean isEnabled() {
            return item.isEnabled();
        }

        @Override
        public void execute(final IExecutionContext actionEvent) throws Exception {
            item.actionPerformed();
        }

        @Override
        public IExceptionHandler getExceptionHandler() {
            return null;
        }

        @Override
        public IActionChangeObservable getActionChangeObservable() {
            return null;
        }

    }
}
