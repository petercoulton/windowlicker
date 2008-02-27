package com.objogate.wl.driver.tests;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Component;

public class ComponentViewer {
    public static JFrame view(Component component) {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel jPanel = new JPanel();
        EmptyBorder paddingToAvoidLittleResizeControlOnMacs = new EmptyBorder(16, 16, 16, 16);
        jPanel.setBorder(paddingToAvoidLittleResizeControlOnMacs);
        jPanel.add(component);
        jFrame.getContentPane().add(jPanel);
        jFrame.setSize(1200, 600);
        jFrame.setVisible(true);
        return jFrame;
    }
}
