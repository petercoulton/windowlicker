package com.objogate.wl.gesture;

import java.awt.Point;
import org.hamcrest.Description;
import com.objogate.wl.Gesture;
import com.objogate.wl.robot.Automaton;

public class HorizontalThenVerticalMouseMoveGesture implements Gesture {
    private Tracker tracker;

    public HorizontalThenVerticalMouseMoveGesture(Tracker tracker) {
        this.tracker = tracker;
    }

    public void performGesture(Automaton automaton) {
        new MouseMoveGesture(new HorizontalOnlyTracker()).performGesture(automaton);
        new MouseMoveGesture(new VerticalOnlyTracker()).performGesture(automaton);
    }

    public void describeTo(Description description) {
        description.appendText("Move Horizontally, then Vertically to ");
        description.appendValue(tracker);
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
