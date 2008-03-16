package com.objogate.wl.matcher.tests;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.awt.Component;

import javax.swing.JLabel;

import org.hamcrest.StringDescription;
import org.junit.Test;

import com.objogate.wl.matcher.ComponentEnabledMatcher;


public class ComponentEnabledMatcherTests {
    ComponentEnabledMatcher matcher = new ComponentEnabledMatcher();
    
    @Test
    public void
    matchesEnabledComponents() {
        Component c = new JLabel();
        
        c.setEnabled(true);
        assertTrue(matcher.matches(c));
        
        c.setEnabled(false);
        assertTrue(!matcher.matches(c));
    }
    
    @Test
    public void
    hasAReadableDescription() {
        assertThat(StringDescription.asString(matcher), containsString("enabled"));
    }
}
