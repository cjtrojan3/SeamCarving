package main;

public class Driver {

	public static void main(String[] args) {
		UWECImage im = new UWECImage("C:\\Users\\tofer\\Documents\\Workspace_335\\SeamCarving\\src\\cat.jpg");
		long[][] powers = new long[im.getWidth()][im.getHeight()];
		
		//Find the powers of each pixel
		for(int i = 1; i<=im.getWidth(); ++i){
			for(int j = 1; j<=im.getHeight(); ++j) {
				long hoTo, vTo;
				int plusOneW = i+1;
				int minusOneW = i-1;
				int plusOneH = j+1;
				int minusOneH = j-1;
				
				//Edge of image checks
				if (plusOneW > im.getWidth()) {
					plusOneW = 0;
				}
				if(minusOneW < 1) {
					minusOneW = im.getWidth();
				}
				if(plusOneH > im.getHeight()) {
					plusOneH = 0;
				}
				if(minusOneH < 1) {
					minusOneH = im.getHeight();
				}
				
				//Calculate powers of adjacent pixels
 				hoTo = (long) (Math.pow((im.getRed(plusOneW, j) - im.getRed(minusOneW, j)), 2) + Math.pow((im.getGreen(plusOneW, j) - im.getGreen(minusOneW, j)), 2) + Math.pow((im.getBlue(plusOneW, j) - im.getBlue(minusOneW, j)), 2));
				vTo = (long) (Math.pow((im.getRed(plusOneH, j) - im.getRed(minusOneH, j)), 2) + Math.pow((im.getGreen(plusOneH, j) - im.getGreen(minusOneH, j)), 2) + Math.pow((im.getBlue(plusOneH, j) - im.getBlue(minusOneH, j)), 2));

				powers[i-1][j-1] = (hoTo + vTo);

			}
		}
		
		long lowestPower = Long.MAX_VALUE;
		long totalPower = Long.MAX_VALUE;
		
		//For each pixel on the lowest row
		for(int i = 1; i<=im.getWidth(); ++i) {
			totalPower = calculatePower(powers, i);
			if(totalPower < lowestPower) {
				//TODO: Create a data structure that holds the path for the lowest power
				lowestPower = totalPower;
			}
		}
		System.out.println("Lowest power is: " + lowestPower);
	}
	
	private static long calculatePower(long[][] powers, int i) {
		int depth = powers.length;
		long runningTotal = powers[i][depth];
		
		//While we aren't at the top of the image
		while(depth >= 1){
			runningTotal += Math.min(Math.min(powers[i-1][depth-1], powers[i][depth-1]), powers[i+1][depth-1]);
			depth--;
		}
		return runningTotal;
	}

}
