package com.objogate.wl.probe;

import static java.util.Arrays.asList;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.MenuElement;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.objogate.wl.swing.ComponentFinder;

public class RecursiveComponentFinder<T extends Component> implements ComponentFinder<T> {
    private final ComponentFinder<? extends Component> parentOrOwnerFinder;
    private final Class<T> componentType;
    private final Matcher<? super T> criteria;

    private Set<T> found = new LinkedHashSet<T>();

    public RecursiveComponentFinder(Class<T> componentType, Matcher<? super T> criteria, ComponentFinder<? extends Component> parentOrOwnerFinder) {
        this.parentOrOwnerFinder = parentOrOwnerFinder;
        this.componentType = componentType;
        this.criteria = criteria;
    }

    public boolean isSatisfied() {
        return parentOrOwnerFinder.isSatisfied();
    }

    public List<T> components() {
        return new ArrayList<T>(found);
    }

    public void probe() {
        parentOrOwnerFinder.probe();

        found.clear();
        searchWithin(parentOrOwnerFinder.components());
    }

    public void describeTo(Description description) {
      describeBrieflyTo(description);
      description.appendText("\n    in ")
              .appendDescriptionOf(parentOrOwnerFinder);
    }

    public boolean describeFailureTo(Description description) {
      if (parentOrOwnerFinder.describeFailureTo(description)) {
          return true;
      }

      description.appendText("\n    contained ")
              .appendText(String.valueOf(found.size()))
              .appendText(" ");
      describeBrieflyTo(description);

      return found.size() == 0;
    }
    
    private void searchWithin(Iterable<? extends Component> components) {
        for (Component component : components) {
            searchWithin(component);
        }
    }

    private void searchWithin(Component component) {
        if (componentType.isInstance(component) && criteria.matches(component)) {
            found.add(componentType.cast(component));
        } else {
            if (component instanceof Container) {
                searchWithin(componentsInside((Container) component));
            }

            if (component instanceof Window) {
                searchWithin(windowsOwnedBy((Window) component));
            }

            if (component instanceof MenuElement) {
                searchWithin(componentsInMenu((MenuElement) component));
            }
        }
    }

    private void describeBrieflyTo(Description description) {
        description.appendText(componentType.getSimpleName())
                .appendText(" ")
                .appendDescriptionOf(criteria);
    }

    private List<Component> componentsInMenu(MenuElement menuElement) {
      List<Component> list = new ArrayList<Component>();
      for (MenuElement element : menuElement.getSubElements()) {
          list.add(element.getComponent());
      }
      return list;
    }

    private List<Window> windowsOwnedBy(Window window) {
      return asList(window.getOwnedWindows());
    }

    private List<Component> componentsInside(Container container) {
      return asList(container.getComponents());
    }

}
