package com.objogate.wl.driver;

import javax.swing.JMenuItem;
import java.awt.Component;
import org.hamcrest.Matcher;
import static com.objogate.wl.gesture.Gestures.BUTTON1;
import static com.objogate.wl.gesture.Gestures.clickMouseButton;
import static com.objogate.wl.gesture.RightAngleMouseMoveGesture.*;

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
