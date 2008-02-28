package com.objogate.wl.example.calculator.tests;

import javax.swing.JButton;
import javax.swing.JTextField;
import static org.hamcrest.Matchers.equalTo;
import com.objogate.wl.driver.*;
import static com.objogate.wl.example.calculator.Calculator.*;
import com.objogate.wl.gesture.GesturePerformer;
import static com.objogate.wl.matcher.ComponentMatchers.named;
import static com.objogate.wl.matcher.ComponentMatchers.showingOnScreen;

public class CalculatorDriver extends JFrameDriver {
    public CalculatorDriver(GesturePerformer gesturePerformer) {
        super(gesturePerformer, named(MAIN_WINDOW), showingOnScreen());
    }

    public void inputWithNumberButtons(String numberText) {
        displaysNumber("");

        for (int i = 0; i < numberText.length(); i++) {
            int digit = numberText.charAt(i) - '0';

            pressDigitButton(digit);
        }

        displaysNumber(numberText);
    }

    private void pressDigitButton(int digit) {
        if (0 <= digit && digit <= 9) {
            clickButton(digitButtonName(digit));
        } else {
            throw new IllegalArgumentException("invalid character in number: " + digit);
        }
    }

    public void clickAddButton() {
        clickButton(ADD_BUTTON);
    }

    public void clickSubtractButton() {
        clickButton(SUB_BUTTON);
    }

    public void clickMultiplyButton() {
        clickButton(MUL_BUTTON);
    }

    public void clickDivideButton() {
        clickButton(DIV_BUTTON);
    }

    public void clickEqualsButton() {
        clickButton(EQUALS_BUTTON);
    }

    private void clickButton(String name) {
        new JButtonDriver(this, JButton.class, named(name)).click();
    }
    
    public void displaysNumber(String expectedResult) {
        display().text(equalTo(expectedResult));
    }

    private JTextComponentDriver display() {
        return new JTextFieldDriver(this, JTextField.class, named(DISPLAY));
    }
}
