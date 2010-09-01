package com.objogate.wl.web;

import org.openqa.selenium.WebElement;

public class ElementActions {
    public static final ElementAction CLICK = new ElementAction() {
        public void performOn(WebElement element) {
            element.click();
        }
    };

    public static final ElementAction CLEAR = new ElementAction() {
        public void performOn(WebElement element) {
            element.clear();
        }
    };

    public static final ElementAction SUBMIT = new ElementAction() {
        public void performOn(WebElement element) {
            element.submit();
        }
    };

    public static final ElementAction SELECT = new ElementAction() {
        public void performOn(WebElement element) {
            element.setSelected();
        }
    };

    public static final ElementAction TOGGLE = new ElementAction() {
        public void performOn(WebElement element) {
            element.toggle();
        }
    };

    public static ElementAction sendKeys(final String string) {
        return new ElementAction() {
            public void performOn(WebElement element) {
                element.sendKeys(string);
            }
        };
    }

    public static ElementAction sequenceOf(ElementAction ... actions) {
        return new ElementActionSequence(actions);
    }
}
