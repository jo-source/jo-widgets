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

package org.jowidgets.util.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.jowidgets.util.Assert;
import org.jowidgets.util.IFactory;

public final class DaemonThreadFactory implements ThreadFactory {

    private static final String DEFAULT_SEPARATOR = "-#";
    private static final int DEFAULT_START_INDEX = 1;

    private final IFactory<String> threadNameFactory;
    private final ThreadFactory defaultThreadFactory;

    /**
     * @deprecated Do not use threads without names because they are inexpressive, use {@link #create(IFactory)} or
     *             {@link #create(String) or #create(String, String, int) or #multi(String) or #multi(String, String, int)
     *             instead
     */
    @Deprecated
    public DaemonThreadFactory() {
        this((IFactory<String>) null);
    }

    /**
     * @deprecated This constructor allows that the given threadNameFactory parameter is null which may lead to inexpressive
     *             thread names. Use {@link #create(IFactory)} instead be ensure that the given factory is not null
     */
    @Deprecated
    public DaemonThreadFactory(final IFactory<String> threadNameFactory) {
        this.threadNameFactory = threadNameFactory;
        this.defaultThreadFactory = Executors.defaultThreadFactory();
    }

    /**
     * Creates a new instance with a given thread name prefix.
     * 
     * The separator will set to an empty string so the given threadNamePrefix may already contain an separator or no separator is
     * used. The thread count will start at index=1.
     * 
     * Example:
     * 
     * threadNameOrPrefix: myClassName.fooExecutor-#
     * 
     * Will produce the following thread names:
     * 
     * myClassName.fooExecutor-#1
     * myClassName.fooExecutor-#2
     * ...
     * myClassName.fooExecutor-#n
     *
     * @param threadNamePrefix The thread name prefix to use
     */
    public DaemonThreadFactory(final String threadNamePrefix) {
        this(new DefaultThreadNamesFactory(threadNamePrefix, "", 1, false));
    }

    /**
     * Create a new {@link DaemonThreadFactory} instance.
     * 
     * Each created thread is a daemon thread and will be named with the given threadNameFactory.
     * 
     * @param threadNameFactory The thread name to use, must not be null
     * 
     * @return A new {@link DaemonThreadFactory} instance
     */
    public static ThreadFactory create(final IFactory<String> threadNameFactory) {
        return new DaemonThreadFactory(threadNameFactory);
    }

    /**
     * Create a new {@link DaemonThreadFactory} instance.
     * 
     * Each created thread is a daemon thread.
     * 
     * The first created thread will be named with the given threadNameOrPrefix parameter.
     * 
     * If more than one thread will be created, following threads will be named with the following pattern:
     * 
     * threadNamePrefix-#threadNr
     * 
     * Example:
     * 
     * threadNameOrPrefix: myClassName.fooExecutor
     * 
     * Will produce the following thread names:
     * 
     * myClassName.fooExecutor
     * myClassName.fooExecutor-#2
     * ...
     * myClassName.fooExecutor-#n
     * 
     * 
     * @param threadNameOrPrefix The thread name to use for the first created thread and the prefix to use for the following
     *            thread, must not be null or empty
     * 
     * @param separator The separator to use if more than one thread will be created
     * 
     * @param startIndex The start index for the second created thread
     * 
     * @return A new {@link DaemonThreadFactory} instance
     */
    public static ThreadFactory create(final String threadNameOrPrefix) {
        return create(threadNameOrPrefix, DEFAULT_SEPARATOR, DEFAULT_START_INDEX + 1);
    }

