//CHECKSTYLE:OFF

package org.jowidgets.impl.layout.miglayout.common;

import java.util.HashMap;

import org.jowidgets.impl.layout.miglayout.MigLayoutToolkit;

/**
 * This class contains static methods to be used by IDE vendors to convert to and from String/API constraints.
 * <p>
 * <b>Note that {@link LayoutUtil#setDesignTime(ContainerWrapper, boolean)} should be set to <code>true</code> for this class'
 * methods to work.</b>
 */
public class IDEUtil {
	/**
	 * A direct reference to the corresponding value for predefined UnitValues in {@link UnitValue}.
	 */
	public final UnitValue ZERO = MigLayoutToolkit.getUnitValueToolkit().ZERO;

	/**
	 * A direct reference to the corresponding value for predefined UnitValues in {@link UnitValue}.
	 */
	public final UnitValue TOP = MigLayoutToolkit.getUnitValueToolkit().TOP;

	/**
	 * A direct reference to the corresponding value for predefined UnitValues in {@link UnitValue}.
	 */
	public final UnitValue LEADING = MigLayoutToolkit.getUnitValueToolkit().LEADING;

	/**
	 * A direct reference to the corresponding value for predefined UnitValues in {@link UnitValue}.
	 */
	public final UnitValue LEFT = MigLayoutToolkit.getUnitValueToolkit().LEFT;

	/**
	 * A direct reference to the corresponding value for predefined UnitValues in {@link UnitValue}.
	 */
	public final UnitValue CENTER = MigLayoutToolkit.getUnitValueToolkit().CENTER;

	/**
	 * A direct reference to the corresponding value for predefined UnitValues in {@link UnitValue}.
	 */
	public final UnitValue TRAILING = MigLayoutToolkit.getUnitValueToolkit().TRAILING;

	/**
	 * A direct reference to the corresponding value for predefined UnitValues in {@link UnitValue}.
	 */
	public final UnitValue RIGHT = MigLayoutToolkit.getUnitValueToolkit().RIGHT;

	/**
	 * A direct reference to the corresponding value for predefined UnitValues in {@link UnitValue}.
	 */
	public final UnitValue BOTTOM = MigLayoutToolkit.getUnitValueToolkit().BOTTOM;

	/**
	 * A direct reference to the corresponding value for predefined UnitValues in {@link UnitValue}.
	 */
	public final UnitValue LABEL = MigLayoutToolkit.getUnitValueToolkit().LABEL;

	/**
	 * A direct reference to the corresponding value for predefined UnitValues in {@link UnitValue}.
	 */
	public final UnitValue INF = MigLayoutToolkit.getUnitValueToolkit().INF;

	/**
	 * A direct reference to the corresponding value for predefined UnitValues in {@link UnitValue}.
	 */
	public final UnitValue BASELINE_IDENTITY = MigLayoutToolkit.getUnitValueToolkit().BASELINE_IDENTITY;

	private final String[] X_Y_STRINGS = new String[] {"x", "y", "x2", "y2"};

	/**
	 * Returns the version of IDEUtil
	 * 
	 * @return The version.
	 */
	public String getIDEUtilVersion() {
		return "1.0";
	}

	/**
	 * Returns the grid cells that the components in <code>parentContainer</code> has.
	 * 
	 * @param parentContainer The parent container. It is an object since MigLayout is GUI toolkit
	 *            independent.
	 * @return A new hashmap with the components mapped to an array [x, y, spanx, spany].
	 *         <p>
	 *         Dock components will always have x and y less than -30000 or more than 30000. This is since they are actually part
	 *         of the grid, but on the outer edges.
	 *         <p>
	 *         Components that span the "rest of the row/column" have really large span values. Actually 30000-x or 30000-y.
	 *         <p>
	 *         Generally, the grid does not need to have the upper left at 0, 0. Though it normally does if you don't set the
	 *         cells explicitly to other values. Rows and columns that are completely empty and that does not have an explicit
	 *         row/column constraint will be totally disregarded.
	 */
	public HashMap<Object, int[]> getGridPositions(final Object parentContainer) {
		return Grid.getGridPositions(parentContainer);
	}

