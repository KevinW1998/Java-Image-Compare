package org.kevsoft.test;

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

	public static void test(String imagePfadA, String imagePfadB, String ausgabedateipfad) throws IOException {
		ArrayList<Double> liste = new ArrayList<Double>();
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
			liste.add (erg);
			
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(ausgabedateipfad));
			bw.write("hallo");
			for (int i=0; i< liste.size(); i++){
				bw.write("/n"+liste.get(i) );
			}
			
			d= d+ 0.1;
			
		}

	}

	public static void main(String args[]) throws IOException {
		test("motorrad.jpg", "motorrad.jpg","./Users/larry/Documents/Entwickler Umgebung/java/ausgabe.csv");
	}
}
