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

package org.jowidgets.tools.powo;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IActionMenuItem;
import org.jowidgets.api.widgets.blueprint.IActionMenuItemBluePrint;
import org.jowidgets.api.widgets.descriptor.IActionMenuItemDescriptor;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;

/**
 * @deprecated The idea of POWO's (Plain Old Widget Object's) has not been established.
 *             For that, POWO's will no longer be supported and may removed completely in middle term.
 *             Feel free to move them to your own open source project.
 */
@Deprecated
public class JoActionMenuItem extends ActionMenuItem<IActionMenuItem, IActionMenuItemBluePrint> implements IActionMenuItem {

    public JoActionMenuItem(final String text, final IImageConstant icon) {
        this(bluePrint(text, icon));
    }

    public JoActionMenuItem(final IAction action) {
        this(bluePrint());
        Assert.paramNotNull(action, "action");
        setAction(action);
    }

    public JoActionMenuItem(final String text) {
        this(bluePrint(text));
    }

    public JoActionMenuItem(final String text, final String tooltipText) {
        this(bluePrint(text, tooltipText));
    }

    public JoActionMenuItem(final IActionMenuItemDescriptor descriptor) {
        super(bluePrint().setSetup(descriptor));
    }

    public static IActionMenuItemBluePrint bluePrint() {
        return Toolkit.getBluePrintFactory().menuItem();
    }

    public static IActionMenuItemBluePrint bluePrint(final String text) {
        return Toolkit.getBluePrintFactory().menuItem(text);
    }

    public static IActionMenuItemBluePrint bluePrint(final String text, final String tooltipText) {
        return Toolkit.getBluePrintFactory().menuItem(text).setToolTipText(tooltipText);
    }

    public static IActionMenuItemBluePrint bluePrint(final String text, final IImageConstant icon) {
        return Toolkit.getBluePrintFactory().menuItem(text).setIcon(icon);
    }

}
