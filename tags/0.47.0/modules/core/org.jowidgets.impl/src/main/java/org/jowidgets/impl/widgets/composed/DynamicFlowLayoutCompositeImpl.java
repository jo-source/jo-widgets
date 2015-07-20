/*
 * Copyright (c) 2015, Michael Grossmann
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
package org.jowidgets.impl.widgets.composed;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.layout.IDynamicFlowLayoutConstraints;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IDynamicFlowLayoutComposite;
import org.jowidgets.api.widgets.descriptor.setup.IDynamicFlowLayoutCompositeSetup;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.AlignmentVertical;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.StringUtils;

public final class DynamicFlowLayoutCompositeImpl extends ControlWrapper implements IDynamicFlowLayoutComposite {

    private final Orientation orientation;
    private final String oppositeLayoutConstraints;
    private final Integer gap;
    private final int leftMargin;
    private final int rightMargin;
    private final int topMargin;
    private final int bottomMargin;

    private final ArrayList<String> widthGroups;
    private final ArrayList<String> heightGroups;
    private final ArrayList<String> layoutConstraints;

    private long sizeGroupNr;

    public DynamicFlowLayoutCompositeImpl(final IComposite composite, final IDynamicFlowLayoutCompositeSetup setup) {
        super(composite);
        Assert.paramNotNull(setup.getOrientation(), "setup.getOrientation()");
        this.orientation = setup.getOrientation();
        this.gap = setup.getGap();
        this.leftMargin = setup.getLeftMargin();
        this.rightMargin = setup.getRightMargin();
        this.topMargin = setup.getTopMargin();
        this.bottomMargin = setup.getBottomMargin();

        this.oppositeLayoutConstraints = createOppositeLayoutConstraints(setup);
        this.widthGroups = new ArrayList<String>();
        this.heightGroups = new ArrayList<String>();
        this.layoutConstraints = new ArrayList<String>();

        this.sizeGroupNr = 0;
    }

    private static String createOppositeLayoutConstraints(final IDynamicFlowLayoutCompositeSetup setup) {
        final List<String> result = new LinkedList<String>();

        if (setup.isLayoutGrowing()) {
            result.add("grow");
        }

        final String sizeConstraints = createMigSizeConstraints(
                setup.getLayoutMinSize(),
                setup.getLayoutPreferredSize(),
                setup.getLayoutMaxSize());
        if (sizeConstraints != null) {
            result.add(sizeConstraints);
        }

        final String innerResultString = StringUtils.concatElementsSeparatedBy(result, ',');

        if (Orientation.VERTICAL == setup.getOrientation()) {
            return "" + setup.getLeftMargin() + "[" + innerResultString + "]" + setup.getRightMargin();
        }
        else if (Orientation.HORIZONTAL == setup.getOrientation()) {
            return "" + setup.getTopMargin() + "[" + innerResultString + "]" + setup.getBottomMargin();
        }
        else {
            throw new IllegalArgumentException("The orientation '" + setup.getOrientation() + "' is not supported");
        }
    }

    @Override
    protected IComposite getWidget() {
        return (IComposite) super.getWidget();
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE addFirst(
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
        final IDynamicFlowLayoutConstraints constraints) {
        return addFirst(getCreatorForDescriptor(descriptor), constraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE addLast(
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
        final IDynamicFlowLayoutConstraints constraints) {
        return addLast(getCreatorForDescriptor(descriptor), constraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final int index,
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
        final IDynamicFlowLayoutConstraints constraints) {
        return add(index, getCreatorForDescriptor(descriptor), constraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE addFirst(
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final IDynamicFlowLayoutConstraints constraints) {
        return add(0, creator, constraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE addLast(
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final IDynamicFlowLayoutConstraints constraints) {
        return add(getChildrenCount(), creator, constraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final int index,
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final IDynamicFlowLayoutConstraints constraints) {
        return addImpl(index, creator, constraints);
    }

    @Override
    public int getChildrenCount() {
        return getWidget().getChildren().size();
    }

    @Override
    public void layoutBegin() {
        getWidget().layoutBegin();
    }

    @Override
    public void layoutEnd() {
        getWidget().layoutEnd();
    }

    @Override
    public void layout() {
        getWidget().layout();
    }

    private <WIDGET_TYPE extends IControl> ICustomWidgetCreator<WIDGET_TYPE> getCreatorForDescriptor(
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
        return new ICustomWidgetCreator<WIDGET_TYPE>() {
            @Override
            public WIDGET_TYPE create(final ICustomWidgetFactory widgetFactory) {
                return widgetFactory.create(descriptor);
            }
        };
    }

    private <WIDGET_TYPE extends IControl> WIDGET_TYPE addImpl(
        final int index,
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final IDynamicFlowLayoutConstraints constraints) {
        final String cellConstraints = createMigCellConstraintsForIndex(index, constraints);
        final WIDGET_TYPE result = getWidget().add(index, creator, cellConstraints);

        result.addDisposeListener(new IDisposeListener() {
            @Override
            public void onDispose() {
                removeControlConstraints(result);
                updateLayout();
            }
        });

        layoutConstraints.add(index, createMigLayoutConstraints(constraints));
        updateLayout();
        return result;
    }

    private void removeControlConstraints(final IControl control) {
        final int indexOfControl = getWidget().getChildren().indexOf(control);
        if (indexOfControl != -1) {
            layoutConstraints.remove(indexOfControl);
            widthGroups.remove(indexOfControl);
            heightGroups.remove(indexOfControl);
        }
    }

    private String createMigCellConstraintsForIndex(final int index, final IDynamicFlowLayoutConstraints constraints) {

        final String widthGroup = getWidthGroup(index, constraints);
        final String heightGroup = getHeightGroup(index, constraints);

        widthGroups.add(index, widthGroup);
        heightGroups.add(index, heightGroup);

        final StringBuilder result = new StringBuilder();
        result.append("sgx ");
        result.append(widthGroup);
        result.append(", sgy ");
        result.append(heightGroup);

        final String cellConstraints = createMigCellConstraints(constraints);
        if (!EmptyCheck.isEmpty(cellConstraints)) {
            result.append(", ");
            result.append(cellConstraints);
        }

        return result.toString();
    }

    private String getWidthGroup(final int index, final IDynamicFlowLayoutConstraints constraints) {
        if (constraints == null || constraints.useWidthOfElementAt() == null) {
            return nextSizeGroup();
        }
        else {
            return widthGroups.get(constraints.useWidthOfElementAt().intValue());
        }
    }

    private String getHeightGroup(final int index, final IDynamicFlowLayoutConstraints constraints) {
        if (constraints == null || constraints.useHeightOfElementAt() == null) {
            return nextSizeGroup();
        }
        else {
            return heightGroups.get(constraints.useHeightOfElementAt().intValue());
        }
    }

    private String createMigCellConstraints(final IDynamicFlowLayoutConstraints constraints) {
        if (constraints == null) {
            return null;
        }

        final List<String> result = new LinkedList<String>();

        if (constraints.isGrowWidth()) {
            result.add("growx");
        }

        if (constraints.isGrowHeight()) {
            result.add("growy");
        }

        final AlignmentHorizontal alignmentHorizontal = constraints.getAlignmentHorizontal();
        if (AlignmentHorizontal.LEFT.equals(alignmentHorizontal)) {
            result.add("alignx l");
        }
        else if (AlignmentHorizontal.RIGHT.equals(alignmentHorizontal)) {
            result.add("alignx r");
        }
        else if (AlignmentHorizontal.CENTER.equals(alignmentHorizontal)) {
            result.add("alignx c");
        }

        final AlignmentVertical alignmentVertical = constraints.getAlignmentVertical();
        if (AlignmentVertical.TOP.equals(alignmentVertical)) {
            result.add("aligny t");
        }
        else if (AlignmentVertical.BOTTOM.equals(alignmentVertical)) {
            result.add("aligny b");
        }
        else if (AlignmentVertical.CENTER.equals(alignmentVertical)) {
            result.add("aligny c");
        }

        final String widthConstraints = createMigWidthCellConstraints(constraints);
        if (!EmptyCheck.isEmpty(widthConstraints)) {
            result.add(widthConstraints);
        }

        final String heightConstraints = createMigHeightCellConstraints(constraints);
        if (!EmptyCheck.isEmpty(heightConstraints)) {
            result.add(heightConstraints);
        }

        return StringUtils.concatElementsSeparatedBy(result, ',');
    }

    private String createMigWidthCellConstraints(final IDynamicFlowLayoutConstraints constraints) {
        final String migSizeConstraints = createMigWidthConstraints(constraints);
        if (migSizeConstraints != null) {
            return "w " + migSizeConstraints;
        }
        else {
            return "";
        }
    }

    private String createMigHeightCellConstraints(final IDynamicFlowLayoutConstraints constraints) {
        final String migSizeConstraints = createMigHeightConstraints(constraints);
        if (migSizeConstraints != null) {
            return "h " + migSizeConstraints;
        }
        else {
            return "";
        }
    }

    private static String createMigWidthConstraints(final IDynamicFlowLayoutConstraints constraints) {
        return createMigSizeConstraints(constraints.getMinWidth(), constraints.getPreferredWidth(), constraints.getMaxWidth());
    }

    private static String createMigHeightConstraints(final IDynamicFlowLayoutConstraints constraints) {
        return createMigSizeConstraints(constraints.getMinHeight(), constraints.getPreferredHeight(), constraints.getMaxHeight());
    }

    private static String createMigSizeConstraints(final Integer min, final Integer pref, final Integer max) {
        if (min == null && pref == null && max == null) {
            return null;
        }
        final StringBuilder builder = new StringBuilder();
        if (min != null) {
            builder.append(min);
        }
        builder.append(":");
        if (pref != null) {
            builder.append(pref);
        }
        builder.append(":");
        if (max != null) {
            builder.append(max);
        }
        return builder.toString();
    }

    private String createMigLayoutConstraints(final IDynamicFlowLayoutConstraints constraints) {
        if (constraints == null) {
            return "";
        }
        final List<String> result = new LinkedList<String>();

        if (Orientation.HORIZONTAL.equals(orientation) && constraints.isGrowWidth()) {
            result.addAll(createMigHorizontalLayoutConstraints(constraints));
        }
        else if (Orientation.VERTICAL.equals(orientation) && constraints.isGrowHeight()) {
            result.addAll(createMigVerticalLayoutConstraints(constraints));
        }

        return StringUtils.concatElementsSeparatedBy(result, ',');
    }

    private List<String> createMigHorizontalLayoutConstraints(final IDynamicFlowLayoutConstraints constraints) {
        final List<String> result = new LinkedList<String>();
        if (constraints.isGrowWidth()) {
            result.add("grow");
        }
        final String widthConstraints = createMigWidthConstraints(constraints);
        if (widthConstraints != null) {
            result.add(widthConstraints);
        }
        return result;
    }

    private List<String> createMigVerticalLayoutConstraints(final IDynamicFlowLayoutConstraints constraints) {
        final List<String> result = new LinkedList<String>();
        if (constraints.isGrowHeight()) {
            result.add("grow");
        }
        final String widthConstraints = createMigHeightConstraints(constraints);
        if (widthConstraints != null) {
            result.add(widthConstraints);
        }
        return result;
    }

    private void updateLayout() {
        final String layoutConstraintsString = createMigLayoutConstraintsString();
        if (Orientation.HORIZONTAL.equals(orientation)) {
            getWidget().setLayout(new MigLayoutDescriptor(layoutConstraintsString, oppositeLayoutConstraints));
        }
        else if (Orientation.VERTICAL.equals(orientation)) {
            getWidget().setLayout(new MigLayoutDescriptor("wrap", oppositeLayoutConstraints, layoutConstraintsString));
        }
        else {
            throw new IllegalStateException("Orientation '" + orientation + "' is not supported");
        }
    }

    private String createMigLayoutConstraintsString() {
        if (layoutConstraints.size() == 0) {
            return null;
        }

        final StringBuilder builder = new StringBuilder();

        if (Orientation.VERTICAL == orientation) {
            builder.append(topMargin);
        }
        else if (Orientation.HORIZONTAL == orientation) {
            builder.append(leftMargin);
        }
        else {
            throw new IllegalArgumentException("The orientation '" + orientation + "' is not supported");
        }

        for (int i = 0; i < layoutConstraints.size(); i++) {
            builder.append("[");
            builder.append(layoutConstraints.get(i));
            builder.append("]");
            if (gap != null && i < layoutConstraints.size() - 1) {
                builder.append(gap.toString());
            }
        }

        if (Orientation.VERTICAL == orientation) {
            builder.append(bottomMargin);
        }
        else if (Orientation.HORIZONTAL == orientation) {
            builder.append(rightMargin);
        }
        else {
            throw new IllegalArgumentException("The orientation '" + orientation + "' is not supported");
        }

        return builder.toString();
    }

    private String nextSizeGroup() {
        return "grp" + sizeGroupNr++;
    }
}
