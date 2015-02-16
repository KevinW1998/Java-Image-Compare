package org.kevsoft.test;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.kevsoft.imagecompare.NearImageCompare;
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
       }catch(Exception e){
    	   System.out.println("Failed to compare images, different sizes?");
       }
       System.out.println("==============");
   }
   public static void main(String[] args)  {
       compare ("mercedes.jpg", "mercedes.jpg");
       compare ("mercedes.jpg", "motorrad.jpg");
       compare ("motorrad.jpg", "mercedes.jpg");
       compare ("motorrad.jpg", "motorrad.jpg");

   }
}