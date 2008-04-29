package com.objogate.wl.probe;

import java.awt.Component;
import java.util.Collections;
import org.hamcrest.Description;
import com.objogate.wl.ComponentSelector;

/**
 * A ComponentFinder that always find a given component.
 * <p/>
 * This is useful for unit-testing a component.
 */
public class ComponentIdentity<T extends Component> implements ComponentSelector<T> {
    private final T component;

    public ComponentIdentity(T component) {
        this.component = component;
    }

    public static <U extends Component> ComponentIdentity<U> selectorFor(U component) {
        return new ComponentIdentity<U>(component);
    }
    
    public T component() {
        return component;
    }

    public java.util.List<T> components() {
        return Collections.singletonList(component);
    }

    public void probe() {
        // Nothing to do
    }

    public boolean isSatisfied() {
        return true;
    }

    public void describeTo(Description description) {
        description.appendText(" the exact ");
        description.appendText(component.getClass().getSimpleName());
        description.appendText(" '");
        description.appendValue(component.toString());
        description.appendText("' ");
    }

    public boolean describeFailureTo(Description description) {
        // Cannot fail
        return false;
    }
}
