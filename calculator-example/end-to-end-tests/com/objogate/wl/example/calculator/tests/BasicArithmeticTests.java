package com.objogate.wl.example.calculator.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.objogate.wl.example.calculator.Calculator;
import com.objogate.wl.gesture.GesturePerformer;

public class BasicArithmeticTests {
    CalculatorDriver calculatorUI;
    
    @Before public void
    runTheApplication() {
        Calculator.main();
        calculatorUI = new CalculatorDriver(new GesturePerformer());
    }

    @After public void
    stopTheApplication() {
        calculatorUI.dispose();
    }

    @Test
    public void
    canAddTwoNumbers() {
        calculatorUI.inputWithNumberButtons("5");
        calculatorUI.clickAddButton();
        calculatorUI.inputWithNumberButtons("2");
        calculatorUI.clickEqualsButton();

        calculatorUI.displaysNumber("7");
    }

    @Test
    public void
    canSubtractTwoNumbers() {
        calculatorUI.inputWithNumberButtons("5");
        calculatorUI.clickSubtractButton();
        calculatorUI.inputWithNumberButtons("2");
        calculatorUI.clickEqualsButton();

        calculatorUI.displaysNumber("3");
    }

    @Test
    public void
    canMultiplyTwoNumbers() {
        calculatorUI.inputWithNumberButtons("5");
        calculatorUI.clickMultiplyButton();
        calculatorUI.inputWithNumberButtons("2");
        calculatorUI.clickEqualsButton();

        calculatorUI.displaysNumber("10");
    }

    @Test
    public void
    canDivideTwoNumbers() {
        calculatorUI.inputWithNumberButtons("10");
        calculatorUI.clickDivideButton();
        calculatorUI.inputWithNumberButtons("2");
        calculatorUI.clickEqualsButton();

        calculatorUI.displaysNumber("5");
    }

}
