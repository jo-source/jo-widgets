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

package org.jowidgets.examples.common.workbench.demo3.command;

import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IEnabledState;
import org.jowidgets.examples.common.workbench.demo3.model.BeanTableModel;
import org.jowidgets.tools.command.AbstractEnabledChecker;
import org.jowidgets.tools.controller.TableDataModelAdapter;

public final class SingleSelectionEnabledChecker extends AbstractEnabledChecker {

	private final BeanTableModel<?> model;

	public SingleSelectionEnabledChecker(final BeanTableModel<?> model) {
		this.model = model;

		model.addDataModelListener(new TableDataModelAdapter() {
			@Override
			public void selectionChanged() {
				fireEnabledStateChanged();
			}
		});
	}

	@Override
	public IEnabledState getEnabledState() {
		if (model.getSelection().size() == 1) {
			return EnabledState.ENABLED;
		}
		else {
			return EnabledState.disabled("Exactly one dataset must be selected!");
		}
	}
}
