package com.objogate.wl.swing.gesture;

import java.awt.Component;
import java.awt.Point;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.Tracker;
import com.objogate.wl.swing.ComponentSelector;

/**
 * @Deprecated introduced temporarily as refactoring code to use Trackers, but should be replaced
 * by a table cell tracker, list element tracker, etc. as appropriate
 */
public class ComponentOffsetTracker implements Tracker {
    private final Prober prober;
    private final ComponentScreenBoundsProbe probe;
    private final Point offset;

    public ComponentOffsetTracker(Prober prober, ComponentSelector<? extends Component> componentSelector, int offsetX, int offsetY) {
        this.prober = prober;
        this.probe = new ComponentScreenBoundsProbe(componentSelector);
        this.offset = new Point(offsetX, offsetY);
    }

    public Point target(Point currentLocation) {
        prober.check(probe);
        return new Point(probe.bounds.x + offset.x, probe.bounds.y + offset.y);
    }
}
