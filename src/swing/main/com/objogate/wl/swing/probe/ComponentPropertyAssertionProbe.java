package com.objogate.wl.swing.probe;

import java.awt.Component;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import com.objogate.wl.Probe;
import com.objogate.wl.Query;
import com.objogate.wl.swing.ComponentSelector;

public class ComponentPropertyAssertionProbe<C extends Component, P> implements Probe {
    private final ComponentSelector<C> selector;
    private final Query<? super C, P> propertyValueQuery;
    private final Matcher<? super P> propertyValueMatcher;

    private P propertyValue;

    public ComponentPropertyAssertionProbe(ComponentSelector<C> selector, Query<? super C, P> propertyValueQuery, Matcher<? super P> propertyValueMatcher) {
        this.selector = selector;
        this.propertyValueQuery = propertyValueQuery;
        this.propertyValueMatcher = propertyValueMatcher;
    }

    public void probe() {
        selector.probe();
        if (selector.isSatisfied()) {
            propertyValue = propertyValueQuery.query(selector.component());
        }
    }

    public boolean isSatisfied() {
        return selector.isSatisfied() && propertyValueMatcher.matches(propertyValue);
    }

    public void describeTo(Description description) {
        description
                .appendDescriptionOf(selector)
                .appendText("\nand check that its ")
                .appendDescriptionOf(propertyValueQuery)
                .appendText(" is ")
                .appendDescriptionOf(propertyValueMatcher);
    }

    public void describeFailureTo(Description description) {
        selector.describeFailureTo(description);
        if (selector.isSatisfied()) {
        description.appendText("\n    ")
                .appendDescriptionOf(propertyValueQuery)
                .appendText(" was ")
                .appendValue(propertyValue);
        }
    }
}
