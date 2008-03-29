package com.objogate.wl.driver;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import com.objogate.wl.Probe;
import com.objogate.wl.UI;
import com.objogate.wl.gesture.GesturePerformer;

public class JFileChooserDriver extends ComponentDriver<JFileChooser> {

    public JFileChooserDriver(GesturePerformer gesturePerformer, JFileChooser chooser) {
        super(gesturePerformer, chooser);
    }

    @SuppressWarnings("unchecked")
    public JFileChooserDriver(ComponentDriver<? extends Component> parentOrOwner, Matcher<? super JFileChooser> matcher) {
        super(parentOrOwner, JFileChooser.class, matcher);
    }

    private FileChooserUIDriver getRelevantComponentDriver() {
        if (UI.is(UI.METAL)) {
            return new MetalFileChooserUIDriver(this);
        } else if (UI.is(UI.WINDOWS)) {
            return new WindowsFileChooserUIDriver(this);
        } else if (UI.is(UI.GTK)) {
            return new GTKFileChooserUIDriver(this);
        } else if (UI.is(UI.AQUA)) {
            return new AquaFileChooserUIDriver(this);
        }

        throw new UnsupportedOperationException("not known about or supported yet ");
    }

    public void cancel() {
        chooserUI().cancel();
    }

    public void approve() {
        chooserUI().approve();
    }

    public void enterManually(String someText) {
        JTextComponentDriver<? extends JTextComponent> textComponentDriver = chooserUI().textBox();
        textComponentDriver.moveMouseToCenter();
        textComponentDriver.selectAll();
        textComponentDriver.typeText(someText);
    }

    private FileChooserUIDriver chooserUI() {
        return getRelevantComponentDriver();
    }

    public void desktop() {
        chooserUI().desktop();
    }

    public void documents() {
        chooserUI().documents();
    }

    public void home() {
        chooserUI().home();
    }

    public void upOneFolder() {
        chooserUI().upOneFolder();
    }

    public void createNewFolder(String folderName) {
        chooserUI().createNewFolder(folderName);
    }

    public void selectFile(String fileName) {
        chooserUI().selectFile(fileName);
    }

    //TODO: make the descriptions recursive
    public void currentDirectory(final Matcher<String> matcher) {
        check(new Probe() {
            File currentDirectory;

            public void probe() {
                currentDirectory = component().component().getCurrentDirectory();
            }

            public boolean isSatisfied() {
                return matcher.matches(currentDirectory.getAbsolutePath());
            }

            public void describeTo(Description description) {
                description.appendText("current directory matches ");
                description.appendDescriptionOf(matcher);
            }

            public boolean describeFailureTo(Description description) {
                description.appendText("current directory was ")
                        .appendValue(currentDirectory);
                return !isSatisfied();
            }
        });
    }

    public static JFrame rootFrameFor(Component parentComponent)
            throws HeadlessException {
        if (parentComponent == null) throw new IllegalArgumentException("Dialog needs a parent");
        if (parentComponent instanceof JFrame) return (JFrame) parentComponent;
        return rootFrameFor(parentComponent.getParent());
    }
}
