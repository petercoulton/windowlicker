package com.objogate.wl.gesture;

import java.awt.Component;
import java.awt.Point;
import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;

public class ComponentEdgeTracker implements Tracker, SelfDescribing {
    private final Prober prober;
    private Edge edge;
    private int offset;
    private final ComponentScreenBoundsProbe probe;

    public enum Edge {
        Left, Right, Top, Bottom
    }

    public ComponentEdgeTracker(Prober prober, ComponentSelector<? extends Component> componentSelector, Edge edge, int offset) {
        this.prober = prober;
        this.edge = edge;
        this.offset = offset;
        this.probe = new ComponentScreenBoundsProbe(componentSelector);
    }

    public Point target(Point currentLocation) {
        prober.check(probe);

        switch (edge) {
            case Left:
                return new Point((int)probe.bounds.getMinX() + offset, (int)probe.bounds.getCenterY());
            case Right:
                return new Point((int)probe.bounds.getMaxX() + offset, (int)probe.bounds.getCenterY());
            case Top:
                return new Point((int)probe.bounds.getCenterX(), (int)probe.bounds.getMinY() + offset);
            case Bottom:
                return new Point((int)probe.bounds.getCenterX(), (int)probe.bounds.getMaxY() + offset);
            default:
                throw new UnsupportedOperationException("do not support that edge yet");
        }
    }

    public void describeTo(Description description) {
        description.appendText("center of " );
        description.appendDescriptionOf(probe);
    }
}