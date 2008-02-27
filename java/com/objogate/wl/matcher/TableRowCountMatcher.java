package com.objogate.wl.matcher;

import static org.hamcrest.Matchers.equalTo;

import java.awt.Component;

import javax.swing.JTable;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class TableRowCountMatcher extends TypeSafeMatcher<Component> {
    private final Matcher<Integer> rowCountMatcher;
    
    public TableRowCountMatcher(Matcher<Integer> rowCountMatcher) {
        this.rowCountMatcher = rowCountMatcher;
    }

    public boolean matchesSafely(Component component) {
        return component instanceof JTable
            && matchesSafely((JTable)component);
    }
    
    public boolean matchesSafely(JTable table) {
        return rowCountMatcher.matches(table.getRowCount());
    }

    public void describeTo(Description description) {
        description.appendText("with row count ");
        rowCountMatcher.describeTo(description);
    }

    public static Matcher<Component> withRowCount(int n) {
        return withRowCount(equalTo(n));
    }

    public static Matcher<Component> withRowCount(Matcher<Integer> rowCountMatcher) {
        return new TableRowCountMatcher(rowCountMatcher);
    }
}
