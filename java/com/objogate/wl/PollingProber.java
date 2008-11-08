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

    public boolean poll(Probe probe) {
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
                sleepWithoutInterruption(pollDelayMillis);
            }
        }
    }

    private void sleepWithoutInterruption(long duration) {
        try {
            Thread.sleep(duration);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException("unexpected interrupt", e);
        }
    }

    public void check(Probe probe) {
        if (!poll(probe)) {
            StringDescription description = new StringDescription();
    
            description.appendText("\nTried to look for...\n    ");
            probe.describeTo(description);
            description.appendText("\nbut...\n    ");
            probe.describeFailureTo(description);
    
            throw new AssertionError(description.toString());
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

    protected abstract void runProbe(Probe probe);
}