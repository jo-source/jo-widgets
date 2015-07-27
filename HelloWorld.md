## Hello World single sourcing example ##

The Hello World single sourcing example shows how to write jowidgets code and use it with Java Swing, Eclipse SWT or Eclipse RWT. The complete Hello World example can be found [here](http://code.google.com/p/jo-widgets/source/browse/#svn%2Ftrunk%2Fhelloworld).

<br>
<hr />

<h2>The Hello World example parent pom.xml</h2>

<pre><code>&lt;groupId&gt;org.jowidgets.helloworld&lt;/groupId&gt;<br>
&lt;artifactId&gt;org.jowidgets.helloworld.parent&lt;/artifactId&gt;<br>
&lt;version&gt;0.0.1-SNAPSHOT&lt;/version&gt;<br>
&lt;packaging&gt;pom&lt;/packaging&gt;<br>
<br>
&lt;properties&gt;<br>
    &lt;!-- jowidgets needs java 1.6 or higher --&gt;<br>
    &lt;java.version&gt;1.6&lt;/java.version&gt;<br>
    &lt;project.build.sourceEncoding&gt;UTF-8&lt;/project.build.sourceEncoding&gt;<br>
    &lt;jowidgets.version&gt;0.22.0&lt;/jowidgets.version&gt;<br>
&lt;/properties&gt;<br>
	<br>
&lt;repositories&gt;<br>
    &lt;!-- The jowidgets maven repository --&gt;<br>
    &lt;repository&gt;<br>
        &lt;id&gt;jowidgets&lt;/id&gt;<br>
        &lt;url&gt;http://jowidgets.org/maven2/&lt;/url&gt;<br>
        &lt;releases&gt;<br>
            &lt;enabled&gt;true&lt;/enabled&gt;<br>
        &lt;/releases&gt;<br>
        &lt;snapshots&gt;<br>
            &lt;enabled&gt;false&lt;/enabled&gt;<br>
        &lt;/snapshots&gt;<br>
    &lt;/repository&gt;<br>
&lt;/repositories&gt;<br>
	<br>
&lt;modules&gt;<br>
    &lt;!-- Hold the ui technology independend hello world code --&gt;<br>
    &lt;module&gt;../org.jowidgets.helloworld.common&lt;/module&gt;<br>
		<br>
    &lt;!-- Holds a starter that uses Java Swing --&gt;<br>
    &lt;module&gt;../org.jowidgets.helloworld.starter.swing&lt;/module&gt;<br>
		<br>
    &lt;!-- Holds a starter that uses Eclipse SWT (win32) --&gt;<br>
    &lt;module&gt;../org.jowidgets.helloworld.starter.swt&lt;/module&gt;<br>
		<br>
    &lt;!-- This module creates a war that uses Eclipse RWT --&gt;<br>
    &lt;module&gt;../org.jowidgets.helloworld.starter.rwt&lt;/module&gt;<br>
&lt;/modules&gt;<br>
	<br>
&lt;build&gt;<br>
    &lt;plugins&gt;<br>
        &lt;plugin&gt;<br>
            &lt;groupId&gt;org.apache.maven.plugins&lt;/groupId&gt;<br>
            &lt;artifactId&gt;maven-compiler-plugin&lt;/artifactId&gt;<br>
            &lt;version&gt;2.3.2&lt;/version&gt;<br>
            &lt;configuration&gt;<br>
                &lt;source&gt;${java.version}&lt;/source&gt;<br>
                &lt;target&gt;${java.version}&lt;/target&gt;<br>
            &lt;/configuration&gt;<br>
        &lt;/plugin&gt;<br>
    &lt;/plugins&gt;<br>
&lt;/build&gt;<br>
</code></pre>

The complete pom.xml can be found <a href='http://code.google.com/p/jo-widgets/source/browse/trunk/helloworld/org.jowidgets.helloworld.common/pom.xml'>here</a>.<br>
<br>
<br>
<hr />
<h2>The common HelloWorldApplication</h2>

<pre><code>package org.jowidgets.helloworld.common;<br>
<br>
import org.jowidgets.api.toolkit.Toolkit;<br>
import org.jowidgets.api.widgets.IButton;<br>
import org.jowidgets.api.widgets.IFrame;<br>
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;<br>
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;<br>
import org.jowidgets.common.application.IApplication;<br>
import org.jowidgets.common.application.IApplicationLifecycle;<br>
import org.jowidgets.common.types.Dimension;<br>
import org.jowidgets.common.widgets.controller.IActionListener;<br>
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;<br>
import org.jowidgets.tools.widgets.blueprint.BPF;<br>
<br>
public final class HelloWorldApplication implements IApplication {<br>
<br>
    public void start(final IApplicationLifecycle lifecycle) {<br>
		<br>
        //Create a frame BluePrint with help of the BluePrintFactory (BPF)<br>
        final IFrameBluePrint frameBp = BPF.frame();<br>
        frameBp.setSize(new Dimension(400, 300)).setTitle("Hello World");<br>
<br>
        //Create a frame with help of the Toolkit and BluePrint. This convenience<br>
        //method finishes the ApplicationLifecycle when the root frame will be closed.<br>
        final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);<br>
<br>
        //Use a simple MigLayout with one column and one row for the frame (a frame is a container also)<br>
        frame.setLayout(new MigLayoutDescriptor("[]", "[]"));<br>
<br>
        //Create a button BluePrint with help of the BluePrintFactory (BPF)<br>
        final IButtonBluePrint buttonBp = BPF.button().setText("Hello World");<br>
<br>
        //Add the button defined by the BluePrint to the frame<br>
        final IButton button = frame.add(buttonBp);<br>
<br>
        //Add an ActionListener to the button<br>
        button.addActionListener(new IActionListener() {<br>
            public void actionPerformed() {<br>
                System.out.println("Hello World");<br>
            }<br>
        });<br>
<br>
        //set the root frame visible<br>
        frame.setVisible(true);<br>
    }<br>
}<br>
</code></pre>

