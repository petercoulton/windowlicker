package com.objogate.wl;

import org.hamcrest.*;

public class QueryResultMatcher<T,V> extends TypeSafeMatcher<T> {
    private final Query<T,V> query;
    private final Matcher<? super V> criteria;
    
    public QueryResultMatcher(Query<T, V> query, Matcher<? super V> criteria) {
        this.query = query;
        this.criteria = criteria;
    }
    
    public boolean matchesSafely(T t) {
        return criteria.matches(query.query(t));
    }
    
    public void describeTo(Description description) {
        description.appendText("has ")
                   .appendDescriptionOf(query)
                   .appendText(" ")
                   .appendDescriptionOf(criteria);
    }

    @Factory
    public static <T,V> Matcher<T> has(Query<T, V> query, Matcher<? super V> criteria) {
        return new QueryResultMatcher<T,V>(query, criteria);
    }
}
