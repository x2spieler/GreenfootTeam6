package objects;

import java.util.Random;

import greenfoot.GreenfootImage;
import greenfoot.World;
import player.BuffType;
import player.Player;
import scrollWorld.ScrollActor;
import world.DungeonMap;

public class Item extends ScrollActor {

	protected int durationInMS = 0;
	protected BuffType buff = null;
	protected double param;
	protected GreenfootImage img = null;
	protected Player player;
	
	public Item (DungeonMap dm, BuffType buff) {
		player = (Player)dm.getPlayer();
		this.buff = buff;
		calculateStats();
	}
	
	private void calculateStats()
	{
		Random r=new Random();
		durationInMS=5000+r.nextInt(5001);
		switch(buff)
		{
		case SPEED_MULTIPLIER:
		case MELEE_DAMAGE:
			param=1.0+r.nextDouble();
			break;
		case MAX_HP:
			param=10+r.nextInt(40);
			break;
		case RELOAD_TIME:
		case WEAPON_SPEED:
			param=1.0-r.nextDouble()/2;		//0.5 - 1.0
			break;
		default:
			throw new IllegalStateException("Seems like somebody forgot to add a BuffType to this switch-statement! "+this.getClass().toString());
		}
	}
	
	@Override
	public void addedToWorld(World world) 
	{
		setImage(new GreenfootImage("objects/pickup.png"));
	}
	
	@Override
	public void act() {
		
		if(intersects(player)) {
			if(player.addBuff(buff, param, durationInMS))
			{
				collect();
			}
		}
	}
	
	
	public void collect() {
		getWorld().removeObject(this);		
	}
}
