package objects;

import greenfoot.GreenfootImage;
import greenfoot.World;
import player.Buff;
import player.Player;
import scrollWorld.ScrollActor;
import world.DungeonMap;

public abstract class Item extends ScrollActor {

	protected int durationInMS = 0;
	protected Buff buff = null;
	protected double[] param;
	protected GreenfootImage img = null;
	protected Player player;
	
	public Item (DungeonMap dm, Buff buff, int durationInMS) {
		player = (Player)dm.getPlayer();
		this.buff = buff;
		this.durationInMS = durationInMS;
	}
	
	@Override
	public void addedToWorld(World world) {
		
		setImage(img);
		
	}
	
	@Override
	public void act() {
		
		if(intersects(player)) {
			
			player.addBuff(buff, param, durationInMS);
			collect();
		}
	}
	
	
	public void collect() {
		
		getWorld().removeObject(this);
				
	}
}
