package com.objogate.wl.gesture;

import java.awt.Adjustable;
import java.awt.Point;
import org.hamcrest.Description;
import com.objogate.exception.Defect;
import com.objogate.wl.Gesture;
import com.objogate.wl.robot.Automaton;

public class MouseMoveAbsoluteGesture implements Gesture {
    private final int direction;
    private final int pixels;


    public MouseMoveAbsoluteGesture(int direction, int pixels) {
        this.direction = direction;
        this.pixels = pixels;
    }

    public void performGesture(Automaton automaton) {
        Point location = automaton.getPointerLocation();

        switch (direction) {
            case Adjustable.HORIZONTAL:
                automaton.mouseMove((int)(location.getX()) + pixels, (int)location.getY());
                break;
            case Adjustable.VERTICAL:
                automaton.mouseMove((int)location.getX(), (int)location.getY() + pixels);
                break;
            default:
                throw new Defect("Only allowed Adjustable.HORIZONTAL and Adjustable.VERTICAL");
        }
    }


    public void describeTo(Description description) {
        description.appendText("todo");
    }
}
