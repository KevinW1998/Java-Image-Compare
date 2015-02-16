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
		
		int pixels1[] = ImageUtils.getPixels(optimizedImage1);
		int pixels2[] = ImageUtils.getPixels(optimizedImage2);
		
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
	
	public void compareColor() throws Exception{
		BufferedImage optimizedImage1 = getFirstOptimizedImage();
		BufferedImage optimizedImage2 = getSecondOptimizedImage();
		
		if(optimizedImage1.getWidth() != optimizedImage2.getWidth() ||
				optimizedImage2.getHeight() != optimizedImage2.getHeight()){
			throw new Exception("Different sizes in optimized Image!");
		}
		
		
		WritableRaster wImg1 = getFirstOptimizedImage().copyData(null);
		WritableRaster wImg2 = getSecondOptimizedImage().copyData(null);
		
		int pixels1[] = ImageUtils.getPixels(optimizedImage1);
		int pixels2[] = ImageUtils.getPixels(optimizedImage2);
		
		double pixCount = wImg1.getWidth()*wImg1.getHeight();
		double pixCount1 = wImg2.getWidth()*wImg2.getHeight();
		double rot = 0.0;
		double grün = 0.0;
		double blau = 0.0;
		
		double rot1 = 0.0;
		double grün1 = 0.0;
		double blau1 = 0.0;
		
		for (int redIndex = 0; redIndex < pixels1.length; redIndex=redIndex+4 ){
			rot += pixels1[redIndex];
			rot1+= pixels2[redIndex];
			
		}
		for (int greenIndex = 1; greenIndex < pixels1.length; greenIndex=greenIndex+4 ){
			grün += pixels1[greenIndex];
			grün1 += pixels2[greenIndex];
		}
		for (int blueIndex = 2; blueIndex < pixels1.length; blueIndex=blueIndex+4 ){
			blau += pixels1[blueIndex];
			blau1 += pixels2[blueIndex];
		}

	
		double averageDiffRot = Math.abs( (rot / pixCount)-(rot1 / pixCount1));
		double averageDiffGrün =Math.abs( ( grün / pixCount)-(grün1 / pixCount1));
		double averageDiffBlau = Math.abs( (blau / pixCount)-(blau1 / pixCount1));

		System.out.println ("Similarity of red: "+(100-(averageDiffRot/256) *100) );
		System.out.println ("Similarity of green: " +(100-( averageDiffGrün/256)*100));
		System.out.println ("Similarity of blue: " +(100-(averageDiffBlau/256)*100));
		
	}
		
	}
	

