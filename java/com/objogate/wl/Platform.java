package com.objogate.wl;

import com.objogate.exception.Defect;

public enum Platform {
    Linux, Windows, Mac;

    public static boolean is(Platform platform) {
        switch (platform) {
            case Mac:
                return System.getProperty("os.name").equals("Mac OS X");
            case Windows:
                return System.getProperty("os.name").toLowerCase().contains("windows");
            case Linux:
                System.out.println("Platform.is " + System.getProperty("os.name"));
                return false;
        }

        throw new Defect("Need to handle " + platform);
    }
}
