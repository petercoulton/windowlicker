package com.objogate.wl;

import java.awt.Component;
import java.util.List;

public interface ComponentFinder<T extends Component> extends Probe {
    List<T> components();
}
