package com.objogate.wl.driver;

import static com.objogate.wl.driver.JFileChooserDriver.rootFrameFor;
import static com.objogate.wl.probe.ComponentIdentity.selectorFor;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.hamcrest.Matchers;

import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.gesture.Gestures;
import com.objogate.wl.matcher.JLabelTextMatcher;

class GTKFileChooserUIDriver implements FileChooserUIDriver {
    private final JFileChooserDriver parentOrOwner;

    public GTKFileChooserUIDriver(JFileChooserDriver jFileChooserDriver) {
        parentOrOwner = jFileChooserDriver;
    }

    public void selectFile(String fileName) {
        JLabelDriver fileEntry = new JLabelDriver(parentOrOwner, parentOrOwner.the(JLabel.class, JLabelTextMatcher.withLabelText(Matchers.equalTo(fileName))));
        fileEntry.leftClickOnComponent();
    }

    public void intoDir(String directoryName) {
        selectFile(directoryName);
        parentOrOwner.performGesture(Gestures.doubleClickMouse());
    }

    public void createNewFolder(String folderName) {
        AbstractButtonDriver<JButton> newFolderButton = new AbstractButtonDriver<JButton>(parentOrOwner, parentOrOwner.the(JButton.class, ComponentDriver.named("GTKFileChooser.newFolderButton")));
        newFolderButton.click();
        
        //TODO: (nat) this breaks the probe threading model!
        JFrameDriver jframe = new JFrameDriver(parentOrOwner, selectorFor(rootFrameFor(parentOrOwner.component().component())));

        JOptionPaneDriver folderEntry = new JOptionPaneDriver(jframe, JOptionPane.class);
        folderEntry.typeText(folderName);
        folderEntry.clickOK();
    }

    @SuppressWarnings("unchecked")
    public void upOneFolder() {
        JListDriver directoryList = new JListDriver(parentOrOwner, JList.class, ComponentDriver.named("GTKFileChooser.directoryList"));
        CurrentDirectoryManipulator directoryManipulator = new CurrentDirectoryManipulator();
        parentOrOwner.perform("get current directory", directoryManipulator);
        directoryList.selectItem(new JLabelTextMatcher(Matchers.equalTo("..")));
    }


    public void desktop() {
        throw new UnsupportedOperationException("There is no 'Desktop' button in the GTK L&F");
    }

    public void documents() {
        throw new UnsupportedOperationException("There is no 'My Documents' button in the GTK L&F");
    }

    public void home() {
        textBox().selectAll();
        textBox().typeText(System.getProperty("user.home"));
        approve();
    }

    @SuppressWarnings("unchecked")
    public void cancel() {
        new JButtonDriver(parentOrOwner, JButton.class, ComponentDriver.named("SynthFileChooser.cancelButton")).click();
    }

    @SuppressWarnings("unchecked")
    public void approve() {
        new JButtonDriver(parentOrOwner, JButton.class, ComponentDriver.named("SynthFileChooser.approveButton")).click();
    }

    @SuppressWarnings("unchecked")
    public JTextComponentDriver<? extends JTextComponent> textBox() {
        return new JTextFieldDriver(parentOrOwner, JTextField.class, ComponentDriver.named("GTKFileChooser.fileNameTextField"));
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
