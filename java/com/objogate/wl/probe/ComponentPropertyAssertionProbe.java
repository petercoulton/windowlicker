package com.objogate.wl.probe;

import java.awt.Component;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import com.objogate.wl.ComponentQuery;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Probe;

public class ComponentPropertyAssertionProbe<C extends Component,P> implements Probe {
    private final ComponentSelector<C> selector;
    private final ComponentQuery<C,P> propertyValueQuery;
    private final Matcher<? super P> propertyValueMatcher;

    private P propertyValue;

    public ComponentPropertyAssertionProbe(ComponentSelector<C> selector, ComponentQuery<C,P> propertyValueQuery, Matcher<? super P> propertyValueMatcher) {
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
            .appendDescriptionOf(propertyValueQuery)
            .appendText(" ")
            .appendDescriptionOf(propertyValueMatcher)
            .appendText("\n    in ")
            .appendDescriptionOf(selector);
    }
    
    public void describeFailureTo(Description description) {
        description.appendDescriptionOf(propertyValueQuery)
                   .appendText(" was ")
                   .appendValue(propertyValue);
    }
}
