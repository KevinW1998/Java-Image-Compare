package org.kevsoft.imagecompare;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.kevsoft.util.NativeUtils;


/**
 * This class is using the pdiff algorithm to compare two images. It however
 * requires the native library "pdiffLib". This library is based on the work of
 * Hector Yee (http://pdiff.sourceforge.net/)
 * 
 * @author Waldock
 * @author Liu
 * @version 05.07.2015
 */


public class PdiffImageCompare extends ImageCompare {
	static {
        boolean success = false;
        try {
            System.loadLibrary("libpdiff");
            success = true;
        } catch (UnsatisfiedLinkError e) {
            // Try next one
        }

        // Try to load directly from jar file.
        // Try windows
        if (!success) {
            try {
                NativeUtils.loadLibraryFromJar("/resources/libpdiff.dll");
                success = true;
            } catch (UnsatisfiedLinkError e) {
                throw e;
            } catch (IOException e) {
                // Try next one
            }
        }

        // Try mac
        if (!success) {
            try {
                NativeUtils.loadLibraryFromJar("/resources/libpdiff.dylib");
                success = true;
            } catch (UnsatisfiedLinkError e) {
                throw e;
            } catch (IOException e) {
                // Try next one
            }
        }
        
        // Try linux
        if (!success) {
            try {
                NativeUtils.loadLibraryFromJar("/resources/libpdiff.so");
                success = true;
            } catch (UnsatisfiedLinkError e) {
                throw e;
            } catch (IOException e) {
                // Try next one
            }
        }
        
        if(!success)
            throw new RuntimeException("Failed to load pdiff!");

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

	public PdiffImageCompare(BufferedImage img1, BufferedImage img2) {
		this();
		setFirstSourceImage(img1);
		setSecondSourceImage(img2);
	}

	/**
	 * Original Description: Field of view in degrees (0.1 to 89.9)
	 * 
	 * @return Field of view
	 * @since 02.04.2015
	 */
	public double getFov() {
		return fov;
	}

	/**
	 * Original Description: Field of view in degrees (0.1 to 89.9)
	 * 
	 * @param fov
	 *            Sets the field of view
	 * @since 02.04.2015
	 */
	public void setFov(double fov) {
		if (fov < 0.1 || fov > 89.9)
			throw new IllegalArgumentException("Fov must be between 0.1 and 89.9");
		this.fov = fov;
	}

	/**
	 * Original Description: Value to convert rgb into linear space (default
	 * 2.2)
	 * 
	 * @return Gamma
	 * @since 02.04.2015
	 */
	public double getGamma() {
		return gamma;
	}

	/**
	 * Original Description: Value to convert rgb into linear space (default
	 * 2.2)
	 * 
	 * @param gamma
	 *            Gamma
	 * @since 02.04.2015
	 */
	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	/**
	 * Original Description: White luminance (default 100.0 cdm^-2)
	 * 
	 * @return Luminance
	 * @since 02.04.2015
	 */
	public double getLuminance() {
		return luminance;
	}

	/**
	 * Original Description: White luminance (default 100.0 cdm^-2)
	 * 
	 * @param luminance
	 *            Luminance
	 * @since 02.04.2015
	 */
	public void setLuminance(double luminance) {
		this.luminance = luminance;
	}

	/**
	 * Original Description: Only consider luminance; ignore chroma (color) in
	 * the comparison
	 * 
	 * @return Only consider luminance
	 * @since 02.04.2015
	 */
	public boolean isLuminanceonly() {
		return luminanceonly;
	}

	/**
	 * Original Description: Only consider luminance; ignore chroma (color) in
	 * the comparison
	 * 
	 * @param luminanceonly
	 *            Only consider luminance
	 * @since 02.04.2015
	 */
	public void setLuminanceonly(boolean luminanceonly) {
		this.luminanceonly = luminanceonly;
	}

	/**
	 * Original Description: How much of color to use, 0.0 to 1.0, 0.0 = ignore
	 * color.
	 * 
	 * @return Colorfactor
	 * @since 02.04.2015
	 */
	public double getColorfactor() {
		return colorfactor;
	}

	/**
	 * Original Description: How much of color to use, 0.0 to 1.0, 0.0 = ignore
	 * color.
	 * 
	 * @param colorfactor
	 *            Colorfactor
	 * @since 02.04.2015
	 */
	public void setColorfactor(double colorfactor) {
		if (colorfactor < 0.0 || colorfactor > 1.0)
			throw new IllegalArgumentException("Fov must be between 0.1 and 89.9");
		this.colorfactor = colorfactor;
	}

	/**
	 * Original Description: How many powers of two to down sample the image.
	 * 
	 * @return Downsample
	 * @since 02.04.2015
	 */
	public int getDownsample() {
		return downsample;
	}

	/**
	 * Original Description: How many powers of two to down sample the image.
	 * 
	 * @param downsample
	 *            Downsample
	 * @since 02.04.2015
	 */
	public void setDownsample(int downsample) {
		this.downsample = downsample;
	}

	/**
	 * Tests the image if it is similar. If you want to check how many pixels
	 * failed the test then use the compare overload.
	 * 
	 * @param thresholdPixels
	 *            How many pixels are allowed to fail, until the test doesn't
	 *            pass it.
	 * @return If the test was passed, given by the thresholdPixels.
	 * @throws RuntimeException
	 *             This exception is thrown when the size of both images are
	 *             different. Setting a fixed size (with .setSizeScale) will
	 *             prevent this exception.
	 * @since 02.04.2015
	 */
	public boolean compare(int thresholdPixels) throws RuntimeException {
		return compare() <= thresholdPixels;
	}

	/**
	 * Checks how many pixels are not similar between the two given images
	 * (Failed Pixels).
	 * 
	 * @return The number of failed pixels in the test.
	 * @throws RuntimeException
	 *             This exception is thrown when the size of both images are
	 *             different. Setting a fixed size (with .setSizeScale) will
	 *             prevent this exception.
	 * @since 05.07.2015
	 */
	public int compare() throws RuntimeException {
		BufferedImage img1 = getFirstOptimizedImage();
		BufferedImage img2 = getSecondOptimizedImage();

		if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
			throw new RuntimeException("Different sizes in optimized Image!");
		}

		return nativeCompareFailedPixels(ImageUtils.getBytePixels(img1), img1.getWidth(), img1.getHeight(), ImageUtils.getBytePixels(img2), img2.getWidth(), img2.getHeight());
	}

