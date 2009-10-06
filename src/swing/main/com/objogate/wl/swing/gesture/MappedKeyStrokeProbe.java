package com.objogate.wl.swing.gesture;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.hamcrest.Description;

import com.objogate.wl.Probe;
import com.objogate.wl.swing.ComponentSelector;

public class MappedKeyStrokeProbe implements Probe {
    private final ComponentSelector<? extends JComponent> componentSelector;
    private final String actionName;
    
    public KeyStroke mappedKeyStroke = null;

    public MappedKeyStrokeProbe(ComponentSelector<? extends JComponent> componentSelector, String actionName) {
		this.actionName = actionName;
        this.componentSelector = componentSelector;
    }

	public void probe() {
        componentSelector.probe();
        
        if (componentSelector.isSatisfied()) {
        	JComponent component = componentSelector.component();
        	mappedKeyStroke = keyStrokeBoundToAction(component); 
        }
	}

	private KeyStroke keyStrokeBoundToAction(JComponent component) {
		InputMap inputMap = component.getInputMap();
		KeyStroke[] allKeyStrokes = inputMap.allKeys();
		for (KeyStroke keyStroke : allKeyStrokes) {
			if (inputMap.get(keyStroke).equals(actionName)) {
				return keyStroke;
			}
		}
		return null;
	}

	public boolean isSatisfied() {
		return mappedKeyStroke != null;
	}

	public void describeTo(Description description) {
		description.appendText(" key stroke for action ")
				   .appendText(actionName);
	}

	public boolean describeFailureTo(Description description) {
        if (componentSelector.describeFailureTo(description)) {
            return true;
        }

		description.appendText(" action not bound to any keystroke");
		return true;
	}
}
