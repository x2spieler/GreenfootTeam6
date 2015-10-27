package DungeonGeneration;

import java.util.Random;

public class MegaRandom {

	//returns a random Integer between min and max
	public static int randomInt (int min, int max) {
		
		Random rn = new Random();
		int randomInteger = rn.nextInt(max - min + 1) + min;
		
		return randomInteger;
	}
	
}
