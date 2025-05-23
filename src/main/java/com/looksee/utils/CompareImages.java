package com.looksee.utils;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;

/**
 * Compares two images and returns true if they are the same.
 */
public class CompareImages {

	/**
	 * Compares two images and returns true if they are the same.
	 * @param img1Url the first image URL
	 * @param img2Url the second image URL
	 * @return true if the images are the same, false otherwise
	 *
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>img1Url != null</li>
	 *   <li>img2Url != null</li>
	 * </ul>
	 */
	public static boolean imagesMatch(String img1Url, String img2Url) {
		assert img1Url != null && img2Url != null;

		String file1 = img1Url;
		String file2 = img2Url;

		// Load the images
		Image image1 = Toolkit.getDefaultToolkit().getImage(file1);
		Image image2 = Toolkit.getDefaultToolkit().getImage(file2);

		try {

			PixelGrabber grabImage1Pixels = new PixelGrabber(image1, 0, 0, -1,
					-1, false);
			PixelGrabber grabImage2Pixels = new PixelGrabber(image2, 0, 0, -1,
					-1, false);

			int[] image1Data = null;

			if (grabImage1Pixels.grabPixels()) {
				int width = grabImage1Pixels.getWidth();
				int height = grabImage1Pixels.getHeight();
				image1Data = new int[width * height];
				image1Data = (int[]) grabImage1Pixels.getPixels();
			}

			int[] image2Data = null;

			if (grabImage2Pixels.grabPixels()) {
				int width = grabImage2Pixels.getWidth();
				int height = grabImage2Pixels.getHeight();
				image2Data = new int[width * height];
				image2Data = (int[]) grabImage2Pixels.getPixels();
			}

			System.out.println("Pixels equal: "
					+ java.util.Arrays.equals(image1Data, image2Data));
			return java.util.Arrays.equals(image1Data, image2Data);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		return false;
	}
}