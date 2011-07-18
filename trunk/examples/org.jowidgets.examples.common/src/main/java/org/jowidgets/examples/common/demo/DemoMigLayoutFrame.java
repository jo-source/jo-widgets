/*
 * Copyright (c) 2011, nimoll
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
 * 
 * Code based on MigLayout demos developed by Mikael Grev, MiG InfoCom AB
 */

package org.jowidgets.examples.common.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jowidgets.api.controler.ITreeSelectionEvent;
import org.jowidgets.api.controler.ITreeSelectionListener;
import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.layout.ILayoutFactoryProvider;
import org.jowidgets.api.layout.miglayout.IAC;
import org.jowidgets.api.layout.miglayout.ILC;
import org.jowidgets.api.layout.miglayout.IMigLayout;
import org.jowidgets.api.layout.miglayout.IMigLayoutToolkit;
import org.jowidgets.api.layout.miglayout.IPlatformDefaults;
import org.jowidgets.api.model.table.ITableColumn;
import org.jowidgets.api.model.table.ITableModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.ICheckBox;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ISplitComposite;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.ITextArea;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.api.widgets.ITextLabel;
import org.jowidgets.api.widgets.IToggleButton;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableDataModelObservable;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.tools.controler.ComponentAdapter;
import org.jowidgets.tools.controler.TableColumnModelObservable;
import org.jowidgets.tools.controler.TableDataModelObservable;
import org.jowidgets.tools.model.table.TableCell;
import org.jowidgets.tools.powo.JoFrame;

public final class DemoMigLayoutFrame extends JoFrame {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();
	private static final ILayoutFactoryProvider LFP = Toolkit.getLayoutFactoryProvider();

	private static final ColorValue LABEL_COLOR = new ColorValue(0, 70, 213);

	private static final String[][] PANELS = new String[][] {
			//			{"BugTestApp", "BugTestApp, Disregard"},
			{"Welcome", "\n\n         \"MigLayout makes complex layouts easy and normal layouts one-liners.\""},
			{
					"Quick Start",
					"This is an example of how to build a common dialog type. Note that there are no special components, "
						+ "nested panels or absolute references to cell positions. If you look at the source code you will see that "
						+ "the layout code is very simple to understand."},
			{
					"Plain",
					"A simple example on how simple it is to create normal forms. No builders needed since the whole layout manager "
						+ "works like a builder."},
			{
					"Alignments",
					"Shows how the alignment of components are specified. At the top/left is the alignment for the column/row. "
						+ "The components have no alignments specified.\n\nNote that baseline alignment will be interpreted as "
						+ "'center' before JDK 6."},
			{
					"Cell Alignments",
					"Shows how components are aligned when both column/row alignments and component constraints are specified. "
						+ "At the top/left are the alignment for the column/row and the text on the buttons is the component "
						+ "constraint that will override the column/row alignment if it is an alignment.\n\nNote that baseline "
						+ "alignment will be interpreted as 'center' before JDK 6."},
			{
					"Basic Sizes",
					"A simple example that shows how to use the column or row min/preferred/max size to set the sizes of the "
						+ "contained components and also an example that shows how to do this directly in the component constraints."},
			{
					"Growing",
					"A simple example that shows how to use the growx and growy constraint to set the sizes and how they should "
						+ "grow to fit the available size. Both the column/row and the component grow/shrink constraints can be "
						+ "set, but the components will always be confined to the space given by its column/row."},
			{
					"Grow Shrink",
					"Demonstrates the very flexible grow and shrink constraints that can be set on a component.\nComponents can "
						+ "be divided into grow/shrink groups and also have grow/shrink weight within each of those groups.\n\nBy "
						+ "default components shrink to their inherent (or specified) minimum size, but they don't grow."},
			{
					"Span",
					"This example shows the powerful spanning and splitting that can be specified in the component constraints. "
						+ "With spanning any number of cells can be merged with the additional option to split that space for more "
						+ "than one component. This makes layouts very flexible and reduces the number of times you will need nested "
						+ "panels to very few."},
			{
					"Flow Direction",
					"Shows the different flow directions. Flow direction for the layout specifies if the next cell will be in "
						+ "the x or y dimension. Note that it can be a different flow direction in the slit cell (the middle cell "
						+ "is slit in two). Wrap is set to 3 for all panels."},
			{
					"Grouping",
					"Sizes for both components and columns/rows can be grouped so they get the same size. For instance buttons in "
						+ "a button bar can be given a size-group so that they will all get the same minimum and preferred size "
						+ "(the largest within the group). Size-groups can be set for the width, height or both."},
			{
					"Units",
					"Demonstrates the basic units that are understood by MigLayout. These units can be extended by the user by "
						+ "adding one or more UnitConverter(s)."},
			{
					"Component Sizes",
					"Minimum, preferred and maximum component sizes can be overridden in the component constraints using any unit "
						+ "type. The format to do this is short and simple to understand. You simply specify the "
						+ "min, preferred and max sizes with a colon between.\n\nAbove are some examples of this. An exclamation "
						+ "mark means that the value will be used for all sizes."},
			{"Bound Sizes", "Shows how to create columns that are stable between tabs using minimum sizes."},
			{
					"Cell Position",
					"Even though MigLayout has automatic grid flow you can still specify the cell position explicitly. You can even "
						+ "combine absolute (x, y) and flow (skip, wrap and newline) constraints to build your layout."},
			{
					"Orientation",
					"MigLayout supports not only right-to-left orientation, but also bottom-to-top. You can even set the flow "
						+ "direction so that the flow is vertical instead of horizontal. It will automatically "
						+ "pick up if right-to-left is to be used depending on the ComponentWrapper, but it can also be manually "
						+ "set for every layout."},
			{
					"Absolute Position",
					"Demonstrates the option to place any number of components using absolute coordinates. This can be just the "
						+ "position (if min/preferred size) using \"x y p p\" format or"
						+ "the bounds using the \"x1 y1 x2 y2\" format. Any unit can be used and percent is relative to the parent.\n"
						+ "Absolute components will not disturb the flow or occupy cells in the grid. "
						+ "Absolute positioned components will be taken into account when calculating the container's preferred size."},
			{
					"Component Links",
					"Components can be linked to any side of any other component. It can be a forward, backward or cyclic link "
						+ "references, as long as it is stable and won't continue to change value over many iterations."
						+ "Links are referencing the ID of another component. The ID can be overridden by the component's "
						+ "constrains or is provided by the ComponentWrapper. For instance it will use the component's 'name' "
						+ "on Swing.\n"
						+ "Since the links can be combined with any expression (such as 'butt1.x+10' or 'max(button.x, 200)' the "
						+ "links are very customizable."},
			{
					"Docking",
					"Docking components can be added around the grid. The docked component will get the whole width/height on the "
						+ "docked side by default, however this can be overridden. When all docked components are laid out, whatever "
						+ "space is left will be available for the normal grid laid out components. Docked components does not in "
						+ "any way affect the flow in the grid.\n\nSince the docking runs in the same code path "
						+ "as the normal layout code the same properties can be specified for the docking components. You can for "
						+ "instance set the sizes and alignment or link other components to their docked component's bounds."},
			{
					"Button Bars",
					"Button order is very customizable and are by default different on the supported platforms. E.g. Gaps, button "
						+ "order and minimum button size are properties that are 'per platform'. MigLayout picks up the current "
						+ "platform automatically and adjusts the button order and minimum button size accordingly, all without "
						+ "using a button builder or any other special code construct."},
			//			{
			//					"Debug",
			//					"Demonstrates the non-intrusive way to get visual debugging aid. There is no need to use a special DebugPanel "
			//						+ "or anything that will need code changes. The user can simply turn on debug on the layout manager by using "
			//						+ "the 'debug' constraint and it will continuously repaint the panel with debug information on top. This "
			//						+ "means you don't have to change your code to debug!"},
			{
					"Layout Showdown",
					"This is an implementation of the Layout Showdown posted on java.net by John O'Conner. The first tab is a pure"
						+ " implemenetation of the showdown that follows all the rules. The second tab is a slightly fixed version "
						+ "that follows some improved layout guidelines. "
						+ "The source code is for bothe the first and for the fixed version. Note the simplification of the code for "
						+ "the fixed version. Writing better layouts with MiG Layout is reasier that writing bad.\n\nReference: "
						+ "http://weblogs.java.net/blog/joconner/archive/2006/10/more_informatio.html"},
			{
					"API Constraints1",
					"This dialog shows the constraint API added to v2.0. It works the same way as the string constraints but with "
						+ "chained method calls. See the source code for details."},
			{
					"API Constraints2",
					"This dialog shows the constraint API added to v2.0. It works the same way as the string constraints but with "
						+ "chained method calls. See the source code for details."},};

	private final IComposite layoutDisplayPanel;

	private final ITabFolder descriptionTabPane;

	private final ITextArea descrTextArea;

	private ITabItem windowMovedListeningWidget = null;

	private final AtomicBoolean allowDispatch = new AtomicBoolean(true);

	private final IPlatformDefaults platformDefaults;

	private boolean ignorePlatformEvents = false;
	private ITextLabel formatLabel;
	private IToggleButton winButt;
	private IToggleButton macButt;

	public DemoMigLayoutFrame() {
		this("");
	}

