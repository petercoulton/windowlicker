package com.objogate.wl.driver.tests;

import static com.objogate.wl.probe.ComponentIdentity.selectorFor;
import static org.hamcrest.Matchers.equalTo;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.objogate.wl.driver.JSliderDriver;

public class JSliderDriverTest extends AbstractComponentDriverTest<JSliderDriver> {
    private JSliderDriver horizontalDriver;
    private JSliderDriver verticalDriver;
    private JSliderDriver invertedHorizontalDriver;
    private JSliderDriver invertedVerticalDriver;
    private JSliderDriver verticalSnapToDriver;

    @Test
    @Ignore
    public void testCanSetTheValueOfAHorizontalSlider() {
        horizontalDriver.hasValue(equalTo(50));
        horizontalDriver.makeValue(78);
        horizontalDriver.hasValue(equalTo(78));
        horizontalDriver.makeValue(12);
        horizontalDriver.hasValue(equalTo(12));
    }

    @Test
    @Ignore
    public void testCanSetTheValueOfAnInvertedHorizontalSlider() {
        invertedHorizontalDriver.hasValue(equalTo(50));
        invertedHorizontalDriver.makeValue(78);
        invertedHorizontalDriver.hasValue(equalTo(78));
        invertedHorizontalDriver.makeValue(12);
        invertedHorizontalDriver.hasValue(equalTo(12));
    }

    @Test
    @Ignore
    public void testCanSetTheValueOfAVerticalSlider() {
        verticalDriver.hasValue(equalTo(50));
        verticalDriver.makeValue(78);
        verticalDriver.hasValue(equalTo(78));
        verticalDriver.makeValue(12);
        verticalDriver.hasValue(equalTo(12));
    }

    @Test
    @Ignore
    public void testCanSetTheValueOfAnInvertedVerticalSlider() {
        invertedVerticalDriver.hasValue(equalTo(50));
        invertedVerticalDriver.makeValue(78);
        invertedVerticalDriver.hasValue(equalTo(78));
        invertedVerticalDriver.makeValue(12);
        invertedVerticalDriver.hasValue(equalTo(12));
    }

    @Test
    @Ignore
    public void testCanSetTheValueOfAnSnapToVerticalSlider() {
        verticalSnapToDriver.hasValue(equalTo(50));
        verticalSnapToDriver.makeValue(75);
        verticalSnapToDriver.hasValue(equalTo(75));
        verticalSnapToDriver.makeValue(15);
        verticalSnapToDriver.hasValue(equalTo(15));
    }

    @Before
    public void setUp() throws Exception {

        JPanel panel = new JPanel();
        JSlider horizontal = new JSlider(0, 100, 50) {
            {
                setName("Horizontal");
                setMajorTickSpacing(20);
                setPaintLabels(true);
                setPaintTicks(true);
                setBorder(new LineBorder(Color.RED, 1));
            }
        };

        JSlider invertedHorizontal = new JSlider(0, 100, 50) {
            {
                setInverted(true);
                setName("Inverted Horizontal");
                setMajorTickSpacing(20);
                setPaintLabels(true);
                setPaintTicks(true);
                setBorder(new LineBorder(Color.YELLOW, 1));
            }
        };

        JSlider vertical = new JSlider(SwingConstants.VERTICAL, 0, 100, 50) {
            {
                setName("Vertical");
                setMajorTickSpacing(10);
                setPaintLabels(true);
                setPaintTicks(true);
                setBorder(new LineBorder(Color.BLUE, 1));
            }
        };

        JSlider invertedVertical = new JSlider(SwingConstants.VERTICAL, 0, 100, 50) {
            {
                setName("Inverted Vertical");
                setInverted(true);
                setMajorTickSpacing(10);
                setPaintLabels(true);
                setPaintTicks(true);
                setBorder(new LineBorder(Color.GREEN, 1));
            }
        };

        JSlider verticalSnapTo = new JSlider(SwingConstants.VERTICAL, 0, 100, 50) {
            {
                setName("Vertical Snap-To");
                setMajorTickSpacing(10);
                setMinorTickSpacing(5);
                setSnapToTicks(true);
                setPaintLabels(true);
                setPaintTicks(true);
                setBorder(new LineBorder(Color.BLACK, 1));
            }
        };

        panel.add(horizontal);
        panel.add(vertical);
        panel.add(invertedVertical);
        panel.add(invertedHorizontal);
        panel.add(verticalSnapTo);

        view(panel);

        horizontalDriver = new JSliderDriver(gesturePerformer, selectorFor(horizontal), prober);
        verticalDriver = new JSliderDriver(gesturePerformer, selectorFor(vertical), prober);
        invertedHorizontalDriver = new JSliderDriver(gesturePerformer, selectorFor(invertedHorizontal), prober);
        invertedVerticalDriver = new JSliderDriver(gesturePerformer, selectorFor(invertedVertical), prober);
        verticalSnapToDriver = new JSliderDriver(gesturePerformer, selectorFor(verticalSnapTo), prober);
    }
}
