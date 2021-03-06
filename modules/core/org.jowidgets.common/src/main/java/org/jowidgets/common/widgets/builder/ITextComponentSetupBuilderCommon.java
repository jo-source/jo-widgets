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
package org.jowidgets.common.widgets.builder;

import java.util.List;

import org.jowidgets.common.verify.IInputVerifier;

public interface ITextComponentSetupBuilderCommon<INSTANCE_TYPE extends ITextComponentSetupBuilderCommon<?>> extends
        IComponentSetupBuilderCommon<INSTANCE_TYPE>,
        IInputObservableSetupBuilderCommon<INSTANCE_TYPE> {

    INSTANCE_TYPE setInputVerifier(IInputVerifier inputVerifier);

    /**
     * Sets the regular expressions that describes the valid inputs. All regular expressions will be logical
     * connected with the AND operator.
     * 
     * REMARK: Implementors should prefer to use regular expressions instead of {@link IInputVerifier}'s
     * if possible because SPI implementations that uses e.g 'AJAX' can implement client side verification easier.
     * 
     * REMARK: Implementors must not assume that the regular expression will be used on all platforms
     */
    INSTANCE_TYPE setAcceptingRegExps(List<String> regExp);

    INSTANCE_TYPE setMaxLength(Integer maxLength);

}
