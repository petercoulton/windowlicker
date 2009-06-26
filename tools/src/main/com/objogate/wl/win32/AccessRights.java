package com.objogate.wl.win32;

public interface AccessRights {
    int DELETE = 0x00010000;
    int READ_CONTROL = 0x00020000;
    int WRITE_DAC = 0x00040000;
    int WRITE_OWNER = 0x00080000;
    int SYNCHRONIZE = 0x00100000;

    int STANDARD_RIGHTS_REQUIRED = 0x000F0000;

    int STANDARD_RIGHTS_READ = READ_CONTROL;
    int STANDARD_RIGHTS_WRITE = READ_CONTROL;
    int STANDARD_RIGHTS_EXECUTE = READ_CONTROL;

    int STANDARD_RIGHTS_ALL = 0x001F0000;

    int SPECIFIC_RIGHTS_ALL = 0x0000FFFF;

    int ACCESS_SYSTEM_SECURITY = 0x01000000;

    int MAXIMUM_ALLOWED = 0x02000000;

    int GENERIC_READ = 0x80000000;
    int GENERIC_WRITE = 0x40000000;
    int GENERIC_EXECUTE = 0x20000000;
    int GENERIC_ALL = 0x10000000;

    int WINSTA_ALL_ACCESS = 0x37F;

    int DESKTOP_CREATEMENU = 0x0004;      // Required to create a menu on the desktop.
    int DESKTOP_CREATEWINDOW = 0x0002;    // Required to create a window on the desktop.
    int DESKTOP_ENUMERATE = 0x0040;       // Required for the desktop to be enumerated.
    int DESKTOP_HOOKCONTROL = 0x0008;     // Required to establish any of the window hooks.
    int DESKTOP_JOURNALPLAYBACK = 0x0020; // Required to perform journal playback on a desktop.
    int DESKTOP_JOURNALRECORD = 0x0010;   // Required to perform journal recording on a desktop.
    int DESKTOP_READOBJECTS = 0x0001;     // Required to read objects on the desktop.
    int DESKTOP_SWITCHDESKTOP = 0x0100;   // Required to activate the desktop using the SwitchDesktop function.
    int DESKTOP_WRITEOBJECTS = 0x0080;    // Required to write objects on the desktop.
}
