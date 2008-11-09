/**
 * Matches an iterable collection of Components against an array of Matchers.
 * Each Matcher has to match the component at its position in the iteration. 
 */
package com.objogate.wl.swing.matcher;

import java.awt.Component;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JComponent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public final class IterableComponentsMatcher extends TypeSafeMatcher<Iterable<? extends Component>> {
  private final Matcher<? extends JComponent>[] matchers;

  public IterableComponentsMatcher(Matcher<? extends JComponent>[] matchers) {
    this.matchers = matchers;
  }

  @Override public boolean matchesSafely(Iterable<? extends Component> components) {
    Iterator<? extends Component> iterator = components.iterator();
    for (Matcher<? extends JComponent> matcher : matchers) {
      if (! isAMatch(matcher, iterator)) {
        return false;
      }
    }
    return ! iterator.hasNext();
  }

  public void describeTo(Description description) {
    description.appendList("with cells ", ", ", "", Arrays.asList(matchers));
  }

  private boolean isAMatch(Matcher<? extends JComponent> matcher, Iterator<? extends Component> iterator) {
    return iterator.hasNext() && matcher.matches(iterator.next());
  }

  public static Matcher<Iterable<? extends Component>> matching(final Matcher<? extends JComponent>... matchers) {
    return new IterableComponentsMatcher(matchers);
  }
}