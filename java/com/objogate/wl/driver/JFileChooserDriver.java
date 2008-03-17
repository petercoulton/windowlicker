package com.objogate.wl.driver;

import javax.swing.*;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import com.sun.java.swing.plaf.motif.MotifFileChooserUI;
import com.sun.java.swing.plaf.windows.WindowsFileChooserUI;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;
import com.objogate.exception.Defect;
import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.Probe;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.matcher.ComponentMatchers;
import static com.objogate.wl.matcher.ComponentMatchers.withButtonText;
import static com.objogate.wl.matcher.ComponentMatchers.withFocus;
import com.objogate.wl.probe.ComponentIdentity;
import com.objogate.wl.probe.ComponentManipulatorProbe;
import com.objogate.wl.probe.NthComponentFinder;
import com.objogate.wl.probe.RecursiveComponentFinder;

public class JFileChooserDriver extends ComponentDriver<JFileChooser> {

    public JFileChooserDriver(GesturePerformer gesturePerformer, JFileChooser chooser) {
        super(gesturePerformer, chooser);
    }

    @SuppressWarnings("unchecked")
    public JFileChooserDriver(ComponentDriver<? extends Component> parentOrOwner, Matcher<? super JFileChooser> matcher) {
        super(parentOrOwner, JFileChooser.class, matcher);
    }

    private javax.swing.plaf.FileChooserUI findOutWhichUiWeAreUsing() {

        final javax.swing.plaf.FileChooserUI[] uis = new javax.swing.plaf.FileChooserUI[1];

        JFileChooser chooser = component().component();
        ComponentIdentity<JFileChooser> identity = new ComponentIdentity<JFileChooser>(chooser);
        check(new ComponentManipulatorProbe<JFileChooser>(identity, new ComponentManipulation<JFileChooser>() {
            public void manipulate(JFileChooser component) {
                uis[0] = component.getUI();
            }
        }));
        return uis[0];
    }

