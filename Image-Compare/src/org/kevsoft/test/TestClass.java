package org.kevsoft.test;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.kevsoft.imagecompare.SimpleImageCompare;

/**
 * Testklasse
 * @author Waldock
 * @author Liu
 *
 */
public class TestClass {

   public static void compare (String imagePfadA, String imagePfadB){ //vergleicht zwei Arrays auf Gleichheit
       SimpleImageCompare sic;
       try{
    	   sic = new SimpleImageCompare(ImageIO.read(new File(imagePfadA)), ImageIO.read(new File(imagePfadB)));
       }catch(IOException e){
    	   e.printStackTrace();
    	   return;
       }
       if(sic.compare()){
    	   JOptionPane.showMessageDialog(null, "Die Bilder sind gleich!");
       }else{
    	   JOptionPane.showMessageDialog(null, "Die Bilder sind ungleich!");
       }
   }
   public static void main(String[] args)  {
       compare ("mercedes.jpg", "mercedes.jpg");
       compare ("mercedes.jpg", "motorrad.jpg");
       compare ("motorrad.jpg", "mercedes.jpg");
       compare ("motorrad.jpg", "motorrad.jpg");

   }
}