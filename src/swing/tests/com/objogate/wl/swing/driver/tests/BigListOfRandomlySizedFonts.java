package com.objogate.wl.swing.driver.tests;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

class BigListOfRandomlySizedFonts extends ArrayList<Font> {
    {
        Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        Random pseudoRandom = new Random(1);
        for (Font font : fonts) {
            add(font.deriveFont((float) Math.round(pseudoRandom.nextFloat() * 20)));
        }
        Collections.sort(this, new Comparator<Font>() {
            public int compare(Font o1, Font o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }
}
