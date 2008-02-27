package com.objogate.wl.gesture;

import org.hamcrest.Description;
import com.objogate.wl.Gesture;
import com.objogate.wl.robot.Automaton;

public class RepetitionGesture implements Gesture {
    private int repetitions;
    private Gesture gesture;

    public RepetitionGesture(int repetitions, Gesture gesture) {
        this.repetitions = repetitions;
        this.gesture = gesture;
    }

    public void performGesture(Automaton automaton) {
        for ( int i = 0; i < repetitions; i++ ) {
            gesture.performGesture(automaton);
        }
    }


    public void describeTo(Description description) {
        description.appendText("repeat ");
        description.appendValue(repetitions);
        description.appendText(" times [");
        description.appendDescriptionOf(gesture);
        description.appendText("]");

    }
}
