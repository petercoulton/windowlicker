package com.objogate.wl.tests;

import static org.hamcrest.Matchers.containsString;

import javax.swing.SwingUtilities;

import org.hamcrest.Description;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import com.objogate.wl.Probe;
import com.objogate.wl.Prober;
import com.objogate.wl.swing.AWTEventQueueProber;


public class AWTEventQueueProberTest {
    static final int TIMEOUT = 250;
    
    Prober prober = new AWTEventQueueProber(TIMEOUT, 50);

    @Test public void
    assertionFailsIfProbeIsSatisfiedWithinTimeout() {
        try {
            prober.check(new Probe() {
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
            MatcherAssert.assertThat(e.getMessage(), containsString("error message"));
        }
    }
    
    @Test public void
    assertonPassesIfProbeIsSatisfied() {
        prober.check(new Probe() {
            public void probe() {
            }
            
            public boolean isSatisfied() {
                return true;
            }
            
            public void describeTo(Description description) {
            }

            public boolean describeFailureTo(Description description) {
                return true;
            }
        });
    }

    @Test public void
    repeatedlyPollsProbeUntilItIsSatisfied() {
        final long start = System.currentTimeMillis();
        
        prober.check(new Probe() {
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
    }
    
    @Test public void
    runsProbeOnAWTEventDispatchThread() {
        prober.check(new Probe() {
            boolean wasOnEventDispatchThread = false;
            
            public void probe() {
                wasOnEventDispatchThread = SwingUtilities.isEventDispatchThread();
            }
            
            public boolean isSatisfied() {
                return wasOnEventDispatchThread;
            }

            public boolean describeFailureTo(Description description) {
                description.appendText("was not event dispatch thread");
                return true;
            }

            public void describeTo(Description description) {
                description.appendText("run on event dispatch thread");
            }
        });
    }
}
