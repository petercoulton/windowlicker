package com.objogate.wl.driver;

import javax.swing.JFrame;
import java.awt.Component;
import static org.hamcrest.CoreMatchers.allOf;
import org.hamcrest.Matcher;
import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.probe.RecursiveComponentFinder;
import com.objogate.wl.probe.SingleComponentFinder;
import com.objogate.wl.probe.TopLevelWindowFinder;

public class JFrameDriver extends ComponentDriver<JFrame> {

    public JFrameDriver(GesturePerformer gesturePerformer, JFrame component) {
        super(gesturePerformer, component);
    }

    public JFrameDriver(GesturePerformer gesturePerformer, JFrame component, Prober prober) {
        super(gesturePerformer, component, prober);
    }

    public JFrameDriver(GesturePerformer gesturePerformer, ComponentSelector<JFrame> componentSelector) {
        super(gesturePerformer, componentSelector);
    }

    public JFrameDriver(GesturePerformer gesturePerformer, ComponentSelector<JFrame> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public JFrameDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JFrame> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JFrameDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JFrame> componentType, Matcher<? super JFrame>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public JFrameDriver(GesturePerformer gesturePerformer, Matcher<? super JFrame>... withMatchers) {
        this(gesturePerformer, topLevelFrame(withMatchers));
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
     * by the {@link com.objogate.wl.matcher.ComponentMatchers#named(String)} named} matcher in a subsequent
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
}
