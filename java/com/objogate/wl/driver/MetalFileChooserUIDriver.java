package com.objogate.wl.driver;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JTextField;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.gesture.Gestures;
import com.objogate.wl.matcher.ComponentMatchers;
import com.objogate.wl.probe.NthComponentFinder;
import com.objogate.wl.probe.RecursiveComponentFinder;

class MetalFileChooserUIDriver implements FileChooserUIDriver {
    protected JFileChooserDriver parentOrOwner;

    public MetalFileChooserUIDriver(JFileChooserDriver jFileChooserDriver) {
        parentOrOwner = jFileChooserDriver;
    }

    public void selectFile(String fileName) {
        JListDriver jListDriver = new JListDriver(parentOrOwner, JList.class);
        jListDriver.selectItem(ComponentMatchers.withLabelText(Matchers.equalTo(fileName)));
    }

    public void intoDir(String directoryName) {
        selectFile(directoryName);
        parentOrOwner.performGesture(Gestures.doubleClickMouse());        
    }

    @SuppressWarnings("unchecked")
    public void cancel() {
        new AbstractButtonDriver<JButton>(parentOrOwner, JButton.class, ComponentMatchers.withButtonText("Cancel")).click();
    }

    @SuppressWarnings("unchecked")
    public void approve() {
        final String[] approveButtonText = new String[1];
        parentOrOwner.perform("finding the approve button text", new ComponentManipulation<JFileChooser>() {
            public void manipulate(JFileChooser component) {
                approveButtonText[0] = component.getApproveButtonText();
            }
        });
        String text = approveButtonText[0] == null ? "Open" : approveButtonText[0];
        new AbstractButtonDriver<JButton>(parentOrOwner, JButton.class, ComponentMatchers.withButtonText(text), new TypeSafeMatcher<JButton>() {
            @Override
            public boolean matchesSafely(JButton jButton) {
                return jButton.isVisible();
            }

            public void describeTo(Description description) {
                description.appendText("visible button");
            }
        }).click();
    }

    @SuppressWarnings("unchecked")
    public JTextFieldDriver textBox() {
        // only one textfield in this laf...
        return new JTextFieldDriver(parentOrOwner, JTextField.class, Matchers.anything());
    }

    public void home() {
        movementIconNumber(1).click();
    }

    public void documents() {
        throw new UnsupportedOperationException("There is no 'My Documents' button in the Metal L&F");
    }

    public void desktop() {
        throw new UnsupportedOperationException("There is no 'Desktop' button in the Metal L&F");
    }

    public void createNewFolder(String folderName) {
        movementIconNumber(2).click();
        JTextFieldDriver folderEntry = new JTextFieldDriver(parentOrOwner, parentOrOwner.the(JTextField.class, ComponentMatchers.withFocus()));
        folderEntry.moveMouseToCenter();
        folderEntry.typeText(folderName);
        folderEntry.typeText("\n");
    }

    public void upOneFolder() {
        movementIconNumber(0).click();
    }

    private AbstractButtonDriver<JButton> movementIconNumber(int toChoose) {
        NthComponentFinder<JButton> finder = new NthComponentFinder<JButton>(new RecursiveComponentFinder<JButton>(JButton.class, Matchers.anything(), parentOrOwner.component()), toChoose);
        return new AbstractButtonDriver<JButton>(parentOrOwner, finder);
    }

}
