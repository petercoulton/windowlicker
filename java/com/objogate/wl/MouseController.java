package com.objogate.wl;

import static javax.swing.SwingUtilities.convertPointToScreen;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Arrays;
import com.objogate.math.CubicBezierCurve2D;
import com.objogate.math.Vector2D;

@Deprecated //TODO: replace with gestures
public class MouseController {
    private static final double ANIMATE_STEP = 0.02;
    private int animationDelay;
    private final Robot robot = robot();

    public static Robot robot() {
        try {
            return new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("no gui!", e);
        }
    }

    public MouseController() {
        this(250); //TODO: make this configurable
    }

    public MouseController(int animationDuration) {
        this.animationDelay = (int) (animationDuration * ANIMATE_STEP);
    }

    public void leftClick() {
        click(InputEvent.BUTTON1_MASK);
    }

    public void rightClick() {
        click(InputEvent.BUTTON3_MASK);
    }

    // james says: double click is quite sensitive to timings on linux. >40ms delay will cause it to fail.
    public void doubleClick() {
        leftClick();
        robot.delay(20);
        leftClick();
    }

    private void click(int buttonMask) {
        robot.mousePress(buttonMask);
        robot.delay(150);
        robot.mouseRelease(buttonMask);
    }

    public void movePointerToCenterOf(Rectangle rectangle) {
        movePointerTo(rectangle.getCenterX(), rectangle.getCenterY());
    }

    public void movePointerTo(Point p) {
        movePointerTo(p.x, p.y);
    }

    public void movePointerTo(double screenX, double screenY) {
        Vector2D destination = new Vector2D(screenX, screenY);

        destination = clipToScreen(destination);

        Vector2D start = new Vector2D(MouseInfo.getPointerInfo().getLocation());
        Vector2D movement = destination.sub(start);
        double distance = movement.magnitude();

        if (distance > 4) {
            Vector2D controlA = start.add(movement.mul(0.7));
            Vector2D controlB = start.add(movement.mul(0.8));
            CubicBezierCurve2D animation = new CubicBezierCurve2D(start, controlA, controlB, destination);

            for (double t = 0.0; t < 1.0; t += ANIMATE_STEP) {
                robot.delay(animationDelay);
                movePointerTo(animation.p(t));
            }
        }

        movePointerTo(destination);
    }

    private Vector2D clipToScreen(Vector2D destination) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        double x = destination.x() > screenSize.getWidth() ? screenSize.getWidth() : destination.x();
        double y = destination.y() > screenSize.getHeight() ? screenSize.getHeight() : destination.y();

        return new Vector2D( x, y );
    }

    private void movePointerTo(Vector2D d) {
        robot.mouseMove((int) d.x(), (int) d.y());
    }

    public void dragHorizonally(int relativeX) {
        whileHolding(leftButton(), horizontalMovement(relativeX));
    }

    public void dragVertically(int relativeY) {
        whileHolding(leftButton(), verticalMovement(relativeY));
    }

    public void whileHolding(MouseButton button, MouseMovement... movements) {
        whileHolding(button, Arrays.asList(movements));
    }

    public void whileHolding(MouseButton button, Iterable<MouseMovement> movements) {
        button.press();
        for (MouseMovement movement : movements) {
            movement.move();
        }
        button.release();
    }

    public void setAnimationDelay(int delay) {
        this.animationDelay = delay;
    }

    public MouseButton leftButton() {
        return new MouseButton() {
            public void press() {
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.delay(100);
            }

            public void release() {
                robot.delay(100);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            }
        };
    }

    public MouseMovement relativeMovement(final Point relativePoint, final Component component) {
        return new MouseMovement() {
            public void move() {
                Point absolutePoint = new Point(relativePoint); // avoid aliasing errors
                convertPointToScreen(absolutePoint, component);
                movePointerTo(absolutePoint);
            }
        };
    }

    public MouseMovement horizontalMovement(final int relativeX) {
        return new MouseMovement() {
            public void move() {
                Point location = MouseInfo.getPointerInfo().getLocation();
                int targetX = location.x + relativeX;
                int targetY = location.y;
                movePointerTo(targetX, targetY);
            }
        };
    }

    public MouseMovement verticalMovement(final int relativeY) {
        return new MouseMovement() {
            public void move() {
                Point location = MouseInfo.getPointerInfo().getLocation();
                int targetX = location.x;
                int targetY = location.y + relativeY;
                movePointerTo(targetX, targetY);
            }
        };
    }

    public interface MouseMovement {
        void move();
    }

    public interface MouseButton {
        void press();

        void release();
    }
}
