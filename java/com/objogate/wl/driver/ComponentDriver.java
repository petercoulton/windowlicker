package com.objogate.wl.driver;

import static com.objogate.wl.gesture.Gestures.BUTTON1;
import static com.objogate.wl.gesture.Gestures.clickMouseButton;
import static com.objogate.wl.gesture.Gestures.moveMouseTo;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.equalTo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.objogate.wl.Gesture;
import com.objogate.wl.Probe;
import com.objogate.wl.Prober;
import com.objogate.wl.Query;
import com.objogate.wl.gesture.ComponentCenterTracker;
import com.objogate.wl.gesture.ComponentOffsetTracker;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.gesture.Gestures;
import com.objogate.wl.gesture.Tracker;
import com.objogate.wl.internal.PropertyQuery;
import com.objogate.wl.matcher.ComponentEnabledMatcher;
import com.objogate.wl.matcher.ComponentOpaqueMatcher;
import com.objogate.wl.matcher.DisplayableComponentMatcher;
import com.objogate.wl.matcher.ShowingOnScreenMatcher;
import com.objogate.wl.probe.ComponentAssertionProbe;
import com.objogate.wl.probe.ComponentFinders;
import com.objogate.wl.probe.ComponentManipulatorProbe;
import com.objogate.wl.probe.ComponentPropertyAssertionProbe;
import com.objogate.wl.probe.RecursiveComponentFinder;
import com.objogate.wl.probe.SingleComponentFinder;
import com.objogate.wl.swing.ComponentFinder;
import com.objogate.wl.swing.ComponentManipulation;
import com.objogate.wl.swing.ComponentSelector;

public abstract class ComponentDriver<T extends Component> {
    private final Prober prober;
    private final ComponentSelector<T> selector;
    
    //TODO: (nat) make this private
    protected final GesturePerformer gesturePerformer;
    
    public ComponentDriver(GesturePerformer gesturePerformer, ComponentSelector<T> selector, Prober prober) {
        this.selector = selector;
        this.prober = prober;
        this.gesturePerformer = gesturePerformer;
    }

