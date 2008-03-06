package com.objogate.wl;

import org.hamcrest.SelfDescribing;

public interface Query<T,V> extends SelfDescribing {
    V query(T component);
}
