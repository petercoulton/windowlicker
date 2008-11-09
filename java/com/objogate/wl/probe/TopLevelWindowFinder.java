package com.objogate.wl.probe;

import java.awt.Frame;
import java.awt.Window;
import java.util.*;
import org.hamcrest.Description;

import com.objogate.wl.swing.ComponentFinder;

public class TopLevelWindowFinder implements ComponentFinder<Window> {
    private List<Window> found = Collections.emptyList();

    public boolean isSatisfied() {
        return true;
    }

    public List<Window> components() {
        return found;
    }

    public void probe() {
        Set<Window> topLevelWindows = new HashSet<Window>();

        for (Frame frame : Frame.getFrames()) {
            topLevelWindows.add(ownershipRoot(frame));
        }

        found = new ArrayList<Window>(topLevelWindows);
    }

    private static Window ownershipRoot(Window w) {
        return w.getOwner() == null
                ? w
                : ownershipRoot(w.getOwner());
    }

    public void describeTo(Description description) {
        description.appendText("all top level windows");
    }

    public boolean describeFailureTo(Description description) {
        describeTo(description);
        return false;
    }
}
