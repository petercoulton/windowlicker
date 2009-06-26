package com.objogate.wl.win32;

import javax.imageio.ImageIO;
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;


public class DumpScreenCapture {
    public static void main(String[] args) throws AWTException, IOException {
        Robot robot = new Robot();
        BufferedImage capture = robot.createScreenCapture(new Rectangle(new Point(0, 0), Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(capture, "png", new File("capture.png"));
    }
}
