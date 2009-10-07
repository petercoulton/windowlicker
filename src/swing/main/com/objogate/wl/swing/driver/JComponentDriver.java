package com.objogate.wl.swing.driver;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.awt.Component;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import com.objogate.wl.Automaton;
import com.objogate.wl.Gesture;
import com.objogate.wl.Prober;
import static com.objogate.wl.gesture.Gestures.*;
import com.objogate.wl.swing.ComponentSelector;
import com.objogate.wl.swing.gesture.GesturePerformer;
import com.objogate.wl.swing.gesture.MappedKeyStrokeProbe;


public class JComponentDriver<T extends JComponent> extends ComponentDriver<T> {
    public JComponentDriver(GesturePerformer gesturePerformer, ComponentSelector<T> tComponentSelector, Prober prober) {
        super(gesturePerformer, tComponentSelector, prober);
    }

    public JComponentDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<T> tComponentSelector) {
        super(parentOrOwner, tComponentSelector);
    }

    public JComponentDriver(ComponentDriver<? extends Component> parentOrOwner, Class<T> componentType, Matcher<? super T>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }


    public void performShortcut(final String shortcutName) {
        performGesture(new Gesture() {
            public void describeTo(Description description) {
                description.appendText(shortcutName);
            }

            public void performGesture(Automaton automaton) {
                MappedKeyStrokeProbe keyStrokeProbe = new MappedKeyStrokeProbe(component(), shortcutName);
                check(keyStrokeProbe);
                automaton.perform(interpretKeyStroke(keyStrokeProbe.mappedKeyStroke));
            }
        });
    }

    private Gesture interpretKeyStroke(KeyStroke keyStroke) {
        return withModifierMask(keyStroke.getModifiers(),
                keyStroke.isOnKeyRelease()
                        ? releaseKey(keyStroke.getKeyCode())
                        : pressKey(keyStroke.getKeyCode()));
    }

    public void cut() {
        performShortcut("cut-to-clipboard");
    }

    public void copy() {
        performShortcut("copy-to-clipboard");
    }

    public void paste() {
        performShortcut("paste-from-clipboard");
    }

    public void selectAll() {
        performShortcut("select-all");
    }

    public void selectNothing() {
        performShortcut("unselect");
    }

}
