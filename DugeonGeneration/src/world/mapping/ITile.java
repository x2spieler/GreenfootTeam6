package world.mapping;

import greenfoot.GreenfootImage;

public interface ITile<E extends Enum<? extends ID>> {
	public E getID();

	public GreenfootImage getTileImage();

	public void setSpecial(GreenfootImage special);

	public GreenfootImage getSpecial();
}
