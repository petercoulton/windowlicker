package com.objogate.wl.driver;

import java.awt.Component;
import org.hamcrest.Matcher;

public interface ListDriver {
    void selectItem(int index);

    void selectItem(Object item);

    void selectItem(Matcher<Component> item);

    void hasSelectedIndex(int index);
}