    public ComponentDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<T> selector) {
        this.selector = selector;
        this.prober = parentOrOwner.prober;
        this.gesturePerformer = parentOrOwner.gesturePerformer;
    }

    public ComponentDriver(ComponentDriver<? extends Component> parentOrOwner, Class<T> componentType, Matcher<? super T>... matchers) {
        this(parentOrOwner, new SingleComponentFinder<T>(new RecursiveComponentFinder<T>(componentType, allOf(matchers), parentOrOwner.selector)));
    }

    /**
     * Check a probe.
     * <p/>
     * This is the fundamental hook for assertions and manipulations, upon which more convenient methods are
     * built.  It is exposed as a public method to act as an "escape route" so that
     * users can extend drivers through a stable extension point.
     *
     * @param probe The probe to be run and checked.
     */
    public void check(Probe probe) {
        prober.check(probe);
    }

    /**
     * Returns a selector that identifies the component driven by this driver.  This can be used to make assertions
     * about the component or search for subcomponents.
     *
     * @return a selector that identifies the component driven by this driver.
     * @see #check(com.objogate.wl.Probe)
     * @see #all(Class,org.hamcrest.Matcher)
     * @see #the(Class,org.hamcrest.Matcher)
     * @see #is(org.hamcrest.Matcher)
     * @see #has(com.objogate.wl.Query,org.hamcrest.Matcher)
     */
    public ComponentSelector<T> component() {
        return selector;
    }

    /**
     * Returns a selector that identifies all subcomponents of the component driven by this driver that match
     * the given criteria.
     *
     * @param type     The type of subcomponents to finde
     * @param criteria The criteria by which to select subcomponents
     * @return a selector that identifies the component driven by this driver.
     */
    public <U extends Component> ComponentFinder<U> all(Class<U> type, Matcher<? super U> criteria) {
        return ComponentFinders.all(type, criteria, selector);
    }

    /**
     * Returns a selector that identifies a single subcomponent of the component driven by this driver that matches
     * the given criteria.
     *
     * @param type     The type of subcomponents to finde
     * @param criteria The criteria by which to select subcomponents
     * @return a selector that identifies the component driven by this driver.
     */
    public <U extends Component> ComponentSelector<U> the(Class<U> type, Matcher<? super U> criteria) {
        return ComponentFinders.the(type, criteria, selector);
    }

    /**
     * Make an assertion about the component's state.  The component must meet all the
     * given criteria.
     * <p/>
     * This is a simpler hook for assertions, upon which more convenient methods can be
     * built.  It is exposed as a public method to act as an "escape route" so that
     * users can extend drivers through a stable extension point.
     *
     * @param criteria The criteria that the component must meet.
     */
    public void is(Matcher<? super T> criteria) {
        check(new ComponentAssertionProbe<T>(selector, criteria));
    }

    /**
     * Make an assertion about the value of a component property of the component.
     * The property value must meet all the given criteria.
     * <p/>
     * This is a simpler hook for assertions, upon which more convenient methods can be
     * built.  It is exposed as a public method to act as an "escape route" so that
     * users can extend drivers through a stable extension point.
     *
     * @param query    A query that returns the value of a property of the component
     * @param criteria The criteria that the property value must meet.
     */
    public <P> void has(Query<? super T, P> query, Matcher<? super P> criteria) {
        check(new ComponentPropertyAssertionProbe<T, P>(selector, query, criteria));
    }

    /**
     * Manipulate the component through it's API, not through the input devices.  The manipulation
     * is performed on the AWT event dispatch thread.
     * <p/>
     * This is the fundamental hook for calling the component, upon which more convenient methods are
     * built.  It is exposed as a public method to act as an "escape route" so that
     * users can extend drivers through a stable extension point.
     *
     * @param description  A description of the manipulation, reported if the component selector fails.
     * @param manipulation The manipulation to perform
     */
    public void perform(String description, ComponentManipulation<? super T> manipulation) {
        check(new ComponentManipulatorProbe<T>(selector, manipulation));
    }

    public void performGesture(Gesture... gestures) {
        gesturePerformer.perform(gestures);
    }

    protected Tracker offset(int offsetX, int offsetY) {
        return new ComponentOffsetTracker(prober, selector, offsetX, offsetY);
    }

    protected Tracker centerOfComponent() {
        return new ComponentCenterTracker(prober, selector);
    }

    protected Prober prober() {
        return prober;
    }

    public void leftClickOnComponent() {
        isShowingOnScreen();
        performGesture(moveMouseTo(centerOfComponent()), clickMouseButton(BUTTON1));
    }

    protected void isShowingOnScreen() {
        is(ComponentDriver.showingOnScreen());
    }

    /**
     * Move to centre of component.
     */
    public void moveMouseToCenter() {
        performGesture(moveMouseTo(centerOfComponent()));
    }

    public void moveMouseToOffset(int offsetX, int offsetY) {
        performGesture(moveMouseTo(offset(offsetX, offsetY)));
    }

    /**
     * @deprecated use a Tracker instead
     */
    protected Rectangle screenBounds() {
        isShowingOnScreen();

        ComponentLayoutManipulation<T> layout = new ComponentLayoutManipulation<T>();
        perform("ensure component is layed out", layout);
        ComponentBoundsManipulation<T> manipulation = new ComponentBoundsManipulation<T>();
        perform("bounds of component on screen", manipulation);
        return manipulation.getBounds();
    }


    public void cut() {
        performGesture(Gestures.cut());
    }

    public void copy() {
        performGesture(Gestures.copy());
    }

    public void paste() {
        performGesture(Gestures.paste());
    }

    public void selectAll() {
        performGesture(Gestures.selectAll());
    }

    public void hasForegroundColor(Color color) {
        hasForegroundColor(equalTo(color));
    }

    public void hasForegroundColor(Matcher<Color> color) {
        has(foregroundColor(), color);
    }

    public void hasBackgroundColor(Color color) {
        hasBackgroundColor(equalTo(color));
    }

    //TODO (nick): what is a nice way of combining these to produce decent error messages?
    public void hasBackgroundColor(Matcher<Color> color) {
        has(backgroundColor(), color);

        //need to check opacity else the background color isn't visible
        // -- nat: not necessarily true, depends on component type
        is(opaque());
    }

    public static <T extends Component, P> Matcher<T> with(Query<? super T, P> propertyQuery,
                                                           Matcher<? super P> valueMatcher) {
        return new QueryResultMatcher<T, P>(propertyQuery, valueMatcher);
    }

    public static class QueryResultMatcher<C, P> extends BaseMatcher<C> {
        private final Query<? super C, P> query;
        private final Matcher<? super P> resultMatcher;

        public QueryResultMatcher(Query<? super C, P> query, Matcher<? super P> resultMatcher) {
            this.query = query;
            this.resultMatcher = resultMatcher;
        }

        //TODO: any way to check the dynamic type of item?
        @SuppressWarnings("unchecked")
        public boolean matches(Object item) {
            return item != null && resultMatcher.matches(query.query((C) item));
        }

        public void describeTo(Description description) {
            description
                    .appendText("with ")
                    .appendDescriptionOf(query)
                    .appendText(" ")
                    .appendDescriptionOf(resultMatcher);

        }
    }

    public static Query<Component, String> name() {
        return new PropertyQuery<Component, String>("name") {
            public String query(Component component) {
                return component.getName();
            }
        };
    }

    public static Query<Component, Color> backgroundColor() {
        return new PropertyQuery<Component, Color>("background color") {
            public Color query(Component component) {
                return component.getBackground();
            }
        };
    }

    public static Matcher<Component> named(String nameValue) {
        return with(name(), equalTo(nameValue));
    }

    public static Matcher<Component> showingOnScreen() {
        return new ShowingOnScreenMatcher();
    }

    public static Matcher<Component> displayable() {
        return new DisplayableComponentMatcher();
    }

    public static Matcher<Component> opaque() {
        return new ComponentOpaqueMatcher();
    }

    public static Matcher<Component> enabled() {
        return new ComponentEnabledMatcher();
    }

    public static Query<Component, Color> foregroundColor() {
        return new PropertyQuery<Component, Color>("foreground color") {
            public Color query(Component component) {
                return component.getForeground();
            }
        };
    }

    /**
     * @Deprecated use a Tracker instead
     */
    @SuppressWarnings("hiding")
    private class ComponentBoundsManipulation<T extends Component> implements ComponentManipulation<T> {
        Rectangle bounds;

        public void manipulate(T component) {
            bounds = new Rectangle(component.getLocationOnScreen(), component.getSize());
        }

        public Rectangle getBounds() {
            return bounds;
        }
    }

    @SuppressWarnings("hiding")
    private class ComponentLayoutManipulation<T extends Component> implements ComponentManipulation<T> {
        public void manipulate(T component) {
            component.getParent().doLayout();
        }
    }
}