    /**
     * Create a new {@link DaemonThreadFactory} instance.
     * 
     * Each created thread is a daemon thread.
     * 
     * The first created thread will be named with the given threadNameOrPrefix parameter.
     * 
     * If more than one thread will be created, following threads will be named with the following pattern:
     * 
     * threadNamePrefix+separator+threadNr
     * 
     * Example:
     * 
     * threadNameOrPrefix: myClassName.fooExecutor
     * separator = -#
     * startIndex = 2
     * 
     * 
     * Will produce the following thread names:
     * 
     * myClassName.fooExecutor
     * myClassName.fooExecutor-#2
     * ...
     * myClassName.fooExecutor-#n
     * 
     * 
     * @param threadNameOrPrefix The thread name to use for the first created thread and the prefix to use for the following
     *            thread, must not be null or empty
     * 
     * @param separator The separator to use if more than one thread will be created
     * 
     * @param startIndex The start index for the second created thread
     * 
     * @return A new {@link DaemonThreadFactory} instance
     */
    public static ThreadFactory create(final String threadNameOrPrefix, final String separator, final int startIndex) {
        return create(new DefaultThreadNamesFactory(threadNameOrPrefix, separator, startIndex, true));
    }

    /**
     * Create a new {@link DaemonThreadFactory} for multiple threads with the following defaults:
     * 
     * separator = -#
     * startIndex = 1
     * 
     * Use this method if it is sure that more than one thread will be created by the returned factory to ensure all threads
     * have the same naming pattern.
     * 
     * Each created thread is a daemon thread and will be named with the following pattern:
     * 
     * threadNamePrefix-#threadNr
     * 
     * The first created thread gets the threadNr=0 that will be increased after each thread creation, example:
     * 
     * threadNamePrefix: myClassName.fooExecutor
     * 
     * Will produce the following thread names:
     * 
     * myClassName.fooExecutor-#0
     * myClassName.fooExecutor-#1
     * ...
     * myClassName.fooExecutor-#n
     * 
     * 
     * @param threadNamePrefix The thread name prefix to use, must neither be null nor be empty
     * @param separator The separator to use, may be null if no separator should be used
     * 
     * @return A new {@link DaemonThreadFactory} instance
     */
    public static ThreadFactory multi(final String threadNamePrefix) {
        return multi(threadNamePrefix, DEFAULT_SEPARATOR, DEFAULT_START_INDEX);
    }

    /**
     * Create a new {@link DaemonThreadFactory} for multiple threads.
     * 
     * Use this method if it is sure that more than one thread will be created by the returned factory to ensure all threads
     * have the same naming pattern.
     * 
     * Each created thread is a daemon thread and will be named with the following pattern:
     * 
     * threadNamePrefix+separator+threadNr
     * 
     * If separator is null, it will be omitted:
     * 
     * threadNamePrefix+threadNr
     * 
     * The first created thread gets the threadNr=startIndex that will be increased after each thread creation, example:
     * 
     * threadNamePrefix: myClassName.fooExecutor
     * separator: -#
     * startIndex: 1
     * 
     * Will produce the following thread names:
     * 
     * myClassName.fooExecutor-#1
     * myClassName.fooExecutor-#2
     * ...
     * myClassName.fooExecutor-#n
     * 
     * 
     * @param threadNamePrefix The thread name prefix to use, must neither be null nor be empty
     * @param separator The separator to use, may be null if no separator should be used
     * 
     * @return A new {@link DaemonThreadFactory} instance
     */
    public static ThreadFactory multi(final String threadNamePrefix, final String separator, final int startIndex) {
        return create(new DefaultThreadNamesFactory(threadNamePrefix, separator, startIndex, false));
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        final Thread result = defaultThreadFactory.newThread(runnable);
        result.setDaemon(true);
        if (threadNameFactory != null) {
            result.setName(threadNameFactory.create());
        }
        return result;
    }

    private static final class DefaultThreadNamesFactory implements IFactory<String> {

        private final String prefix;
        private final String separator;
        private int count;
        private final boolean omitCountForFirst;

        private final boolean createdAtLeastOne;

        private DefaultThreadNamesFactory(
            final String prefix,
            final String separator,
            final int startIndex,
            final boolean omitCountForFirst) {
            Assert.paramNotEmpty(prefix, "prefix");

            this.prefix = prefix;
            this.separator = separator != null ? separator : "";
            this.count = startIndex;
            this.omitCountForFirst = omitCountForFirst;

            this.createdAtLeastOne = false;
        }

        @Override
        public String create() {
            if (!createdAtLeastOne && omitCountForFirst) {
                return prefix;
            }
            else {
                return prefix + separator + count++;
            }
        }

    }

}
