/*
 * Copyright (c) 2015, grossmann
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

package org.jowidgets.api.layout.miglayout;

import org.jowidgets.api.toolkit.Toolkit;

public final class MigLayoutToolkit {

    private MigLayoutToolkit() {}

    /**
     * Gets the Mig Layout toolkit instance
     * 
     * @return The mig layout toolkit instance
     */
    public static IMigLayoutToolkit getInstance() {
        return Toolkit.getLayoutFactoryProvider().getMigLayoutToolkit();
    }

    /**
     * Creates column constraints
     * 
     * @return New column constraints
     */
    public static IAC columnConstraints() {
        return getInstance().columnConstraints();
    }

    /**
     * Creates row constraints
     * 
     * @return New row constraints
     */
    public static IAC rowConstraints() {
        return getInstance().rowConstraints();
    }

    /**
     * Creates layout constraints
     * 
     * @return New layout constraints
     */
    public static ILC layoutConstraints() {
        return getInstance().layoutConstraints();
    }

    /**
     * Creates component constraints
     * 
     * @return New component constraints
     */
    public static ICC componentConstraints() {
        return getInstance().componentConstraints();
    }

    /**
     * Creates component constraints
     * 
     * @return New component constraints
     */
    public static ICC cc() {
        return getInstance().cc();
    }

    /**
     * Creates row / column constraints
     * 
     * @return New row / column constraints
     */
    public static IAC ac() {
        return getInstance().ac();
    }

    /**
     * Creates layout constraints
     * 
     * @return New layout constraints
     */
    public static ILC lc() {
        return getInstance().lc();
    }

    /**
     * Gets the platform defaults
     * 
     * @return The platform defaults, never null
     */
    public static IPlatformDefaults getPlatformDefaults() {
        return getInstance().getPlatformDefaults();
    }

    /**
     * Gets the mig layout version
     * 
     * @return The mig layout version
     */
    public static String getMigLayoutVersion() {
        return getInstance().getMigLayoutVersion();
    }

}
