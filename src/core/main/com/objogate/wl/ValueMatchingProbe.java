package com.objogate.wl;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public abstract class ValueMatchingProbe<T> implements Probe {
    private final Matcher<? super T> criteria;
    private T snapshot;
    
    protected ValueMatchingProbe(Matcher<? super T> criteria) {
        this.criteria = criteria;
    }

    public void probe() {
        snapshot = snapshotValue();
    }

    protected abstract T snapshotValue();
    
    public boolean isSatisfied() {
        return criteria.matches(snapshot);
    }

    public void describeTo(Description description) {
        describeValueTo(description);
        description.appendText(" that ")
                   .appendDescriptionOf(criteria);
    }

    protected abstract void describeValueTo(Description description);

    public void describeFailureTo(Description description) {
        describeValueTo(description);
        description.appendText(" was ")
                   .appendValue(snapshot);
    }
}
