package world;

import greenfoot.GreenfootImage;
import greenfoot.World;
import scrollWorld.ScrollActor;

public class MapElement extends ScrollActor {

	public MapElement(GreenfootImage image) {
		setImage(image);
	}

	@Override
	protected void addedToWorld(World world) {
		super.addedToWorld(world);
		world.setPaintOrder(this.getClass());
	}

	@Override
	public void move(int distance) {
	}

	@Override
	public void turnTowardsGlobalLocation(int x, int y) {
	}

	@Override
	public void turnTowardsCameraLocation(int x, int y) {
	}

	@Override
	public void setRotation(int rotation) {
	}

	@Override
	public void turnTowards(int x, int y) {
	}

	@Override
	public void turn(int amount) {
	}
}
