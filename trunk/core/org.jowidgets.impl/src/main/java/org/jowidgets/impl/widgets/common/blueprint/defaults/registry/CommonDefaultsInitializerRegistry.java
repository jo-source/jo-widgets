/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.impl.widgets.common.blueprint.defaults.registry;

import org.jowidgets.common.widgets.builder.IButtonSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.ICheckBoxSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.IContainerSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.IDialogSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.IFrameSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.IPopupDialogSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.IScrollCompositeSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.ISeparatorSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.ITabFolderSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.ITableSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.ITextAreaSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.ITextFieldSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.ITextLabelSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.IToolBarSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.ITreeSetupBuilderCommon;
import org.jowidgets.common.widgets.builder.IWindowSetupBuilderCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.ButtonDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.CheckBoxDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.ContainerDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.DialogDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.FrameDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.PopupDialogDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.ScrolledCompositeDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.SeparatorDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.TabFolderDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.TableDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.TextAreaDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.TextFieldDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.TextLabelDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.ToolBarDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.TreeDefaultsCommon;
import org.jowidgets.impl.widgets.common.blueprint.defaults.WindowDefaultsCommon;
import org.jowidgets.tools.widgets.blueprint.defaults.DefaultsInitializerRegistry;

public class CommonDefaultsInitializerRegistry extends DefaultsInitializerRegistry {

	public CommonDefaultsInitializerRegistry() {
		super();
		register(IButtonSetupBuilderCommon.class, new ButtonDefaultsCommon());
		register(ICheckBoxSetupBuilderCommon.class, new CheckBoxDefaultsCommon());
		register(IContainerSetupBuilderCommon.class, new ContainerDefaultsCommon());
		register(IWindowSetupBuilderCommon.class, new WindowDefaultsCommon());
		register(IFrameSetupBuilderCommon.class, new FrameDefaultsCommon());
		register(IDialogSetupBuilderCommon.class, new DialogDefaultsCommon());
		register(IScrollCompositeSetupBuilderCommon.class, new ScrolledCompositeDefaultsCommon());
		register(ISeparatorSetupBuilderCommon.class, new SeparatorDefaultsCommon());
		register(ITextLabelSetupBuilderCommon.class, new TextLabelDefaultsCommon());
		register(ITextFieldSetupBuilderCommon.class, new TextFieldDefaultsCommon());
		register(ITextAreaSetupBuilderCommon.class, new TextAreaDefaultsCommon());
		register(ITabFolderSetupBuilderCommon.class, new TabFolderDefaultsCommon());
		register(ITreeSetupBuilderCommon.class, new TreeDefaultsCommon());
		register(ITableSetupBuilderCommon.class, new TableDefaultsCommon());
		register(IToolBarSetupBuilderCommon.class, new ToolBarDefaultsCommon());
		register(IPopupDialogSetupBuilderCommon.class, new PopupDialogDefaultsCommon());
	}
}
