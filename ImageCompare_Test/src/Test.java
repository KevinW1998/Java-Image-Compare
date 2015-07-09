

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

public class Test {

	public static void testSingle(String imagePfadA, String imagePfadB, String ausgabedateipfad) throws IOException {
		ArrayList<Double> liste = new ArrayList<Double>();	
		ArrayList<Double> liste1 = new ArrayList<Double>();
		ArrayList<Double> liste2 = new ArrayList<Double>();

		BufferedWriter bw = new BufferedWriter(new FileWriter(ausgabedateipfad));
		bw.write("fov;colorfactor 0;colorfactor 0.5;colorfactor 1");
		int d= 1;
		while ( d < 90) {

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
		ArrayList<PdiffImageCompare> pa1 = new ArrayList<PdiffImageCompare>();		
		ArrayList<PdiffImageCompare> pa2 = new ArrayList<PdiffImageCompare>();
		
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(ausgabedateipfad));
		bw.write("fov;colorfactor 0;colorfactor 0.5;colorfactor 1");

		

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
				p.setColorfactor(0);
				
				PdiffImageCompare p1 = new PdiffImageCompare(img1, img2);
				p1.setFov(d);
				p1.setSizeScale(new Dimension(600, 450));
				p1.setColorfactor(0.5);
				
				PdiffImageCompare p2 = new PdiffImageCompare(img1, img2);
				p2.setFov(d);
				p2.setSizeScale(new Dimension(600, 450));
				p2.setColorfactor(1);
				
				pa.add(p);
				pa1.add(p1);
				pa2.add(p2);
				
			
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			

		}
		HashMap<PdiffImageCompare, Double> hm = PdiffImageCompare.comparePercentMultipleParallel(pa);
		HashMap<PdiffImageCompare, Double> hm1 = PdiffImageCompare.comparePercentMultipleParallel(pa1);
		HashMap<PdiffImageCompare, Double> hm2 = PdiffImageCompare.comparePercentMultipleParallel(pa2);
		for (int i = 0; i < pa.size(); i++) {
			
			int fov = i+1;
			
			String percent =""+ hm.get(pa.get(i)).doubleValue();
			percent= percent.replace('.',',');
			
			String percent1 =""+ hm1.get(pa1.get(i)).doubleValue();
			percent1= percent1.replace('.',',');
			
			String percent2 =""+hm2.get(pa2.get(i)).doubleValue();
			percent2= percent2.replace('.',',');
			
			bw.write("\n"+ fov+";"+ percent +";" + percent1  +";" + percent2  );
			
		}
		bw.close();

	}

	public static void main(String args[]) throws IOException {
//		testSingle("wohnung1.jpg", "wohnung2.jpg","diagramm.csv");
		test("zimmer1.jpg", "zimmer1LeichtVerschoben.jpg","leichtverschoben.csv");
	}
}
