package com.objogate.wl.matcher;

import java.awt.Component;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class ComponentTypeMatcher extends BaseMatcher<Component> {
    private final Class<? extends Component> componentType;

    public ComponentTypeMatcher(Class<? extends Component> componentType) {
        this.componentType = componentType;
    }

    public boolean matches(Object o) {
        return o != null && componentType.isInstance(o);
    }

    public void describeTo(Description description) {
        description.appendText("are ").appendText(componentType.getSimpleName());
    }
}
