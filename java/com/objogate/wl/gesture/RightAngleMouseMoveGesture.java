package com.objogate.wl.gesture;

import java.awt.Point;
import org.hamcrest.Description;
import com.objogate.wl.Gesture;
import com.objogate.wl.robot.Automaton;

public class RightAngleMouseMoveGesture implements Gesture {
    private final Tracker tracker;
    private final Mode mode;

    private RightAngleMouseMoveGesture(Tracker tracker, Mode mode) {
        this.tracker = tracker;
        this.mode = mode;
    }

    public void performGesture(Automaton automaton) {
        if (mode == Mode.H_THEN_V) {
            new MouseMoveGesture(new HorizontalOnlyTracker()).performGesture(automaton);
            new MouseMoveGesture(new VerticalOnlyTracker()).performGesture(automaton);
        } else {
            new MouseMoveGesture(new VerticalOnlyTracker()).performGesture(automaton);
            new MouseMoveGesture(new HorizontalOnlyTracker()).performGesture(automaton);
        }
    }

    public void describeTo(Description description) {
        description.appendText("Move Horizontally, then Vertically to ");
        description.appendValue(tracker);
    }

    private static enum Mode {
        H_THEN_V, V_THEN_H
    }

    public static RightAngleMouseMoveGesture createVerticalThenHorizontalMouseMoveGesture(Tracker tracker) {
        return new RightAngleMouseMoveGesture(tracker, Mode.V_THEN_H);
    }

    public static RightAngleMouseMoveGesture createHorizontalThenVerticalMouseMoveGesture(Tracker tracker) {
        return new RightAngleMouseMoveGesture(tracker, Mode.H_THEN_V);
    }

    public class HorizontalOnlyTracker implements Tracker {

        public Point target(Point currentLocation) {
            Point target = tracker.target(currentLocation);

            return new Point(target.x, currentLocation.y);
        }
    }

    public class VerticalOnlyTracker implements Tracker {
        public Point target(Point currentLocation) {
            Point target = tracker.target(currentLocation);

            return new Point(currentLocation.x, target.y);
        }
    }
}
