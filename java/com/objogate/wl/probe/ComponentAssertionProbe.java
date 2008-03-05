package com.objogate.wl.probe;

import java.awt.Component;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import com.objogate.wl.ComponentFinder;
import com.objogate.wl.ComponentSelector;

public class ComponentAssertionProbe<T extends Component> implements ComponentFinder<T> {
    private final ComponentSelector<T> selector;
    private final Matcher<? super T> assertion;

    private boolean assertionMet = false;

    public ComponentAssertionProbe(ComponentSelector<T> selector, Matcher<? super T> assertion) {
        this.assertion = assertion;
        this.selector = selector;
    }

    public java.util.List<T> components() {
        return selector.components();
    }

    public void probe() {
        selector.probe();
        assertionMet = selector.isSatisfied() && assertion.matches(selector.component());
    }

    public boolean isSatisfied() {
        return assertionMet;
    }
    
    public void describeTo(Description description) {
        description.appendDescriptionOf(selector)
                .appendText(", and is ")
                .appendDescriptionOf(assertion);
    
        if (selector.isSatisfied() && !assertionMet) {
        }
    }
    
    public void describeFailureTo(Description description) {
        description.appendText("it is not ")
                   .appendDescriptionOf(assertion);
    }
}
