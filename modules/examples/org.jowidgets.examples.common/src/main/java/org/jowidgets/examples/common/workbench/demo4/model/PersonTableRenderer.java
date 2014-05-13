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

package org.jowidgets.examples.common.workbench.demo4.model;

import org.jowidgets.api.model.table.ITableCellBuilder;
import org.jowidgets.api.model.table.ITableColumn;
import org.jowidgets.api.model.table.ITableColumnModel;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.tools.model.table.DefaultTableColumn;
import org.jowidgets.tools.model.table.TableCell;
import org.jowidgets.util.StringUtils;

public final class PersonTableRenderer implements IBeanTableRenderer<Person>, ITableColumnModel {

	@Override
	public ITableCell getCell(final int rowIndex, final int columnIndex, final Person bean) {
		if (columnIndex == 0) {
			return builder().setText(bean.getName()).build();
		}
		else if (columnIndex == 1) {
			if (bean.getDayOfBirth() != null) {
				return builder().setText("" + bean.getDayOfBirth()).build();
			}
			else {
				return builder().build();
			}
		}
		else if (columnIndex == 2) {
			if (bean.getGender() != null) {
				return builder().setText("" + bean.getGender()).build();
			}
			else {
				return builder().build();
			}
		}
		else if (columnIndex == 2) {
			if (bean.getGender() != null) {
				return builder().setText("" + bean.getGender()).build();
			}
			else {
				return builder().build();
			}
		}
		else if (columnIndex == 3) {
			if (bean.getQuota() != null) {
				return builder().setText("" + bean.getQuota()).build();
			}
			else {
				return builder().build();
			}
		}
		else if (columnIndex == 4) {
			if (bean.getRoles() != null) {
				StringUtils.concatElementsSeparatedByComma(bean.getRoles());
				return builder().setText(StringUtils.concatElementsSeparatedByComma(bean.getRoles())).build();
			}
			else {
				return builder().build();
			}
		}
		return null;
	}

	private static ITableCellBuilder builder() {
		return TableCell.builder().setEditable(true);
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public ITableColumn getColumn(final int columnIndex) {
		if (columnIndex == 0) {
			return DefaultTableColumn.builder("Name").setWidth(150).build();
		}
		else if (columnIndex == 1) {
			return DefaultTableColumn.builder("Day of birth").setWidth(150).build();
		}
		else if (columnIndex == 2) {
			return DefaultTableColumn.builder("Gender").setWidth(150).build();
		}
		else if (columnIndex == 3) {
			return DefaultTableColumn.builder("Quota").setWidth(150).build();
		}
		else if (columnIndex == 4) {
			return DefaultTableColumn.builder("Tags").setWidth(150).build();
		}
		return null;
	}

	@Override
	public ITableColumnModelObservable getTableColumnModelObservable() {
		return null;
	}

}
