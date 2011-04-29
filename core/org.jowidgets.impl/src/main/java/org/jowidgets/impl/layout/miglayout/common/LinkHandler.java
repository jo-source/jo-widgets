//CHECKSTYLE:OFF

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

	private static final ArrayList<WeakReference<Object>> LAYOUTS = new ArrayList<WeakReference<Object>>(4);
	private static final ArrayList<HashMap<String, int[]>> VALUES = new ArrayList<HashMap<String, int[]>>(4);
	private static final ArrayList<HashMap<String, int[]>> VALUES_TEMP = new ArrayList<HashMap<String, int[]>>(4);

	private LinkHandler() {}

	public synchronized static Integer getValue(final Object layout, final String key, final int type) {
		Integer ret = null;
		boolean cont = true;

		for (int i = LAYOUTS.size() - 1; i >= 0; i--) {
			final Object l = LAYOUTS.get(i).get();
			if (ret == null && l == layout) {
				int[] rect = VALUES_TEMP.get(i).get(key);
				if (cont && rect != null && rect[type] != LayoutUtil.NOT_SET) {
					ret = Integer.valueOf(rect[type]);
				}
				else {
					rect = VALUES.get(i).get(key);
					ret = (rect != null && rect[type] != LayoutUtil.NOT_SET) ? Integer.valueOf(rect[type]) : null;
				}
				cont = false;
			}

			if (l == null) {
				LAYOUTS.remove(i);
				VALUES.remove(i);
				VALUES_TEMP.remove(i);
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
	public synchronized static boolean setBounds(
		final Object layout,
		final String key,
		final int x,
		final int y,
		final int width,
		final int height) {
		return setBounds(layout, key, x, y, width, height, false, false);
	}

	synchronized static boolean setBounds(
		final Object layout,
		final String key,
		final int x,
		final int y,
		final int width,
		final int height,
		final boolean temporary,
		final boolean incCur) {
		for (int i = LAYOUTS.size() - 1; i >= 0; i--) {
			final Object l = LAYOUTS.get(i).get();
			if (l == layout) {
				final HashMap<String, int[]> map = (temporary ? VALUES_TEMP : VALUES).get(i);
				final int[] old = map.get(key);

				if (old == null || old[X] != x || old[Y] != y || old[WIDTH] != width || old[HEIGHT] != height) {
					if (old == null || incCur == false) {
						map.put(key, new int[] {x, y, width, height, x + width, y + height});
						return true;
					}
					else {
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
				}
				return false;
			}
		}

		LAYOUTS.add(new WeakReference<Object>(layout));
		final int[] bounds = new int[] {x, y, width, height, x + width, y + height};

		HashMap<String, int[]> values = new HashMap<String, int[]>(4);
		if (temporary)
			values.put(key, bounds);
		VALUES_TEMP.add(values);

		values = new HashMap<String, int[]>(4);
		if (temporary == false)
			values.put(key, bounds);
		VALUES.add(values);

		return true;
	}

	/**
	 * This method clear any weak references right away instead of waiting for the GC. This might be advantageous
	 * if lots of layout are created and disposed of quickly to keep memory consumption down.
	 * 
	 * @since 3.7.4
	 */
	public synchronized static void clearWeakReferencesNow() {
		LAYOUTS.clear();
	}

	public synchronized static boolean clearBounds(final Object layout, final String key) {
		for (int i = LAYOUTS.size() - 1; i >= 0; i--) {
			final Object l = LAYOUTS.get(i).get();
			if (l == layout)
				return VALUES.get(i).remove(key) != null;
		}
		return false;
	}

	synchronized static void clearTemporaryBounds(final Object layout) {
		for (int i = LAYOUTS.size() - 1; i >= 0; i--) {
			final Object l = LAYOUTS.get(i).get();
			if (l == layout) {
				VALUES_TEMP.get(i).clear();
				return;
			}
		}
	}
}
