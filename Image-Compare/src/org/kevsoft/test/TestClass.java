package org.kevsoft.test;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
/**
 * Ganz simples Programm von mir, welches zwei gleiche Bilder auf Gleichheit �berpr�ft
 * @author Larry Liu
 *
 */

public class TestClass {

	public static boolean vergleicheArrays(int[] array1, int[] array2) { //vergleicht zwei Arrays auf Gleichheit
	    boolean b = false;
	    for (int i = 0; i < array2.length; i++) {

	        for (int a = 0; a < array1.length; a++) {

	            if (array2[i] == array1[a]) {
	                b = true;
	                
	            } else {
	                b = false;
	                
	                break;
	            }
	        }
	    }
	    return b;
	}
	

   public static void compare (String imagePfadA, String imagePfadB){ //vergleicht zwei Arrays auf Gleichheit
	   BufferedImage imageA;
       BufferedImage imageB;
       int[] pixelOfA;
       int[] pixelOfB;
       int RGBarray[];
       int RGBarray2[];
    		   
       try {
    	   imageA = ImageIO.read(new File(imagePfadA)); // Einscannen der Bilder
    	   imageB = ImageIO.read(new File(imagePfadB));
    	   
    	   pixelOfA = new int[imageA.getWidth() * imageA.getHeight()]; //Array, welches so gro� ist wie die Anzahl der Pixel
	       pixelOfB = new int[imageB.getWidth() * imageB.getHeight()];
	       
		   
	
	    	   for (int i=0; i<imageA.getHeight();i++){
	    		   
	    		   RGBarray = new int [imageA.getHeight()]; //Array, mit RGB Werten
	    		   RGBarray2 = new int [imageB.getHeight()];
	    		   imageA.getData().getPixel(i, i, pixelOfA);
	    		   imageB.getData().getPixel(i, i, pixelOfB);
	    		 
	    		   boolean wert= vergleicheArrays (RGBarray,RGBarray2);
	    		   
	    		   for (int y=0; y<RGBarray.length;y++){ //F�llt das Array mit RGB Werten von Bild A
	    		   		RGBarray[y] = imageA.getRGB(y, y);
	    		   	
	    		   }
	    		   for (int z=0; z<RGBarray2.length;z++){ //F�llt das Array mit RGB Werten von Bild B
	    		   		RGBarray2[z] = imageB.getRGB(z, z);
	    		   	
	    		   }
	    		  
	    		   if (Arrays.equals(pixelOfA, pixelOfB)&& wert ){ //Nur wenn die Pixel & die Werte im RGB Array �bereinstimmen ist das Bild ident
	    			   JOptionPane.showMessageDialog(null, "Bilder sind ident");
	    			   break;
	    		   }
	    		   else  {
	    			   JOptionPane.showMessageDialog(null, "Bilder sind verschieden");
	    			   break;
	    		   }
	    	  }
	    
       } catch (IOException e) {
		// TODO Auto-generated catch block
    	   JOptionPane.showMessageDialog(null, "Bilder konnten nicht gefunden werden");
      
       }
       
   }
   public static void main(String[] args)  {
       compare ("motorrad.jpg", "motorrad.jpg");

   }
}