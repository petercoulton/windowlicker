package com.objogate.wl.driver;

import javax.swing.AbstractButton;
import java.awt.Component;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.Query;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.internal.PropertyQuery;
import com.objogate.wl.internal.query.MnemonicQuery;
import com.objogate.wl.internal.query.TextQuery;


public class AbstractButtonDriver<T extends AbstractButton> extends ComponentDriver<T>
        implements TextQuery, MnemonicQuery {

    public AbstractButtonDriver(GesturePerformer gesturePerformer, ComponentSelector<T> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public AbstractButtonDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<T> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public AbstractButtonDriver(ComponentDriver<? extends Component> parentOrOwner, Class<T> componentType, Matcher<? super T>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public void click() {
        leftClickOnComponent();
    }

    public void hasText(Matcher<String> matcher) {
        has(text(), matcher);
    }

    private Query<T, String> text() {
        return new Query<T, String>() {
            public String query(T button) {
                return button.getText();
            }

            public void describeTo(Description description) {
                description.appendText("text");
            }
        };
    }

    public void mnemonic(Matcher<Character> matcher) {
        has(mnemonic(), matcher);
    }

    private static <B extends AbstractButton> Query<B, Character> mnemonic() {
        return new PropertyQuery<B, Character>("mnemonic") {
            public Character query(B button) {
                return (char) button.getMnemonic();
            }
        };
    }

    public void isChecked() {
        is(selected());
    }

    public void isNotChecked() {
        is(Matchers.not(selected()));
    }

    private TypeSafeMatcher<AbstractButton> selected() {
        return new TypeSafeMatcher<AbstractButton>() {
            @Override
            public boolean matchesSafely(AbstractButton item) {
                return item.isSelected();
            }

            public void describeTo(Description description) {
                description.appendText("is selected");
            }
        };
    }
}
