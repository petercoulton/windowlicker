package com.objogate.wl.swing.driver;

import javax.swing.JCheckBox;
import java.awt.Component;
import org.hamcrest.Matcher;
import com.objogate.wl.Prober;
import com.objogate.wl.swing.ComponentSelector;
import com.objogate.wl.swing.gesture.GesturePerformer;

public class JCheckBoxDriver extends AbstractButtonDriver<JCheckBox> {
    public JCheckBoxDriver(GesturePerformer gesturePerformer, ComponentSelector<JCheckBox> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public JCheckBoxDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JCheckBox> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JCheckBoxDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JCheckBox> componentType, Matcher<? super JCheckBox>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }
}
