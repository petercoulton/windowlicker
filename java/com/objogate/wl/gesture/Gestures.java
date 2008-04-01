package com.objogate.wl.gesture;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import com.objogate.wl.Gesture;
import com.objogate.wl.Platform;
import com.objogate.wl.UI;

/**
 * Convenient factory functions for gestures that can be combined to act as a gesture "DSL".
 */
public class Gestures {
    public static int BUTTON1 = InputEvent.BUTTON1_MASK;
    public static int BUTTON2 = InputEvent.BUTTON2_MASK;
    public static int BUTTON3 = InputEvent.BUTTON3_MASK;

    public static int SHIFT = InputEvent.SHIFT_MASK;
    public static int CONTROL = InputEvent.CTRL_MASK;
    public static int ALT = InputEvent.ALT_MASK;
    public static int ALT_GRAPH = InputEvent.ALT_GRAPH_MASK;
    public static int META = InputEvent.META_MASK;
    private static final int MID_KEY_PRESS_DELAY = 50;
    private static final int MID_MOUSE_CLICK_DELAY = 100;
    private static final int MID_DOUBLE_CLICK_DELAY = 5;
    public static final int MIN_TIME_TO_WAIT_TO_AVOID_DOUBLE_CLICK = 1000;//does it really matter that this is very long? probably not.

    public static Gesture sequence(Gesture... gestures) {
        return new GestureSequence(asList(gestures));
    }

    public static Gesture pause(int waitMs) {
        return new WaitGesture(waitMs);
    }

    public static Gesture repeat(int repetitions, Gesture gesture) {
        return new RepetitionGesture(repetitions, gesture);
    }

    /**
     * Creates a gesture that presses one or more mouse buttons.  The mouse buttons should
     * be released using the <code>mouseRelease</code> gesture.
     *
     * @param buttons the button mask; a combination of one or more
     *                of these flags:
     *                <ul>
     *                <li><code>BUTTON1</code>
     *                <li><code>BUTTON2</code>
     *                <li><code>BUTTON3</code>
     *                </ul>
     * @return a gesture that presses the mouse buttons
     * @see #releaseMouse(int)
     */
    public static Gesture pressMouse(int buttons) {
        return new MousePressGesture(buttons);
    }

    /**
     * Creates a gesture that releases one or more mouse buttons.
     *
     * @param buttons the button mask; a combination of one or more
     *                of these flags:
     *                <ul>
     *                <li><code>BUTTON1</code>
     *                <li><code>BUTTON2</code>
     *                <li><code>BUTTON3</code>
     *                </ul>
     * @return a gesture that releases the mouse buttons
     * @see #pressMouse(int)
     */
    public static Gesture releaseMouse(int buttons) {
        return new MouseReleaseGesture(buttons);
    }

    public static Gesture moveMouseWheel(int amount) {
        return new MouseWheelGesture(amount);
    }

    public static Gesture clickMouseButton(int buttons) {
        return sequence(pressMouse(buttons), pause(MID_MOUSE_CLICK_DELAY), releaseMouse(buttons));
    }

    public static Gesture leftClickMouse() {
        return clickMouseButton(BUTTON1);
    }

    public static Gesture rightClickMouse() {
        return clickMouseButton(BUTTON2);
    }

    public static Gesture doubleClickMouse() {
        Gesture leftClick = clickMouseButton(BUTTON1);

        return sequence(leftClick, pause(MID_DOUBLE_CLICK_DELAY), leftClick);
    }

    public static Gesture moveMouseTo(Tracker tracker) {
        return new MouseMoveGesture(tracker);
    }

    public static Gesture translateMouseTo(Tracker tracker) {
        return new MouseTranslateGesture(tracker);
    }

    public static Gesture pressKey(int keyCode) {
        return new KeyPressGesture(keyCode);
    }

    public static Gesture releaseKey(int keyCode) {
        return new KeyReleaseGesture(keyCode);
    }

    public static Gesture typeKey(int keyCode) {
        return sequence(pressKey(keyCode), pause(MID_KEY_PRESS_DELAY), releaseKey(keyCode));
    }

    public static Gesture type(String text) {
        return new TypeTextGesture(text);
    }

    public static Gesture whileHoldingKey(int keyCode, Gesture gesture) {
        return sequence(pressKey(keyCode), gesture, releaseKey(keyCode));
    }

    public static Gesture whileHoldingMouseButton(int buttons, Gesture gesture) {
        return sequence(pressMouse(buttons), gesture, pause(MID_MOUSE_CLICK_DELAY), releaseMouse(buttons));
    }

    public static Gesture withModifierKey(int modifierKeyCode, Gesture modifiedGesture) {
        return new ModifierKeyGesture(modifierKeyCode, modifiedGesture);
    }

    public static Gesture withModifierMask(int modifierMask, Gesture modifiedGesture) {
        Gesture result = modifiedGesture;
        if ((modifierMask & SHIFT) != 0) {
            result = withModifierKey(KeyEvent.VK_SHIFT, result);
        }
        if ((modifierMask & CONTROL) != 0) {
            result = withModifierKey(KeyEvent.VK_CONTROL, result);
        }
        if ((modifierMask & ALT) != 0) {
            result = withModifierKey(KeyEvent.VK_ALT, result);
        }
        if ((modifierMask & ALT_GRAPH) != 0) {
            result = withModifierKey(KeyEvent.VK_ALT_GRAPH, result);
        }
        if ((modifierMask & META) != 0) {
            result = withModifierKey(KeyEvent.VK_META, result);
        }

        return result;
    }

    /**
     * Does not work on OS X with the metal l&f, only allows selection of single cells for some reason
     */
    public static Gesture whileHoldingMultiSelect(Gesture gesture) {
        int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        // there is a bug in the Metal L&F, for multi-select this should be 'commnd'
        if(UI.is(UI.METAL) && Platform.is(Platform.Mac))
            menuShortcutKeyMask = META;

        return withModifierMask(menuShortcutKeyMask, gesture);
    }

    /**
     * Do stuff while holding ctrl (or command on OS X)
     */
    public static Gesture shortcut(Gesture gesture) {
        int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        // there is a bug in the Metal L&F on OS X, for instead of 'command' this needs to use control
        if(UI.is(UI.METAL) && Platform.is(Platform.Mac))
            menuShortcutKeyMask = CONTROL;

        return withModifierMask(menuShortcutKeyMask, gesture);
    }

    public static Gesture shortcut(int keyCode) {
        return shortcut(typeKey(keyCode));
    }

    public static Gesture cut() {
        return shortcut(KeyEvent.VK_X);
    }

    public static Gesture copy() {
        return shortcut(KeyEvent.VK_C);
    }

    public static Gesture paste() {
        return shortcut(KeyEvent.VK_V);
    }

    public static Gesture selectAll() {
        return shortcut(KeyEvent.VK_A);
    }

    public static List<String> buttonListFor(int buttons) {
        List<String> list = new ArrayList<String>();
        if ((buttons & BUTTON1) != 0) {
            list.add("Left");
        }
        if ((buttons & BUTTON2) != 0) {
            list.add("Middle");
        }
        if ((buttons & BUTTON3) != 0) {
            list.add("Right");
        }

        return list;
    }
}
