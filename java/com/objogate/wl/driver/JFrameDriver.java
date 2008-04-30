package com.objogate.wl.driver;

import javax.swing.JFrame;
import java.awt.Component;
import static org.hamcrest.CoreMatchers.allOf;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.equalTo;
import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.Query;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.probe.RecursiveComponentFinder;
import com.objogate.wl.probe.SingleComponentFinder;
import com.objogate.wl.probe.TopLevelWindowFinder;

public class JFrameDriver extends ComponentDriver<JFrame> {
    public JFrameDriver(GesturePerformer gesturePerformer, ComponentSelector<JFrame> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public JFrameDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JFrame> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JFrameDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JFrame> componentType, Matcher<? super JFrame>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public JFrameDriver(GesturePerformer gesturePerformer, Prober prober, Matcher<? super JFrame>... withMatchers) {
        this(gesturePerformer, topLevelFrame(withMatchers), prober);
    }

    public JFrameDriver(ComponentDriver<? extends Component> ownerDriver, Matcher<? super JFrame>... matchers) {
        super(ownerDriver,
                new SingleComponentFinder<JFrame>(
                        new RecursiveComponentFinder<JFrame>(JFrame.class, allOf(matchers),
                                ownerDriver.component())));
    }

    /**
     * Creates a ComponentSelector that will find a top-level JFrame that conforms
     * to the given matchers
     *
     * @param matchers The matchers to conform to
     * @return A ComponentSelector
     */
    public static ComponentSelector<JFrame> topLevelFrame(Matcher<? super JFrame>... matchers) {
        return new SingleComponentFinder<JFrame>(
                new RecursiveComponentFinder<JFrame>(JFrame.class, allOf(matchers),
                        new TopLevelWindowFinder()));
    }

    /**
     * Disposes of the frame and also sets the frame's name to <code>null</code> so that it cannot be found
     * by the {@link com.objogate.wl.driver.ComponentDriver#named(String)} named} matcher in a subsequent
     * test while it is being garbage collected.
     */
    public void dispose() {
        perform("disposing", new ComponentManipulation<JFrame>() {
            public void manipulate(JFrame component) {
                component.dispose();
                component.setName(null);
            }
        });
    }

    public void hasTitle(String title) {
        hasTitle(equalTo(title));
    }

    public void hasTitle(Matcher<String> matcher) {
        has(title(), matcher);
    }

    private static Query<JFrame, String> title() {
        return new Query<JFrame, String>() {
            public String query(JFrame label) {
                return label.getTitle();
            }

            public void describeTo(Description description) {
                description.appendText("frame title");
            }
        };
    }

    public JMenuBarDriver menuBar() {
        return new JMenuBarDriver(this);
    }
}
