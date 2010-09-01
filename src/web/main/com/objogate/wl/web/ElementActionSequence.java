package com.objogate.wl.web;

import org.openqa.selenium.WebElement;

public class ElementActionSequence implements ElementAction {
    private final ElementAction[] actions;

    public ElementActionSequence(ElementAction... actions) {
        this.actions = actions;
    }

    public void performOn(WebElement element) {
        for (ElementAction action : actions) {
            action.performOn(element);
        }
    }
}
