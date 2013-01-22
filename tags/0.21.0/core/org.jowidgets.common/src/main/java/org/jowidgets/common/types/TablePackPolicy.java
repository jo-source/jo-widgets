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

package org.jowidgets.common.types;

public enum TablePackPolicy {

	/** Consider only the table header */
	HEADER(true, false, false),

	/** Consider all data, can be very expensive for virtual tables */
	DATA_ALL(false, true, true),

	/** Consider only the visible data (inside the visible viewport) */
	DATA_VISIBLE(false, true, false),

	/** Consider all data and the header, can be very expensive for virtual tables */
	HEADER_AND_DATA_ALL(true, true, true),

	/** Consider the header and the visible data (inside the visible viewport) */
	HEADER_AND_DATA_VISIBLE(true, true, false);

	private final boolean considerHeader;
	private final boolean considerData;
	private final boolean considerAllData;

	private TablePackPolicy(final boolean considerHeader, final boolean considerData, final boolean considerAllData) {
		this.considerHeader = considerHeader;
		this.considerData = considerData;
		this.considerAllData = considerAllData;
	}

	public boolean considerData() {
		return considerData;
	}

	public boolean considerHeader() {
		return considerHeader;
	}

	public boolean considerAllData() {
		return considerAllData;
	}

}
