package com.objogate.wl.driver.tests;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import junit.framework.Assert;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import com.objogate.wl.driver.JFileChooserDriver;
import com.objogate.wl.probe.ComponentIdentity;
import com.objogate.wl.probe.RecursiveComponentFinder;

public class JFileChooserDriverTest extends AbstractComponentDriverTest<JFileChooserDriver> {
    private JFileChooser chooser;

    @Before
    public void setUp() throws Exception {
        view(new JLabel("some label"));
        chooser = new JFileChooser();
        driver = new JFileChooserDriver(gesturePerformer, chooser);
    }

    public void testPrintOutSubComponents() throws InterruptedException {
        final int[] results = new int[]{-999};
        final CountDownLatch latch = showChooserInAnotherThreadBecauseItsModal(results, "Go");

        ComponentIdentity<Component> parentOrOwnerFinder = new ComponentIdentity<Component>(chooser);
        RecursiveComponentFinder<Component> finder = new RecursiveComponentFinder<Component>(Component.class, Matchers.anything(), parentOrOwnerFinder);
        finder.probe();
        for (Component component : finder.components()) {
            System.out.println("component = " + component);
        }
        driver.cancel();

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testCanClickCancelButton() throws InterruptedException {
        final int[] results = new int[]{-999};
        final CountDownLatch latch = showChooserInAnotherThreadBecauseItsModal(results, "Go");

        driver.cancel();

        latch.await(5, TimeUnit.SECONDS);

        assertThat(results[0], equalTo(JFileChooser.CANCEL_OPTION));
    }

    @Test
    public void testCanClickDefaultOkButton() throws InterruptedException {
        final int[] results = new int[]{-999};
        final CountDownLatch latch = showChooserInAnotherThreadBecauseItsModal(results, null);

        driver.enterManually("whatever");
        driver.ok();

        latch.await(5, TimeUnit.SECONDS);

        assertThat(results[0], equalTo(JFileChooser.APPROVE_OPTION));
    }

    @Test
    public void testWillTypeGivenTextIntoTextbox() throws InterruptedException {
        final int[] results = new int[]{-999};
        final CountDownLatch latch = showChooserInAnotherThreadBecauseItsModal(results, null);

        driver.enterManually("somethingorother");
        driver.ok();

        latch.await(5, TimeUnit.SECONDS);

        assertThat(chooser.getSelectedFile().getName(), equalTo("somethingorother"));
        assertThat(results[0], equalTo(JFileChooser.APPROVE_OPTION));
    }

    @Test
    public void testCanClickOnTheHomeButton() {
        showChooserInAnotherThreadBecauseItsModal(new int[]{-999}, null);

        driver.upOneFolder();
        driver.upOneFolder();

        driver.currentDirectory(Matchers.not(Matchers.equalTo(System.getProperty("user.home"))));
        driver.home();
        driver.currentDirectory(Matchers.equalTo(System.getProperty("user.home")));
    }

    @Test
    public void testCanCreateNewFolder() throws InterruptedException {
        String folderName = "testfolder";
        File fileForFolder = new File(System.getProperty("user.home"), folderName);

        try {
            final int[] results = new int[]{-999};
            final CountDownLatch latch = showChooserInAnotherThreadBecauseItsModal(results, null);

            driver.home();
            driver.createNewFolder(folderName);

            latch.await(5, TimeUnit.SECONDS);

            Assert.assertTrue("Folder " + fileForFolder.getAbsolutePath() + " exists", fileForFolder.exists());
        }
        finally {
            fileForFolder.delete();
        }
    }

    public void testCanSelectAFile() throws InterruptedException, IOException {
        String fileName = "testFile1234567890";
        File file = new File(System.getProperty("user.home"), fileName);

       file.createNewFile();

        try {
            final int[] results = new int[]{-999};
            final CountDownLatch latch = showChooserInAnotherThreadBecauseItsModal(results, null);

            driver.home();
            driver.selectFile(fileName);

            latch.await(5, TimeUnit.SECONDS);

            assertThat(chooser.getSelectedFile().getName(), equalTo(fileName));
        }
        finally {
            file.delete();
        }
    }

    private CountDownLatch showChooserInAnotherThreadBecauseItsModal(final int[] results, final String approveButtonText) {
        final CountDownLatch latch = new CountDownLatch(1);

        new Thread(new Runnable() {
            public void run() {
                results[0] = chooser.showDialog(frame, approveButtonText);
                latch.countDown();
            }
        }).start();
        return latch;
    }
}
