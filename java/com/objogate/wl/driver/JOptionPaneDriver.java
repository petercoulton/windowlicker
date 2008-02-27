package com.objogate.wl.driver;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.Component;
import org.hamcrest.Matcher;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.gesture.TypeTextGesture;
import com.objogate.wl.matcher.ButtonTextMatcher;

public class JOptionPaneDriver extends ComponentDriver<JOptionPane> {

    public JOptionPaneDriver(GesturePerformer gesturePerformer, JOptionPane component) {
        super(gesturePerformer, component);
    }

    public JOptionPaneDriver(GesturePerformer gesturePerformer, JOptionPane component, Prober prober) {
        super(gesturePerformer, component, prober);
    }

    public JOptionPaneDriver(GesturePerformer gesturePerformer, ComponentSelector<JOptionPane> componentSelector) {
        super(gesturePerformer, componentSelector);
    }

    public JOptionPaneDriver(GesturePerformer gesturePerformer, ComponentSelector<JOptionPane> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public JOptionPaneDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JOptionPane> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JOptionPaneDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JOptionPane> componentType, Matcher<? super JOptionPane>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public void clickOK() {
        clickButtonWithText("OK");
    }

    public void clickYes() {
        clickButtonWithText("Yes");
    }

    public void clickButtonWithText(String buttonText) {
        new AbstractButtonDriver<JButton>(this, JButton.class, new ButtonTextMatcher(buttonText)).click();
    }

    public void enterText(String text) {
        performGesture(new TypeTextGesture(text));
        performGesture(new TypeTextGesture("\n"));
    }
}
