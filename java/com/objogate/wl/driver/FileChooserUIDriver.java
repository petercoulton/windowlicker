package com.objogate.wl.driver;

import javax.swing.text.JTextComponent;

public interface FileChooserUIDriver {
    void createNewFolder(String folderName);

    void upOneFolder();

    void home();

    void desktop();

    void documents();

    void cancel();

    void approve();

    JTextComponentDriver<? extends JTextComponent> textBox();

    void selectFile(String fileName);

    void intoDir(String directoryName);
}
