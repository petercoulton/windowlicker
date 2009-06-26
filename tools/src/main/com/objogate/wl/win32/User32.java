package com.objogate.wl.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;


public interface User32 extends Win32Library, AccessRights {
    User32 INSTANCE = (User32)Native.loadLibrary("user32", User32.class, DEFAULT_OPTIONS);


    class HWINSTA extends PointerType {
        public HWINSTA() {
        }

        public HWINSTA(Pointer pointer) {
            super(pointer);
        }
    }

    HWINSTA CreateWindowStation(String winsta, int flags, int desiredAccess, SECURITY_ATTRIBUTES securityAttributes);
    boolean SetProcessWindowStation(HWINSTA windowStation);
    HWINSTA GetProcessWindowStation();


    class HDESK extends PointerType {
        public HDESK() {
        }

        public HDESK(Pointer pointer) {
            super(pointer);
        }
    }

    HDESK CreateDesktop(String desktop, String device, Pointer devmode, int flags, int desiredAccess, SECURITY_ATTRIBUTES securityAttributes);
    boolean SetThreadDesktop(HDESK desktop);
    boolean SwitchDesktop(HDESK desktop);
    HDESK GetThreadDesktop(int threadId);

    class SECURITY_ATTRIBUTES extends Structure {
        int length;
        Pointer securityDescriptor;
        boolean inheritHandle;
    }

    int GetLastError();

}
