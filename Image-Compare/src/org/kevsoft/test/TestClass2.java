package org.kevsoft.test;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ArrayList.*;
import java.util.HashMap;
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
		ArrayList<PdiffImageCompare> pa = new ArrayList<PdiffImageCompare>();
		HashMap map = new HashMap();
//		BufferedWriter bw = new BufferedWriter(new FileWriter(ausgabedateipfad));
		
		for (int d =1 ; d < 90;d++) {
			
//			PdiffImageCompare pdiff;   
			   BufferedImage img1;
			   BufferedImage img2;
			   try {
				   img1 = ImageIO.read(new File(imagePfadA));
				   img2 = ImageIO.read(new File(imagePfadB));
//				   pdiff = new PdiffImageCompare(img1, img2);
				   PdiffImageCompare p= new PdiffImageCompare (img1, img2);
				   p.setFov(d);
				   p.setSizeScale(new Dimension (600,450));
				   pa.add(p);
				  
				   
//				   pdiff.setSizeScale(new Dimension(600, 450));
			   }catch(Exception e) {
				   e.printStackTrace();
		    	   return;
			   }
//			   pdiff.setFov(d);
//			double erg = pdiff.compareMultipleParallel();
//			liste.add (erg);
//			System.out.println(erg);
			
			 
		}
		HashMap <PdiffImageCompare, Integer> hm = PdiffImageCompare.compareMultipleParallel(pa); 
		for (int i=0; i<pa.size();i++ ){
			
			int pixelsfailed= hm.get(pa.get(i)).intValue();
			System.out.println(pixelsfailed);
		}
		 
			
			
			
		}

	

	public static void main(String args[]) throws IOException {
		test("wohnung1.jpg", "wohnung2.jpg");
	}
}
