package AI;

import java.awt.Point;

import DungeonGeneration.MapField;

public interface IWorldInterfaceForAI
{
	public void addPlayerScore(int score);
	
	public void addBulletToWorld(Bullet bullet);
	
	public IDamageable getPlayer();
	
	public MapField[][] getMap();
	
	/**
	 * @return Player position in pixels, NOT IN TILES
	 */
	public Point getPlayerPosition();
	
	public int getTileSize();
}


