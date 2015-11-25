package world.mapping;

import greenfoot.GreenfootImage;

public class MyTile implements ITile {

	private final String id;
	private GreenfootImage image;

	public MyTile(String id, GreenfootImage image) {
		this.id = id;
		this.image = image;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public GreenfootImage getTileImage() {
		return image;
	}

}