	/**
	 * Compares two images and returns the similarity of the two images.
	 * 
	 * @return The similarity in percent.
	 * @throws RuntimeException
	 *             This exception is thrown when the size of both images are
	 *             different. Setting a fixed size (with .setSizeScale) will
	 *             prevent this exception.
	 * @since 05.07.2015
	 */
	public double comparePercent() throws RuntimeException {
		BufferedImage img1 = getFirstOptimizedImage();
		int totalPixels = img1.getWidth() * img1.getHeight();
		return 100.0 - ((double) compare()) / (double) totalPixels * 100.0;
	}

	/**
	 * Compare multiple images parallel. The number of threads used is
	 * determined by number of cores used for the JVM.
	 * 
	 * @param allImageCompare
	 *            A collection with all comparer objects.
	 * @return A HashMap, where the <b>key</b> is the comparer object and the
	 *         <b>value</b> is the similarity in percent.
	 * @since 07.07.2015
	 */
	public static HashMap<PdiffImageCompare, Double> comparePercentMultipleParallel(Collection<PdiffImageCompare> allImageCompare) {
		return comparePercentMultipleParallel((PdiffImageCompare[]) allImageCompare.toArray(new PdiffImageCompare[allImageCompare.size()]), Runtime.getRuntime().availableProcessors());
	}

	/**
	 * Compare multiple images parallel. The number of threads used is
	 * determined by number of cores used for the JVM.
	 * 
	 * @param allImageCompare
	 *            An array with all comparer objects.
	 * @throws RuntimeException
	 *             This exception is thrown when the size of both images are
	 *             different. Setting a fixed size (with .setSizeScale) will
	 *             prevent this exception.
	 * @return A HashMap, where the <b>key</b> is the comparer object and the
	 *         <b>value</b> is the similarity in percent.
	 * @since 07.07.2015
	 */
	public static HashMap<PdiffImageCompare, Double> comparePercentMultipleParallel(PdiffImageCompare[] allImageCompare) {
		return comparePercentMultipleParallel(allImageCompare, Runtime.getRuntime().availableProcessors());
	}

	/**
	 * Compare multiple images parallel. The number of threads used is
	 * determined by number of cores used for the JVM.
	 * 
	 * @param allImageCompare
	 *            A collection with all comparer objects.
	 * @param numberOfThreads
	 *            The number of threads, which should be executed.
	 * @throws RuntimeException
	 *             This exception is thrown when the size of both images are
	 *             different. Setting a fixed size (with .setSizeScale) will
	 *             prevent this exception.
	 * @return A HashMap, where the <b>key</b> is the comparer object and the
	 *         <b>value</b> is the similarity in percent.
	 * @since 07.07.2015
	 */
	public static HashMap<PdiffImageCompare, Double> comparePercentMultipleParallel(Collection<PdiffImageCompare> allImageCompare, int numberOfThreads) {
		return comparePercentMultipleParallel((PdiffImageCompare[]) allImageCompare.toArray(new PdiffImageCompare[allImageCompare.size()]), numberOfThreads);
	}

