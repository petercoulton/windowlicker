package com.objogate.wl.driver;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import static org.hamcrest.CoreMatchers.allOf;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import com.objogate.wl.*;
import com.objogate.wl.gesture.*;
import static com.objogate.wl.gesture.Gestures.*;
import static com.objogate.wl.matcher.ComponentMatchers.showingOnScreen;
import com.objogate.wl.probe.*;

public abstract class ComponentDriver<T extends Component> {
    private final Prober prober;
    private final ComponentSelector<T> selector;

    protected final GesturePerformer gesturePerformer;

    /**
     * Used to unit-test a component
     *
     * @param gesturePerformer
     * @param component The component to test.
     */
    public ComponentDriver(GesturePerformer gesturePerformer, T component) {
        this(gesturePerformer, new ComponentIdentity<T>(component));
    }

    /**
     * Used to unit-test a component with timeouts specified by the given prober.
     *
     * @param gesturePerformer
     * @param component The component to test.
     * @param prober    The prober used to probe the component under test with specific timeouts
     */
    public ComponentDriver(GesturePerformer gesturePerformer, T component, Prober prober) {
        this(gesturePerformer, new ComponentIdentity<T>(component), prober);
    }

    public ComponentDriver(GesturePerformer gesturePerformer, ComponentSelector<T> selector) {
        this(gesturePerformer, selector, new AWTEventQueueProber());
    }

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
     * @param description The description of what is being asserted by the criteria
     * @param probe       The probe to be run and checked.
     */
    public void check(String description, Probe probe) {
        prober.check(description, probe);
    }

    /**
     * Returns a selector that identifies the component driven by this driver.  This can be used to make assertions
     * about the component or search for subcomponents.
     *
     * @return a selector that identifies the component driven by this driver.
     * @see #check(String,com.objogate.wl.Probe)
     * @see #all(Class,org.hamcrest.Matcher)
     * @see #the(Class,org.hamcrest.Matcher)
     * @see #is(String,org.hamcrest.Matcher)
     * @see #has(String,com.objogate.wl.ComponentQuery,org.hamcrest.Matcher)
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
     * @param description The description of what is being asserted by the criteria
     * @param criteria    The criteria that the component must meet.
     */
    public void is(String description, Matcher<? super T> criteria) {
        check(description, new ComponentAssertionProbe<T>(selector, criteria));
    }

    /**
     * Make an assertion about the value of a component property of the component.
     * The property value must meet all the given criteria.
     * <p/>
     * This is a simpler hook for assertions, upon which more convenient methods can be
     * built.  It is exposed as a public method to act as an "escape route" so that
     * users can extend drivers through a stable extension point.
     *
     * @param description The description of what is being asserted by the criteria
     * @param query       A query that returns the value of a property of the component
     * @param criteria    The criteria that the component must meet.
     */
    public <P> void has(String description, ComponentQuery<T, P> query, Matcher<? super P> criteria) {
        check(description, new ComponentPropertyAssertionProbe<T, P>(selector, query, criteria));
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
        check(description, new ComponentManipulatorProbe<T>(selector, manipulation));
    }

    public void performGesture(Gesture... gestures) {
        gesturePerformer.perform(gestures);
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
        is("can only click on visible components", showingOnScreen());
    }

    /**
     * Move to centre of component.
     */
    public void moveMouseToCenter() {
        performGesture(moveMouseTo(centerOfComponent()));
    }

    /**
     * @Deprecated should be replaced by some kind of Tracker and the moveMouseTo Gesture.
     */
    public void moveMouseToOffset(int offsetX, int offsetY) {
        performGesture(moveMouseTo(new ComponentOffsetTracker(prober, selector, offsetX, offsetY)));
    }

    /**
     * Dont use this unless you have to - should try to make everything asynchronous
     */
    public void waitUntilGesturesCompleted() {
        // no-op
    }

    /**
     * @Deprecated use a Tracker instead
     */
    protected Rectangle screenBounds() {
        isShowingOnScreen();

        ComponentLayoutManipulation<T> layout = new ComponentLayoutManipulation<T>();
        perform("ensure component is layed out", layout);
        ComponentBoundsManipulation<T> manipulation = new ComponentBoundsManipulation<T>();
        perform("bounds of component on screen", manipulation);
        return manipulation.getBounds();
    }

    //TODO: make this work with gestures
    protected int multiSelectKey() {
        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        if (lookAndFeel.getClass().getName().equals("apple.laf.AquaLookAndFeel")) {
            return KeyEvent.VK_META;
        } else {
            return KeyEvent.VK_CONTROL;
        }
    }

    protected void cut() {
        performGesture(Gestures.cut());
    }
    
    protected void copy() {
        performGesture(Gestures.copy());
    }
    
    protected void paste() {
        performGesture(Gestures.paste());
    }

    protected void selectAll() {
        performGesture(Gestures.selectAll());
    }

    public void hasForegroundColor(Color color) {
        hasForegroundColor(Matchers.equalTo(color));
    }

    public void hasForegroundColor(Matcher<Color> color) {
        has("foreground color", new ComponentQuery<T, Color>() {
            public Color query(T component) {
                return component.getForeground();
            }
        }, color);
    }

    public void hasBackgroundColor(Color color) {
        hasBackgroundColor(Matchers.equalTo(color));
    }

    // todo (nick): what a nice way of combining these to produce decent error messages?
    public void hasBackgroundColor(Matcher<Color> color) {
        has("background color", new ComponentQuery<T, Color>() {
            public Color query(T component) {
                return component.getBackground();
            }
        }, color);

        //need to check opacity else the background color isn't visible
        has("opacity", new ComponentQuery<T, Boolean>() {
            public Boolean query(T component) {
                return component.isOpaque();
            }
        }, Matchers.is(true));
    }

    /**
     * @Deprecated use a Tracker instead
     */
    private class ComponentBoundsManipulation<T extends Component> implements ComponentManipulation<T> {
        Rectangle bounds;

        public void manipulate(T component) {
            bounds = new Rectangle(component.getLocationOnScreen(), component.getSize());
        }

        public Rectangle getBounds() {
            return bounds;
        }
    }

    private class ComponentLayoutManipulation<T extends Component> implements ComponentManipulation<T> {
        public void manipulate(T component) {
            component.getParent().doLayout();
        }
    }
}