The complete code can be found <a href='http://code.google.com/p/jo-widgets/source/browse/trunk/helloworld/org.jowidgets.helloworld.common/src/main/java/org/jowidgets/helloworld/common/HelloWorldApplication.java'>here</a>.<br>
<br>
<br>

<h2>The common pom.xml</h2>

The common Hello World Application has no dependencies to special ui frameworks and can be used with any supported spi.<br>
<br>
<pre><code>&lt;artifactId&gt;org.jowidgets.helloworld.common&lt;/artifactId&gt;<br>
<br>
&lt;parent&gt;<br>
    &lt;groupId&gt;org.jowidgets.helloworld&lt;/groupId&gt;<br>
    &lt;artifactId&gt;org.jowidgets.helloworld.parent&lt;/artifactId&gt;<br>
    &lt;version&gt;0.0.1-SNAPSHOT&lt;/version&gt;<br>
    &lt;relativePath&gt;../parent/pom.xml&lt;/relativePath&gt;<br>
&lt;/parent&gt;<br>
	<br>
&lt;dependencies&gt;<br>
<br>
    &lt;!-- The jowidgets api --&gt;<br>
    &lt;dependency&gt;<br>
        &lt;groupId&gt;org.jowidgets&lt;/groupId&gt;<br>
        &lt;artifactId&gt;org.jowidgets.api&lt;/artifactId&gt;<br>
        &lt;version&gt;${jowidgets.version}&lt;/version&gt;<br>
    &lt;/dependency&gt;<br>
<br>
    &lt;!-- Some utility classes like wrappers or abstract base classes --&gt;<br>
    &lt;dependency&gt;<br>
        &lt;groupId&gt;org.jowidgets&lt;/groupId&gt;<br>
        &lt;artifactId&gt;org.jowidgets.tools&lt;/artifactId&gt;<br>
        &lt;version&gt;${jowidgets.version}&lt;/version&gt;<br>
    &lt;/dependency&gt;<br>
