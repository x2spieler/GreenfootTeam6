package DungeonGeneration;

import java.util.Random;

public class MegaRandom {

	private Random r;
	
	public MegaRandom()
	{
		r=new Random();
	}
	
	public MegaRandom(int seed)
	{
		r=new Random();
		r.setSeed(seed);
	}
	
	public void setSeed(int seed)
	{
		r.setSeed(seed);
	}
	
	//returns a random Integer between min and max
	public int randomInt (int min, int max) {
		int randomInteger = r.nextInt(max - min + 1) + min;
		
		return randomInteger;
	}
	
}
