package com.objogate.wl.swing;

import java.awt.Component;
import java.util.List;

import com.objogate.wl.Probe;

public interface ComponentFinder<T extends Component> extends Probe {
    List<T> components();
}
