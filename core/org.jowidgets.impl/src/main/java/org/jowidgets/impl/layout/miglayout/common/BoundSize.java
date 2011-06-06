//CHECKSTYLE:OFF

package org.jowidgets.impl.layout.miglayout.common;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

import org.jowidgets.impl.layout.miglayout.MigLayoutToolkit;

/**
 * A size that contains minimum, preferred and maximum size of type {@link UnitValue}.
 * <p>
 * This class is a simple value container and it is immutable.
 * <p>
 * If a size is missing (i.e., <code>null</code>) that boundary should be considered "not in use".
 * <p>
 * You can create a BoundSize from a String with the use of {@link ConstraintParser#parseBoundSize(String, boolean, boolean)}
 */
public class BoundSize implements Serializable {
	public static final BoundSize NULL_SIZE = new BoundSize(null, null);
	public static final BoundSize ZERO_PIXEL = new BoundSize(MigLayoutToolkit.getUnitValueToolkit().ZERO, "0px");

	private final transient UnitValue min;
	private final transient UnitValue pref;
	private final transient UnitValue max;
	private final transient boolean gapPush;

	private final LayoutUtil layoutUtil;

	/**
	 * Constructor that use the same value for min/preferred/max size.
	 * 
	 * @param minMaxPref The value to use for min/preferred/max size.
	 * @param createString The string used to create the BoundsSize.
	 */
	public BoundSize(final UnitValue minMaxPref, final String createString) {
		this(minMaxPref, minMaxPref, minMaxPref, createString);
	}

	/**
	 * Constructor. <b>This method is here for serilization only and should normally not be used. Use
	 * {@link ConstraintParser#parseBoundSize(String, boolean, boolean)} instead.
	 * 
	 * @param min The minimum size. May be <code>null</code>.
	 * @param preferred The preferred size. May be <code>null</code>.
	 * @param max The maximum size. May be <code>null</code>.
	 * @param createString The string used to create the BoundsSize.
	 */
	public BoundSize(final UnitValue min, final UnitValue preferred, final UnitValue max, final String createString) // Bound to old delegate!!!!!
	{
		this(min, preferred, max, false, createString);
	}

	/**
	 * Constructor. <b>This method is here for serilization only and should normally not be used. Use
	 * {@link ConstraintParser#parseBoundSize(String, boolean, boolean)} instead.
	 * 
	 * @param min The minimum size. May be <code>null</code>.
	 * @param preferred The preferred size. May be <code>null</code>.
	 * @param max The maximum size. May be <code>null</code>.
	 * @param gapPush If the size should be hinted as "pushing" and thus want to occupy free space if no one else is claiming it.
	 * @param createString The string used to create the BoundsSize.
	 */
	public BoundSize(
		final UnitValue min,
		final UnitValue preferred,
		final UnitValue max,
		final boolean gapPush,
		final String createString) {
		this.layoutUtil = MigLayoutToolkit.getLayoutUtil();

		this.min = min;
		this.pref = preferred;
		this.max = max;
		this.gapPush = gapPush;

		layoutUtil.putCCString(this, createString); // this escapes!!
	}

	/**
	 * Returns the minimum size as sent into the constructor.
	 * 
	 * @return The minimum size as sent into the constructor. May be <code>null</code>.
	 */
	public final UnitValue getMin() {
		return min;
	}

	/**
	 * Returns the preferred size as sent into the constructor.
	 * 
	 * @return The preferred size as sent into the constructor. May be <code>null</code>.
	 */
	public final UnitValue getPreferred() {
		return pref;
	}

	/**
	 * Returns the maximum size as sent into the constructor.
	 * 
	 * @return The maximum size as sent into the constructor. May be <code>null</code>.
	 */
	public final UnitValue getMax() {
		return max;
	}

	/**
	 * If the size should be hinted as "pushing" and thus want to occupy free space if noone else is claiming it.
	 * 
	 * @return The value.
	 */
	public boolean getGapPush() {
		return gapPush;
	}

	/**
	 * Returns if this bound size has no min, preferred and maximum size set (they are all <code>null</code>)
	 * 
	 * @return If unset.
	 */
	public boolean isUnset() {
		// Most common case by far is this == ZERO_PIXEL...
		return this == ZERO_PIXEL || (pref == null && min == null && max == null && gapPush == false);
	}

