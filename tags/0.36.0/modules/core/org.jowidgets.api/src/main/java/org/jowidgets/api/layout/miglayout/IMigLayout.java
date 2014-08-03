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

package org.jowidgets.api.layout.miglayout;

import java.util.Map;

import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.widgets.layout.ILayouter;

public interface IMigLayout extends ILayouter {

	/**
	 * Sets the layout constraints for the layout manager instance as a String.
	 * <p>
	 * See the class JavaDocs for information on how this string is formatted.
	 * 
	 * @param constraints The layout constraints as a String representation. <code>null</code> is converted to <code>""</code> for
	 *            storage.
	 * @throws RuntimeException if the constaint was not valid.
	 */
	void setLayoutConstraints(Object constraints);

	/**
	 * Returns layout constraints eighter as a <code>String</code> or {@link net.miginfocom.layout.LC} depending what was sent in
	 * to the constructor or set with {@link #setLayoutConstraints(Object)}.
	 * 
	 * @return The layout constraints eighter as a <code>String</code> or {@link net.miginfocom.layout.LC} depending what was sent
	 *         in
	 *         to the constructor or set with {@link #setLayoutConstraints(Object)}. Never <code>null</code>.
	 */
	Object getLayoutConstraints();

	/**
	 * Sets the column layout constraints for the layout manager instance as a String.
	 * <p>
	 * See the class JavaDocs for information on how this string is formatted.
	 * 
	 * @param constraints The column layout constraints as a String representation. <code>null</code> is converted to
	 *            <code>""</code> for storage.
	 * @throws RuntimeException if the constaint was not valid.
	 */
	void setColumnConstraints(Object constraints);

	/**
	 * Returns the column layout constraints either as a <code>String</code> or {@link net.miginfocom.layout.AC}.
	 * 
	 * @return The column constraints eighter as a <code>String</code> or {@link net.miginfocom.layout.LC} depending what was sent
	 *         in
	 *         to the constructor or set with {@link #setLayoutConstraints(Object)}. Never <code>null</code>.
	 */
	Object getColumnConstraints();

	/**
	 * Sets the row layout constraints for the layout manager instance as a String.
	 * <p>
	 * See the class JavaDocs for information on how this string is formatted.
	 * 
	 * @param constraints The row layout constraints as a String representation. <code>null</code> is converted to <code>""</code>
	 *            for
	 *            storage.
	 * @throws RuntimeException if the constaint was not valid.
	 */
	void setRowConstraints(Object constraints);

	/**
	 * Returns the row layout constraints as a String representation. This string is the exact string as set with
	 * {@link #setRowConstraints(Object)} or sent into the constructor.
	 * <p>
	 * See the class JavaDocs for information on how this string is formatted.
	 * 
	 * @return The row layout constraints as a String representation. Never <code>null</code>.
	 */
	Object getRowConstraints();

	/**
	 * Sets the constraints map.
	 * 
	 * @param map The map. Will be copied.
	 */
	void setConstraintMap(Map<IControl, Object> map);

	/**
	 * Returns a shallow copy of the constraints map.
	 * 
	 * @return A shallow copy of the constraints map. Never <code>null</code>.
	 */
	Map<IControl, Object> getConstraintMap();

	/**
	 * Returns if this layout manager is currently managing this component.
	 * 
	 * @param control The component to check. If <code>null</code> then <code>false</code> will be returned.
	 * @return If this layout manager is currently managing this component.
	 */
	boolean isManagingComponent(IControl control);

	// TODO NM implement to allow layout callbacks
	///**
	// * Adds the callback function that will be called at different stages of the layout cylce.
	// * 
	// * @param callback The callback. Not <code>null</code>.
	// */
	//void addLayoutCallback(LayoutCallback callback);
	//
	///**
	// * Removes the callback if it exists.
	// * 
	// * @param callback The callback. May be <code>null</code>.
	// */
	//void removeLayoutCallback(LayoutCallback callback);

}
