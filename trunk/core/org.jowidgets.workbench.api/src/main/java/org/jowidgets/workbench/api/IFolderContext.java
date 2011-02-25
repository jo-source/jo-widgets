/*
 * Copyright (c) 2011, M. Grossmann, H. Westphal
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

package org.jowidgets.workbench.api;

import org.jowidgets.api.model.item.IMenuModel;

public interface IFolderContext {

	/**
	 * @return The id of the corresponding folder
	 */
	String getFolderId();

	/**
	 * Gets the id of the original folder in the layout, if this folder was cloned.
	 * If this folder is not a clone, the id is returned.
	 * 
	 * @return The id of the original folder for clones or the id of this folder, if folder is not a clone.
	 */
	String getOriginalFolderId();

	void addView(IViewLayout viewLayout);

	void addView(boolean addToFront, IViewLayout viewLayout);

	void removeView(IView remove);

	/**
	 * Gets the folders popop menu model.
	 * If no popup menu model already exists for the folder, a popup menu model will be created
	 * 
	 * @return the popup menu model for a folder or null if the folder is not known
	 */
	IMenuModel getPopupMenu();

	IComponentContext getComponentContext();

	IComponentTreeNodeContext getComponentTreeNodeContext();

	IWorkbenchApplicationContext getWorkbenchApplicationContext();

	IWorkbenchContext getWorkbenchContext();

}
