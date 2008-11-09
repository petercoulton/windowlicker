package com.objogate.wl.gesture;

import org.hamcrest.Description;
import com.objogate.wl.Gesture;
import com.objogate.wl.Automaton;

public class GestureSequence implements Gesture {
    private final Iterable<Gesture> gestures;

    public GestureSequence(Iterable<Gesture> gestures) {
        this.gestures = gestures;
    }

    public void performGesture(Automaton automaton) {
        for (Gesture gesture : gestures) {
            gesture.performGesture(automaton);
        }
    }

    public void describeTo(Description description) {
        description.appendList("[", ", ", "]", gestures);
    }
}