	/**
	 * Returns the sizes of the rows and gaps for a container.
	 * There will be two arrays returned [0] and [1].
	 * <p>
	 * The first array will be the indexes of the rows where indexes that are less than 30000 or larger than 30000 is docking
	 * rows. There might be extra docking rows that aren't visible but they always have size 0. Non docking indexes will probably
	 * always be 0, 1, 2, 3, etc..
	 * <p>
	 * The second array is the sizes of the form:<br>
	 * <code>[left inset][row size 1][gap 1][row size 2][gap 2][row size n][right inset]</code>.
	 * <p>
	 * The returned sizes will be the ones calculated in the last layout cycle.
	 * 
	 * @param parentContainer The container to retuern the row sizes and gaps for. In Swing it will be a
	 *            {@link java.awt.Container} and
	 *            in SWT it will be a {@link org.eclipse.swt.widgets.Composite}.
	 * @return The sizes or <code>null</code> if {@link LayoutUtil#isDesignTime(ContainerWrapper)} is <code>false</code> or
	 *         <code>parentContainer</code> does not have a MigLayout layout manager.
	 *         The returned sizes will be the ones calculated in the last layout cycle.
	 * @see LayoutUtil#isDesignTime(ContainerWrapper)
	 */
	public int[][] getRowSizes(final Object parentContainer) {
		return Grid.getSizesAndIndexes(parentContainer, true);
	}

	/**
	 * Returns the sizes of the columns and gaps for a container.
	 * There will be two arrays returned [0] and [1].
	 * <p>
	 * The first array will be the indexes of the columns where indexes that are less than 30000 or larger than 30000 is docking
	 * columns. There might be extra docking columns that aren't visible but they always have size 0. Non docking indexes will
	 * probably always be 0, 1, 2, 3, etc..
	 * <p>
	 * The second array is the sizes of the form:<br>
	 * <code>[top inset][column size 1][gap 1][column size 2][gap 2][column size n][bottom inset]</code>.
	 * <p>
	 * The returned sizes will be the ones calculated in the last layout cycle.
	 * 
	 * @param parentContainer The container to retuern the column sizes and gaps for. In Swing it will be a
	 *            {@link java.awt.Container} and
	 *            in SWT it will be a {@link org.eclipse.swt.widgets.Composite}.
	 * @return The sizes and indexes or <code>null</code> if {@link LayoutUtil#isDesignTime(ContainerWrapper)} is
	 *         <code>false</code> or <code>parentContainer</code> does not have a MigLayout layout manager.
	 *         The returned sizes will be the ones calculated in the last layout cycle.
	 * @see LayoutUtil#isDesignTime(ContainerWrapper)
	 */
	public int[][] getColumnSizes(final Object parentContainer) {
		return Grid.getSizesAndIndexes(parentContainer, false);
	}

	/**
	 * Returns the a constraint string that can be re-parsed to be the exact same AxisConstraint.
	 * 
	 * @param ac The axis constraint to return as a constraint string.
	 * @param asAPI If the returned string should be of API type (e.g. .flowX().gap("rel").align("right")) or
	 *            as a String type (e.g. "flowx, gap rel, right").
	 * @param isCols The the constraint should be returned for columns rather than rows.
	 * @return A String. Never <code>null</code>.
	 */
	public final String getConstraintString(final AC ac, final boolean asAPI, final boolean isCols) {
		final StringBuffer sb = new StringBuffer(32);

		final DimConstraint[] dims = ac.getConstaints();
		final BoundSize defGap = isCols
				? MigLayoutToolkit.getPlatformDefaults().getGridGapX() : MigLayoutToolkit.getPlatformDefaults().getGridGapY();

		for (int i = 0; i < dims.length; i++) {
			final DimConstraint dc = dims[i];

			addRowDimConstraintString(dc, sb, asAPI);

			if (i < dims.length - 1) {
				BoundSize gap = dc.getGapAfter();

				if (gap == defGap || gap == null)
					gap = dims[i + 1].getGapBefore();

				if (gap != null) {
					final String gapStr = getBS(gap);
					if (asAPI) {
						sb.append(".gap(\"").append(gapStr).append("\")");
					}
					else {
						sb.append(gapStr);
					}
				}
				else {
					if (asAPI)
						sb.append(".gap()");
				}
			}
		}

		return sb.toString();
	}

