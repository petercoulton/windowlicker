package com.objogate.wl.probe.tests;

import java.awt.Component;
import static java.util.Arrays.asList;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.objogate.wl.ComponentFinder;
import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.probe.ComponentManipulatorProbe;

@RunWith(JMock.class)
public class ComponentManipulatorProbeTest {
    Mockery context = new JUnit4Mockery();

    @SuppressWarnings("unchecked")
    ComponentFinder<ComponentType> finder = context.mock(ComponentFinder.class);

    @SuppressWarnings("unchecked")
    ComponentManipulation<ComponentType> manipulation = context.mock(ComponentManipulation.class);
    
    ComponentType componentA = new ComponentType("A");

    ComponentType componentB = new ComponentType("B");

    ComponentManipulatorProbe<ComponentType> manipulatorProbe =
            new ComponentManipulatorProbe<ComponentType>(finder, manipulation);


    @Test public void
    manipulatesAllComponentsFoundIfFinderIsSatisfied() {
        context.checking(new Expectations(){{
            one (finder).probe();
            allowing (finder).isSatisfied(); will(returnValue(true));
            allowing (finder).components(); will(returnValue(asList(componentA, componentB)));
            one (manipulation).manipulate(componentA);
            one (manipulation).manipulate(componentB);
        }});

        manipulatorProbe.probe();
        
        assertTrue("is satisfied", manipulatorProbe.isSatisfied());
    }

    @Test public void
    doesNotManipulateAnyComponentsIfFinderIsNotSatisfied() {
        context.checking(new Expectations(){{
            one (finder).probe();
            allowing (finder).isSatisfied(); will(returnValue(false));
            never (manipulation);
        }});

        manipulatorProbe.probe();

        assertTrue("is not satisfied", !manipulatorProbe.isSatisfied());
    }

    private static class ComponentType extends Component {
        public ComponentType(String name) {
            setName(name);
        }
    }
}
