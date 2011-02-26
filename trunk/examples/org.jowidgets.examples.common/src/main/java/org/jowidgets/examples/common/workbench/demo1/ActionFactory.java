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

package org.jowidgets.examples.common.workbench.demo1;

import java.util.UUID;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IInputDialog;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.IInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.workbench.api.IComponentTreeNode;
import org.jowidgets.workbench.api.IComponentTreeNodeContext;
import org.jowidgets.workbench.api.IFolderContext;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;

public class ActionFactory {

	public IAction createAddFolderAction(final IWorkbenchApplicationContext context) {
		final IActionBuilder actionBuilder = Toolkit.getActionBuilderFactory().create();
		actionBuilder.setText("Add new Folder");
		actionBuilder.setIcon(SilkIcons.FOLDER_ADD);
		actionBuilder.setCommand(new ICommandExecutor() {

			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				final IInputDialog<String> inputDialog = createInputDialog(executionContext, "Folder name");
				inputDialog.setVisible(true);
				if (inputDialog.isOkPressed()) {
					final IComponentTreeNode componentTreeNode = new FolderTreeNodeDemo(
						UUID.randomUUID().toString(),
						inputDialog.getValue());
					context.add(componentTreeNode);
				}
			}

		});

		return actionBuilder.build();
	}

	public IAction createAddComponentAction(final IComponentTreeNodeContext context) {
		final IActionBuilder actionBuilder = Toolkit.getActionBuilderFactory().create();
		actionBuilder.setText("Add new Component");
		actionBuilder.setIcon(SilkIcons.APPLICATION_ADD);
		actionBuilder.setCommand(new ICommandExecutor() {

			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				final IInputDialog<String> inputDialog = createInputDialog(executionContext, "Component name");
				inputDialog.setVisible(true);
				if (inputDialog.isOkPressed()) {
					final IComponentTreeNode componentTreeNode = new ComponentTreeNodeDemo1(
						UUID.randomUUID().toString(),
						inputDialog.getValue());
					context.add(componentTreeNode);
					context.setExpanded(true);
				}
			}

		});

		return actionBuilder.build();
	}

	public IAction createAddFolderAction(final IComponentTreeNodeContext context) {
		final IActionBuilder actionBuilder = Toolkit.getActionBuilderFactory().create();
		actionBuilder.setText("Add new Folder");
		actionBuilder.setIcon(SilkIcons.FOLDER_ADD);
		actionBuilder.setCommand(new ICommandExecutor() {

			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				final IInputDialog<String> inputDialog = createInputDialog(executionContext, "Folder name");
				inputDialog.setVisible(true);
				if (inputDialog.isOkPressed()) {
					final IComponentTreeNode componentTreeNode = new FolderTreeNodeDemo(
						UUID.randomUUID().toString(),
						inputDialog.getValue());

					final IComponentTreeNodeContext parentTreeNode = context.getParent();
					if (parentTreeNode == null) {
						context.getWorkbenchApplicationContext().add(componentTreeNode);
					}
					else {
						parentTreeNode.add(componentTreeNode);
					}
				}
			}

		});

		return actionBuilder.build();
	}

	public IAction createDeleteAction(
		final IComponentTreeNodeContext context,
		final IComponentTreeNode treeNode,
		final String text,
		final IImageConstant icon) {

		final IActionBuilder actionBuilder = Toolkit.getActionBuilderFactory().create();
		actionBuilder.setText(text);
		actionBuilder.setIcon(icon);
		actionBuilder.setCommand(new ICommandExecutor() {

			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				final IComponentTreeNodeContext parentTreeNode = context.getParent();
				if (parentTreeNode == null) {
					context.getWorkbenchApplicationContext().remove(treeNode);
				}
				else {
					parentTreeNode.remove(treeNode);
				}
			}

		});

		return actionBuilder.build();
	}

	private IInputDialog<String> createInputDialog(final IExecutionContext executionContext, final String inputName) {
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		final IInputDialogBluePrint<String> inputDialogBp = bpf.inputDialog(createLabelDialogCreator(inputName));
		inputDialogBp.setValidationLabel(bpf.validationLabel().setOkIcon(SilkIcons.ACCEPT));
		inputDialogBp.setTitle(executionContext.getAction().getText());
		inputDialogBp.setIcon(executionContext.getAction().getIcon());
		inputDialogBp.setMissingInputText("Please enter the " + inputName);
		inputDialogBp.setMissingInputIcon(IconsSmall.INFO);
		inputDialogBp.setCloseable(true);
		inputDialogBp.setResizable(false);

		final IWindow windowAncestor = Toolkit.getWidgetUtils().getWindowAncestor(executionContext.getSource());
		if (windowAncestor != null) {
			return windowAncestor.createChildWindow(inputDialogBp);
		}
		return Toolkit.getWidgetFactory().create(inputDialogBp);
	}

	private IInputContentCreator<String> createLabelDialogCreator(final String inputName) {
		return new IInputContentCreator<String>() {

			private IInputControl<String> inputField;

			@Override
			public void createContent(final IInputContentContainer contentContainer) {
				final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
				contentContainer.setLayout(new MigLayoutDescriptor("[][grow]", "[]"));
				contentContainer.add(bpf.textLabel(inputName), "");
				inputField = contentContainer.add(bpf.inputFieldString(), "growx, w 180:180:180");

				contentContainer.registerInputWidget(inputName, inputField);
			}

			@Override
			public void setValue(final String value) {
				inputField.setValue(value);
			}

			@Override
			public String getValue() {
				return inputField.getValue();
			}

			@Override
			public ValidationResult validate() {
				return new ValidationResult();
			}

			@Override
			public boolean isMandatory() {
				return true;
			}
		};
	}

	public IAction createAddViewAction(final IFolderContext context) {
		final IActionBuilder actionBuilder = Toolkit.getActionBuilderFactory().create();
		actionBuilder.setText("Add new View");
		actionBuilder.setIcon(SilkIcons.ADD);
		actionBuilder.setCommand(new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				final IInputDialog<String> inputDialog = createInputDialog(executionContext, "View name");
				inputDialog.setVisible(true);
				// CHECKSTYLE:OFF
				if (inputDialog.isOkPressed()) {
					// TODO MG implement
				}
				// CHECKSTYLE:ON
			}

		});

		return actionBuilder.build();
	}

}
