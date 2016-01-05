package weapons.short_range;

import java.awt.geom.Point2D;

import greenfoot.GreenfootImage;
import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;
import world.DungeonMap;

public class Sword extends ShortRangeWeapon {
	private static final int FRAME_COUNT = 12;

	public Sword(ScrollActor owner) {
		damage = 8;
		weaponName = "sword";
		displayName = "Sword";
		this.owner = owner;
		reloadTimeInMS = 750;
		additionalValue = 1;
		ticksPerAnimImg = 1;
		anim_frame_count = FRAME_COUNT;
		weaponOffsetToPlayer = new Point2D.Double(DungeonMap.TILE_SIZE, DungeonMap.TILE_SIZE / 3);
		default_anim_frame = FRAME_COUNT - 1;
		icon = new GreenfootImage("enemies/weapons/sword/sword.png");
	}

	@Override
	protected int getAnimFrameCount() {
		return FRAME_COUNT;
	}
}
