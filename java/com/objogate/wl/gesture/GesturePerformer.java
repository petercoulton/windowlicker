package com.objogate.wl.gesture;

import javax.swing.SwingUtilities;
import com.objogate.exception.Defect;
import com.objogate.wl.Gesture;
import com.objogate.wl.robot.Automaton;
import com.objogate.wl.robot.RoboticAutomaton;

public class GesturePerformer {
    private final Automaton automaton;

    public GesturePerformer() {
        this(new RoboticAutomaton());
    }

    public GesturePerformer(Automaton automaton) {
        this.automaton = automaton;
    }

    public void perform(Gesture... gestures) {
        if (SwingUtilities.isEventDispatchThread()) {
            throw new Defect("attempted to perform gestures on the Swing event dispatch thread");
        }
        
        for (Gesture gesture : gestures) {
            gesture.performGesture(automaton);
        }
    }    
}
