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

package org.jowidgets.api.clipboard;

import java.io.Serializable;

import org.jowidgets.util.Assert;

public final class TransferType<JAVA_TYPE> implements Serializable {

    public static final TransferType<String> STRING_TYPE = new TransferType<String>(String.class);

    public static final TransferType<UnknownTransferTypeClass> UNKOWN_TYPE = new TransferType<UnknownTransferTypeClass>(
        UnknownTransferTypeClass.class);

    private static final long serialVersionUID = 3536962082573394080L;

    private final Class<JAVA_TYPE> javaType;
    private final String className;

    public TransferType(final Class<JAVA_TYPE> javaType) {
        Assert.paramNotNull(javaType, "javaType");
        this.javaType = javaType;
        this.className = javaType.getName();
    }

    public Class<JAVA_TYPE> getJavaType() {
        return javaType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((className == null) ? 0 : className.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TransferType)) {
            return false;
        }
        @SuppressWarnings({"unchecked", "rawtypes"})
        final TransferType<JAVA_TYPE> other = (TransferType) obj;
        return className.equals(other.getJavaType().getName());
    }

    @Override
    public String toString() {
        return "TransferType [className=" + className + "]";
    }

    private static final class UnknownTransferTypeClass implements Serializable {
        private static final long serialVersionUID = -5806128937509663454L;
    }

}
