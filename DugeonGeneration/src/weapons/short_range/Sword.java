package weapons.short_range;

import java.awt.geom.Point2D;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;
import world.DungeonMap;

public class Sword extends ShortRangeWeapon {
	public Sword(ScrollActor owner) {
		damage = 2000;
		weaponName = "sword";
		displayName = "Sword";
		this.owner = owner;
		reloadTimeInMS = 750;
		additionalValue = 1;
		ticksPerAnimImg = 3;
		anim_frame_count = 6;
		weaponOffsetToPlayer = new Point2D.Double(DungeonMap.TILE_SIZE, DungeonMap.TILE_SIZE / 3);
		default_anim_frame = 5;
	}

	@Override
	protected int getAnimFrameCount() {
		return 6;
	}
}
