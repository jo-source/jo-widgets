/*
 * Copyright (c) 2004, Mikael Grev, MiG InfoCom AB. (miglayout (at) miginfocom (dot) com), 
 * modifications by Nikolaus Moll
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * Neither the name of the MiG InfoCom AB nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 */
package org.jowidgets.impl.layout.miglayout.common;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 */
public final class LinkHandler {
	public static final int X = 0;
	public static final int Y = 1;
	public static final int WIDTH = 2;
	public static final int HEIGHT = 3;
	public static final int X2 = 4;
	public static final int Y2 = 5;

	private final ArrayList<WeakReference<Object>> layouts = new ArrayList<WeakReference<Object>>(4);
	private final ArrayList<HashMap<String, int[]>> valuesMap = new ArrayList<HashMap<String, int[]>>(4);
	private final ArrayList<HashMap<String, int[]>> valuesTemp = new ArrayList<HashMap<String, int[]>>(4);

	public LinkHandler() {}

	public synchronized Integer getValue(final Object layout, final String key, final int type) {
		Integer ret = null;
		boolean cont = true;

		for (int i = layouts.size() - 1; i >= 0; i--) {
			final Object l = layouts.get(i).get();
			if (ret == null && l == layout) {
				int[] rect = valuesTemp.get(i).get(key);
				if (cont && rect != null && rect[type] != LayoutUtil.NOT_SET) {
					ret = Integer.valueOf(rect[type]);
				}
				else {
					rect = valuesMap.get(i).get(key);
					ret = (rect != null && rect[type] != LayoutUtil.NOT_SET) ? Integer.valueOf(rect[type]) : null;
				}
				cont = false;
			}

			if (l == null) {
				layouts.remove(i);
				valuesMap.remove(i);
				valuesTemp.remove(i);
			}
		}
		return ret;
	}

	/**
	 * Sets a key that can be linked to from any component.
	 * 
	 * @param layout The MigLayout instance
	 * @param key The key to link to. This is the same as the ID in a component constraint.
	 * @param x x
	 * @param y y
	 * @param width Width
	 * @param height Height
	 * @return If the value was changed
	 */
	public synchronized boolean setBounds(
		final Object layout,
		final String key,
		final int x,
		final int y,
		final int width,
		final int height) {
		return setBounds(layout, key, x, y, width, height, false, false);
	}

	synchronized boolean setBounds(
		final Object layout,
		final String key,
		final int x,
		final int y,
		final int width,
		final int height,
		final boolean temporary,
		final boolean incCur) {
		for (int i = layouts.size() - 1; i >= 0; i--) {
			final Object l = layouts.get(i).get();
			if (l != layout) {
				continue;
			}

			final HashMap<String, int[]> map = (temporary ? valuesTemp : valuesMap).get(i);
			final int[] old = map.get(key);

			if (old == null || old[X] != x || old[Y] != y || old[WIDTH] != width || old[HEIGHT] != height) {
				if (old == null || incCur == false) {
					map.put(key, new int[] {x, y, width, height, x + width, y + height});
					return true;
				}

				boolean changed = false;

				if (x != LayoutUtil.NOT_SET) {
					if (old[X] == LayoutUtil.NOT_SET || x < old[X]) {
						old[X] = x;
						old[WIDTH] = old[X2] - x;
						changed = true;
					}

					if (width != LayoutUtil.NOT_SET) {
						final int x2 = x + width;
						if (old[X2] == LayoutUtil.NOT_SET || x2 > old[X2]) {
							old[X2] = x2;
							old[WIDTH] = x2 - old[X];
							changed = true;
						}
					}
				}

				if (y != LayoutUtil.NOT_SET) {
					if (old[Y] == LayoutUtil.NOT_SET || y < old[Y]) {
						old[Y] = y;
						old[HEIGHT] = old[Y2] - y;
						changed = true;
					}

					if (height != LayoutUtil.NOT_SET) {
						final int y2 = y + height;
						if (old[Y2] == LayoutUtil.NOT_SET || y2 > old[Y2]) {
							old[Y2] = y2;
							old[HEIGHT] = y2 - old[Y];
							changed = true;
						}
					}
				}
				return changed;
			}
			return false;
		}

		layouts.add(new WeakReference<Object>(layout));
		final int[] bounds = new int[] {x, y, width, height, x + width, y + height};

		HashMap<String, int[]> values = new HashMap<String, int[]>(4);
		if (temporary) {
			values.put(key, bounds);
		}
		valuesTemp.add(values);

		values = new HashMap<String, int[]>(4);
		if (temporary == false) {
			values.put(key, bounds);
		}
		valuesMap.add(values);

		return true;
	}

	/**
	 * This method clear any weak references right away instead of waiting for the GC. This might be advantageous
	 * if lots of layout are created and disposed of quickly to keep memory consumption down.
	 * 
	 * @since 3.7.4
	 */
	public synchronized void clearWeakReferencesNow() {
		layouts.clear();
	}

	public synchronized boolean clearBounds(final Object layout, final String key) {
		for (int i = layouts.size() - 1; i >= 0; i--) {
			final Object l = layouts.get(i).get();
			if (l == layout) {
				return valuesMap.get(i).remove(key) != null;
			}
		}
		return false;
	}

	synchronized void clearTemporaryBounds(final Object layout) {
		for (int i = layouts.size() - 1; i >= 0; i--) {
			final Object l = layouts.get(i).get();
			if (l == layout) {
				valuesTemp.get(i).clear();
				return;
			}
		}
	}
}
