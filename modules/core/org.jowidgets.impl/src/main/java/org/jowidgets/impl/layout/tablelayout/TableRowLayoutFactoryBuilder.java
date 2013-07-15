/*
 * Copyright (c) 2011, Nikolaus Moll
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

package org.jowidgets.impl.layout.tablelayout;

import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.layout.tablelayout.ITableLayout;
import org.jowidgets.api.layout.tablelayout.ITableRowLayout;
import org.jowidgets.api.layout.tablelayout.ITableRowLayoutFactoryBuilder;
import org.jowidgets.api.widgets.IContainer;

public final class TableRowLayoutFactoryBuilder implements ITableRowLayoutFactoryBuilder {

	private final ITableLayout common;
	private boolean ignore;

	public TableRowLayoutFactoryBuilder(final ITableLayout common) {
		this.common = common;
	}

	@Override
	public ITableRowLayoutFactoryBuilder ignoreInCalculations(final boolean ignore) {
		this.ignore = ignore;
		return this;
	}

	@Override
	public ILayoutFactory<ITableRowLayout> build() {
		return new ILayoutFactory<ITableRowLayout>() {
			@Override
			public ITableRowLayout create(final IContainer container) {
				return new TableRowLayout(container, common, ignore);
			}
		};
	}

}
