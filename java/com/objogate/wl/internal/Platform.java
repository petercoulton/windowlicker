package com.objogate.wl.internal;

import com.objogate.exception.Defect;

public enum Platform {
    Linux, Windows, Mac;

    public static boolean is(Platform platform) {
        String osName = System.getProperty("os.name");
        switch (platform) {
            case Mac:
                return osName.equals("Mac OS X");
            case Windows:
                return osName.toLowerCase().contains("windows");
            case Linux:
                return osName.toLowerCase().contains("linux");
        }

        throw new Defect("Need to handle " + platform);
    }

    public static String osName() {
        return System.getProperty("os.name").replace(" ", "");
    }
    
    public static void main(String[] args) {
        for (Object key : System.getProperties().keySet()) {
            System.out.println(key + "=" + System.getProperty((String)key));
        }
    }
}
