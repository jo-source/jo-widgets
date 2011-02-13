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

package org.jowidgets.impl.widgets.basic.blueprint.defaults.registry;

import org.jowidgets.api.widgets.blueprint.builder.IButtonSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.IComboBoxSelectionSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.IComponentSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.IInputComponentSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.ISelectableItemSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.ISplitCompositeSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.ITextFieldSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.ITreeNodeSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.IWindowSetupBuilder;
import org.jowidgets.impl.widgets.basic.blueprint.defaults.ButtonDefaults;
import org.jowidgets.impl.widgets.basic.blueprint.defaults.ComboBoxSelectionDefaults;
import org.jowidgets.impl.widgets.basic.blueprint.defaults.InputWidgetDefaults;
import org.jowidgets.impl.widgets.basic.blueprint.defaults.SelectableItemDefaults;
import org.jowidgets.impl.widgets.basic.blueprint.defaults.SplitCompositeWidgetDefaults;
import org.jowidgets.impl.widgets.basic.blueprint.defaults.TextFieldDefaults;
import org.jowidgets.impl.widgets.basic.blueprint.defaults.TreeNodeDefaults;
import org.jowidgets.impl.widgets.basic.blueprint.defaults.WidgetDefaults;
import org.jowidgets.impl.widgets.basic.blueprint.defaults.WindowDefaults;
import org.jowidgets.impl.widgets.common.blueprint.defaults.registry.CommonDefaultsInitializerRegistry;

public class BasicDefaultsInitializerRegistry extends CommonDefaultsInitializerRegistry {

	public BasicDefaultsInitializerRegistry() {
		super();
		register(IButtonSetupBuilder.class, new ButtonDefaults());
		register(IComboBoxSelectionSetupBuilder.class, new ComboBoxSelectionDefaults());
		register(IInputComponentSetupBuilder.class, new InputWidgetDefaults());
		register(ITextFieldSetupBuilder.class, new TextFieldDefaults());
		register(IWindowSetupBuilder.class, new WindowDefaults());
		register(ISplitCompositeSetupBuilder.class, new SplitCompositeWidgetDefaults());
		register(IComponentSetupBuilder.class, new WidgetDefaults());
		register(ISelectableItemSetupBuilder.class, new SelectableItemDefaults());
		register(ITreeNodeSetupBuilder.class, new TreeNodeDefaults());
	}
}
