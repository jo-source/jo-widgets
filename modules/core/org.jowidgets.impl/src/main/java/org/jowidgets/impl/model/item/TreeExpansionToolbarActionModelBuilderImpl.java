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

package org.jowidgets.impl.model.item;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.command.CollapseTreeAction;
import org.jowidgets.api.command.ExpandTreeAction;
import org.jowidgets.api.command.ITreeExpansionAction;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IRadioItemModel;
import org.jowidgets.api.model.item.IToolBarItemModel;
import org.jowidgets.api.model.item.ITreeExpansionToolbarItemModelBuilder;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.common.widgets.controller.IItemStateListener;
import org.jowidgets.tools.model.item.ActionItemModel;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.item.PopupActionItemModel;
import org.jowidgets.util.Assert;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.util.Tuple;
import org.jowidgets.util.maybe.IMaybe;
import org.jowidgets.util.maybe.Some;

final class TreeExpansionToolbarActionModelBuilderImpl implements ITreeExpansionToolbarItemModelBuilder {

	private final List<Tuple<Integer, String>> levels;
	private IMaybe<Integer> defaultLevelMaybe;
	private boolean exhausted;

	private ITreeExpansionAction expandAction;
	private ITreeExpansionAction collapseAction;

	private IToolBarItemModel expandItem;
	private IToolBarItemModel collapseItem;

	TreeExpansionToolbarActionModelBuilderImpl() {
		this.levels = new LinkedList<Tuple<Integer, String>>();
		exhausted = false;
	}

	@Override
	public ITreeExpansionToolbarItemModelBuilder setActions(final ITreeContainer tree) {
		Assert.paramNotNull(tree, "tree");
		setExpandAction(ExpandTreeAction.create(tree));
		setCollapseAction(CollapseTreeAction.create(tree));
		return this;
	}

	@Override
	public ITreeExpansionToolbarItemModelBuilder setCollapseAction(final ITreeExpansionAction collapseAction) {
		Assert.paramNotNull(collapseAction, "collapseAction");
		this.collapseAction = collapseAction;
		return this;
	}

	@Override
	public ITreeExpansionToolbarItemModelBuilder setExpandAction(final ITreeExpansionAction expandAction) {
		Assert.paramNotNull(expandAction, "expandAction");
		this.expandAction = expandAction;
		return this;
	}

	@Override
	public ITreeExpansionToolbarItemModelBuilder addUnboundLevel() {
		return addUnboundLevel(getDefaultUnboundLevelName());
	}

	@Override
	public ITreeExpansionToolbarItemModelBuilder addUnboundLevel(final String label) {
		checkExhausted();
		Assert.paramNotNull(label, "label");

		levels.add(new Tuple<Integer, String>(null, label));
		return this;
	}

	@Override
	public ITreeExpansionToolbarItemModelBuilder addLevel(final int level) {
		return addLevel(level, getDefaultLevelName(level));
	}

	@Override
	public ITreeExpansionToolbarItemModelBuilder addLevel(final int level, final String levelName) {
		checkExhausted();
		Assert.paramNotNull(levelName, "levelName");

		levels.add(new Tuple<Integer, String>(Integer.valueOf(level), levelName));
		return this;
	}

	@Override
	public ITreeExpansionToolbarItemModelBuilder setLevels(final int maxLevel) {
		return setLevels(maxLevel, true);
	}

	@Override
	public ITreeExpansionToolbarItemModelBuilder setLevels(final int maxLevel, final boolean unboundPossible) {
		return setLevels(0, maxLevel, unboundPossible);
	}

	@Override
	public ITreeExpansionToolbarItemModelBuilder setLevels(final int minLevel, final int maxLevel, final boolean unboundPossible) {
		checkExhausted();

		Assert.paramNotNegative(minLevel, "minLevel");
		Assert.paramNotNegative(maxLevel, "maxLevel");
		Assert.paramLessOrEqual(minLevel, maxLevel, "minLevel", "maxLevel");

		defaultLevelMaybe = null;
		levels.clear();

		if (unboundPossible) {
			addUnboundLevel();
		}

		for (int level = minLevel; level <= maxLevel; level++) {
			addLevel(level);
		}

		return this;
	}

