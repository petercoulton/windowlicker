package com.objogate.wl.driver;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.Component;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsEqual;
import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.ComponentQuery;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.probe.RecursiveComponentFinder;
import com.objogate.wl.probe.SingleComponentFinder;
import com.objogate.wl.probe.TopLevelWindowFinder;

public class JComboBoxDriver extends ComponentDriver<JComboBox> implements ListDriver {

    public JComboBoxDriver(GesturePerformer gesturePerformer, JComboBox component) {
        super(gesturePerformer, component);
    }

    public JComboBoxDriver(GesturePerformer gesturePerformer, JComboBox component, Prober prober) {
        super(gesturePerformer, component, prober);
    }

    public JComboBoxDriver(GesturePerformer gesturePerformer, ComponentSelector<JComboBox> componentSelector) {
        super(gesturePerformer, componentSelector);
    }

    public JComboBoxDriver(GesturePerformer gesturePerformer, ComponentSelector<JComboBox> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public JComboBoxDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JComboBox> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JComboBoxDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JComboBox> componentType, Matcher<? super JComboBox>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public void selectItem(int index) {
        clickToOpenPopup();

        ComboPopupComponentDriver comboPopupDriver = new ComboPopupComponentDriver(gesturePerformer);

        comboPopupDriver.selectItem(index);
    }

    public void selectItem(Object item) {
        clickToOpenPopup();

        ComboPopupComponentDriver comboPopupDriver = new ComboPopupComponentDriver(gesturePerformer);

        comboPopupDriver.selectItem(item);
    }

    public void selectItem(Matcher<Component>  item) {
        clickToOpenPopup();

        ComboPopupComponentDriver comboPopupDriver = new ComboPopupComponentDriver(gesturePerformer);

        comboPopupDriver.selectItem(item);
    }

    private void clickToOpenPopup() {
        leftClickOnComponent();
    }

    public void hasSelectedIndex(int index) {
        has("selected index", new ComponentQuery<JComboBox, Integer>() {
            public Integer query(JComboBox component) {
                return component.getSelectedIndex();
            }
        }, new IsEqual<Integer>(index));
    }

    private static class ComboPopupComponentDriver extends ComponentDriver<JComponent> {
        public ComboPopupComponentDriver(GesturePerformer gesturePerformer) {
            super(gesturePerformer, new SingleComponentFinder<JComponent>(new RecursiveComponentFinder<JComponent>(JComponent.class, thatImplementsComboPopup(), new TopLevelWindowFinder())));
        }

        private static TypeSafeMatcher<JComponent> thatImplementsComboPopup() {
            return new TypeSafeMatcher<JComponent>() {
                @Override
                public boolean matchesSafely(JComponent component) {
                    return component instanceof ComboPopup;
                }

                public void describeTo(Description description) {
                    description.appendText("a component that implements ComboPopup");
                }
            };
        }
        
        public void selectItem(final int index) {
            JListFinderManipulation listFinderManipulation = new JListFinderManipulation();
            perform("selecting index in combo popup", listFinderManipulation);
            JListDriver listDriver = new JListDriver(gesturePerformer, listFinderManipulation.list);
            listDriver.selectItem(index);
        }

        public void selectItem(Object item) {
            JListFinderManipulation listFinderManipulation = new JListFinderManipulation();
            perform("selecting item in combo popup", listFinderManipulation);
            JListDriver listDriver = new JListDriver(gesturePerformer, listFinderManipulation.list);
            listDriver.selectItem(item);
        }

        public void selectItem(Matcher<Component>  matcher) {
            JListFinderManipulation listFinderManipulation = new JListFinderManipulation();
            perform("selecting item in combo popup", listFinderManipulation);
            JListDriver listDriver = new JListDriver(gesturePerformer, listFinderManipulation.list);
            listDriver.selectItem(matcher);
        }

        private class JListFinderManipulation implements ComponentManipulation<JComponent> {
            JList list;

            public void manipulate(JComponent component) {
                ComboPopup popup = (ComboPopup) component;
                list = popup.getList();
            }
        }
    }

}
