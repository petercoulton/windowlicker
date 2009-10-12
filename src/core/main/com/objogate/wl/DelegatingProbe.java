package com.objogate.wl;

import org.hamcrest.Description;

public class DelegatingProbe<T extends Probe> implements Probe {
	public boolean isSatisfied() {
		// TODO Auto-generated method stub
		return false;
	}

	public void probe() {
		
	}
	
	public void describeTo(Description description) {
		
	}

	public boolean describeFailureTo(Description description) {
		return false;
	}
	
}
