package org.kevsoft.imagecompare;

import java.awt.image.BufferedImage;

public abstract class ImageCompare {
	private BufferedImage firstImage;
	private BufferedImage secondImage;
	
	public BufferedImage getFirstImage() {
		return firstImage;
	}
	public void setFirstImage(BufferedImage firstImage) {
		this.firstImage = firstImage;
	}
	public BufferedImage getSecondImage() {
		return secondImage;
	}
	public void setSecondImage(BufferedImage secondImage) {
		this.secondImage = secondImage;
	}
}
