package com.objogate.wl.driver;

import javax.swing.JProgressBar;
import java.awt.Component;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.GesturePerformer;

public class JProgressBarDriver extends ComponentDriver<JProgressBar> {
    public JProgressBarDriver(GesturePerformer gesturePerformer, JProgressBar component) {
        super(gesturePerformer, component);
    }

    public JProgressBarDriver(GesturePerformer gesturePerformer, JProgressBar component, Prober prober) {
        super(gesturePerformer, component, prober);
    }

    public JProgressBarDriver(GesturePerformer gesturePerformer, ComponentSelector<JProgressBar> jProgressBarComponentSelector) {
        super(gesturePerformer, jProgressBarComponentSelector);
    }

    public JProgressBarDriver(GesturePerformer gesturePerformer, ComponentSelector<JProgressBar> jProgressBarComponentSelector, Prober prober) {
        super(gesturePerformer, jProgressBarComponentSelector, prober);
    }

    public JProgressBarDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JProgressBar> jProgressBarComponentSelector) {
        super(parentOrOwner, jProgressBarComponentSelector);
    }

    public JProgressBarDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JProgressBar> componentType, Matcher<? super JProgressBar>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public void hasMinimum(final Matcher<Integer> matcher) {
        is(new TypeSafeMatcher<JProgressBar>() {
            @Override
            public boolean matchesSafely(JProgressBar jProgressBar) {
                return matcher.matches(jProgressBar.getMinimum());
            }

            public void describeTo(Description description) {
                description.appendText("minimumvalue matches");
                description.appendDescriptionOf(matcher);
            }
        });
    }

    public void hasMaximum(final Matcher<Integer> matcher) {
        is(new TypeSafeMatcher<JProgressBar>() {
            @Override
            public boolean matchesSafely(JProgressBar jProgressBar) {
                return matcher.matches(jProgressBar.getMaximum());
            }

            public void describeTo(Description description) {
                description.appendText("maximumvalue matches");
                description.appendDescriptionOf(matcher);
            }
        });
    }

    public void hasValue(final Matcher<Integer> matcher) {
        is(new TypeSafeMatcher<JProgressBar>() {
            @Override
            public boolean matchesSafely(JProgressBar jProgressBar) {
                return matcher.matches(jProgressBar.getValue());
            }

            public void describeTo(Description description) {
                description.appendText("value matches");
                description.appendDescriptionOf(matcher);
            }
        });
    }

    public void hasString(final Matcher<String> matcher) {
        is(new TypeSafeMatcher<JProgressBar>() {
            @Override
            public boolean matchesSafely(JProgressBar jProgressBar) {
                return matcher.matches(jProgressBar.getString());
            }

            public void describeTo(Description description) {
                description.appendText("string matches");
                description.appendDescriptionOf(matcher);
            }
        });
    }

    public void hasPercentComplete(final Matcher<Double> matcher) {
        is(new TypeSafeMatcher<JProgressBar>() {
            @Override
            public boolean matchesSafely(JProgressBar jProgressBar) {
                return matcher.matches(jProgressBar.getPercentComplete());
            }

            public void describeTo(Description description) {
                description.appendText("percentage matches");
                description.appendDescriptionOf(matcher);
            }
        });
    }


}
