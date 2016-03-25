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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

import org.jowidgets.classloading.api.SharedClassLoader;
import org.jowidgets.logging.tools.ConsoleLoggerProvider;
import org.jowidgets.logging.tools.DefaultLoggerProvider;
import org.jowidgets.logging.tools.ILoggerFactory;
import org.jowidgets.logging.tools.LoggerFactoryComposite;
import org.jowidgets.logging.tools.LoggerProviderToFactoryAdapter;
import org.jowidgets.util.Assert;

/**
 * This class gets the access to the currently set logger provider
 * and can be used to set a new logger provider.
 */
public final class LoggerProvider {

    static final String DEFAULT_LOGGER_WARNING = "No logging adapter found for "
        + ILoggerProvider.class.getName()
        + ". Using default logging adapter that will log errors and warnings to the console and ignore all other log levels.";

    private static final List<ILoggerProvider> REGISTERED_PROVIDERS = createRegisteredLoggerProviders();
    private static final List<ILoggerProvider> MANUALLY_ADDED_PROVIDERS = new LinkedList<ILoggerProvider>();

    private static ILoggerProvider loggerProvider;

    private LoggerProvider() {}

    /**
     * Sets a new logger provider implementation.
     * 
     * Remark: Use this method with care. This will remove all previously set providers and
     * all java service loader registered providers.
     * 
     * @param factory The logger provider to set, may be null if default should be used
     */
    public static synchronized void setLoggerProvider(final ILoggerProvider provider) {
        LoggerProvider.loggerProvider = null;
        REGISTERED_PROVIDERS.clear();
        MANUALLY_ADDED_PROVIDERS.clear();
        if (provider != null) {
            MANUALLY_ADDED_PROVIDERS.add(provider);
        }
    }

    /**
     * Adds a new logger provider implementation.
     * 
     * Remark: Use this method with care. This will not remove the previously set providers and registered providers
     * 
     * @param factory The logger provider to add, must not be null
     */
    public static synchronized void addLoggerProvider(final ILoggerProvider provider) {
        Assert.paramNotNull(provider, "provider");
        LoggerProvider.loggerProvider = null;
        MANUALLY_ADDED_PROVIDERS.add(provider);
    }

    /**
     * Removes a new logger provider implementation.
     * 
     * Remark: Only the previously added providers can be removed
     * 
     * @param factory The logger provider to remove, must not be null
     * 
     * @return true if the logger provider was removed
     */
    public static synchronized boolean removeLoggerProvider(final ILoggerProvider provider) {
        Assert.paramNotNull(provider, "provider");
        LoggerProvider.loggerProvider = null;
        return MANUALLY_ADDED_PROVIDERS.remove(provider);
    }

    /**
     * Gets the logger for the given name or creates a new one if not already exists
     * 
     * @param name The name to get the logger for
     * 
     * @return The logger for the given name, never null
     */
    public static ILogger get(final String name) {
        return instance().get(name, null);
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
        return instance().get(clazz.getName(), null);
    }

    /**
     * Gets the logger for the given name and callerFQCN or creates a new one if not already exists
     * 
     * @param name The name to get the logger for
     * @param wrapperFQCN The full qualified class name of the wrapper if the created logger will be
     *            used by a logger wrapper. May be null. The wrapper class name will be used to correctly
     *            determine the calling method of the logger log method omitting the wrapper class
     * 
     * @return The logger for the given name, never null
     */
    public static ILogger get(final String name, final String wrapperFQCN) {
        return instance().get(name, wrapperFQCN);
    }

    /**
     * Gets the logger for the given name and callerFQCN or creates a new one if not already exists
     * 
     * @param clazz The class to get the logger for
     * @param wrapperClazz The class of the wrapper if the created logger will be
     *            used by a logger wrapper. The wrapper class name will be used to correctly determine the calling
     *            method of the log methods omitting the wrapper class
     * 
     * @return The logger for the given name, never null
     */
    public static ILogger get(final Class<?> clazz, final Class<?> wrapperClazz) {
        return instance().get(clazz.getName(), wrapperClazz.getName());
    }

    /**
     * Gets the currently set logger provider instance or creates one
     * 
     * @return The logger provider instance, never null
     */
    private static ILoggerProvider instance() {
        if (loggerProvider == null) {
            loggerProvider = createLoggerProvider();
        }
        return loggerProvider;
    }

    private static synchronized ILoggerProvider createLoggerProvider() {
        final List<ILoggerProvider> loggerProviders = getAllLoggerProviders();
        if (loggerProviders.size() > 1) {
            return new DefaultLoggerProvider(new LoggerFactoryComposite(createLoggerFactoriesForProviders(loggerProviders)));
        }
        else {
            return loggerProviders.iterator().next();
        }
    }

    private static List<ILoggerFactory> createLoggerFactoriesForProviders(final List<ILoggerProvider> providers) {
        final List<ILoggerFactory> result = new ArrayList<ILoggerFactory>();
        for (final ILoggerProvider provider : providers) {
            result.add(new LoggerProviderToFactoryAdapter(provider));
        }
        return result;
    }

    private static synchronized List<ILoggerProvider> getAllLoggerProviders() {
        final List<ILoggerProvider> result = new LinkedList<ILoggerProvider>(REGISTERED_PROVIDERS);
        result.addAll(MANUALLY_ADDED_PROVIDERS);

        if (result.isEmpty()) {
            printNoLoggingAdapterFound();
            result.add(ConsoleLoggerProvider.warnLoggerProvider());
        }

        return result;
    }

    private static void printNoLoggingAdapterFound() {
        //CHECKSTYLE:OFF
        System.err.println(DEFAULT_LOGGER_WARNING);
        //CHECKSTYLE:ON
    }

    private static synchronized List<ILoggerProvider> createRegisteredLoggerProviders() {
        final List<ILoggerProvider> result = new LinkedList<ILoggerProvider>();

        final ServiceLoader<ILoggerProvider> loader = ServiceLoader.load(
                ILoggerProvider.class,
                SharedClassLoader.getCompositeClassLoader());
        final Iterator<ILoggerProvider> iterator = loader.iterator();

        while (iterator.hasNext()) {
            result.add(iterator.next());
        }

        return result;
    }

}
