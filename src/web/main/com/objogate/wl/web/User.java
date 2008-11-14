package com.objogate.wl.web;

import com.objogate.wl.Automaton;
import com.objogate.wl.Gesture;
import com.objogate.wl.keyboard.KeyboardLayout;
import com.objogate.wl.robot.RoboticAutomaton;

public class User {
    private final Automaton automaton;
    
    public User() {
        this(new RoboticAutomaton());
    }
    
    public User(KeyboardLayout keyboard) {
        this(new RoboticAutomaton(keyboard));
    }
    
    public User(Automaton automaton) {
        this.automaton = automaton;
    }
    
    public void perform(Gesture... gestures) {
    	for (Gesture gesture : gestures) {
			gesture.performGesture(automaton);
		}
    }
}
