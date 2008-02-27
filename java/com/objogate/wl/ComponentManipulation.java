package com.objogate.wl;

import java.awt.Component;

public interface ComponentManipulation<T extends Component> {
    void manipulate(T component);

}
