package org.kevsoft.test;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;


public class TestClass {

 
   public static void main(String[] args) throws Exception {
       BufferedImage imageA = ImageIO.read(new File("C:\\User\\Larry Liu\\Pictures\\mercedes.jpg"));
       BufferedImage imageB = ImageIO.read(new File("C:\\User\\Larry Liu\\Pictures\\mercedes.jpg"));

       long time = -System.currentTimeMillis();

       int[] pixelOfA = new int[imageA.getWidth() * imageA.getHeight()];
       imageA.getData().getPixel(0, 0, pixelOfA);

       int[] pixelOfB = new int[imageB.getWidth() * imageB.getHeight()];
       imageB.getData().getPixel(0, 0, pixelOfB);

       System.out.println(Arrays.equals(pixelOfA, pixelOfB));

       System.out.println(time + System.currentTimeMillis() + " ms");

   }
}