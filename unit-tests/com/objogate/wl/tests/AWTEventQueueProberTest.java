package com.objogate.wl.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Description;
import org.junit.Test;

import com.objogate.wl.AWTEventQueueProber;
import com.objogate.wl.Probe;


public class AWTEventQueueProberTest {
    static final int TIMEOUT = 250;
    
    AWTEventQueueProber prober = new AWTEventQueueProber(TIMEOUT, 50);
    
    @Test public void
    isSatisfiedIfProbeIsSatisfiedWithinTimeout() {
        final long start = System.currentTimeMillis();
        
        boolean isSatisfied = prober.poll(new Probe() {
            long probeTime;
            
            public void probe() {
                probeTime = System.currentTimeMillis();
            }
            
            public boolean isSatisfied() {
                return probeTime-start > 100 ;
            }
            
            public void describeTo(Description description) {
            }

            public boolean describeFailureTo(Description description) {
                return true;
            }
        });
        
        assertTrue("should be satisfied within timeout", isSatisfied);
    }

    @Test public void
    isNotSatisfiedIfProbeIsNotSatisfiedWithinTimeout() {
        boolean isSatisfied = prober.poll(new Probe() {
            public void probe() {
            }
            
            public boolean isSatisfied() {
                return false;
            }
            
            public void describeTo(Description description) {
            }

            public boolean describeFailureTo(Description description) {
                return true;
            }
        });
        
        assertTrue("should not be satisfied within timeout", !isSatisfied);
    }
    
    @Test public void
    assertsIfProbeIsSatisfiedWithinTimeout() {
        try {
            prober.check("context", new Probe() {
                public void probe() {
                }
                
                public boolean isSatisfied() {
                    return false;
                }
                
                public void describeTo(Description description) {
                    description.appendText("error message");
                }

                public boolean describeFailureTo(Description description) {
                    return true;
                }
            });
        }
        catch (AssertionError e) {
            assertEquals("context: error message", e.getMessage());
        }
    }
}
