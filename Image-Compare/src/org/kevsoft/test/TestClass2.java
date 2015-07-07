package org.kevsoft.test;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ArrayList.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.kevsoft.imagecompare.PdiffImageCompare;

public class TestClass2 {

	public static void test(String imagePfadA, String imagePfadB) throws IOException {
		ArrayList<Double> liste = new ArrayList<Double>();
//		BufferedWriter bw = new BufferedWriter(new FileWriter(ausgabedateipfad));
		double d = 1;
		while (d < 90) {
			PdiffImageCompare pdiff;   
			   BufferedImage img1;
			   BufferedImage img2;
			   try {
				   img1 = ImageIO.read(new File(imagePfadA));
				   img2 = ImageIO.read(new File(imagePfadB));
				   pdiff = new PdiffImageCompare(img1, img2);
				   pdiff.setSizeScale(new Dimension(600, 450));
			   }catch(Exception e) {
				   e.printStackTrace();
		    	   return;
			   }
			   pdiff.setFov(d);
			double erg = pdiff.compare();
			liste.add (erg);
			
			d++;
		}
			
			for (int i=0; i< liste.size(); i++){
//				bw.write("/n"+liste.get(i) );
				System.out.println(liste.get(i));
			}
			
			
			
		}

	

	public static void main(String args[]) throws IOException {
		test("wohnung1.jpg", "wohnung2.jpg");
	}
}
