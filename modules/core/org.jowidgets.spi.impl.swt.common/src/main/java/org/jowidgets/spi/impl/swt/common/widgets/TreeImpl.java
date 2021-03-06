/*
 * Copyright (c) 2011, grossmann
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.dnd.DropMode;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.SelectionPolicy;
import org.jowidgets.common.widgets.controller.ITreeNodeListener;
import org.jowidgets.spi.dnd.ITreeDropLocationSpi.TreeDropPositionSpi;
import org.jowidgets.spi.impl.controller.TreeSelectionObservableSpi;
import org.jowidgets.spi.impl.dnd.TreeDropLocationSpiImpl;
import org.jowidgets.spi.impl.swt.common.color.ColorCache;
import org.jowidgets.spi.impl.swt.common.dnd.IDropSelectionProvider;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;
import org.jowidgets.spi.impl.swt.common.options.SwtOptions;
import org.jowidgets.spi.impl.swt.common.util.PositionConvert;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.spi.widgets.ITreeSpi;
import org.jowidgets.spi.widgets.controller.ITreeSelectionListenerSpi;
import org.jowidgets.spi.widgets.setup.ITreeSetupSpi;
import org.jowidgets.util.Assert;

public class TreeImpl extends SwtControl implements ITreeSpi, ITreeNodeSpi, IDropSelectionProvider {

    static final ColorValue DISABLED_COLOR = new ColorValue(130, 130, 130);

    private final SwtImageRegistry imageRegistry;

    private final boolean multiSelection;
    private final Map<TreeItem, TreeNodeImpl> items;
    private final Set<TreeItem> uncheckableItems;
    private final TreeSelectionObservableSpi treeObservable;

    private final IColorConstant selectedForeground;
    private final IColorConstant selectedBackground;
    private final IColorConstant selectedBorder;
    private final IColorConstant disabledSelectedForeground;
    private final IColorConstant disabledSelectedBackground;
    private final IColorConstant disabledSelectedBorder;

    private ToolTip toolTip;

    private List<TreeItem> lastSelection;

    public TreeImpl(final Object parentUiReference, final ITreeSetupSpi setup, final SwtImageRegistry imageRegistry) {
        super(new Tree((Composite) parentUiReference, getStyle(setup)), imageRegistry);

        this.imageRegistry = imageRegistry;
        this.lastSelection = new LinkedList<TreeItem>();
        this.treeObservable = new TreeSelectionObservableSpi();
        this.items = new HashMap<TreeItem, TreeNodeImpl>();
        this.uncheckableItems = new HashSet<TreeItem>();
        this.multiSelection = setup.getSelectionPolicy() == SelectionPolicy.MULTI_SELECTION;

        this.selectedForeground = setup.getSelectedForegroundColor() != null
                ? setup.getSelectedForegroundColor() : SwtOptions.getTreeSelectedForegroundColor();

        this.selectedBackground = setup.getSelectedBackgroundColor() != null
                ? setup.getSelectedBackgroundColor() : SwtOptions.getTreeSelectedBackgroundColor();

        this.selectedBorder = setup.getSelectedBorderColor() != null
                ? setup.getSelectedBorderColor() : SwtOptions.getTreeSelectedBorderColor();

        this.disabledSelectedForeground = setup.getDisabledSelectedForegroundColor() != null
                ? setup.getDisabledSelectedForegroundColor() : SwtOptions.getTreeDisabledSelectedForegroundColor();

        this.disabledSelectedBackground = setup.getDisabledSelectedBackgroundColor() != null
                ? setup.getDisabledSelectedBackgroundColor() : SwtOptions.getTreeDisabledSelectedBackgroundColor();

        this.disabledSelectedBorder = setup.getDisabledSelectedBorderColor() != null
                ? setup.getDisabledSelectedBorderColor() : SwtOptions.getTreeDisabledSelectedBorderColor();

        setMenuDetectListener(new MenuDetectListener() {
            @Override
            public void menuDetected(final MenuDetectEvent e) {
                final Point position = getUiReference().toControl(e.x, e.y);
                final TreeItem item = getUiReference().getItem(position);
                if (item == null) {
                    getPopupDetectionObservable().firePopupDetected(new Position(position.x, position.y));
                }
                else {
                    final TreeNodeImpl itemImpl = items.get(item);
                    itemImpl.firePopupDetected(new Position(position.x, position.y));
                }
            }
        });

        getUiReference().addTreeListener(new TreeListener() {

            @Override
            public void treeExpanded(final TreeEvent e) {
                fireExpandedChanged(e, true);
            }

            @Override
            public void treeCollapsed(final TreeEvent e) {
                fireExpandedChanged(e, false);
            }

            private void fireExpandedChanged(final TreeEvent event, final boolean expanded) {
                final TreeNodeImpl itemImpl = items.get(event.item);
                if (itemImpl != null) {
                    itemImpl.fireExpandedChanged(expanded);
                }
                else {
                    throw new IllegalStateException("No item impl registered for item '"
                        + event.item
                        + "'. This seems to be a bug");
                }
            }

        });

        final SelectionListener selectionListener = new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (e.item.isDisposed()) {
                    return;
                }
                if (e.detail == SWT.CHECK) {
                    final TreeNodeImpl itemImpl = items.get(e.item);

                    if (itemImpl != null) {
                        if (itemImpl.isCheckable()) {
                            itemImpl.fireCheckedChanged(itemImpl.isChecked());
                        }
                        else {
                            itemImpl.getUiReference().setChecked(itemImpl.getUiReference().getGrayed());
                        }
                    }
                    else {
                        throw new IllegalStateException("No item impl registered for item '"
                            + e.item
                            + "'. This seems to be a bug");
                    }
                }
                else {
                    if (!multiSelection) {
                        getUiReference().removeSelectionListener(this);
                        if ((e.stateMask & SWT.CTRL) > 0) {
                            final TreeItem[] selection = getUiReference().getSelection();
                            if (selection != null && selection.length > 1) {
                                getUiReference().setSelection(new TreeItem[] {(TreeItem) e.item});
                            }
                            else {
                                getUiReference().setSelection(new TreeItem[] {});
                            }
                        }
                        else {
                            getUiReference().setSelection(new TreeItem[] {(TreeItem) e.item});
                        }
                        getUiReference().addSelectionListener(this);
                    }

                    fireSelectionChange(getUiReference().getSelection());
                }
            }
        };

        getUiReference().addSelectionListener(selectionListener);

        // ToolTip support
        try {
            this.toolTip = new ToolTip(getUiReference().getShell(), SWT.NONE);
        }
        catch (final NoClassDefFoundError error) {
            //TODO MG rwt has no tooltip, may use a window instead. 
            //(New rwt version supports tooltips)
        }

        if (toolTip != null) {
            final ToolTipListener toolTipListener = new ToolTipListener();
            final Tree tree = getUiReference();
            tree.addListener(SWT.Dispose, toolTipListener);
            tree.addListener(SWT.KeyDown, toolTipListener);
            tree.addListener(SWT.MouseHover, toolTipListener);
            tree.addListener(SWT.MouseMove, toolTipListener);
        }

        if (selectedForeground != null
            || selectedBackground != null
            || selectedBorder != null
            || disabledSelectedForeground != null
            || disabledSelectedBackground != null
            || disabledSelectedBorder != null) {
            getUiReference().addListener(SWT.EraseItem, new CustomSelectionRenderingListener());
        }

    }

    @Override
    public Tree getUiReference() {
        return (Tree) super.getUiReference();
    }

    @Override
    public Object getDropSelection(final Widget item, final Position position, final int dropFeedback) {
        final TreeNodeImpl node = items.get(item);
        if (node != null) {
            if ((dropFeedback & DND.FEEDBACK_INSERT_BEFORE) != 0) {
                return new TreeDropLocationSpiImpl(node, TreeDropPositionSpi.BEFORE);
            }
            else if ((dropFeedback & DND.FEEDBACK_INSERT_AFTER) != 0) {
                return new TreeDropLocationSpiImpl(node, TreeDropPositionSpi.AFTER);
            }
            else if ((dropFeedback & DND.FEEDBACK_SELECT) != 0) {
                return new TreeDropLocationSpiImpl(node, TreeDropPositionSpi.ON);
            }
        }
        return null;
    }

    @Override
    public Integer getFeedback(final Widget widget, final Position position, final DropMode dropMode) {
        final int result = DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
        if (DropMode.SELECT.equals(dropMode)) {
            return result | DND.FEEDBACK_SELECT;
        }
        else if (DropMode.SELECT_OR_INSERT.equals(dropMode)) {
            if (widget instanceof TreeItem) {
                final TreeItem item = (TreeItem) widget;
                if (item.isDisposed()) {//pragmatic approach, no clear why swt throws events for disposed items
                    return result;
                }
                final int y = position.getY();
                final Rectangle bounds = item.getBounds();
                if (y < bounds.y + bounds.height / 3) {
                    return result | DND.FEEDBACK_INSERT_BEFORE;
                }
                else if (y > bounds.y + 2 * bounds.height / 3) {
                    return result | DND.FEEDBACK_INSERT_AFTER;
                }
                else {
                    return result | DND.FEEDBACK_SELECT;
                }
            }
        }
        else if (DropMode.INSERT.equals(dropMode)) {
            if (widget instanceof TreeItem) {
                final TreeItem item = (TreeItem) widget;
                if (item.isDisposed()) {//pragmatic approach, no clear why swt throws events for disposed items
                    return result;
                }
                final int y = position.getY();
                final Rectangle bounds = item.getBounds();
                if (y < bounds.y + bounds.height / 2) {
                    return result | DND.FEEDBACK_INSERT_BEFORE;
                }
                else {
                    return result | DND.FEEDBACK_INSERT_AFTER;
                }
            }
        }
        return null;
    }

    @Override
    public ITreeNodeSpi getRootNode() {
        return this;
    }

    @Override
    public List<ITreeNodeSpi> getSelectedNodes() {
        final List<ITreeNodeSpi> result = new LinkedList<ITreeNodeSpi>();
        for (final TreeItem item : getUiReference().getSelection()) {
            result.add(items.get(item));
        }
        return result;
    }

    @Override
    public ITreeNodeSpi addNode(final Integer index) {
        final TreeNodeImpl result = new TreeNodeImpl(this, null, index, imageRegistry);
        registerItem(result.getUiReference(), result);
        return result;
    }

    @Override
    public void removeNode(final int index) {
        final TreeItem child = getUiReference().getItem(index);
        if (child != null) {
            if (isNodeSelected(child)) {
                final TreeNodeImpl treeNode = items.get(child);
                if (treeNode != null) {
                    setSelected(treeNode, false);
                }
            }
            unRegisterItem(child);
            child.dispose();
        }
    }

    @Override
    public ITreeNodeSpi getNodeAt(final Position position) {
        Assert.paramNotNull(position, "position");
        final TreeItem item = getUiReference().getItem(PositionConvert.convert(position));
        if (item != null) {
            return items.get(item);
        }
        else {
            return null;
        }
    }

    @Override
    public void addTreeSelectionListener(final ITreeSelectionListenerSpi listener) {
        treeObservable.addTreeSelectionListener(listener);
    }

    @Override
    public void removeTreeSelectionListener(final ITreeSelectionListenerSpi listener) {
        treeObservable.removeTreeSelectionListener(listener);
    }

    @Override
    public void setMarkup(final Markup markup) {
        throw new UnsupportedOperationException("setMarkup is not possible on the root node");
    }

    @Override
    public void setExpanded(final boolean expanded) {
        throw new UnsupportedOperationException("setExpanded is not possible on the root node");
    }

    @Override
    public boolean isExpanded() {
        throw new UnsupportedOperationException("isExpanded is not possible on the root node");
    }

    @Override
    public void setSelected(final boolean selected) {
        throw new UnsupportedOperationException("setSelected is not possible on the root node");
    }

    @Override
    public boolean isSelected() {
        throw new UnsupportedOperationException("isSelected is not possible on the root node");
    }

    @Override
    public void setText(final String text) {
        throw new UnsupportedOperationException("setText is not possible on the root node");
    }

    @Override
    public void setToolTipText(final String text) {
        throw new UnsupportedOperationException("setToolTipText is not possible on the root node");
    }

    @Override
    public void setIcon(final IImageConstant icon) {
        throw new UnsupportedOperationException("setIcon is not possible on the root node");
    }

    @Override
    public void addTreeNodeListener(final ITreeNodeListener listener) {
        throw new UnsupportedOperationException("addTreeNodeListener is not possible on the root node");
    }

    @Override
    public void removeTreeNodeListener(final ITreeNodeListener listener) {
        throw new UnsupportedOperationException("removeTreeNodeListener is not possible on the root node");
    }

    @Override
    public void setChecked(final boolean checked) {
        throw new UnsupportedOperationException("setChecked is not possible on the root node");
    }

    @Override
    public boolean isChecked() {
        throw new UnsupportedOperationException("isChecked is not possible on the root node");
    }

    @Override
    public void setGreyed(final boolean greyed) {
        throw new UnsupportedOperationException("setGreyed is not possible on the root node");
    }

    @Override
    public boolean isGreyed() {
        throw new UnsupportedOperationException("isGreyed is not possible on the root node");
    }

    @Override
    public void setCheckable(final boolean checkable) {
        throw new UnsupportedOperationException("setCheckable is not possible on the root node");
    }

    private void showToolTip(final String message) {
        toolTip.setMessage(message);
        final Point location = Display.getCurrent().getCursorLocation();
        toolTip.setLocation(location.x + 16, location.y + 16);
        toolTip.setVisible(true);
    }

    protected void setSelected(final TreeNodeImpl treeNode, final boolean selected) {
        if (selected != treeNode.isSelected()) {
            final TreeItem[] newSelection;
            if (multiSelection) {
                if (selected) {//add item to selection
                    TreeItem[] oldSelection = getUiReference().getSelection();
                    if (oldSelection == null) {
                        oldSelection = new TreeItem[0];
                    }
                    newSelection = new TreeItem[oldSelection.length + 1];
                    newSelection[0] = treeNode.getUiReference();
                    for (int i = 0; i < oldSelection.length; i++) {
                        newSelection[i + 1] = oldSelection[i];
                    }
                }
                else {//not selected, so remove item from selection
                    final TreeItem[] oldSelection = getUiReference().getSelection();
                    if (oldSelection == null || oldSelection.length == 0) {
                        //nothing to remove from, so return
                        return;
                    }
                    newSelection = new TreeItem[oldSelection.length - 1];
                    int offset = 0;
                    for (int i = 0; i < newSelection.length; i++) {
                        if (oldSelection[i] == treeNode.getUiReference()) {
                            offset = 1;
                        }
                        else {
                            newSelection[i] = oldSelection[i + offset];
                        }
                    }
                }
            }
            else {//single selection
                if (selected) {
                    newSelection = new TreeItem[1];
                    newSelection[0] = treeNode.getUiReference();
                }
                else {
                    newSelection = new TreeItem[0];
                }
            }
            getUiReference().setSelection(newSelection);
            fireSelectionChange(newSelection);
        }
    }

    boolean isNodeSelected(final TreeItem item) {
        final TreeNodeImpl treeNode = getTreeNodeItem(item);
        if (treeNode != null) {
            return treeNode.isSelected();
        }
        else {
            return false;
        }
    }

    protected TreeNodeImpl getTreeNodeItem(final TreeItem item) {
        return items.get(item);
    }

    protected void registerItem(final TreeItem item, final TreeNodeImpl treeNodeImpl) {
        items.put(item, treeNodeImpl);
    }

    protected void unRegisterItem(final TreeItem item) {
        items.remove(item);
        uncheckableItems.remove(item);
    }

    void addUncheckableItem(final TreeItem item) {
        Assert.paramNotNull(item, "item");
        uncheckableItems.add(item);
    }

    void removeUncheckableItem(final TreeItem item) {
        Assert.paramNotNull(item, "item");
        uncheckableItems.remove(item);
    }

    private void fireSelectionChange(final TreeItem[] newSelection) {
        final List<TreeItem> newSelectionList = Arrays.asList(newSelection);

        boolean selectionChanged = false;

        for (final TreeItem wasSelected : lastSelection) {
            if (!newSelectionList.contains(wasSelected)) {
                final TreeNodeImpl treeNodeImpl = items.get(wasSelected);
                if (treeNodeImpl != null) {
                    selectionChanged = true;
                    treeNodeImpl.fireSelectionChanged(false);
                }
            }
        }

        for (final TreeItem isSelected : newSelectionList) {
            if (!lastSelection.contains(isSelected)) {
                final TreeNodeImpl treeNodeImpl = items.get(isSelected);
                if (treeNodeImpl != null) {
                    selectionChanged = true;
                    treeNodeImpl.fireSelectionChanged(true);
                }
            }
        }

        lastSelection = newSelectionList;
        if (selectionChanged) {
            treeObservable.fireSelectionChanged();
        }
    }

    private static int getStyle(final ITreeSetupSpi setup) {
        //do not use the single selection mode of SWT, it behaves strange!!!
        //e.g. tree get auto selected when it get the focus
        //selection could no disabled with clicking on item together with CTRL
        //single selection will be simulated by selection listener
        int result = SWT.MULTI;

        if (setup.isChecked()) {
            result = result | SWT.CHECK;
        }
        if (!setup.isContentScrolled()) {
            result = result | SWT.NO_SCROLL;
        }

        return result;
    }

    final class ToolTipListener implements Listener {

        @Override
        public void handleEvent(final Event event) {
            if (event.type == SWT.MouseHover) {
                final TreeItem item = getUiReference().getItem(new Point(event.x, event.y));

                if (item != null) {
                    final TreeNodeImpl itemImpl = items.get(item);
                    toolTip.setVisible(false);
                    if (itemImpl.getToolTipText() != null) {
                        showToolTip(itemImpl.getToolTipText());
                    }
                }
            }
            else {
                if (toolTip != null && !toolTip.isDisposed()) {
                    toolTip.setVisible(false);
                }
            }
        }
    }

    private final class CustomSelectionRenderingListener implements Listener {

        @Override
        public void handleEvent(final Event event) {
            final TreeItem item = (TreeItem) event.item;

            final GC gc = event.gc;

            final Color currentBackground = gc.getBackground();
            final Color currentForeground = gc.getForeground();

            final boolean checkable = !uncheckableItems.contains(item);

            if ((event.detail & SWT.SELECTED) != 0) {
                final Rectangle rect = item.getBounds(0);

                if (selectedBackground != null && checkable) {
                    gc.setBackground(ColorCache.getInstance().getColor(selectedBackground));
                    gc.fillRectangle(rect);
                    event.detail &= ~SWT.SELECTED;
                }
                else if (disabledSelectedBackground != null && !checkable) {
                    gc.setBackground(ColorCache.getInstance().getColor(disabledSelectedBackground));
                    gc.fillRectangle(rect);
                    event.detail &= ~SWT.SELECTED;
                }
                else {
                    gc.setBackground(currentBackground);
                }

                if (selectedBorder != null && checkable) {
                    gc.setForeground(ColorCache.getInstance().getColor(selectedBorder));
                    gc.drawRectangle(rect.x, rect.y, rect.width, rect.height - 1);
                    event.detail &= ~SWT.SELECTED;
                }
                else if (disabledSelectedBorder != null && !checkable) {
                    gc.setForeground(ColorCache.getInstance().getColor(disabledSelectedBorder));
                    gc.drawRectangle(rect.x, rect.y, rect.width, rect.height - 1);
                    event.detail &= ~SWT.SELECTED;
                }

                if (selectedForeground != null && checkable) {
                    gc.setForeground(ColorCache.getInstance().getColor(selectedForeground));
                    event.detail &= ~SWT.SELECTED;
                }
                else if (disabledSelectedForeground != null && !checkable) {
                    gc.setForeground(ColorCache.getInstance().getColor(disabledSelectedForeground));
                    event.detail &= ~SWT.SELECTED;
                }
                else {
                    gc.setForeground(currentForeground);
                }

            }
            else if ((event.detail & SWT.HOT) != 0) {
                event.detail &= ~SWT.HOT;
                return;
            }

        }
    }

}
