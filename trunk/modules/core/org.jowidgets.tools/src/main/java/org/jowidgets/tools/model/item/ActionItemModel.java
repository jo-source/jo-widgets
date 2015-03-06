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
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.util.IDecorator;

public class ActionItemModel extends AbstractItemModelWrapper implements IActionItemModel {

	/**
	 * Creates a new action item model
	 */
	public ActionItemModel() {
		this(builder());
	}

	/**
	 * Creates a new action item model
	 * 
	 * @param action The action use
	 */
	public ActionItemModel(final IAction action) {
		this(builder(action));
	}

	/**
	 * Creates a new action item model
	 * 
	 * @param text The label text to use
	 */
	public ActionItemModel(final String text) {
		this(builder(text));
	}

	/**
	 * Creates a new action item model
	 * 
	 * @param text The label text to use
	 * @param icon The icon to use
	 */
	public ActionItemModel(final String text, final IImageConstant icon) {
		this(builder(text, icon));
	}

	/**
	 * Creates a new action item model
	 * 
	 * @param text The label text to use
	 * @param toolTipText The tooltip text to use
	 */
	public ActionItemModel(final String text, final String toolTipText) {
		this(builder(text, toolTipText));
	}

	/**
	 * Creates a new action item model
	 * 
	 * @param text The label text to use
	 * @param toolTipText The tooltip text to use
	 * @param icon The icon to use
	 */
	public ActionItemModel(final String text, final String toolTipText, final IImageConstant icon) {
		this(builder(text, toolTipText, icon));
	}

	/**
	 * Creates a new action item model
	 * 
	 * @param id The id to use
	 * @param text The label text to use
	 * @param toolTipText The tooltip text to use
	 * @param icon The icon to use
	 */
	public ActionItemModel(final String id, final String text, final String toolTipText, final IImageConstant icon) {
		this(builder(id, text, toolTipText, icon));
	}

	/**
	 * Creates a action item model defined by a builder
	 * 
	 * @param builder The builder that defines the model
	 */
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
	public void addDecorator(final IDecorator<IAction> action) {
		getItemModel().addDecorator(action);
	}

	@Override
	public void removeDecorator(final IDecorator<IAction> action) {
		getItemModel().removeDecorator(action);
	}

	@Override
	public final void actionPerformed() {
		getItemModel().actionPerformed();
	}

	@Override
	public IActionItemModel createCopy() {
		return getItemModel().createCopy();
	}

	/**
	 * Creates a new action item model
	 * 
	 * @return A new action item model
	 */
	public static IActionItemModel create() {
		return builder().build();
	}

	/**
	 * Creates a new action item model builder
	 * 
	 * @return A new action item model builder
	 */
	public static IActionItemModelBuilder builder() {
		return Toolkit.getModelFactoryProvider().getItemModelFactory().actionItemBuilder();
	}

	/**
	 * Creates a new action item model builder
	 * 
	 * @param text The label text to set on the builder
	 * 
	 * @return A new action item model builder
	 */
	public static IActionItemModelBuilder builder(final String text) {
		return builder().setText(text);
	}

	/**
	 * Creates a new action item model builder
	 * 
	 * @param action The action to set on the builder
	 * 
	 * @return A new action item model builder
	 */
	public static IActionItemModelBuilder builder(final IAction action) {
		return builder().setAction(action);
	}

	/**
	 * Creates a new action item model builder
	 * 
	 * @param text The label text to set on the builder
	 * @param toolTipText The tooltip text to set on the builder
	 * 
	 * @return A new action item model builder
	 */
	public static IActionItemModelBuilder builder(final String text, final String toolTipText) {
		return builder(text).setToolTipText(toolTipText);
	}

	/**
	 * Creates a new action item model builder
	 * 
	 * @param text The label text to set on the builder
	 * @param icon The icon to set on the builder
	 * 
	 * @return A new action item model builder
	 */
	public static IActionItemModelBuilder builder(final String text, final IImageConstant icon) {
		return builder().setText(text).setIcon(icon);
	}

	/**
	 * Creates a new action item model builder
	 * 
	 * @param text The label text to set on the builder
	 * @param toolTipText The tooltip text to set on the builder
	 * @param icon The icon to set on the builder
	 * 
	 * @return A new action item model builder
	 */
	public static IActionItemModelBuilder builder(final String text, final String toolTipText, final IImageConstant icon) {
		return builder(text, toolTipText).setIcon(icon);
	}

	/**
	 * Creates a new action item model builder
	 * 
	 * @param id The id to set on builder
	 * @param text The label text to set on the builder
	 * @param toolTipText The tooltip text to set on the builder
	 * @param icon The icon to set on the builder
	 * 
	 * @return A new action item model builder
	 */
	public static IActionItemModelBuilder builder(
		final String id,
		final String text,
		final String toolTipText,
		final IImageConstant icon) {
		return builder(text, toolTipText, icon).setId(id);
	}

}
