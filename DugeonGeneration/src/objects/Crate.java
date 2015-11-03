package objects;

import core.Mathf;
import greenfoot.GreenfootImage;
import greenfoot.World;
import AI.IDamageable;
import scrollWorld.ScrollActor;

public class Crate extends ScrollActor implements IDamageable{

	private int maxHealth = 100;
	private int health;
	
	private GreenfootImage imgIdle;
	private GreenfootImage imgBroken;
	
	public enum State {alive, dead};
	public State state = State.alive;
		
	public Crate (int maxHealth) {
		
		health = maxHealth;
		this.maxHealth = maxHealth;
		
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
	
	public void destroy() {
		
		setImage(imgBroken);
		state = State.dead;
		
	}
	
}
