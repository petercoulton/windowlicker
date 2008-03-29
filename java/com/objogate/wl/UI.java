package com.objogate.wl;

import javax.swing.UIManager;
import com.objogate.exception.Defect;

public enum UI {
    AQUA, GTK, METAL, WINDOWS;

    public static boolean is(UI l) {
        String name = UIManager.getLookAndFeel().getName();
        System.out.println("name = " + name);
        switch (l) {
            case AQUA:
                return name.toLowerCase().contains("aqua");
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
