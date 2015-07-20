/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.impl.toolkit;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.jowidgets.api.toolkit.IToolkit;
import org.jowidgets.api.toolkit.IToolkitProvider;
import org.jowidgets.classloading.api.SharedClassLoader;
import org.jowidgets.spi.IWidgetsServiceProvider;

public final class DefaultToolkitProvider implements IToolkitProvider {

    private final IToolkit toolkit;

    /**
     * Creates a new Instance that uses the injected IWidgetsServiceProvider
     */
    public DefaultToolkitProvider() {
        this(true);
    }

    /**
     * Creates a new Instance that uses the injected IWidgetsServiceProvider
     * 
     * @param doToolkitInterception If true, the injected toolkit interceptors (IToolkitIterceptor)
     *            will be invoked for the created toolkit
     */
    public DefaultToolkitProvider(final boolean doToolkitInterception) {
        final ServiceLoader<IWidgetsServiceProvider> widgetServiceLoader = ServiceLoader.load(
                IWidgetsServiceProvider.class,
                SharedClassLoader.getCompositeClassLoader());
        final Iterator<IWidgetsServiceProvider> iterator = widgetServiceLoader.iterator();

        if (iterator.hasNext()) {
            this.toolkit = new DefaultToolkit(iterator.next(), doToolkitInterception);

            if (iterator.hasNext()) {
                throw new IllegalStateException("More than one implementation found for '"
                    + IWidgetsServiceProvider.class.getName()
                    + "'");
            }
        }
        else {
            throw new IllegalStateException("No implementation found for '" + IWidgetsServiceProvider.class.getName() + "'");
        }
    }

    /**
     * Creates a new DefaultToolkitProvider
     * 
     * @param toolkitSpi The IWidgetsServiceProvider (SPI) to use
     */
    public DefaultToolkitProvider(final IWidgetsServiceProvider toolkitSpi) {
        this.toolkit = new DefaultToolkit(toolkitSpi, true);
    }

    /**
     * Creates a new DefaultToolkitProvider
     * 
     * @param toolkitSpi The IWidgetsServiceProvider (SPI) to use
     * @param doToolkitInterception If true, the injected toolkit interceptors (IToolkitIterceptor)
     *            will be invoked for the created toolkit
     */
    public DefaultToolkitProvider(final IWidgetsServiceProvider toolkitSpi, final boolean doToolkitInterception) {
        this.toolkit = new DefaultToolkit(toolkitSpi, doToolkitInterception);
    }

    @Override
    public IToolkit get() {
        return toolkit;
    }

}
