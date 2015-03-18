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

package org.jowidgets.spi.impl.swt.common.clipboard;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jowidgets.spi.clipboard.ITransferableSpi;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.util.Assert;

final class TransferableSpiAdapter implements ITransferableSpi {

    private final Map<TransferTypeSpi, Object> dataMap;

    TransferableSpiAdapter(final Map<TransferTypeSpi, Object> dataMap) {
        Assert.paramNotNull(dataMap, "dataMap");
        this.dataMap = new LinkedHashMap<TransferTypeSpi, Object>(dataMap);
    }

    @Override
    public Collection<TransferTypeSpi> getSupportedTypes() {
        return dataMap.keySet();
    }

    @Override
    public Object getData(final TransferTypeSpi type) {
        Assert.paramNotNull(type, "type");
        return dataMap.get(type);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataMap == null) ? 0 : dataMap.hashCode());
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
        if (!(obj instanceof TransferableSpiAdapter)) {
            return false;
        }
        final TransferableSpiAdapter other = (TransferableSpiAdapter) obj;
        if (dataMap == null) {
            if (other.dataMap != null) {
                return false;
            }
        }
        else if (!dataMap.equals(other.dataMap)) {
            return false;
        }
        return true;
    }

}
