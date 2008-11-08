package com.objogate.wl.driver;

import static com.objogate.wl.gesture.Gestures.BUTTON1;
import static com.objogate.wl.gesture.Gestures.clickMouseButton;
import static com.objogate.wl.gesture.RightAngleMouseMoveGesture.horizontalThenVerticalMouseMove;

import java.awt.Component;

import javax.swing.JMenuItem;

import org.hamcrest.Matcher;

public class JMenuItemDriver extends AbstractButtonDriver<JMenuItem> {
    @SuppressWarnings("unchecked")
    public JMenuItemDriver(ComponentDriver<? extends Component> parentOrOwner, Matcher<? super JMenuItem> matcher) {
        super(parentOrOwner, JMenuItem.class, matcher);
    }

    @Override
    public void leftClickOnComponent() {
        isShowingOnScreen();
        performGesture(horizontalThenVerticalMouseMove(centerOfComponent()), clickMouseButton(BUTTON1));
    }
}
