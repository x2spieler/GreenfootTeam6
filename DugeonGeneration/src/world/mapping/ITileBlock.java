package world.mapping;

import world.MapElement;
import greenfoot.GreenfootImage;

public interface ITileBlock {
	public boolean transcribe(int x, int y, GreenfootImage[][] map, MapElement[][] specialTiles);

	public GreenfootImage getImageAt(int x, int y);

	public GreenfootImage getSpecial(int i, int j);
}
