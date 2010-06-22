package com.objogate.wl.swing.probe;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Description;
import com.objogate.wl.swing.ComponentFinder;
import com.objogate.wl.swing.ComponentSelector;

public class NthComponentFinder<T extends Component> implements ComponentSelector<T> {
    private ComponentFinder<T> parentOrOwnerFinder;
    private int whichToChoose;

    public NthComponentFinder(ComponentFinder<T> parentOrOwnerFinder, int whichToChoose) {
        this.parentOrOwnerFinder = parentOrOwnerFinder;
        this.whichToChoose = whichToChoose;
    }

    @SuppressWarnings("unchecked")
    public List<T> components() {
        final List<T> components = parentOrOwnerFinder.components();
        return components.isEmpty() ? new ArrayList<T>() : Arrays.asList(components.get(whichToChoose));
    }

    public T component() {
        return parentOrOwnerFinder.components().get(whichToChoose);
    }

    public void probe() {
        parentOrOwnerFinder.probe();
    }

    public boolean isSatisfied() {
        return parentOrOwnerFinder.isSatisfied() && parentOrOwnerFinder.components().size() > whichToChoose;
    }

    public void describeTo(Description description) {
        description.appendText("the ");
        description.appendValue(whichToChoose);
        description.appendText(" component from those matching ");
        description.appendDescriptionOf(parentOrOwnerFinder);
    }

    /**
     * This expects the ComponentFinder it wraps to describe how many were actually found.
     */
    public void describeFailureTo(Description description) {
        parentOrOwnerFinder.describeFailureTo(description);
    }
}
