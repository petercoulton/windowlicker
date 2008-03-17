package com.objogate.wl.probe;

import java.awt.Component;
import org.hamcrest.Matcher;
import com.objogate.wl.ComponentFinder;
import com.objogate.wl.ComponentSelector;

public class ComponentFinders {
    /**
     * Returns a selector that identifies all subcomponents of the component driven by this driver that match
     * the given criteria.
     *
     * @param type     The type of subcomponents to finde
     * @param criteria The criteria by which to select subcomponents
     * @return a selector that identifies the component driven by this driver.
     */
    public static <U extends Component> ComponentFinder<U> all(Class<U> type, Matcher<? super U> criteria, ComponentFinder<? extends Component> selector) {
        return new RecursiveComponentFinder<U>(type, criteria, selector);
    }

    /**
     * Returns a selector that identifies a single subcomponent of the component driven by this driver that matches
     * the given criteria.
     *
     * @param type     The type of subcomponents to finde
     * @param criteria The criteria by which to select subcomponents
     * @return a selector that identifies the component driven by this driver.
     */
    public static <U extends Component> ComponentSelector<U> the(Class<U> type, Matcher<? super U> criteria, ComponentFinder<? extends Component> selector) {
        return new SingleComponentFinder<U>(all(type, criteria, selector));
    }

}
