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

package org.jowidgets.spi.impl.swt.widgets.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jowidgets.common.types.SelectionPolicy;
import org.jowidgets.spi.impl.swt.widgets.SwtControl;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.spi.widgets.ITreeSpi;
import org.jowidgets.spi.widgets.setup.ITreeSetupSpi;

public class TreeImpl extends SwtControl implements ITreeSpi {

	public TreeImpl(final Object parentUiReference, final ITreeSetupSpi setup) {
		super(new Tree((Composite) parentUiReference, getStyle(setup)));

		//TODO remove later
		for (int i = 0; i < 10; i++) {
			final TreeItem item = new TreeItem(getUiReference(), 0);
			item.setText("Item " + i);

			for (int j = 0; j < 10; j++) {
				final TreeItem subItem = new TreeItem(item, 0);
				subItem.setText("SubItem " + j);

				for (int k = 0; k < 10; k++) {
					final TreeItem subSubItem = new TreeItem(subItem, 0);
					subSubItem.setText("SubSubItem " + k);
				}
			}
		}
	}

	@Override
	public Tree getUiReference() {
		return (Tree) super.getUiReference();
	}

	@Override
	public ITreeNodeSpi getRootNode() {
		// TODO Auto-generated method stub
		return null;
	}

	private static int getStyle(final ITreeSetupSpi setup) {
		int result = SWT.NONE;

		if (setup.isContentScrolled()) {
			result = result | SWT.V_SCROLL | SWT.H_SCROLL;
		}

		if (SelectionPolicy.MULTI_SELECTION == setup.getSelectionPolicy()) {
			result = result | SWT.MULTI;
		}
		else if (SelectionPolicy.SINGLE_SELECTION != setup.getSelectionPolicy()) {
			throw new IllegalArgumentException("SelectionPolicy '" + setup.getSelectionPolicy() + "' is not known");
		}

		return result;
	}
}
