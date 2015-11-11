package weapons.abstracts;

import java.util.ArrayList;
import java.util.List;

import AI.Enemy;
import AI.IDamageable;
import DungeonGeneration.FieldType;
import DungeonGeneration.MapField;
import player.Player;
import weapons.EntityType;
import world.DungeonMap;

public abstract class ShortRangeWeapon extends Weapon
{
	private ArrayList<IDamageable> hitEntities;
	
	public ShortRangeWeapon()
	{
		hitEntities=new ArrayList<IDamageable>();
		isLongRangeWeapon=false;
	}

	@Override
	public void act()
	{
		super.act();
		if(playAnimation)
		{
			dealDamage();
		}
		if(!playAnimation&&hitEntities.size()!=0)
			hitEntities.clear();
	}

	/**
	 * @return Returns true if we hit a player/enemy
	 */
	public void dealDamage()
	{
		MapField mf=wi.getMap()[getGlobalX()/wi.getTileSize()][getGlobalY()/wi.getTileSize()];
		if(!mf.walkable()&&mf.getFieldType()==FieldType.WALL)
		{
			//Should prevent damaging enemies through wall
			//Second part allows damaging crates though 
			return;
		}
		List<?> intersectingObjects = getObjectsInRange(DungeonMap.TILE_SIZE+DungeonMap.TILE_SIZE/2, null);
		if (intersectingObjects.size() != 0)
		{
			for (Object o : intersectingObjects)
			{
				if(!(o instanceof IDamageable))
					continue;
				
				if((typeToIgnore==EntityType.ENEMY&&(o instanceof Enemy))
						||(typeToIgnore==EntityType.PLAYER&&(o instanceof Player)))
					continue;
				
				IDamageable id=(IDamageable) o;
				if(hitEntities.contains(id))
					continue;
				
				id.damage(damage);
				hitEntities.add(id);
				return;
			}
		}
	}

	@Override
	public boolean triggerUse()
	{
		//Short range weapons don't have ammo, we always trigger the use
		return true;
	}

}
