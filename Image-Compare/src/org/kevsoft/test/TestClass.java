
package org.kevsoft.test;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.kevsoft.imagecompare.PdiffImageCompare;

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
			badQualityMercedes = ImageIO.read(new File("pdifftest_mercedes.jpg"));
			motorcycle = ImageIO.read(new File("pdifftest_mercedes.jpg"));
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
		
		HashMap<PdiffImageCompare, Double> resultPercent = PdiffImageCompare.comparePercentMultipleParallel(pdiffComparer);
		for(PdiffImageCompare nextComparer : pdiffComparer) {
			System.out.println("Results: " + resultPercent.get(nextComparer) + "%"); 
		}
	}

	public static void main(String[] args) {
		multipleCompares();
	}
}