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

package org.jowidgets.api.widgets.blueprint.factory;

import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenience;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultInitializer;
import org.jowidgets.common.widgets.builder.ISetupBuilder;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

/**
 * Creates implementations of blue print with help of Java proxies
 */
public interface IBluePrintProxyFactory {

    /**
     * Creates a blue print for a bluePrint type.
     * 
     * The created blue print was set up by all registered default initializers and implements
     * all convenience methods that was added with the {@link #setSetupBuilderConvenience(Class, ISetupBuilderConvenience)} method
     * before.
     * 
     * @param bluePrintType The type to get the blue print for
     * 
     * @return The blue print
     */
    <WIDGET_TYPE extends IWidget, BLUE_PRINT_TYPE extends ISetupBuilder<BLUE_PRINT_TYPE> & IWidgetDescriptor<WIDGET_TYPE>> BLUE_PRINT_TYPE bluePrint(
        final Class<BLUE_PRINT_TYPE> bluePrintType);

    /**
     * Sets an implementation for a convenience setter method
     * 
     * @param setupBuilder The type of the setup builder to add the implementation for
     * @param setupBuilderConvenience The convenience method implementation
     */
    @SuppressWarnings("rawtypes")
    void setSetupBuilderConvenience(
        Class<? extends ISetupBuilder> setupBuilder,
        ISetupBuilderConvenience<?> setupBuilderConvenience);

    /**
     * Adds a default initializer that will be invoked when a blue print for the given builder type will be created
     * 
     * The default initializers will be invoked in the same order they was added
     * 
     * @param setupBuilder The type of the setup builder to add the defaults for
     * @param defaultInitializer The defaults initializer
     */
    @SuppressWarnings("rawtypes")
    void addDefaultsInitializer(Class<? extends ISetupBuilder> setupBuilder, IDefaultInitializer<?> defaultInitializer);

    /**
     * Sets a new default initializer that will be invoked when a blue print for the given builder type will be created.
     * 
     * Remark: All other previously set defaults initializer will be removed. Use this method with care!!!
     * 
     * @param setupBuilder The type of the setup builder to set the defaults for
     * @param defaultInitializer The defaults initializer
     */
    @SuppressWarnings("rawtypes")
    void setDefaultsInitializer(Class<? extends ISetupBuilder> setupBuilder, IDefaultInitializer<?> defaultInitializer);

}
