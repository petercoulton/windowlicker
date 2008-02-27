package com.objogate.wl.driver;

import javax.swing.AbstractButton;
import java.awt.Component;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.matchers.TypeSafeMatcher;
import com.objogate.wl.ComponentQuery;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.internal.query.MnemonicQuery;
import com.objogate.wl.internal.query.TextQuery;


public class AbstractButtonDriver<T extends AbstractButton> extends ComponentDriver<T>
        implements TextQuery, MnemonicQuery {

    public AbstractButtonDriver(GesturePerformer gesturePerformer, T component) {
        super(gesturePerformer, component);
    }

    public AbstractButtonDriver(GesturePerformer gesturePerformer, T component, Prober prober) {
        super(gesturePerformer, component, prober);
    }

    public AbstractButtonDriver(GesturePerformer gesturePerformer, ComponentSelector<T> componentSelector) {
        super(gesturePerformer, componentSelector);
    }

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
    
    public void text(Matcher<String> textMatcher) {
        has("text", text(), textMatcher);
    }
    
    private ComponentQuery<T,String> text() {
        return new ComponentQuery<T,String>(){
            public String query(T button) {
                return button.getText();
            }
        };
    }

    public void mnemonic(Matcher<Character> matcher) {
        has("mnemonic", mnemonic(), matcher);
    }

    private ComponentQuery<T,Character> mnemonic() {
        return new ComponentQuery<T,Character>(){
            public Character query(T button) {
                return (char) button.getMnemonic();
            }
        };
    }

    public void isChecked() {
        is("selecting", selected());
    }

    public void isNotChecked() {
        is("selecting", Matchers.not(selected()));
    }

    private TypeSafeMatcher<AbstractButton> selected() {
        return new TypeSafeMatcher<AbstractButton>() {
            public boolean matchesSafely(AbstractButton item) {
                return item.isSelected();
            }

            public void describeTo(Description description) {
                description.appendText("selected");
            }
        };
    }

}
