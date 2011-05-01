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

package org.jowidgets.common.mask;

public interface ICharacterMask {

	/**
	 * Determines if the user could do some input for this character mask.
	 * 
	 * If false will be returned, the {@link ICharacterMask#getPlaceholder()} method
	 * must not return null.
	 * 
	 * @return True if the user could input some character, false otherwise
	 */
	boolean isReadonly();

	/**
	 * Gets a regular expression that describes the valid character's for this mask.
	 * If the result is null, all character's are valid.
	 * 
	 * Remark: The accepting regExp will be applied before the rejecting regExp
	 * 
	 * @return A regular expression or null if all character's are valid
	 */
	String getAcceptingRegExp();

	/**
	 * Gets a regular expression that describes the invalid character's for this mask.
	 * If the result is null, no character's are invalid.
	 * 
	 * Remark: The accepting regExp will be applied before the rejecting regExp
	 * 
	 * @return A regular expression or null if no character's are invalid
	 */
	String getRejectingRegExp();

	/**
	 * Gets the placeholder character for this mask. The placeholder would be shown, if
	 * no character input has already been occurred.
	 * 
	 * @return The placeholder or null if no placeholder is defined
	 */
	Character getPlaceholder();

}
