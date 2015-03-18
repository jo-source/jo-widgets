/*
 * Copyright (c) 2011, nimoll
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

package org.jowidgets.impl.layout.miglayout;

import org.jowidgets.api.layout.miglayout.IAC;
import org.jowidgets.api.layout.miglayout.ICC;
import org.jowidgets.api.layout.miglayout.ILC;
import org.jowidgets.api.layout.miglayout.IMigLayoutToolkit;
import org.jowidgets.api.layout.miglayout.IPlatformDefaults;
import org.jowidgets.api.toolkit.Toolkit;

public final class MigLayoutToolkitImpl implements IMigLayoutToolkit {

    private LayoutUtilCommon layoutUtil;
    private PlatformDefaultsCommon platformDefaults;
    private UnitValueToolkitCommon unitValueToolkit;
    private LinkHandlerCommon linkHandler;

    public MigLayoutToolkitImpl() {}

    private static MigLayoutToolkitImpl getToolkit() {
        return (MigLayoutToolkitImpl) Toolkit.getLayoutFactoryProvider().getMigLayoutToolkit();
    }

    static LayoutUtilCommon getMigLayoutUtil() {
        final MigLayoutToolkitImpl toolkit = getToolkit();
        if (toolkit.layoutUtil == null) {
            toolkit.layoutUtil = new LayoutUtilCommon();
        }
        return toolkit.layoutUtil;
    }

    static PlatformDefaultsCommon getMigPlatformDefaults() {
        final MigLayoutToolkitImpl toolkit = getToolkit();
        if (toolkit.platformDefaults == null) {
            toolkit.platformDefaults = new PlatformDefaultsCommon();
        }
        return toolkit.platformDefaults;
    }

    static UnitValueToolkitCommon getMigUnitValueToolkit() {
        final MigLayoutToolkitImpl toolkit = getToolkit();
        if (toolkit.unitValueToolkit == null) {
            toolkit.unitValueToolkit = new UnitValueToolkitCommon();
        }
        return toolkit.unitValueToolkit;
    }

    static LinkHandlerCommon getMigLinkHandler() {
        final MigLayoutToolkitImpl toolkit = getToolkit();
        if (toolkit.linkHandler == null) {
            toolkit.linkHandler = new LinkHandlerCommon();
        }
        return toolkit.linkHandler;
    }

    @Override
    public IAC columnConstraints() {
        return ac();
    }

    @Override
    public IAC rowConstraints() {
        return ac();
    }

    @Override
    public ILC layoutConstraints() {
        return lc();
    }

    @Override
    public ICC componentConstraints() {
        return cc();
    }

    @Override
    public ICC cc() {
        return new CCWrapper();
    }

    @Override
    public IAC ac() {
        return new ACWrapper();
    }

    @Override
    public ILC lc() {
        return new LCWrapper();
    }

    @Override
    public IPlatformDefaults getPlatformDefaults() {
        return getMigPlatformDefaults();
    }

    @Override
    public String getMigLayoutVersion() {
        return getMigLayoutUtil().getVersion();
    }

}
