package com.objogate.wl.build;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.objogate.exception.Defect;

public class RecordMyDesktopWrapper {
    private Process process;
    private final File rootDir;

    public RecordMyDesktopWrapper(String rootDir) {
        this.rootDir = new File(rootDir);
        this.rootDir.mkdirs();
    }

    public File start(String filename) {
        try {
            File file = new File(rootDir, filename);
            file.getParentFile().mkdirs();
            process = Runtime.getRuntime().exec("recordmydesktop --no-sound --on-the-fly-encoding -o " + file.getAbsolutePath());
            return file;
        } catch (IOException e) {
            throw new Defect("Cannot start recording", e);
        }
    }

    public void stop() {
//        try {
            if(process == null)
                return;
//        Signal.raise(new Signal("INT"))

            process.destroy();
//            process.waitFor();
//        } catch (InterruptedException e) {
//            throw new Defect("Not supposed to happen!", e);
//        }
    }

    public static void main(String[] args) throws Exception {
        final RecordMyDesktopWrapper recorder = new RecordMyDesktopWrapper("poo");
        final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(new Runnable() {
            public void run() {
                try {
                    recorder.stop();
                    executorService.shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 10, TimeUnit.SECONDS);

        executorService.shutdown();
    }
}

