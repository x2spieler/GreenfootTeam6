package weapons.bullets;

import weapons.EntityType;
import weapons.abstracts.Bullet;

public class PurpleSpell extends Bullet{

	public PurpleSpell(EntityType typeToIgnore) {
		super(typeToIgnore);
		damage=15;
		lifetimeInMs=3500;
		bulletName="spell_purple";
		setSpeed(200);
	}

}
