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

package org.jowidgets.workbench.impl;

import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IComponentNode;
import org.jowidgets.workbench.api.ILayout;

public final class ComponentContext implements IComponentContext {

    private final ComponentNodeContext nodeContext;
    private final WorkbenchContentPanel workbenchContentPanel;
    private final IComponent component;

    private boolean active;
    private boolean layoutReset;
    private ILayout currentLayout;

    public ComponentContext(final IComponentNode componentNode, final ComponentNodeContext nodeContext) {
        this.active = false;
        this.layoutReset = false;
        this.nodeContext = nodeContext;
        this.workbenchContentPanel = nodeContext.getWorkbenchContext().getWorkbenchContentPanel();
        this.component = componentNode.createComponent(this);
    }

    public void activate() {
        if (!active) {
            this.active = true;
            if (component != null) {
                if (currentLayout != null && layoutReset) {
                    workbenchContentPanel.resetLayout(this, currentLayout);
                }
                if (currentLayout != null) {
                    workbenchContentPanel.setLayout(this, currentLayout);
                }
                else {
                    workbenchContentPanel.setEmptyContent();
                }
                component.onActivation();
            }
            else {
                workbenchContentPanel.setEmptyContent();
            }
        }
    }

    public VetoHolder deactivate() {
        final VetoHolder result = new VetoHolder();
        if (active) {
            this.active = false;
            if (component != null) {
                component.onDeactivation(result);
                if (result.hasVeto()) {
                    this.active = true;
                }
            }
        }
        return result;
    }

    public void onDispose() {
        if (component != null) {
            component.onDispose();
        }
        workbenchContentPanel.disposeComponent(this);
    }

    @Override
    public void setLayout(final ILayout layout) {
        this.currentLayout = layout;
        if (active && currentLayout != null) {
            workbenchContentPanel.setLayout(this, currentLayout);
        }
        else if (active) {
            workbenchContentPanel.setEmptyContent();
        }
    }

    @Override
    public void resetLayout(final ILayout layout) {
        this.currentLayout = layout;
        if (active && currentLayout != null) {
            workbenchContentPanel.resetLayout(this, currentLayout);
        }
        else if (active) {
            workbenchContentPanel.setEmptyContent();
        }
        else {
            layoutReset = true;
        }
    }

    @Override
    public ComponentNodeContext getComponentNodeContext() {
        return nodeContext;
    }

    @Override
    public WorkbenchApplicationContext getWorkbenchApplicationContext() {
        return getComponentNodeContext().getWorkbenchApplicationContext();
    }

    @Override
    public WorkbenchContext getWorkbenchContext() {
        return getWorkbenchApplicationContext().getWorkbenchContext();
    }

    protected IComponent getComponent() {
        return component;
    }

}