	public DemoMigLayoutFrame(final String framework) {
		super((framework + " MigLayout Demo v2.5 - Mig Layout v").trim()
			+ Toolkit.getLayoutFactoryProvider().getMigLayoutToolkit().getMigLayoutVersion());
		platformDefaults = Toolkit.getLayoutFactoryProvider().getMigLayoutToolkit().getPlatformDefaults();

		addComponentListener(new ComponentAdapter() {
			@Override
			public void positionChanged() {
				windowMoved();
			}
		});

		setLayout(LFP.migLayoutBuilder().constraints("wrap").columnConstraints("[]u[grow,fill]").rowConstraints(
				"[grow,fill][pref]").build());

		final ITabFolder layoutPickerTabPane = add(BPF.tabFolder().setTabsCloseable(false), "spany, grow");

		// font bold...
		final ITabItem tabPickerList = layoutPickerTabPane.addItem(BPF.tabItem().setText("Example Browser"));
		tabPickerList.setLayout(LFP.migLayoutBuilder().constraints("fill, insets 0").build());
		final ITree pickerList = tabPickerList.add(BPF.tree(), "spany, grow");
		//pickerList.setBackgroundRole(layoutPickerTabPane.backgroundRole());

		for (int i = 0; i < PANELS.length; i++) {
			final ITreeNode node = pickerList.addNode();
			node.setText(PANELS[i][0]);
			node.setMarkup(Markup.STRONG);
			node.setIcon(null);
		}

		layoutDisplayPanel = add(BPF.composite());
		layoutDisplayPanel.setLayout(LFP.migLayoutBuilder().constraints("fill, insets 0").build());

		descriptionTabPane = add(BPF.tabFolder(), "growx, hmin 120");
		final ITabItem tabDescriptionPane = descriptionTabPane.addItem(BPF.tabItem().setText("Description"));
		tabDescriptionPane.setLayout(LFP.migLayoutBuilder().constraints("fill, insets 0").build());
		descrTextArea = tabDescriptionPane.add(BPF.textArea().setEditable(false).setBorder(false), "grow");
		descrTextArea.setFontName("tahoma");
		descrTextArea.setFontSize(8);

		//setActivePage(16);
		setActivePage(0);
		setSize(900, 600);

		pickerList.addTreeSelectionListener(new ITreeSelectionListener() {

			@Override
			public void selectionChanged(final ITreeSelectionEvent event) {
				setActivePage(getSelectionIndex(event.getSelected().get(0).getText()));
			}
		});
	}

	private void setActivePage(final int index) {
		if (index == -1) {
			return;
		}
		if (allowDispatch.getAndSet(false)) {
			layoutDisplayPanel.removeAll();
			windowMovedListeningWidget = null;

			final String methodName = "create" + PANELS[index][0].replace(" ", "");

			try {
				descrTextArea.setText(PANELS[index][1]);
				layoutBegin();
				DemoMigLayoutFrame.class.getMethod(methodName, new Class[] {}).invoke(DemoMigLayoutFrame.this, new Object[] {});
				layoutEnd();

			}
			catch (final Exception e1) {
				// CHECKSTYLE:OFF
				e1.printStackTrace(); // Should never happpen...
				// CHECKSTYLE:ON
			}

			layoutDisplayPanel.redraw();
			allowDispatch.set(true);
		}
	}

	private static int getSelectionIndex(final String text) {
		for (int i = 0; i < PANELS.length; i++) {
			if (PANELS[i][0].equals(text)) {
				return i;
			}
		}
		return -1;
	}

	private static ITabFolder createTabFolder(final IContainer layoutDisplayPanel) {
		return layoutDisplayPanel.add(BPF.tabFolder(), "grow, wmin 500");
	}

	public void createWelcome() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		final ITabItem panel = result.addItem(BPF.tabItem().setText("Welcome"));
		panel.setLayout(LFP.migLayoutBuilder().constraints("ins 20, fill").build());

		final String s = "MigLayout's main purpose is to make layouts for SWT and Swing, and possibly other frameworks, much more powerful and a lot easier to create, especially for manual coding.\n\n"
			+ "The motto is: \"MigLayout makes complex layouts easy and normal layouts one-liners.\"\n\n"
			+ "The layout engine is very flexible and advanced, something that is needed to make it simple to use yet handle almost all layout use-cases.\n\n"
			+ "MigLayout can handle all layouts that the commonly used Swing Layout Managers can handle and this with a lot of extra features. "
			+ "It also incorporates most, if not all, of the open source alternatives FormLayout's and TableLayout's functionality."
			+ "\n\n\nThanks to Karsten Lentzsch from JGoodies.com for allowing the reuse of the main demo application layout and for his inspiring talks that led to this layout Manager."
			+ "\n\n\nMikael Grev\n"
			+ "MiG InfoCom AB\n"
			+ "miglayout@miginfocom.com";

		final ITextArea textArea = panel.add(BPF.textArea().setBorder(false).setText(s), "w 500:500, ay top, grow, push");
		textArea.setFontName("tahoma");
		textArea.setFontSize(8);
		textArea.setMarkup(Markup.STRONG);
		textArea.setPreferredSize(new Dimension(500, 300));

		// textArea should be transparent
		// textArea.setBackgroundMode(SWT.INHERIT_NONE);
		textArea.setBackgroundColor(panel.getBackgroundColor());

