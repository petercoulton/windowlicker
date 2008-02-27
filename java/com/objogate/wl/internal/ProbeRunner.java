package com.objogate.wl.internal;

import java.awt.EventQueue;
import com.objogate.wl.Probe;

public class ProbeRunner implements Runnable {
    private final Probe probe;
    
    public ProbeRunner(Probe probe) {
        this.probe = probe;
    }

    public void run() {
        if ( !EventQueue.isDispatchThread()) {
            throw new IllegalStateException("Probe must be run on event dispatch thread");
        }
        probe.probe();
    }
}
