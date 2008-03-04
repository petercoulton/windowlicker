package com.objogate.wl;

import java.awt.*;

import org.hamcrest.SelfDescribing;

public interface ComponentQuery<T extends Component,V> extends SelfDescribing {
    V query(T component);
}
