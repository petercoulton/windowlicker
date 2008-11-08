package com.objogate.wl;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import com.objogate.wl.internal.ProbeRunner;


public class AWTEventQueueProber extends PollingProber {
    public AWTEventQueueProber() {
        super();
    }
    
    public AWTEventQueueProber(long timeoutMillis, long pollDelayMillis) {
        super(timeoutMillis, pollDelayMillis);
    }
    
    @Override
    protected void runProbe(Probe probe) {
        try {
            EventQueue.invokeAndWait(new ProbeRunner(probe));
        }
        catch (InterruptedException e) {
            throw new IllegalStateException("unexpected interrupt", e);
        }
        catch (InvocationTargetException e) {
            throw new IllegalStateException("probe failed", e.getCause());
        }
    }
}
