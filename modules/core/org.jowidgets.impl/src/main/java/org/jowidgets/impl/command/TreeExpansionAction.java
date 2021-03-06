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

package org.jowidgets.impl.command;

import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ICommand;
import org.jowidgets.api.command.ICommandAction;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IEnabledChecker;
import org.jowidgets.api.command.ITreeExpansionAction;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.i18n.api.MessageReplacer;
import org.jowidgets.tools.command.ActionWrapper;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.IFilter;
import org.jowidgets.util.NullCompatibleEquivalence;

final class TreeExpansionAction extends ActionWrapper implements ITreeExpansionAction {

    private final String unboundPivotlevelLabel;
    private final String boundPivotlevelLabel;
    private final TreeExpansionExecutor executor;
    private final TreeExpansionEnabledChecker enabledChecker;
    private final ICommandAction action;

    TreeExpansionAction(
        final IActionBuilder builder,
        final ITreeContainer tree,
        final ExpansionMode expansionMode,
        final IFilter<ITreeNode> filter,
        final boolean enabledChecking,
        final Integer pivotLevel,
        final String unboundPivotlevelLabel,
        final String boundPivotlevelLabel) {

        super(createAction(builder, tree, expansionMode, filter, enabledChecking, pivotLevel));

        this.unboundPivotlevelLabel = unboundPivotlevelLabel;
        this.boundPivotlevelLabel = boundPivotlevelLabel;

        this.action = ((ICommandAction) unwrap());

        final ICommand command = action.getCommand();
        this.executor = (TreeExpansionExecutor) command.getCommandExecutor();
        this.enabledChecker = (TreeExpansionEnabledChecker) command.getEnabledChecker();
    }

    private static ICommandAction createAction(
        final IActionBuilder builder,
        final ITreeContainer tree,
        final ExpansionMode expansionMode,
        final IFilter<ITreeNode> filter,
        final boolean enabledChecking,
        final Integer level) {

        final ICommandExecutor executor = new TreeExpansionExecutor(tree, expansionMode, filter, level);

        if (enabledChecking) {
            final IEnabledChecker enabledChecker = new TreeExpansionEnabledChecker(tree, expansionMode, filter, level);
            return builder.setCommand(executor, enabledChecker).build();
        }
        else {
            return builder.setCommand(executor).build();
        }
    }

    @Override
    public void setPivotLevel(final Integer pivotLevel) {
        setPivotLevel(pivotLevel, unboundPivotlevelLabel);
    }

    @Override
    public void setPivotLevel(final Integer level, final String levelName) {
        if (level == null || levelName == null || EmptyCheck.isEmpty(boundPivotlevelLabel)) {
            if (!NullCompatibleEquivalence.equals(action.getText(), unboundPivotlevelLabel)) {
                action.setText(unboundPivotlevelLabel);
            }
        }
        else {
            action.setText(MessageReplacer.replace(boundPivotlevelLabel, levelName));
        }
        executor.setPivotLevel(level);
        if (enabledChecker != null) {
            enabledChecker.setPivotLevel(level);
        }
    }

    @Override
    public void setPivotLevel(final int level) {
        setPivotLevel(Integer.valueOf(level));
    }

    @Override
    public void setPivotLevel(final int level, final String levelName) {
        setPivotLevel(Integer.valueOf(level), levelName);
    }

}
