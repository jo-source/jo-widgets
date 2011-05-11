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

package org.jowidgets.tools.model.item;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IActionItemModelBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.controler.IActionListener;

public class ActionItemModel extends AbstractItemModelWrapper implements IActionItemModel {

	public ActionItemModel() {
		this(builder());
	}

	public ActionItemModel(final IAction action) {
		this(builder(action));
	}

	public ActionItemModel(final String text) {
		this(builder(text));
	}

	public ActionItemModel(final String text, final IImageConstant icon) {
		this(builder(text, icon));
	}

	public ActionItemModel(final String text, final String toolTipText) {
		this(builder(text, toolTipText));
	}

	public ActionItemModel(final String text, final String toolTipText, final IImageConstant icon) {
		this(builder(text, toolTipText, icon));
	}

	public ActionItemModel(final String id, final String text, final String toolTipText, final IImageConstant icon) {
		this(builder(id, text, toolTipText, icon));
	}

	public ActionItemModel(final IActionItemModelBuilder builder) {
		super(builder.build());
	}

	@Override
	protected IActionItemModel getItemModel() {
		return (IActionItemModel) super.getItemModel();
	}

	@Override
	public final void addActionListener(final IActionListener actionListener) {
		getItemModel().addActionListener(actionListener);
	}

	@Override
	public final void removeActionListener(final IActionListener actionListener) {
		getItemModel().removeActionListener(actionListener);
	}

	@Override
	public final IAction getAction() {
		return getItemModel().getAction();
	}

	@Override
	public final void setAction(final IAction action) {
		getItemModel().setAction(action);
	}

	@Override
	public final void actionPerformed() {
		getItemModel().actionPerformed();
	}

	@Override
	public IActionItemModel createCopy() {
		return getItemModel().createCopy();
	}

	public static IActionItemModelBuilder builder() {
		return Toolkit.getModelFactoryProvider().getItemModelFactory().actionItemBuilder();
	}

	public static IActionItemModelBuilder builder(final String text) {
		return builder().setText(text);
	}

	public static IActionItemModelBuilder builder(final IAction action) {
		return builder().setAction(action);
	}

	public static IActionItemModelBuilder builder(final String text, final String toolTipText) {
		return builder(text).setToolTipText(toolTipText);
	}

	public static IActionItemModelBuilder builder(final String text, final IImageConstant icon) {
		return builder().setText(text).setIcon(icon);
	}

	public static IActionItemModelBuilder builder(final String text, final String toolTipText, final IImageConstant icon) {
		return builder(text, toolTipText).setIcon(icon);
	}

	public static IActionItemModelBuilder builder(
		final String id,
		final String text,
		final String toolTipText,
		final IImageConstant icon) {
		return builder(text, toolTipText, icon).setId(id);
	}

}
