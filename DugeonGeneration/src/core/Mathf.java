package core;

public class Mathf {

	public static int clamp (int theValue, int min, int max) {
		
		if(theValue < min) {
			return min;
		}
		
		else if(theValue > max) {
			return max;
		}
		
		return theValue;
		
	}
	
	public static double clamp (double theValue, double min, double max) {
		
		if(theValue < min) {
			return min;
		}
		
		else if(theValue > max) {
			return max;
		}
		
		return theValue;
		
	}
		
}