		result.setVisible(true);
	}

	public void createQuickStart() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		final ITabItem panel = result.addItem(BPF.tabItem().setText("Quick Start"));
		panel.setLayout(LFP.migLayoutBuilder().constraints("wrap").columnConstraints(
				"[right][fill,sizegroup]unrel[right][fill,sizegroup]").build());

		addSeparator(panel, "General");
		createLabel(panel, "Company", "gap indent");
		createTextField(panel, "", "span,growx");
		createLabel(panel, "Contact", "gap indent");
		createTextField(panel, "", "span,growx");

		addSeparator(panel, "Propeller");
		createLabel(panel, "PTI/kW", "gap indent");
		createTextField(panel, "", "wmin 130");
		createLabel(panel, "Power/kW", "gap indent");
		createTextField(panel, "", "wmin 130");
		createLabel(panel, "R/mm", "gap indent");
		createTextField(panel, "", "wmin 130");
		createLabel(panel, "D/mm", "gap indent");
		createTextField(panel, "", "wmin 130");
	}

	public void createPlain() {
		createPlainImpl(layoutDisplayPanel);
	}

	private ITabFolder createPlainImpl(final IComposite parent) {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		final ITabItem panel = result.addItem(BPF.tabItem().setText("Plain"));
		panel.setLayout(LFP.migLayoutBuilder().columnConstraints("[r][100lp, fill][60lp][95lp, fill]").build());

		addSeparator(panel, "Manufacturer");
		createLabel(panel, "Company", "");
		createTextField(panel, "", "span,growx");
		createLabel(panel, "Contact", "");
		createTextField(panel, "", "span,growx");
		createLabel(panel, "Order No", "");
		createTextField(panel, "", "wmin 15*6,wrap");

		addSeparator(panel, "Inspector");
		createLabel(panel, "Name", "");
		createTextField(panel, "", "span,growx");
		createLabel(panel, "Reference No", "");
		createTextField(panel, "", "wrap");
		createLabel(panel, "Status", "");
		createCombo(panel, new String[] {"In Progress", "Finnished", "Released"}, "wrap");

		addSeparator(panel, "Ship");
		createLabel(panel, "Shipyard", "");
		createTextField(panel, "", "span,growx");
		createLabel(panel, "Register No", "");
		createTextField(panel, "", "");
		createLabel(panel, "Hull No", "right");
		createTextField(panel, "", "wmin 15*6,wrap");
		createLabel(panel, "Project StructureType", "");
		createCombo(panel, new String[] {"New Building", "Convention", "Repair"}, "wrap");

		return result;
	}

	public void createAlignments() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		// Horizontal tab
		final ITabItem horTab = result.addItem(BPF.tabItem().setText("Horizontal"));
		horTab.setLayout(LFP.migLayoutBuilder().constraints("wrap").columnConstraints(
				"[label]15[left]15[center]15[right]15[fill]15[]").rowConstraints("[]15[]").build());

		final String[] horLabels = new String[] {"[label]", "[left]", "[center]", "[right]", "[fill]", "[] (Default)"};
		final String[] horNames = new String[] {"First Name", "Phone Number", "Facsmile", "Email", "Address", "Other"};

		for (int c = 0; c < horLabels.length; c++) {
			createLabel(horTab, horLabels[c], "");
		}

		for (int r = 0; r < horLabels.length; r++) {
			for (int c = 0; c < horNames.length; c++) {
				if (c == 0) {
					createLabel(horTab, horNames[r] + ":", "");
				}
				else {
					createButton(horTab, horNames[r], "");
				}
			}
		}

		// Vertical tab
		final ITabItem verTab = result.addItem(BPF.tabItem().setText("Vertical"));
		verTab.setLayout(LFP.migLayoutBuilder().constraints("wrap, flowy").columnConstraints("[]unrel[]rel[]").rowConstraints(
				"[top]15[center]15[bottom]15[fill]15[fill,baseline]15[baseline]15[]").build());

		final String[] verLabels = new String[] {
				"[top]", "[center]", "[bottom]", "[fill]", "[fill,baseline]", "[baseline]", "[] (Default)"};

		for (int c = 0; c < verLabels.length; c++) {
			createLabel(verTab, verLabels[c], "");
		}

		for (int c = 0; c < verLabels.length; c++) {
			createButton(verTab, "A Button", "");
		}

		for (int c = 0; c < verLabels.length; c++) {
			createTextField(verTab, "JTextFied", "");
		}

		for (int c = 0; c < verLabels.length; c++) {
			createTextArea(verTab, "Text    ", "");
		}

		for (int c = 0; c < verLabels.length; c++) {
			createTextArea(verTab, "Text\nwith two lines", "");
		}

		for (int c = 0; c < verLabels.length; c++) {
			createTextArea(verTab, "Scrolling Text\nwith two lines", "");
		}
	}

	public void createCellAlignments() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		// Horizontal
		final ITabItem hPanel = result.addItem(BPF.tabItem().setText("Horizontal"));
		hPanel.setLayout(LFP.migLayoutBuilder().constraints("wrap").columnConstraints(
				"[grow,left][grow,center][grow,right][grow,fill,center]").rowConstraints("[]unrel[][]").build());

		final String[] sizes = new String[] {"", "growx", "growx 0", "left", "center", "right", "leading", "trailing"};
		createLabel(hPanel, "[left]", "c");
		createLabel(hPanel, "[center]", "c");
		createLabel(hPanel, "[right]", "c");
		createLabel(hPanel, "[fill,center]", "c, growx 0");

		for (int r = 0; r < sizes.length; r++) {
			for (int c = 0; c < 4; c++) {
				final String text = sizes[r].length() > 0 ? sizes[r] : "default";
				createButton(hPanel, text, sizes[r]);
			}
		}

		// Vertical
		final ITabItem vPanel = result.addItem(BPF.tabItem().setText("Vertical"));
		vPanel.setLayout(LFP.migLayoutBuilder().constraints("wrap,flowy").columnConstraints("[right][]").rowConstraints(
				"[grow,top][grow,center][grow,bottom][grow,fill,bottom][grow,fill,baseline]").build());

		final String[] vSizes = new String[] {"", "growy", "growy 0", "top", "center", "bottom"};
		createLabel(vPanel, "[top]", "center");
		createLabel(vPanel, "[center]", "center");
		createLabel(vPanel, "[bottom]", "center");
		createLabel(vPanel, "[fill, bottom]", "center, growy 0");
		createLabel(vPanel, "[fill, baseline]", "center");

		for (int c = 0; c < vSizes.length; c++) {
			for (int r = 0; r < 5; r++) {
				final String text = vSizes[c].length() > 0 ? vSizes[c] : "default";
				createButton(vPanel, text, vSizes[c]);
			}
		}
	}

	public void createBasicSizes() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		// Horizontal tab
		final ITabItem horTab = result.addItem(BPF.tabItem().setText("Horizontal - Column size set"));
		horTab.setLayout(LFP.migLayoutBuilder().columnConstraints("[]15[75px]25[min]25[]").rowConstraints("[]15").build());

		createLabel(horTab, "75px", "skip");
		createLabel(horTab, "Min", "");
		createLabel(horTab, "Pref", "wrap");

		createLabel(horTab, "new Text(15)", "");
		createTextField(horTab, "               ", "wmin 10");
		createTextField(horTab, "               ", "wmin 10");
		createTextField(horTab, "               ", "wmin 10");

		// Vertical tab 1
		final ITabItem verTab = result.addItem(BPF.tabItem().setText("Vertical - Row sized"));
		verTab.setLayout(LFP.migLayoutBuilder().constraints("flowy,wrap").columnConstraints("[]15[]").rowConstraints(
				"[]15[c,45:45]15[c,min]15[c,pref]").build());

		createLabel(verTab, "45px", "skip");
		createLabel(verTab, "Min", "");
		createLabel(verTab, "Pref", "");

		createLabel(verTab, "new Text(SWT.MULTI)", "");
		createTextArea(verTab, "", "");
		createTextArea(verTab, "", "");
		createTextArea(verTab, "", "");

		// Componentsized/Baseline 2
		final ITabItem verTab2 = result.addItem(BPF.tabItem().setText("Vertical - Component sized + Baseline"));
		verTab2.setLayout(LFP.migLayoutBuilder().constraints("flowy,wrap").columnConstraints("[]15[]").rowConstraints(
				"[]15[baseline]15[baseline]15[baseline]").build());

		createLabel(verTab2, "45px", "skip");
		createLabel(verTab2, "Min", "");
		createLabel(verTab2, "Pref", "");

		createLabel(verTab2, "new Text(SWT.MULTI)", "");
		createTextArea(verTab2, "", "height 45");
		createTextArea(verTab2, "", "height min");
		createTextArea(verTab2, "", "height pref");
	}

	public void createGrowing() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		// All tab
		final ITabItem allTab = result.addItem(BPF.tabItem().setText("All"));
		allTab.setLayout(LFP.migLayoutBuilder().columnConstraints("[pref!][grow,fill]").rowConstraints("[]15[]").build());

		createLabel(allTab, "Fixed", "");
		createLabel(allTab, "Gets all extra space", "wrap");
		createTextField(allTab, "     ", "");
		createTextField(allTab, "     ", "");

		// Half tab
		final ITabItem halfTab = result.addItem(BPF.tabItem().setText("Half"));
		halfTab.setLayout(LFP.migLayoutBuilder().columnConstraints("[pref!][grow,fill]").rowConstraints("[]15[]").build());

		createLabel(halfTab, "Fixed", "");
		createLabel(halfTab, "Gets half of extra space", "");
		createLabel(halfTab, "Gets half of extra space", "wrap");
		createTextField(halfTab, "     ", "");
		createTextField(halfTab, "     ", "");
		createTextField(halfTab, "     ", "");

		// Percent 1 tab
		final ITabItem p1Tab = result.addItem(BPF.tabItem().setText("Percent 1"));
		p1Tab.setLayout(LFP.migLayoutBuilder().columnConstraints("[pref!][0:0,grow 25,fill][0:0,grow 75,fill]").rowConstraints(
				"[]15[]").build());

		createLabel(p1Tab, "Fixed", "");
		createLabel(p1Tab, "Gets 25% of extra space", "");
		createLabel(p1Tab, "Gets 75% of extra space", "wrap");
		createTextField(p1Tab, "     ", "");
		createTextField(p1Tab, "     ", "");
		createTextField(p1Tab, "     ", "");

		// Percent 2 tab
		final ITabItem p2Tab = result.addItem(BPF.tabItem().setText("Percent 2"));
		p2Tab.setLayout(LFP.migLayoutBuilder().columnConstraints("[0:0,grow 33,fill][0:0,grow 67,fill]").rowConstraints("[]15[]").build());

		createLabel(p2Tab, "Gets 33% of extra space", "");
		createLabel(p2Tab, "Gets 67% of extra space", "wrap");
		createTextField(p2Tab, "     ", "");
		createTextField(p2Tab, "     ", "");

		// Vertical 1 tab
		final ITabItem v1Tab = result.addItem(BPF.tabItem().setText("Vertical 1"));
		v1Tab.setLayout(LFP.migLayoutBuilder().constraints("flowy").columnConstraints("[]15[]").rowConstraints(
				"[][c,pref!][c,grow 25,fill][c,grow 75,fill]").build());

		createLabel(v1Tab, "Fixed", "skip");
		createLabel(v1Tab, "Gets 25% of extra space", "");
		createLabel(v1Tab, "Gets 75% of extra space", "wrap");
		createLabel(v1Tab, "new Text(SWT.MULTI | SWT.WRAP | SWT.BORDER)", "");
		createTextArea(v1Tab, "", "hmin 4*13");
		createTextArea(v1Tab, "", "hmin 4*13");
		createTextArea(v1Tab, "", "hmin 4*13");

		// Vertical 2 tab
		final ITabItem v2Tab = result.addItem(BPF.tabItem().setText("Vertical 2"));
		v2Tab.setLayout(LFP.migLayoutBuilder().constraints("flowy").columnConstraints("[]15[]").rowConstraints(
				"[][c,grow 33,fill][c,grow 67,fill]").build());

		createLabel(v2Tab, "Gets 33% of extra space", "skip");
		createLabel(v2Tab, "Gets 67% of extra space", "wrap");
		createLabel(v2Tab, "new Text(SWT.MULTI | SWT.WRAP | SWT.BORDER)", "");
		createTextArea(v2Tab, "", "hmin 4*13");
		createTextArea(v2Tab, "", "hmin 4*13");
	}

	public void createGrowShrink() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		// shrink tab
		final ITabItem shrinkPanel = result.addItem(BPF.tabItem().setText("Shrink"));
		shrinkPanel.setLayout(LFP.migLayoutBuilder().constraints("fill, insets 0").columnConstraints("[fill]").rowConstraints(
				"[fill]").build());

		final ISplitComposite sSplitPane = shrinkPanel.add(BPF.splitHorizontal(), "grow");
		final IContainer sPanel = sSplitPane.getFirst();
		final IContainer sTextPanel = sSplitPane.getSecond();

		sPanel.setLayout(LFP.migLayoutBuilder().constraints("nogrid").build());

		createTextField(sPanel, "shp 110", "shp 110,w 10:130");
		createTextField(sPanel, "Default (100)", "w 10:130");
		createTextField(sPanel, "shp 90", "shp 90,w 10:130");

		createTextField(sPanel, "shrink 25", "newline,shrink 25,w 10:130");
		createTextField(sPanel, "shrink 75", "shrink 75,w 10:130");

		createTextField(sPanel, "Default", "newline, w 10:130");
		createTextField(sPanel, "Default", "w 10:130");

		createTextField(sPanel, "shrink 0", "newline,shrink 0,w 10:130");

		createTextField(sPanel, "shp 110", "newline,shp 110,w 10:130");
		createTextField(sPanel, "shp 100,shrink 25", "shp 100,shrink 25,w 10:130");
		createTextField(sPanel, "shp 100,shrink 75", "shp 100,shrink 75,w 10:130");

		// final ITextControl sDescText = 
		createTextArea(
				sTextPanel,
				"Use the slider to see how the components shrink depending on the constraints set on them.\n\n'shp' means Shrink Priority. "
					+ "Lower values will be shrunk before higer ones and the default value is 100.\n\n'shrink' means Shrink Weight. "
					+ "Lower values relative to other's means they will shrink less when space is scarse. "
					+ "Shrink Weight is only relative to components with the same Shrink Priority. Default Shrink Weight is 100.\n\n"
					+ "The component's minimum size will always be honored.\n\nFor SWT, which doesn't have a component notion of minimum, "
					+ "preferred or maximum size, those sizes are set explicitly to minimum 10 and preferred 130 pixels.",
				"");
		// sDescText no border

		// Grow tab
		final ITabItem growPanel = result.addItem(BPF.tabItem().setText("Grow"));
		growPanel.setLayout(LFP.migLayoutBuilder().constraints("fill, insets 0").columnConstraints("[fill]").rowConstraints(
				"[fill]").build());

		final ISplitComposite gSplitPane = growPanel.add(BPF.splitHorizontal(), "grow");
		final IContainer gPanel = gSplitPane.getFirst();
		final IContainer gTextPanel = gSplitPane.getSecond();

		gPanel.setLayout(LFP.migLayoutBuilder().constraints("nogrid").columnConstraints("[grow]").build());

		createButton(gPanel, "gp 110, grow", "gp 110, grow, wmax 170");
		createButton(gPanel, "Default (100), grow", "grow, wmax 170");
		createButton(gPanel, "gp 90, grow", "gp 90, grow, wmax 170");

		createButton(gPanel, "Default Button", "newline");

		createButton(gPanel, "growx", "newline,growx,wrap");

		createButton(gPanel, "gp 110, grow", "gp 110, grow, wmax 170");
		createButton(gPanel, "gp 100, grow 25", "gp 100, grow 25, wmax 170");
		createButton(gPanel, "gp 100, grow 75", "gp 100, grow 75, wmax 170");

		// final ITextControl gDescText = 
		createTextArea(
				gTextPanel,
				"'gp' means Grow Priority. "
					+ "Higher values will be grown before lower ones and the default value is 100.\n\n'grow' means Grow Weight. "
					+ "Higher values relative to other's means they will grow more when space is up for takes. "
					+ "Grow Weight is only relative to components with the same Grow Priority. Default Grow Weight is 0 which means "
					+ "components will normally not grow. \n\nNote that the buttons in the first and last row have max width set to 170 to "
					+ "emphasize Grow Priority.\n\nThe component's maximum size will always be honored.",
				"");
		// gDescText no border
	}

	public void createSpan() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		// Horizontal span
		final ITabItem colPanel = result.addItem(BPF.tabItem().setText("Column Span/Split"));
		colPanel.setLayout(LFP.migLayoutBuilder().columnConstraints("[fill][25%,fill][105lp!,fill][150px!,fill]").rowConstraints(
				"[]15[][]").build());

		createTextField(colPanel, "Col1 [ ]", "");
		createTextField(colPanel, "Col2 [25%]", "");
		createTextField(colPanel, "Col3 [105lp!]", "");
		createTextField(colPanel, "Col4 [150px!]", "wrap");

		createLabel(colPanel, "Full Name:", "");
		createTextField(colPanel, "span, growx                              ", "span,growx");

		createLabel(colPanel, "Phone:", "");
		createTextField(colPanel, "   ", "span 3, split 5");
		createTextField(colPanel, "     ", null);
		createTextField(colPanel, "     ", null);
		createTextField(colPanel, "       ", null);
		createLabel(colPanel, "(span 3, split 4)", "wrap");

		createLabel(colPanel, "Zip/City:", "");
		createTextField(colPanel, "     ", "");
		createTextField(colPanel, "span 2, growx", null);

		// Vertical span
		final ITabItem rowPanel = result.addItem(BPF.tabItem().setText("Row Span"));
		rowPanel.setLayout(LFP.migLayoutBuilder().constraints("wrap").columnConstraints("[225lp]para[225lp]").rowConstraints(
				"[]3[]unrel[]3[]unrel[]3[]").build());

		createLabel(rowPanel, "Name", "");
		createLabel(rowPanel, "Notes", "");
		createTextField(rowPanel, "growx", null);
		createTextArea(rowPanel, "spany,grow          ", "spany,grow,hmin 13*5");
		createLabel(rowPanel, "Phone", "");
		createTextField(rowPanel, "growx", null);
		createLabel(rowPanel, "Fax", "");
		createTextField(rowPanel, "growx", null);
	}

	public void createFlowDirection() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		createFlowPanel(result, "Layout: flowx, Cell: flowx", "", "flowx");
		createFlowPanel(result, "Layout: flowx, Cell: flowy", "", "flowy");
		createFlowPanel(result, "Layout: flowy, Cell: flowx", "flowy", "flowx");
		createFlowPanel(result, "Layout: flowy, Cell: flowy", "flowy", "flowy");
	}

	private ITabItem createFlowPanel(final ITabFolder parent, final String text, final String gridFlow, final String cellFlow) {
		final ITabItem result = parent.addItem(BPF.tabItem().setText(text));
		result.setLayout(LFP.migLayoutBuilder().constraints("center, wrap 3," + gridFlow).columnConstraints("[110,fill]").rowConstraints(
				"[110,fill]").build());

		for (int i = 0; i < 9; i++) {
			// final IComposite b = 
			createPanel(result, "" + (i + 1), cellFlow);
			//b.setFont(deriveFont(b.getFont(), false, 20));
		}

		// final IComposite b = 
		createPanel(result, "5:2", cellFlow + ",cell 1 1");
		//b.setFont(deriveFont(b.getFont(), false, 20));

		return result;
	}

	public void createGrouping() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		// Ungrouped
		final ITabItem ugPanel = result.addItem(BPF.tabItem().setText("Ungrouped"));
		ugPanel.setLayout(LFP.migLayoutBuilder().columnConstraints("[]push[][][]").build());

		createButton(ugPanel, "Help", "");
		createButton(ugPanel, "< Back", "gap push");
		createButton(ugPanel, "Forward >", "");
		createButton(ugPanel, "Apply", "gap unrel");
		createButton(ugPanel, "Cancel", "gap unrel");

		// Grouped Components
		final ITabItem gPanel = result.addItem(BPF.tabItem().setText("Grouped (Components)"));
		gPanel.setLayout(LFP.migLayoutBuilder().constraints("nogrid, fillx").build());

		createButton(gPanel, "Help", "sg");
		createButton(gPanel, "< Back", "sg, gap push");
		createButton(gPanel, "Forward >", "sg");
		createButton(gPanel, "Apply", "sg, gap unrel");
		createButton(gPanel, "Cancel", "sg, gap unrel");

		// Grouped Columns
		final ITabItem gcPanel = result.addItem(BPF.tabItem().setText("Grouped (Columns)"));
		gcPanel.setLayout(LFP.migLayoutBuilder().columnConstraints("[sg,fill]push[sg,fill][sg,fill]unrel[sg,fill]unrel[sg,fill]").build());

		createButton(gcPanel, "Help", "");
		createButton(gcPanel, "< Back", "");
		createButton(gcPanel, "Forward >", "");
		createButton(gcPanel, "Apply", "");
		createButton(gcPanel, "Cancel", "");

		// Ungrouped Rows
		final ITabItem ugrPanel = result.addItem(BPF.tabItem().setText("Ungrouped Rows"));
		ugrPanel.setLayout(LFP.migLayout());

		createLabel(ugrPanel, "File Number:", "");
		createTextField(ugrPanel, "30                            ", "wrap");
		createLabel(ugrPanel, "BL/MBL number:", "");
		createTextField(ugrPanel, "7      ", "split 2");
		createTextField(ugrPanel, "7      ", "wrap");
		createLabel(ugrPanel, "Entry Date:", "");
		createTextField(ugrPanel, "7      ", "wrap");
		createLabel(ugrPanel, "RFQ Number:", "");
		createTextField(ugrPanel, "30                            ", "wrap");
		createLabel(ugrPanel, "Goods:", "");
		createCheck(ugrPanel, "Dangerous", "wrap");
		createLabel(ugrPanel, "Shipper:", "");
		createTextField(ugrPanel, "30                            ", "wrap");
		createLabel(ugrPanel, "Customer:", "");
		createTextField(ugrPanel, "", "split 2,growx");
		createButton(ugrPanel, "...", "width 60px:pref,wrap");
		createLabel(ugrPanel, "Port of Loading:", "");
		createTextField(ugrPanel, "30                            ", "wrap");
		createLabel(ugrPanel, "Destination:", "");
		createTextField(ugrPanel, "30                            ", "wrap");

		// Grouped Rows
		final ITabItem grPanel = result.addItem(BPF.tabItem().setText("Grouped Rows"));
		grPanel.setLayout(LFP.migLayoutBuilder().columnConstraints("[]").rowConstraints("[sg]").build()); // "sg" is the only difference to previous panel

		createLabel(grPanel, "File Number:", "");
		createTextField(grPanel, "30                            ", "wrap");
		createLabel(grPanel, "BL/MBL number:", "");
		createTextField(grPanel, "7      ", "split 2");
		createTextField(grPanel, "7      ", "wrap");
		createLabel(grPanel, "Entry Date:", "");
		createTextField(grPanel, "7      ", "wrap");
		createLabel(grPanel, "RFQ Number:", "");
		createTextField(grPanel, "30                            ", "wrap");
		createLabel(grPanel, "Goods:", "");
		createCheck(grPanel, "Dangerous", "wrap");
		createLabel(grPanel, "Shipper:", "");
		createTextField(grPanel, "30                            ", "wrap");
		createLabel(grPanel, "Customer:", "");
		createTextField(grPanel, "", "split 2,growx");
		createButton(grPanel, "...", "width 50px:pref,wrap");
		createLabel(grPanel, "Port of Loading:", "");
		createTextField(grPanel, "30                            ", "wrap");
		createLabel(grPanel, "Destination:", "");
		createTextField(grPanel, "30                            ", "wrap");
	}

	public void createUnits() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		// Horizontal
		final ITabItem hPanel = result.addItem(BPF.tabItem().setText("Horizontal"));
		hPanel.setLayout(LFP.migLayoutBuilder().constraints("wrap").columnConstraints("[right][]").build());

		final String[] sizes = new String[] {"72pt", "25.4mm", "2.54cm", "1in", "72px", "96px", "120px", "25%", "20sp"};
		for (int i = 0; i < sizes.length; i++) {
			createLabel(hPanel, sizes[i], "");
			createTextField(hPanel, "", "width " + sizes[i] + "");
		}

		// Horizontal lp
		final ITabItem hlpPanel = result.addItem(BPF.tabItem().setText("Horizontal LP"));
		hlpPanel.setLayout(LFP.migLayoutBuilder().columnConstraints("[right][][]").build());

		createLabel(hlpPanel, "9 cols", "");
		createTextField(hlpPanel, "", "wmin 9*6");
		final String[] lpSizes = new String[] {"75lp", "75px", "88px", "100px"};
		createLabel(hlpPanel, "", "wrap");
		for (int i = 0; i < lpSizes.length; i++) {
			createLabel(hlpPanel, lpSizes[i], "");
			createTextField(hlpPanel, "", "width " + lpSizes[i] + ", wrap");
		}

		// Vertical
		final ITabItem vPanel = result.addItem(BPF.tabItem().setText("Vertical"));
		vPanel.setLayout(LFP.migLayoutBuilder().constraints("wrap,flowy").columnConstraints("[c]").rowConstraints("[top][top]").build());

		final String[] vSizes = new String[] {"72pt", "25.4mm", "2.54cm", "1in", "72px", "96px", "120px", "25%", "20sp"};
		for (int i = 0; i < sizes.length; i++) {
			createLabel(vPanel, vSizes[i], "");
			createTextArea(vPanel, "", "width 50!, height " + vSizes[i] + "");
		}

		// Vertical lp
		final ITabItem vlpPanel = result.addItem(BPF.tabItem().setText("Vertical LP"));
		vlpPanel.setLayout(LFP.migLayoutBuilder().constraints("wrap,flowy").columnConstraints("[c]").rowConstraints(
				"[top][top]40px[top][top]").build());

		createLabel(vlpPanel, "4 rows", "");
		createTextArea(vlpPanel, "\n\n\n\n", "width 50!");
		createLabel(vlpPanel, "field", "");
		createTextField(vlpPanel, "", "wmin 5*9");

		final String[] vlpSizes1 = new String[] {"63lp", "57px", "63px", "68px", "25%"};
		final String[] vlpSizes2 = new String[] {"21lp", "21px", "23px", "24px", "10%"};
		for (int i = 0; i < vlpSizes1.length; i++) {
			createLabel(vlpPanel, vlpSizes1[i], "");
			createTextArea(vlpPanel, "", "width 50!, height " + vlpSizes1[i] + "");
			createLabel(vlpPanel, vlpSizes2[i], "");
			createTextField(vlpPanel, "", "height " + vlpSizes2[i] + "!,wmin 5*6");
		}

		createLabel(vlpPanel, "button", "skip 2");
		createButton(vlpPanel, "...", "");
	}

	public void createComponentSizes() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		final ITabItem tabPanel = result.addItem(BPF.tabItem().setText("Component Sizes"));
		tabPanel.setLayout(LFP.migLayoutBuilder().constraints("fill").build());

		final ISplitComposite splitPane = tabPanel.add(BPF.splitHorizontal(), "grow");
		final IContainer panel = splitPane.getFirst();
		final IContainer textPanel = splitPane.getSecond();

		panel.setLayout(LFP.migLayoutBuilder().constraints("wrap").columnConstraints("[right][0:pref,grow]").build());

		createLabel(panel, "", "");
		createTextField(panel, "8       ", "");
		createLabel(panel, "width min!", null);
		createTextField(panel, "3  ", "width min!");
		createLabel(panel, "width pref!", "");
		createTextField(panel, "3  ", "width pref!");
		createLabel(panel, "width min:pref", null);
		createTextField(panel, "8       ", "width min:pref");
		createLabel(panel, "width min:100:150", null);
		createTextField(panel, "8       ", "width min:100:150");
		createLabel(panel, "width min:100:150, growx", null);
		createTextField(panel, "8       ", "width min:100:150, growx");
		createLabel(panel, "width min:100, growx", null);
		createTextField(panel, "8       ", "width min:100, growx");
		createLabel(panel, "width 40!", null);
		createTextField(panel, "8       ", "width 40!");
		createLabel(panel, "width 40:40:40", null);
		createTextField(panel, "8       ", "width 40:40:40");

		// final ITextControl sDescText = 
		createTextArea(
				textPanel,
				"Use slider to see how the components grow and shrink depending on the constraints set on them.",
				"");
		// sDescText remove border
	}

	public void createBoundSizes() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		for (int i = 0; i < 2; i++) { // Jumping for 0 and Stable for 1
			final String colConstr = i == 0 ? "[right][300]" : "[right, 150lp:pref][300]";

			final ITabItem panel1 = result.addItem(BPF.tabItem().setText(i == 0 ? "Jumping 1" : "Stable 1"));
			panel1.setLayout(LFP.migLayoutBuilder().constraints("wrap").columnConstraints(colConstr).build());

			createLabel(panel1, "File Number:", "");
			createTextField(panel1, "", "growx");
			createLabel(panel1, "RFQ Number:", "");
			createTextField(panel1, "", "growx");
			createLabel(panel1, "Entry Date:", "");
			createTextField(panel1, "        ", "wmin 6*6");
			createLabel(panel1, "Sales Person:", "");
			createTextField(panel1, "", "growx");

			final ITabItem panel2 = result.addItem(BPF.tabItem().setText(i == 0 ? "Jumping 2" : "Stable 2"));
			panel2.setLayout(LFP.migLayoutBuilder().constraints("wrap").columnConstraints(colConstr).build());

			createLabel(panel2, "Shipper:", "");
			createTextField(panel2, "        ", "split 2");
			createTextField(panel2, "", "growx");
			createLabel(panel2, "Consignee:", "");
			createTextField(panel2, "        ", "split 2");
			createTextField(panel2, "", "growx");
			createLabel(panel2, "Departure:", "");
			createTextField(panel2, "        ", "split 2");
			createTextField(panel2, "", "growx");
			createLabel(panel2, "Destination:", "");
			createTextField(panel2, "        ", "split 2");
			createTextField(panel2, "", "growx");
		}
	}

	public void createCellPosition() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		// Absolute grid position
		final ITabItem absPanel = result.addItem(BPF.tabItem().setText("Absolute"));
		absPanel.setLayout(LFP.migLayoutBuilder().columnConstraints("[100:pref,fill]").rowConstraints("[100:pref,fill]").build());

		createPanel(absPanel, "cell 0 0", null);
		createPanel(absPanel, "cell 2 0", null);
		createPanel(absPanel, "cell 3 0", null);
		createPanel(absPanel, "cell 1 1", null);
		createPanel(absPanel, "cell 0 2", null);
		createPanel(absPanel, "cell 2 2", null);
		createPanel(absPanel, "cell 2 2", null);

		// Relative grid position with wrap
		final ITabItem relAwPanel = result.addItem(BPF.tabItem().setText("Relative + Wrap"));
		relAwPanel.setLayout(LFP.migLayoutBuilder().constraints("wrap").columnConstraints(
				"[100:pref,fill][100:pref,fill][100:pref,fill][100:pref,fill]").rowConstraints("[100:pref,fill]").build());

		createPanel(relAwPanel, "", null);
		createPanel(relAwPanel, "skip", null);
		createPanel(relAwPanel, "", null);
		createPanel(relAwPanel, "skip,wrap", null);
		createPanel(relAwPanel, "", null);
		createPanel(relAwPanel, "skip,split", null);
		createPanel(relAwPanel, "", null);

		// Relative grid position with manual wrap
		final ITabItem relWPanel = result.addItem(BPF.tabItem().setText("Relative"));
		relWPanel.setLayout(LFP.migLayoutBuilder().columnConstraints("[100:pref,fill]").rowConstraints("[100:pref,fill]").build());

		createPanel(relWPanel, "", null);
		createPanel(relWPanel, "skip", null);
		createPanel(relWPanel, "wrap", null);
		createPanel(relWPanel, "skip,wrap", null);
		createPanel(relWPanel, "", null);
		createPanel(relWPanel, "skip,split", null);
		createPanel(relWPanel, "", null);

		// Mixed relative and absolute grid position
		final ITabItem mixPanel = result.addItem(BPF.tabItem().setText("Mixed"));
		mixPanel.setLayout(LFP.migLayoutBuilder().columnConstraints("[100:pref,fill]").rowConstraints("[100:pref,fill]").build());

		createPanel(mixPanel, "", null);
		createPanel(mixPanel, "cell 2 0", null);
		createPanel(mixPanel, "", null);
		createPanel(mixPanel, "cell 1 1,wrap", null);
		createPanel(mixPanel, "", null);
		createPanel(mixPanel, "cell 2 2,split", null);
		createPanel(mixPanel, "", null);
	}

	public void createOrientation() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		final ITabItem mainPanel = result.addItem(BPF.tabItem().setText("Orientation"));
		mainPanel.setLayout(LFP.migLayoutBuilder().constraints("flowy").columnConstraints("[grow,fill]").rowConstraints(
				"[]0[]15lp[]0[]").build());

		// Default orientation
		final IContainer defPanel = createPanel(mainPanel, LFP.migLayoutBuilder().columnConstraints("[][grow,fill]").build());

		addSeparator(defPanel, "Default Orientation");
		createLabel(defPanel, "Level", "");
		createTextField(defPanel, "", "span,growx");
		createLabel(defPanel, "Radar", "");
		createTextField(defPanel, "", "");
		createTextField(defPanel, "", "");

		// Right-to-left, Top-to-bottom
		final IContainer rtlPanel = createPanel(
				mainPanel,
				LFP.migLayoutBuilder().constraints("rtl,ttb").columnConstraints("[][grow,fill]").build());
		addSeparator(rtlPanel, "Right to Left");
		createLabel(rtlPanel, "Level", "");
		createTextField(rtlPanel, "", "span,growx");
		createLabel(rtlPanel, "Radar", "");
		createTextField(rtlPanel, "", "");
		createTextField(rtlPanel, "", "");

		// Right-to-left, Bottom-to-top
		final IContainer rtlbPanel = createPanel(
				mainPanel,
				LFP.migLayoutBuilder().constraints("rtl,btt").columnConstraints("[][grow,fill]").build());
		addSeparator(rtlbPanel, "Right to Left, Bottom to Top");
		createLabel(rtlbPanel, "Level", "");
		createTextField(rtlbPanel, "", "span,growx");
		createLabel(rtlbPanel, "Radar", "");
		createTextField(rtlbPanel, "", "");
		createTextField(rtlbPanel, "", "");

		// Left-to-right, Bottom-to-top
		final IContainer ltrbPanel = createPanel(
				mainPanel,
				LFP.migLayoutBuilder().constraints("ltr,btt").columnConstraints("[][grow,fill]").build());
		addSeparator(ltrbPanel, "Left to Right, Bottom to Top");
		createLabel(ltrbPanel, "Level", "");
		createTextField(ltrbPanel, "", "span,growx");
		createLabel(ltrbPanel, "Radar", "");
		createTextField(ltrbPanel, "", "");
		createTextField(ltrbPanel, "", "");
	}

	public void createAbsolutePosition() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		final ITabItem posPanel = result.addItem(BPF.tabItem().setText("X Y Positions"));
		posPanel.setLayout(LFP.migLayout());

		windowMovedListeningWidget = posPanel;

		createButton(posPanel, "pos 0.5al 0al", null);
		createButton(posPanel, "pos 1al 0al", null);
		createButton(posPanel, "pos 0.5al 0.5al", null);
		createButton(posPanel, "pos 5in 45lp", null);
		createButton(posPanel, "pos 0.5al 0.5al", null);
		createButton(posPanel, "pos 0.5al 1al", null);
		createButton(posPanel, "pos 1al .25al", null);
		createButton(posPanel, "pos visual.x2-pref visual.y2-pref", null);
		createButton(posPanel, "pos 1al -1in", null);
		createButton(posPanel, "pos 100 100", null);
		createButton(posPanel, "pos (10+(20*3lp)) 200", null);
		createButton(
				posPanel,
				"Drag Window! (pos 500-container.xpos 500-container.ypos)",
				"pos 500-container.xpos 500-container.ypos");

		// Bounds tab
		final ITabItem boundsTabPanel = result.addItem(BPF.tabItem().setText("X1 Y1 X2 Y2 Bounds"));
		boundsTabPanel.setLayout(LFP.migLayoutBuilder().constraints("fill").columnConstraints("[grow,fill]").rowConstraints(
				"[grow,fill]").build());

		final IContainer boundsPanel = boundsTabPanel.add(BPF.composite(), "grow");
		boundsPanel.setLayout(LFP.migLayout());

		createButton(boundsPanel, "pos n 50% 50% n", null);
		createButton(boundsPanel, "pos 50% n n 50%", null);
		createButton(boundsPanel, "pos 50% 50% n n", null);
		createButton(boundsPanel, "pos n n 50% 50%", null);
		createButton(boundsPanel, "pos 0.5al 0.5al, pad 3 0 -3 0", null);
		createButton(boundsPanel, "pos 0.9al 0.4al n visual.y2-10", null);
		createButton(boundsPanel, "pos 0.1al 0.4al n visual.y2-10", null);
		createButton(boundsPanel, "pos visual.x 100 visual.x2 p", null);
		createButton(boundsPanel, "pos visual.x 40 visual.x2 70", null);
		createButton(boundsPanel, "pos 0 0 container.x2 n", null);

		// final ITextLabel southLabel = 
		createLabel(boundsPanel, "pos (visual.x+visual.w*0.1) visual.y2-40 (visual.x2-visual.w*0.1) visual.y2", null);
		//final QPalette plt = new QPalette();
		// set Background
		//plt.setBrush(ColorRole.Window, new QBrush(new QColor(200, 200, 255)));
		//southLabel.setPalette(plt);
		//southLabel.setAutoFillBackground(true);
		//southLabel.setFont(deriveFont(southLabel.font(), true, 10));

	}

	public void createComponentLinks() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		// Links tab
		final ITabItem linksPanel = result.addItem(BPF.tabItem().setText("Component Links"));
		linksPanel.setLayout(LFP.migLayout());

		createButton(linksPanel, "Mini", "pos null ta.y ta.x2 null, pad 3 0 -3 0");
		createTextArea(linksPanel, "Components, Please Link to Me!\nMy ID is: 'ta'", "id ta, pos 0.5al 0.5al, w 300");
		createButton(linksPanel, "id b1,pos ta.x2 ta.y2", null);
		createButton(linksPanel, "pos b1.x2+rel b1.y visual.x2 null", null);
		createCheck(linksPanel, "pos (ta.x+indent) (ta.y2+rel)", null);
		createButton(linksPanel, "pos ta.x2+rel ta.y visual.x2 null", null);
		createButton(linksPanel, "pos null ta.y+(ta.h-pref)/2 ta.x-rel null", null);
		createButton(linksPanel, "pos ta.x ta.y2+100 ta.x2 null", null);

		// External tab
		final ITabItem externalPanel = result.addItem(BPF.tabItem().setText("External Components"));
		externalPanel.setLayout(LFP.migLayout());

		final IButton extButt = createButton(externalPanel, "Bounds Externally Set!", "id ext, external");
		extButt.setSize(200, 40);
		extButt.setPosition(250, 130);
		createButton(externalPanel, "pos ext.x2 ext.y2", "pos ext.x2 ext.y2");
		createButton(externalPanel, "pos null null ext.x ext.y", "pos null null ext.x ext.y");

		// End grouping
		final ITabItem egPanel = result.addItem(BPF.tabItem().setText("End Grouping"));
		linksPanel.setLayout(LFP.migLayout());

		createButton(egPanel, "id b1, endgroupx g1, pos 200 200", null);
		createButton(egPanel, "id b2, endgroupx g1, pos (b1.x+2ind) (b1.y2+rel)", null);
		createButton(egPanel, "id b3, endgroupx g1, pos (b1.x+4ind) (b2.y2+rel)", null);
		createButton(egPanel, "id b4, endgroupx g1, pos (b1.x+6ind) (b3.y2+rel)", null);

		// Group Bounds tab
		final ITabItem gpPanel = result.addItem(BPF.tabItem().setText("Group Bounds"));
		linksPanel.setLayout(LFP.migLayout());

		// final IContainer boundsPanel = 
		createPanel(gpPanel, null, "pos grp1.x grp1.y grp1.x2 grp1.y2");
		//final QPalette plt = new QPalette();
		// set Background
		//plt.setBrush(ColorRole.Window, new QBrush(new QColor(200, 200, 255)));
		//boundsPanel.setPalette(plt);
		//boundsPanel.setAutoFillBackground(true);

		createButton(gpPanel, "id grp1.b1, pos n 0.5al 50% n", null);
		createButton(gpPanel, "id grp1.b2, pos 50% 0.5al n n", null);
		createButton(gpPanel, "id grp1.b3, pos 0.5al n n b1.y", null);
		createButton(gpPanel, "id grp1.b4, pos 0.5al b1.y2 n n", null);

		createButton(gpPanel, "pos n grp1.y2 grp1.x n", null);
		createButton(gpPanel, "pos n n grp1.x grp1.y", null);
		createButton(gpPanel, "pos grp1.x2 n n grp1.y", null);
		createButton(gpPanel, "pos grp1.x2 grp1.y2", null);
	}

	public void createDocking() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		final ITabItem p1 = result.addItem(BPF.tabItem().setText("Docking 1"));
		p1.setLayout(LFP.migLayoutBuilder().constraints("fill").build());

		createPanel(p1, "1. North", "north");
		createPanel(p1, "2. West", "west");
		createPanel(p1, "3. East", "east");
		createPanel(p1, "4. South", "south");

		p1.add(BPF.table(new ITableModel() {
			private final TableColumnModelObservable columnModelObservable = new TableColumnModelObservable();
			private final TableDataModelObservable dataModelObservable = new TableDataModelObservable();

			final class TableColumn implements ITableColumn {

				private final String text;
				private int width = 100;

				private TableColumn(final int index) {
					this.text = "Column " + index;
				}

				@Override
				public String getText() {
					return text;
				}

				@Override
				public String getToolTipText() {
					return text;
				}

				@Override
				public IImageConstant getIcon() {
					return null;
				}

				@Override
				public void setWidth(final int width) {
					this.width = width;
				}

				@Override
				public int getWidth() {
					return width;
				}

				@Override
				public AlignmentHorizontal getAlignment() {
					return AlignmentHorizontal.LEFT;
				}

				@Override
				public boolean isVisible() {
					return true;
				}

			}

			@Override
			public int getColumnCount() {
				return 5;
			}

			@Override
			public ITableColumn getColumn(final int columnIndex) {
				return new TableColumn(columnIndex);
			}

			@Override
			public ITableColumnModelObservable getTableColumnModelObservable() {
				return columnModelObservable;
			}

			@Override
			public int getRowCount() {
				return 15;
			}

			@Override
			public ITableCell getCell(final int rowIndex, final int columnIndex) {
				final TableCell cell = new TableCell("Cell " + (rowIndex + 1) + ", " + (columnIndex + 1));
				return cell;
			}

			@Override
			public ArrayList<Integer> getSelection() {
				return new ArrayList<Integer>();
			}

			@Override
			public void setSelection(final List<Integer> selection) {}

			@Override
			public ITableDataModelObservable getTableDataModelObservable() {
				return dataModelObservable;
			}

		}), "grow");

		//table.setHeaderVisible(true);
		//table.setLinesVisible(true);

		final ITabItem p2 = result.addItem(BPF.tabItem().setText("Docking 2 (fill)"));
		p2.setLayout(LFP.migLayoutBuilder().constraints("fill").columnConstraints("[c]").build());

		createPanel(p2, "1. North", "north");
		createPanel(p2, "2. North", "north");
		createPanel(p2, "3. West", "west");
		createPanel(p2, "4. West", "west");
		createPanel(p2, "5. South", "south");
		createPanel(p2, "6. East", "east");
		createButton(p2, "7. Normal", "");
		createButton(p2, "8. Normal", "");
		createButton(p2, "9. Normal", "");

		final ITabItem p3 = result.addItem(BPF.tabItem().setText("Docking 3"));
		p3.setLayout(LFP.migLayoutBuilder().build());

		createPanel(p3, "1. North", "north");
		createPanel(p3, "2. South", "south");
		createPanel(p3, "3. West", "west");
		createPanel(p3, "4. East", "east");
		createButton(p3, "5. Normal", "");

		final ITabItem p4 = result.addItem(BPF.tabItem().setText("Docking 4"));
		p4.setLayout(LFP.migLayoutBuilder().build());

		createPanel(p4, "1. North", "north");
		createPanel(p4, "2. North", "north");
		createPanel(p4, "3. West", "west");
		createPanel(p4, "4. West", "west");
		createPanel(p4, "5. South", "south");
		createPanel(p4, "6. East", "east");
		createButton(p4, "7. Normal", "");
		createButton(p4, "8. Normal", "");
		createButton(p4, "9. Normal", "");

		final ITabItem p5 = result.addItem(BPF.tabItem().setText("Docking 5 (fillx)"));
		p5.setLayout(LFP.migLayoutBuilder().constraints("fillx").columnConstraints("[c]").build());

		createPanel(p5, "1. North", "north");
		createPanel(p5, "2. North", "north");
		createPanel(p5, "3. West", "west");
		createPanel(p5, "4. West", "west");
		createPanel(p5, "5. South", "south");
		createPanel(p5, "6. East", "east");
		createButton(p5, "7. Normal", "");
		createButton(p5, "8. Normal", "");
		createButton(p5, "9. Normal", "");

		final ITabItem p6 = result.addItem(BPF.tabItem().setText("Random Docking"));
		p6.setLayout(LFP.migLayoutBuilder().constraints("fill").build());

		final String[] sides = {"north", "east", "south", "west"};
		final Random rand = new Random();
		for (int i = 0; i < 20; i++) {
			final int side = rand.nextInt(4);
			createPanel(p6, ((i + 1) + " " + sides[side]), sides[side]);
		}
		createPanel(p6, "I'm in the Center!", "grow");
	}

	public void createButtonBars() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		final ITabItem mainPanel = result.addItem(BPF.tabItem().setText("Button Bars"));
		mainPanel.setLayout(LFP.migLayoutBuilder().constraints("ins 0 0 15lp 0").columnConstraints("[grow]").rowConstraints(
				"[grow]u[baseline,nogrid]").build());

		// TODO NM don't create an inner tab folder 
		final ITabFolder tabbedPane = mainPanel.add(BPF.tabFolder(), "grow, wrap");

		createButtonBarsPanel(tabbedPane, "Buttons", "help", false);
		createButtonBarsPanel(tabbedPane, "Buttons with Help2", "help2", false);
		createButtonBarsPanel(tabbedPane, "Buttons (Same width)", "help", true);
		// TODO NM check if it is necessary to add the panels...

		createLabel(mainPanel, "Button Order:", "");
		formatLabel = createLabel(mainPanel, "'" + platformDefaults.getButtonOrder() + "'", "growx");
		formatLabel.setMarkup(Markup.STRONG);
		formatLabel.setFontSize(8);

		winButt = createToggleButton(mainPanel, "Windows", "wmin button");
		macButt = createToggleButton(mainPanel, "Mac OS X", "wmin button");

		winButt.addInputListener(new IInputListener() {

			@Override
			public void inputChanged() {
				if (ignorePlatformEvents) {
					return;
				}
				setPlatform(IPlatformDefaults.WINDOWS_XP);
			}
		});
		macButt.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				if (ignorePlatformEvents) {
					return;
				}
				setPlatform(IPlatformDefaults.MAC_OSX);
			}
		});

		final IButton helpButt = createButton(mainPanel, "Help", "gap unrel,wmin button");
		helpButt.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				Toolkit.getMessagePane().showInfo(
						"Help",
						"See JavaDoc for PlatformConverter.getButtonBarOrder(..) for details on the format string.");
			}
		});

		(platformDefaults.getPlatform() == IPlatformDefaults.WINDOWS_XP ? winButt : macButt).setSelected(true);
	}

	private void setPlatform(final int platform) {
		ignorePlatformEvents = true;
		platformDefaults.setPlatform(platform);
		formatLabel.setText("'" + platformDefaults.getButtonOrder() + "'");
		winButt.setSelected(platform == IPlatformDefaults.WINDOWS_XP);
		macButt.setSelected(platform == IPlatformDefaults.MAC_OSX);
		ignorePlatformEvents = false;
		layoutBegin();
		layoutEnd();
		redraw();
	}

	private ITabItem createButtonBarsPanel(
		final ITabFolder parent,
		final String text,
		final String helpTag,
		final boolean sizeLocked) {
		final ITabItem panel = parent.addItem(BPF.tabItem().setText(text));
		panel.setLayout(LFP.migLayoutBuilder().constraints("nogrid, fillx, aligny 100%, gapy unrel").build());

		// Notice that the order in the rows below does not matter...
		final String[][] buttons = new String[][] {
				{"No", "Yes"}, {"Help", "Close"}, {"OK", "Help"}, {"OK", "Cancel", "Help"}, {"OK", "Cancel", "Apply", "Help"},
				{"No", "Yes", "Cancel"}, {"Help", "< Move Back", "Move Forward >", "Cancel"}, {"Print...", "Cancel", "Help"},};

		for (int r = 0; r < buttons.length; r++) {
			for (int i = 0; i < buttons[r].length; i++) {
				final String txt = buttons[r][i];
				String tag = txt;

				if (txt.equals("Help")) {
					tag = helpTag;
				}
				else if (txt.equals("< Move Back")) {
					tag = "back";
				}
				else if (txt.equals("Close")) {
					tag = "cancel";
				}
				else if (txt.equals("Move Forward >")) {
					tag = "next";
				}
				else if (txt.equals("Print...")) {
					tag = "other";
				}
				final String wrap = (i == buttons[r].length - 1) ? ",wrap" : "";
				final String sizeGroup = sizeLocked ? ("sgx " + r + ",") : "";
				createButton(panel, txt, sizeGroup + "tag " + tag + wrap);
			}
		}

		return panel;
	}

	public void createLayoutShowdown() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		final ITabItem p1 = result.addItem(BPF.tabItem().setText("Layout Showdown (pure)"));
		p1.setLayout(LFP.migLayoutBuilder().columnConstraints("[]15[][grow,fill]15[grow]").build());

		// References to text fields not stored to reduce code clutter.

		createList(p1, "Mouse, Mickey", "spany, growy, wmin 150");
		createLabel(p1, "Last Name", "");
		createTextField(p1, "", "");
		createLabel(p1, "First Name", "split"); // split divides the cell
		createTextField(p1, "", "growx, wrap");
		createLabel(p1, "Phone", "");
		createTextField(p1, "", "");
		createLabel(p1, "Email", "split");
		createTextField(p1, "", "growx, wrap");
		createLabel(p1, "Address 1", "");
		createTextField(p1, "", "span, growx"); // span merges cells
		createLabel(p1, "Address 2", "");
		createTextField(p1, "", "span, growx");
		createLabel(p1, "City", "");
		createTextField(p1, "", "wrap"); // wrap continues on next line
		createLabel(p1, "State", "");
		createTextField(p1, "", "");
		createLabel(p1, "Postal Code", "split");
		createTextField(p1, "", "growx, wrap");
		createLabel(p1, "Country", "");
		createTextField(p1, "", "wrap 15");

		createButton(p1, "New", "span, split, align right");
		createButton(p1, "Delete", "");
		createButton(p1, "Edit", "");
		createButton(p1, "Save", "");
		createButton(p1, "Cancel", "wrap push");

		// Fixed version *******************************************

		final ITabItem p2 = result.addItem(BPF.tabItem().setText("Layout Showdown (improved)"));
		p2.setLayout(LFP.migLayoutBuilder().columnConstraints("[]15[][grow,fill]15[][grow,fill]").build());

		// References to text fields not stored to reduce code clutter.

		createList(p2, "Mouse, Mickey", "spany, growy, wmin 150");
		createLabel(p2, "Last Name", "");
		createTextField(p2, "", "");
		createLabel(p2, "First Name", "");
		createTextField(p2, "", "wrap");
		createLabel(p2, "Phone", "");
		createTextField(p2, "", "");
		createLabel(p2, "Email", "");
		createTextField(p2, "", "wrap");
		createLabel(p2, "Address 1", "");
		createTextField(p2, "", "span");
		createLabel(p2, "Address 2", "");
		createTextField(p2, "", "span");
		createLabel(p2, "City", "");
		createTextField(p2, "", "wrap");
		createLabel(p2, "State", "");
		createTextField(p2, "", "");
		createLabel(p2, "Postal Code", "");
		createTextField(p2, "", "width 50, grow 0, wrap");
		createLabel(p2, "Country", "");
		createTextField(p2, "", "wrap 15");

		createButton(p2, "New", "tag other, span, split");
		createButton(p2, "Delete", "tag other");
		createButton(p2, "Edit", "tag other");
		createButton(p2, "Save", "tag other");
		createButton(p2, "Cancel", "tag cancel, wrap push");
	}

	public void createAPIConstraints1() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		final IMigLayoutToolkit cf = LFP.getMigLayoutToolkit();

		final ILC layC = cf.lc().fill().wrap();
		final IAC colC = cf.ac().align("right", 0).fill(1, 3).grow(100, 1, 3).align("right", 2).gap("15", 1);
		final IAC rowC = cf.ac().align("top", 7).gap("15!", 6).grow(100, 8);

		final ITabItem p1 = result.addItem(BPF.tabItem().setText("Layout Showdown (improved)"));
		p1.setLayout(LFP.migLayoutBuilder().constraints(layC).columnConstraints(colC).rowConstraints(rowC).build());

		// References to text fields not stored to reduce code clutter.

		createList(p1, "Mouse, Mickey", cf.cc().dockWest().minWidth("150").gapX(null, "10"));
		createLabel(p1, "Last Name", "");
		createTextField(p1, "", "");
		createLabel(p1, "First Name", "");
		createTextField(p1, "", cf.cc().wrap());
		createLabel(p1, "Phone", "");
		createTextField(p1, "", "");
		createLabel(p1, "Email", "");
		createTextField(p1, "", "");
		createLabel(p1, "Address 1", "");
		createTextField(p1, "", cf.cc().spanX().growX());
		createLabel(p1, "Address 2", "");
		createTextField(p1, "", cf.cc().spanX().growX());
		createLabel(p1, "City", "");
		createTextField(p1, "", cf.cc().wrap());
		createLabel(p1, "State", "");
		createTextField(p1, "", "");
		createLabel(p1, "Postal Code", "");
		createTextField(p1, "", cf.cc().spanX(2).growX(0));
		createLabel(p1, "Country", "");
		createTextField(p1, "", cf.cc().wrap());

		createButton(p1, "New", cf.cc().spanX(5).split(5).tag("other"));
		createButton(p1, "Delete", cf.cc().tag("other"));
		createButton(p1, "Edit", cf.cc().tag("other"));
		createButton(p1, "Save", cf.cc().tag("other"));
		createButton(p1, "Cancel", cf.cc().tag("cancel"));
	}

	public void createAPIConstraints2() {
		final ITabFolder result = createTabFolder(layoutDisplayPanel);

		final IMigLayoutToolkit cf = LFP.getMigLayoutToolkit();

		final ILC layC = cf.lc().fill().wrap();
		final IAC colC = cf.ac().align("right", 0).fill(1, 3).grow(100, 1, 3).align("right", 2).gap("15", 1);
		final IAC rowC = cf.ac().index(6).gap("15!").align("top").grow(100, 8);

		final ITabItem p1 = result.addItem(BPF.tabItem().setText("Layout Showdown (improved)"));
		p1.setLayout(LFP.migLayoutBuilder().constraints(layC).columnConstraints(colC).rowConstraints(rowC).build());

		// References to text fields not stored to reduce code clutter.

		createLabel(p1, "Last Name", "");
		createTextField(p1, "", "");
		createLabel(p1, "First Name", "");
		createTextField(p1, "", cf.cc().wrap());
		createLabel(p1, "Phone", "");
		createTextField(p1, "", "");
		createLabel(p1, "Email", "");
		createTextField(p1, "", "");
		createLabel(p1, "Address 1", "");
		createTextField(p1, "", cf.cc().spanX().growX());
		createLabel(p1, "Address 2", "");
		createTextField(p1, "", cf.cc().spanX().growX());
		createLabel(p1, "City", "");
		createTextField(p1, "", cf.cc().wrap());
		createLabel(p1, "State", "");
		createTextField(p1, "", "");
		createLabel(p1, "Postal Code", "");
		createTextField(p1, "", cf.cc().spanX(2).growX(0));
		createLabel(p1, "Country", "");
		createTextField(p1, "", cf.cc().wrap());

		createButton(p1, "New", cf.cc().spanX(5).split(5).tag("other"));
		createButton(p1, "Delete", cf.cc().tag("other"));
		createButton(p1, "Edit", cf.cc().tag("other"));
		createButton(p1, "Save", cf.cc().tag("other"));
		createButton(p1, "Cancel", cf.cc().tag("cancel"));
	}

	private void windowMoved() {
		if (windowMovedListeningWidget != null) {
			windowMovedListeningWidget.layoutBegin();
			windowMovedListeningWidget.layoutEnd();
			windowMovedListeningWidget.redraw();
		}
	}

	private static void addSeparator(final IContainer panel, final String text) {
		final ITextLabel l = createLabel(panel, text, "gapbottom 1, span, split 2, aligny center");
		l.setForegroundColor(LABEL_COLOR);
		panel.add(BPF.separator(), "gapleft rel, growx");
	}

	private static ITextLabel createLabel(final IContainer parent, final String text, final Object layoutdata) {
		return parent.add(BPF.textLabel(text), layoutdata != null ? layoutdata : text);
	}

	private static ITextControl createTextField(final IContainer parent, final String text, final Object layoutdata) {
		return parent.add(BPF.textField().setText(text), layoutdata != null ? layoutdata : text);
	}

	private static ITextControl createTextArea(final IContainer parent, final String text, final Object layoutdata) {
		return parent.add(BPF.textArea().setText(text), layoutdata != null ? layoutdata : text);
	}

	private static IComboBox<String> createCombo(final IContainer parent, final String[] texts, final Object layoutdata) {
		final IComboBox<String> b = parent.add(BPF.comboBox().setElements(texts), layoutdata);
		b.setEditable(true);
		return b;
	}

	private static IButton createButton(final IContainer parent, final String text, final Object layoutdata) {
		return createButton(parent, text, layoutdata, false);
	}

	private static IButton createButton(final IContainer parent, final String text, final Object layoutdata, final boolean bold) {
		final IButton b = parent.add(BPF.button(), layoutdata != null ? layoutdata : text);
		b.setText(text.length() == 0 ? "\"\"" : text);
		// TODO NM set bold ?
		return b;
	}

	private IToggleButton createToggleButton(final IContainer parent, final String text, final Object layoutdata) {
		final IToggleButton b = parent.add(BPF.toggleButton().setText(text.length() == 0 ? "\"\"" : text), layoutdata != null
				? layoutdata : text);
		return b;
	}

	private static IComposite createPanel(final IContainer parent, String text, final Object layout) {
		final IComposite panel = parent.add(BPF.compositeWithBorder(), layout != null ? layout : text);
		panel.setLayout(LFP.migLayoutBuilder().constraints("fill").build());

		//QColor bg = new Color(display.getActiveShell().getDisplay(), 255, 255, 255);
		//panel.setBackground(bg);

		if (text != null) {
			text = text.length() == 0 ? "\"\"" : text;
			//final ITextLabel label = 
			createLabel(panel, text, "grow");
			//label.setAlignment(Qt.AlignmentFlag.AlignCenter);
			//label.setBackground(bg);
		}

		return panel;
	}

	private static IContainer createPanel(final IContainer parent, final ILayoutFactory<IMigLayout> layout) {
		final IComposite panel = parent.add(BPF.composite());
		if (layout != null) {
			panel.setLayout(layout);
		}

		return panel;
	}

	private static ICheckBox createCheck(final IContainer parent, final String text, final Object layoutdata) {
		final ICheckBox b = parent.add(BPF.checkBox().setText(text), layoutdata != null ? layoutdata : text);
		return b;
	}

	private IWidget createList(final IContainer parent, final String text, final Object layoutdata) {
		final ITree list = parent.add(BPF.tree(), (layoutdata != null) ? layoutdata : text);
		list.addNode().setText(text);
		return list;
	}

	//	public static Font deriveFont(final Font baseFont, final boolean bold, final int size) {
	//
	//		final boolean italic = false;
	//		final int weight;
	//		if (bold) {
	//			weight = QFont.Weight.Bold.value();
	//		}
	//		else {
	//			weight = QFont.Weight.Normal.value();
	//		}
	//
	//		final int usedSize;
	//		if (size < 0) {
	//			usedSize = baseFont.pointSize() + size;
	//		}
	//		else {
	//			usedSize = size;
	//		}
	//
	//		return new QFont(baseFont.family(), usedSize, weight, italic);
	//	}
}
