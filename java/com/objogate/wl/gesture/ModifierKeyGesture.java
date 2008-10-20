package com.objogate.wl.gesture;

import org.hamcrest.Description;
import com.objogate.wl.Gesture;
import com.objogate.wl.Automaton;

public class ModifierKeyGesture implements Gesture {
    private final int modifierKeyCode;
    private final Gesture modifiedGesture;

    public ModifierKeyGesture(int modifierKeyCode, Gesture modifiedGesture) {
        this.modifierKeyCode = modifierKeyCode;
        this.modifiedGesture = modifiedGesture;
    }

    public void performGesture(Automaton automaton) {
        automaton.keyPress(modifierKeyCode);
        modifiedGesture.performGesture(automaton);
        automaton.keyRelease(modifierKeyCode);
    }


    public void describeTo(Description description) {
        description.appendText("Press modifier ");
        description.appendValue(modifierKeyCode);
        description.appendText(" then ");
        description.appendDescriptionOf(modifiedGesture);

    }
}
