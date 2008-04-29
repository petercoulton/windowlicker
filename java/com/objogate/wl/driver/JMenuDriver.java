package com.objogate.wl.driver;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Component;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.equalTo;
import com.objogate.wl.Query;
import static com.objogate.wl.gesture.Gestures.BUTTON1;
import static com.objogate.wl.gesture.Gestures.clickMouseButton;
import static com.objogate.wl.gesture.RightAngleMouseMoveGesture.createVerticalThenHorizontalMouseMoveGesture;

public class JMenuDriver extends AbstractButtonDriver<JMenu> {

    public JMenuDriver(ComponentDriver<? extends Component> parentOrOwner, Matcher<? super JMenu>... matchers) {
        super(parentOrOwner, JMenu.class, matchers);
    }

    public void isShowing() {
        has(popupVisible(), equalTo(true));
    }

    public JMenuDriver selectSubMenu(Matcher<? super JMenu> matcher) {
        JMenuDriver menuDriver = subMenu(matcher);
        menuDriver.click();
        return menuDriver;
    }

    @SuppressWarnings("unchecked")
    public JMenuDriver subMenu(Matcher<? super JMenu> matcher) {
        return new JMenuDriver(this, matcher);
    }

    public JMenuItemDriver selectMenuItem(Matcher<? super JMenuItem> matcher) {
        JMenuItemDriver itemDriver = menuItem(matcher);
        itemDriver.click();
        return itemDriver;
    }

    public JMenuItemDriver menuItem(Matcher<? super JMenuItem> matcher) {
        return new JMenuItemDriver(this, matcher);
    }

    private static Query<JMenu, Boolean> popupVisible() {
        return new Query<JMenu, Boolean>() {
            public Boolean query(JMenu label) {
                return label.isPopupMenuVisible();
            }

            public void describeTo(Description description) {
                description.appendText("popup menu visibility");
            }
        };
    }

    public void leftClickOn(Matcher<? super JMenuItem> matcher) {
        JMenuItemDriver buttonDriver = menuItem(matcher);
        prober().check(buttonDriver.component());
        buttonDriver.isShowingOnScreen();
        buttonDriver.performGesture(
                createVerticalThenHorizontalMouseMoveGesture(buttonDriver.centerOfComponent()), clickMouseButton(BUTTON1));
    }
}
