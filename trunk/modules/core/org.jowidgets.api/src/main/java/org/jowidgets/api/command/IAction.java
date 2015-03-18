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

package org.jowidgets.api.command;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;

public interface IAction {

    /**
     * Gets the action label text
     * 
     * @return The actions label text, may be null or empty
     */
    String getText();

    /**
     * Gets the actions tooltip text
     * 
     * @return The actions tooltip text, may be null or empty
     */
    String getToolTipText();

    /**
     * Gets the action icon
     * 
     * @return The actions icon, may be null
     */
    IImageConstant getIcon();

    /**
     * Gets the action mnemonic
     * 
     * @return The mnemonic key, may be null
     */
    Character getMnemonic();

    /**
     * Gets the actions accelerator
     * 
     * @return The accelerator, may be null
     */
    Accelerator getAccelerator();

    /**
     * Gets the enabled state of the action.
     * 
     * A action that is not enabled can not be executed
     * 
     * @return True if the action is enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Executes the action
     * 
     * @param executionContext The execution context
     * 
     * @throws Exception
     */
    void execute(IExecutionContext executionContext) throws Exception;

    /**
     * Gets the ExceptionHandler of this action. If no ExceptionHandler is defined,
     * a default exception handler will be used.
     * 
     * @return exceptionHandler The exception handler or null, if no handler is defined
     */
    IExceptionHandler getExceptionHandler();

    /**
     * For mutable actions, an observable can be implemented to recognize changes
     * on this action.
     * 
     * The following properties can be mutable: text, toolTipText, icon, enabled
     * 
     * @return The observable if the action is mutable or null if the action is immutable
     */
    IActionChangeObservable getActionChangeObservable();

}
