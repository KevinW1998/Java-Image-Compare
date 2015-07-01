
package org.kevsoft.test;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.kevsoft.imagecompare.ImageUtils;
import org.kevsoft.imagecompare.NearImageCompare;
import org.kevsoft.imagecompare.PdiffImageCompare;
import org.kevsoft.imagecompare.SimpleImageCompare;

/**
 * Testklasse
 * @author Waldock
 * @author Liu
 *
 */
public class TestClass {

   public static void compare (String imagePfadA, String imagePfadB){ //vergleicht zwei Arrays auf Gleichheit
       NearImageCompare nic;
       try{
    	   nic = new NearImageCompare(ImageIO.read(new File(imagePfadA)), ImageIO.read(new File(imagePfadB)));
    	   nic.setSizeScale(new Dimension(600, 600));
       }catch(Exception e){
    	   e.printStackTrace();
    	   return;
       }
       System.out.println("==============");
       try{
    	   double compareVal = nic.compare();
    	   
    	   System.out.println(imagePfadA + " <=> " + imagePfadB);
    	   System.out.println("Similarity: " + compareVal);
    	   nic.compareColor();
       }catch(Exception e){
    	   e.printStackTrace();
    	   //System.out.println("Failed to compare images, different sizes?");
       }
       System.out.println("==============");
   }
   
   public static void pdiffCompare(String imagePfadA, String imagePfadB) {
	   PdiffImageCompare pdiff;   
	   BufferedImage img1;
	   BufferedImage img2;
	   try {
		   img1 = ImageIO.read(new File(imagePfadA));
		   img2 = ImageIO.read(new File(imagePfadB));
		   pdiff = new PdiffImageCompare(img1, img2);
		   //pdiff.setSizeScale(new Dimension(600, 600));
	   }catch(Exception e) {
		   e.printStackTrace();
    	   return;
	   }
	   
	   pdiff.setFov(15.0);
	   pdiff.setColorfactor(1.0);
	   
	   BufferedImage img = pdiff.getFirstOptimizedImage();
	   int pixels = img.getWidth() * img.getHeight();
	   
	   System.out.println("==============");   
	   System.out.println(imagePfadA + " <=> " + imagePfadB);
	   int failedPixels = pdiff.compare();
	   System.out.println("Failed pixels: " + failedPixels);
	   System.out.println("Total pixels: "  + pixels);
       System.out.println("==============");
	   
   }
   
   
   public static void main(String[] args)  {
	   pdiffCompare ("mercedes.jpg", "mercedes1.jpg");

   }
}