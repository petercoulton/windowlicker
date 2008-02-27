package com.objogate.wl.driver;

import javax.swing.JLabel;
import java.awt.Component;
import org.hamcrest.Matcher;
import com.objogate.wl.ComponentQuery;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.internal.query.TextQuery;

public class JLabelDriver extends ComponentDriver<JLabel> implements TextQuery {
    public JLabelDriver(GesturePerformer gesturePerformer, JLabel component, Prober prober) {
        super(gesturePerformer, component, prober);
    }

    public JLabelDriver(ComponentDriver<? extends Component> containerDriver, Matcher<? super JLabel>... matchers) {
        super(containerDriver, JLabel.class, matchers);
    }

    public JLabelDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JLabel> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public void text(Matcher<String> textMatcher) {
        has("text", text(), textMatcher);
    }

    private static ComponentQuery<JLabel, String> text() {
        return new ComponentQuery<JLabel, String>() {
            public String query(JLabel label) {
                return label.getText();
            }
        };
    }

}
