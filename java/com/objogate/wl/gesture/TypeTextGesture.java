package com.objogate.wl.gesture;

import org.hamcrest.Description;
import com.objogate.wl.Gesture;
import com.objogate.wl.robot.Automaton;

public class TypeTextGesture implements Gesture {
    private final String text;

    public TypeTextGesture(String text) {
        this.text = text;
    }

    public void performGesture(Automaton automaton) {
        for (int i = 0; i < text.length(); i++) {
            automaton.typeCharacter(text.charAt(i));
        }
    }

    public void describeTo(Description description) {
        description.appendText("type text '");
        description.appendValue(text);
        description.appendText("'");
    }
}
