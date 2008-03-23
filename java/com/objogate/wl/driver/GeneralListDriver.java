package com.objogate.wl.driver;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import java.awt.Component;
import org.hamcrest.Matcher;
import com.objogate.exception.Defect;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.GesturePerformer;

/**
 * Simple wrapper for list operations on either JComboBox or JList.
 * <p/>
 * Some UI components (notably JOptionPane) dynamically choose whether to use a JList or
 * JComboBox at runtime.
 * <p/>
 * This is a wrapper so simple operations on these components can be generalised.
 */
public class GeneralListDriver extends ComponentDriver<JComponent> implements ListDriver {
    public GeneralListDriver(GesturePerformer gesturePerformer, JComponent component) {
        super(gesturePerformer, component);
    }

    public GeneralListDriver(GesturePerformer gesturePerformer, JComponent component, Prober prober) {
        super(gesturePerformer, component, prober);
    }

    public GeneralListDriver(GesturePerformer gesturePerformer, ComponentSelector<JComponent> componentSelector) {
        super(gesturePerformer, componentSelector);
    }

    public GeneralListDriver(GesturePerformer gesturePerformer, ComponentSelector<JComponent> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public GeneralListDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JComponent> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public GeneralListDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JComponent> componentType, Matcher<? super JComponent>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public void selectItem(int index) {
        getDriver().selectItem(index);
    }

    public void selectItem(Object item) {
        getDriver().selectItem(item);
    }

    public void selectItem(Matcher<? extends Component> item) {
        getDriver().selectItem(item);
    }

    public void hasSelectedIndex(int index) {
        getDriver().hasSelectedIndex(index);
    }

    public ListDriver getDriver() {
        isShowingOnScreen();
        JComponent component = component().component();
        if (component instanceof JList) {
            return new JListDriver(this, JList.class);
        } else if (component instanceof JComboBox) {
            return new JComboBoxDriver(this, JComboBox.class);
        } else {
            throw new Defect("Only support JList & JComboBox, not " + component.getClass().getName());
        }
    }
}
