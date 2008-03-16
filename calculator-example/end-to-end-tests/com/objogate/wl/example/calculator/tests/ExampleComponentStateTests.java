package com.objogate.wl.example.calculator.tests;

import static com.objogate.wl.driver.ComponentDriver.enabled;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

public class ExampleComponentStateTests extends CalculatorTestCase {
    @Test
    public void
    mustEnterTwoOperandsToPerformArithmetic() {
        calculatorUI.calculate().is(not(enabled()));
        
        calculatorUI.inputWithNumberButtons("77");
        calculatorUI.calculate().is(not(enabled()));
        calculatorUI.clickAddButton();
        calculatorUI.calculate().is(enabled());
        calculatorUI.inputWithNumberButtons("33");
        calculatorUI.performCalculation();
        
        calculatorUI.displaysNumber("110");
    }
}