<br>
&lt;/dependencies&gt;<br>
</code></pre>

The complete pom.xml can be found <a href='http://code.google.com/p/jo-widgets/source/browse/trunk/helloworld/org.jowidgets.helloworld.common/pom.xml'>here</a>.<br>
<br>
<br>
<hr />

<h2>The Hello World Swing starter</h2>

The Hello World Swing starter starts the HelloWorldApplication with the jowidgets Swing SPI implementation.<br>
<br>
<pre><code>package org.jowidgets.helloworld.starter.swing;<br>
<br>
import javax.swing.UIManager;<br>
<br>
import org.jowidgets.api.toolkit.Toolkit;<br>
import org.jowidgets.helloworld.common.HelloWorldApplication;<br>
<br>
public final class HelloWorldStarterSwing {<br>
<br>
    private HelloWorldStarterSwing() {}<br>
<br>
    public static void main(final String[] args) throws Exception {<br>
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());<br>
        System.setProperty("apple.laf.useScreenMenuBar", "true");<br>
        Toolkit.getApplicationRunner().run(new HelloWorldApplication());<br>
        System.exit(0);<br>
    }<br>
}<br>
</code></pre>

The complete code can be found <a href='http://code.google.com/p/jo-widgets/source/browse/trunk/helloworld/org.jowidgets.helloworld.starter.swing/src/main/java/org/jowidgets/helloworld/starter/swing/HelloWorldStarterSwing.java'>here</a>.<br>
<br>
<br>
<h2>The Swing starter pom.xml</h2>

<pre><code>&lt;artifactId&gt;org.jowidgets.helloworld.starter.swing&lt;/artifactId&gt;<br>
<br>
&lt;parent&gt;<br>
    &lt;groupId&gt;org.jowidgets.helloworld&lt;/groupId&gt;<br>
    &lt;artifactId&gt;org.jowidgets.helloworld.parent&lt;/artifactId&gt;<br>
    &lt;version&gt;0.0.1-SNAPSHOT&lt;/version&gt;<br>
    &lt;relativePath&gt;../parent/pom.xml&lt;/relativePath&gt;<br>
&lt;/parent&gt;<br>
	<br>
&lt;dependencies&gt;<br>
<br>
    &lt;!-- The ui technology independend hello world module --&gt;<br>
    &lt;dependency&gt;<br>
        &lt;groupId&gt;org.jowidgets.helloworld&lt;/groupId&gt;<br>
        &lt;artifactId&gt;org.jowidgets.helloworld.common&lt;/artifactId&gt;<br>
        &lt;version&gt;0.0.1-SNAPSHOT&lt;/version&gt;<br>
    &lt;/dependency&gt;<br>
<br>
    &lt;!-- The default implementation of the jowidgets api --&gt;<br>
    &lt;dependency&gt;<br>
        &lt;groupId&gt;org.jowidgets&lt;/groupId&gt;<br>
        &lt;artifactId&gt;org.jowidgets.impl&lt;/artifactId&gt;<br>
        &lt;version&gt;${jowidgets.version}&lt;/version&gt;<br>
    &lt;/dependency&gt;<br>
<br>
    &lt;!-- The Swing implementation of the jowidgets spi --&gt;<br>
    &lt;dependency&gt;<br>
        &lt;groupId&gt;org.jowidgets&lt;/groupId&gt;<br>
        &lt;artifactId&gt;org.jowidgets.spi.impl.swing&lt;/artifactId&gt;<br>
        &lt;version&gt;${jowidgets.version}&lt;/version&gt;<br>
    &lt;/dependency&gt;<br>
<br>
&lt;/dependencies&gt;<br>
</code></pre>

