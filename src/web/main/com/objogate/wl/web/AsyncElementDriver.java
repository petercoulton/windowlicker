package com.objogate.wl.web;

import java.awt.Dimension;
import java.awt.Point;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.openqa.selenium.By;
import org.openqa.selenium.RenderedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.Tracker;


public class AsyncElementDriver implements SelfDescribing {
    private final Prober prober;
    private final WebDriver webDriver;
    private final AsyncElementDriver searchStart;
    private final By criteria;

    public AsyncElementDriver(Prober prober, WebDriver webDriver, By criteria) {
        this(prober, webDriver, null, criteria);
    }

    public AsyncElementDriver(Prober prober, WebDriver webDriver, AsyncElementDriver searchStart, By criteria) {
        this.prober = prober;
        this.webDriver = webDriver;
        this.searchStart = searchStart;
        this.criteria = criteria;
    }

    public AsyncElementDriver element(By criteria) {
        return new AsyncElementDriver(prober, webDriver, this, criteria);
    }

    public void assertText(final Matcher<String> textMatcher) {
        prober.check(new ElementPropertyProbe(this, "text", textMatcher) {
            @Override
            protected String propertyValue(WebElement e) {
                return e.getText();
            }
        });
    }

    public void assertValue(final Matcher<String> valueMatcher) {
        prober.check(new ElementPropertyProbe(this, "value", valueMatcher) {
            @Override
            protected String propertyValue(WebElement e) {
                return e.getValue();
            }
        });
    }

    public void assertIsEnabled() {
        assertEnabledFlagIs(true);
    }

    public void assertIsNotEnabled() {
        assertEnabledFlagIs(false);
    }

    private void assertEnabledFlagIs(final boolean expectedFlagValue) {
        prober.check(new ElementFlagProbe(this, "enabled", expectedFlagValue) {
            @Override
            protected boolean flagValue(WebElement e) {
                return e.isEnabled();
            }
        });
    }
    
    public void assertIsSelected() {
    	assertSelectedFlagIs(true);
    }
    
    public void assertIsNotSelected() {
    	assertSelectedFlagIs(false);
    }
    
    private void assertSelectedFlagIs(final boolean expectedFlagValue) {
        prober.check(new ElementFlagProbe(this, "selected", expectedFlagValue) {
            @Override
            protected boolean flagValue(WebElement e) {
                return e.isSelected();
            }
        });
    }

    public void click() {
        prober.check(new ElementProbe(this) {
            @Override
            protected void probe(WebElement element) {
                element.click();
            }
        });
    }

    public void type(final String string) {
        prober.check(new ElementProbe(this) {
            @Override
            protected void probe(WebElement element) {
                element.sendKeys(string);
            }
        });
    }

    public void clear() {
        prober.check(new ElementProbe(this) {
            @Override
            protected void probe(WebElement element) {
                element.clear();
            }
        });
    }
    
    public Tracker center() {
        return new Tracker() {
            private Point center;
            
            public Point target(Point currentLocation) {
            	if (center == null) {
	                prober.check(new ElementProbe(AsyncElementDriver.this) {
	                    @Override
	                    protected void probe(WebElement e) {
	                        Point p = ((Locatable)e).getLocationOnScreenOnceScrolledIntoView();
	                        Dimension d = ((RenderedWebElement)e).getSize();
	                        
	                        center = new Point(p.x + d.width/2, p.y + d.height/2);
	                    }
	                });
            	}
                return center;
            }
        };
    }
    
    public void describeTo(Description description) {
        description.appendText("an element ").appendText(formatCriteria());
        if (searchStart != null) {
            description.appendText(", in ").appendDescriptionOf(searchStart);
        }
    }

    private String formatCriteria() {
        return criteria.toString().toLowerCase().replaceAll("[^\\w]+", " ");
    }

    WebElement findElement() {
        if (searchStart == null) {
            return webDriver.findElement(criteria);
        }
        else {
            WebElement ancestor = searchStart.findElement();
            WebElement descendent = ancestor.findElement(criteria);
            return descendent;
        }
    }
}
