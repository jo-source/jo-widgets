### Die Schnittstelle IItem{#item_interface}

[Items](#item_interface) sind zum Einen die Elemente von [Menüs](#menus_and_items) oder [Toolbars](#toolbar_widget). Weitere Items sind TabItems, also die _Reiter_ eines [TabFolder](#tabFolder) sowie die Nodes eines [Tree](#tree_widget). Items sind somit keine eigenständigen Komponenten sondern Teil anderer Komponenten oder Menüs. Eine TreeNode kann nicht ohne zugehörigen Tree existieren, ein TabItem nicht ohne zugehörigen TabFolder und ein MenuItem nicht ohne zugehöriges Menü.

Items können einen Text (Label^[Die Eigenschaft `text` sollte passender `label` heißen. Angelehnt an das SWT _wording_ wurde jedoch anfangs `text` gewählt. Dies nachträglich zu ändern würde einen nicht unerheblichen Aufwand mit sich bringen und müsste dann konsequenterweise auch an anderen Stellen (z.B. beim Button) umgesetzt werden.]), ein Tooltip und ein Icon haben. Zum Setzen und Auslesen dieser Eigenschaften existieren die folgenden Methoden:

~~~
	void setText(String text);

	String getText();

	void setToolTipText(String text);
	
	String getToolTipText();

	IImageConstant getIcon();
	
	void setIcon(IImageConstant icon);
~~~

Die Verwendung von Icons wird im Abschnitt [Icons und Images](#icons_and_images) beschrieben.


