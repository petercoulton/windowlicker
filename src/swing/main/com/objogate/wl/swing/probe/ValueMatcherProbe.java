package com.objogate.wl.swing.probe;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import com.objogate.wl.Probe;


public class ValueMatcherProbe<T> implements Probe {
    private final Matcher<? super T> matcher;
    private final String message;
    private boolean hasReceivedAValue = false;
    private T receivedValue;

    public ValueMatcherProbe(Matcher<? super T> matcher, String message) {
        this.matcher = matcher;
        this.message = message;
    }

    public void describeFailureTo(Description description) {
        description.appendText(message).appendText(" ").appendDescriptionOf(matcher).appendText(". ");
        if (hasReceivedAValue) {
            description.appendText(". Received: ").appendValue(receivedValue);
        } else {
            description.appendText(" Received nothing");
        }
    }

    public boolean isSatisfied() {
        return matcher.matches(receivedValue);
    }

    public void probe() {
        // nothing to do.
    }

    public void describeTo(Description description) {
        description.appendText(message).appendText(" ").appendDescriptionOf(matcher);
    }

    public void setReceivedValue(T receivedValue) {
        this.receivedValue = receivedValue;
        this.hasReceivedAValue = true;
    }
}