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
    private static final String RECORDINGS = "build/tests/recordings";
    private final RecordMyDesktopWrapper desktopRecorder = new RecordMyDesktopWrapper(RECORDINGS);
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
        try {
            currentSuit = suite.getName();
            failures.clear();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void endTestSuite(JUnitTest suite) throws BuildException {
        try {
            for (File failure : failures) {
                try {
                    copyFile(failure, new File(FAILED_TEST_RECORDINGS, failure.getName()));
                } catch (IOException e) {
                    throw new BuildException("Cannot copy " + failure);
                }
            }
        } catch (BuildException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void startTest(Test test) {
        try {
            JUnit4TestCaseFacade facade = (JUnit4TestCaseFacade) test;
            String testName = testName(facade);
            currentRecording = startRecording(currentSuit + "." + testName);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private String testName(JUnit4TestCaseFacade facade) {
        String s = facade.getDescription().toString();
        return s.substring(0, s.indexOf("("));
    }

    public void addError(Test test, Throwable throwable) {
        try {
            if (currentRecording != null) {
                failures.add(currentRecording);
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void addFailure(Test test, AssertionFailedError assertionFailedError) {
        try {
            if (currentRecording != null) {
                failures.add(currentRecording);
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void endTest(Test test) {
        System.out.println("RecordingFormatter.endTest " + test);
        try {
            desktopRecorder.stop();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private File startRecording(String name) {
        if (Platform.is(Platform.Linux)) {
            return desktopRecorder.start(name + ".ogg");
        } else {
            File file = new File(RECORDINGS, name + ".crap");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return file;
        }
    }

    private static void copyFile(File sourceFile, File destFile) throws IOException {
        System.out.println(sourceFile.getAbsolutePath());
        System.out.println(destFile.getAbsolutePath());
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
