package com.objogate.wl.gesture;

import java.awt.Component;
import java.awt.Rectangle;
import org.hamcrest.Description;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Probe;

class ComponentScreenBoundsProbe implements Probe {
    private final ComponentSelector componentSelector;

    public Rectangle bounds;


    public ComponentScreenBoundsProbe(ComponentSelector componentSelector) {
        this.componentSelector = componentSelector;
    }

    public void probe() {
        componentSelector.probe();

        // if the component cant be found - probably not rendered yet
        if ( componentSelector.components().isEmpty() ) {
            bounds = null;
            return;
        }

        Component component = componentSelector.component();
        if (component.isShowing()) {
            bounds = new Rectangle(component.getLocationOnScreen(), component.getSize());
        }
        else {
            bounds = null;
        }
    }

    public boolean isSatisfied() {
        return bounds != null && bounds.getWidth() > 0 && bounds.getHeight() > 0;
    }

    public void describeTo(Description description) {
        description.appendText(" screen dimensions of ");
        description.appendDescriptionOf(componentSelector);
    }
}
