package com.objogate.wl.driver;

import javax.swing.JRadioButton;
import java.awt.Component;
import org.hamcrest.Matcher;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.GesturePerformer;

public class JRadioButtonDriver extends AbstractButtonDriver<JRadioButton> {
    public JRadioButtonDriver(GesturePerformer gesturePerformer, ComponentSelector<JRadioButton> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public JRadioButtonDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JRadioButton> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JRadioButtonDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JRadioButton> componentType, Matcher<? super JRadioButton>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }
}
