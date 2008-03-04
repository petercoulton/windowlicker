package com.objogate.math;

import java.awt.Point;

public final class Vector2D {
    private final double x, y;
    
    public Vector2D() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(x);
        bits ^= Double.doubleToLongBits(y) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Vector2D
                && this.equals((Vector2D) o);
    }

    public boolean equals(Vector2D v) {
        return x == v.x && y == v.y;
    }

    public boolean equals(Vector2D v, double allowable_error) {
        return Math.abs(x - v.x) <= allowable_error
                && Math.abs(y - v.y) <= allowable_error;
    }

    public double magnitudeSquared() {
        return x * x + y * y;
    }

    public double magnitude() {
        return Math.sqrt(magnitudeSquared());
    }

    public Vector2D normalise() {
        return this.div(this.magnitude());
    }

    public Vector2D add(Vector2D v) {
        return new Vector2D(x + v.x, y + v.y);
    }

    public Vector2D sub(Vector2D v) {
        return new Vector2D(x - v.x, y - v.y);
    }

    public Vector2D mul(double n) {
        return new Vector2D(x * n, y * n);
    }

    public Vector2D div(double n) {
        return new Vector2D(x / n, y / n);
    }

    public double dot(Vector2D v) {
        return (x * v.x) + (y * v.y);
    }

    public Vector2D rotate(double cosTheta, double sinTheta) {
        return new Vector2D(x * cosTheta - y * sinTheta,
                x * sinTheta + y * cosTheta);
    }

    public Vector2D rotate(double radians) {
        return rotate(Math.cos(radians), Math.sin(radians));
    }

    public Vector2D translate(double dx, double dy) {
        return new Vector2D(x + dx, y + dy);
    }

    public Vector2D scale(double sx, double sy) {
        return new Vector2D(x * sx, y * sy);
    }

    public double distanceSquared(Vector2D p) {
        return sub(p).magnitudeSquared();
    }

    public double distance(Vector2D p) {
        return sub(p).magnitude();
    }

    public double cosAngle(Vector2D v) {
        return this.dot(v) / (this.magnitude() * v.magnitude());
    }

    public double angle(Vector2D v) {
        return Math.acos(cosAngle(v));
    }

    public boolean isPerpendicular(Vector2D v) {
        return cosAngle(v) == 0;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
    
    public Point floor() {
        return new Point((int) Math.floor(x), (int) Math.floor(y));
    }

    public Point ceil() {
        return new Point((int) Math.ceil(x), (int) Math.ceil(y));
    }

    public Point round() {
        return new Point((int) Math.round(x), (int) Math.round(y));
    }

    public static Vector2D polar(double angle, double length) {
        return new Vector2D(Math.cos(angle) * length, Math.sin(angle) * length);
    }
}