    private FileChooserDriverUI getRelevantComponentGetter(javax.swing.plaf.FileChooserUI ui) {
        String name = ui.getClass().getName();

        if (ui instanceof javax.swing.plaf.metal.MetalFileChooserUI) {
            return new MetalFileChooserDriverUI();
        } else if (ui instanceof MotifFileChooserUI) {
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                throw new Defect("programmer didn't handle", e);
            }
            throw new UnsupportedOperationException("not supported yet " + name);
        } else if (ui instanceof WindowsFileChooserUI) {
            throw new UnsupportedOperationException("not supported yet " + name);
        } else if (ui.getClass().getName().equals("com.sun.java.swing.plaf.gtk.GTKFileChooserUI")) {
            return new GTKFileChooserDriverUI();
        } else if (ui instanceof BasicFileChooserUI) {
            throw new UnsupportedOperationException("not supported yet " + name);
        }
        throw new UnsupportedOperationException("not known about or supported yet " + name);
    }

    public void cancel() {
        chooserUI().cancel();
    }

    public void ok() {
        chooserUI().ok();
    }


    public void enterManually(String someText) {
        JTextComponentDriver<? extends JTextComponent> textComponentDriver = chooserUI().textBox();
        textComponentDriver.moveMouseToCenter();
        textComponentDriver.selectAll();
        textComponentDriver.typeText(someText);
    }

    private FileChooserDriverUI chooserUI() {
        return getRelevantComponentGetter(findOutWhichUiWeAreUsing());
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
        JLabelDriver fileEntry = new JLabelDriver(this, the(JLabel.class, ComponentMatchers.withLabelText(equalTo(fileName))));
        fileEntry.leftClickOnComponent();
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

    private class GTKFileChooserDriverUI implements FileChooserDriverUI {
        public void createNewFolder(String folderName) {
            AbstractButtonDriver<JButton> newFolderButton = new AbstractButtonDriver<JButton>(JFileChooserDriver.this, the(JButton.class, ComponentDriver.named("GTKFileChooser.newFolderButton")));
            newFolderButton.click();

            JFrameDriver jframe = new JFrameDriver(gesturePerformer, rootFrameFor(JFileChooserDriver.this.component().component()));

            JOptionPaneDriver folderEntry = new JOptionPaneDriver(jframe, JOptionPane.class);
            folderEntry.typeText(folderName);
            folderEntry.clickOK();
        }

        @SuppressWarnings("unchecked")
        public void upOneFolder() {
            JListDriver directoryList = new JListDriver(JFileChooserDriver.this, JList.class, ComponentDriver.named("GTKFileChooser.directoryList"));
            CurrentDirectoryManipulator directoryManipulator = new CurrentDirectoryManipulator();
            perform("get current directory", directoryManipulator);
            File currentDirectory = directoryManipulator.getCurrentDirectory();
            directoryList.selectItem(new File(currentDirectory, ".."));
        }

        public void home() {
            textBox().selectAll();
            textBox().typeText(System.getProperty("user.home"));
            ok();
        }

        @SuppressWarnings("unchecked")
        public void cancel() {
            new JButtonDriver(JFileChooserDriver.this, JButton.class, ComponentDriver.named("SynthFileChooser.cancelButton")).click();
        }

        @SuppressWarnings("unchecked")
        public void ok() {
            new JButtonDriver(JFileChooserDriver.this, JButton.class, ComponentDriver.named("SynthFileChooser.approveButton")).click();
        }

        @SuppressWarnings("unchecked")
        public JTextComponentDriver<? extends JTextComponent> textBox() {
            return new JTextFieldDriver(JFileChooserDriver.this, JTextField.class, ComponentDriver.named("GTKFileChooser.fileNameTextField"));
        }

        private class CurrentDirectoryManipulator implements ComponentManipulation<JFileChooser> {
            File currentDirectory;

            public void manipulate(JFileChooser component) {
                currentDirectory = component.getCurrentDirectory();
            }

            public File getCurrentDirectory() {
                return currentDirectory;
            }
        }
    }

    private class MetalFileChooserDriverUI implements FileChooserDriverUI {

        @SuppressWarnings("unchecked")
        public void cancel() {
            new AbstractButtonDriver<JButton>(JFileChooserDriver.this, JButton.class, withButtonText("Cancel")).click();
        }

        @SuppressWarnings("unchecked")
        public void ok() {
            new AbstractButtonDriver<JButton>(JFileChooserDriver.this, JButton.class, withButtonText("Open")).click();
        }

        @SuppressWarnings("unchecked")
        public JTextFieldDriver textBox() {
            // only one textfield in this laf...
            return new JTextFieldDriver(JFileChooserDriver.this, JTextField.class, Matchers.anything());
        }

        public void home() {
            movementIconNumber(1).click();
        }

        public void createNewFolder(String folderName) {
            movementIconNumber(2).click();
            JTextFieldDriver folderEntry = new JTextFieldDriver(JFileChooserDriver.this, the(JTextField.class, withFocus()));
            folderEntry.moveMouseToCenter();
            folderEntry.typeText(folderName);
            folderEntry.typeText("\n");
        }

        public void upOneFolder() {
            movementIconNumber(0).click();
        }

        private AbstractButtonDriver<JButton> movementIconNumber(int toChoose) {
            NthComponentFinder<JButton> finder = new NthComponentFinder<JButton>(new RecursiveComponentFinder<JButton>(JButton.class, Matchers.anything(), component()), toChoose);
            return new AbstractButtonDriver<JButton>(JFileChooserDriver.this, finder);
        }
    }

    private interface FileChooserDriverUI {
        void createNewFolder(String folderName);

        void upOneFolder();

        void home();

        void cancel();

        void ok();

        JTextComponentDriver<? extends JTextComponent> textBox();
    }
}
