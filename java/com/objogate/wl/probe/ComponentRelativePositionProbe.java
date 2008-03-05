package com.objogate.wl.probe;

import java.awt.Component;
import java.awt.Rectangle;

import org.hamcrest.Description;

import com.objogate.exception.Defect;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Probe;

//TODO: is this really useful?  It complicates the design and I'm a bit uncomfortable testing layout in end-to-end tests.
public class ComponentRelativePositionProbe implements Probe {
    private final ComponentSelector<? extends Component> selectorA;
    private final ComponentSelector<? extends Component> selectorB;
    private final RelativePosition relativePosition;

    private boolean satisfied = false;

    public enum RelativePosition {
        ABOVE, BELOW, LEFTOF, RIGHTOF
    }

    public ComponentRelativePositionProbe(ComponentSelector<? extends Component> selectorA, 
                                          RelativePosition position, 
                                          ComponentSelector<? extends Component> selectorB) 
    {
        this.selectorA = selectorA;
        this.relativePosition = position;
        this.selectorB = selectorB;
    }

    public void probe() {

        selectorA.probe();
        selectorB.probe();

        if (selectorA.isSatisfied() && selectorB.isSatisfied()) {
            Rectangle boundsA = getBoundsFor(selectorA.component());
            Rectangle boundsB = getBoundsFor(selectorB.component());

            switch(relativePosition) {
                case ABOVE:
                    satisfied = boundsA.getMaxY() <= boundsB.getMinY();
                    break;
                case BELOW:
                    satisfied = boundsA.getMaxY() >= boundsB.getMaxY();
                    break;
                case LEFTOF:
                    satisfied = boundsA.getMaxX() <= boundsB.getMinX();
                    break;
                case RIGHTOF:
                    satisfied = boundsB.getMinX() >= boundsA.getMaxX();
                    break;
                default:
                    throw new Defect(relativePosition + " position not supported");
            }
        }
    }

    public boolean isSatisfied() {
        return satisfied;
    }

    private Rectangle getBoundsFor(Component component) {
        return new Rectangle(component.getLocationOnScreen(), component.getSize());
    }

    public void describeTo(Description description) {
        description.appendDescriptionOf(selectorA);
        description.appendText("\n");
        description.appendValue(relativePosition);
        description.appendText("\n");
        description.appendDescriptionOf(selectorB);
    }

    public boolean describeFailureTo(Description description) {
        description.appendText("was not in the expected relative position");
        return true;
    }
}
