
package org.kevsoft.imagecompare;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * This class is the base for all image compare algorithm.
 * 
 * @author Waldock
 * @version 05.07.2015
 */
public abstract class ImageCompare {
	// Fields
	private BufferedImage firstImage;
	private BufferedImage secondImage;
	// Fields Optimization
	private double scalefactor;
	private Dimension sizeScale;

	// CACHE
	private boolean forceRenderFirstImage;
	private boolean forceRenderSecondImage;
	private BufferedImage cachedFirstImage;
	private BufferedImage cachedSecondImage;

	// private
	private BufferedImage optimizeImage(BufferedImage img) {
		BufferedImage opImg = img;
		if (sizeScale != null) {
			opImg = ImageUtils.scaleToFixSize(sizeScale, opImg);
		}
		if (scalefactor != 1.0) {
			opImg = ImageUtils.scale(scalefactor, opImg);
		}
		return opImg;
	}

	private boolean needOptimization() {
		return scalefactor != 1.0 || sizeScale != null;
	}

	// Constructor
	public ImageCompare() {
		this(null, null);
	}

	public ImageCompare(BufferedImage firstImage, BufferedImage secondImage) {
		this.firstImage = firstImage;
		this.secondImage = secondImage;

		scalefactor = 1.0;
		sizeScale = null;

		// Cache:
		forceRenderFirstImage = true; // Be sure to force render on the first
										// time;
		forceRenderSecondImage = true;
		cachedFirstImage = null;
		cachedSecondImage = null;
	}

	// Normal Getter Setter
	public BufferedImage getFirstSourceImage() {
		return firstImage;
	}

	public void setFirstSourceImage(BufferedImage firstImage) {
		forceRenderFirstImage = forceRenderFirstImage || this.firstImage != firstImage;
		this.firstImage = firstImage;
	}

	public BufferedImage getSecondSourceImage() {
		return secondImage;
	}

	public void setSecondSourceImage(BufferedImage secondImage) {
		forceRenderSecondImage = forceRenderSecondImage || this.secondImage != secondImage;
		this.secondImage = secondImage;
	}

	public double getScalefactor() {
		return scalefactor;
	}

	public void setScale(double scalefactor) {
		forceRenderFirstImage = forceRenderFirstImage || this.scalefactor == scalefactor;
		forceRenderSecondImage = forceRenderSecondImage || this.scalefactor == scalefactor;
		this.scalefactor = scalefactor;
	}

	public Dimension getSizeScale() {
		return sizeScale;
	}

	public void setSizeScale(Dimension sizeScale) {
		forceRenderFirstImage = forceRenderFirstImage || (this.sizeScale.width != sizeScale.width || this.sizeScale.height != sizeScale.height);
		forceRenderSecondImage = forceRenderSecondImage || (this.sizeScale.width != sizeScale.width || this.sizeScale.height != sizeScale.height);
		this.sizeScale = sizeScale;
	}

	// Special Getter
	public BufferedImage getFirstOptimizedImage() {
		if (forceRenderFirstImage || cachedFirstImage == null) {
			if (needOptimization()) {
				cachedFirstImage = ImageUtils.transformTo4ByteARGB(optimizeImage(firstImage));
			} else {
				cachedFirstImage = ImageUtils.transformTo4ByteARGB(getFirstSourceImage());
			}
			forceRenderFirstImage = false;
		}
		return cachedFirstImage;
	}

	public BufferedImage getSecondOptimizedImage() {
		if (forceRenderSecondImage || cachedSecondImage == null) {
			if (needOptimization()) {
				cachedSecondImage = ImageUtils.transformTo4ByteARGB(optimizeImage(secondImage));
			} else {
				cachedSecondImage = ImageUtils.transformTo4ByteARGB(getSecondSourceImage());
			}
			forceRenderSecondImage = false;
		}
		return cachedSecondImage;
	}
}
