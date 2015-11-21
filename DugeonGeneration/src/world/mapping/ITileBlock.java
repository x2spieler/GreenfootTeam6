package world.mapping;

import greenfoot.GreenfootImage;

public interface ITileBlock {
	public boolean transcribe(int x, int y, GreenfootImage[][] map);

	public GreenfootImage getImageAt(int x, int y);
}
