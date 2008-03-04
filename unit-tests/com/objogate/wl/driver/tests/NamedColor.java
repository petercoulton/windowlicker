package com.objogate.wl.driver.tests;

import com.objogate.exception.Defect;

import java.awt.Color;
import java.lang.reflect.Field;

public class NamedColor extends Color {
    private final String name;

    public NamedColor(String name) {
        super(find(name).getRGB());
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    static Color find(String name) {
        try {
            Field[] declaredFields = Color.class.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if(declaredField.getName().equals(name))
                    return (Color) declaredField.get(null);
            }
            throw new Defect(name + " not found");
        } catch (SecurityException e) {
            throw new Defect("won't happen", e);
        } catch (IllegalArgumentException e) {
            throw new Defect("won't happen", e);
        } catch (IllegalAccessException e) {
            throw new Defect("mental jvm problem", e);
        }
    }

    static NamedColor color(String name) {
        return new NamedColor(name);
    }
}