	/**
	 * Adds the a constraint string that can be re-parsed to be the exact same DimConstraint.
	 * 
	 * @param dc The layout constraint to return as a constraint string.
	 * @param asAPI If the returned string should be of API type (e.g. .flowX().gap("rel").align("right")) or
	 *            as a String type (e.g. "flowx, gap rel, right").
	 */
	private final void addRowDimConstraintString(final DimConstraint dc, final StringBuffer sb, final boolean asAPI) {
		final int gp = dc.getGrowPriority();

		final int firstComma = sb.length();

		final BoundSize size = dc.getSize();
		if (size.isUnset() == false) {
			if (asAPI) {
				sb.append(".size(\"").append(getBS(size)).append("\")");
			}
			else {
				sb.append(',').append(getBS(size));
			}
		}

		if (gp != 100) {
			if (asAPI) {
				sb.append(".growPrio(").append(gp).append("\")");
			}
			else {
				sb.append(",growprio ").append(gp);
			}
		}

		final Float gw = dc.getGrow();
		if (gw != null) {
			final String g = gw.floatValue() != 100f ? floatToString(gw.floatValue(), asAPI) : "";
			if (asAPI) {
				if (g.length() == 0) {
					sb.append(".grow()");
				}
				else {
					sb.append(".grow(\"").append(g).append("\")");
				}
			}
			else {
				sb.append(",grow").append(g.length() > 0 ? (" " + g) : "");
			}
		}

		final int sp = dc.getShrinkPriority();
		if (sp != 100) {
			if (asAPI) {
				sb.append(".shrinkPrio(").append(sp).append("\")");
			}
			else {
				sb.append(",shrinkprio ").append(sp);
			}
		}

		final Float sw = dc.getShrink();
		if (sw != null && sw.intValue() != 100) {
			final String s = floatToString(sw.floatValue(), asAPI);
			if (asAPI) {
				sb.append(".shrink(\"").append(s).append("\")");
			}
			else {
				sb.append(",shrink ").append(s);
			}
		}

		final String eg = dc.getEndGroup();
		if (eg != null) {
			if (asAPI) {
				sb.append(".endGroup(\"").append(eg).append("\")");
			}
			else {
				sb.append(",endgroup ").append(eg);
			}
		}

		final String sg = dc.getSizeGroup();
		if (sg != null) {
			if (asAPI) {
				sb.append(".sizeGroup(\"").append(sg).append("\")");
			}
			else {
				sb.append(",sizegroup ").append(sg);
			}
		}

		final UnitValue al = dc.getAlign();
		if (al != null) {
			if (asAPI) {
				sb.append(".align(\"").append(getUV(al)).append("\")");
			}
			else {
				final String s = getUV(al);
				final String alKw = (s.equals("top")
					|| s.equals("bottom")
					|| s.equals("left")
					|| s.equals("label")
					|| s.equals("leading")
					|| s.equals("center")
					|| s.equals("trailing")
					|| s.equals("right") || s.equals("baseline")) ? "" : "align ";
				sb.append(',').append(alKw).append(s);
			}
		}

		if (dc.isNoGrid()) {
			if (asAPI) {
				sb.append(".noGrid()");
			}
			else {
				sb.append(",nogrid");
			}
		}

		if (dc.isFill()) {
			if (asAPI) {
				sb.append(".fill()");
			}
			else {
				sb.append(",fill");
			}
		}

		if (asAPI == false) {
			if (sb.length() > firstComma) {
				sb.setCharAt(firstComma, '[');
				sb.append(']');
			}
			else {
				sb.append("[]");
			}
		}
	}

