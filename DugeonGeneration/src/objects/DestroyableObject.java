package objects;

import java.awt.Point;
import java.util.HashMap;
import java.util.Random;

import AI.IDamageable;
import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.FieldType;
import core.Mathf;
import greenfoot.GreenfootImage;
import player.BuffType;
import scrollWorld.ScrollActor;
import world.DungeonMap;

public abstract class DestroyableObject extends ScrollActor implements IDamageable{

	private int maxHealth = 100;
	private int health;
	private GreenfootImage imgIdle;
	private GreenfootImage imgBroken;
	
	public enum State {alive, dead};
	private State state = State.alive;
	
	public Point mapCoords = new Point(0,0); 
	
	private DungeonGenerator dgen;
	
	protected HashMap<BuffType, Integer> dropChances;
			
	public DestroyableObject (int maxHealth, Point mapCoords, DungeonGenerator dgen) {
		health = maxHealth;
		this.maxHealth = maxHealth;
		this.mapCoords = mapCoords;
		this.dgen = dgen;
	}	
	
	@Override
	public void damage(int dmg) {
		if(state == State.dead) {
			return;
		}
		
		health = Mathf.clamp(health - dmg, 0, maxHealth);
		
		if(health <= 0) {
			destroy();
		}
	}

	@Override
	public int getHP() {
		return health;
	}
	
	public int getMaxHP() {
		return maxHealth;
	}
	
	protected void loadImage(String name) {
		imgIdle = new GreenfootImage("objects/"+name+".png");
		imgBroken = new GreenfootImage("objects/"+name+"_broken.png");
		setImage(imgIdle);
		
	}
	
	public Point getMapCoords () {
		return mapCoords;
	}
	
	public void destroy() {
		
		setImage(imgBroken);
		state = State.dead;
		
		spawnItem();
				
		dgen.setMapFieldsType(mapCoords.x, mapCoords.y, FieldType.GROUND);
	}
	
	private void spawnItem() {
		Random r = new Random();
		int rand=r.nextInt(100);
			
		int count=0;
		for(BuffType bt:dropChances.keySet())
		{
			count+=dropChances.get(bt);
			if(rand<count)
			{
				DungeonMap dm = (DungeonMap)getWorld();
				Item item = new Item(dm, bt);
				int xAdd=1;
				if(r.nextBoolean())
					xAdd=-1;
				int yAdd=1;
				if(r.nextBoolean())
					yAdd=-1;
				dm.addObject(item, (mapCoords.x+xAdd) * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE/2, (mapCoords.y+yAdd) * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE/2);
				return;
			}
		}
	}
	
}
