package com.objogate.wl.driver.tests;

import static com.objogate.wl.driver.JFrameDriver.topLevelFrame;
import static java.lang.Thread.sleep;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JFrame;

import org.junit.After;

import com.objogate.wl.AWTEventQueueProber;
import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.driver.ComponentDriver;
import com.objogate.wl.driver.JFrameDriver;
import com.objogate.wl.gesture.GesturePerformer;

public abstract class AbstractComponentDriverTest<T extends ComponentDriver<? extends Component>> {
    public static final String WINDOWLICKER_END_OF_TEST_PAUSE = "windowlicker.end-of-test-pause";
    
    protected final GesturePerformer gesturePerformer = new GesturePerformer();
    protected final AWTEventQueueProber prober = new AWTEventQueueProber();
    protected int endOfTestPause = Integer.valueOf(System.getProperty(AbstractComponentDriverTest.WINDOWLICKER_END_OF_TEST_PAUSE, "0"));
    protected JFrame frame;
    protected JFrameDriver frameDriver;
    protected T driver;
    
    @After
    public void disposeOfFrame() throws Exception {
        pause(endOfTestPause); // just to give the view a chance to see the result of the test
        if (frame != null) {
            frame.setName(null);
            frame.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    protected void view(Component c) {
        frame = ComponentViewer.view(c);
        frame.setTitle(getClass().getSimpleName());
        frame.setName("componentViewer");
        frame.pack();
        
        frameDriver = new JFrameDriver(gesturePerformer, 
                                       topLevelFrame(ComponentDriver.named("componentViewer")),
                                       prober);
        
        try {
            pause(500);
        } catch (InterruptedException e) {
            //
        }
    }

    protected void pause() throws InterruptedException {
        pause(10000);
    }

    protected void pause(long i) throws InterruptedException {
        sleep(i);
    }

    protected void setColors(final Color foreground, final Color background) {
        driver.perform("setting colors", new ComponentManipulation<Component>() {
            public void manipulate(Component component) {
                component.setForeground(foreground);
                component.setBackground(background);
            }
        });
    }

    protected void pack() {
        frameDriver.perform("packing", new ComponentManipulation<JFrame>() {
            public void manipulate(JFrame component) {
                component.pack();
            }
        });
    }
}
