package world.mapping;

import java.awt.Point;
import java.util.Map;

import greenfoot.GreenfootImage;

public interface ITileBlock {
	public boolean transcribe(int x, int y, GreenfootImage[][] map);

	public GreenfootImage getImageAt(int x, int y);
}
