package com.objogate.wl.driver;

import javax.swing.text.JTextComponent;
import org.hamcrest.Description;
import com.objogate.wl.Probe;
import com.objogate.wl.swing.ComponentSelector;

public abstract class AbstractJTextComponentProbe<T extends JTextComponent> implements Probe {
    protected final ComponentSelector<T> selector;

    public AbstractJTextComponentProbe(ComponentSelector<T> selector) {
        this.selector = selector;
    }

    public void probe() {
        selector.probe();
        if (selector.isSatisfied()) {
            JTextComponent jTextComponent = selector.component();
            probe(jTextComponent);
        }
    }

    protected abstract void probe(JTextComponent jTextComponent);

    public boolean isSatisfied() {
        return selector.isSatisfied();
    }

    public void describeTo(Description description) {
        description.appendText("inner bounds");
    }
}
