package com.objogate.wl.swing.driver;

import javax.swing.JButton;
import java.awt.Component;
import org.hamcrest.Matcher;
import com.objogate.wl.Prober;
import com.objogate.wl.swing.ComponentSelector;
import com.objogate.wl.swing.gesture.GesturePerformer;

public class JButtonDriver extends AbstractButtonDriver<JButton> {
    public JButtonDriver(GesturePerformer gesturePerformer, ComponentSelector<JButton> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public JButtonDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JButton> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JButtonDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JButton> componentType, Matcher<? super JButton>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }
}
