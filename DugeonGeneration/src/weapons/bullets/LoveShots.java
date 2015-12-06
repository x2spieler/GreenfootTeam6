package weapons.bullets;

import weapons.EntityType;
import weapons.abstracts.Bullet;

public class LoveShots extends Bullet{

	public LoveShots(EntityType typeToIgnore) {
		super(typeToIgnore);
		damage=4;
		lifetimeInMs=4000;
		bulletName="love_shots";
		setSpeed(200);
	}

}
