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

package org.jowidgets.spi.impl.controller;

import java.util.Collections;
import java.util.Set;

import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.MouseButton;
import org.jowidgets.common.widgets.controller.ITableCellMouseEvent;

public class TableCellMouseEvent extends TableCellEvent implements ITableCellMouseEvent {

    private final MouseButton mouseButton;
    private final Set<Modifier> modifier;

    public TableCellMouseEvent(
        final int rowIndex,
        final int columnIndex,
        final MouseButton mouseButton,
        final Set<Modifier> modifier) {
        super(rowIndex, columnIndex);
        this.mouseButton = mouseButton;
        this.modifier = Collections.unmodifiableSet(modifier);
    }

    @Override
    public MouseButton getMouseButton() {
        return mouseButton;
    }

    @Override
    public Set<Modifier> getModifiers() {
        return modifier;
    }

    @Override
    public String toString() {
        return "TableCellMouseEvent [mouseButton="
            + mouseButton
            + ", modifier="
            + modifier
            + ", rowIndex="
            + getRowIndex()
            + ", columnIndex="
            + getColumnIndex()
            + "]";
    }

}
