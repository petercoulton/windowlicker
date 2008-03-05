package com.objogate.wl;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import org.hamcrest.StringDescription;
import com.objogate.wl.internal.ProbeRunner;
import com.objogate.wl.internal.Timeout;


public class AWTEventQueueProber implements Prober, SystemProperties {
    private long timeoutMillis;
    private long pollDelayMillis;

    public AWTEventQueueProber() {
        this(defaultTimeout(), defaultPollDelay());
    }
    
    public AWTEventQueueProber(long timeoutMillis, long pollDelayMillis) {
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
        ProbeRunner probeRunner = new ProbeRunner(probe);
        Timeout timeout = new Timeout(timeoutMillis);
        
        for(;;) {
            try {
                EventQueue.invokeAndWait(probeRunner);
                if (probe.isSatisfied()) {
                    return true;
                }
                else if (timeout.hasTimedOut()) {
                    return false;
                }
                else {
                    Thread.sleep(pollDelayMillis);
                }
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("unexpected interrupt", e);
            }
            catch (InvocationTargetException e) {
                throw new IllegalStateException("predicate failed", e.getCause());
            }
        }
    }
    
    public void check(String context, Probe probe) {
        try {
            if (!poll(probe)) {
                StringDescription description = new StringDescription();
                
                description.appendText(context)
                           .appendText("\nTried to look for...\n    ");
                probe.describeTo(description);
                description.appendText("\nbut...\n    ");
                probe.describeFailureTo(description);
                
                throw new AssertionError(description.toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
            System.err.println("probe blew up:" + e.getMessage());
        }
    }
    
    private static int defaultPollDelay() {
        return parseIntSystemProperty(POLL_DELAY, "100");
    }
    
    private static int defaultTimeout() {
        return parseIntSystemProperty(POLL_TIMEOUT, "5000");
    }
    
    private static int parseIntSystemProperty(final String propertyName, final String defaultValue) {
        return Integer.parseInt(System.getProperty(propertyName, defaultValue));
    }
}
