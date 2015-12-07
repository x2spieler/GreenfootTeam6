package weapons.bullets;

import weapons.EntityType;
import weapons.abstracts.Bullet;

public class PurpleSpell extends Bullet{

	public PurpleSpell(EntityType typeToIgnore) {
		super(typeToIgnore);
		damage=4;
		lifetimeInMs=4000;
		bulletName="spell_purple";
		setSpeed(200);
	}

}
