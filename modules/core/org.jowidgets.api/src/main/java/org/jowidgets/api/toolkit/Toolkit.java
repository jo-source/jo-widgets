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

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

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
import org.jowidgets.api.widgets.content.IInputContentCreatorFactory;
import org.jowidgets.api.widgets.descriptor.IFrameDescriptor;
import org.jowidgets.classloading.api.SharedClassLoader;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.util.Assert;
import org.jowidgets.util.ITypedKey;

public final class Toolkit {

    private static IToolkitProvider toolkitProvider;

    private Toolkit() {}

    public static void initialize(final IToolkitProvider toolkitProvider) {
        Assert.paramNotNull(toolkitProvider, "toolkitProvider");
        if (Toolkit.toolkitProvider != null) {
            throw new IllegalStateException("Toolkit is already initialized");
        }
        Toolkit.toolkitProvider = toolkitProvider;
    }

    public static boolean isInitialized() {
        return toolkitProvider != null;
    }

    public static synchronized IToolkit getInstance() {
        if (toolkitProvider == null) {
            final ServiceLoader<IToolkitProvider> toolkitProviderLoader = ServiceLoader.load(
                    IToolkitProvider.class,
                    SharedClassLoader.getCompositeClassLoader());
            final Iterator<IToolkitProvider> iterator = toolkitProviderLoader.iterator();

            if (!iterator.hasNext()) {
                throw new IllegalStateException("No implementation found for '" + IToolkitProvider.class.getName() + "'");
            }

            Toolkit.toolkitProvider = iterator.next();

            if (iterator.hasNext()) {
                throw new IllegalStateException("More than one implementation found for '"
                    + IToolkitProvider.class.getName()
                    + "'");
            }

        }
        return toolkitProvider.get();
    }

    /**
     * Gets the toolkits application runner
     * 
     * @return The toolkits application runner
     */
    public static IApplicationRunner getApplicationRunner() {
        return getInstance().getApplicationRunner();
    }

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
    public static IUiThreadAccess getUiThreadAccess() {
        return getInstance().getUiThreadAccess();
    }

    /**
     * Gets the blue print factory
     * 
     * @return The blue print factory
     */
    public static IBluePrintFactory getBluePrintFactory() {
        return getInstance().getBluePrintFactory();
    }

    /**
     * Gets the toolkits converter provider
     * 
     * @return The toolkits converter provider
     */
    public static IConverterProvider getConverterProvider() {
        return getInstance().getConverterProvider();
    }

    /**
     * Gets the slider converter factory
     * 
     * @return The slider converter factory
     */
    public static ISliderConverterFactory getSliderConverterFactory() {
        return getInstance().getSliderConverterFactory();
    }

    /**
     * Creates a root frame for the given descriptor
     * 
     * @param descriptor The descriptor to create the frame for
     * 
     * @return The created frame
     */
    public static IFrame createRootFrame(final IFrameDescriptor descriptor) {
        return getInstance().createRootFrame(descriptor);
    }

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
    public static IFrame createRootFrame(final IFrameDescriptor descriptor, final IApplicationLifecycle lifecycle) {
        return getInstance().createRootFrame(descriptor, lifecycle);
    }

    /**
     * Gets the generic widget factory
     * 
     * @return The generic widget factory
     */
    public static IGenericWidgetFactory getWidgetFactory() {
        return getInstance().getWidgetFactory();
    }

    /**
     * Gets the widget wrapper factory that can be used to create wrappers
     * for native widgets
     * 
     * @return The widget wrapper factory
     */
    public static IWidgetWrapperFactory getWidgetWrapperFactory() {
        return getInstance().getWidgetWrapperFactory();
    }

    /**
     * Gets the image factory
     * 
     * @return The image factory
     */
    public static IImageFactory getImageFactory() {
        return getInstance().getImageFactory();
    }

    /**
     * Gets the image registry that can be used to register icons and images
     * 
     * @return The image registry
     */
    public static IImageRegistry getImageRegistry() {
        return getInstance().getImageRegistry();
    }

    /**
     * Gets the message pane that can be used to show user messages
     * 
     * @return The message pane
     */
    public static IMessagePane getMessagePane() {
        return getInstance().getMessagePane();
    }

    /**
     * Gets the question pane that can be used to ask user questions
     * 
     * @return The question pane
     */
    public static IQuestionPane getQuestionPane() {
        return getInstance().getQuestionPane();
    }

    /**
     * Gets the login pane that can be used for user login
     * 
     * @return The login pane
     */
    public static ILoginPane getLoginPane() {
        return getInstance().getLoginPane();
    }

