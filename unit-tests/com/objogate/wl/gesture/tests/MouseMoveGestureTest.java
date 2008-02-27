package com.objogate.wl.gesture.tests;

import java.awt.AWTException;
import java.awt.Point;
import org.junit.Test;
import com.objogate.wl.gesture.MouseMoveGesture;
import com.objogate.wl.gesture.StaticTracker;
import com.objogate.wl.gesture.Tracker;
import com.objogate.wl.robot.RoboticAutomaton;

public class MouseMoveGestureTest {
    // TODO (nat) What is this testing?
    @Test public void
    alwaysMovesMouseTowardsTarget() throws AWTException {
        Tracker destination = new StaticTracker(new Point(1000,1000));
        MouseMoveGesture gesture = new MouseMoveGesture(destination);
        gesture.performGesture(new RoboticAutomaton());
    }
}
