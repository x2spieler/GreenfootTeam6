package AI;

import java.awt.Point;

import DungeonGeneration.MapField;
import weapons.abstracts.Bullet;

public interface IWorldInterfaceForAI
{
	public void enemyDied(Enemy e);
	
	public void addBulletToWorld(Bullet bullet);
	
	public IDamageable getPlayer();
	
	public MapField[][] getMap();
	
	/**
	 * @return Player position in pixels, NOT IN TILES
	 */
	public Point getPlayerPosition();
	
	public int getTileSize();
	
	public double getFPS();
	
	public int getSeed();
}


