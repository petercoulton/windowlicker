package com.objogate.wl.swing;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import static com.objogate.wl.swing.gesture.MappedKeyStrokeProbe.invertInputMap;


public class Shortcuts {
    public static void main(String[] args) throws Exception {
        List<JComponent> components = new ArrayList<JComponent>();

        if (args.length == 0) {
            components.add(new JTextField());
        }
        else {
            for (String arg : args) {
                components.add((JComponent) Class.forName(arg).newInstance());
            }
        }

        for (JComponent component : components) {
            printShortcutsFor(component);
        }
    }
    
    private static void printShortcutsFor(JComponent component) {
        InputMap inputMap = component.getInputMap();

        TreeMap<Object, SortedSet<KeyStroke>> inverse = invertInputMap(inputMap);
        
        System.out.println(component.getClass().getName());
        for (Object key : inverse.keySet()) {
            System.out.println("  " + key + " -> " + inverse.get(key));
        }
    }
}
