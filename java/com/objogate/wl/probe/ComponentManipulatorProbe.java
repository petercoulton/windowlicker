package com.objogate.wl.probe;

import java.awt.Component;
import org.hamcrest.Description;
import com.objogate.wl.ComponentFinder;
import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.Probe;

public class ComponentManipulatorProbe<T extends Component> implements Probe {
    private final ComponentFinder<T> finder;
    private final ComponentManipulation<? super T>[] manipulations;

    public ComponentManipulatorProbe(ComponentFinder<T> finder, ComponentManipulation<? super T>... manipulations) {
        this.finder = finder;
        this.manipulations = manipulations;
    }

    public void probe() {
        finder.probe();
        if (finder.isSatisfied()) {
            for (T component : finder.components()) {
                for (ComponentManipulation<? super T> manipulation : manipulations) {
                    manipulation.manipulate(component);
                }
            }
        }
    }

    public boolean isSatisfied() {
        return finder.isSatisfied();
    }

    public void describeTo(Description description) {
        finder.describeTo(description);
    }

    public boolean describeFailureTo(Description description) {
        return finder.describeFailureTo(description);
    }
}
