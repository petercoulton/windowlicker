package com.objogate.wl;

import java.awt.Point;

/**
 * Wanted to have an interface so that more complex interactions can be tested without using a real awt robot
 */
public interface Automaton {
    Point getPointerLocation();

    void mouseMove(int x, int y);

    void mousePress(int buttons);

    void mouseRelease(int buttons);

    void mouseWheel(int amount);

    void keyPress(int keycode);

    void keyRelease(int keycode);

    void typeCharacter(char ch);

    void delay(int ms);
}
