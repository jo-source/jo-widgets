/*
 * Copyright (c) 2018, herrg
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

package org.jowidgets.logging.tools;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class JUnitLoggerTest {

    private JUnitLogger logger;

    @Before
    public void setUp() {
        JUnitLoggerProvider.reset();
        logger = JUnitLoggerProvider.getGlobalLogger();
    }

    @After
    public void tearDown() {
        JUnitLoggerProvider.reset();
    }

    @Test
    public void testLogMessage() {
        final String logMessage = "FooMessage";

        logger.trace(logMessage + 1);
        Assert.assertEquals(1, logger.getMessageCount());

        logger.debug(logMessage + 2);
        Assert.assertEquals(2, logger.getMessageCount());

        logger.info(logMessage + 3);
        Assert.assertEquals(3, logger.getMessageCount());

        logger.warn(logMessage + 4);
        Assert.assertEquals(4, logger.getMessageCount());

        logger.error(logMessage + 5);
        Assert.assertEquals(5, logger.getMessageCount());

        logger.reset();
        Assert.assertEquals(0, logger.getMessageCount());
    }

    @Test
    public void testLogLevels() {
        final String logMessage = "FooMessage";

        logger.setTraceEnabled(false);
        logger.trace(logMessage + 1);
        Assert.assertEquals(0, logger.getMessageCount());
        logger.setTraceEnabled(true);
        logger.trace(logMessage + 2);
        Assert.assertEquals(1, logger.getMessageCount());

        logger.reset();
        logger.setDebugEnabled(false);
        logger.debug(logMessage + 3);
        Assert.assertEquals(0, logger.getMessageCount());
        logger.setDebugEnabled(true);
        logger.debug(logMessage + 4);
        Assert.assertEquals(1, logger.getMessageCount());

        logger.reset();
        logger.setInfoEnabled(false);
        logger.info(logMessage + 5);
        Assert.assertEquals(0, logger.getMessageCount());
        logger.setInfoEnabled(true);
        logger.info(logMessage + 6);
        Assert.assertEquals(1, logger.getMessageCount());

        logger.reset();
        logger.setWarnEnabled(false);
        logger.warn(logMessage + 7);
        Assert.assertEquals(0, logger.getMessageCount());
        logger.setWarnEnabled(true);
        logger.warn(logMessage + 8);
        Assert.assertEquals(1, logger.getMessageCount());

        logger.reset();
        logger.setErrorEnabled(false);
        logger.error(logMessage + 7);
        Assert.assertEquals(0, logger.getMessageCount());
        logger.setErrorEnabled(true);
        logger.error(logMessage + 8);
        Assert.assertEquals(1, logger.getMessageCount());
    }

}
