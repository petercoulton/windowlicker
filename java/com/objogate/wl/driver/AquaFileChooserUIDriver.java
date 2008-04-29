package com.objogate.wl.driver;

import javax.swing.*;
import java.awt.Component;
import java.awt.Container;
import java.util.concurrent.TimeUnit;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import com.objogate.exception.Defect;
import com.objogate.wl.gesture.Gestures;
import com.objogate.wl.matcher.ComponentMatchers;
import com.objogate.wl.matcher.JLabelTextMatcher;

class AquaFileChooserUIDriver extends MetalFileChooserUIDriver {
    public AquaFileChooserUIDriver(JFileChooserDriver jFileChooserDriver) {
        super(jFileChooserDriver);
    }

    @Override 
    public void selectFile(String fileName) {
        JTableDriver fileEntry = new JTableDriver(parentOrOwner, JTable.class);
        fileEntry.selectCell(new JLabelTextMatcher(Matchers.equalTo(fileName)));

        //there seems to be a bug in the chooser, the first selection is always lost so we reselect it
        try {
            TimeUnit.MILLISECONDS.sleep(Gestures.MIN_TIME_TO_WAIT_TO_AVOID_DOUBLE_CLICK);
            fileEntry.selectCell(new JLabelTextMatcher(Matchers.equalTo(fileName)));
        } catch (InterruptedException e) {
            throw new Defect("Unable to select file", e);
        }
    }

    @Override
    public void intoDir(String directoryName) {
        selectFile(directoryName);
        parentOrOwner.performGesture(Gestures.doubleClickMouse());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void createNewFolder(String folderName) {
        new AbstractButtonDriver<JButton>(parentOrOwner, JButton.class, ComponentMatchers.withButtonText("New Folder")).click();
        JTextFieldDriver textDriver = new JTextFieldDriver(parentOrOwner, JTextField.class, new TypeSafeMatcher<JTextField>() {
            @Override
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

    @SuppressWarnings("unchecked")
    @Override
    public void upOneFolder() {
        JComboBoxDriver boxDriver = new JComboBoxDriver(parentOrOwner, JComboBox.class,
                new MacComboBoxModelTypeMatcher(), new ComboBoxModelMinSizeMatcher(2));
        boxDriver.selectItem(1);
    }

    @Override
    public void documents() {
        throw new UnsupportedOperationException("There is no 'Documents' button in the Aqua L&F");
    }

    @Override
    public void home() {
        throw new UnsupportedOperationException("There is no 'Home' button in the Aqua L&F");
    }

    @Override
    public void desktop() {
        throw new UnsupportedOperationException("There is no 'Desktop' button in the Aqua L&F");
    }

    private class MacComboBoxModelTypeMatcher extends TypeSafeMatcher<JComboBox> {
        private static final String TYPE = "apple.laf.AquaFileChooserUI$DirectoryComboBoxModel";

        @Override
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

        @Override
        public boolean matchesSafely(JComboBox jComboBox) {
            ComboBoxModel comboBoxModel = jComboBox.getModel();
            return comboBoxModel.getSize() >= minSize;
        }

        public void describeTo(Description description) {
            description.appendText("JComboBox with model with at least on entry");
        }
    }
}
