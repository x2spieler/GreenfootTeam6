package objects;

import greenfoot.GreenfootImage;
import greenfoot.World;
import player.BuffType;
import world.DungeonMap;

public class SpeedItem extends Item {

	public static final double MULTIPLIER = 1.8;
	public static final int DURATION_IN_MS = 5000;
	
	public static final BuffType BUFF = BuffType.SPEED_MULTIPLIER;
	
	protected GreenfootImage img = new GreenfootImage("item_speed1.png");
	
	
	public SpeedItem (DungeonMap dm) {
		
		super(dm, BUFF, DURATION_IN_MS);
		param = MULTIPLIER;
		
	}
	
	@Override
	public void addedToWorld(World world) {
		
		setImage(img);
		
	}
	
	
}
