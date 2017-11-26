package main;

public class Seam {
	
	private UWECImage im;
	
	public Seam(UWECImage im) {
		this.im = im;
	}

	public void caclulateSeam() {	
		
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
		
		//Second 2d array to hold the total powers of everything above it
		long[][] totalPower = new long[powers.length][powers[0].length];
		int[][] backTracker = new int[im.getWidth()][im.getHeight()];
		for(int i = 0; i<im.getHeight(); i++){
			for(int j = 0; j<im.getWidth(); j++){
				
				long minimum;
				
				if(i==0){	//Top row
					totalPower[j][i] = powers[j][i];
					backTracker[j][i] = 0;
				}
				else{
					if(j==0){	//We are on the left column
						minimum = Math.min(totalPower[j][i-1], totalPower[j+1][i-1]);
						if(minimum == totalPower[j][i-1]){
							backTracker[j][i] = 0;
						}
						else{
							backTracker[j][i] = 1;
						}
						totalPower[j][i] = powers[j][i] + minimum;
					}			//Far right column
					else if(j == im.getWidth()-1){
						minimum = Math.min(totalPower[j-1][i-1], totalPower[j][i-1]);
						if(minimum == totalPower[j-1][i-1]){
							backTracker[j][i] = -1;
						}
						else{
							backTracker[j][i] = 0;
						}
						totalPower[j][i] = powers[j][i] + minimum;
					}
					else{
						minimum = Math.min(Math.min(totalPower[j-1][i-1], totalPower[j][i-1]), totalPower[j+1][i-1]);
						if(minimum == totalPower[j-1][i-1]){
							backTracker[j][i] = -1;
						}
						else if(minimum == totalPower[j][i-1]){
							backTracker[j][i] = 0;
						}
						else{
							backTracker[j][i] = 1;
						}
						totalPower[j][i] = powers[j][i] + minimum;
					}
				}
			}
		}
		
		//Find lowest total power
		long lowestPower = Long.MAX_VALUE;
		int lowestPowerIndex = 0;
		for(int i = 0; i<totalPower.length-1; i++) {
			if(totalPower[i][totalPower[0].length-1] < lowestPower) {
				lowestPower = totalPower[i][totalPower[0].length-1];
				lowestPowerIndex = i;
			}
		}
		
		int copy = lowestPowerIndex;
		paintSeam(im, copy, backTracker);
		
		
		//Shift seam over - Dynamic seam
		for(int i = totalPower[0].length-1; i>= 0; i--) {
			shiftSeam(im, lowestPowerIndex, i);
			int shift = backTracker[lowestPowerIndex][i];
			lowestPowerIndex = lowestPowerIndex + shift;
		}
	}
	
	private void shiftSeam(UWECImage im, int x, int y) {
		while(x < im.getWidth() - 1) {
			im.setRGB(x, y, im.getRed(x+1, y), im.getGreen(x+1, y), im.getBlue(x+1, y));
			x++;
		}
	}
	
	private static void paintSeam(UWECImage im, int lowestPowerIndex, int[][] backTracker) {
		//Alter the image so the seam stands out
		for(int i=backTracker[0].length-1; i>=0; i--) {
			im.setRGB(lowestPowerIndex, i, 255, 20, 147);
			int shift = backTracker[lowestPowerIndex][i];
			lowestPowerIndex = lowestPowerIndex + shift;
		}
		im.repaintCurrentDisplayWindow();
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
