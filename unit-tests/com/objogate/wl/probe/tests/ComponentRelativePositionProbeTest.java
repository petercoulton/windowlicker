package com.objogate.wl.probe.tests;

import static com.objogate.wl.probe.ComponentRelativePositionProbe.RelativePosition.ABOVE;
import static com.objogate.wl.probe.ComponentRelativePositionProbe.RelativePosition.BELOW;
import static com.objogate.wl.probe.ComponentRelativePositionProbe.RelativePosition.LEFTOF;
import static com.objogate.wl.probe.ComponentRelativePositionProbe.RelativePosition.RIGHTOF;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.AWTEventQueueProber;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Probe;
import com.objogate.wl.driver.ComponentDriver;
import com.objogate.wl.driver.JFrameDriver;
import com.objogate.wl.driver.tests.ComponentViewer;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.probe.ComponentRelativePositionProbe;
import com.objogate.wl.probe.RecursiveComponentFinder;
import com.objogate.wl.probe.SingleComponentFinder;
import com.objogate.wl.probe.TopLevelWindowFinder;

@SuppressWarnings("unchecked")
public class ComponentRelativePositionProbeTest {
    private JPanel panel = new JPanel();
    private JButton buttonOne = new JButton("one") {{ setName("one");}};
    private JButton buttonTwo = new JButton("two") {{ setName("two");}};
    private TopLevelWindowFinder top = new TopLevelWindowFinder();

    private String name = this.getClass().getSimpleName();
    private GesturePerformer gesturePerformer;
    private JFrameDriver frameDriver = new JFrameDriver(gesturePerformer, ComponentDriver.named(name));

    private SingleComponentFinder<JFrame> frameFinder = new SingleComponentFinder<JFrame>(
            new RecursiveComponentFinder<JFrame>(JFrame.class, ComponentDriver.named(name), top)
    );

    private ComponentSelector<JButton> buttonOneSelector = new SingleComponentFinder<JButton>(
            new RecursiveComponentFinder<JButton>(JButton.class, ComponentDriver.named("one"), frameFinder));
    private ComponentSelector<JButton> buttonTwoSelector = new SingleComponentFinder<JButton>(
            new RecursiveComponentFinder<JButton>(JButton.class, ComponentDriver.named("two"), frameFinder));


    @Before public void
    setUp() {
        panel.add(buttonOne);
        panel.add(buttonTwo);

        JFrame frame = ComponentViewer.view(panel);
        frame.setName(name);

        top.probe();

        gesturePerformer = new GesturePerformer();
    }

    @After
    public void
    tearDown() {
        frameDriver.dispose();
    }

    @Test public void
    oneLeftOfTwo() {
        ComponentRelativePositionProbe probe =
                new ComponentRelativePositionProbe(buttonOneSelector, LEFTOF, buttonTwoSelector);
        check(probe);
    }

    @Test public void
    twoRightOfOne() {
        ComponentRelativePositionProbe probe =
                new ComponentRelativePositionProbe(buttonOneSelector, RIGHTOF, buttonTwoSelector);
        check(probe);
    }

    @Test public void
    oneAboveTwo() {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.doLayout();
        ComponentRelativePositionProbe probe =
                new ComponentRelativePositionProbe(buttonOneSelector, ABOVE, buttonTwoSelector);
        check(probe);
    }

    @Test public void
    twoBelowOne() {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.doLayout();
        ComponentRelativePositionProbe probe =
                new ComponentRelativePositionProbe(buttonTwoSelector, BELOW, buttonOneSelector);
        check(probe);
    }

    private void check(Probe probe) {
        AWTEventQueueProber prober = new AWTEventQueueProber();

        if ( ! prober.poll(probe) ) {
            Description description = new StringDescription();
            probe.describeTo(description);
            Assert.fail(description.toString());
        }
    }

}
