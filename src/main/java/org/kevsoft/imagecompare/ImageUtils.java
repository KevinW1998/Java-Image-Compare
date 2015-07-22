
package org.kevsoft.imagecompare;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.WritableRaster;

public class ImageUtils {
	public static int[] getPixels(BufferedImage img){
		int pixels[] = new int[img.getWidth()*img.getHeight()*4];
		img.copyData(null).getPixels(0, 0, img.getWidth(), img.getHeight(), pixels);
		return pixels;
	}
	
	// Required by native PdiffImageCompare
	// Only modify with caution!
	public static byte[] getBytePixels(BufferedImage img) {
		return ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
	}
	
	
	public static BufferedImage scale(double factor, BufferedImage img){
		BufferedImage after = new BufferedImage((int)(factor*img.getWidth()), (int)(factor*img.getHeight()), BufferedImage.TYPE_4BYTE_ABGR);
		AffineTransform at = new AffineTransform();
		at.scale(factor, factor);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(img, after);		
		
		return after;
	}
	
	public static BufferedImage scaleToFixSize(int w, int h, BufferedImage img){
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		Graphics g = newImage.createGraphics();
		g.drawImage(img, 0, 0, w, h, null);
		g.dispose();
		return newImage;
	}
	
	public static BufferedImage scaleToFixSize(Dimension d, BufferedImage img){
		return scaleToFixSize((int)d.getWidth(), (int)d.getHeight(), img);
	}
	
	
	
	public static boolean is4ByteABGR(BufferedImage img) {
		return img.getColorModel().getTransparency() == ColorModel.TRANSLUCENT &&
				!img.getColorModel().isAlphaPremultiplied() &&
				img.getRaster().getDataBuffer().getDataType() == DataBuffer.TYPE_BYTE;
	}
	
	public static BufferedImage transformTo4ByteABGR(BufferedImage img) {
		if(ImageUtils.is4ByteABGR(img))
			return img;
		else {
			BufferedImage newImg =  new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g1 = newImg.getGraphics();
			g1.drawImage(img, 0, 0, null);
			g1.dispose();
			return newImg;
		}
	}
	
	/**
	 * Returns true if the image is binary full of one color or if it is invalid.
	 * @param img1 The image which should be scanned for.
	 * @return True if the image is binary full of one color or if it is invalid (.
	 * @since 10.07.2015
	 */
	public static boolean isOneColorOrNotValid(BufferedImage img1) {
		if(img1.getWidth() == 0 || img1.getHeight() == 0)
			return true;
		
		int lastRGBValue = img1.getRGB(0, 0);
		for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                int nextRGBValue = img1.getRGB(x, y);
             	if (nextRGBValue != lastRGBValue)
                    return false;
                lastRGBValue = nextRGBValue;
            }
        }
		
		return true;
	}
}
