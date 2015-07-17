package org.kevsoft.test;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.kevsoft.imagecompare.PdiffImageCompare;

public class TestClass2 {

	public static void testSingle(String imagePfadA, String imagePfadB, String ausgabedateipfad) throws IOException {
		ArrayList<Double> liste = new ArrayList<Double>();	
		ArrayList<Double> liste1 = new ArrayList<Double>();
		ArrayList<Double> liste2 = new ArrayList<Double>();

		BufferedWriter bw = new BufferedWriter(new FileWriter(ausgabedateipfad));
		bw.write("fov;colorfactor 0;colorfactor 0.5;colorfactor 1");
		int d= 1;
		while ( d < 3) {

			PdiffImageCompare pdiff;
			BufferedImage img1;
			BufferedImage img2;
			try {
				img1 = ImageIO.read(new File(imagePfadA));
				img2 = ImageIO.read(new File(imagePfadB));
				pdiff = new PdiffImageCompare(img1, img2);

				pdiff.setFov(d);
				pdiff.setSizeScale(new Dimension(600, 450));
				
				pdiff.setColorfactor(0);
				double speicher = pdiff.comparePercent();			
				liste.add(speicher);
				
				pdiff.setColorfactor(0.5);
				double speicher1 = pdiff.comparePercent();
				liste1.add(speicher1);
				
				pdiff.setColorfactor(1);
				double speicher2 = pdiff.comparePercent();
				liste2.add(speicher2);
				
				
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			d++;
		}
		for (int z = 0; z<liste.size(); z++){
			int fov = z+1;
			String reihe1 = ""+liste.get(z);
			reihe1= reihe1.replace('.',',');
			
			String reihe2 = ""+liste1.get(z);
			reihe2= reihe2.replace('.',',');
			
			String reihe3 = ""+liste2.get(z);
			reihe3= reihe3.replace('.',',');
			
			
			bw.write("\n"+ fov+";"+ reihe1 +";" + reihe2  +";" + reihe3  );
		}
		bw.close();
	}

	public static void test(String imagePfadA, String imagePfadB, String ausgabedateipfad) throws IOException {
		ArrayList<Double> liste = new ArrayList<Double>();
		ArrayList<PdiffImageCompare> pa = new ArrayList<PdiffImageCompare>();
		HashMap map = new HashMap();


		for (int d = 1; d < 90; d++) {

			// PdiffImageCompare pdiff;
			BufferedImage img1;
			BufferedImage img2;
			try {
				img1 = ImageIO.read(new File(imagePfadA));
				img2 = ImageIO.read(new File(imagePfadB));
				// pdiff = new PdiffImageCompare(img1, img2);
				PdiffImageCompare p = new PdiffImageCompare(img1, img2);
				p.setFov(d);
				p.setSizeScale(new Dimension(600, 450));
				pa.add(p);

				// pdiff.setSizeScale(new Dimension(600, 450));
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			// pdiff.setFov(d);
			// double erg = pdiff.compareMultipleParallel();
			// liste.add (erg);
			// System.out.println(erg);

		}
		HashMap<PdiffImageCompare, Integer> hm = PdiffImageCompare.compareMultipleParallel(pa);
		for (int i = 0; i < pa.size(); i++) {

			int pixelsfailed = hm.get(pa.get(i)).intValue();
			System.out.println(pixelsfailed);
		}

	}
	
	public static void testeasy (String pfad1, String pfad2 ){
		
		
		


			PdiffImageCompare pdiff;
			BufferedImage img1;
			BufferedImage img2;
			try {
				img1 = ImageIO.read(new File(pfad1));
				img2 = ImageIO.read(new File(pfad2));
				pdiff = new PdiffImageCompare(img1, img2);

				pdiff.setSizeScale(new Dimension(600, 450));
				
				pdiff.setGamma(1);
				System.out.println( pdiff.comparePercent());			
				
				
				
			
				
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			
		}
		
		
		
		
	
	public static void main(String args[]) throws IOException {
//		testSingle("wohnung1.jpg", "wohnung2.jpg","E:\\Irian\\Image-Compare\\ausgabedatei.csv");
		testeasy ("mercedes.jpg","wohnung2.jpg");
	}
}
