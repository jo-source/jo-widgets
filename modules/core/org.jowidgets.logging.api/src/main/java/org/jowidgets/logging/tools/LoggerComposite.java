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

package org.jowidgets.logging.tools;

import java.util.ArrayList;
import java.util.Collection;

import org.jowidgets.logging.api.ILogger;
import org.jowidgets.util.Assert;

/**
 * Composite pattern implementation for ILogger.
 * 
 * When retrieving enabled state for log level, true will be returned if at least one logger is enabled for this state
 * 
 * When invoking a log method on this composite, the method will be invoked on all given loggers where this
 * log mode is enabled.
 */
public final class LoggerComposite implements ILogger {

    private final Collection<ILogger> loggers;

    /**
     * Creates a new instance for given loggers
     * 
     * @param loggers The loggers to use, must not be null
     */
    public LoggerComposite(final Collection<ILogger> loggers) {
        Assert.paramNotNull(loggers, "loggers");
        this.loggers = new ArrayList<ILogger>(loggers);
    }

    /**
     * @return True, is at least for one logger trace is enabled
     */
    @Override
    public boolean isTraceEnabled() {
        for (final ILogger logger : loggers) {
            if (logger.isTraceEnabled()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return True, is at least for one logger debug is enabled
     */
    @Override
    public boolean isDebugEnabled() {
        for (final ILogger logger : loggers) {
            if (logger.isDebugEnabled()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return True, is at least for one logger info is enabled
     */
    @Override
    public boolean isInfoEnabled() {
        for (final ILogger logger : loggers) {
            if (logger.isInfoEnabled()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return True, is at least for one logger warn is enabled
     */
    @Override
    public boolean isWarnEnabled() {
        for (final ILogger logger : loggers) {
            if (logger.isWarnEnabled()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return True, is at least for one logger error is enabled
     */
    @Override
    public boolean isErrorEnabled() {
        for (final ILogger logger : loggers) {
            if (logger.isErrorEnabled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void error(final String message) {
        for (final ILogger logger : loggers) {
            if (logger.isErrorEnabled()) {
                logger.error(message);
            }
        }
    }

    @Override
    public void warn(final String message) {
        for (final ILogger logger : loggers) {
            if (logger.isWarnEnabled()) {
                logger.warn(message);
            }
        }
    }

    @Override
    public void info(final String message) {
        for (final ILogger logger : loggers) {
            if (logger.isInfoEnabled()) {
                logger.info(message);
            }
        }
    }

    @Override
    public void debug(final String message) {
        for (final ILogger logger : loggers) {
            if (logger.isDebugEnabled()) {
                logger.debug(message);
            }
        }
    }

    @Override
    public void trace(final String message) {
        for (final ILogger logger : loggers) {
            if (logger.isTraceEnabled()) {
                logger.trace(message);
            }
        }
    }

    @Override
    public void error(final Throwable throwable) {
        for (final ILogger logger : loggers) {
            if (logger.isErrorEnabled()) {
                logger.error(throwable);
            }
        }
    }

    @Override
    public void warn(final Throwable throwable) {
        for (final ILogger logger : loggers) {
            if (logger.isWarnEnabled()) {
                logger.warn(throwable);
            }
        }
    }

    @Override
    public void info(final Throwable throwable) {
        for (final ILogger logger : loggers) {
            if (logger.isInfoEnabled()) {
                logger.info(throwable);
            }
        }
    }

    @Override
    public void debug(final Throwable throwable) {
        for (final ILogger logger : loggers) {
            if (logger.isDebugEnabled()) {
                logger.debug(throwable);
            }
        }
    }

    @Override
    public void trace(final Throwable throwable) {
        for (final ILogger logger : loggers) {
            if (logger.isTraceEnabled()) {
                logger.trace(throwable);
            }
        }
    }

    @Override
    public void error(final String message, final Throwable throwable) {
        for (final ILogger logger : loggers) {
            if (logger.isErrorEnabled()) {
                logger.error(message, throwable);
            }
        }
    }

    @Override
    public void warn(final String message, final Throwable throwable) {
        for (final ILogger logger : loggers) {
            if (logger.isWarnEnabled()) {
                logger.warn(message, throwable);
            }
        }
    }

    @Override
    public void info(final String message, final Throwable throwable) {
        for (final ILogger logger : loggers) {
            if (logger.isInfoEnabled()) {
                logger.info(message, throwable);
            }
        }
    }

    @Override
    public void debug(final String message, final Throwable throwable) {
        for (final ILogger logger : loggers) {
            if (logger.isDebugEnabled()) {
                logger.debug(message, throwable);
            }
        }
    }

    @Override
    public void trace(final String message, final Throwable throwable) {
        for (final ILogger logger : loggers) {
            if (logger.isTraceEnabled()) {
                logger.trace(message, throwable);
            }
        }
    }

}
