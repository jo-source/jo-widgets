/*
 * Copyright (c) 2012, David Bauknecht
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

package org.jowidgets.spi.impl.javafx.widgets.base;

import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.types.MouseButton;
import org.jowidgets.common.widgets.controller.ITableCellMouseEvent;
import org.jowidgets.spi.impl.controller.TableCellMouseEvent;
import org.jowidgets.spi.impl.controller.TableCellObservable;
import org.jowidgets.spi.impl.javafx.util.MouseUtil;

public class JoCell extends TableCell<ITableCell, Object> {

	private TextField textField;
	private int index;
	private final TableCellObservable tableCellObservable;

	public JoCell(final TableCellObservable tableCellObservable) {
		this.tableCellObservable = tableCellObservable;
		addEventFilter(MouseEvent.ANY, new TableCellHandler());
	}

	@Override
	public void startEdit() {
		super.startEdit();
		if (getCell(getItem()).isEditable()) {

			if (isEmpty()) {
				return;
			}

			if (textField == null) {
				createTextField();
			}
			else {
				textField.setText(getCell(getItem()).getText());
			}

			setGraphic(textField);
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			textField.requestFocus();
		}
	}

	@Override
	public void updateItem(final Object item, final boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {

			if (!isEmpty()) {
				if (textField != null) {
					textField.setText(getCell(item).getText());
				}

				setText(getCell(item).getText());
			}
		}

	}

	private void createTextField() {
		textField = new TextField(getCell(getItem()).getText());
		textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		textField.setMinHeight(this.getHeight());
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(final KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					commitEdit(getItem());
					setContentDisplay(ContentDisplay.TEXT_ONLY);
				}
			}
		});
	}

	private ITableCell getCell(final Object item) {
		if (item instanceof JoTableRow) {
			final JoTableRow tablerow = (JoTableRow) item;
			index = getTableView().getColumns().indexOf(getTableColumn());
			final ITableCell cell = tablerow.getCell(index);
			return cell;
		}
		return null;
	}

	class TableCellHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(final MouseEvent event) {
			if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
				final ITableCellMouseEvent mouseEvent = getMouseEvent(event, 2);
				tableCellObservable.fireMouseDoubleClicked(mouseEvent);
			}
			else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				final ITableCellMouseEvent mouseEvent = getMouseEvent(event, 1);
				if (mouseEvent != null) {
					tableCellObservable.fireMousePressed(mouseEvent);
				}
			}
			else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
				final ITableCellMouseEvent mouseEvent = getMouseEvent(event, 1);
				if (mouseEvent != null) {
					tableCellObservable.fireMouseReleased(mouseEvent);
				}
			}
		}

		private ITableCellMouseEvent getMouseEvent(final MouseEvent event, final int clickCount) {
			if (event.getClickCount() != clickCount) {
				return null;
			}

			final MouseButton mouseButton = MouseUtil.getMouseButton(event);
			if (mouseButton == null) {
				return null;
			}
			final JoCell cell = (JoCell) event.getSource();
			return new TableCellMouseEvent(
				cell.getIndex(),
				cell.getTableView().getColumns().indexOf(cell.getTableColumn()),
				mouseButton,
				MouseUtil.getModifier(event));
		}
	}
}
