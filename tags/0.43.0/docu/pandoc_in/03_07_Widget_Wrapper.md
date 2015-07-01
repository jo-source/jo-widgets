## Widget Wrapper{#widget_wrapper}

Für die meisten Widget Schnittstellen existieren Wrapper Klassen, welche es ermöglichen, von einem Widget abzuleiten und eigene Funktionen hinzuzufügen. Die Wrapper befinden sich im Packet `org.jowidgets.tools.widgets.wrapper`. Die folgenden Wrapper sind derzeit vorhanden:

* ButtonWrapper
* CheckBoxWrapper
* ComboBoxWrapper
* ComponentWrapper
* CompositeWrapper
* ContainerWrapper
* ControlWrapper
* DisplayWrapper
* FrameWrapper
* InputControlWrapper
* InputDialogWrapper
* TabFolderWrapper
* TableWrapper
* TreeWrapper
* WidgetWrapper
* WindowWrapper

Die Implementierung eines Wrappers ist immer nach dem gleichen Schechma aufgebaut. Das folgende beispiel zeigt den `ComboBoxWrapper`:

~~~{.java .numberLines startFrom="1"}
package org.jowidgets.tools.widgets.wrapper;

import java.util.Collection;
import java.util.List;

import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.util.IObservableValue;

public class ComboBoxWrapper<VALUE_TYPE> 
	extends InputControlWrapper<VALUE_TYPE> 
	implements IComboBox<VALUE_TYPE> {

    public ComboBoxWrapper(final IComboBox<VALUE_TYPE> widget) {
        super(widget);
    }

    @Override
    protected IComboBox<VALUE_TYPE> getWidget() {
        return (IComboBox<VALUE_TYPE>) super.getWidget();
    }

    @Override
    public IObservableValue<VALUE_TYPE> getObservableValue() {
        return getWidget().getObservableValue();
    }

    @Override
    public List<VALUE_TYPE> getElements() {
        return getWidget().getElements();
    }

    @Override
    public void setElements(final Collection<? extends VALUE_TYPE> elements) {
        getWidget().setElements(elements);
    }

	// ... removed some methods in example

    @Override
    public boolean isPopupVisible() {
        return getWidget().isPopupVisible();
    }

}
~~~

Falls für eine Widget Schnittstelle noch kein Wrapper existiert, kann ein solcher also einfach selbst erstellt werden^[Patches mit solchen Erweiterungen werden dankbar angenommen und integriert.
]. 



### Widget Wrapper in Kombination mit der IWidgetFactory

Widget Wrapper werden unter anderem bei der [Erstellung eigener Widget Bibliotheken](#custom_widget_libraries) benötigt. 

Das folgende Beispiel zeigt die Widget Factory und Implementierung des Label Widget. Ein Label Widget besteht aus einen Icon und einem Text Label.

~~~{.java .numberLines startFrom="1"}
public final class LabelFactory implements IWidgetFactory<ILabel, ILabelDescriptor> {

    @Override
    public ILabel create(final Object parentUiReference, final ILabelDescriptor descriptor) {
        final ICompositeBluePrint compositeBp = BPF.composite();
        compositeBp.setSetup(descriptor);

        final IComposite composite = Toolkit.getWidgetFactory().create(
			parentUiReference, 
			compositeBp);
			
        return new LabelImpl(composite, descriptor);
    }

}
~~~

Für die Implementierung soll ein Composite verwendet werden, welches das Icon und das Text Label enthält. In Zeile 5 wird ein Composite BluePrint erstellt. In Zeile 6 werden alle passenden Parameter des Label Descriptor auch auf dem Composite gesetzt. In Zeile 8 wird das Composite erzeugt und in Zeile 12 wir dieses an die Klasse `LabelImpl` übergeben.

Die Klasse `LabelImpl` sieht wie folgt aus:

~~~{.java .numberLines startFrom="1"}
public final class LabelImpl extends ControlWrapper implements ILabel {

    private final IIcon iconWidget;
    private final ITextLabel textLabelWidget;
    private final IComposite composite;

    private String text;
    private IImageConstant icon;

    public LabelImpl(final IComposite composite, final ILabelSetup setup) {
        super(composite);

        this.composite = composite;

        final IIconDescriptor iconDescriptor = BPF.icon(setup.getIcon()).setSetup(setup);
        this.iconWidget = composite.add(iconDescriptor, "w 0::");
        this.icon = setup.getIcon();

        final ITextLabelDescriptor textLabelDescriptor = BPF.textLabel().setSetup(setup);
        this.textLabelWidget = composite.add(textLabelDescriptor, "w 0::");

        setLayout();

        VisibiliySettingsInvoker.setVisibility(setup, this);
        ColorSettingsInvoker.setColors(setup, this);
    }

    @Override
    public void setToolTipText(final String text) {
        textLabelWidget.setToolTipText(text);
        iconWidget.setToolTipText(text);
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        textLabelWidget.setForegroundColor(colorValue);
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        textLabelWidget.setBackgroundColor(colorValue);
        iconWidget.setBackgroundColor(colorValue);
    }

	//... removed some methods in example

}
~~~

Es wird von der Klasse `ControlWrapper` abgeleitet. Dadurch werden alle Methoden von `IControl`, die nicht von `LabelImpl` überschrieben werden, an das Composite delegiert. 

__Hinweis:__ Es wurde bewusst nicht von `CompositeWrapper` abgeleitet, da die Schnittstelle `ILabel` auch nicht von `IComposite` abgeleitet ist. Bei der Verwendung von Swing oder Swt findet man häufig eigene, firmen- oder projektinterne Widgets, die zum Beispiel von JPanel oder Composite angeleitet sind. Diese haben dadurch in Ihrer öffentlichen Schnittstelle Methoden wie `remove()`, `add()` oder `setLayout()` welche vom Benutzer des Widgets nicht verwendet werden sollten (zum Beispiel hat ein Label Widget keine `setLayout()` oder `remove()` Methode). Bei der [Erstellung eigener Widget Bibliotheken](#custom_widget_libraries) kann man explizit die Schnittstelle für das Widget (z.B. ILabel) festlegen. Dadurch wäre auch das Ableiten von CompositeWrapper nicht tragisch, da die Widget Factory nur ein ILabel zurückgibt. Wird keine eigene Widget Schnittstelle festgelegt ist es jedoch umso wichtiger, keinen Wrapper zu verwenden, durch den man Methoden erbt, die für das Widget nicht sinvoll sind.



