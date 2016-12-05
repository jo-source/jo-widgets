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

package org.jowidgets.api.layout.tablelayout;

public interface ITableLayoutBuilder {

    enum ColumnMode {
        PREFERRED,
        GROWING,
        HIDDEN,
        FIXED
    };

    enum Alignment {
        LEFT,
        CENTER,
    }

    ITableLayoutBuilder verticalGap(int verticalGap);

    ITableLayoutBuilder layoutMinRows(int layoutMinRows);

    ITableLayoutBuilder columnCount(int columnCount);

    ITableLayoutBuilder widths(int[] widths);

    ITableLayoutBuilder gaps(int[] gaps);

    ITableLayoutBuilder modes(ColumnMode[] modes);

    ITableLayoutBuilder alignments(Alignment[] alignments);

    ITableLayoutBuilder fixedColumnWidth(int column, int width);

    ITableLayoutBuilder columnMode(int column, ColumnMode mode);

    ITableLayoutBuilder columnAlignment(int column, Alignment alignment);

    ITableLayoutBuilder gap(int gap);

    ITableLayoutBuilder gapBeforeColumn(int column, int gap);

    ITableLayoutBuilder gapAfterColumn(int column, int gap);

    ITableLayoutBuilder alignment(int index, Alignment alignment);

    ITableLayout build();

}
