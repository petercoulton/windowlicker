package com.objogate.wl.swing;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.util.*;
import static java.util.Arrays.asList;
import static com.objogate.wl.swing.gesture.MappedKeyStrokeProbe.invertInputMap;


public class Shortcuts {
    private static final Map<String,Integer> inputMapIds = new HashMap<String,Integer>() {{
        put("focus", JComponent.WHEN_FOCUSED);
        put("ancestor", JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        put("window", JComponent.WHEN_IN_FOCUSED_WINDOW);
    }};


    public static void main(String[] args) throws Exception {
        List<JComponent> components = new ArrayList<JComponent>();

        int inputMapId = inputMapIds.get(args[0]);

        if (args.length == 1) {
            components.add(new JTextField());
        } else {
            for (String arg : asList(args).subList(1, args.length)) {
                components.add((JComponent) Class.forName(arg).newInstance());
            }
        }

        for (JComponent component : components) {
            printShortcutsFor(component, inputMapId);
        }
    }

    private static void printShortcutsFor(JComponent component, int inputMapId) {
        TreeMap<Object, SortedSet<KeyStroke>> inverse = invertInputMap(component.getInputMap(inputMapId));

        System.out.println(component.getClass().getName());
        if (inverse.isEmpty()) {
            System.out.println("  no shortcuts");
        } else {
            for (Object key : inverse.keySet()) {
                System.out.println("  " + key + " -> " + inverse.get(key));
            }
        }
    }
}
