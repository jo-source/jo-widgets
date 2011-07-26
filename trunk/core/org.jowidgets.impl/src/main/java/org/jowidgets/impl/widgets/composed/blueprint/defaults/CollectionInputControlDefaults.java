/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.widgets.composed.blueprint.defaults;

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.blueprint.builder.ICollectionInputControlSetupBuilder;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultInitializer;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.Validator;

public class CollectionInputControlDefaults implements IDefaultInitializer<ICollectionInputControlSetupBuilder<?, ?>> {

	// i18n
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void initialize(final ICollectionInputControlSetupBuilder<?, ?> builder) {
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		builder.setValidationLabel(bpf.inputComponentValidationLabel().setShowValidationMessage(false));
		builder.setValidationLabelSize(new Dimension(20, 20));
		builder.setAddButton(bpf.button().setIcon(IconsSmall.ADD).setToolTipText("Add new entry"));
		builder.setAddButtonSize(new Dimension(21, 21));
		builder.setRemoveButton(bpf.button().setIcon(IconsSmall.SUB));
		builder.setRemoveButtonSize(new Dimension(21, 21));
		builder.setEditable(true);
		final IValidator validator = Validator.okValidator();
		builder.setValidator(validator);
		builder.setCollectionValidator(validator);
	}
}
