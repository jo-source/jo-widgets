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

package org.jowidgets.spi.impl.dummy;

import java.util.Collections;
import java.util.List;

import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.threads.IUiThreadAccessCommon;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.spi.IOptionalWidgetsFactorySpi;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.IWidgetsServiceProvider;
import org.jowidgets.spi.clipboard.IClipboardSpi;
import org.jowidgets.spi.image.IImageHandleFactorySpi;
import org.jowidgets.spi.impl.clipboard.ApplicationLocalClipboard;
import org.jowidgets.spi.impl.dummy.application.DummyUiThreadAccess;
import org.jowidgets.spi.impl.dummy.application.EventQueueApplicationRunner;
import org.jowidgets.spi.impl.dummy.application.SingleThreadApplicationRunner;
import org.jowidgets.spi.impl.dummy.application.SingleThreadUiThreadAccess;
import org.jowidgets.spi.impl.dummy.application.UiThreadPolicy;
import org.jowidgets.spi.impl.dummy.image.DummyImageFactory;
import org.jowidgets.spi.impl.dummy.image.DummyImageHandleFactory;
import org.jowidgets.spi.impl.dummy.image.DummyImageHandleFactorySpi;
import org.jowidgets.spi.impl.dummy.image.DummyImageRegistry;
import org.jowidgets.util.Assert;

public class DummyWidgetsServiceProvider implements IWidgetsServiceProvider {

    private final DummyImageHandleFactorySpi imageHandleFactorySpi;
    private final DummyImageRegistry imageRegistry;
    private final DummyImageFactory imageFactory;
    private final DummyWidgetFactory widgetFactory;
    private final DummyOptionalWidgetsFactory optionalWidgetsFactory;
    private final IClipboardSpi clipboard;
    private final IUiThreadAccessCommon uiThreadAccess;
    private final IApplicationRunner applicationRunner;

    public DummyWidgetsServiceProvider() {
        this(UiThreadPolicy.SINGLE_THREAD);
    }

    public DummyWidgetsServiceProvider(final UiThreadPolicy uiThreadPolicy) {
        Assert.paramNotNull(uiThreadPolicy, "uiThreadPolicy");

        this.imageRegistry = new DummyImageRegistry(new DummyImageHandleFactory());
        this.imageHandleFactorySpi = new DummyImageHandleFactorySpi(imageRegistry);
        this.widgetFactory = new DummyWidgetFactory(imageRegistry);
        this.imageFactory = new DummyImageFactory(imageHandleFactorySpi);
        this.optionalWidgetsFactory = new DummyOptionalWidgetsFactory();
        this.clipboard = new ApplicationLocalClipboard();

        if (UiThreadPolicy.SINGLE_THREAD == uiThreadPolicy) {
            this.uiThreadAccess = new SingleThreadUiThreadAccess();
            this.applicationRunner = new SingleThreadApplicationRunner(uiThreadAccess);
        }
        else if (UiThreadPolicy.EVENT_QUEUE == uiThreadPolicy) {
            final EventQueueApplicationRunner eventQueueApplicationRunner = new EventQueueApplicationRunner();
            this.uiThreadAccess = eventQueueApplicationRunner;
            this.applicationRunner = eventQueueApplicationRunner;
        }
        else if (UiThreadPolicy.DUMMY == uiThreadPolicy) {
            this.uiThreadAccess = new DummyUiThreadAccess();
            this.applicationRunner = new SingleThreadApplicationRunner(uiThreadAccess);
        }
        else {
            throw new IllegalArgumentException("The thread policy '" + uiThreadPolicy + "' is not supported");
        }
    }

    @Override
    public IImageRegistry getImageRegistry() {
        return imageRegistry;
    }

    @Override
    public IImageHandleFactorySpi getImageHandleFactory() {
        return imageHandleFactorySpi;
    }

    @Override
    public DummyImageFactory getImageFactory() {
        return imageFactory;
    }

    @Override
    public IWidgetFactorySpi getWidgetFactory() {
        return widgetFactory;
    }

    @Override
    public IOptionalWidgetsFactorySpi getOptionalWidgetFactory() {
        return optionalWidgetsFactory;
    }

    @Override
    public IUiThreadAccessCommon createUiThreadAccess() {
        return uiThreadAccess;
    }

    @Override
    public IApplicationRunner createApplicationRunner() {
        return applicationRunner;
    }

    @Override
    public Object getActiveWindowUiReference() {
        //TODO must be implemented
        return null;
    }

    @Override
    public List<Object> getAllWindowsUiReference() {
        return Collections.emptyList();
    }

    @Override
    public Position toScreen(final Position localPosition, final IComponentCommon component) {
        //TODO must be implemented
        return null;
    }

    @Override
    public Position toLocal(final Position screenPosition, final IComponentCommon component) {
        //TODO must be implemented
        return null;
    }

    @Override
    public IClipboardSpi getClipboard() {
        return clipboard;
    }

}
