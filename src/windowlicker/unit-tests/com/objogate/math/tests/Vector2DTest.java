package com.objogate.math.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import org.junit.Test;

import com.objogate.math.Vector2D;


public class Vector2DTest {
    @Test
    public void isZeroVectorWhenConstructedWithNoCoordinates() {
        Vector2D v = new Vector2D();
        assertEquals(0.0, v.x(), 0.0);
        assertEquals(0.0, v.y(), 0.0);
    }

    @Test
    public void canBeConstructedWithXAndYCoordinates() {
        Vector2D v = new Vector2D(1.0, 2.0);
        assertEquals(1.0, v.x(), 0.0);
        assertEquals(2.0, v.y(), 0.0);
    }

    @Test
    public void canBeConstructedFromAWTPoint() {
        Vector2D v = new Vector2D(new Point(3, 4));
        assertEquals(3.0, v.x(), 0.0);
        assertEquals(4.0, v.y(), 0.0);
    }

    @Test
    public void comparesEquality() {
        Vector2D v1 = vec(1.0, -2.0);
        Vector2D v2 = vec(1.0, -2.0);
        Vector2D v3 = vec(11.0, 15.0);

        assertEquals(v1, v1);
        assertEquals(v1, v2);
        assertTrue(!v1.equals(v3));
    }

    @Test
    public void canReturnMagnitude() {
        Vector2D v;

        v = vec(3.0, 4.0);
        assertEquals(25.0, v.magnitudeSquared(), 0.0);
        assertEquals(5.0, v.magnitude(), 0.0);

        v = vec(-3.0, 4.0);
        assertEquals(25.0, v.magnitudeSquared(), 0.0);
        assertEquals(5.0, v.magnitude(), 0.0);

        v = vec(3.0, -4.0);
        assertEquals(25.0, v.magnitudeSquared(), 0.0);
        assertEquals(5.0, v.magnitude(), 0.0);

        v = vec(-3.0, -4.0);
        assertEquals(25.0, v.magnitudeSquared(), 0.0);
        assertEquals(5.0, v.magnitude(), 0.0);
    }

    @Test
    public void canBeNormalised() {
        Vector2D v;

        v = vec(11.0, 0.0);
        assertEquals(vec(1.0, 0.0), v.normalise());
        assertEquals(1.0, v.normalise().magnitude(), 0.0);

        v = vec(0.0, 11.0);
        assertEquals(vec(0.0, 1.0), v.normalise());
        assertEquals(1.0, v.normalise().magnitude(), 0.0);

        v = vec(-11.0, 0.0);
        assertEquals(vec(-1.0, 0.0), v.normalise());
        assertEquals(1.0, v.normalise().magnitude(), 0.0);

        v = vec(0.0, -11);
        assertEquals(vec(0.0, -1.0), v.normalise());
        assertEquals(1.0, v.normalise().magnitude(), 0.0);

        v = vec(3.0, 4.0);
        assertEquals(vec(0.6, 0.8), v.normalise());
        assertEquals(1.0, v.normalise().magnitude(), 0.0);
    }

    @Test
    public void canBeAdded() {
        assertEquals(vec(2, 0), vec(1, 0).add(vec(1, 0)));
        assertEquals(vec(0, 0), vec(1, 0).add(vec(-1, 0)));
        assertEquals(vec(1, 1), vec(1, 0).add(vec(0, 1)));
        assertEquals(vec(2, 1).add(vec(4, 3)), vec(4, 3).add(vec(2, 1)));
        assertEquals(vec(2, 3), vec(2, 3).add(vec(0, 0)));
    }

    @Test
    public void canBeSubtracted() {
        assertEquals(vec(1, 2), vec(2, 4).sub(vec(1, 2)));
        assertEquals(vec(0, 0), vec(2, 4).sub(vec(2, 4)));
        assertEquals(vec(-1, -2), vec(2, 4).sub(vec(3, 6)));
    }

    @Test
    public void canReturnAngleFromAnotherVector() {
        assertTrue(vec(1, 0).isPerpendicular(vec(0, 1)));
        assertEquals(0.0, vec(1, 0).cosAngle(vec(0, 1)), 0.0000000001);
        assertEquals(Math.PI / 2, vec(1, 0).angle(vec(0, 1)), 0.0000000001);
        assertEquals(Math.PI / 4, vec(1, 0).angle(vec(1, 1)), 0.0000000001);
    }

    @Test
    public void canBeCreatedFromPolarCoordinates() {
        final double angle = Math.PI / 8.0; // 22.5 degrees
        final double distance = 16.0;

        Vector2D v = Vector2D.polar(angle, distance);

        assertEquals("x coordinate", distance * Math.cos(angle), v.x(), 0);
        assertEquals("y coordinate", distance * Math.sin(angle), v.y(), 0);
    }

    private static Vector2D vec(double x, double y) {
        return new Vector2D(x, y);
    }
}
