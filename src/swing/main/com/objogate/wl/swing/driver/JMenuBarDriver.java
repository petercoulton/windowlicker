package com.objogate.wl.swing.driver;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.awt.Component;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import com.objogate.wl.Query;

public class JMenuBarDriver extends ComponentDriver<JMenuBar> {

    public JMenuBarDriver(ComponentDriver<? extends Component> parentOrOwner, Matcher<? super JMenuBar>... matchers) {
        super(parentOrOwner, JMenuBar.class, matchers);
    }

    @SuppressWarnings("unchecked")
    public JMenuDriver menu(final Matcher<? super JMenu> matching) {
        has(matching);

        return new JMenuDriver(this, matching);
    }

    public void has(final Matcher<? super JMenu> matching) {
        has(new Query<JMenuBar, JMenu>() {
            public JMenu query(JMenuBar component) {
                for (int i = 0; i < component.getMenuCount(); i++) {
                    JMenu jMenu = component.getMenu(i);
                    if (matching.matches(jMenu)) {
                        return jMenu;
                    }
                }
                return new JMenu();
            }

            public void describeTo(Description description) {
                description.appendText("jmenu");
            }
        }, matching);
    }

}
