package com.objogate.wl.matcher;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.Container;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ScrollBarMatcher extends TypeSafeMatcher<JScrollBar> {
    private final Container parent;
    private final int orientation;

    public ScrollBarMatcher(JScrollPane pane, int orientation) {
        parent = pane;
        this.orientation = orientation;
    }

    @Override
    public boolean matchesSafely(JScrollBar component) {
        return parent == component.getParent() && component.getOrientation() == orientation;
    }

    public void describeTo(Description description) {
        description.appendText("scrollbar");
    }
}
