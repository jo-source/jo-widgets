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
package org.jowidgets.spi.impl.swt.common.widgets;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;
import org.jowidgets.spi.widgets.ISubMenuSpi;

public class SubMenuImpl extends SwtMenu implements ISubMenuSpi, IToolTipTextProvider {

    private final MenuItemImpl menuItemDelegate;

    public SubMenuImpl(final MenuItem menuItem, final Menu menu, final SwtImageRegistry imageRegistry) {
        super(menu, imageRegistry);
        this.menuItemDelegate = new MenuItemImpl(menuItem, imageRegistry);
    }

    @Override
    public Menu getUiReference() {
        return super.getUiReference();
    }

    @Override
    public void setText(final String text) {
        menuItemDelegate.setText(text);
    }

    @Override
    public String getToolTipText() {
        return menuItemDelegate.getToolTipText();
    }

    @Override
    public void setToolTipText(final String text) {
        menuItemDelegate.setToolTipText(text);
    }

    @Override
    public void setIcon(final IImageConstant icon) {
        menuItemDelegate.setIcon(icon);
    }

    @Override
    public void setMnemonic(final char mnemonic) {
        menuItemDelegate.setMnemonic(mnemonic);
    }

}
