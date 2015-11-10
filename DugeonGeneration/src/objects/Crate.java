package objects;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import core.Mathf;
import greenfoot.GreenfootImage;
import greenfoot.World;
import greenfoot.core.WorldInvokeListener;
import AI.IDamageable;
import AI.IWorldInterfaceForAI;
import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.FieldType;
import scrollWorld.ScrollActor;
import world.DungeonMap;

public class Crate extends ScrollActor implements IDamageable{

	private int maxHealth = 100;
	private int health;
	
	private GreenfootImage imgIdle;
	private GreenfootImage imgBroken;
	
	public enum State {alive, dead};
	public State state = State.alive;
	
	public Point mapCoords = new Point(0,0); 
	
	private DungeonGenerator dgen;
	
	
	
	private final double DropChanceSpeedItem = 1/3.0;
	private final double DropChanceMaxHealthItem = 1/5.0;
			
	public Crate (int maxHealth, Point mapCoords, DungeonGenerator dgen) {
		
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
		
		//I'm fucked
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
	
	private void loadImage() {
		
		imgIdle = new GreenfootImage("crate1.png");
		imgBroken = new GreenfootImage("crate1Broken1.png");
		
	}
	
	@Override
	public void addedToWorld(World world) {
				
		loadImage();
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
		double randomValue = 0.0 + (1.0 - 0.0) * r.nextDouble();
				
		if(randomValue <= DropChanceSpeedItem) {
			DungeonMap dm = (DungeonMap)getWorld();
			Item item = new SpeedItem(dm);
			dm.addObject(item, mapCoords.x * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE/2, mapCoords.y * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE/2);
		}
		
	}
	
}
