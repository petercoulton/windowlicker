package com.objogate.wl;

import java.awt.Component;

/**
 * A ComponentFinder that is satisfied only if it finds a single, matching component.
 */
public interface ComponentSelector<T extends Component> extends ComponentFinder<T> {
    T component();
}
