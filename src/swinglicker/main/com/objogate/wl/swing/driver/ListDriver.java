package com.objogate.wl.swing.driver;

import java.awt.Component;
import org.hamcrest.Matcher;

public interface ListDriver {
    void selectItem(int index);

    void selectItem(Matcher<? extends Component> item);

    void hasSelectedIndex(int index);
}
