package com.objogate.wl.swing.matcher.tests;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import javax.swing.JLabel;

import org.hamcrest.StringDescription;
import org.junit.Test;

import com.objogate.wl.swing.matcher.ComponentOpaqueMatcher;


public class ComponentOpaqueMatcherTests {
    ComponentOpaqueMatcher matcher = new ComponentOpaqueMatcher();
    
    @Test
    public void
    matchesOpaqueComponents() {
        JLabel c = new JLabel();
        
        c.setOpaque(true);
        assertTrue(matcher.matches(c));
        
        c.setOpaque(false);
        assertTrue(!matcher.matches(c));
    }
    
    @Test
    public void
    hasAReadableDescription() {
        assertThat(StringDescription.asString(matcher), containsString("opaque"));
    }
}
