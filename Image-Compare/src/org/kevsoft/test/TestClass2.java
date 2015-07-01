package org.kevsoft.test;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;

import javax.imageio.ImageIO;

import org.kevsoft.imagecompare.PdiffImageCompare;

public class TestClass2 {

	public static void test(String imagePfadA, String imagePfadB) {
		double d = 0;
		while (d < 89) {
			PdiffImageCompare pdiff;   
			   BufferedImage img1;
			   BufferedImage img2;
			   try {
				   img1 = ImageIO.read(new File(imagePfadA));
				   img2 = ImageIO.read(new File(imagePfadB));
				   pdiff = new PdiffImageCompare(img1, img2);
				   //pdiff.setSizeScale(new Dimension(600, 600));
			   }catch(Exception e) {
				   e.printStackTrace();
		    	   return;
			   }
			   pdiff.setFov(d);
			double erg = pdiff.compare();
			d= d+ 0.1;
			System.out.println(erg);
		}

	}

	public static void main(String args[]) {
		test("motorrad.jpg", "motorrad.jpg");
	}
}
