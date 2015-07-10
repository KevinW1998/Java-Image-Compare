
package org.kevsoft.test;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.kevsoft.imagecompare.PdiffImageCompare;
import org.kevsoft.network.NetworkPdiffImageCompare;

/**
 * Testklasse
 * 
 * @author Waldock
 * @author Liu
 *
 */
public class TestClass {

	public static void pdiffCompare(String imagePfadA, String imagePfadB) {
		PdiffImageCompare pdiff;
		BufferedImage img1;
		BufferedImage img2;
		try {
			img1 = ImageIO.read(new File(imagePfadA));
			img2 = ImageIO.read(new File(imagePfadB));
			pdiff = new PdiffImageCompare(img1, img2);
			// pdiff.setSizeScale(new Dimension(600, 600));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		pdiff.setFov(15.0);
		pdiff.setColorfactor(1.0);

		System.out.println("==============");
		System.out.println(imagePfadA + " <=> " + imagePfadB);
		System.out.println("Similar: " + pdiff.comparePercent() + "%");
		System.out.println("==============");
	}

	public static void multipleCompares() {
		BufferedImage goodQualityMercedes;
		BufferedImage badQualityMercedes;
		BufferedImage motorcycle;

		try {
			goodQualityMercedes = ImageIO.read(new File("pdifftest_mercedes.jpg"));
			badQualityMercedes = ImageIO.read(new File("pdifftest_mercedes1.jpg"));
			motorcycle = ImageIO.read(new File("pdifftest_motorrad.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		PdiffImageCompare goodbadMercedes = new PdiffImageCompare(goodQualityMercedes, badQualityMercedes);
		PdiffImageCompare goodMercedes_motorcycle = new PdiffImageCompare(goodQualityMercedes, motorcycle);
		PdiffImageCompare badMercedes_motorcycle = new PdiffImageCompare(badQualityMercedes, motorcycle);
		
		PdiffImageCompare[] pdiffComparer = new PdiffImageCompare[]
				{goodbadMercedes,
					goodMercedes_motorcycle,
					badMercedes_motorcycle};
		
		for(int i = 0; i < pdiffComparer.length; i++) {
			pdiffComparer[i].setSizeScale(new Dimension(600, 600));
		}
		
		/*
		goodbadMercedes.setScalefactor(3.0);
		BufferedImage img1 = goodbadMercedes.getFirstOptimizedImage();
		System.out.println("goodbadMercedes optimzed size: width: " + img1.getHeight() + " height: " + img1.getWidth());
		*/
		HashMap<PdiffImageCompare, Double> resultPercent = PdiffImageCompare.comparePercentMultipleParallel(pdiffComparer);
		for(PdiffImageCompare nextComparer : pdiffComparer) {
			System.out.println("Results: " + resultPercent.get(nextComparer) + "%"); 
		}
	}
	
	public static String[] readList(File file) throws FileNotFoundException {
		ArrayList<String> urlList = new ArrayList<String>();
		
		Scanner sc = new Scanner(file);
		while(sc.hasNextLine()) {
			String nextLine = sc.nextLine();
			if(!nextLine.isEmpty())
				urlList.add(nextLine);
		}
		sc.close();
		
		return (String[]) urlList.toArray(new String[urlList.size()]);
	}
	
	public static void compareTwoUrlList(String fileWithUrls1, String fileWithUrls2) {
		try {
			double completeResult = NetworkPdiffImageCompare.compareAcross(readList(new File(fileWithUrls1)),
					readList(new File(fileWithUrls2)));
			System.out.println("Complete result: " + completeResult);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		compareTwoUrlList("pdifftest_links_bazar.txt", "pdifftest_links_wohnnet.txt");
	}
	
	
	
	
	
	
	
	
	
	
	
}