	/**
	 * Returns the a constraint string that can be re-parsed to be the exact same DimConstraint.
	 * 
	 * @param dc The layout constraint to return as a constraint string.
	 * @param asAPI If the returned string should be of API type (e.g. .flowX().gap("rel").align("right")) or
	 *            as a String type (e.g. "flowx, gap rel, right").
	 * @param isHor The the DimConstraint is decoration something horizontal (column or x).
	 * @param noGrowAdd If <code>true</code> no grow constraints will be added.
	 * @return A constraint string. Never <code>null</code>.
	 */
	private final void addComponentDimConstraintString(
		final DimConstraint dc,
		final StringBuffer sb,
		final boolean asAPI,
		final boolean isHor,
		final boolean noGrowAdd) {
		final int gp = dc.getGrowPriority();
		if (gp != 100) {
			if (asAPI) {
				sb.append(isHor ? ".growPrioX(" : ".growPrioY(").append(gp).append(')');
			}
			else {
				sb.append(isHor ? ",growpriox " : ",growprioy ").append(gp);
			}
		}

		if (noGrowAdd == false) {
			final Float gw = dc.getGrow();
			if (gw != null) {
				final String g = gw.floatValue() != 100f ? floatToString(gw.floatValue(), asAPI) : "";
				if (asAPI) {
					sb.append(isHor ? ".growX(" : ".growY(").append(g).append(')');
				}
				else {
					sb.append(isHor ? ",growx" : ",growy").append(g.length() > 0 ? (" " + g) : "");
				}
			}
		}

		final int sp = dc.getShrinkPriority();
		if (sp != 100) {
			if (asAPI) {
				sb.append(isHor ? ".shrinkPrioX(" : ".shrinkPrioY(").append(sp).append(')');
			}
			else {
				sb.append(isHor ? ",shrinkpriox " : ",shrinkprioy ").append(sp);
			}
		}

		final Float sw = dc.getShrink();
		if (sw != null && sw.intValue() != 100) {
			final String s = floatToString(sw.floatValue(), asAPI);
			if (asAPI) {
				sb.append(isHor ? ".shrinkX(" : ".shrinkY(").append(s).append(')');
			}
			else {
				sb.append(isHor ? ",shrinkx " : ",shrinky ").append(s);
			}
		}

		final String eg = dc.getEndGroup();
		if (eg != null) {
			if (asAPI) {
				sb.append(isHor ? ".endGroupX(\"" : ".endGroupY(\"").append(eg).append("\")");
			}
			else {
				sb.append(isHor ? ",endgroupx " : ",endgroupy ").append(eg);
			}
		}

		final String sg = dc.getSizeGroup();
		if (sg != null) {
			if (asAPI) {
				sb.append(isHor ? ".sizeGroupX(\"" : ".sizeGroupY(\"").append(sg).append("\")");
			}
			else {
				sb.append(isHor ? ",sizegroupx " : ",sizegroupy ").append(sg);
			}
		}

		appendBoundSize(dc.getSize(), sb, isHor, asAPI);

		final UnitValue al = dc.getAlign();
		if (al != null) {
			if (asAPI) {
				sb.append(isHor ? ".alignX(\"" : ".alignY(\"").append(getUV(al)).append("\")");
			}
			else {
				sb.append(isHor ? ",alignx " : ",aligny ").append(getUV(al));
			}
		}

		final BoundSize gapBef = dc.getGapBefore();
		final BoundSize gapAft = dc.getGapAfter();
		if (gapBef != null || gapAft != null) {
			if (asAPI) {
				sb.append(isHor ? ".gapX(\"" : ".gapY(\"").append(getBS(gapBef)).append("\", \"").append(getBS(gapAft)).append(
						"\")");
			}
			else {
				sb.append(isHor ? ",gapx " : ",gapy ").append(getBS(gapBef));
				if (gapAft != null)
					sb.append(' ').append(getBS(gapAft));
			}
		}
	}

