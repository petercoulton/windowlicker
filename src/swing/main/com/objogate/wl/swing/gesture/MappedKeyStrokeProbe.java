package com.objogate.wl.swing.gesture;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.hamcrest.Description;
import com.objogate.wl.Probe;
import com.objogate.wl.swing.ComponentSelector;

public class MappedKeyStrokeProbe implements Probe {
    private final ComponentSelector<? extends JComponent> componentSelector;
    private final Object actionName;
    
    public KeyStroke mappedKeyStroke = null;

    public MappedKeyStrokeProbe(ComponentSelector<? extends JComponent> componentSelector, Object actionName) {
		this.actionName = actionName;
        this.componentSelector = componentSelector;
    }

	public void probe() {
        componentSelector.probe();
        
        if (componentSelector.isSatisfied()) {
        	JComponent component = componentSelector.component();
        	mappedKeyStroke = preferredKeyStrokeBoundToAction(component);
        }
	}

    // Pick key strokes with lower valued key codes because higher key codes seem to be used for obscure keyboards
    // like the cut/copy/paste keys that only exist on old Sun keyboards.
	private KeyStroke preferredKeyStrokeBoundToAction(JComponent component) {
        TreeMap<Object, SortedSet<KeyStroke>> invertedInputMap = invertInputMap(component.getInputMap());
        
        if (invertedInputMap.containsKey(actionName)) {
            return invertedInputMap.get(actionName).first();
        }
        else {
            return null;
        }
	}

    public boolean isSatisfied() {
		return mappedKeyStroke != null;
	}

	public void describeTo(Description description) {
		description.appendText(" key stroke for action ")
				   .appendValue(actionName);
	}

	public boolean describeFailureTo(Description description) {
        if (componentSelector.describeFailureTo(description)) {
            return true;
        }

		description.appendText(" action not bound to any keystroke");
		return true;
	}
    
    public static TreeMap<Object, SortedSet<KeyStroke>> invertInputMap(InputMap inputMap) {
        TreeMap<Object, SortedSet<KeyStroke>> inverse = new TreeMap<Object, SortedSet<KeyStroke>>();
        for (KeyStroke keyStroke : inputMap.allKeys()) {
            Object name = inputMap.get(keyStroke);

            SortedSet<KeyStroke> keyStrokesForName = inverse.get(name);
            if (keyStrokesForName == null) {
                keyStrokesForName = new TreeSet<KeyStroke>(new SortedByKeyCodeValue());
                inverse.put(name, keyStrokesForName);
            }

            keyStrokesForName.add(keyStroke);
        }
        
        return inverse;
    }

    private static class SortedByKeyCodeValue implements Comparator<KeyStroke> {
        public int compare(KeyStroke o1, KeyStroke o2) {
            return new Integer(o1.getKeyCode()).compareTo(new Integer(o2.getKeyCode()));
        }
    }
}
