package objects;

import java.util.List;

import greenfoot.GreenfootImage;
import player.DungeonMover;
import player.Player;
import world.DungeonMap;

public class StairsToHeaven /*well, at least to the next floor!*/ extends DungeonMover
{	
	public StairsToHeaven()
	{
		setImage(new GreenfootImage("objects/stairsToHeaven.png"));
	}
	
	public void act()
	{
		super.act();
		List<?> intersectingObjects = getIntersectingObjects(Player.class);
		if(intersectingObjects.size()!=0)
		{
			((DungeonMap)getWorld()).endRound();
		}
	}
	
}
