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

/**
 * The logger facade interface for logging issues.
 * 
 * 
 * General method information:
 * ---------------------------
 * 
 * There are two groups of methods:
 * 
 * I. Retrieve the enablement for each log level (is...Enabled()).
 * 
 * If a log level is not enabled, the invocation of a log method must not have any effect.
 * 
 * 
 * 
 * II. Do some logging (e.g. warn(String message), error(String message, Throwable t), ...)
 * 
 * Invocation of a logging method will route the information to the underlying logging system.
 * 
 * It is not obligatory that after the method returns the logging was completed, but it is allowed
 * to do it synchronously.
 * 
 * Depending on the logging subsystem and the performance claim it may be possible e.g. that the log
 * messages will be queued and processed later to allow to return from the log method immediately.
 * 
 * 
 * LogLevels
 * ---------
 * 
 * There is no general contract when to use which log level, and this often will be discussed religious.
 * 
 * If you have no idea how to use levels, here is my philosophy about this issue.
 * 
 * If you have your own opinion about that, ignore this chapter :-)
 * 
 * I only want to see error messages
 * if anything seriously goes wrong that must be fixed.
 * 
 * I only want to see warn messages
 * if there may be a problem AND there is a chance to fix the problem to avoid the message in future
 * or the system is in an unusual (not normal) state
 * 
 * I only want to see info messages
 * for things they do not happen to often and that are important enough to spam the log.
 * 
 * If i run the application with warn log level and the application runs stable
 * i want to see nothing in the log
 * 
 * If i run the application with info log level and the application runs stable,
 * i want to see in the log that it runs and what it is doing if anything happens one a rough information level
 * 
 * If i try to find a bug,
 * i want to get necessary information in debug level (what ever this is:-)
 * 
 * 
 * javadoc
 * -------
 * 
 * Documentation of each single methods was omitted with purpose to enhance the readability of the interface:-)
 * If it is not clear what the contract of a single method is, please let me know (mail@herr-grossmann.de)
 */
public interface ILogger {

    boolean isErrorEnabled();

    void error(String message);

    void error(Throwable throwable);

    void error(String message, Throwable throwable);

    boolean isWarnEnabled();

    void warn(String message);

    void warn(Throwable throwable);

    void warn(String message, Throwable throwable);

    boolean isInfoEnabled();

    void info(String message);

    void info(Throwable throwable);

    void info(String message, Throwable throwable);

    boolean isDebugEnabled();

    void debug(String message);

    void debug(Throwable throwable);

    void debug(String message, Throwable throwable);

    boolean isTraceEnabled();

    void trace(String message);

    void trace(Throwable throwable);

    void trace(String message, Throwable throwable);

}
