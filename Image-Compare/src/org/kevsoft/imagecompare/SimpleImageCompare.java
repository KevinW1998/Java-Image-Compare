package org.kevsoft.imagecompare;

import java.awt.image.BufferedImage;
import java.util.Arrays;


public class SimpleImageCompare extends ImageCompare {
	public SimpleImageCompare(BufferedImage img1, BufferedImage img2){
		setFirstSourceImage(img1);
		setSecondSourceImage(img2);
	}
	
	public boolean compare(){
		BufferedImage imageA;
		BufferedImage imageB;
		int[] pixelsOfA;
		int[] pixelsOfB;

		imageA = getFirstSourceImage();
		imageB = getSecondSourceImage();
	   
		pixelsOfA = new int[imageA.getWidth() * imageA.getHeight()]; //Array, welches so groﬂ ist wie die Anzahl der Pixel
		pixelsOfB = new int[imageB.getWidth() * imageB.getHeight()];
	  
		for (int x=0; x<imageA.getWidth();x++){ //F¸llt das Array mit RGB Werten von Bild A
			for (int y = 0; y < imageB.getHeight(); y++){
				pixelsOfA[x*imageA.getWidth() + y] = imageA.getRGB(x, y);
			}
		}
		for (int x=0; x<imageB.getWidth();x++){ //F¸llt das Array mit RGB Werten von Bild B
			for (int y = 0; y < imageB.getHeight(); y++){
				pixelsOfB[x*imageB.getWidth() + y] = imageB.getRGB(x, y);
			}
		}
		return Arrays.equals(pixelsOfA, pixelsOfB);
	}
}
