package org.kevsoft.imagecompare;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageUtils {
	public static int[] getPixels(BufferedImage img){
		int pixels[] = new int[img.getWidth()*img.getHeight()*4];
		img.copyData(null).getPixels(0, 0, img.getWidth(), img.getHeight(), pixels);
		return pixels;
	}
	
	public static byte[] getBytePixels(BufferedImage img) {
		return ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
	}
	
	
	public static BufferedImage scale(double factor, BufferedImage img){
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(2.0, 2.0);
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
}
