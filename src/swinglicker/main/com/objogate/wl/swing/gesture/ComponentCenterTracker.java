package com.objogate.wl.swing.gesture;

import java.awt.Component;
import java.awt.Point;
import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.Tracker;
import com.objogate.wl.swing.ComponentSelector;

public class ComponentCenterTracker implements Tracker, SelfDescribing {
    private final Prober prober;
    private final ComponentScreenBoundsProbe probe;

    public ComponentCenterTracker(Prober prober, ComponentSelector<? extends Component> componentSelector) {
        this.prober = prober;
        this.probe = new ComponentScreenBoundsProbe(componentSelector);
    }

    public Point target(Point currentLocation) {
        prober.check(probe);

        return new Point((int) probe.bounds.getCenterX(), (int) probe.bounds.getCenterY());
    }

    public void describeTo(Description description) {
        description.appendText("center of ");
        description.appendDescriptionOf(probe);
    }
}
