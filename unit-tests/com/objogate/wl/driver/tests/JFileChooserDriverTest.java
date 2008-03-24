package com.objogate.wl.driver.tests;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import java.awt.Component;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import junit.framework.Assert;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import org.junit.Before;
import org.junit.Test;
import com.objogate.wl.driver.JFileChooserDriver;
import com.objogate.wl.probe.ComponentIdentity;
import com.objogate.wl.probe.RecursiveComponentFinder;

public class JFileChooserDriverTest extends AbstractComponentDriverTest<JFileChooserDriver> {
    private JFileChooser chooser;
    private File testFile = new File("build/parent.dir/child.dir/", "test.file");

    @Before
    public void setUp() throws Exception {
        view(new JLabel("some label"));
        File dir = testFile.getParentFile();
        delete(dir);
        dir.mkdirs();
        testFile.createNewFile();

        chooser = new JFileChooser(dir);
        chooser.setControlButtonsAreShown(true);
        driver = new JFileChooserDriver(gesturePerformer, chooser);
    }

    private void delete(File dir) {
        File[] files = dir.listFiles();
        if (files != null)
            for (File file : files) {
                if (file.isDirectory())
                    delete(file);

                file.delete();
            }
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
    @Problematic(platform = Platform.Mac, why = "The OK button isn't enabled without selecting a file")
    public void testCanClickDefaultApproveButton() throws InterruptedException {
        final int[] results = new int[]{-999};
        final CountDownLatch latch = showChooserInAnotherThreadBecauseItsModal(results, null);

        driver.enterManually("whatever");
        driver.approve();

        latch.await(5, TimeUnit.SECONDS);

        assertThat(results[0], equalTo(JFileChooser.APPROVE_OPTION));
    }

    @Test
    public void testCanClickNamedApproveButton() throws InterruptedException {
        final int[] results = new int[]{-999};
        final CountDownLatch latch = showChooserInAnotherThreadBecauseItsModal(results, "Go");

        driver.enterManually("whatever");
        driver.approve();

        latch.await(5, TimeUnit.SECONDS);

        assertThat(results[0], equalTo(JFileChooser.APPROVE_OPTION));
    }

    @Test
    public void testCanSelectFile() throws Exception {
        final int[] results = new int[]{-999};
        final CountDownLatch latch = showChooserInAnotherThreadBecauseItsModal(results, "Go");

        driver.selectFile(testFile.getName());
        driver.approve();

        latch.await(5, TimeUnit.SECONDS);

        assertThat(results[0], equalTo(JFileChooser.APPROVE_OPTION));
        assertThat(chooser.getSelectedFile().getName(), equalTo(testFile.getName()));
    }

    @Test
    public void testWillTypeGivenTextIntoTextbox() throws InterruptedException {
        final int[] results = new int[]{-999};
        final CountDownLatch latch = showChooserInAnotherThreadBecauseItsModal(results, "Go");

        driver.enterManually("somethingorother");
        driver.approve();

        latch.await(5, TimeUnit.SECONDS);

        assertThat(chooser.getSelectedFile().getName(), equalTo("somethingorother"));
        assertThat(results[0], equalTo(JFileChooser.APPROVE_OPTION));
    }

    @Test
    public void testCanNavigateDirectories() throws InterruptedException {
        showChooserInAnotherThreadBecauseItsModal(new int[]{-999}, "Go");

        driver.upOneFolder();
        driver.upOneFolder();

        driver.currentDirectory(not(Matchers.equalTo(testFile.getParentFile().getParentFile().getParentFile().getName())));
    }

    @Test
    @Problematic(platform = Platform.Mac, why = "there is no home button on a mac file chooser UI")
    public void testCanClickOnTheHomeButton() throws InterruptedException {
        showChooserInAnotherThreadBecauseItsModal(new int[]{-999}, null);

        driver.upOneFolder();
        driver.upOneFolder();

        driver.currentDirectory(not(Matchers.equalTo(System.getProperty("user.home"))));
        driver.home();
        driver.currentDirectory(Matchers.equalTo(System.getProperty("user.home")));
    }

    @Test
    public void testCanCreateNewFolder() throws InterruptedException {
        File testFolder = new File(testFile.getParentFile(), "test.folder");
        final int[] results = new int[]{-999};
        final CountDownLatch latch = showChooserInAnotherThreadBecauseItsModal(results, "Go");

        driver.createNewFolder(testFolder.getName());

        latch.await(5, TimeUnit.SECONDS);

        Assert.assertTrue("Folder " + testFolder.getAbsolutePath() + " exists", testFolder.exists());
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
