/*
 * Copyright (c) 2010, grossmann, Nikolaus Moll
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

package org.jowidgets.nattable.impl.plugin;

import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.nattable.impl.plugin.layer.NatTableLayers;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.ITableSpiFactory;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;
import org.jowidgets.util.Assert;

public final class NatTableSpiFactory implements ITableSpiFactory {

    private final SwtImageRegistry imageRegistry;

    public NatTableSpiFactory(final SwtImageRegistry imageRegistry) {
        Assert.paramNotNull(imageRegistry, "imageRegistry");
        this.imageRegistry = imageRegistry;
    }

    @Override
    public ITableSpi createTable(
        final IGenericWidgetFactory genericWidgetFactory,
        final Object parentUiReference,
        final ITableSetupSpi setup) {
        final NatTableLayers layers = new NatTableLayers(
            setup.getDataModel(),
            setup.getColumnModel(),
            setup.getColumnsResizeable(),
            setup.getColumnsMoveable(),
            setup.getSelectionPolicy());
        return new NatTableImplSpi(layers, genericWidgetFactory, parentUiReference, setup, imageRegistry);
    }

}
