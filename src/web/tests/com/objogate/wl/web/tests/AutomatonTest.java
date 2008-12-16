package com.objogate.wl.web.tests;

import static com.objogate.wl.gesture.Gestures.BUTTON1;
import static com.objogate.wl.gesture.Gestures.clickMouseButton;
import static com.objogate.wl.gesture.Gestures.moveMouseTo;
import static com.objogate.wl.gesture.Gestures.selectAll;
import static com.objogate.wl.gesture.Gestures.sequence;
import static com.objogate.wl.gesture.Gestures.type;
import static com.objogate.wl.gesture.Gestures.typeKey;
import static java.awt.event.KeyEvent.VK_DELETE;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.openqa.selenium.By.id;

import org.junit.Test;

import com.objogate.wl.Automaton;
import com.objogate.wl.Gesture;
import com.objogate.wl.robot.RoboticAutomaton;
import com.objogate.wl.web.AsyncElementDriver;

public class AutomatonTest extends AbstractWebTest {
	Automaton automaton = new RoboticAutomaton();
	
	private Gesture clickOn(AsyncElementDriver element) {
		return sequence(
    		moveMouseTo(element.center()),
    		clickMouseButton(BUTTON1));
	}
    
	private Gesture typeInto(AsyncElementDriver element, String text) {
		return sequence(
				clickOn(element),
				type(text));
	}
	
	private Gesture clear(AsyncElementDriver element) {
		return sequence(
			clickOn(element),
			selectAll(),
			typeKey(VK_DELETE));
	}
	
	
    @Test
    public void initiatingDynamicBehaviourWithUserAction() {
        openResource("button-click.html");
        
        automaton.perform(clickOn(browser.element(id("button"))));
        browser.element(id("display")).assertText(containsString("Changed"));
    }
    
    @Test
    public void interactingWithDynamicContent() {
        openResource("dynamic-button.html");
        
        automaton.perform(clickOn(browser.element(id("buttonA"))));
        automaton.perform(clickOn(browser.element(id("buttonB"))));
        browser.element(id("display")).assertText(containsString("B"));
    }
    
    @Test
    public void enablingElements() {
        openResource("enable-disable-button.html");
        
        browser.element(id("button")).assertIsNotEnabled();
        automaton.perform(clickOn(browser.element(id("enable"))));
        browser.element(id("button")).assertIsEnabled();
        automaton.perform(clickOn(browser.element(id("button"))));
        browser.element(id("display")).assertText(containsString("Changed"));
    }
    
    @Test
    public void typingText() {
        openResource("text-entry.html");
        
        automaton.perform(
        		typeInto(browser.element(id("input")), "hello world\n"));
        browser.element(id("reversed")).assertText(equalTo("dlrow olleh"));
    }
    
    @Test
    public void clearingText() {
        openResource("text-entry.html");
        
        AsyncElementDriver input = browser.element(id("input"));
        AsyncElementDriver output = browser.element(id("reversed"));
        
        automaton.perform(typeInto(input, "bananas\n"));
        output.assertText(not(equalTo("")));
        
        automaton.perform(clear(input), typeInto(input, "\n"));
        output.assertText(equalTo(""));
    }

    @Test
    public void radioButtons() {
    	openResource("radio-buttons.html");
    	
    	AsyncElementDriver output = browser.element(id("output"));
        AsyncElementDriver bananas = browser.element(id("bananas"));
        AsyncElementDriver breadfruit = browser.element(id("breadfruit"));
        
        automaton.perform(clickOn(bananas));
        bananas.assertIsSelected();
        breadfruit.assertIsNotSelected();
        output.assertText(equalTo("Bananas"));
        
        automaton.perform(clickOn(breadfruit));
        breadfruit.assertIsSelected();
        bananas.assertIsNotSelected();
        output.assertText(equalTo("Bread Fruit"));

        automaton.perform(clickOn(bananas));
        bananas.assertIsSelected();
        breadfruit.assertIsNotSelected();
        output.assertText(equalTo("Bananas"));
    }
}
