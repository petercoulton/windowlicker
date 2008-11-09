package com.objogate.wl.swing.probe;

import java.awt.Component;
import org.hamcrest.Description;
import com.objogate.wl.Probe;
import com.objogate.wl.swing.ComponentFinder;
import com.objogate.wl.swing.ComponentManipulation;

public class ComponentManipulatorProbe<T extends Component> implements Probe {
    private final ComponentFinder<T> finder;
    private final ComponentManipulation<? super T> manipulation;

    public ComponentManipulatorProbe(ComponentFinder<T> finder, ComponentManipulation<? super T> manipulation) {
        this.finder = finder;
        this.manipulation = manipulation;
    }

    public void probe() {
        finder.probe();
        if (finder.isSatisfied()) {
            for (T component : finder.components()) {
                manipulation.manipulate(component);
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
