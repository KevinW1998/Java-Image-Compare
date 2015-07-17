
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

	/**
	 * Returns the first original image.
	 * @return The first original image.
	 * @since 09.07.2015
	 */
	public BufferedImage getFirstSourceImage() {
		return firstImage;
	}

	/**
	 * Sets the first original image. 
	 * @param firstImage The first original image.
	 * @since 09.07.2015
	 */
	public void setFirstSourceImage(BufferedImage firstImage) {
		forceRenderFirstImage = forceRenderFirstImage || this.firstImage != firstImage;
		this.firstImage = firstImage;
	}

	/**
	 * Returns the second original image.
	 * @return The second original image.
	 * @since 09.07.2015
	 */
	public BufferedImage getSecondSourceImage() {
		return secondImage;
	}

	/**
	 * Set the second original image.
	 * @param secondImage The second original image.
	 * @since 09.07.2015
	 */
	public void setSecondSourceImage(BufferedImage secondImage) {
		forceRenderSecondImage = forceRenderSecondImage || this.secondImage != secondImage;
		this.secondImage = secondImage;
	}

	/**
	 * Get the used scalefactor. Default is 1.0 (no changes).
	 * @return The used scalefactor.
	 * @since 09.07.2015
	 */
	public double getScalefactor() {
		return scalefactor;
	}

	/**
	 * Sets the scalefactor. This will be used when optimzing the image for the compare algorithm.
	 * @param scalefactor The used scalefactor.
	 * @since 09.07.2015
	 */
	public void setScalefactor(double scalefactor) {
		forceRenderFirstImage = forceRenderFirstImage || this.scalefactor == scalefactor;
		forceRenderSecondImage = forceRenderSecondImage || this.scalefactor == scalefactor;
		this.scalefactor = scalefactor;
	}

	/**
	 * Returns the fixed size scale. 
	 * @return The fixed size scale.
	 * @since 09.07.2015
	 */
	public Dimension getSizeScale() {
		return sizeScale;
	}

	/**
	 * Sets the fixed size scale. This will be used when optimzing the image for the compare algorithm.
	 * @param sizeScale The fixed size scale.
	 * @since 09.07.2015
	 */
	public void setSizeScale(Dimension sizeScale) {
		forceRenderFirstImage = forceRenderFirstImage || (this.sizeScale.width != sizeScale.width || this.sizeScale.height != sizeScale.height);
		forceRenderSecondImage = forceRenderSecondImage || (this.sizeScale.width != sizeScale.width || this.sizeScale.height != sizeScale.height);
		this.sizeScale = sizeScale;
	}

	/**
	 * Returns the first optimized image used for the algorithm.
	 * If the image was already drawn and no parameter has been changed (sizeScale and scalefactor) the a cached version will be returned.
	 * @throws NullPointerException If the first image is null.
	 * @return The optimzed first image.
	 * @since 09.07.2015
	 */
	public BufferedImage getFirstOptimizedImage() {
		if(firstImage == null) 
			throw new NullPointerException("First image is null!");
		
		if (forceRenderFirstImage || cachedFirstImage == null) {
			if (needOptimization()) {
				cachedFirstImage = ImageUtils.transformTo4ByteABGR(optimizeImage(firstImage));
			} else {
				cachedFirstImage = ImageUtils.transformTo4ByteABGR(getFirstSourceImage());
			}
			forceRenderFirstImage = false;
		}
		return cachedFirstImage;
	}

	/**
	 * Returns the second optimized image used for the algorithm.
	 * If the image was already drawn and no parameter has been changed (sizeScale and scalefactor) the a cached version will be returned.
	 * @throws NullPointerException If the second image is null.
	 * @return The optimzed second image.
	 * @since 09.07.2015
	 */
	public BufferedImage getSecondOptimizedImage() {
		if(secondImage == null) 
			throw new NullPointerException("Second image is null!");
		
		if (forceRenderSecondImage || cachedSecondImage == null) {
			if (needOptimization()) {
				cachedSecondImage = ImageUtils.transformTo4ByteABGR(optimizeImage(secondImage));
			} else {
				cachedSecondImage = ImageUtils.transformTo4ByteABGR(getSecondSourceImage());
			}
			forceRenderSecondImage = false;
		}
		return cachedSecondImage;
	}
}
