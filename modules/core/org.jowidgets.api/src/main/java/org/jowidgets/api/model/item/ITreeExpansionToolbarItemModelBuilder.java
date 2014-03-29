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

package org.jowidgets.api.model.item;

import org.jowidgets.api.command.ITreeExpansionAction;
import org.jowidgets.api.widgets.ITreeContainer;

public interface ITreeExpansionToolbarItemModelBuilder {

	/**
	 * Sets the expand and collapse action by creating them from a ITreeConatainer
	 * 
	 * @param tree The tree to create the expand and collapse actions from
	 * 
	 * @return This builder
	 */
	ITreeExpansionToolbarItemModelBuilder setActions(ITreeContainer tree);

	/**
	 * Sets the collapse action that should be used
	 * 
	 * @param expandAction The collapse action to set
	 * 
	 * @return This builder
	 */
	ITreeExpansionToolbarItemModelBuilder setCollapseAction(ITreeExpansionAction collapseAction);

	/**
	 * Sets the expand action that should be used
	 * 
	 * @param expandAction The expand action to set
	 * 
	 * @return This builder
	 */
	ITreeExpansionToolbarItemModelBuilder setExpandAction(ITreeExpansionAction expandAction);

	/**
	 * Adds an unbound level with a default label
	 * 
	 * @return This builder
	 */
	ITreeExpansionToolbarItemModelBuilder addUnboundLevel();

	/**
	 * Adds an unbound level
	 * 
	 * @param label The label of the unbound level
	 * 
	 * @return This builder
	 */
	ITreeExpansionToolbarItemModelBuilder addUnboundLevel(String label);

	/**
	 * Adds a level, a default label will be created
	 * 
	 * @param level The level to add
	 * 
	 * @return This builder
	 */
	ITreeExpansionToolbarItemModelBuilder addLevel(int level);

	/**
	 * Adds a level with a corresponding level name
	 * 
	 * @param level The level to add
	 * @param levelName The name of the level
	 * 
	 * @return This builder
	 */
	ITreeExpansionToolbarItemModelBuilder addLevel(int level, String levelName);

	/**
	 * Sets a range of levels with default labels for each level
	 * 
	 * @param minLevel The minimum level
	 * @param maxLevel The maximum level
	 * @param unboundPossible If true, a unbound expansion can be set
	 * 
	 * @return This builder
	 * 
	 * @throw IllegalArgumentException if the min level is not less or equal the max level or some levels are negative
	 */
	ITreeExpansionToolbarItemModelBuilder setLevels(int minlevel, int maxLevel, boolean unboundPossible);

	/**
	 * Sets a range of levels with default labels for each level.
	 * The minLevel is set to 0 by default
	 * 
	 * @param maxLevel The maximum level
	 * @param unboundPossible If true, a unbound expansion can be set
	 * 
	 * @return This builder
	 * 
	 * @throw IllegalArgumentException if the max level is negative
	 */
	ITreeExpansionToolbarItemModelBuilder setLevels(int maxLevel, boolean unboundPossible);

	/**
	 * Sets a range of levels with default labels for each level.
	 * The minLevel is set to 0 by default.
	 * A unbound level is added by default
	 * 
	 * @param maxLevel The maximum level
	 * 
	 * @return This builder
	 * 
	 * @throw IllegalArgumentException if the max level is negative
	 */
	ITreeExpansionToolbarItemModelBuilder setLevels(int maxLevel);

	/**
	 * Sets the level that is used by default.
	 * If no default is set, the first added level is the default level
	 * 
	 * @param defaultLevel The level that will be used by default.
	 * 
	 * @return This builder
	 */
	ITreeExpansionToolbarItemModelBuilder setDefaultLevel(int defaultLevel);

	/**
	 * Sets the unbound level as default level.
	 * 
	 * @return This builder
	 */
	ITreeExpansionToolbarItemModelBuilder setUnboundDefaultLevel();

	/**
	 * Builds the expand tree toolbar item
	 * 
	 * @return The expand tree toolbar item
	 */
	IToolBarItemModel buildExpandItem();

	/**
	 * Builds the collapse tree toolbar item
	 * 
	 * @return The collapse tree toolbar item
	 */
	IToolBarItemModel buildCollapseItem();

}
