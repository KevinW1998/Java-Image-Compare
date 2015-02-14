package org.kevsoft.imagecompare;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

public abstract class ImageCompare {
	//Fields
	private BufferedImage firstImage;
	private BufferedImage secondImage;
	//Fields Optimization
	private double scalefactor;
	private Dimension sizeScale;
	
	//private
	private BufferedImage optimizeImage(BufferedImage img){
		BufferedImage opImg = img;
		if(sizeScale!=null){
			opImg = ImageUtils.scaleToFixSize(sizeScale, opImg);
		}
		if(scalefactor!=1.0){
			opImg = ImageUtils.scale(scalefactor, opImg);
		}
		return opImg;
	}
	private boolean needOptimization(){
		return scalefactor == 1.0 || 
				sizeScale != null;
	}
	
	//Normal Getter Setter
	public BufferedImage getFirstSourceImage() {
		return firstImage;
	}
	public void setFirstSourceImage(BufferedImage firstImage) {
		this.firstImage = firstImage;
	}
	public BufferedImage getSecondSourceImage() {
		return secondImage;
	}
	public void setSecondSourceImage(BufferedImage secondImage) {
		this.secondImage = secondImage;
	}
	public double getScalefactor() {
		return scalefactor;
	}
	public void setScale(double scalefactor) {
		this.scalefactor = scalefactor;
	}
	public Dimension getSizeScale() {
		return sizeScale;
	}
	public void setSizeScale(Dimension sizeScale) {
		this.sizeScale = sizeScale;
	}
	
	//Special Getter/Setter
	public BufferedImage getFirstOptimizedImage(){
		if(needOptimization()){
			return optimizeImage(firstImage);
		}else{
			return getFirstSourceImage();
		}
	}
	
	public BufferedImage getSecondOptimizedImage(){
		if(needOptimization()){
			return optimizeImage(secondImage);
		}else{
			return getSecondSourceImage();
		}
		
	}
	
}
