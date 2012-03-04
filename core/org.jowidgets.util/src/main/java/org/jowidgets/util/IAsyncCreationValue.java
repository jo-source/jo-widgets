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

package org.jowidgets.util;

/**
 * A value of a specific type that will be created later.
 * 
 * @param <VALUE_TYPE> The type of the value that will be created later
 */
public interface IAsyncCreationValue<VALUE_TYPE> {

	/**
	 * Adds a callback that will be invoked in the following manner:
	 * 
	 * 1. If the value is already created, the given callback will be invoked immediately
	 * 2. If the value was not already created, the given callback will be invoked later
	 * but immediately after the value was created
	 * 
	 * @param callback The callback that gets the created value
	 */
	void addCreationCallback(IAsyncCreationCallback<VALUE_TYPE> callback);

	/**
	 * Removes a creation callback if no longer interested on the value.
	 * 
	 * @param callback The callback to remove
	 */
	void removeCreationCallback(IAsyncCreationCallback<VALUE_TYPE> callback);

	/**
	 * @return True if the value was already created, false otherwise
	 */
	boolean isCreated();

}
