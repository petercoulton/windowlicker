package com.objogate.wl.probe;

import java.awt.Component;
import org.hamcrest.Description;
import com.objogate.wl.ComponentFinder;
import com.objogate.wl.ComponentSelector;

/**
 * A ComponentFinder that is satisfied only if it finds a single, matching component.
 */
public class SingleComponentFinder<T extends Component> implements ComponentSelector<T> {
    private final ComponentFinder<T> finder;

    public SingleComponentFinder(ComponentFinder<T> finder) {
        this.finder = finder;
    }

    public T component() {
        return components().get(0);
    }

    public java.util.List<T> components() {
        return finder.components();
    }

    public boolean isSatisfied() {
        return finder.isSatisfied() && isSingle();
    }

    public void probe() {
        finder.probe();
    }
    
    public void describeTo(Description description) {
        description.appendText("exactly 1 ")
                   .appendDescriptionOf(finder);

        if (finder.isSatisfied() && !isSingle()) {
            description.appendText("\nbut found " + components().size());
        }
    }

    private boolean isSingle() {
      return components().size() == 1;
    }
}
