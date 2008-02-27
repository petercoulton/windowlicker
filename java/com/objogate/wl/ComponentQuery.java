package com.objogate.wl;

import java.awt.*;

public interface ComponentQuery<T extends Component,V> {
    V query(T component);
}