	@Override
	public ITreeExpansionToolbarItemModelBuilder setDefaultLevel(final int defaultLevel) {
		return setDefaultLevelImpl(Integer.valueOf(defaultLevel));
	}

	@Override
	public ITreeExpansionToolbarItemModelBuilder setUnboundDefaultLevel() {
		return setDefaultLevelImpl(null);
	}

	private ITreeExpansionToolbarItemModelBuilder setDefaultLevelImpl(final Integer defaultLevel) {
		checkExhausted();
		if (!hasDefaultLevel(defaultLevel)) {
			throw new IllegalArgumentException("There was now level with the value '" + defaultLevel + "' added yet.");
		}
		this.defaultLevelMaybe = new Some<Integer>(defaultLevel);
		return this;
	}

	private boolean hasDefaultLevel(final Integer defaultLevel) {
		for (final Tuple<Integer, String> level : levels) {
			if (NullCompatibleEquivalence.equals(level.getFirst(), defaultLevel)) {
				return true;
			}
		}
		return false;
	}

	private void checkExhausted() {
		if (exhausted) {
			throw new IllegalStateException("The builder is exhausted. It's a single use builder that can only be used once.");
		}
	}

	private static String getDefaultLevelName(final int level) {
		return "Level " + (level + 1);
	}

	private static String getDefaultUnboundLevelName() {
		return "All levels";
	}

	@Override
	public IToolBarItemModel buildExpandItem() {
		if (expandItem == null) {
			doBuild();
		}
		if (expandItem == null) {
			throw new IllegalStateException("The was no expand action set so far");
		}
		return expandItem;
	}

	@Override
	public IToolBarItemModel buildCollapseItem() {
		if (collapseItem == null) {
			doBuild();
		}
		if (collapseItem == null) {
			throw new IllegalStateException("The was no collapse action set so far");
		}
		return collapseItem;
	}

	private void doBuild() {
		if (expandAction == null && collapseAction == null) {
			throw new IllegalStateException("There are no expand or collapse actions set so far");
		}
		if (levels.size() > 0) {
			final IMenuModel levelMenu = buildLevelMenu(expandAction, collapseAction);
			if (expandAction != null) {
				expandItem = PopupActionItemModel.builder(expandAction, levelMenu).build();
			}
			if (collapseAction != null) {
				collapseItem = PopupActionItemModel.builder(collapseAction, levelMenu).build();
			}
		}
		else {
			if (expandAction != null) {
				expandItem = ActionItemModel.builder(expandAction).build();
			}
			if (collapseAction != null) {
				collapseItem = ActionItemModel.builder(collapseAction).build();
			}
		}
		exhausted = true;
	}

	private IMenuModel buildLevelMenu(final ITreeExpansionAction expandAction, final ITreeExpansionAction collapseAction) {
		final MenuModel levelMenu = new MenuModel();

		final Integer defaultLevel;
		if (defaultLevelMaybe != null) {
			defaultLevel = defaultLevelMaybe.getValue();
		}
		else if (levels.size() > 0) {
			defaultLevel = levels.iterator().next().getFirst();
		}
		else {
			//levels are empty, default value has no effect
			defaultLevel = null;
		}

		for (final Tuple<Integer, String> levelTuple : levels) {
			final String label = levelTuple.getSecond();
			final Integer level = levelTuple.getFirst();
			final boolean selected = NullCompatibleEquivalence.equals(defaultLevel, level);
			addItemListener(levelMenu.addRadioItem(label), expandAction, collapseAction, level, selected);
		}
		return levelMenu;
	}

	private void addItemListener(
		final IRadioItemModel itemModel,
		final ITreeExpansionAction expandAction,
		final ITreeExpansionAction collapseAction,
		final Integer level,
		final boolean selected) {

		itemModel.addItemListener(new IItemStateListener() {
			@Override
			public void itemStateChanged() {
				if (itemModel.isSelected()) {
					expandAction.setPivotLevel(level);
					collapseAction.setPivotLevel(level);
				}
			}
		});
		itemModel.setSelected(selected);
	}

}
