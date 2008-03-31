package com.objogate.wl;

import com.objogate.exception.Defect;

public enum Platform {
    Linux, Windows, Mac;

    public static boolean is(Platform platform) {
        String osName = System.getProperty("os.name");
        System.out.println("osName = " + osName);
        switch (platform) {
            case Mac:
                return osName.equals("Mac OS X");
            case Windows:
                return osName.toLowerCase().contains("windows");
            case Linux:
                return osName.toLowerCase().contains("Linux");
        }

        throw new Defect("Need to handle " + platform);
    }
}
