package com.objogate.wl.swing.driver.tests;

import static com.objogate.wl.swing.driver.JFrameDriver.topLevelFrame;

import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.junit.After;

import com.objogate.exception.Defect;
import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.ComponentManipulation;
import com.objogate.wl.swing.UI;
import com.objogate.wl.swing.driver.ComponentDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

public abstract class AbstractComponentDriverTest<T extends ComponentDriver<? extends Component>> {

    static {
        try {
            String lnfName = System.getProperty("swing.defaultlaf", UIManager.getSystemLookAndFeelClassName());
            setLookAndFeel(lnfName);
        } catch (Exception e) {
            throw new Defect("Does this happen?", e);
        }
    }

    protected void setLookAndFeel(UI ui) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        setLookAndFeel(ui, frame);
    }

    protected void setLookAndFeel(UI ui, final Component c) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        setLookAndFeel(ui.classOfLookAnfFeel);
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    SwingUtilities.updateComponentTreeUI(c);
                }
            });
        } catch (InterruptedException e) {
            throw new Defect("Should never happen", e);
        } catch (InvocationTargetException e) {
            throw new Defect("Should never happen", e);
        }
    }

    private static void setLookAndFeel(String lnfName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(lnfName);
    }

    public static final String WINDOWLICKER_END_OF_TEST_PAUSE = "windowlicker.end-of-test-pause";

    protected final GesturePerformer gesturePerformer = new GesturePerformer();
    protected final AWTEventQueueProber prober = new AWTEventQueueProber();
    protected int endOfTestPause = Integer.valueOf(System.getProperty(AbstractComponentDriverTest.WINDOWLICKER_END_OF_TEST_PAUSE, "0"));
    protected JFrame frame;
    protected JFrameDriver frameDriver;
    protected T driver;

    @After
    public void disposeOfFrame() throws Exception {
        pause(endOfTestPause); // just to give the viewer a chance to see the result of the test
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

    protected void pause(long sleep) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(sleep);
    }

    //had to add this as i just love the method name
    protected void pauseForSecs(long sleep) throws InterruptedException {
        TimeUnit.SECONDS.sleep(sleep);
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
