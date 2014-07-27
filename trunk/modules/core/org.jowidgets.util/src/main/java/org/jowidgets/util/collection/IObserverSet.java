/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.util.collection;

/**
 * A set designed to hold observers when implementing the observer pattern
 * 
 * For this use case, the set has the following contract
 * 
 * 1. Observers exists only once (its a set), adding them a second time will not modify the set
 * 
 * This will be done to avoid that listeners will be fired more than once in the case they was added accidently more than once.
 * This also ensures, that after invoking the remove() method the listener no longer remains in the set because it was added more
 * than once before accidently. This protects from heavy to find bugs where listeners was not removed correctly.
 * 
 * 2. Iteration over the set is done in the same order the elements was added
 * 
 * It's reasonable to iterate always in the same order to avoid indeterministic behavior, e.g. to reproduce bugs.
 * 
 * 3. Iteration is always done with a defensive copy, so changing the set while iterating over it is explicitly allowed
 * 
 * The are many cases where it is very practical the remove a listener while it will be invoked, e.g. on dispose or for binding
 * issues
 * 
 * 
 * Implementations have not to be threadsafe. Implementors can assume that all modifications will be done
 * in the same thread and have not have to check the thread explicitly.
 * 
 * @param <OBSERVER_TYPE> The type of the observer hold by this set
 */
public interface IObserverSet<OBSERVER_TYPE> extends Iterable<OBSERVER_TYPE> {

	/**
	 * Adds an observer to the set. If the observer was added already, the set remains unchanged
	 * 
	 * @param observer The observer to add, not null
	 * 
	 * @throws IllegalArgumentException if the observer is null
	 */
	void add(OBSERVER_TYPE observer);

	/**
	 * Removes an observer from the set. If the observer is not a element of the set, the set remains unchanged
	 * 
	 * @param observer The observer to remove, not null
	 * 
	 * @throws IllegalArgumentException if the given observer is null
	 */
	void remove(OBSERVER_TYPE observer);

	/**
	 * Removes all elements from the set an releases the used memory
	 */
	void clear();

}
