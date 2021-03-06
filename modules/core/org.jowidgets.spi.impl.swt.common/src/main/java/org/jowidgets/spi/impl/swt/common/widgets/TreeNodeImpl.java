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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.spi.impl.controller.TreeNodeObservable;
import org.jowidgets.spi.impl.swt.common.color.ColorCache;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;
import org.jowidgets.spi.impl.swt.common.util.ColorConvert;
import org.jowidgets.spi.impl.swt.common.util.FontProvider;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.util.Assert;

public class TreeNodeImpl extends TreeNodeObservable implements ITreeNodeSpi {

    private final Set<IPopupDetectionListener> popupDetectionListeners;

    private final TreeImpl parentTree;
    private final TreeItem item;
    private final SwtImageRegistry imageRegistry;

    private String toolTipText;

    private Boolean expanded;

    private boolean selected;
    private boolean checkable;

    private IColorConstant lastCheckableColor;

    public TreeNodeImpl(
        final TreeImpl parentTree,
        final TreeItem parentItem,
        final Integer index,
        final SwtImageRegistry imageRegistry) {
        Assert.paramNotNull(parentTree, "parentTree");

        this.imageRegistry = imageRegistry;

        this.popupDetectionListeners = new HashSet<IPopupDetectionListener>();

        this.parentTree = parentTree;
        this.checkable = true;

        if (index != null) {
            if (parentItem == null) {
                this.item = new TreeItem(parentTree.getUiReference(), SWT.NONE, index.intValue());
            }
            else {
                this.item = new TreeItem(parentItem, SWT.NONE, index.intValue());
            }
        }
        else {
            if (parentItem == null) {
                this.item = new TreeItem(parentTree.getUiReference(), SWT.NONE);
            }
            else {
                this.item = new TreeItem(parentItem, SWT.NONE);
            }
        }

        item.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(final DisposeEvent arg0) {
                parentTree.unRegisterItem(item);
            }
        });

        this.selected = false;

    }

    @Override
    public TreeItem getUiReference() {
        return item;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        if (!enabled) {
            throw new UnsupportedOperationException("Tree items can not be disabled");
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setText(final String text) {
        if (text != null) {
            getUiReference().setText(text);
        }
        else {
            getUiReference().setText(String.valueOf(""));
        }
    }

    @Override
    public void setToolTipText(final String toolTipText) {
        this.toolTipText = toolTipText;
    }

    public String getToolTipText() {
        return toolTipText;
    }

    @Override
    public void setIcon(final IImageConstant icon) {
        final Image oldImage = getUiReference().getImage();
        final Image newImage = imageRegistry.getImage(icon);
        if (oldImage != newImage) {
            getUiReference().setImage(newImage);
        }
    }

    @Override
    public void setMarkup(final Markup markup) {
        final Font newFont = FontProvider.deriveFont(item.getFont(), markup);
        item.setFont(newFont);
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        getUiReference().setForeground(ColorCache.getInstance().getColor(colorValue));
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        getUiReference().setBackground(ColorCache.getInstance().getColor(colorValue));
    }

    @Override
    public void setChecked(final boolean checked) {
        getUiReference().setChecked(checked);
        fireCheckedChanged(checked);
    }

    @Override
    public boolean isChecked() {
        return getUiReference().getChecked();
    }

    @Override
    public void setGreyed(final boolean grayed) {
        getUiReference().setGrayed(grayed);
    }

    @Override
    public boolean isGreyed() {
        return getUiReference().getGrayed();
    }

    @Override
    public void setCheckable(final boolean checkable) {
        this.checkable = checkable;
        if (!checkable) {
            parentTree.addUncheckableItem(item);
            if (isChecked()) {
                setChecked(false);
            }
            lastCheckableColor = ColorConvert.convert(getUiReference().getForeground());
            setForegroundColor(TreeImpl.DISABLED_COLOR);
        }
        else {
            parentTree.removeUncheckableItem(item);
            setForegroundColor(lastCheckableColor);
        }
    }

    boolean isCheckable() {
        return checkable;
    }

    @Override
    public void setExpanded(final boolean expanded) {
        item.setExpanded(expanded);
        if (item.getExpanded() == expanded) {
            fireExpandedChanged(expanded);
        }
    }

    @Override
    public void fireExpandedChanged(final boolean expanded) {
        this.expanded = Boolean.valueOf(expanded);
        super.fireExpandedChanged(expanded);
    }

    @Override
    public boolean isExpanded() {
        if (expanded != null) {
            return expanded.booleanValue();
        }
        else {
            return item.getExpanded();
        }
    }

    @Override
    public void setSelected(final boolean selected) {
        if (this.selected != selected) {
            parentTree.setSelected(this, selected);
            this.selected = selected;
        }
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void addPopupDetectionListener(final IPopupDetectionListener listener) {
        popupDetectionListeners.add(listener);
    }

    @Override
    public void removePopupDetectionListener(final IPopupDetectionListener listener) {
        popupDetectionListeners.remove(listener);
    }

    protected void firePopupDetected(final Position position) {
        for (final IPopupDetectionListener listener : popupDetectionListeners) {
            listener.popupDetected(position);
        }
    }

    @Override
    public ITreeNodeSpi addNode(final Integer index) {
        final TreeNodeImpl result = new TreeNodeImpl(parentTree, item, index, imageRegistry);
        parentTree.registerItem(result.getUiReference(), result);
        return result;
    }

    @Override
    public void removeNode(final int index) {
        final TreeItem child = getUiReference().getItem(index);
        if (child != null) {
            if (parentTree.isNodeSelected(child)) {
                final TreeNodeImpl treeNode = parentTree.getTreeNodeItem(child);
                if (treeNode != null) {
                    parentTree.setSelected(treeNode, false);
                }
            }
            final boolean wasExpanded = isExpanded();
            parentTree.unRegisterItem(child);
            child.dispose();

            if (wasExpanded && item.getItemCount() == 0) {
                item.setExpanded(false);
                fireExpandedChanged(false);
            }
        }
    }

    @Override
    public IPopupMenuSpi createPopupMenu() {
        return new PopupMenuImpl(parentTree.getUiReference(), imageRegistry);
    }

}
