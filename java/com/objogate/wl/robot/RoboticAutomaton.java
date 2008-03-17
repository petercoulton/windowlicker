package com.objogate.wl.robot;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import com.objogate.exception.Defect;
import com.objogate.wl.keyboard.KeyboardLayout;

public class RoboticAutomaton implements Automaton {
    private final Robot robot;
    private final KeyboardLayout keyboard;

    public RoboticAutomaton(Robot robot, KeyboardLayout keyboard) {
        this.robot = robot;
        this.keyboard = keyboard;
    }

    public RoboticAutomaton(KeyboardLayout keyboard) {
        this(createRobot(), keyboard);
    }

    public RoboticAutomaton() {
        this(createRobot(), KeyboardLayout.getDefaultKeyboardLayout());
    }

    private static Robot createRobot() {
        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new Defect("could not create AWT robot", e);
        }
        return robot;
    }

    public Point getPointerLocation() {
        return MouseInfo.getPointerInfo().getLocation();
    }

    public void mouseMove(int x, int y) {
        robot.mouseMove(x, y);
    }

    public void mousePress(int buttons) {
        robot.mousePress(buttons);
    }

    public void mouseRelease(int buttons) {
        robot.mouseRelease(buttons);
    }

    public void mouseWheel(int wheelAmt) {
        robot.mouseWheel(wheelAmt);
    }

    public void keyPress(int keycode) {
        robot.keyPress(keycode);
    }

    public void keyRelease(int keycode) {
        robot.keyRelease(keycode);
    }

    public void typeCharacter(char ch) {
        keyboard.gestureForTyping(ch).performGesture(this);
    }

    public void delay(int ms) {
        robot.delay(ms);
    }
}
