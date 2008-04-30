package com.objogate.wl.driver;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.text.JTextComponent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.objogate.exception.Defect;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Probe;
import com.objogate.wl.Prober;
import com.objogate.wl.UI;
import com.objogate.wl.gesture.GesturePerformer;

public class JFileChooserDriver extends ComponentDriver<JFileChooser> {
    @SuppressWarnings("unchecked")
    public JFileChooserDriver(ComponentDriver<? extends Component> parentOrOwner, Matcher<? super JFileChooser> matcher) {
        super(parentOrOwner, JFileChooser.class, matcher);
    }

    public JFileChooserDriver(GesturePerformer gesturePerformer, ComponentSelector<JFileChooser> selector, Prober prober) {
        super(gesturePerformer, selector, prober);
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
        isShowingOnScreen();
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

    public void intoDir(String name) {
        chooserUI().intoDir(name);
    }

    public void createNewFolder(String folderName) {
        chooserUI().createNewFolder(folderName);
    }

    public void selectFile(String fileName) {
        chooserUI().selectFile(fileName);
    }

    public void currentDirectory(final File expectedDir) {
        currentDirectory(new TypeSafeMatcher<File>() {
            @Override
            public boolean matchesSafely(File file) {
                try {
                    return file.getCanonicalPath().equals(expectedDir.getCanonicalPath());
                } catch (IOException e) {
                    throw new Defect("Cannot get path of directory " + expectedDir);
                }
            }

            public void describeTo(Description description) {
                try {
                    description.appendText(expectedDir.getCanonicalPath());
                } catch (IOException e) {
                    throw new Defect("Cannot get path of directory " + expectedDir);
                }
            }
        });
    }

    public void currentDirectory(final Matcher<File> matcher) {
        check(new Probe() {
            File currentDirectory;

            public void probe() {
                currentDirectory = component().component().getCurrentDirectory();
            }

            public boolean isSatisfied() {
                return matcher.matches(currentDirectory);
            }

            public void describeTo(Description description) {
                description.appendText("current directory matches ").
                        appendDescriptionOf(matcher);
            }

            public boolean describeFailureTo(Description description) {
                description.appendText("current directory was ")
                        .appendValue(currentDirectory);
                return !isSatisfied();
            }
        });
    }

    public static JFrame rootFrameFor(Component parentComponent)
            throws HeadlessException 
    {
        if (parentComponent == null) throw new IllegalArgumentException("Dialog needs a parent");
        if (parentComponent instanceof JFrame) return (JFrame) parentComponent;
        return rootFrameFor(parentComponent.getParent());
    }
}
