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

package org.jowidgets.addons.widgets.mediaplayer.impl.ole;

final class EventIds {

	static final int OPEN_STATE_CHANGE = 0x00001389;//void OpenStateChange(int NewState);
	static final int PLAY_STATE_CHANGE = 0x000013ED;//void PlayStateChange(int NewState);
	static final int POSITION_CHANGE = 0x00001452;//void PositionChange(double oldPosition, double newPosition);
	static final int END_OF_STREAM = 0x00001451;// void EndOfStream(int Result);

	static final int STATUS_CHANGE = 0x0000138A;//void StatusChange(); 
	static final int MEDIA_CHANGE = 0x000016AA;// void MediaChange(IDispatch Item);
	static final int CURRENT_MEDIA_ITEM_AVAILABLE = 0x000016AB;// void CurrentMediaItemAvailable(wchar* bstrItemName);
	static final int MARKER_HIT = 0x00001453;// void MarkerHit(int MarkerNum);
	static final int DURATION_UNIT_CHANGE = 0x00001454;//void DurationUnitChange(int NewDurationUnit); 
	static final int AUDIO_LANGUAGE_CHANGE = 0x000013EE;//void AudioLanguageChange(int LangID);

	static final int ERROR = 0x0000157D;//void Error();
	static final int MEDIA_ERROR = 0x000016BD;//void MediaError(IDispatch pMediaObject);
	static final int WARNING = 0x000015E1;//void Warning(final int WarningType, final int Param, wchar* Description);

	private EventIds() {}

}
