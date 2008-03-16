package com.objogate.wl.example.calculator.tests;

import static com.objogate.wl.example.calculator.Calculator.ADD_BUTTON;
import static com.objogate.wl.example.calculator.Calculator.DISPLAY;
import static com.objogate.wl.example.calculator.Calculator.DIV_BUTTON;
import static com.objogate.wl.example.calculator.Calculator.CALCULATE_BUTTON;
import static com.objogate.wl.example.calculator.Calculator.MAIN_WINDOW;
import static com.objogate.wl.example.calculator.Calculator.MUL_BUTTON;
import static com.objogate.wl.example.calculator.Calculator.SUB_BUTTON;
import static com.objogate.wl.example.calculator.Calculator.digitButtonName;
import static org.hamcrest.Matchers.equalTo;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.objogate.wl.driver.ComponentDriver;
import com.objogate.wl.driver.JButtonDriver;
import com.objogate.wl.driver.JFrameDriver;
import com.objogate.wl.driver.JTextFieldDriver;
import com.objogate.wl.gesture.GesturePerformer;

public class CalculatorDriver extends JFrameDriver {
    @SuppressWarnings("unchecked")
    public CalculatorDriver() {
        super(new GesturePerformer(), named(MAIN_WINDOW), showingOnScreen());
    }

    public void inputWithNumberButtons(String numberText) {
        displaysNumber("0");

        for (int i = 0; i < numberText.length(); i++) {
            int digit = numberText.charAt(i) - '0';

            clickDigitButton(digit);
        }

        displaysNumber(numberText);
    }

    public void clickDigitButton(int digit) {
        if (0 <= digit && digit <= 9) {
            button(digitButtonName(digit)).click();
        } else {
            throw new IllegalArgumentException("invalid character in number: " + digit);
        }
    }

    public void clickAddButton() {
        button(ADD_BUTTON).click();
    }

    public void clickSubtractButton() {
        button(SUB_BUTTON).click();
    }

    public void clickMultiplyButton() {
        button(MUL_BUTTON).click();
    }

    public void clickDivideButton() {
        button(DIV_BUTTON).click();
    }
    
    public void performCalculation() {
        calculate().click();
    }
    
    public JButtonDriver calculate() {
        return button(CALCULATE_BUTTON);
    }
    
    public void displaysNumber(String expectedResult) {
        display().text(equalTo(expectedResult));
    }
    
    @SuppressWarnings("unchecked")
    private JTextFieldDriver display() {
        return new JTextFieldDriver(this, JTextField.class, ComponentDriver.named(DISPLAY));
    }

    @SuppressWarnings("unchecked")
    private JButtonDriver button(String name) {
        return new JButtonDriver(this, JButton.class, ComponentDriver.named(name));
    }

}
