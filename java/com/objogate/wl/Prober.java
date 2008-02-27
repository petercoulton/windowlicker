package com.objogate.wl;

public interface Prober {
    boolean poll(Probe probe);

    void check(String context, Probe probe);
}
