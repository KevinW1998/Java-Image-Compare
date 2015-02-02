package org.kevsoft.test;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class TestClass {

 
   public static void main(String[] args)  {
	   
       BufferedImage imageA;
       BufferedImage imageB;
       try {
    	   imageA = ImageIO.read(new File("mercedes.jpg"));
    	   imageB = ImageIO.read(new File("merceAdes.jpg"));
    	   int[] pixelOfA = new int[imageA.getWidth() * imageA.getHeight()];
	       int[] pixelOfB = new int[imageB.getWidth() * imageB.getHeight()];

	       for (int i=0; i<imageA.getHeight();i++){
	    	  
	           imageA.getData().getPixel(i, i, pixelOfA);

	           
	           imageB.getData().getPixel(i, i, pixelOfB);
	           if (Arrays.equals(pixelOfA, pixelOfB)==false){
	        	  JOptionPane.showMessageDialog(null, "Bilder sind verschieden");
	        	  break;
	           }
	           if (Arrays.equals(pixelOfA, pixelOfB)){
	         	  JOptionPane.showMessageDialog(null, "Bilder sind ident");
	         	  break;
	            }
	       }
	       
       } catch (IOException e) {
		// TODO Auto-generated catch block
    	   JOptionPane.showMessageDialog(null, "Bilder konnte nicht gefunden werden");
       }
       

       

   }
}