	/**
	 * Makes sure that <code>size</code> is within min and max of this size.
	 * 
	 * @param size The size to constrain.
	 * @param refValue The reference to use for relative sizes.
	 * @param parent The parent container.
	 * @return The size, constrained within min and max.
	 */
	public int constrain(int size, final float refValue, final ContainerWrapper parent) {
		if (max != null)
			size = Math.min(size, max.getPixels(refValue, parent, parent));
		if (min != null)
			size = Math.max(size, min.getPixels(refValue, parent, parent));
		return size;
	}

	/**
	 * Returns the minimum, preferred or maximum size for this bounded size.
	 * 
	 * @param sizeType The type. <code>LayoutUtil.MIN</code>, <code>LayoutUtil.PREF</code> or <code>LayoutUtil.MAX</code>.
	 * @return
	 */
	final UnitValue getSize(final int sizeType) {
		if (sizeType == LayoutUtil.MIN) {
			return min;
		}
		else if (sizeType == LayoutUtil.PREF) {
			return pref;
		}
		else if (sizeType == LayoutUtil.MAX) {
			return max;
		}
		else {
			throw new IllegalArgumentException("Unknown size: " + sizeType);
		}
	}

	/**
	 * Convert the bound sizes to pixels.
	 * <p>
	 * <code>null</code> bound sizes will be 0 for min and preferred and {@link net.miginfocom.layout.LayoutUtil#INF} for max.
	 * 
	 * @param refSize The reference size.
	 * @param parent The parent. Not <code>null</code>.
	 * @param comp The component, if applicable, can be <code>null</code>.
	 * @return An array of lenth three (min,pref,max).
	 */
	final int[] getPixelSizes(final float refSize, final ContainerWrapper parent, final ComponentWrapper comp) {
		return new int[] {
				min != null ? min.getPixels(refSize, parent, comp) : 0, pref != null ? pref.getPixels(refSize, parent, comp) : 0,
				max != null ? max.getPixels(refSize, parent, comp) : LayoutUtil.INF};
	}

	/**
	 * Returns the a constraint string that can be re-parsed to be the exact same UnitValue.
	 * 
	 * @return A String. Never <code>null</code>.
	 */
	String getConstraintString() {
		final String cs = layoutUtil.getCCString(this);
		if (cs != null)
			return cs;

		if (min == pref && pref == max)
			return min != null ? (min.getConstraintString() + "!") : "null";

		final StringBuilder sb = new StringBuilder(16);

		if (min != null)
			sb.append(min.getConstraintString()).append(':');

		if (pref != null) {
			if (min == null && max != null)
				sb.append(":");
			sb.append(pref.getConstraintString());
		}
		else if (min != null) {
			sb.append('n');
		}

		if (max != null)
			sb.append(sb.length() == 0 ? "::" : ":").append(max.getConstraintString());

		if (gapPush) {
			if (sb.length() > 0)
				sb.append(':');
			sb.append("push");
		}

		return sb.toString();
	}

	void checkNotLinked() {
		if (min != null && min.isLinkedDeep() || pref != null && pref.isLinkedDeep() || max != null && max.isLinkedDeep())
			throw new IllegalArgumentException("Size may not contain links");
	}

	static {
		final LayoutUtil lUtil = MigLayoutToolkit.getLayoutUtil();
		lUtil.setDelegate(BoundSize.class, new PersistenceDelegate() {
			@Override
			protected Expression instantiate(final Object oldInstance, final Encoder out) {
				final BoundSize bs = (BoundSize) oldInstance;
				if (Grid.TEST_GAPS) {
					return new Expression(oldInstance, BoundSize.class, "new", new Object[] {
							bs.getMin(), bs.getPreferred(), bs.getMax(), bs.getGapPush(), bs.getConstraintString()});
				}
				else {
					return new Expression(oldInstance, BoundSize.class, "new", new Object[] {
							bs.getMin(), bs.getPreferred(), bs.getMax(), bs.getConstraintString()});
				}
			}
		});
	}

	// ************************************************
	// Persistence Delegate and Serializable combined.
	// ************************************************

	private static final long serialVersionUID = 1L;

	protected Object readResolve() throws ObjectStreamException {
		return layoutUtil.getSerializedObject(this);
	}

	private void writeObject(final ObjectOutputStream out) throws IOException {
		if (getClass() == BoundSize.class) {
			layoutUtil.writeAsXML(out, this);
		}
	}

	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		layoutUtil.setSerializedObject(this, layoutUtil.readAsXML(in));
	}
}
