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

package org.jowidgets.impl.model;

import org.jowidgets.api.model.IModelFactoryProvider;
import org.jowidgets.api.model.item.IItemModelFactory;
import org.jowidgets.api.model.levelmeter.ILevelMeterModelFactory;
import org.jowidgets.api.model.table.ITableModelFactory;
import org.jowidgets.impl.model.item.ItemModelFactory;
import org.jowidgets.impl.model.levelmeter.LevelMeterModelFactoryImpl;
import org.jowidgets.impl.model.table.TableModelFactory;

public class ModelFactoryProvider implements IModelFactoryProvider {

	private IItemModelFactory itemModelFactory;
	private ITableModelFactory tableModelFactory;
	private ILevelMeterModelFactory levelMeterModelFactory;

	@Override
	public IItemModelFactory getItemModelFactory() {
		if (itemModelFactory == null) {
			itemModelFactory = new ItemModelFactory();
		}
		return itemModelFactory;
	}

	@Override
	public ITableModelFactory getTableModelFactory() {
		if (tableModelFactory == null) {
			this.tableModelFactory = new TableModelFactory();
		}
		return tableModelFactory;
	}

	@Override
	public ILevelMeterModelFactory getLevelMeterModelFactory() {
		if (levelMeterModelFactory == null) {
			this.levelMeterModelFactory = new LevelMeterModelFactoryImpl();
		}
		return levelMeterModelFactory;
	}

}
