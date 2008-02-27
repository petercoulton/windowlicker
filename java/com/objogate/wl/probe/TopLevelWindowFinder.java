package com.objogate.wl.probe;

import java.awt.Frame;
import java.awt.Window;
import java.util.*;
import org.hamcrest.Description;
import com.objogate.wl.ComponentFinder;

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
    
    private Window ownershipRoot(Window w) {
        if (w.getOwner() == null) {
            return w;
        }
        else {
            return ownershipRoot(w.getOwner());
        }
    }
    
    public void describeTo(Description description) {
        description.appendText("top level window");
    }
}