	/**
	 * Compare multiple images parallel. The number of threads used is
	 * determined by number of cores used for the JVM.
	 * 
	 * @param allImageCompare
	 *            An array with all comparer objects.
	 * @param numberOfThreads
	 *            The number of threads, which should be executed.
	 * @throws RuntimeException
	 *             This exception is thrown when the size of both images are
	 *             different. Setting a fixed size (with .setSizeScale) will
	 *             prevent this exception.
	 * @return A HashMap, where the <b>key</b> is the comparer object and the
	 *         <b>value</b> is the similarity in percent.
	 * @since 07.07.2015
	 */
	public static HashMap<PdiffImageCompare, Double> comparePercentMultipleParallel(PdiffImageCompare[] allImageCompare, int numberOfThreads) {
		HashMap<PdiffImageCompare, Integer> ret = new HashMap<PdiffImageCompare, Integer>();
		nativeCompareFailedPixelsMultiple(allImageCompare, numberOfThreads, ret);

		HashMap<PdiffImageCompare, Double> percentRet = new HashMap<PdiffImageCompare, Double>();
		for (Entry<PdiffImageCompare, Integer> entry : ret.entrySet()) {
			PdiffImageCompare pdiffObj = entry.getKey();
			BufferedImage nextImg = pdiffObj.getFirstOptimizedImage();
			int totalPixels = nextImg.getWidth() * nextImg.getHeight();

			percentRet.put(pdiffObj, new Double(100.0 - (entry.getValue().doubleValue()) / (double) totalPixels * 100.0));
		}
		return percentRet;
	}

	/**
	 * Compare multiple images parallel. The number of threads used is
	 * determined by number of cores used for the JVM.
	 * 
	 * @param allImageCompare
	 *            A collection with all comparer objects.
	 * @throws RuntimeException
	 *             This exception is thrown when the size of both images are
	 *             different. Setting a fixed size (with .setSizeScale) will
	 *             prevent this exception.
	 * @return A HashMap, where the <b>key</b> is the comparer object and the
	 *         <b>value</b> is the number of failed pixels.
	 * @since 07.07.2015
	 */
	public static HashMap<PdiffImageCompare, Integer> compareMultipleParallel(Collection<PdiffImageCompare> allImageCompare) {
		return compareMultipleParallel((PdiffImageCompare[]) allImageCompare.toArray(new PdiffImageCompare[allImageCompare.size()]), Runtime.getRuntime().availableProcessors());
	}

	/**
	 * Compare multiple images parallel. The number of threads used is
	 * determined by number of cores used for the JVM.
	 * 
	 * @param allImageCompare
	 *            An array with all comparer objects.
	 * @throws RuntimeException
	 *             This exception is thrown when the size of both images are
	 *             different. Setting a fixed size (with .setSizeScale) will
	 *             prevent this exception.
	 * @return A HashMap, where the <b>key</b> is the comparer object and the
	 *         <b>value</b> is the number of failed pixels.
	 * @since 07.07.2015
	 */
	public static HashMap<PdiffImageCompare, Integer> compareMultipleParallel(PdiffImageCompare[] allImageCompare) {
		return compareMultipleParallel(allImageCompare, Runtime.getRuntime().availableProcessors());
	}

	/**
	 * Compares multiple images parallel.
	 * 
	 * @param allImageCompare
	 *            A collection with all comparer objects.
	 * @param numberOfThreads
	 *            The number of threads, which should be executed.
	 * @throws RuntimeException
	 *             This exception is thrown when the size of both images are
	 *             different. Setting a fixed size (with .setSizeScale) will
	 *             prevent this exception.
	 * @return A HashMap, where the <b>key</b> is the comparer object and the
	 *         <b>value</b> is the number of failed pixels.
	 * @since 07.07.2015
	 */
	public static HashMap<PdiffImageCompare, Integer> compareMultipleParallel(Collection<PdiffImageCompare> allImageCompare, int numberOfThreads) {
		return compareMultipleParallel((PdiffImageCompare[]) allImageCompare.toArray(new PdiffImageCompare[allImageCompare.size()]), numberOfThreads);
	}

	/**
	 * Compares multiple images parallel.
	 * 
	 * @param allImageCompare
	 *            An array with all comparer objects.
	 * @param numberOfThreads
	 *            The number of threads, which should be executed.
	 * @throws RuntimeException
	 *             This exception is thrown when the size of both images are
	 *             different. Setting a fixed size (with .setSizeScale) will
	 *             prevent this exception.
	 * @return A HashMap, where the <b>key</b> is the comparer object and the
	 *         <b>value</b> is the number of failed pixels.
	 * @since 07.07.2015
	 */
	public static HashMap<PdiffImageCompare, Integer> compareMultipleParallel(PdiffImageCompare[] allImageCompare, int numberOfThreads) {
		HashMap<PdiffImageCompare, Integer> ret = new HashMap<PdiffImageCompare, Integer>();
		nativeCompareFailedPixelsMultiple(allImageCompare, numberOfThreads, ret);
		return ret;
	}

	// private natives:
	private native int nativeCompareFailedPixels(byte[] pixOfImage1, int width1, int height1, byte[] pixOfImage2, int width2, int height2);

	private static native void nativeCompareFailedPixelsMultiple(PdiffImageCompare[] allImageCompare, int numThreads, HashMap<PdiffImageCompare, Integer> out);

}
