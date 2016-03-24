/*
 * Copyright (c) 2016, grossmann
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

import org.jowidgets.logging.tools.DefaultLoggerProvider;
import org.jowidgets.logging.tools.ILoggerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DefaultLoggerProviderTest {

    private ILoggerFactory loggerFactory;

    @Before
    public void setUp() {
        loggerFactory = Mockito.mock(ILoggerFactory.class);
        Mockito.when(loggerFactory.create(Mockito.anyString())).thenReturn(Mockito.mock(ILogger.class));
        LoggerProvider.setLoggerProvider(new DefaultLoggerProvider(loggerFactory));
    }

    @After
    public void tearDown() {
        LoggerProvider.setLoggerProvider(null);
    }

    @Test
    public void testLazyLoggerCreation() {
        final String loggerName1 = "LOGGER_1";
        final String loggerName2 = "LOGGER_2";
        final String loggerName3 = "LOGGER_3";

        LoggerProvider.get(loggerName1);
        LoggerProvider.get(loggerName1);
        LoggerProvider.get(loggerName1);

        LoggerProvider.get(loggerName2);
        LoggerProvider.get(loggerName2);
        LoggerProvider.get(loggerName2);

        LoggerProvider.get(loggerName3);
        LoggerProvider.get(loggerName3);
        LoggerProvider.get(loggerName3);

        Mockito.verify(loggerFactory, Mockito.times(1)).create(loggerName1);
        Mockito.verify(loggerFactory, Mockito.times(1)).create(loggerName2);
        Mockito.verify(loggerFactory, Mockito.times(1)).create(loggerName3);
    }

}
