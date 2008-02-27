package com.objogate.wl.driver;

import org.hamcrest.Matcher;

import javax.swing.*;
import java.awt.*;

public class JMenuBarDriver extends ComponentDriver<JMenuBar>{

    public JMenuBarDriver(ComponentDriver<? extends Component> parentOrOwner, Matcher<? super JMenuBar>... matchers) {
        super(parentOrOwner, JMenuBar.class, matchers);
    }

    public JMenuDriver menu(Matcher matching) {
        return new JMenuDriver(this, matching);
    }

}
