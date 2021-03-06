/*
 * Copyright (c) 2004, Mikael Grev, MiG InfoCom AB. (miglayout (at) miginfocom (dot) com), 
 * modifications by Nikolaus Moll
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * Neither the name of the MiG InfoCom AB nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 */
package org.jowidgets.impl.layout.miglayout;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;

/**
 * A parsed constraint that specifies how an entity (normally column/row or component) can shrink or
 * grow compared to other entities.
 */
final class ResizeConstraintCommon implements Externalizable {
    static final Float WEIGHT_100 = new Float(100);

    /**
     * How flexilble the entity should be, relative to other entities, when it comes to growing. <code>null</code> or
     * zero mean it will never grow. An entity that has twise the growWeight compared to another entity will get twice
     * as much of available space.
     * <p>
     * "grow" are only compared within the same "growPrio".
     */
    //CHECKSTYLE:OFF
    Float grow = null;
    //CHECKSTYLE:ON

    /**
     * The relative priority used for determining which entities gets the extra space first.
     */
    //CHECKSTYLE:OFF
    int growPrio = 100;
    //CHECKSTYLE:ON

    //CHECKSTYLE:OFF
    Float shrink = WEIGHT_100;
    //CHECKSTYLE:ON

    //CHECKSTYLE:OFF
    int shrinkPrio = 100;

    //CHECKSTYLE:ON

    ResizeConstraintCommon() // For Externalizable
    {}

    ResizeConstraintCommon(final int shrinkPrio, final Float shrinkWeight, final int growPrio, final Float growWeight) {
        this.shrinkPrio = shrinkPrio;
        this.shrink = shrinkWeight;
        this.growPrio = growPrio;
        this.grow = growWeight;
    }

    // ************************************************
    // Persistence Delegate and Serializable combined.
    // ************************************************

    private Object readResolve() throws ObjectStreamException {
        return MigLayoutToolkitImpl.getMigLayoutUtil().getSerializedObject(this);
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        final LayoutUtilCommon layoutUtil = MigLayoutToolkitImpl.getMigLayoutUtil();
        layoutUtil.setSerializedObject(this, layoutUtil.readAsXML(in));
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        if (getClass() == ResizeConstraintCommon.class) {
            MigLayoutToolkitImpl.getMigLayoutUtil().writeAsXML(out, this);
        }
    }
}
