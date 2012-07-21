/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.spi.impl.bridge.swt.awt.common.awt;

import org.eclipse.swt.widgets.Composite;
import org.jowidgets.spi.widgets.IControlSpi;
import org.jowidgets.util.IMutableValue;

public interface IAwtSwtControlSpi extends IControlSpi {

	/**
	 * The swt composite is a mutable value.
	 * 
	 * Remark: Users of this control must considers the following behavior :
	 * 
	 * The initial value may be null and mutates later to a composite, because the swt awt bridge needs a peer
	 * to work. This peer will eventually be created e.g. at pack or set visible of any parent.
	 * 
	 * The implemented AwtSwtControl may be disposed, if any parent ancestor swing panel will be removed from its parent.
	 * Then the mutable value mutates (changes) to null.
	 * 
	 * If the holding swing panel will be reattached to another parent,
	 * the mutable value mutates back to a NEW composite instance.
	 * 
	 * @return The mutable swt composite
	 */
	IMutableValue<Composite> getSwtComposite();

	void dispose();

}
