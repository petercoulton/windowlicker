package com.objogate.wl.driver;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Component;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.equalTo;
import com.objogate.wl.ComponentQuery;

public class JMenuDriver extends AbstractButtonDriver<JMenu> {

    public JMenuDriver(ComponentDriver<? extends Component> parentOrOwner, Matcher<? super JMenu>... matchers) {
        super(parentOrOwner, JMenu.class, matchers);
    }

    public void isShowing() {
        has(popupVisible(), equalTo(true));
    }

    public JMenuDriver select() {
        leftClickOnComponent();
        return this;
    }

    @SuppressWarnings("unchecked")
    public JMenuDriver subMenu(Matcher<? super JMenu> matcher) {
        select();
        return new JMenuDriver(this, matcher);    
    }

    public JMenuItemDriver subItem(Matcher<? super JMenuItem> matcher) {
        select();
        return new JMenuItemDriver(this, matcher);
    }

    private static ComponentQuery<JMenu, Boolean> popupVisible() {
        return new ComponentQuery<JMenu, Boolean>() {
            public Boolean query(JMenu label) {
                return label.isPopupMenuVisible();
            }
            public void describeTo(Description description) {
              description.appendText("popup menu visibility");
            }
        };
    }

    @SuppressWarnings("unchecked")
    public AbstractButtonDriver<JMenuItem> menuItem(Matcher<AbstractButton> matcher) {
        return new AbstractButtonDriver<JMenuItem>(this, JMenuItem.class, matcher);
    }
}