	private void appendBoundSize(final BoundSize size, final StringBuffer sb, final boolean isHor, final boolean asAPI) {
		if (size.isUnset() == false) {
			if (size.getPreferred() == null) {
				if (size.getMin() == null) {
					if (asAPI) {
						sb.append(isHor ? ".maxWidth(\"" : ".maxHeight(\"").append(getUV(size.getMax())).append("\")");
					}
					else {
						sb.append(isHor ? ",wmax " : ",hmax ").append(getUV(size.getMax()));
					}

				}
				else if (size.getMax() == null) {
					if (asAPI) {
						sb.append(isHor ? ".minWidth(\"" : ".minHeight(\"").append(getUV(size.getMin())).append("\")");
					}
					else {
						sb.append(isHor ? ",wmin " : ",hmin ").append(getUV(size.getMin()));
					}
				}
				else { // None are null
					if (asAPI) {
						sb.append(isHor ? ".width(\"" : ".height(\"").append(getUV(size.getMin())).append("::").append(
								getUV(size.getMax())).append("\")");
					}
					else {
						sb.append(isHor ? ",width " : ",height ").append(getUV(size.getMin())).append("::").append(
								getUV(size.getMax()));
					}
				}
			}
			else {
				if (asAPI) {
					sb.append(isHor ? ".width(\"" : ".height(\"").append(getBS(size)).append("\")");
				}
				else {
					sb.append(isHor ? ",width " : ",height ").append(getBS(size));
				}
			}
		}
	}

