package com.objogate.wl.driver;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.awt.Component;
import org.hamcrest.Matcher;

public class JMenuBarDriver extends ComponentDriver<JMenuBar>{

    public JMenuBarDriver(ComponentDriver<? extends Component> parentOrOwner, Matcher<? super JMenuBar>... matchers) {
        super(parentOrOwner, JMenuBar.class, matchers);
    }

    public JMenuDriver menu(Matcher<? super JMenu> matching) {
        return new JMenuDriver(this, matching);
    }

}
