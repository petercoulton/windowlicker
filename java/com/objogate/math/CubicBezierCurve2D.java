package com.objogate.math;

/**
 * A two-dimensional cubic B&eacute;zier curve
 */
public class CubicBezierCurve2D {
    private double _p1_x;
    private double _p1_y;
    private double _ctl1_x;
    private double _ctl1_y;
    private double _ctl2_x;
    private double _ctl2_y;
    private double _p2_x;
    private double _p2_y;


    /**
     * Constructs a CubicBezierCurve2D.
     *
     * @param p1   The start of the curve.
     * @param ctl1 The first control point.
     * @param ctl2 The second control point.
     * @param p2   The end of the curve.
     */
    public CubicBezierCurve2D(Vector2D p1,
                              Vector2D ctl1,
                              Vector2D ctl2,
                              Vector2D p2) {
        this(p1.x(), p1.y(),
             ctl1.x(), ctl1.y(),
             ctl2.x(), ctl2.y(),
             p2.x(), p2.y());
    }

    /**
     * Constructs a CubicBezierCurve2D.
     *
     * @param p1_x   The x coordinate of the start of the curve.
     * @param p1_y   The y coordinate of the start of the curve.
     * @param ctl1_x The x coordinate of the first control point.
     * @param ctl1_y The y coordinate of the first control point.
     * @param ctl2_x The x coordinate of the second control point.
     * @param ctl2_y The t coordinate of the second control point.
     * @param p2_x   The x coordinate of the end of the curve.
     */
    public CubicBezierCurve2D(double p1_x, double p1_y,
                              double ctl1_x, double ctl1_y,
                              double ctl2_x, double ctl2_y,
                              double p2_x, double p2_y) {
        _p1_x = p1_x;
        _p1_y = p1_y;
        _ctl1_x = ctl1_x;
        _ctl1_y = ctl1_y;
        _ctl2_x = ctl2_x;
        _ctl2_y = ctl2_y;
        _p2_x = p2_x;
        _p2_y = p2_y;
    }

    /**
     * Constructs a CubicBezierCurve2D from an array of point coordinates.
     *
     * @param curve An array of 8 doubles, the x and y coordinates of the start
     *              point, first control point, second control point, and end
     *              point, in that order.
     */
    public CubicBezierCurve2D(double[] curve) {
        this(curve[0], curve[1],
                curve[2], curve[3],
                curve[4], curve[5],
                curve[6], curve[7]);
    }

    /**
     * The start point of the curve.
     */
    public Vector2D p1() {
        return new Vector2D(_p1_x, _p1_y);
    }

    /**
     * The first control point of the curve.
     */
    public Vector2D ctl1() {
        return new Vector2D(_ctl1_x, _ctl1_y);
    }

    /**
     * The second control point of the curve.
     */
    public Vector2D ctl2() {
        return new Vector2D(_ctl2_x, _ctl2_y);
    }

    /**
     * The end point of the curve.
     */
    public Vector2D p2() {
        return new Vector2D(_p2_x, _p2_y);
    }

    /**
     * The point of the curve at parameter `u'.
     */
    public Vector2D p(double u) {
        return new Vector2D(x(u), y(u));
    }

    /**
     * The x coordinate of the curve at parameter `u'.
     */
    public double x(double u) {
        return interpolate(_p1_x, _ctl1_x, _ctl2_x, _p2_x, u);
    }

    /**
     * The y coordinate of the curve at parameter `u'.
     */
    public double y(double u) {
        return interpolate(_p1_y, _ctl1_y, _ctl2_y, _p2_y, u);
    }

    private static double interpolate(double p1, double p2,
                                      double p3, double p4,
                                      double u) {
        double inv_u = 1 - u;

        return inv_u * inv_u * inv_u * p1
                + 3 * inv_u * inv_u * u * p2
                + 3 * inv_u * u * u * p3
                + u * u * u * p4;
    }
}
