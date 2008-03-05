package com.objogate.wl;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;

/**
 * A Probe samples the system and reports whether the last sample satisfies some test criteria.  
 * It can describe itself and why the last sample (if any) did not satisfy its test criteria.
 *  
 * @author nat
 */
public interface Probe extends SelfDescribing {
    void probe();
    boolean isSatisfied();
    
    /**
     * Describe the reason that the Probe failed.
     * If the Probe decorates another, it must only describe its own success or failure if 
     * its delegate probe returns false.  This makes a chain of probes describe the root 
     * cause of a failure and not confuse the reader with unnecessary output.
     * 
     * @param description
     *      The Description to append to
     * @return
     *      true if the reason for the failure has been described, false otherwise.
     */
    boolean describeFailureTo(Description description);
}
