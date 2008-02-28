package com.objogate.wl.driver;

import javax.swing.JButton;
import java.awt.Component;
import org.hamcrest.Matcher;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.Prober;
import com.objogate.wl.ComponentSelector;

public class JButtonDriver extends AbstractButtonDriver<JButton> {
    public JButtonDriver(GesturePerformer gesturePerformer, JButton component) {
        super(gesturePerformer, component);
    }

    public JButtonDriver(GesturePerformer gesturePerformer, JButton component, Prober prober) {
        super(gesturePerformer, component, prober);
    }

    public JButtonDriver(GesturePerformer gesturePerformer, ComponentSelector<JButton> componentSelector) {
        super(gesturePerformer, componentSelector);
    }

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
