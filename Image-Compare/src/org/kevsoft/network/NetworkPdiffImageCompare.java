package org.kevsoft.network;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.kevsoft.imagecompare.ImageUtils;
import org.kevsoft.imagecompare.PdiffImageCompare;

public class NetworkPdiffImageCompare {

	private static class NetworkCompareResults {
		public BufferedImage usedImage;
		public BufferedImage highestResultImage;
		public double comparePercentResult;
	}
	
	private static Dimension balancedSize(BufferedImage img1, BufferedImage img2) {
		return new Dimension((img1.getWidth()+img2.getWidth()) / 2, (img1.getHeight()+img2.getHeight()) / 2);
	}
	
	private static NetworkCompareResults compareNext(BufferedImage nextImage, ArrayList<BufferedImage> otherImages) {
		ArrayList<PdiffImageCompare> allComparer = new ArrayList<PdiffImageCompare>();
		for(BufferedImage nextImageOfList : otherImages) {
			PdiffImageCompare nextComparer = new PdiffImageCompare(nextImage, nextImageOfList);
			nextComparer.setFov(32.0);
			nextComparer.setSizeScale(balancedSize(nextImage, nextImageOfList));
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
				BufferedImage downloadedImg = ImageIO.read(new URL(nextPossibleUrl));
				if(!ImageUtils.isOneColorOrNotValid(downloadedImg)) {
					allValidDownloadedImages.add(downloadedImg);
				}
			} catch (MalformedURLException e) {
				System.out.println("Invalid Url: " + nextPossibleUrl);
			} catch (IOException e) {
				System.out.println("IO Exception: " + nextPossibleUrl);
			}
		}
		return allValidDownloadedImages;
	}
	
	public static double compareAcross(String[] urlArray1, String[] urlArray2) {
		long startTime = System.nanoTime(); 			//FIXME DBG
		ArrayList<BufferedImage> allImages1 = downloadAllValidImages(urlArray1);
		ArrayList<BufferedImage> allImages2 = downloadAllValidImages(urlArray2);
		
		
		System.out.println("Downloaded all valid images!"); 			//FIXME DBG
		
		
		ArrayList<NetworkCompareResults> results = new ArrayList<NetworkCompareResults>();
		for (BufferedImage nextImage : allImages1) {
			if(allImages2.size() <= 0)
				break;
			
			System.out.println("Comparing next with " + allImages2.size() + " other images! " + nextImage.toString()); 			//FIXME DBG
			
			NetworkCompareResults nextResult = compareNext(nextImage, allImages2);
			allImages2.remove(nextResult.highestResultImage);
			results.add(nextResult);
			System.out.println("The highest result with: " + nextResult.comparePercentResult + "%"); 			//FIXME DBG
		}
		
		double sumOfPercent = 0;
		for (NetworkCompareResults nextResult : results) {
			sumOfPercent += nextResult.comparePercentResult;
			
			//FIXME DBG
			String fName = "pdifftestresult_" + (int)(Math.random()*10000) + "_"; 
			try {
				ImageIO.write(nextResult.usedImage, "png", new File(fName+"1.png"));
				ImageIO.write(nextResult.highestResultImage, "png", new File(fName+"2.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		System.out.println("Time taken: " + (System.nanoTime() - startTime) + "ns!"); 			//FIXME DBG
		return (results.size() != 0 ? sumOfPercent / results.size() : 0.0);
	}
}
