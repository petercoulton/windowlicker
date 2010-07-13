package com.objogate.wl;

import org.hamcrest.StringDescription;

import com.objogate.wl.internal.Timeout;

public abstract class PollingProber implements Prober, SystemProperties {
    private long timeoutMillis;
    private long pollDelayMillis;

    public PollingProber() {
        this(defaultTimeout(), defaultPollDelay());
    }
    
    public PollingProber(long timeoutMillis, long pollDelayMillis) {
        this.timeoutMillis = timeoutMillis;
        this.pollDelayMillis = pollDelayMillis;
    }

    public long getTimeout() {
        return timeoutMillis;
    }

    public void setTimeout(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public long getPollDelay() {
        return pollDelayMillis;
    }

    public void setPollDelay(long pollDelayMillis) {
        this.pollDelayMillis = pollDelayMillis;
    }

    public void check(Probe probe) {
        if (!poll(probe)) {
            throw new AssertionError(describeFailureOf(probe));
        }
    }
    
    protected String describeFailureOf(Probe probe) {
        StringDescription description = new StringDescription();
   
        description.appendText("\nTried to find:\n    ");
        probe.describeTo(description);
        description.appendText("\nbut:\n    ");
        probe.describeFailureTo(description);

        return description.toString();
    }

    private boolean poll(Probe probe) {
        Timeout timeout = new Timeout(timeoutMillis);
    
        for (;;) {
            runProbe(probe);
            
            if (probe.isSatisfied()) {
                return true;
            }
            else if (timeout.hasTimedOut()) {
                return false;
            }
            else {
                waitFor(pollDelayMillis);
            }
        }
    }
    
    protected abstract void runProbe(Probe probe);

    private void waitFor(long duration) {
        try {
            Thread.sleep(duration);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException("unexpected interrupt", e);
        }
    }
    
    private static long defaultPollDelay() {
        return parseIntSystemProperty(POLL_DELAY, DEFAULT_POLL_DELAY);
    }

    private static long defaultTimeout() {
        return parseIntSystemProperty(POLL_TIMEOUT, DEFAULT_POLL_TIMEOUT);
    }

    private static long parseIntSystemProperty(String propertyName, long defaultValue) {
        return Long.parseLong(System.getProperty(propertyName, String.valueOf(defaultValue)));
    }
}