	/**
	 * Returns the a constraint string that can be re-parsed to be the exact same LayoutConstraint.
	 * 
	 * @param cc The component constraint to return as a constraint string.
	 * @param asAPI If the returned string should be of API type (e.g. .flowX().gap("rel").align("right")) or
	 *            as a String type (e.g. "flowx, gap rel, right").
	 * @return A String. Never <code>null</code>.
	 */
	public final String getConstraintString(final CC cc, final boolean asAPI) {
		final StringBuffer sb = new StringBuffer(16);

		if (cc.isNewline())
			sb.append(asAPI ? ".newline()" : ",newline");

		if (cc.isExternal())
			sb.append(asAPI ? ".external()" : ",external");

		final Boolean flowX = cc.getFlowX();
		if (flowX != null) {
			if (asAPI) {
				sb.append(flowX.booleanValue() ? ".flowX()" : ".flowY()");
			}
			else {
				sb.append(flowX.booleanValue() ? ",flowx" : ",flowy");
			}
		}

		final UnitValue[] pad = cc.getPadding();
		if (pad != null) {
			sb.append(asAPI ? ".pad(\"" : ",pad ");
			for (int i = 0; i < pad.length; i++)
				sb.append(getUV(pad[i])).append(i < pad.length - 1 ? " " : "");
			if (asAPI)
				sb.append("\")");
		}

		final UnitValue[] pos = cc.getPos();
		if (pos != null) {
			if (cc.isBoundsInGrid()) {
				for (int i = 0; i < 4; i++) {
					if (pos[i] != null) {
						if (asAPI) {
							sb.append('.').append(X_Y_STRINGS[i]).append("(\"").append(getUV(pos[i])).append("\")");
						}
						else {
							sb.append(',').append(X_Y_STRINGS[i]).append(getUV(pos[i]));
						}
					}
				}
			}
			else {
				sb.append(asAPI ? ".pos(\"" : ",pos ");
				final int iSz = (pos[2] != null || pos[3] != null) ? 4 : 2; // "pos x y" vs "pos x1 y1 x2 y2".
				for (int i = 0; i < iSz; i++)
					sb.append(getUV(pos[i])).append(i < iSz - 1 ? " " : "");

				if (asAPI)
					sb.append("\")");
			}
		}

		final String id = cc.getId();
		if (id != null) {
			if (asAPI) {
				sb.append(".id(\"").append(id).append("\")");
			}
			else {
				sb.append(",id ").append(id);
			}
		}

		final String tag = cc.getTag();
		if (tag != null) {
			if (asAPI) {
				sb.append(".tag(\"").append(tag).append("\")");
			}
			else {
				sb.append(",tag ").append(tag);
			}
		}

		final int hideMode = cc.getHideMode();
		if (hideMode >= 0) {
			if (asAPI) {
				sb.append(".hideMode(").append(hideMode).append(')');
			}
			else {
				sb.append(",hideMode ").append(hideMode);
			}
		}

		final int skip = cc.getSkip();
		if (skip > 0) {
			if (asAPI) {
				sb.append(".skip(").append(skip).append(')');
			}
			else {
				sb.append(",skip ").append(skip);
			}
		}

		final int split = cc.getSplit();
		if (split > 1) {
			final String s = split == LayoutUtil.INF ? "" : String.valueOf(split);
			if (asAPI) {
				sb.append(".split(").append(s).append(')');
			}
			else {
				sb.append(",split ").append(s);
			}
		}

		final int cx = cc.getCellX();
		final int cy = cc.getCellY();
		final int spanX = cc.getSpanX();
		final int spanY = cc.getSpanY();
		if (cx >= 0 && cy >= 0) {
			if (asAPI) {
				sb.append(".cell(").append(cx).append(", ").append(cy);
				if (spanX > 1 || spanY > 1)
					sb.append(", ").append(spanX).append(", ").append(spanY);
				sb.append(')');
			}
			else {
				sb.append(",cell ").append(cx).append(' ').append(cy);
				if (spanX > 1 || spanY > 1)
					sb.append(' ').append(spanX).append(' ').append(spanY);
			}
		}
		else if (spanX > 1 || spanY > 1) {
			if (spanX > 1 && spanY > 1) {
				sb.append(asAPI ? ".span(" : ",span ").append(spanX).append(asAPI ? ", " : " ").append(spanY);
			}
			else if (spanX > 1) {
				sb.append(asAPI ? ".spanX(" : ",spanx ").append(spanX == LayoutUtil.INF ? "" : (String.valueOf(spanX)));
			}
			else if (spanY > 1) {
				sb.append(asAPI ? ".spanY(" : ",spany ").append(spanY == LayoutUtil.INF ? "" : (String.valueOf(spanY)));
			}
			if (asAPI)
				sb.append(')');
		}

		final Float pushX = cc.getPushX();
		final Float pushY = cc.getPushY();
		if (pushX != null || pushY != null) {
			if (pushX != null && pushY != null) {
				sb.append(asAPI ? ".push(" : ",push ");
				if (pushX != 100.0 || pushY != 100.0)
					sb.append(pushX).append(asAPI ? ", " : " ").append(pushY);
			}
			else if (pushX != null) {
				sb.append(asAPI ? ".pushX(" : ",pushx ").append(pushX == 100 ? "" : (String.valueOf(pushX)));
			}
			else if (pushY != null) {
				sb.append(asAPI ? ".pushY(" : ",pushy ").append(pushY == 100 ? "" : (String.valueOf(pushY)));
			}
			if (asAPI)
				sb.append(')');
		}

		final int dock = cc.getDockSide();
		if (dock >= 0) {
			final String ds = CC.DOCK_SIDES[dock];
			if (asAPI) {
				sb.append(".dock").append(Character.toUpperCase(ds.charAt(0))).append(ds.substring(1)).append("()");
			}
			else {
				sb.append(",").append(ds);
			}
		}

		final boolean noGrowAdd = cc.getHorizontal().getGrow() != null
			&& cc.getHorizontal().getGrow().intValue() == 100
			&& cc.getVertical().getGrow() != null
			&& cc.getVertical().getGrow().intValue() == 100;

		addComponentDimConstraintString(cc.getHorizontal(), sb, asAPI, true, noGrowAdd);
		addComponentDimConstraintString(cc.getVertical(), sb, asAPI, false, noGrowAdd);
		if (noGrowAdd)
			sb.append(asAPI ? ".grow()" : ",grow"); // Combine ".growX().growY()" into ".grow()".

		if (cc.isWrap())
			sb.append(asAPI ? ".wrap()" : ",wrap");

		final String s = sb.toString();
		return s.length() == 0 || s.charAt(0) != ',' ? s : s.substring(1);
	}

