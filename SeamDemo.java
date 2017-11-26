package main;

import java.awt.image.BufferedImage;

public class SeamDemo {

	public static void main(String[] args) {
		UWECImage im = new UWECImage("chris.jpg");
		im.openNewDisplayWindow();
		for(int i=0; i<=200; i++) {
			calculateVerticalSeam(im);
		}
		for(int i=0; i<=200; i++) {
			calculateHorizontalSeam(im);
		}
	}
	
	
	public static void calculateVerticalSeam(UWECImage im) {
		Seam seam = new Seam(im);
		seam.caclulateSeam();
		BufferedImage newImage = im.getImage().getSubimage(0, 0, im.getWidth()-1, im.getHeight());
		im.switchImage(newImage);
		im.repaintCurrentDisplayWindow();
	}
	public static void calculateHorizontalSeam(UWECImage im) {
		im.switchImage(im.rotateImage(im));
		Seam seam = new Seam(im);
		seam.caclulateSeam();
		BufferedImage newImage = im.getImage().getSubimage(0, 0, im.getWidth()-1, im.getHeight());
		im.switchImage(newImage);
		im.switchImage(im.rotateImage(im));
		im.repaintCurrentDisplayWindow();
	}
}
