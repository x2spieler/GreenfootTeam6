package objects;

import java.awt.Point;
import java.util.HashMap;

import AI.IDamageable;
import DungeonGeneration.DungeonGenerator;
import player.BuffType;

public class Grave extends DestroyableObject implements IDamageable
{
	public Grave (int maxHealth, Point mapCoords, DungeonGenerator dgen) {
		super(maxHealth, mapCoords, dgen);
		dropChances=new HashMap<BuffType, Integer>();
		dropChances.put(BuffType.MAX_HP, 10);
		dropChances.put(BuffType.MELEE_DAMAGE, 10);
		dropChances.put(BuffType.RELOAD_TIME, 10);
		dropChances.put(BuffType.SPEED_MULTIPLIER, 10);
		dropChances.put(BuffType.WEAPON_SPEED, 10);
		loadImage("grave");
	}	
}
