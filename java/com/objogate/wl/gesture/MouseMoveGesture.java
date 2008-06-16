package com.objogate.wl.gesture;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import static java.lang.Math.max;
import static java.lang.Math.min;
import org.hamcrest.Description;
import com.objogate.exception.Defect;
import com.objogate.math.CubicBezierCurve2D;
import com.objogate.math.Vector2D;
import com.objogate.wl.Gesture;
import com.objogate.wl.robot.Automaton;

public class MouseMoveGesture implements Gesture {
    private static final double ANIMATE_STEP = 0.02;
    private int animationDelayMs = 25;

    private final Tracker tracker;

    public MouseMoveGesture(Tracker tracker) {
        this.tracker = tracker;
    }

    public void performGesture(Automaton automaton) {
        Vector2D destination = null;
        for (double t = 0.0; t < 1.0; t += ANIMATE_STEP) {
            Point currentLocation = automaton.getPointerLocation();
            Point trackerDestination = getDestination(currentLocation);

            if (currentLocation.equals(trackerDestination)) {
                return;
            }

            destination = new Vector2D(clipToScreen(trackerDestination));

            Vector2D start = new Vector2D(automaton.getPointerLocation());
            Vector2D movement = destination.sub(start);

            if (movement.magnitude() < 2.0) {
                break;
            }

            Vector2D controlA = start.add(movement.mul(0.7));
            Vector2D controlB = start.add(movement.mul(0.8));
            CubicBezierCurve2D animation = new CubicBezierCurve2D(start, controlA, controlB, destination);

            moveMouse(automaton, animation.p(t));
            automaton.delay(animationDelayMs);
        }

        moveMouse(automaton, destination);

        automaton.delay(200);
    }

    private Point getDestination(Point currentLocation) {
        return tracker.target(currentLocation);
    }

    private Point clipToScreen(Point p) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point(clip(p.x, screenSize.width - 1), clip(p.y, screenSize.height - 1));
    }

    private int clip(int n, int maxN) {
        return max(0, min(n, maxN));
    }

    private void moveMouse(Automaton automaton, Vector2D d) {
        automaton.mouseMove((int) d.x(), (int) d.y());
    }

    public void describeTo(Description description) {
        description.appendText("move mouse to ");
        description.appendValue(tracker);
    }
}
