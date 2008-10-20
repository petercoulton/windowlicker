package com.objogate.wl;

import org.hamcrest.SelfDescribing;
import com.objogate.wl.Automaton;

public interface Gesture extends SelfDescribing {
    void performGesture(Automaton automaton);
}
