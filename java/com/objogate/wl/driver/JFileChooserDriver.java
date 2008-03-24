package com.objogate.wl.driver;

import javax.swing.*;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.Container;
import java.awt.HeadlessException;
import java.io.File;
import java.util.concurrent.TimeUnit;
import com.sun.java.swing.plaf.motif.MotifFileChooserUI;
import com.sun.java.swing.plaf.windows.WindowsFileChooserUI;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;
import org.hamcrest.TypeSafeMatcher;
import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.Probe;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.gesture.Gestures;
import com.objogate.wl.matcher.ComponentMatchers;
import static com.objogate.wl.matcher.ComponentMatchers.withButtonText;
import static com.objogate.wl.matcher.ComponentMatchers.withFocus;
import com.objogate.wl.matcher.JLabelTextMatcher;
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
            throw new UnsupportedOperationException("not supported yet " + name);
        } else if (ui instanceof WindowsFileChooserUI) {
            throw new UnsupportedOperationException("not supported yet " + name);
        } else if (ui.getClass().getName().equals("com.sun.java.swing.plaf.gtk.GTKFileChooserUI")) {
            return new GTKFileChooserDriverUI();
        } else if (ui instanceof BasicFileChooserUI) {
            throw new UnsupportedOperationException("not supported yet " + name);
        } else if (name.equals("apple.laf.AquaFileChooserUI")) {
            return new AquaFileChooserUIDriver();
        }
        throw new UnsupportedOperationException("not known about or supported yet " + name);
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

    private class GTKFileChooserDriverUI implements FileChooserDriverUI {

        public void selectFile(String fileName) {
            JLabelDriver fileEntry = new JLabelDriver(JFileChooserDriver.this, the(JLabel.class, ComponentMatchers.withLabelText(equalTo(fileName))));
            fileEntry.leftClickOnComponent();
        }

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

//            File currentDirectory = directoryManipulator.getCurrentDirectory();
//            directoryList.selectItem(new File(currentDirectory, ".."));

            //todo (nick): i changed this but was unable to test it - sorry if i broke it!
            directoryList.selectItem(new JLabelTextMatcher(equalTo("..")));
        }

        public void home() {
            textBox().selectAll();
            textBox().typeText(System.getProperty("user.home"));
            approve();
        }

        @SuppressWarnings("unchecked")
        public void cancel() {
            new JButtonDriver(JFileChooserDriver.this, JButton.class, ComponentDriver.named("SynthFileChooser.cancelButton")).click();
        }

        @SuppressWarnings("unchecked")
        public void approve() {
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

        public void selectFile(String fileName) {
            JLabelDriver fileEntry = new JLabelDriver(JFileChooserDriver.this, the(JLabel.class, ComponentMatchers.withLabelText(equalTo(fileName))));
            fileEntry.leftClickOnComponent();
        }

        public void cancel() {
            new AbstractButtonDriver<JButton>(JFileChooserDriver.this, JButton.class, withButtonText("Cancel")).click();
        }

        public void approve() {
            final String[] approveButtonText = new String[1];
            perform("finding the approve button text", new ComponentManipulation<JFileChooser>() {
                public void manipulate(JFileChooser component) {
                    approveButtonText[0] = component.getApproveButtonText();
                }
            });
            String text = approveButtonText[0] == null ? "Open" : approveButtonText[0];
            new AbstractButtonDriver<JButton>(JFileChooserDriver.this, JButton.class, withButtonText(text)).click();
        }

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

        void approve();

        JTextComponentDriver<? extends JTextComponent> textBox();

        void selectFile(String fileName);
    }

    private class AquaFileChooserUIDriver extends MetalFileChooserDriverUI {

        public void selectFile(String fileName) {
            JTableDriver fileEntry = new JTableDriver(JFileChooserDriver.this, JTable.class);
            fileEntry.selectCell(new JLabelTextMatcher(Matchers.equalTo(fileName)));

            //there seems to be a bug in the chooser, the first selection is always lost so we reselect it
            try {
                TimeUnit.MILLISECONDS.sleep(Gestures.MIN_TIME_TO_WAIT_TO_AVOID_DOUBLE_CLICK);
            } catch (InterruptedException e) {
                return;
            }
            fileEntry.selectCell(new JLabelTextMatcher(Matchers.equalTo(fileName)));
        }

        public void createNewFolder(String folderName) {
            new AbstractButtonDriver<JButton>(JFileChooserDriver.this, JButton.class, withButtonText("New Folder")).click();
            JTextFieldDriver textDriver = new JTextFieldDriver(JFileChooserDriver.this, JTextField.class, new TypeSafeMatcher<JTextField>() {
                public boolean matchesSafely(JTextField jTextField) {
                    Container container = jTextField.getParent();
                    Component component = container.getComponent(0);
                    if (component instanceof JLabel) {
                        JLabel jLabel = (JLabel) component;
                        return jLabel.getText().equals("File:");
                    }

                    return false;
                }

                public void describeTo(Description description) {
                    description.appendText("JTextField with JLabel sibling containing text 'File'");
                }
            });
            textDriver.typeText(folderName);

            //hack (nick): can't get hold of the 'Create' button!  so use the keyboard to navigate to it 
            textDriver.typeText("\t");//move focus to 'Cancel' button
            textDriver.typeText("\t");//move focus to 'Create' button
            textDriver.typeText(" ");//select 'Create' button
        }

        public void upOneFolder() {
            JComboBoxDriver boxDriver = new JComboBoxDriver(JFileChooserDriver.this, JComboBox.class,
                    new MacComboBoxModelTypeMatcher(), new ComboBoxModelMinSizeMatcher(2));
            boxDriver.selectItem(1);
        }

        public void home() {
            throw new UnsupportedOperationException("Aqua file chooser has no 'home' button");
        }

        private class MacComboBoxModelTypeMatcher extends TypeSafeMatcher<JComboBox> {
            private static final String TYPE = "apple.laf.AquaFileChooserUI$DirectoryComboBoxModel";

            public boolean matchesSafely(JComboBox jComboBox) {
                ComboBoxModel comboBoxModel = jComboBox.getModel();
                return comboBoxModel.getClass().getName().equals(TYPE);
            }

            public void describeTo(Description description) {
                description.appendText("JComboBox with model of type " + TYPE);
            }
        }

        private class ComboBoxModelMinSizeMatcher extends TypeSafeMatcher<JComboBox> {
            private final int minSize;

            public ComboBoxModelMinSizeMatcher(int minSize) {
                this.minSize = minSize;
            }

            public boolean matchesSafely(JComboBox jComboBox) {
                ComboBoxModel comboBoxModel = jComboBox.getModel();
                return comboBoxModel.getSize() >= minSize;
            }

            public void describeTo(Description description) {
                description.appendText("JComboBox with model with at least on entry");
            }
        }
    }
}
