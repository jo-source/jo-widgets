## Base Widgets{#base_widgets}

Die Base Widgets können zur Kapselung eigener Widgets verwendet werden, ohne dass man dazu eine [eigene Widget Bibliotheken](#custom_widget_libraries) erstellen muss. Sie stellen somit die _klassische_ Variante zur Erstellung eigener Widgets dar. Um alle Vorteile von jowidgets zu nutzen wird jedoch empfohlen, Widget in einer [eigenen Widget Bibliotheken](#custom_widget_libraries) zu kapseln.

Die Base Widgets sind von den [Widget Wrappern](#widget_wrapper) abgeleitet. Es ist auch einfach möglich, eigene Base Widgets zu erstellen. Die Base Widgets befinden sich im Packet `org.jowidgets.tools.widgets.base`.

Derzeit existieren die folgenden Base Widgets:

* [Frame](#base_frame)
* [Dialog](#base_dialog)
* [Composite Control](#base_composite_control)

### Frame{#base_frame}

Die Klasse `Frame` ist wie folgt implementiert:

~~~{.java .numberLines startFrom="1"}
public class Frame extends FrameWrapper implements IFrame {

    public Frame(final String title, final IImageConstant icon) {
        this(BPF.frame(title).setIcon(icon));
    }

    public Frame(final String title) {
        this(BPF.frame(title));
    }

    public Frame(final IFrameDescriptor descriptor) {
        super(Toolkit.createRootFrame(descriptor));
    }

    protected IFrame getFrame() {
        return (IFrame) super.getWidget();
    }

}
~~~

Das [BaseFrameSnipped](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.common/src/main/java/org/jowidgets/examples/common/snipped/BaseFrameSnipped.java) demonstriert die Verwendung der Klasse `Frame`:

~~~{.java .numberLines startFrom="1"}
public final class BaseFrameSnipped implements IApplication {

    @Override
    public void start(final IApplicationLifecycle lifecycle) {

        final IFrame frame = new MyFrame();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed() {
                lifecycle.finish();
            }
        });

        frame.setVisible(true);
    }

    private final class MyFrame extends Frame {

        public MyFrame() {
            super("Base frame Snipped");

            setMinPackSize(new Dimension(300, 0));

            setLayout(new MigLayoutDescriptor("wrap", "[][grow]", "[][]"));

            add(BPF.textLabel("Label 1"));
            add(BPF.inputFieldString(), "growx");

            add(BPF.textLabel("Label 2"));
            add(BPF.inputFieldString(), "growx");
        }

    }
}
~~~

### Dialog{#base_dialog}

Die Klasse `Dialog` ist wie folgt implementiert:

~~~{.java .numberLines startFrom="1"}
public class Dialog extends FrameWrapper implements IFrame {

    public Dialog(final IWindow parent, final String title, final IImageConstant icon) {
        this(parent, BPF.dialog(title).setIcon(icon));
    }

    public Dialog(final IWindow parent, final String title) {
        this(parent, BPF.dialog(title));
    }
	
    public Dialog(final IWindow parent, final IWidgetDescriptor<? extends IFrame> descriptor) {
        super(Toolkit.getWidgetFactory().create(
			Assert.getParamNotNull(parent, "parent").getUiReference(), 
			descriptor));
    }

    protected IFrame getFrame() {
        return (IFrame) super.getWidget();
    }
}
~~~

Das [BaseDialogSnipped](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.common/src/main/java/org/jowidgets/examples/common/snipped/BaseDialogSnipped.java) demonstriert die Verwendung der Klasse `Dialog`:

~~~{.java .numberLines startFrom="1"}
public final class BaseDialogSnipped implements IApplication {

    @Override
    public void start(final IApplicationLifecycle lifecycle) {

        //create the root frame
        final IFrameBluePrint frameBp = BPF.frame().setTitle("Base dialog Snipped");
        frameBp.setSize(new Dimension(300, 200));
        final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);
        frame.setLayout(new MigLayoutDescriptor("[]", "[]"));

        //create a dialog
        final IFrame dialog = new MyDialog(frame);

        //create a button that opens the dialog
        final IButton button = frame.add(BPF.button("Open dialog"));
        button.addActionListener(new IActionListener() {
            @Override
            public void actionPerformed() {
                dialog.setVisible(true);
            }
        });

        //set the frame visible
        frame.setVisible(true);
    }

    private final class MyDialog extends Dialog {

        public MyDialog(final IWindow parent) {
            super(parent, "My Dialog");

            setMinPackSize(new Dimension(300, 0));

            setLayout(new MigLayoutDescriptor("wrap", "[][grow]", "[][]"));

            add(BPF.textLabel("Label 1"));
            add(BPF.inputFieldString(), "growx");

            add(BPF.textLabel("Label 2"));
            add(BPF.inputFieldString(), "growx");
        }
		
    }
}
~~~

### Composite Control{#base_composite_control}

Das Composite Control implementiert die Schnittstelle `IControl` und bietet ein `IComposite` als Content Pane zum Hinzufügen von Controls.

Die Klasse `CompositeControl` ist wie folgt implementiert:

~~~{.java .numberLines startFrom="1"}
public class CompositeControl extends ControlWrapper implements IControl {

    public CompositeControl(final IContainer parent) {
        this(parent, BPF.composite(), null);
    }

    public CompositeControl(
		final IContainer parent, 
		final int index) {
		
        this(parent, index, BPF.composite(), null);
    }

    public CompositeControl(
		final IContainer parent, 
		final ICompositeDescriptor descriptor) {
		
        this(parent, descriptor, null);
    }

    public CompositeControl(
		final IContainer parent, 
		final int index, 
		final ICompositeDescriptor descriptor) {
       
		this(parent, index, descriptor, null);
    }

    public CompositeControl(
		final IContainer parent, 
		final Object layoutConstraints) {
       
		this(parent, BPF.composite(), layoutConstraints);
    }

    public CompositeControl(
		final IContainer parent, 
		final int index, 
		final Object layoutConstraints) {
		
        this(parent, index, BPF.composite(), layoutConstraints);
    }

    public CompositeControl(
		final IContainer parent, 
		final ICompositeDescriptor descriptor, 
		final Object layoutConstraints) {
       
		super(Assert.getParamNotNull(parent, "parent").add(
			descriptor, 
			layoutConstraints));
    }

    public CompositeControl(
        final IContainer parent,
        final int index,
        final ICompositeDescriptor descriptor,
        final Object layoutConstraints) {
        
		super(Assert.getParamNotNull(parent, "parent").add(
				index, 
				descriptor, 
				layoutConstraints));
    }

    protected IComposite getComposite() {
        return (IComposite) super.getWidget();
    }

}
~~~


Das [BaseCompositeControlSnipped](http://code.google.com/p/jo-widgets/source/browse/trunk/modules/examples/org.jowidgets.examples.common/src/main/java/org/jowidgets/examples/common/snipped/BaseCompositeControlSnipped.java) demonstriert die Verwendung der Klasse `Dialog`:

~~~{.java .numberLines startFrom="1"}
public final class BaseCompositeControlSnipped implements IApplication {

    @Override
    public void start(final IApplicationLifecycle lifecycle) {

        final IFrame frame = new MyFrame();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed() {
                lifecycle.finish();
            }
        });

        frame.setVisible(true);
    }

    private final class MyFrame extends Frame {

        public MyFrame() {
            super("Base composite control snipped");

            setMinPackSize(new Dimension(300, 0));

            setLayout(FillLayout.get());

            final MyControl control = new MyControl(this, "growx, growy");
            control.setText1("Hello");
            control.setText2("World");
        }

    }

    private final class MyControl extends CompositeControl {

        private final IInputField<String> field1;
        private final IInputField<String> field2;

        public MyControl(final IContainer parent, final Object layoutConstraints) {
            super(parent, layoutConstraints);

            final IComposite composite = getComposite();

            composite.setLayout(new MigLayoutDescriptor("wrap", "[][grow]", "[][]"));

            composite.add(BPF.textLabel("Label 1"));
            field1 = composite.add(BPF.inputFieldString(), "growx");

            composite.add(BPF.textLabel("Label 2"));
            field2 = composite.add(BPF.inputFieldString(), "growx");
        }

        public void setText1(final String text) {
            field1.setValue(text);
        }

        public void setText2(final String text) {
            field2.setValue(text);
        }

    }
}
~~~

