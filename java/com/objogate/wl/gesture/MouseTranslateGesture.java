package com.objogate.wl.gesture;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import org.hamcrest.Description;
import com.objogate.wl.Gesture;
import com.objogate.wl.robot.Automaton;

public class MouseTranslateGesture implements Gesture {
    private int animationDelayMs = 25;

    private final Tracker tracker;

    public MouseTranslateGesture(Tracker tracker) {
        this.tracker = tracker;
    }

    public void performGesture(Automaton automaton) {
        for (;;) {
            Point currentLocation = automaton.getPointerLocation();
            Point trackerDestination = tracker.target(currentLocation);

            if ( currentLocation.equals(trackerDestination)) {
                return;
            }

            automaton.delay(animationDelayMs);
            moveMouse(automaton, clipToScreen(trackerDestination));
        }

    }

    private Point clipToScreen(Point p) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point(clip(p.x, screenSize.width - 1), clip(p.y, screenSize.height-1));
    }

    private int clip(int n, int maxN) {
        return Math.max(0, Math.min(n, maxN));
    }

    private void moveMouse(Automaton automaton, Point d) {
        automaton.mouseMove((int) d.getX(), (int) d.getY());
    }

    public void describeTo(Description description) {
        description.appendText("move mouse to ");
        description.appendValue(tracker);
    }
}
