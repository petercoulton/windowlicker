package com.objogate.wl.probe;

import com.objogate.wl.Probe;
import org.hamcrest.Description;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerProbe implements ActionListener, Probe {
    private boolean hasBeenPerformed = false;

    public synchronized void actionPerformed(ActionEvent e) {
        hasBeenPerformed = true;
    }

    public void probe() {
        // nothing to do but wait for the action
    }

    public synchronized boolean isSatisfied() {
        return hasBeenPerformed;
    }

    public void describeTo(Description description) {
        description.appendText("click action should have been performed");
    }

    public boolean describeFailureTo(Description description) {
        description.appendText("the action was ")
                   .appendText(hasBeenPerformed ? "" : "not ")
                   .appendText("performed");
        
        return !hasBeenPerformed;
    }
}
