package world.mapping;

import greenfoot.GreenfootImage;

public class HouseTile implements ITile<HouseID> {

	private final HouseID id;
	private GreenfootImage image;
	private GreenfootImage special;

	public HouseTile(HouseID id, GreenfootImage image) {
		this.id = id;
		this.image = image;
	}

	@Override
	public HouseID getID() {
		return id;
	}

	@Override
	public GreenfootImage getTileImage() {
		return image;
	}

	@Override
	public void setSpecial(GreenfootImage special) {
		this.special = special;
	}

	@Override
	public GreenfootImage getSpecial() {
		return special;
	}

}
