package com.objogate.wl.web;

import org.openqa.selenium.WebElement;

public class ElementActionProbe extends ElementProbe {
    private final ElementAction action;

    public ElementActionProbe(AsyncElementDriver driver, ElementAction action) {
        super(driver);
        this.action = action;
    }

    @Override
    protected void probe(WebElement element) {
        action.performOn(element);
    }
}
