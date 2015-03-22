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

package org.jowidgets.api.toolkit;

import java.util.List;

import org.jowidgets.api.animation.IAnimationRunnerBuilder;
import org.jowidgets.api.animation.IWaitAnimationProcessor;
import org.jowidgets.api.clipboard.IClipboard;
import org.jowidgets.api.clipboard.ITransferableBuilder;
import org.jowidgets.api.command.IActionBuilderFactory;
import org.jowidgets.api.command.IDefaultActionFactory;
import org.jowidgets.api.convert.IConverterProvider;
import org.jowidgets.api.convert.ISliderConverterFactory;
import org.jowidgets.api.event.IDelayedEventRunnerBuilder;
import org.jowidgets.api.image.IImageFactory;
import org.jowidgets.api.layout.ILayoutFactoryProvider;
import org.jowidgets.api.mask.ITextMaskBuilder;
import org.jowidgets.api.model.IModelFactoryProvider;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.utils.IWidgetUtils;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintProxyFactory;
import org.jowidgets.api.widgets.content.IInputContentCreatorFactory;
import org.jowidgets.api.widgets.descriptor.IFrameDescriptor;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.util.ITypedKey;

public interface IToolkit {

    /**
     * Gets the toolkits application runner
     * 
     * @return The toolkits application runner
     */
    IApplicationRunner getApplicationRunner();

    /**
     * Gets the ui thread access.
     * 
     * Remark: This method must be invoked in the ui thread
     * 
     * 
     * @throws IllegalStateException if the method was not accessed in the ui thread
     * 
     * @return The ui thread access
     */
    IUiThreadAccess getUiThreadAccess();

    /**
     * Gets the blue print factory
     * 
     * @return The blue print factory
     */
    IBluePrintFactory getBluePrintFactory();

    /**
     * Gets the blue print proxy factory
     * 
     * @return The blue print proxy factory, never null
     */
    IBluePrintProxyFactory getBluePrintProxyFactory();

    /**
     * Gets the toolkits converter provider
     * 
     * @return The toolkits converter provider
     */
    IConverterProvider getConverterProvider();

    /**
     * Gets the slider converter factory
     * 
     * @return The slider converter factory
     */
    ISliderConverterFactory getSliderConverterFactory();

    /**
     * Creates a root frame for the given descriptor
     * 
     * @param descriptor The descriptor to create the frame for
     * 
     * @return The created frame
     */
    IFrame createRootFrame(IFrameDescriptor descriptor);

    /**
     * Creates an root frame for the given descriptor and application lifecycle.
     * 
     * When the rootFrame will be closed, the lifecycle will be finished.
     * 
     * @param descriptor The descriptor to create the frame for
     * @param lifecycle The lifecycle to bind on the frame
     * 
     * @return the created frame
     */
    IFrame createRootFrame(IFrameDescriptor descriptor, IApplicationLifecycle lifecycle);

    /**
     * Gets the generic widget factory
     * 
     * @return The generic widget factory
     */
    IGenericWidgetFactory getWidgetFactory();

    /**
     * Gets the widget wrapper factory that can be used to create wrappers
     * for native widgets
     * 
     * @return The widget wrapper factory
     */
    IWidgetWrapperFactory getWidgetWrapperFactory();

    /**
     * Gets the image factory
     * 
     * @return The image factory
     */
    IImageFactory getImageFactory();

    /**
     * Gets the image registry that can be used to register icons and images
     * 
     * @return The image registry
     */
    IImageRegistry getImageRegistry();

    /**
     * Gets the message pane that can be used to show user messages
     * 
     * @return The message pane
     */
    IMessagePane getMessagePane();

    /**
     * Gets the question pane that can be used to ask user questions
     * 
     * @return The question pane
     */
    IQuestionPane getQuestionPane();

    /**
     * Gets the login pane that can be used for user login
     * 
     * @return The login pane
     */
    ILoginPane getLoginPane();

    /**
     * Gets the factory for layouts
     * 
     * @return The layout factory provider
     */
    ILayoutFactoryProvider getLayoutFactoryProvider();

    /**
     * Gets the action builder factory
     * 
     * @return The action builder factory
     */
    IActionBuilderFactory getActionBuilderFactory();

    /**
     * Gets the default action factory
     * 
     * @return The default action factory
     */
    IDefaultActionFactory getDefaultActionFactory();

    /**
     * Gets the model factory provider
     * 
     * @return The model factory provider
     */
    IModelFactoryProvider getModelFactoryProvider();

    /**
     * Gets the text mask builder
     * 
     * @return The text mask builder
     */
    ITextMaskBuilder createTextMaskBuilder();

    /**
     * Gets the input content creator factory
     * 
     * @return the input content creator factory
     */
    IInputContentCreatorFactory getInputContentCreatorFactory();

    /**
     * Gets the wait animation processor
     * 
     * @return the wait animation processor
     */
    IWaitAnimationProcessor getWaitAnimationProcessor();

    /**
     * Creates an animation runner builder
     * 
     * @return An animation runner builder
     */
    IAnimationRunnerBuilder getAnimationRunnerBuilder();

    /**
     * Gets the delayed event runner builder
     * 
     * @return The delayed event runner builder
     */
    IDelayedEventRunnerBuilder getDelayedEventRunnerBuilder();

    /**
     * Gets the system clipboard
     * 
     * @return The system clipboard
     */
    IClipboard getClipboard();

    /**
     * Creates a transferable builder
     * 
     * @return a transferable builder
     */
    ITransferableBuilder createTransferableBuilder();

    /**
     * Gets the widget utils
     * 
     * @return The widget utils
     */
    IWidgetUtils getWidgetUtils();

    /**
     * Gets the active window
     * 
     * @return The active window or null, if no active window exists
     */
    IWindow getActiveWindow();

    /**
     * Gets a list of all windows
     * 
     * @return A list of all windows
     */
    List<IWindow> getAllWindows();

    /**
     * Sets a value for a typed key for the toolkit
     * 
     * @param <VALUE_TYPE> The type of the value
     * @param key The key
     * @param value The value to set, may be null
     */
    <VALUE_TYPE> void setValue(ITypedKey<VALUE_TYPE> key, VALUE_TYPE value);

    /**
     * Gets a value for a typed key
     * 
     * @param <VALUE_TYPE> The type of the resulting value
     * @param key The key to get the value for
     * 
     * @return The value for the key, may be null
     */
    <VALUE_TYPE> VALUE_TYPE getValue(ITypedKey<VALUE_TYPE> key);

    /**
     * Transforms a local component position to a screen position
     * 
     * @param localPosition Local position relative to the component
     * @param component The component
     * @return screen position
     */
    Position toScreen(final Position localPosition, final IComponent component);

    /**
     * Transforms a screen position to a local component position
     * 
     * @param screenPosition Screen position
     * @param component The component
     * @return local position relative to the component
     */
    Position toLocal(final Position screenPosition, final IComponent component);

    /**
     * Gets the supported widgets information
     * 
     * @return The supported widgets information
     */
    ISupportedWidgets getSupportedWidgets();

    /**
     * Checks if the underlying spi implementation has mig layout support
     * 
     * @return True if native mig layout is supported, false otherwise
     */
    boolean hasSpiMigLayoutSupport();

}
