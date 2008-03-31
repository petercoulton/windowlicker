package com.objogate.wl.build;


import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import junit.framework.AssertionFailedError;
import junit.framework.JUnit4TestCaseFacade;
import junit.framework.Test;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitResultFormatter;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;
import com.objogate.wl.Platform;

public class RecordingFormatter implements JUnitResultFormatter {
    private static final File FAILED_TEST_RECORDINGS = new File("build/artifacts/reports/tests/recordings");
    private final RecordMyDesktopWrapper desktopRecorder = new RecordMyDesktopWrapper("build/tests/recordings");
    private final List<File> failures = new ArrayList<File>();
    private File currentRecording;
    private String currentSuit;

    public RecordingFormatter() {
        FAILED_TEST_RECORDINGS.mkdirs();
    }

    public void setOutput(OutputStream out) {
    }

    public void setSystemOutput(String out) {
    }

    public void setSystemError(String err) {
    }

    public void startTestSuite(JUnitTest suite) throws BuildException {
        currentSuit = suite.getName();
        failures.clear();
    }

    public void endTestSuite(JUnitTest suite) throws BuildException {
        for (File failure : failures) {
            try {
                copyFile(failure, new File(FAILED_TEST_RECORDINGS, failure.getName()));
            } catch (IOException e) {
                throw new BuildException("Cannot copy " + failure);
            }
        }
    }

    public void startTest(Test test) {
        JUnit4TestCaseFacade facade = (JUnit4TestCaseFacade) test;
        String testName = testName(facade);
        currentRecording = startRecording(currentSuit + "." + testName);
    }

    private String testName(JUnit4TestCaseFacade facade) {
        String s = facade.getDescription().toString();
        return s.substring(0, s.indexOf("("));
    }

    public void addError(Test test, Throwable throwable) {
        if (currentRecording != null) {
            failures.add(currentRecording);
        }
    }

    public void addFailure(Test test, AssertionFailedError assertionFailedError) {
        if (currentRecording != null) {
            failures.add(currentRecording);
        }
    }

    public void endTest(Test test) {
        desktopRecorder.stop();
    }

    private File startRecording(String name) {
        if (Platform.is(Platform.Linux)) {
            return desktopRecorder.start(name + ".ogg");
        } else {
            return null;
        }
    }

    private static void copyFile(File sourceFile, File destFile) throws IOException {
        FileChannel source = new FileInputStream(sourceFile).getChannel();
        try {
            FileChannel destination = new FileOutputStream(destFile).getChannel();
            try {
                destination.transferFrom(source, 0, source.size());
            } finally {
                if (destination != null) {
                    destination.close();
                }
            }
        } finally {
            source.close();
        }
    }
}
