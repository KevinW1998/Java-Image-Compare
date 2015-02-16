package org.kevsoft.imagecompare;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.JOptionPane;

public class NearImageCompare extends ImageCompare {
	public NearImageCompare(BufferedImage img1, BufferedImage img2){
		setFirstSourceImage(img1);
		setSecondSourceImage(img2);
	}
	
	public double compare() throws Exception{
		BufferedImage optimizedImage1 = getFirstOptimizedImage();
		BufferedImage optimizedImage2 = getSecondOptimizedImage();
		
		if(optimizedImage1.getWidth() != optimizedImage2.getWidth() ||
				optimizedImage2.getHeight() != optimizedImage2.getHeight()){
			throw new Exception("Different sizes in optimized Image!");
		}
		
		
		WritableRaster wImg1 = getFirstOptimizedImage().copyData(null);
		WritableRaster wImg2 = getSecondOptimizedImage().copyData(null);
		
		int pixels1[] = new int[wImg1.getWidth()*wImg1.getHeight()*4];
		wImg1.getPixels(0, 0, wImg1.getWidth(), wImg1.getHeight(), pixels1);
		
		int pixels2[] = new int[wImg2.getWidth()*wImg2.getHeight()*4];
		wImg2.getPixels(0, 0, wImg2.getWidth(), wImg2.getHeight(), pixels2);
		
		double pixCount = wImg1.getWidth()*wImg1.getHeight();
		double diffRed = 0.0;
		double diffGreen = 0.0;
		double diffBlue = 0.0;
		
		for (int redIndex = 0; redIndex < pixels1.length; redIndex=redIndex+4 ){
			diffRed += Math.abs(pixels1[redIndex] - pixels2[redIndex]);
		}
		for (int greenIndex = 1; greenIndex < pixels1.length; greenIndex=greenIndex+4 ){
			diffGreen += Math.abs(pixels1[greenIndex] - pixels2[greenIndex]);
		}
		for (int blueIndex = 2; blueIndex < pixels1.length; blueIndex=blueIndex+4 ){
			diffBlue += Math.abs(pixels1[blueIndex] - pixels2[blueIndex]);
		}

		
		double averageDiffRot = diffRed / pixCount;
		double averageDiffGrün = diffGreen / pixCount;
		double averageDiffBlau = diffBlue / pixCount;
		
		return Math.abs((((averageDiffRot + averageDiffGrün + averageDiffBlau) / 3.0 / 256.0) * 100) - 100);
	}
}
