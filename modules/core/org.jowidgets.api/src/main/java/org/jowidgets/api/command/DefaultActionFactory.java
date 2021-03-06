/*
 * Copyright (c) 2014, MGrossmann
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

package org.jowidgets.api.command;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.ITreeContainer;

public final class DefaultActionFactory {

    private static IDefaultActionFactory instance;

    private DefaultActionFactory() {}

    public static IDefaultActionFactory getInstance() {
        if (instance == null) {
            instance = Toolkit.getDefaultActionFactory();
        }
        return instance;
    }

    public static ITreeExpansionActionBuilder collapseTreeActionBuilder(final ITreeContainer tree) {
        return getInstance().collapseTreeActionBuilder(tree);
    }

    public static ITreeExpansionAction collapseTreeAction(final ITreeContainer tree) {
        return getInstance().collapseTreeAction(tree);
    }

    public static ITreeExpansionActionBuilder expandTreeActionBuilder(final ITreeContainer tree) {
        return getInstance().expandTreeActionBuilder(tree);
    }

    public static ITreeExpansionAction expandTreeAction(final ITreeContainer tree) {
        return getInstance().expandTreeAction(tree);
    }

    public static ITreeExpansionActionBuilder expandCollapseTreeActionBuilder(final ITreeContainer tree) {
        return getInstance().expandCollapseTreeActionBuilder(tree);
    }

    public static ITreeExpansionAction expandCollapseTreeAction(final ITreeContainer tree) {
        return getInstance().expandCollapseTreeAction(tree);
    }

    public static ITreeExpansionActionBuilder expandCheckedNodesTreeActionBuilder(final ITreeContainer tree) {
        return getInstance().expandCheckedNodesTreeActionBuilder(tree);
    }

    public static ITreeExpansionAction expandCheckedNodesTreeAction(final ITreeContainer tree) {
        return getInstance().expandCheckedNodesTreeAction(tree);
    }

    public static IDefaultActionBuilder checkTreeActionBuilder(final ITreeContainer tree) {
        return getInstance().checkTreeActionBuilder(tree);
    }

    public static IAction checkTreeAction(final ITreeContainer tree) {
        return getInstance().checkTreeAction(tree);
    }

    public static IDefaultActionBuilder uncheckTreeActionBuilder(final ITreeContainer tree) {
        return getInstance().uncheckTreeActionBuilder(tree);
    }

    public static IAction uncheckTreeAction(final ITreeContainer tree) {
        return getInstance().uncheckTreeAction(tree);
    }

}
