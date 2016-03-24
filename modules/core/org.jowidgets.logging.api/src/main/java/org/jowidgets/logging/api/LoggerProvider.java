/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.logging.api;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.jowidgets.classloading.api.SharedClassLoader;
import org.jowidgets.util.Assert;

public final class LoggerProvider {

    private static ILoggerProvider provider;

    private LoggerProvider() {}

    /**
     * Sets a new logger provider implementation
     * 
     * @param factory The logger provider to set, must not be null
     */
    public static synchronized void setLoggerProvider(final ILoggerProvider provider) {
        Assert.paramNotNull(provider, "provider");
        LoggerProvider.provider = provider;
    }

    /**
     * Gets the logger with the given name or crates one if not exists
     * 
     * @param name The name to get the logger for
     * 
     * @return The logger for the given name, never null
     */
    public static ILogger get(final String name) {
        return instance().get(name);
    }

    /**
     * Gets the logger with the given class or crates one if not exists
     * 
     * @param clazz The class to get the logger for
     * 
     * @return The logger for the given class, never null
     */
    public static ILogger get(final Class<?> clazz) {
        Assert.paramNotNull(clazz, "clazz");
        return instance().get(clazz.getName());
    }

    /**
     * Gets the logger provider instance.
     * 
     * @return The logger provider instance, never null
     */
    public static ILoggerProvider instance() {
        if (provider == null) {
            provider = createLoggerProvider();
        }
        return provider;
    }

    private static synchronized ILoggerProvider createLoggerProvider() {
        ILoggerProvider result;

        final ServiceLoader<ILoggerProvider> loader = ServiceLoader.load(
                ILoggerProvider.class,
                SharedClassLoader.getCompositeClassLoader());
        final Iterator<ILoggerProvider> iterator = loader.iterator();

        if (!iterator.hasNext()) {
            System.err.println("No logging adapter found for "
                + ILoggerProvider.class.getName()
                + ". Using default logging adapter.");
            result = new DefaultLoggerProvider();
        }
        else {
            result = iterator.next();
            while (iterator.hasNext()) {
                final String msg = "Another logging adapter '"
                    + iterator.next().getClass().getName()
                    + "' found for '"
                    + ILoggerProvider.class.getName()
                    + "'. The adapter will be ignored and the adapter '"
                    + result.getClass()
                    + "' will be used.";
                //CHECKSTYLE:OFF
                System.err.println(msg);
                //CHECKSTYLE:ON
                result.get(LoggerProvider.class.getName()).error(msg);
            }
        }
        return result;
    }

}
