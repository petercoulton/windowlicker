package com.objogate.wl.example.calculator.tests;

import org.junit.Test;


public class BasicArithmeticTests extends CalculatorTestCase {
    @Test
    public void
    canAddTwoNumbers() {
        calculatorUI.inputWithNumberButtons("5");
        calculatorUI.clickAddButton();
        calculatorUI.inputWithNumberButtons("2");
        calculatorUI.performCalculation();

        calculatorUI.displaysNumber("7");
    }

    @Test
    public void
    canSubtractTwoNumbers() {
        calculatorUI.inputWithNumberButtons("5");
        calculatorUI.clickSubtractButton();
        calculatorUI.inputWithNumberButtons("2");
        calculatorUI.performCalculation();

        calculatorUI.displaysNumber("3");
    }

    @Test
    public void
    canMultiplyTwoNumbers() {
        calculatorUI.inputWithNumberButtons("5");
        calculatorUI.clickMultiplyButton();
        calculatorUI.inputWithNumberButtons("2");
        calculatorUI.performCalculation();

        calculatorUI.displaysNumber("10");
    }

    @Test
    public void
    canDivideTwoNumbers() {
        calculatorUI.inputWithNumberButtons("10");
        calculatorUI.clickDivideButton();
        calculatorUI.inputWithNumberButtons("2");
        calculatorUI.performCalculation();

        calculatorUI.displaysNumber("5");
    }

}
