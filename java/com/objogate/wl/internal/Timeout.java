package com.objogate.wl.internal;

public class Timeout {
    private final long duration;
    private final long start;
    
    public Timeout(long duration) {
        this.duration = duration;
        this.start = System.currentTimeMillis();
    }
    
    public boolean hasTimedOut() {
        final long now = System.currentTimeMillis();
        return (now-start) > duration;
    }
}
