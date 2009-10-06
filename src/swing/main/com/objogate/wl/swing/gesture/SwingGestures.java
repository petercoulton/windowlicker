package com.objogate.wl.swing.gesture;

import java.awt.Toolkit;

import com.objogate.wl.Gesture;
import com.objogate.wl.gesture.Gestures;
import com.objogate.wl.internal.Platform;
import com.objogate.wl.swing.UI;

/**
 * Gestures for interacting with Swing components
 * 
 * TODO (nat, stevef) - use InputMaps instead.
 */
public class SwingGestures extends Gestures {
    /**
     * Does not work on OS X with the metal l&f, only allows selection of single cells for some reason
     */
    public static Gesture whileHoldingMultiSelect(Gesture gesture) {
        int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        if(UI.is(UI.METAL) && Platform.is(Platform.Mac))
            menuShortcutKeyMask = META;

        return withModifierMask(menuShortcutKeyMask, gesture);
    }
    
    /**
     * Do stuff while holding ctrl (or command on OS X)
     */
    public static Gesture shortcut(Gesture gesture) {
        int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        // there is a bug in the Metal L&F on OS X, instead of 'command' this needs to use control
        if(UI.is(UI.METAL) && Platform.is(Platform.Mac))
            menuShortcutKeyMask = CONTROL;

        return withModifierMask(menuShortcutKeyMask, gesture);
    }
}
