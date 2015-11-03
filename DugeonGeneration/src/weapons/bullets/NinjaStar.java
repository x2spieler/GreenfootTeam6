package weapons.bullets;

import weapons.EntityType;
import weapons.abstracts.Bullet;

public class NinjaStar extends Bullet{

	public NinjaStar(EntityType typeToIgnore) {
		super(typeToIgnore);
		damage=4;
		lifetimeInMs=4000;
		bulletName="ninja_star";
		setSpeed(200);
	}

}
