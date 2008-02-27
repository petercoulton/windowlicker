package com.objogate.wl.gesture;

import java.awt.Point;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;

/**
 * @Deprecated introduced temporarily as refactoring code to use Trackers, but should be replaced
 *             by a table cell tracker, list element tracker, etc. as appropriate
 *
 */
public class ComponentOffsetTracker implements Tracker {
    private final Prober prober;
    private final ComponentScreenBoundsProbe probe;
    private final Point offset;

    public ComponentOffsetTracker(Prober prober, ComponentSelector componentSelector, int offsetX, int offsetY) {
        this.prober = prober;
        this.probe = new ComponentScreenBoundsProbe(componentSelector);
        this.offset = new Point(offsetX, offsetY);
    }

    public Point target(Point currentLocation) {
        prober.check("moving to component", probe);
        return new Point(probe.bounds.x + offset.x, probe.bounds.y + offset.y);
    }
}
