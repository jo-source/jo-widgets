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

package org.jowidgets.impl.base.delegate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class ModelViewIndexConverter<MODEL_TYPE> {

	private final ArrayList<MODEL_TYPE> allModels;
	private final Set<MODEL_TYPE> invisibleModels;
	private final ArrayList<Integer> modelToView;

	public ModelViewIndexConverter() {
		this.allModels = new ArrayList<MODEL_TYPE>();
		this.invisibleModels = new HashSet<MODEL_TYPE>();
		this.modelToView = new ArrayList<Integer>();
	}

	/**
	 * Adds a model
	 * 
	 * @param model
	 * @param visibility
	 * @param modelIndex
	 * @return The view index where object must be added , -1 if model is not visible
	 */
	public int addModel(final MODEL_TYPE model, final boolean visibility, final int modelIndex) {
		if (modelIndex >= 0 && modelIndex <= modelToView.size()) {
			allModels.add(Integer.valueOf(modelIndex), model);
			if (visibility) {
				final int previousVisibleIndex = findPreviousVisibleIndex(modelIndex - 1);
				modelToView.add(modelIndex, previousVisibleIndex + 1);
				increaseFollowingIndices(modelIndex + 1);
				return modelToView.get(modelIndex).intValue();
			}
			else {
				invisibleModels.add(model);
				modelToView.add(modelIndex, Integer.valueOf(-1));
				return -1;
			}
		}
		else {
			throw new IndexOutOfBoundsException("Index must be between '0' and '" + modelToView.size() + "'");
		}
	}

	private int findPreviousVisibleIndex(final int startIndex) {
		if (startIndex != -1) {
			final int fixedStartIndex = Math.min(startIndex, Math.max(0, modelToView.size() - 1));
			for (int index = fixedStartIndex; index >= 0; index--) {
				final int viewIndex = modelToView.get(index).intValue();
				if (viewIndex != -1) {
					return viewIndex;
				}
			}
		}
		return -1;
	}

	private void increaseFollowingIndices(final int startIndex) {
		for (int index = startIndex; index < modelToView.size(); index++) {
			final int viewIndex = modelToView.get(index).intValue();
			if (viewIndex != -1) {
				modelToView.set(index, viewIndex + 1);
			}
		}
	}

	/**
	 * Removes a model
	 * 
	 * @param model
	 * @param visibility
	 * @param modelIndex
	 * @return The view index where object must be removed , -1 if model is not visible
	 */
	public int removeModel(final MODEL_TYPE model, final int modelIndex) {
		if (modelIndex >= 0 && modelIndex < modelToView.size()) {
			final boolean removed = allModels.remove(model);
			if (removed) {
				final int viewIndex = modelToView.get(modelIndex).intValue();
				modelToView.remove(modelIndex);
				if (viewIndex != -1) {
					decreaseFollowingIndices(modelIndex);
				}
				invisibleModels.remove(model);
				return viewIndex;
			}
			else {
				return -1;
			}
		}
		else {
			throw new IndexOutOfBoundsException("Index must be between '0' and '" + (modelToView.size() - 1) + "'");
		}
	}

	private void decreaseFollowingIndices(final int startIndex) {
		for (int index = startIndex; index < modelToView.size(); index++) {
			final int viewIndex = modelToView.get(index).intValue();
			if (viewIndex != -1) {
				modelToView.set(index, viewIndex - 1);
			}
		}
	}

	boolean isMarkedVisible(final MODEL_TYPE model) {
		return !invisibleModels.contains(model);
	}

	/**
	 * Changes the visibility of the model
	 * 
	 * @param model
	 * @param visibility
	 * @return The view index where object must be added / removed from the view, -1 if nothing changed
	 */
	public int markVisibility(final MODEL_TYPE model, final boolean visibility) {
		if (isMarkedVisible(model) != visibility) {
			final int modelIndex = allModels.indexOf(model);
			if (modelIndex != -1) {
				final Integer viewIndex = modelToView.get(modelIndex);
				if (viewIndex == -1 && visibility) {
					invisibleModels.remove(model);
					final int previousVisibleIndex = findPreviousVisibleIndex(modelIndex - 1);
					final int newViewIndex = previousVisibleIndex + 1;
					modelToView.set(modelIndex, newViewIndex);
					increaseFollowingIndices(modelIndex + 1);
					return newViewIndex;
				}
				else if (viewIndex != -1 && !visibility) {
					invisibleModels.add(model);
					modelToView.set(modelIndex, -1);
					decreaseFollowingIndices(modelIndex + 1);
					return viewIndex.intValue();
				}
				else {
					//CHECKSTYLE:OFF
					System.out.println("ModelViewIndexConverter seems to be inconsistent");
					//CHECKSTYLE:ON
				}
			}
		}
		return -1;

	}

	public void clear() {
		allModels.clear();
		invisibleModels.clear();
		modelToView.clear();
	}

	public void dispose() {
		clear();
	}

}
