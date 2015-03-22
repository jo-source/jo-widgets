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
package org.jowidgets.impl.base.blueprint.factory;

import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenience;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultInitializer;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintProxyFactory;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.builder.ISetupBuilder;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.util.Assert;

public class BaseBluePrintFactory implements IBluePrintProxyFactory {

    private final IBluePrintProxyFactory bluePrintProxyFactory;

    public BaseBluePrintFactory(final IBluePrintProxyFactory bluePrintProxyFactory) {
        Assert.paramNotNull(bluePrintProxyFactory, "IBluePrintProxyFactory");
        this.bluePrintProxyFactory = bluePrintProxyFactory;
    }

    @Override
    public <WIDGET_TYPE extends IWidget, BLUE_PRINT_TYPE extends ISetupBuilder<BLUE_PRINT_TYPE> & IWidgetDescriptor<WIDGET_TYPE>> BLUE_PRINT_TYPE bluePrint(
        final Class<BLUE_PRINT_TYPE> bluePrintType) {
        return bluePrintProxyFactory.bluePrint(bluePrintType);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected <BLUE_PRINT_TYPE extends IWidgetDescriptor<? extends IWidgetCommon>> BLUE_PRINT_TYPE createProxy(
        final Class bluePrintType) {
        final IWidgetDescriptor bluePrint = bluePrint(bluePrintType);
        return (BLUE_PRINT_TYPE) bluePrint;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setSetupBuilderConvenience(
        final Class<? extends ISetupBuilder> setupBuilder,
        final ISetupBuilderConvenience<?> setupBuilderConvenience) {
        bluePrintProxyFactory.setSetupBuilderConvenience(setupBuilder, setupBuilderConvenience);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addDefaultsInitializer(
        final Class<? extends ISetupBuilder> setupBuilder,
        final IDefaultInitializer<?> defaultsImpl) {
        bluePrintProxyFactory.addDefaultsInitializer(setupBuilder, defaultsImpl);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setDefaultsInitializer(
        final Class<? extends ISetupBuilder> setupBuilder,
        final IDefaultInitializer<?> defaultsImpl) {
        bluePrintProxyFactory.setDefaultsInitializer(setupBuilder, defaultsImpl);
    }

}
