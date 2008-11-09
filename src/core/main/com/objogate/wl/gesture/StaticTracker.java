package com.objogate.wl.gesture;

import java.awt.Point;

/**
 * Tracker for something that doesn't, in fact, move
 */
public class StaticTracker implements Tracker {
    private Point point;

    public StaticTracker(Point point) {
        this.point = point;
    }

    public Point target(Point currentLocation) {
        return point;
    }
}
