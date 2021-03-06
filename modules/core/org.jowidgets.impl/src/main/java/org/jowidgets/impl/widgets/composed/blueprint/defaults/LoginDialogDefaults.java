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

import org.jowidgets.api.types.InputDialogDefaultButtonPolicy;
import org.jowidgets.api.widgets.blueprint.builder.ILoginDialogSetupBuilder;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultInitializer;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;

public class LoginDialogDefaults implements IDefaultInitializer<ILoginDialogSetupBuilder<?>> {

    @Override
    public void initialize(final ILoginDialogSetupBuilder<?> builder) {
        final BluePrintFactory bpF = new BluePrintFactory();
        builder.setDecorated(false);
        builder.setBackgroundColor(new ColorValue(197, 216, 226));
        builder.setLoginButton(bpF.button(Messages.getString("LoginDialogDefaults.login"))); //$NON-NLS-1$
        builder.setCancelButton(bpF.button(Messages.getString("LoginDialogDefaults.cancel"))); //$NON-NLS-1$
        builder.setDefaultButtonPolicy(InputDialogDefaultButtonPolicy.OK);
        builder.setCloseable(false);
        builder.setResizable(false);
        builder.setMinSize(new Dimension(400, 200));
    }

}
