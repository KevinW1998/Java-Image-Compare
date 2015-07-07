
package org.kevsoft.imagecompare;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * This class is the base for all image compare algorithm.
 * @author Waldock
 * @version 05.07.2015
 */
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
			return ImageUtils.transformTo4ByteARGB(optimizeImage(firstImage));
		}else{
			return ImageUtils.transformTo4ByteARGB(getFirstSourceImage());
		}
	}
	
	public BufferedImage getSecondOptimizedImage(){
		if(needOptimization()){
			return ImageUtils.transformTo4ByteARGB(optimizeImage(secondImage));
		}else{
			return ImageUtils.transformTo4ByteARGB(getSecondSourceImage());
		}
		
	}
	
}
