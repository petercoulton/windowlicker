package com.objogate.wl.example.calculator.tests;

import org.junit.Test;

public class ExampleWhenNothingHappensTests extends CalculatorTestCase {
    @Test
    public void 
    cannotEnterMultipleLeadingZeros() {
        calculatorUI.displaysNumber("0");
        
        calculatorUI.pressDigitButton(0);
        calculatorUI.pressDigitButton(0);
        calculatorUI.pressDigitButton(0);
        
        // Cannot verify that the displayed number is "0" not "000", 
        // because it has not changed.  Have to enter another digit
        // and verify that the displayed number does not have leading
        // zeros.
        
        calculatorUI.pressDigitButton(1);
        
        calculatorUI.displaysNumber("1");
    }
}
