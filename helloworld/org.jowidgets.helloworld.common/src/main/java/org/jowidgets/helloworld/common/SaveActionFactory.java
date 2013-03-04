/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.helloworld.common;

import org.jowidgets.api.command.CommandAction;
import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IEnabledChecker;
import org.jowidgets.api.command.IEnabledState;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.model.item.ICheckedItemModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.widgets.controller.IItemStateListener;
import org.jowidgets.util.event.ChangeObservable;

public final class SaveActionFactory {

	private SaveActionFactory() {}

	public static IAction create(final ICheckedItemModel checkedItem) {
		final IActionBuilder builder = CommandAction.builder();
		builder.setText("Save");
		builder.setIcon(IconsSmall.DISK);
		builder.setCommand(new SaveCommand(), new SaveEnabledChecker(checkedItem));
		return builder.build();
	}

	private static final class SaveCommand implements ICommandExecutor {
		@Override
		public void execute(final IExecutionContext executionContext) throws Exception {
			Toolkit.getMessagePane().showInfo(executionContext, "Save done!");
		}
	}

	private static final class SaveEnabledChecker extends ChangeObservable implements IEnabledChecker {

		private final ICheckedItemModel checkedItem;

		private SaveEnabledChecker(final ICheckedItemModel checkedItem) {
			this.checkedItem = checkedItem;
			checkedItem.addItemListener(new IItemStateListener() {
				@Override
				public void itemStateChanged() {
					fireChangedEvent();
				}
			});
		}

		@Override
		public IEnabledState getEnabledState() {
			if (!checkedItem.isSelected()) {
				return EnabledState.disabled("Checked item must be enabled");
			}
			else {
				return EnabledState.ENABLED;
			}
		}
	}

}
