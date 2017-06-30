package com.cjvnjde;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by nvjrsgu on 6/29/2017.
 */
public class ComputerController {

    public static void shutdownComputer(){
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec("shutdown -s -t 0");
        } catch (IOException e) {
            System.out.println("Can't shutdown PC");
            e.printStackTrace();
        }
    }

    public static BufferedImage createScreenCapture(){
        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rectangle = new Rectangle(sSize);
        try {
            return new Robot().createScreenCapture(rectangle);
        } catch (AWTException e) {
            System.out.println("Can't create screen capture");
            e.printStackTrace();
            return null;
        }
    }


}