	/**
	 * Returns the a constraint string that can be re-parsed to be the exact same LayoutConstraint.
	 * 
	 * @param lc The layout constraint to return as a constraint string.
	 * @param asAPI If the returned string should be of API type (e.g. .flowX().gap("rel").align("right")) or
	 *            as a String type (e.g. "flowx, gap rel, right").
	 * @return A String. Never <code>null</code>.
	 */
	public final String getConstraintString(final LC lc, final boolean asAPI) {
		final StringBuffer sb = new StringBuffer(16);

		if (lc.isFlowX() == false)
			sb.append(asAPI ? ".flowY()" : ",flowy");

		final boolean fillX = lc.isFillX();
		final boolean fillY = lc.isFillY();
		if (fillX || fillY) {
			if (fillX == fillY) {
				sb.append(asAPI ? ".fill()" : ",fill");
			}
			else {
				sb.append(asAPI ? (fillX ? ".fillX()" : ".fillY()") : (fillX ? ",fillx" : ",filly"));
			}
		}

		final Boolean leftToRight = lc.getLeftToRight();
		if (leftToRight != null) {
			if (asAPI) {
				sb.append(".leftToRight(").append(leftToRight).append(')');
			}
			else {
				sb.append(leftToRight.booleanValue() ? ",ltr" : ",rtl");
			}
		}

		if (!lc.getPackWidth().isUnset() || !lc.getPackHeight().isUnset()) {
			if (asAPI) {
				final String w = getBS(lc.getPackWidth());
				final String h = getBS(lc.getPackHeight());
				sb.append(".pack(");
				if (w.equals("pref") && h.equals("pref")) {
					sb.append(')');
				}
				else {
					sb.append('\"').append(w).append("\", \"").append(h).append("\")");
				}
			}
			else {
				sb.append(",pack");
				final String size = getBS(lc.getPackWidth()) + " " + getBS(lc.getPackHeight());
				if (size.equals("pref pref") == false)
					sb.append(' ').append(size);
			}
		}

		if (lc.getPackWidthAlign() != 0.5f || lc.getPackHeightAlign() != 1f) {
			if (asAPI) {
				sb.append(".packAlign(").append(floatToString(lc.getPackWidthAlign(), asAPI)).append(", ").append(
						floatToString(lc.getPackHeightAlign(), asAPI)).append(')');
			}
			else {
				sb.append(",packalign ").append(floatToString(lc.getPackWidthAlign(), asAPI)).append(' ').append(
						floatToString(lc.getPackHeightAlign(), asAPI));
			}
		}

		if (lc.isTopToBottom() == false)
			sb.append(asAPI ? ".bottomToTop()" : ",btt");

		final UnitValue[] insets = lc.getInsets();
		if (insets != null) {
			final LayoutUtil layoutUtil = MigLayoutToolkit.getLayoutUtil();
			final String cs = layoutUtil.getCCString(insets);
			if (cs != null) {
				if (asAPI) {
					sb.append(".insets(\"").append(cs).append("\")");
				}
				else {
					sb.append(",insets ").append(cs);
				}
			}
			else {
				sb.append(asAPI ? ".insets(\"" : ",insets ");
				for (int i = 0; i < insets.length; i++)
					sb.append(getUV(insets[i])).append(i < insets.length - 1 ? " " : "");
				if (asAPI)
					sb.append("\")");
			}
		}

		if (lc.isNoGrid())
			sb.append(asAPI ? ".noGrid()" : ",nogrid");

		if (lc.isVisualPadding() == false)
			sb.append(asAPI ? ".noVisualPadding()" : ",novisualpadding");

		final int hideMode = lc.getHideMode();
		if (hideMode > 0) {
			if (asAPI) {
				sb.append(".hideMode(").append(hideMode).append(')');
			}
			else {
				sb.append(",hideMode ").append(hideMode);
			}
		}

		appendBoundSize(lc.getWidth(), sb, true, asAPI);
		appendBoundSize(lc.getHeight(), sb, false, asAPI);

		final UnitValue alignX = lc.getAlignX();
		final UnitValue alignY = lc.getAlignY();
		if (alignX != null || alignY != null) {
			if (alignX != null && alignY != null) {
				sb.append(asAPI ? ".align(\"" : ",align ").append(getUV(alignX)).append(' ').append(getUV(alignY));
			}
			else if (alignX != null) {
				sb.append(asAPI ? ".alignX(\"" : ",alignx ").append(getUV(alignX));
			}
			else if (alignY != null) {
				sb.append(asAPI ? ".alignY(\"" : ",aligny ").append(getUV(alignY));
			}
			if (asAPI)
				sb.append("\")");
		}

		final BoundSize gridGapX = lc.getGridGapX();
		final BoundSize gridGapY = lc.getGridGapY();
		if (gridGapX != null || gridGapY != null) {
			if (gridGapX != null && gridGapY != null) {
				sb.append(asAPI ? ".gridGap(\"" : ",gap ").append(getBS(gridGapX)).append(' ').append(getBS(gridGapY));
			}
			else if (gridGapX != null) {
				sb.append(asAPI ? ".gridGapX(\"" : ",gapx ").append(getBS(gridGapX));
			}
			else if (gridGapY != null) {
				sb.append(asAPI ? ".gridGapY(\"" : ",gapy ").append(getBS(gridGapY));
			}
			if (asAPI)
				sb.append("\")");
		}

		final int wrapAfter = lc.getWrapAfter();
		if (wrapAfter != LayoutUtil.INF) {
			final String ws = wrapAfter > 0 ? String.valueOf(wrapAfter) : "";
			if (asAPI) {
				sb.append(".wrap(").append(ws).append(')');
			}
			else {
				sb.append(",wrap ").append(ws);
			}
		}

		final int debugMillis = lc.getDebugMillis();
		if (debugMillis > 0) {
			if (asAPI) {
				sb.append(".debug(").append(debugMillis).append(')');
			}
			else {
				sb.append(",debug ").append(debugMillis);
			}
		}

		final String s = sb.toString();
		return s.length() == 0 || s.charAt(0) != ',' ? s : s.substring(1);
	}

	private String getUV(final UnitValue uv) {
		return uv != null ? uv.getConstraintString() : "null";
	}

	private String getBS(final BoundSize bs) {
		return bs != null ? bs.getConstraintString() : "null";
	}

	/**
	 * Converts a <code>float</code> to a string and is removing the ".0" if the float is an integer.
	 * 
	 * @param f the float.
	 * @return <code>f</code> as a string. Never <code>null</code>.
	 */
	private final String floatToString(final float f, final boolean asAPI) {
		final String valS = String.valueOf(f);
		return valS.endsWith(".0") ? valS.substring(0, valS.length() - 2) : (valS + (asAPI ? "f" : ""));
	}
}
