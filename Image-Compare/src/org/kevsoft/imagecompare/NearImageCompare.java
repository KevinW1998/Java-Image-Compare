package org.kevsoft.imagecompare;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class NearImageCompare extends ImageCompare {
	public NearImageCompare(BufferedImage img1, BufferedImage img2){
		setFirstSourceImage(img1);
		setSecondSourceImage(img2);
	}
	
	public int compare(){
		WritableRaster wImg1 = getFirstOptimizedImage().copyData(null);
		WritableRaster wImg2 = getSecondOptimizedImage().copyData(null);
		
		int pixels1[] = new int[wImg1.getWidth()*wImg1.getHeight()*4];
		wImg1.getPixels(0, 0, wImg1.getWidth(), wImg1.getHeight(), pixels1);
		
		int pixels2[] = new int[wImg2.getWidth()*wImg2.getHeight()*4];
		wImg2.getPixels(0, 0, wImg2.getWidth(), wImg2.getHeight(), pixels2);
		
		
		double durchschnittRot;
		double durchschnittGrün;
		double durchschnittBlau;
		
		return 0; //Im moment 0
	}
}
