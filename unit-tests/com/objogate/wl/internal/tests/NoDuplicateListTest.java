package com.objogate.wl.internal.tests;

import org.junit.Assert;
import org.junit.Test;
import com.objogate.wl.internal.NoDuplicateList;

public class NoDuplicateListTest  {

    NoDuplicateList<Number> list = new NoDuplicateList<Number>();


    @Test
    public void canAddSomethingOnce() {
        Number number = 1234;
        Assert.assertTrue(list.add(number));
        Assert.assertFalse(list.add(number));
        Assert.assertEquals(1, list.size());
    }
}
