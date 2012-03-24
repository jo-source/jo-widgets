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

package org.jowidgets.common.types;

public final class Interval<NUMBER_TYPE extends Number> {

	private final NUMBER_TYPE leftBoundary;
	private final boolean leftOpen;

	private final NUMBER_TYPE rightBoundary;
	private final boolean rightOpen;

	private String stringRepresentation;

	public Interval(final NUMBER_TYPE leftBoundary, final NUMBER_TYPE rightBoundary) {
		this(leftBoundary, false, rightBoundary, false);
	}

	public Interval(
		final NUMBER_TYPE leftBoundary,
		final boolean leftOpen,
		final NUMBER_TYPE rightBoundary,
		final boolean rightOpen) {

		this.leftBoundary = leftBoundary;
		this.leftOpen = leftOpen;
		this.rightBoundary = rightBoundary;
		this.rightOpen = rightOpen;
	}

	public NUMBER_TYPE getLeftBoundary() {
		return leftBoundary;
	}

	public boolean isLeftOpen() {
		return leftOpen;
	}

	public NUMBER_TYPE getRightBoundary() {
		return rightBoundary;
	}

	public boolean isRightOpen() {
		return rightOpen;
	}

	@Override
	public String toString() {
		if (stringRepresentation == null) {
			final StringBuilder builder = new StringBuilder();
			if (leftOpen) {
				builder.append("(");
			}
			else {
				builder.append("[");
			}
			if (leftBoundary != null) {
				builder.append(leftBoundary);
			}
			else {
				builder.append("UNDEFINED");
			}
			builder.append(",");
			if (rightBoundary != null) {
				builder.append(rightBoundary);
			}
			else {
				builder.append("UNDEFINED");
			}
			if (rightOpen) {
				builder.append(")");
			}
			else {
				builder.append("]");
			}
			stringRepresentation = builder.toString();
		}

		return stringRepresentation;
	}

}
