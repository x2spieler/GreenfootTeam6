package objects;

import player.BuffType;
import world.DungeonMap;
import greenfoot.GreenfootImage;
import greenfoot.World;

public class MaxHealthItem extends Item {

	public static final int EXTRA_HEALTH_POINTS = 10;
	public static final int PERMANENT = -1;
	
	public static final BuffType BUFF = BuffType.MAX_HP;
	
	protected GreenfootImage img = new GreenfootImage("item_maxHp1.png");
	
	
	public MaxHealthItem (DungeonMap dm) {
		
		super(dm, BUFF, PERMANENT);
		param = new double[1];
		param[0] = EXTRA_HEALTH_POINTS;
		
	}
	
	@Override
	public void addedToWorld(World world) {
		
		setImage(img);
		
	}
	
}
