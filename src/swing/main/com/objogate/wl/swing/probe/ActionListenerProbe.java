package com.objogate.wl.swing.probe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.hamcrest.Description;
import com.objogate.wl.Probe;

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

    public void describeFailureTo(Description description) {
        description.appendText("the action was ")
                .appendText(hasBeenPerformed ? "" : "not ")
                .appendText("performed");
    }
}
