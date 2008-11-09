package com.objogate.wl.swing.probe;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.objogate.wl.Probe;

public class ValueMatcherProbe<T> implements Probe {
  private final Matcher<T> matcher;
  private final String message;
  private boolean hasReceivedAValue = false;
  private T receivedValue;
  
  public ValueMatcherProbe(Matcher<T> matcher, String message) {
    this.matcher = matcher;
    this.message = message;
  }
  
  public boolean describeFailureTo(Description description) {
    description.appendText(message).appendText(" ").appendDescriptionOf(matcher).appendText(". ");
    if (hasReceivedAValue) {
      description .appendText(". Received: <").appendValue(receivedValue).appendText(">");
    } else {
      description.appendText(" Received nothing");
    }
    return isSatisfied();
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