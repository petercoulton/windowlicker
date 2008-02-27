package com.objogate.wl.internal;

import java.util.ArrayList;

public class NoDuplicateList<T> extends ArrayList<T> {

    public boolean add(T o) {
        return !contains(o) && super.add(o);
    }
}
