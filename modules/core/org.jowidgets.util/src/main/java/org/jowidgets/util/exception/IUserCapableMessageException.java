/*
 * Copyright (c) 2018, grossmann
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

package org.jowidgets.util.exception;

/**
 * Implementing exceptions provide a user capable exception message that can be presented directly to the end user, e.g. via the
 * GUI in contrast to the method {@link Exception#getMessage()} that often has a detailed technical part that may e.g. be useful
 * for debugging or analysis within logfiles but may overcharge user with technical terms beyond it's domain.
 * 
 * This often leads to the conflict to write message that not overcharge the end user with technical information on the one hand,
 * and on the other hand not hide import information to administrators in error cases.
 * 
 * This interface should help to distinct these two different aspects of exception messages.
 * 
 * By the way, the lack of the method {@link Exception#getLocalizedMessage()} is, that the default implementation will return the
 * result of {@link Exception#getMessage()} which often is not suitable to provide to end user, and in case that both messages are
 * equal the message may be user capable anyhow because locale of user is English and message has no technical terms. Because of
 * this, only user messages must be returned if they are capable to provide to end user, in any other cases null should be
 * returned.
 */
public interface IUserCapableMessageException {

    /**
     * Gets the user message of the exception.
     * 
     * The user message is a message that could be presented directly to the end user.
     * 
     * User messages should normally not presume technical / programming background
     * from the user. Instead they should describe the problem (and possible solution)
     * within domain language the application is designed for and that can be understand by user not being it expert
     * (Expect the application is designed for IP experts like IDE's e.g.).
     * 
     * The user message should be localized and formatted so it can be directly presented via
     * GUI to the user.
     * 
     * If no user message is available, this method must not return the {@link Exception#getMessage()} result but null instead.
     * 
     * @return The user message or null, if no user message was set
     */
    String getUserMessage();

}
