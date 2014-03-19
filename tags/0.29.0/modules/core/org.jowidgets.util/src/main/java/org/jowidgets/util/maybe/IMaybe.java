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
package org.jowidgets.util.maybe;

/**
 * An Object of the type TYPE that can be defined, that can be null (undefined)
 * or that can be nothing (not even undefined).
 * This can be used e.g. if a value is not available, e.g. when a user input was aborted
 * 
 * @param <TYPE> the type of the maybe's value
 */
public interface IMaybe<TYPE> {

	/**
	 * @return The value of the maybe
	 */
	TYPE getValue();

	/**
	 * @return true if the maybe is nothing, not even undefined
	 *         Remark: isSomething() == true implies isNothing() == false and vice versa.
	 */
	boolean isNothing();

	/**
	 * @return true, if the maybe is something.
	 *         Remark: isSomething() == true implies isNothing() == false and vice versa.
	 */
	boolean isSomething();

	/**
	 * Gets the value of the maybe, if the maybe is not nothing, else
	 * the default is returned
	 * 
	 * @param defaultValue the default value
	 * @return the value if the maybe is not nothing and else the default
	 */
	TYPE getValueOrElse(TYPE defaultValue);

}
