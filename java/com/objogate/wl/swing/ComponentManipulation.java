package com.objogate.wl.swing;

import java.awt.Component;

public interface ComponentManipulation<T extends Component> {
    void manipulate(T component);

}
