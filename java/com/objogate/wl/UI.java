package com.objogate.wl;

import javax.swing.UIManager;
import com.objogate.exception.Defect;

public enum UI {
    AQUA("apple.laf.AquaLookAndFeel"),
    GTK("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"),
    METAL("javax.swing.plaf.metal.MetalLookAndFeel"),
    WINDOWS("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")
    ;

    public final String classOfLookAnfFeel;

    UI(String className) {
        this.classOfLookAnfFeel = className;
    }

    public static boolean is(UI l) {
        String name = UIManager.getLookAndFeel().getName();
        switch (l) {
            case AQUA:
                return name.toLowerCase().contains("aqua")
                    || name.equals("Mac OS X");
            case GTK:
                return name.toLowerCase().contains("gtk");
            case METAL:
                return name.toLowerCase().contains("metal");
            case WINDOWS:
                return name.toLowerCase().contains("windows");
        }

        throw new Defect("Need to implement " + l);
    }
}
