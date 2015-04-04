package org.kevsoft.imagecompare;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
public class PdiffImageCompare extends ImageCompare {
	static {
		System.loadLibrary("libpdiff");
	}
	
	private double fov;
	private double gamma;
	private double luminance;
	private boolean luminanceonly;
	private double colorfactor;
	private int downsample;
	
	public PdiffImageCompare() {
		fov = 45.0;
		gamma = 2.2;
		luminance = 100.0;
		luminanceonly = false;
		colorfactor = 1.0;
		downsample = 0;
	}
	
	public PdiffImageCompare(BufferedImage img1, BufferedImage img2){
		this();
		setFirstSourceImage(img1);
		setSecondSourceImage(img2);
	}
	
	/**
	 * Original Description: Field of view in degrees (0.1 to 89.9)
	 * @return Field of view
	 * @since 02.04.2015
	 */
	public double getFov() {
		return fov;
	}
	/**
	 * Original Description: Field of view in degrees (0.1 to 89.9)
	 * @param fov Sets the field of view
	 * @since 02.04.2015
	 */
	public void setFov(double fov) {
		if(fov < 0.1 || fov > 89.9)
			throw new IllegalArgumentException("Fov must be between 0.1 and 89.9");
		this.fov = fov;
	}
	
	/**
	 * Original Description: Value to convert rgb into linear space (default 2.2)
	 * @return Gamma
	 * @since 02.04.2015
	 */
	public double getGamma() {
		return gamma;
	}
	/**
	 * Original Description: Value to convert rgb into linear space (default 2.2)
	 * @param gamma Gamma
	 * @since 02.04.2015
	 */
	public void setGamma(double gamma) {
		this.gamma = gamma;
	}
	
	/**
	 * Original Description: White luminance (default 100.0 cdm^-2)
	 * @return Luminance
	 * @since 02.04.2015
	 */
	public double getLuminance() {
		return luminance;
	}
	/**
	 * Original Description: White luminance (default 100.0 cdm^-2)
	 * @param luminance Luminance
	 * @since 02.04.2015
	 */
	public void setLuminance(double luminance) {
		this.luminance = luminance;
	}
	
	/**
	 * Original Description: Only consider luminance; ignore chroma (color) in the comparison
	 * @return Only consider luminance
	 * @since 02.04.2015
	 */
	public boolean isLuminanceonly() {
		return luminanceonly;
	}
	/**
	 * Original Description: Only consider luminance; ignore chroma (color) in the comparison
	 * @param luminanceonly Only consider luminance
	 * @since 02.04.2015
	 */
	public void setLuminanceonly(boolean luminanceonly) {
		this.luminanceonly = luminanceonly;
	}
	
	/**
	 * Original Description: How much of color to use, 0.0 to 1.0, 0.0 = ignore color.
	 * @return Colorfactor
	 * @since 02.04.2015
	 */
	public double getColorfactor() {
		return colorfactor;
	}
	/**
	 * Original Description: How much of color to use, 0.0 to 1.0, 0.0 = ignore color.
	 * @param colorfactor Colorfactor
	 * @since 02.04.2015
	 */
	public void setColorfactor(double colorfactor) {
		if(fov < 0.0 || fov > 1.0)
			throw new IllegalArgumentException("Fov must be between 0.1 and 89.9");
		this.colorfactor = colorfactor;
	}
	
	/**
	 * Original Description: How many powers of two to down sample the image.
	 * @return Downsample
	 * @since 02.04.2015
	 */
	public int getDownsample() {
		return downsample;
	}
	/**
	 * Original Description: How many powers of two to down sample the image.
	 * @param downsample Downsample
	 * @since 02.04.2015
	 */
	public void setDownsample(int downsample) {
		this.downsample = downsample;
	}
	
	private native boolean nativeCompare(int thresholdPixels, byte[] pixOfImage1, int width1, int height1, byte[] pixOfImage2, int width2, int height2);
	
	/**
	 * Compares the two images with pdiff.
	 * @return true If the pdiff test succeed.
	 * @throws Exception When the sizes of the two images are diffrent.
	 * @since 02.04.2015
	 */
	public boolean compare() throws Exception {
		return compare(100);
	}
	
	/**
	 * 
	 * @param thresholdPixels
	 * @return
	 * @throws Exception
	 * @since 02.04.2015
	 */
	public boolean compare(int thresholdPixels) throws Exception {
		BufferedImage img1Orig = getFirstOptimizedImage();
		BufferedImage img2Orig = getSecondOptimizedImage();
		
		BufferedImage img1 = new BufferedImage(img1Orig.getWidth(), img1Orig.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		BufferedImage img2 = new BufferedImage(img2Orig.getWidth(), img2Orig.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		
		Graphics g1 = img1.getGraphics();
		Graphics g2 = img2.getGraphics();
		
		g1.drawImage(img1Orig, 0, 0, null);
		g2.drawImage(img2Orig, 0, 0, null);
		
		g1.dispose();
		g2.dispose();
		
		
		if(img1.getWidth() != img2.getWidth() ||
				img1.getHeight() != img2.getHeight()){
			throw new Exception("Different sizes in optimized Image!");
		}
		
		return nativeCompare(thresholdPixels, ImageUtils.getBytePixels(img1), img1.getWidth(), img1.getHeight(), ImageUtils.getBytePixels(img2), img2.getWidth(), img2.getHeight());
	}
	
	
}
