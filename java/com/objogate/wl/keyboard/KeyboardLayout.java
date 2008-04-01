package com.objogate.wl.keyboard;

import javax.swing.KeyStroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.im.InputContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import com.objogate.wl.Gesture;
import com.objogate.wl.Platform;
import com.objogate.wl.SystemProperties;
import static com.objogate.wl.gesture.Gestures.typeKey;
import static com.objogate.wl.gesture.Gestures.withModifierMask;

/**
 * Represents how characters are input by key strokes.
 * <p/>
 * More information about keyboard layouts is available at
 * <a href="http://en.wikipedia.org/wiki/Keyboard_layout">http://en.wikipedia.org/wiki/Keyboard_layout</a>.
 *
 * @see com.objogate.wl.SystemProperties
 */
public class KeyboardLayout implements SystemProperties {
    private final Map<Character, KeyStroke> keyStrokes = new HashMap<Character, KeyStroke>();
    private final String name;

    static {
        if(System.getProperty(KEYBOARD_LAYOUT) == null && Platform.is(Platform.Mac)) {
            System.setProperty(KEYBOARD_LAYOUT, "MAC.GB");
        }
    }

    private KeyboardLayout(String name, URL resource) throws IOException {
        this.name = name;
        initialiseDefaults();
        parseKeyStrokes(resource);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " keyboard layout";
    }

    public Gesture gestureForTyping(char ch) {
        KeyStroke keyStroke = keyStrokeFor(ch);
        System.out.println("keyStroke = " + keyStroke + " " + ch);

        return withModifierMask(keyStroke.getModifiers(), typeKey(keyStroke.getKeyCode()));
    }

    private KeyStroke keyStrokeFor(char ch) {
        KeyStroke keyStroke = keyStrokes.get(ch);
        if (keyStroke != null) {
            return keyStroke;
        } else {
            throw new IllegalArgumentException("no stroke available for character '" + ch + "'");
        }
    }

    /**
     * Returns the default keyboard layout.  The name of the layout is specified by the
     * com.objogate.wl.keyboard system property.  If that is not set, the name of the default
     * {@link java.util.Locale Locale} is used.
     *
     * @return the default keyboard layout
     */
    public static KeyboardLayout getDefaultKeyboardLayout() {
        String layoutName = System.getProperty(KEYBOARD_LAYOUT, InputContext.getInstance().getLocale().getCountry());
        return getKeyboardLayout(layoutName);
    }

    public static KeyboardLayout getKeyboardLayout(String layoutName) {
        URL configURL = KeyboardLayout.class.getResource(layoutName + ".keyboard");
        if (configURL == null) {
            throw new IllegalArgumentException("keyboard layout " + layoutName + " not available.");
        }

        try {
            return new KeyboardLayout(layoutName, configURL);
        } catch (IOException e) {
            throw new IllegalStateException("could not parse " + layoutName + " keyboard layout properties");
        }
    }

    private void initialiseDefaults() {
        for (char ch = '0'; ch <= '9'; ch++) {
            keyStrokes.put(ch, KeyStroke.getKeyStroke(ch, 0));
        }
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            keyStrokes.put(Character.toLowerCase(ch), KeyStroke.getKeyStroke(ch, 0));
            keyStrokes.put(ch, KeyStroke.getKeyStroke(ch, InputEvent.SHIFT_MASK));
        }
        keyStrokes.put('\n', KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        keyStrokes.put('\t', KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
        keyStrokes.put('\b', KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0));
        keyStrokes.put(' ', KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
    }

    private void parseKeyStrokes(URL resource) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(resource.openStream()));
        try {
            parseKeyStrokes(in);
        }
        finally {
            in.close();
        }
    }

    private void parseKeyStrokes(BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            char ch = line.charAt(0);
            String keyStrokeSpec = line.substring(2);
            keyStrokes.put(ch, KeyStroke.getKeyStroke(keyStrokeSpec));
        }
    }
}
