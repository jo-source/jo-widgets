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

package org.jowidgets.unit.tools.units;

import org.jowidgets.unit.api.IUnit;
import org.jowidgets.unit.api.IUnitBuilder;
import org.jowidgets.unit.api.IUnitSet;
import org.jowidgets.unit.api.IUnitSetBuilder;
import org.jowidgets.unit.api.Unit;
import org.jowidgets.unit.api.UnitSet;

public final class ByteUnitSet {

    public static final IUnit B = b();
    public static final IUnit KB = kb();
    public static final IUnit MB = mb();
    public static final IUnit GB = gb();
    public static final IUnit TB = tb();

    private static final IUnitSet INSTANCE = createInstance();

    private ByteUnitSet() {}

    private static IUnit b() {
        final IUnitBuilder builder = Unit.builder();
        builder.abbreviation(Messages.getMessage("ByteUnitSet.b_abbreviation"));
        builder.name(Messages.getMessage("ByteUnitSet.b_name"));
        return builder.build();
    }

    private static IUnit kb() {
        final IUnitBuilder builder = Unit.builder();
        builder.abbreviation(Messages.getMessage("ByteUnitSet.kb_abbreviation"));
        builder.name(Messages.getMessage("ByteUnitSet.kb_name"));
        builder.conversionFactor(1024);
        return builder.build();
    }

    private static IUnit mb() {
        final IUnitBuilder builder = Unit.builder();
        builder.abbreviation(Messages.getMessage("ByteUnitSet.mb_abbreviation"));
        builder.name(Messages.getMessage("ByteUnitSet.mb_name"));
        builder.conversionFactor(1024 * 1024);
        return builder.build();
    }

    private static IUnit gb() {
        final IUnitBuilder builder = Unit.builder();
        builder.abbreviation(Messages.getMessage("ByteUnitSet.gb_abbreviation"));
        builder.name(Messages.getMessage("ByteUnitSet.gb_name"));
        builder.conversionFactor(1024 * 1024 * 1024);
        return builder.build();
    }

    private static IUnit tb() {
        final IUnitBuilder builder = Unit.builder();
        builder.abbreviation(Messages.getMessage("ByteUnitSet.tb_abbreviation"));
        builder.name(Messages.getMessage("ByteUnitSet.tb_name"));
        builder.conversionFactor(1024 * 1024 * 1024 * 1024);
        return builder.build();
    }

    private static IUnitSet createInstance() {
        final IUnitSetBuilder builder = UnitSet.builder();
        builder.add(B);
        builder.add(KB);
        builder.add(MB);
        builder.add(GB);
        builder.add(TB);
        return builder.build();
    }

    public static IUnitSet instance() {
        return INSTANCE;
    }

}
