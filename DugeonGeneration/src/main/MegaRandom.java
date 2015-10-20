package main;

import java.util.Random;

public class MegaRandom {

	public static int randomInt (int min, int max) {
		
		Random rn = new Random();
		int randomInteger = rn.nextInt(max - min + 1) + min;
		
		return randomInteger;
	}
	
}