The complete pom.xml can be found <a href='http://code.google.com/p/jo-widgets/source/browse/trunk/helloworld/org.jowidgets.helloworld.starter.swing/pom.xml'>here</a>.<br>
<br>
<br>
<hr />

<h2>The Hello World SWT starter</h2>

The Hello World SWT starter starts the HelloWorldApplication with the jowidgets SWT SPI implementation.<br>
<br>
<pre><code>import org.jowidgets.api.toolkit.Toolkit;<br>
import org.jowidgets.helloworld.common.HelloWorldApplication;<br>
<br>
public final class HelloWorldStarterSwt {<br>
<br>
    private HelloWorldStarterSwt() {}<br>
<br>
    public static void main(final String[] args) throws Exception {<br>
        Toolkit.getApplicationRunner().run(new HelloWorldApplication());<br>
        System.exit(0);<br>
    }<br>
}<br>
</code></pre>

The complete code can be found <a href='http://code.google.com/p/jo-widgets/source/browse/trunk/helloworld/org.jowidgets.helloworld.starter.swt/src/main/java/org/jowidgets/helloworld/starter/swt/HelloWorldStarterSwt.java'>here</a>.<br>
<br>
<br>
<h2>The SWT starter pom.xml</h2>

<pre><code>&lt;artifactId&gt;org.jowidgets.helloworld.starter.swt&lt;/artifactId&gt;<br>
<br>
&lt;parent&gt;<br>
    &lt;groupId&gt;org.jowidgets.helloworld&lt;/groupId&gt;<br>
    &lt;artifactId&gt;org.jowidgets.helloworld.parent&lt;/artifactId&gt;<br>
    &lt;version&gt;0.0.1-SNAPSHOT&lt;/version&gt;<br>
    &lt;relativePath&gt;../parent/pom.xml&lt;/relativePath&gt;<br>
&lt;/parent&gt;<br>
	<br>
&lt;dependencies&gt;<br>
<br>
    &lt;!-- The ui technology independend hello world module --&gt;<br>
    &lt;dependency&gt;<br>
        &lt;groupId&gt;org.jowidgets.helloworld&lt;/groupId&gt;<br>
        &lt;artifactId&gt;org.jowidgets.helloworld.common&lt;/artifactId&gt;<br>
        &lt;version&gt;0.0.1-SNAPSHOT&lt;/version&gt;<br>
    &lt;/dependency&gt;<br>
<br>
    &lt;!-- The default implementation of the jowidgets api --&gt;<br>
    &lt;dependency&gt;<br>
        &lt;groupId&gt;org.jowidgets&lt;/groupId&gt;<br>
        &lt;artifactId&gt;org.jowidgets.impl&lt;/artifactId&gt;<br>
        &lt;version&gt;${jowidgets.version}&lt;/version&gt;<br>
    &lt;/dependency&gt;<br>
<br>
    &lt;!-- The SWT implementation of the jowidgets spi --&gt;<br>
    &lt;dependency&gt;<br>
        &lt;groupId&gt;org.jowidgets&lt;/groupId&gt;<br>
        &lt;artifactId&gt;org.jowidgets.spi.impl.swt&lt;/artifactId&gt;<br>
        &lt;version&gt;${jowidgets.version}&lt;/version&gt;<br>
    &lt;/dependency&gt;<br>
		<br>
    &lt;!-- The SWT implementation for win32 --&gt;<br>
    &lt;dependency&gt;<br>
        &lt;groupId&gt;org.eclipse&lt;/groupId&gt;<br>
        &lt;artifactId&gt;swt-win32-win32-x86&lt;/artifactId&gt;<br>
        &lt;version&gt;4.2.1&lt;/version&gt;<br>
    &lt;/dependency&gt;<br>
<br>
&lt;/dependencies&gt;<br>
</code></pre>

The complete pom.xml can be found <a href='http://code.google.com/p/jo-widgets/source/browse/trunk/helloworld/org.jowidgets.helloworld.starter.swt/pom.xml'>here</a>.