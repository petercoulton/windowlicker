package com.objogate.wl.driver;

import static com.objogate.wl.gesture.Gestures.selectAll;
import static com.objogate.wl.gesture.Gestures.shortcut;
import static java.awt.event.KeyEvent.VK_A;
import static com.objogate.wl.gesture.Gestures.typeKey;
import static java.awt.event.KeyEvent.VK_DELETE;
import static com.objogate.wl.gesture.Gestures.BUTTON1;
import static com.objogate.wl.gesture.Gestures.clickMouseButton;
import static com.objogate.wl.gesture.Gestures.moveMouseTo;
import static com.objogate.wl.gesture.Gestures.translateMouseTo;
import static com.objogate.wl.gesture.Gestures.whileHoldingMouseButton;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.matchers.TypeSafeMatcher;

import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.gesture.Gestures;
import com.objogate.wl.gesture.Tracker;

public class JTextFieldDriver extends JTextComponentDriver<JTextField> {
    public JTextFieldDriver(GesturePerformer gesturePerformer, ComponentSelector<JTextField> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }
    
    public JTextFieldDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JTextField> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JTextFieldDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JTextField> componentType, Matcher<? super JTextField>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }
}
