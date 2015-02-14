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
				
		double anzahlDerPxImg= wImg1.getWidth()*wImg1.getHeight();

		double durchschnittRot=0;
		double durchschnittGrün=0;
		double durchschnittBlau=0;
		double durchschnittRot1=0;
		double durchschnittGrün1=0;
		double durchschnittBlau1=0;
		
		for (int i = 0; i< pixels1.length; i= i+ 4 ){
			durchschnittRot=durchschnittRot + pixels1[i];
			durchschnittRot1=durchschnittRot1 + pixels2[i];
		}
		for (int x = 1; x< pixels1.length; x= x+ 4 ){
			durchschnittGrün=durchschnittGrün + pixels1[x];
			durchschnittGrün1=durchschnittGrün1 + pixels2[x];
		}
		for (int y= 1; y< pixels1.length; y= y+ 4 ){
			durchschnittBlau=durchschnittBlau + pixels1[y];
			durchschnittBlau1=durchschnittBlau1 + pixels2[y];
		}
		durchschnittRot = durchschnittRot / anzahlDerPxImg;
		durchschnittGrün = durchschnittGrün / anzahlDerPxImg;
		durchschnittBlau = durchschnittBlau / anzahlDerPxImg;
		durchschnittRot1 = durchschnittRot1 / anzahlDerPxImg;
		durchschnittGrün1 = durchschnittGrün1 / anzahlDerPxImg;
		durchschnittBlau1 = durchschnittBlau1 / anzahlDerPxImg;
		return 0; //Im moment 0
	}
}
