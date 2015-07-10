package org.kevsoft.network;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.kevsoft.imagecompare.PdiffImageCompare;

public class NetworkPdiffImageCompare {

	private static class NetworkCompareResults {
		public BufferedImage usedImage;
		public BufferedImage highestResultImage;
		public double comparePercentResult;
	}
	
	
	private static NetworkCompareResults compareNext(BufferedImage nextImage, ArrayList<BufferedImage> otherImages) {
		ArrayList<PdiffImageCompare> allComparer = new ArrayList<PdiffImageCompare>();
		for(BufferedImage nextImageOfList : otherImages) {
			PdiffImageCompare nextComparer = new PdiffImageCompare(nextImage, nextImageOfList);
			nextComparer.setFov(32.0);
			nextComparer.setSizeScale(new Dimension(600, 600));
			allComparer.add(nextComparer);
		}
		HashMap<PdiffImageCompare, Double> allResult = PdiffImageCompare.comparePercentMultipleParallel(allComparer);
		
		NetworkCompareResults highestResult = new NetworkCompareResults();
		highestResult.comparePercentResult = 0.0;
		highestResult.usedImage = nextImage;
		for(Entry<PdiffImageCompare, Double> nextResult : allResult.entrySet()) {
			if(highestResult.comparePercentResult < nextResult.getValue()) {
				highestResult.comparePercentResult = nextResult.getValue();
				highestResult.highestResultImage = nextResult.getKey().getSecondSourceImage();
			}
		}
		return highestResult;
	}
	
	private static ArrayList<BufferedImage> downloadAllValidImages(String[] urlPackages){
		ArrayList<BufferedImage> allValidDownloadedImages = new ArrayList<BufferedImage>();
		for(String nextPossibleUrl : urlPackages) {
			try {
				allValidDownloadedImages.add(ImageIO.read(new URL(nextPossibleUrl)));
			} catch (MalformedURLException e) {
				System.out.println("Invalid Url: " + nextPossibleUrl);
			} catch (IOException e) {
				System.out.println("IO Exception: " + nextPossibleUrl);
			}
		}
		return allValidDownloadedImages;
	}
	
	public static double compareAcross(String[] urlArray1, String[] urlArray2) {
		ArrayList<BufferedImage> allImages1 = downloadAllValidImages(urlArray1);
		ArrayList<BufferedImage> allImages2 = downloadAllValidImages(urlArray2);
		
		
		System.out.println("Downloaded all valid images!");
		
		
		ArrayList<NetworkCompareResults> results = new ArrayList<NetworkCompareResults>();
		for (BufferedImage nextImage : allImages1) {
			if(allImages2.size() < 0)
				break;
			
			System.out.println("Comparing next with " + allImages2.size() + " other images! " + nextImage.toString());
			
			NetworkCompareResults nextResult = compareNext(nextImage, allImages2);
			allImages2.remove(nextResult.highestResultImage);
			results.add(nextResult);
			System.out.println("The highest result with: " + nextResult.comparePercentResult + "%");
		}
		
		double sumOfPercent = 0;
		for (NetworkCompareResults nextResult : results) {
			sumOfPercent += nextResult.comparePercentResult;
		}
		
		return (results.size() != 0 ? sumOfPercent / results.size() : 0.0);
	}
}
