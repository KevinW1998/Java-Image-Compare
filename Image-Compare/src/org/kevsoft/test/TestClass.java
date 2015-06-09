
package org.kevsoft.test;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

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
	   try {
		   pdiff = new PdiffImageCompare(ImageIO.read(new File(imagePfadA)), ImageIO.read(new File(imagePfadB)));
		   pdiff.setSizeScale(new Dimension(600, 600));
	   }catch(Exception e) {
		   e.printStackTrace();
    	   return;
	   }
	   System.out.println("==============");
	   System.out.println("==============");
       try{
    	   
    	   System.out.println(imagePfadA + " <=> " + imagePfadB);
    	   if(pdiff.compare()) {
    		   System.out.println("SUCCESS");
    	   } else {
    		   System.out.println("FAILED");
    	   }
       }catch(Exception e){
    	   e.printStackTrace();
    	   System.out.println("Failed to compare images, different sizes?");
       }
       System.out.println("==============");
	   
   }
   
   
   public static void main(String[] args)  {
	   pdiffCompare ("00ff00.png", "ff0000.png");
	   pdiffCompare ("mercedes.jpg", "motorrad.jpg");
	   pdiffCompare ("motorrad.jpg", "mercedes.jpg");
	   pdiffCompare ("motorrad.jpg", "motorrad.jpg");

   }
}