    /**
     * Gets the factory for layouts
     * 
     * @return The layout factory provider
     */
    public static ILayoutFactoryProvider getLayoutFactoryProvider() {
        return getInstance().getLayoutFactoryProvider();
    }

    /**
     * Gets the action builder factory
     * 
     * @return The action builder factory
     */
    public static IActionBuilderFactory getActionBuilderFactory() {
        return getInstance().getActionBuilderFactory();
    }

    /**
     * Gets the default action factory
     * 
     * @return The default action factory
     */
    public static IDefaultActionFactory getDefaultActionFactory() {
        return getInstance().getDefaultActionFactory();
    }

    /**
     * Gets the model factory provider
     * 
     * @return The model factory provider
     */
    public static IModelFactoryProvider getModelFactoryProvider() {
        return getInstance().getModelFactoryProvider();
    }

    /**
     * Gets the text mask builder
     * 
     * @return The text mask builder
     */
    public static ITextMaskBuilder createTextMaskBuilder() {
        return getInstance().createTextMaskBuilder();
    }

    /**
     * Gets the input content creator factory
     * 
     * @return the input content creator factory
     */
    public static IInputContentCreatorFactory getInputContentCreatorFactory() {
        return getInstance().getInputContentCreatorFactory();
    }

    /**
     * Gets the wait animation processor
     * 
     * @return the wait animation processor
     */
    public static IWaitAnimationProcessor getWaitAnimationProcessor() {
        return getInstance().getWaitAnimationProcessor();
    }

    /**
     * Creates an animation runner builder
     * 
     * @return An animation runner builder
     */
    public static IAnimationRunnerBuilder getAnimationRunnerBuilder() {
        return getInstance().getAnimationRunnerBuilder();
    }

    /**
     * Gets the delayed event runner builder
     * 
     * @return The delayed event runner builder
     */
    public static IDelayedEventRunnerBuilder getDelayedEventRunnerBuilder() {
        return getInstance().getDelayedEventRunnerBuilder();
    }

    /**
     * Gets the system clipboard
     * 
     * @return The system clipboard
     */
    public static IClipboard getClipboard() {
        return getInstance().getClipboard();
    }

    /**
     * Creates a transferable builder
     * 
     * @return a transferable builder
     */
    public static ITransferableBuilder createTransferableBuilder() {
        return getInstance().createTransferableBuilder();
    }

    /**
     * Gets the widget utils
     * 
     * @return The widget utils
     */
    public static IWidgetUtils getWidgetUtils() {
        return getInstance().getWidgetUtils();
    }

    /**
     * Gets the active window
     * 
     * @return The active window or null, if no active window exists
     */
    public static IWindow getActiveWindow() {
        return getInstance().getActiveWindow();
    }

    /**
     * Gets a list of all windows
     * 
     * @return A list of all windows
     */
    public static List<IWindow> getAllWindows() {
        return getInstance().getAllWindows();
    }

    /**
     * Sets a value for a typed key for the toolkit
     * 
     * @param <VALUE_TYPE> The type of the value
     * @param key The key
     * @param value The value to set, may be null
     */
    public static <VALUE_TYPE> void setValue(final ITypedKey<VALUE_TYPE> key, final VALUE_TYPE value) {
        getInstance().setValue(key, value);
    }

    /**
     * Gets a value for a typed key
     * 
     * @param <VALUE_TYPE> The type of the resulting value
     * @param key The key to get the value for
     * 
     * @return The value for the key, may be null
     */
    public static <VALUE_TYPE> VALUE_TYPE getValue(final ITypedKey<VALUE_TYPE> key) {
        return getInstance().getValue(key);
    }

    /**
     * Transforms a local component position to a screen position
     * 
     * @param localPosition Local position relative to the component
     * @param component The component
     * @return screen position
     */
    public static Position toScreen(final Position localPosition, final IComponent component) {
        return getInstance().toScreen(localPosition, component);
    }

    /**
     * Transforms a screen position to a local component position
     * 
     * @param screenPosition Screen position
     * @param component The component
     * @return local position relative to the component
     */
    public static Position toLocal(final Position screenPosition, final IComponent component) {
        return getInstance().toLocal(screenPosition, component);
    }

    /**
     * Gets the supported widgets information
     * 
     * @return The supported widgets information
     */
    public static ISupportedWidgets getSupportedWidgets() {
        return getInstance().getSupportedWidgets();
    }

    /**
     * Checks if the underlying spi implementation has mig layout support
     * 
     * @return True if native mig layout is supported, false otherwise
     */
    public static boolean hasSpiMigLayoutSupport() {
        return getInstance().hasSpiMigLayoutSupport();
    }

}
