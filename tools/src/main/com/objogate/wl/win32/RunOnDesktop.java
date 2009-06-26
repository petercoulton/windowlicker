package com.objogate.wl.win32;

import com.sun.jna.ptr.IntByReference;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.objogate.wl.win32.AccessRights;
import com.objogate.wl.win32.Kernel32;
import com.objogate.wl.win32.User32;
import static com.objogate.wl.win32.Kernel32.*;
import static com.objogate.wl.win32.User32.*;

public class RunOnDesktop {
    static final User32 user32 = User32.INSTANCE;
    static final Kernel32 kernel32 = Kernel32.INSTANCE;

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 2) {
            System.err.println("usage: " + System.getProperty("app.name", RunOnDesktop.class.getName()) + " <desktop> <command> [<command> ...]");
            System.exit(1);
        }
        
        String desktopName = args[0];
        
        HDESK offscreenDesktop = user32.CreateDesktop(desktopName, null, null, 0, AccessRights.MAXIMUM_ALLOWED, null);
        if (offscreenDesktop == null) {
            fail("could not create desktop " + desktopName);
        }

        List<PROCESS_INFORMATION> processes = startProcessesOnDesktop(desktopName, args);
        waitForProcessesToFinish(args, processes);
    }

    private static List<PROCESS_INFORMATION> startProcessesOnDesktop(String desktopName, String[] args) throws IOException {
        STARTUPINFO startupinfo = new STARTUPINFO();
        startupinfo.clear();
        startupinfo.size = startupinfo.size();
        startupinfo.desktop = desktopName;

        List<PROCESS_INFORMATION> processes = new ArrayList<PROCESS_INFORMATION>(args.length-1);
        for (int i = 1; i < args.length; i++) {
            PROCESS_INFORMATION processInformation = new PROCESS_INFORMATION();
            processInformation.clear();

            boolean processCreated = kernel32.CreateProcess(
                null,
                args[i],
                null,
                null,
                false,
                0,
                null,
                null,
                startupinfo,
                processInformation);

            if (processCreated) {
                processes.add(processInformation);
            } else {
                terminateAll(processes, 0);
                fail("could not create process: " + args[i]);
            }
        }
        return processes;
    }

    private static void waitForProcessesToFinish(String[] args, List<PROCESS_INFORMATION> processes) throws IOException {
        for (int i = 0; i < processes.size(); i++) {
            String commandLine = args[i + 1];
            if (!waitForProcessToFinish(processes.get(i), commandLine)) {
                terminateAll(processes, i);
                break;
            }
        }
    }

    private static boolean waitForProcessToFinish(PROCESS_INFORMATION processInformation, String commandLine) throws IOException {
        int status = kernel32.WaitForSingleObject(processInformation.hProcess, Kernel32.INFINITE);
        if (status != Kernel32.WAIT_OBJECT_0) {
            warn("did not get woken by end of process: " + commandLine + " (status = " + status + ")");
            return false;
        }

        IntByReference exitCode = new IntByReference();
        if (!kernel32.GetExitCodeProcess(processInformation.hProcess, exitCode)) {
            warn("could not get exit code of process: " + commandLine);
            return false;
        }

        System.out.println(Integer.toHexString(exitCode.getValue()).toUpperCase() + ": " + commandLine);

        closeHandles(processInformation);

        return true;
    }

    private static void terminateAll(List<PROCESS_INFORMATION> processes, int fromIndex) {
        for (int i = fromIndex; i < processes.size(); i++) {
            PROCESS_INFORMATION process = processes.get(i);
            kernel32.TerminateProcess(process.hProcess, 0);
            closeHandles(process);
        }
    }

    private static void closeHandles(PROCESS_INFORMATION processInformation) {
        kernel32.CloseHandle(processInformation.hThread);
        kernel32.CloseHandle(processInformation.hProcess);
    }

    private static void warn(String reason) {
        System.err.println(reason);
    }

    private static void fail(String reason) throws IOException {
        System.err.println(reason);
        System.exit(1);
    }
}

