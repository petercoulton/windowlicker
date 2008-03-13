package com.objogate.wl.example.calculator.tests;

import org.junit.After;
import org.junit.Before;

import com.objogate.wl.example.calculator.Calculator;

public class CalculatorTestCase {
    protected CalculatorDriver calculatorUI;

    @Before
    public void runTheApplication() {
        Calculator.main();
        calculatorUI = new CalculatorDriver();
    }

    @After
    public void stopTheApplication() {
        calculatorUI.dispose();
    }

}