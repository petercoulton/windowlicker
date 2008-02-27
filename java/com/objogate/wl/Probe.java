package com.objogate.wl;

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
}
