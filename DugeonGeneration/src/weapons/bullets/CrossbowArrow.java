package weapons.bullets;

import weapons.EntityType;
import weapons.abstracts.Bullet;

public class CrossbowArrow extends Bullet {
	public CrossbowArrow(EntityType typeToIgnore) {
		super(typeToIgnore);
		damage = 4;
		lifetimeInMs = 4000;
		bulletName = "crossbow_arrow";
		setSpeed(800);
		maxHits = 3;
	}
}
