/*
 * Copyright (c) 2016, MGrossmann
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

package org.jowidgets.util;

/**
 * Implementation of system time provider that can be used for e.g. for JUnit tests.
 * 
 * The implementation increments a given start system time (default is 0) each time the
 * currentTimeMillis() method will be invoked (if invocationCountToChangeTime=1, default).
 */
public final class DummySystemTimeProvider implements ISystemTimeProvider {

    public static final long DEFAULT_START_TIME = 0;

    private final long period;
    private final int invocationCountToChangeTime;

    private long currentTime;
    private int invocationCount;

    /**
     * Creates a new DummySystemTimeProvider with default start time
     * 
     * @param startTime The start time of the provider
     * @param period The period the time will be increased after each invocation of {@link #currentTimeMillis()}
     */
    public DummySystemTimeProvider(final long period) {
        this(DEFAULT_START_TIME, period, 1);
    }

    /**
     * Creates a new DummySystemTimeProvider with default start time
     * 
     * @param period The period the time will be increased after invocationCountToChangeTime invocations of
     *            {@link #currentTimeMillis()}
     * @param invocationCountToChangeTime The number of invocations necessary to change the time by adding one period
     */
    public DummySystemTimeProvider(final long period, final int invocationCountToChangeTime) {
        this(DEFAULT_START_TIME, period, invocationCountToChangeTime);
    }

    /**
     * Creates a new DummySystemTimeProvider
     * 
     * @param startTime The start time of the provider
     * @param period The period the time will be increased after invocationCountToChangeTime invocations of
     *            {@link #currentTimeMillis()}
     * @param invocationCountToChangeTime The number of invocations necessary to change the time by adding one period
     */
    public DummySystemTimeProvider(final long startTime, final long period, final int invocationCountToChangeTime) {
        currentTime = DEFAULT_START_TIME;
        this.invocationCountToChangeTime = invocationCountToChangeTime;
        this.period = period;
        this.invocationCount = 0;
    }

    @Override
    public long currentTimeMillis() {
        final long result = currentTime;
        if (invocationCount % invocationCountToChangeTime == 0) {
            currentTime = currentTime + period;
        }
        invocationCount++;
        return result;
    }

    /**
     * Gets the last system time without counting up the time
     * 
     * @return The last system time
     */
    public long getLastCurrentTime() {
        return currentTime;
    }
}
