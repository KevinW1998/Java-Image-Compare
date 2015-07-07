
package org.kevsoft.test;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.kevsoft.imagecompare.ImageUtils;
import org.kevsoft.imagecompare.NearImageCompare;
import org.kevsoft.imagecompare.PdiffImageCompare;
import org.kevsoft.imagecompare.SimpleImageCompare;

/**
 * Testklasse
 * 
 * @author Waldock
 * @author Liu
 *
 */
public class TestClass {

	public static void compare(String imagePfadA, String imagePfadB) { // vergleicht
																		// zwei
																		// Arrays
																		// auf
																		// Gleichheit
		NearImageCompare nic;
		try {
			nic = new NearImageCompare(ImageIO.read(new File(imagePfadA)), ImageIO.read(new File(imagePfadB)));
			nic.setSizeScale(new Dimension(600, 600));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println("==============");
		try {
			double compareVal = nic.compare();

			System.out.println(imagePfadA + " <=> " + imagePfadB);
			System.out.println("Similarity: " + compareVal);
			nic.compareColor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("==============");
	}

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

		PdiffImageCompare[] pdiffComparer = new PdiffImageCompare[]
				{new PdiffImageCompare(goodQualityMercedes, badQualityMercedes),
						new PdiffImageCompare(goodQualityMercedes, motorcycle),
						new PdiffImageCompare(badQualityMercedes, motorcycle)};
		
		for(int i = 0; i < pdiffComparer.length; i++) {
			pdiffComparer[i].setSizeScale(new Dimension(600, 600));
		}
		
		HashMap<PdiffImageCompare, Integer> results = PdiffImageCompare.compareMultipleParallel(pdiffComparer);
		for(PdiffImageCompare nextComparer : pdiffComparer) {
			System.out.println("Results: " + results.get(nextComparer)); 
		}
	}

	public static void main(String[] args) {
		//pdiffCompare("pdifftest_mercedes.jpg", "pdifftest_mercedes1.jpg");
		multipleCompares();
	}
}