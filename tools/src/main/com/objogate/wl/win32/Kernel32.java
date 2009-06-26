package com.objogate.wl.win32;

import com.sun.jna.PointerType;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;


public interface Kernel32 extends Win32Library {
    Kernel32 INSTANCE = (Kernel32)Native.loadLibrary("kernel32", Kernel32.class, DEFAULT_OPTIONS);

    class HANDLE extends PointerType {
        public HANDLE() {
        }

        public HANDLE(Pointer pointer) {
            super(pointer);
        }
    }

    class STARTUPINFO extends Structure {
        public int size;
        public String reserved;
        public String desktop;
        public String title;
        public int x;
        public int y;
        public int xSize;
        public int ySize;
        public int xCountChars;
        public int yCountChars;
        public int fillAttribute;
        public int flags;
        public short showWindow;
        public short reserved2Size;
        public Pointer reserved2;
        public HANDLE stdInput;
        public HANDLE stdOutput;
        public HANDLE stdError;
    }

    class PROCESS_INFORMATION extends Structure {
        public HANDLE hProcess;
        public HANDLE hThread;
        public int dwProcessId;
        public int  dwThreadId;
    }

    boolean CreateProcess(String applicationName,
                          String commandLine,
                          SECURITY_ATTRIBUTES processAttributes,
                          SECURITY_ATTRIBUTES threadAttributes,
                          boolean inheritHandles,
                          int creationFlags,
                          Pointer environment,
                          String currentDirectory,
                          STARTUPINFO startupinfo,
                          PROCESS_INFORMATION processInformation);

    boolean TerminateProcess(HANDLE process, int exitCode);

    int WaitForSingleObject(HANDLE handle, int timeoutMs);

    int INFINITE = 0xFFFFFFFF;

    int WAIT_ABANDONED = 0x00000080;
    int WAIT_OBJECT_0 = 0x00000000;
    int WAIT_TIMEOUT = 0x00000102;
    int WAIT_FAILED = 0xFFFFFFFF;

    boolean GetExitCodeProcess(HANDLE process, IntByReference exitCode);

    int GetCurrentThreadId();

    boolean CloseHandle(HANDLE handle);

    public static class SECURITY_ATTRIBUTES extends Structure {
        int length;
        Pointer securityDescriptor;
        boolean inheritHandle;
    }
}
